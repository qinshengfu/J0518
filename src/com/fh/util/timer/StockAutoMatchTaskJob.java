package com.fh.util.timer;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.text.StrSpliter;
import cn.hutool.core.util.NumberUtil;
import com.fh.controller.front.BaseFrontController;

import com.fh.entity.Page;
import com.fh.entity.system.User;
import com.fh.service.front.AccountManager;
import com.fh.service.front.Sys_configManager;
import com.fh.service.record.Money_recordManager;
import com.fh.service.record.Points_recordManager;
import com.fh.service.share.Shares_orderManager;
import com.fh.util.PageData;

import com.fh.util.QuartzManager;
import org.apache.commons.lang.text.StrSubstitutor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;


import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;


/**
 * 说明：行情价格变动定时任务
 * 创建人：Ajie
 * 创建时间：2020年6月4日15:51:57
 */
public class StockAutoMatchTaskJob extends BaseFrontController implements Job {


    /**
     * 功能描述：入口，基础条件判断
     *
     * @author Ajie
     * @date 2020/6/10 0010
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        // 普通类从spring容器中拿出service
        WebApplicationContext webctx = ContextLoader.getCurrentWebApplicationContext();
        // 系统参数
        Sys_configManager sys_configService = (Sys_configManager) webctx.getBean("sys_configService");
        PageData pd = new PageData();
        String type = "买入";
        pd.put("TYPE", type);
        PageData param = null;
        try {
            // 获取系统参数
            param = sys_configService.findById(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (param == null) {
            return;
        }
        // 每周交易范围
        List<String> weeklyTradingHours = StrSpliter.split(param.getString("WEEKLY_TRADING_HOURS"), '-', 0, true, true);
        // 每天上午交易时间范围
        List<String> morningTradingHours = StrSpliter.split(param.getString("MORNING_TRADING_HOURS"), '-', 0, true, true);
        // 每天下午交易时间范围
        List<String> afternoonTradingTime = StrSpliter.split(param.getString("AFTERNOON_TRADING_TIME"), '-', 0, true, true);
        // 获得今天是星期几，1表示周日，2表示周一
        int week = DateUtil.thisDayOfWeek();
        int weekStart = Convert.toInt(weeklyTradingHours.get(0));
        int weekEnd = Convert.toInt(weeklyTradingHours.get(1));
        // 周范围检查
        if (weekStart > week && weekEnd < week) {
            return;
        }
        // 是否在上午交易时间范围内
        String amStartTime = morningTradingHours.get(0);
        String amEndTime = morningTradingHours.get(1);
        boolean isInMorn = com.fh.util.DateUtil.isBelongTime(amStartTime, amEndTime);
        // 是否在下午交易时间范围内
        String pmStartTime = afternoonTradingTime.get(0);
        String pmEndTime = afternoonTradingTime.get(1);
        boolean isInPm = com.fh.util.DateUtil.isBelongTime(pmStartTime, pmEndTime);
        // 不在上午交易时间与下午交易时间就返回
        if (!isInMorn && !isInPm) {
            return;
        }
        // 每次执行后就修改此定时任务间隔，然后通过“开始匹配模块”控制是否继续匹配
        QuartzManager.modifyJobTime("stockAutoMatchTaskJob", "0 0 0/1 * * ? ");
        // 定义隔10秒后在执行一次的Cron表达式
        String matchTime = "0/10 * * * * ? *";
        // 执行匹配
        boolean isContinue = false;
        int i = 1;
        int count = 100;
        while (!isContinue) {
            logger.info(DateUtil.now() + "-------- 第" + i + "次执行自动匹配模块，当前类型为：" + pd.getString("TYPE"));
            try {
                isContinue = startMatch(webctx, param, pd);
            } catch (Exception e) {
                // 出错后隔10秒钟后在执行
                QuartzManager.modifyJobTime("stockAutoMatchTaskJob", matchTime);
                e.printStackTrace();
            }
            // 更换查询类型
            if (type.equals(pd.getString("TYPE"))) {
                pd.put("TYPE", "卖出");
            } else {
                pd.put("TYPE", type);
            }
            // 超过100次就休息10秒
            if (i >= count) {
                QuartzManager.modifyJobTime("stockAutoMatchTaskJob", matchTime);
                return;
            }
            i++;
        }
        logger.info("--------------匹配成功，隔10秒后在执行");
        QuartzManager.modifyJobTime("stockAutoMatchTaskJob", matchTime);
    }

    /**
     * 功能描述：开始匹配
     *
     * @param pd key:TYPE value:“买入”/“卖出”
     * @return true 休息10秒后继续执行，false 更改类型：把“买入”换成“卖出”继续执行
     * @author Ajie
     * @date 2020年6月9日18:26:22
     */
    public boolean startMatch(WebApplicationContext webctx, PageData param, PageData pd) throws Exception {
        // 股票订单表
        Shares_orderManager shares_orderService = (Shares_orderManager) webctx.getBean("shares_orderService");
        // 各查询一条买入/卖出订单，且用户不一致，且买单>=卖单的最低价
        List<PageData> list = shares_orderService.listBuyAndSell(pd);
        // 没匹配成功 返回
        if ((list != null ? list.size() : 0) < 2) {
            return false;
        }
        // 判断类型
        boolean isSame = list.get(0).getString("TYPE").equals(list.get(1).getString("TYPE"));
        if (isSame) {
            return false;
        }
        if ("买入".equals(list.get(0).getString("TYPE"))) {
            // 执行交易
            startTrade(webctx, list.get(0), list.get(1), param);
        }
        if ("卖出".equals(list.get(0).getString("TYPE"))) {
            // 执行交易
            startTrade(webctx, list.get(1), list.get(0), param);
        }
        return true;
    }

    /**
     * 功能描述：开始交易
     *
     * @param webctx spring上下文
     * @param buy    买入订单
     * @param sell   卖出订单
     * @param param  系统参数
     * @author Ajie
     * @date 2020/6/10 0010
     */
    @Transactional(rollbackFor = Exception.class)
    public void startTrade(WebApplicationContext webctx, PageData buy, PageData sell, PageData param) throws Exception {

        // 股票订单表
        Shares_orderManager shares_orderService = (Shares_orderManager) webctx.getBean("shares_orderService");
        // 用户表
        AccountManager accountService = (AccountManager) webctx.getBean("accountService");

        PageData pd = new PageData();
        // 后台设置的卖出手续费比例、买入单锁定天数
        double transactionFee = NumberUtil.div(param.get("TRANSACTION_FEE").toString(), "100").doubleValue();
        int lockedDay = Convert.toInt(param.get("LOCKED_DAY"));

        // 获取订单价格
        double buyPrice = Convert.toDouble(buy.get("ORI_PRICE"));
//            double sellPrice = Convert.toDouble(sell.get("ORI_PRICE"));
        // 获取订单库存
        int buyRemainder = Convert.toInt(buy.get("REMAINDER"));
        int sellRemainder = Convert.toInt(sell.get("REMAINDER"));
        // 查询用户的信息
        PageData buyUser = accountService.findByPhone(buy);
        PageData sellUser = accountService.findByPhone(sell);
        // 交易数量
        int tradeNum = buyRemainder;
        // 判断那笔订单完成交易了
        if (buyRemainder > sellRemainder) {
            tradeNum = buyRemainder - sellRemainder;
            // 添加买单对方手机号
            pd.put("SHARES_ORDER_ID", buy.get("SHARES_ORDER_ID"));
            pd.put("HE_PHONE", sellUser.get("PHONE") + "," + buy.get("HE_PHONE"));
            shares_orderService.edit(pd);
            // 执行卖单完成操作
            pd.put("STATUS", "卖出成功");
            pd.put("EVENT", "交易成功");
            pd.put("SHARES_ORDER_ID", sell.get("SHARES_ORDER_ID"));
            pd.put("HE_PHONE", buyUser.get("PHONE") + "," + sell.get("PHONE"));
            shares_orderService.edit(pd);
        }
        if (buyRemainder == sellRemainder) {
            // 执行买卖订单完成操作
            pd.put("GMT_MODIFIED", DateUtil.offsetDay(new Date(), lockedDay).toString());
            pd.put("SHARES_ORDER_ID", buy.get("SHARES_ORDER_ID"));
            pd.put("STATUS", "持仓");
            pd.put("EVENT", "交易成功");
            pd.put("HE_PHONE", sellUser.get("PHONE"));
            shares_orderService.edit(pd);
            // 添加卖单中的对方手机号
            pd.put("STATUS", "卖出成功");
            pd.put("SHARES_ORDER_ID", sell.get("SHARES_ORDER_ID"));
            pd.put("HE_PHONE", buyUser.get("PHONE") + "," + sell.get("HE_PHONE"));
            shares_orderService.edit(pd);
        }
        if (buyRemainder < sellRemainder) {
            // 执行买单完成操作
            pd.put("GMT_MODIFIED", DateUtil.offsetDay(new Date(), lockedDay).toString());
            pd.put("SHARES_ORDER_ID", buy.get("SHARES_ORDER_ID"));
            pd.put("STATUS", "持仓");
            pd.put("EVENT", "交易成功");
            pd.put("HE_PHONE", sellUser.get("PHONE") + "," + buy.get("HE_PHONE"));
            shares_orderService.edit(pd);
        }
        // 扣除卖单对应库存
        pd.put("SHARES_ORDER_ID", sell.get("SHARES_ORDER_ID"));
        pd.put("num", tradeNum);
        shares_orderService.updateOrderStock(pd, false);
        // 计算总金额、手续费
        double totalAmount = NumberUtil.mul(tradeNum, buyPrice);
        double charge = NumberUtil.mul(totalAmount, transactionFee);
        double actualReceipt = NumberUtil.sub(totalAmount, charge);
        pd.put("ACCOUNT_ID", sellUser.get("ACCOUNT_ID"));
        pd.put("money", actualReceipt);
        // 增加余额、创建钱包记录
        accountService.updateMoney(pd, true);
        addMoneyRec(webctx, sellUser.get("PHONE"), sellUser.getString("ACCOUNT_ID"), totalAmount, "已完成"
                , charge, actualReceipt, buyUser.get("PHONE"), buyUser.getString("ACCOUNT_ID"), '+', "卖出股票");
        // 结算商城积分和奖金收益
        settlementMallPoints(webctx, sell, totalAmount, tradeNum, param, sellUser.get("ACCOUNT_ID").toString());
        settlementBonusIncome(webctx, sell, totalAmount, param, sellUser.get("ACCOUNT_ID").toString());


    }


    /**
     * 功能描述：结算商城积分
     *
     * @param webctx          Spring上下文
     * @param sell            卖出订单
     * @param sellTotalAmount 卖出总金额
     * @param param           系统参数
     * @param sellNumber      卖出数量
     * @param userId          卖出订单的用户ID
     * @author Ajie
     * @date 2020/6/11 0011
     */
    public void settlementMallPoints(WebApplicationContext webctx, PageData sell, double sellTotalAmount
            , int sellNumber, PageData param, String userId) throws Exception {
        // 股票订单表
        Shares_orderManager shares_orderService = (Shares_orderManager) webctx.getBean("shares_orderService");
        // 用户表
        AccountManager accountService = (AccountManager) webctx.getBean("accountService");
        // 商城积分表
        Points_recordManager points_recordService = (Points_recordManager) webctx.getBean("points_recordService");
        PageData pd = new PageData();
        // 根据卖出订单中的主订单ID查询
        pd.put("SHARES_ORDER_ID", sell.get("MAIN_ORDER_ID"));
        PageData mainOrder = shares_orderService.findById(pd);
        // 主订单的买入价（本金）
        double initialPrice = Convert.toDouble(mainOrder.get("ORI_PRICE"));
        double initialTotalAmount = NumberUtil.div(initialPrice, sellNumber);
        // 商城积分比例
        double mallPoints = Convert.toDouble(param.get("MALL_POINTS"));
        mallPoints = NumberUtil.div(mallPoints, 100);
        // （卖出总额-初始总额）* 积分比例
        double profit = NumberUtil.mul(NumberUtil.sub(sellTotalAmount, initialTotalAmount), mallPoints);
        if (profit <= 0) {
            return;
        }
        // 增加用户商城积分
        pd.put("ACCOUNT_ID", userId);
        pd.put("money", profit);
        accountService.updateIntegral(pd, true);
        // 创建商城积分钱包记录
        pd.put("PHONE", sell.get("PHONE"));
        pd.put("USER_ID", userId);
        pd.put("MONEY", profit);
        pd.put("STATUS", "已完成");
        pd.put("CHARGE", "0");
        pd.put("ACTUAL_RECEIPT", profit);
        pd.put("HE_PHONE", "");
        pd.put("HE_USER_ID", "");
        pd.put("TAG", "+");
        points_recordService.save(pd);
    }

    /**
     * 功能描述：结算奖金收益
     *
     * @param webctx          Spring上下文
     * @param sell            卖出订单
     * @param sellTotalAmount 卖出总额
     * @param param           系统参数
     * @param userId          卖出订单的用户ID
     * @author Ajie
     * @date 2020/6/11 0011
     */
    public void settlementBonusIncome(WebApplicationContext webctx, PageData sell, double sellTotalAmount, PageData param, String userId) throws Exception {
        // 如果是顶点用户直接返回
        if ("10000".equals(userId)) {
            return;
        }
        // 用户表
        AccountManager accountService = (AccountManager) webctx.getBean("accountService");

        PageData pd = new PageData();

        // 直推奖、市级代理、省级代理收益比例
        double generalMembers = NumberUtil.div(param.get("GENERAL_MEMBERS").toString(), "100").doubleValue();
        double municipalAgent = NumberUtil.div(param.get("MUNICIPAL_AGENT").toString(), "100").doubleValue();
        double provincialAgent = NumberUtil.div(param.get("PROVINCIAL_AGENT").toString(), "100").doubleValue();
        // 查询卖出用户信息
        pd.put("ACCOUNT_ID", userId);
        PageData user = accountService.findById(pd);
        // 定义已发放奖金等级 1：普通、2：市级代理、3：省级代理
        int rank = checkRank(user.getString("USER_RANK"));
        double bonus = 0;
        // 查询团队所有上级列表
        List<PageData> leaderList = accountService.listByRePath(user);
        for (PageData map : leaderList) {
            int leadreRank = checkRank(map.getString("USER_RANK"));
            // 先发放直推奖
            if (bonus == 0) {
                bonus = NumberUtil.mul(sellTotalAmount, generalMembers);
                // 增加用户金额
                pd.put("ACCOUNT_ID", map.get("ACCOUNT_ID"));
                pd.put("money", bonus);
                accountService.updateMoney(pd, true);
                // 新增钱包记录
                addMoneyRec(webctx, bonus, map.get("ACCOUNT_ID").toString(), map.get("PHONE").toString(), userId, user.get("PHONE").toString(), "直推奖");
                // 判断等级,已经发到省级代理直接跳出循环
                if (rank == 3) {
                    break;
                }
                if (rank <= leadreRank) {
                    // 记录已发放的等级
                    rank = leadreRank;
                    if (leadreRank == 2) {
                        // 发放市级代理奖
                        bonus = NumberUtil.mul(sellTotalAmount, municipalAgent);
                        // 增加用户金额
                        pd.put("ACCOUNT_ID", map.get("ACCOUNT_ID"));
                        pd.put("money", bonus);
                        accountService.updateMoney(pd, true);
                        // 新增钱包记录
                        addMoneyRec(webctx, bonus, map.get("ACCOUNT_ID").toString(), map.get("PHONE").toString(), userId, user.get("PHONE").toString(), "市级代理奖");
                    }
                    if (leadreRank == 3) {
                        // 发放省级代理奖
                        bonus = NumberUtil.mul(sellTotalAmount, provincialAgent);
                        // 增加用户金额
                        pd.put("ACCOUNT_ID", map.get("ACCOUNT_ID"));
                        pd.put("money", bonus);
                        accountService.updateMoney(pd, true);
                        // 新增钱包记录
                        addMoneyRec(webctx, bonus, map.get("ACCOUNT_ID").toString(), map.get("PHONE").toString(), userId, user.get("PHONE").toString(), "省级代理奖");
                    }
                }
            } else {
                // 发放代理奖
                // 判断等级,已经发到省级代理直接跳出循环
                if (rank == 3) {
                    break;
                }
                if (leadreRank > rank) {
                    // 记录已发放的等级
                    rank = leadreRank;
                    if (leadreRank == 2) {
                        // 发放市级代理奖
                        bonus = NumberUtil.mul(sellTotalAmount, municipalAgent);
                        // 增加用户金额
                        pd.put("ACCOUNT_ID", map.get("ACCOUNT_ID"));
                        pd.put("money", bonus);
                        accountService.updateMoney(pd, true);
                        // 新增钱包记录
                        addMoneyRec(webctx, bonus, map.get("ACCOUNT_ID").toString(), map.get("PHONE").toString(), userId, user.get("PHONE").toString(), "市级代理奖");
                    }
                    if (leadreRank == 3) {
                        // 发放省级代理奖
                        bonus = NumberUtil.mul(sellTotalAmount, provincialAgent);
                        // 增加用户金额
                        pd.put("ACCOUNT_ID", map.get("ACCOUNT_ID"));
                        pd.put("money", bonus);
                        accountService.updateMoney(pd, true);
                        // 新增钱包记录
                        addMoneyRec(webctx, bonus, map.get("ACCOUNT_ID").toString(), map.get("PHONE").toString(), userId, user.get("PHONE").toString(), "省级代理奖");
                    }
                }
            }
        }
    }

    /**
     * 功能描述：检查用户等级对应的数字，1：普通、2：市级代理、3：省级代理
     *
     * @param userRank 用户等级
     * @return -1 不存在
     * @author Ajie
     * @date 2020/6/11 0011
     */
    public int checkRank(String userRank) {
        if ("普通会员".equals(userRank)) {
            return 1;
        }
        if ("市级代理".equals(userRank)) {
            return 2;
        }
        if ("省级代理".equals(userRank)) {
            return 3;
        }
        return -1;
    }

    /**
     * 功能描述：新增金额钱包记录
     *
     * @param webctx   Spring上下文
     * @param bonus    奖金
     * @param userId   用户ID
     * @param phone    手机号
     * @param heUserId 对方用户ID
     * @param hePhone  对方手机号
     * @param dataType 类型：直推奖，代理奖等
     * @author Ajie
     * @date 2020/6/11 0011
     */
    public void addMoneyRec(WebApplicationContext webctx, double bonus, String userId, String phone
            , String heUserId, String hePhone, String dataType) throws Exception {
        // 金额表
        Money_recordManager money_recordService = (Money_recordManager) webctx.getBean("money_recordService");
        PageData pd = new PageData();
        // 创建金额钱包记录
        pd.put("PHONE", phone);
        pd.put("USER_ID", userId);
        pd.put("MONEY", bonus);
        pd.put("STATUS", "已完成");
        pd.put("CHARGE", "0");
        pd.put("ACTUAL_RECEIPT", bonus);
        pd.put("HE_PHONE", hePhone);
        pd.put("HE_USER_ID", heUserId);
        pd.put("TAG", "+");
        pd.put("DATA_TYPE", dataType);
        money_recordService.save(pd);
    }


    /**
     * 功能描述：增加金额钱包记录
     *
     * @param webctx        Spring上下文
     * @param phone         手机号
     * @param userId        用户ID
     * @param money         金额
     * @param status        状态
     * @param charge        手续费
     * @param actualReceipt 实际到账
     * @param hePhone       对方手机号
     * @param heUserId      对方用户ID
     * @param tag           + 或者 -
     * @param dataType      数据类型 卖出股票、买入股票等
     * @author Ajie
     * @date 2020/6/10 0010
     */
    public void addMoneyRec(WebApplicationContext webctx, Object phone, String userId, Object money, String status, Object charge
            , Object actualReceipt, Object hePhone, String heUserId, char tag, String dataType) throws Exception {
        // 金额记录钱包表
        Money_recordManager money_recordService = (Money_recordManager) webctx.getBean("money_recordService");

        PageData pd = new PageData();
        pd.put("PHONE", phone);    //手机号
        pd.put("USER_ID", userId);    //用户ID
        pd.put("MONEY", money);    //金额
        pd.put("STATUS", status);    //状态
        pd.put("CHARGE", charge);    //手续费
        pd.put("ACTUAL_RECEIPT", actualReceipt);    //实际到账
        pd.put("HE_PHONE", hePhone);    //对方手机号
        pd.put("HE_USER_ID", heUserId);    //对方用户ID
        pd.put("TAG", tag);
        pd.put("DATA_TYPE", dataType);
        money_recordService.save(pd);
    }


}
