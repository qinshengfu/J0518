package com.fh.controller.front;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.fh.annotation.CacheLock;
import com.fh.entity.MemUser;
import com.fh.entity.result.R;
import com.fh.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 说明：前台股票相关控制器
 * 创建人：Ajie
 * 创建时间：2020年5月22日17:31:01
 */
@Controller
@RequestMapping("/front")
@Api(tags = "股票操作")
public class StockController extends BaseFrontController {

    /**
     * 雪花算法
     */
    private final Snowflake snowflake = new Snowflake(8L, 1L);

    /**
     * 功能描述：访问前台【行情】页面
     *
     * @author Ajie
     * @date 2020年5月22日17:29:31
     */
    @RequestMapping(value = "to_quotation")
    public ModelAndView toQuotation() {
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/quotation/quotation");
        mv.addObject("flag", "quotation");
        return mv;
    }

    /**
     * 功能描述：访问前台【股票交易】页面
     *
     * @author Ajie
     * @date 2020年5月22日17:29:27
     */
    @RequestMapping(value = "to_transaction")
    public ModelAndView toTransaction() {
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/transaction/transaction");
        mv.addObject("flag", "transaction");
        return mv;
    }

    /**
     * 功能描述：访问前台【提货确认订单】页面
     *
     * @author Ajie
     * @date 2020年6月5日11:48:08
     */
    @RequestMapping(value = "toConfirmOrder")
    public ModelAndView toConfirmOrder() {
        PageData pd = this.getPageData();
        ModelAndView mv = getModelAndView();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        mv.setViewName("front/center/takeGoodsConfirm");
        mv.addObject("pd", pd);
        mv.addObject("user", user);
        return mv;
    }

    /**
     * 功能描述：访问前台【股票商品持有页面】页面
     *
     * @author Ajie
     * @date 2020年6月5日11:48:08
     */
    @RequestMapping(value = "toHold")
    public ModelAndView toHold() {
        ModelAndView mv = getModelAndView();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        mv.setViewName("front/transaction/hold");
        mv.addObject("user", user);
        return mv;
    }

    /**
     * 功能描述：获取首页股票版本信息
     *
     * @author Ajie
     * @date 2020/6/3 0003
     */
    @RequestMapping(value = "getStockInfoList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取首页股票版本信息")
    public R getStockInfoList() throws Exception {
        PageData pd = this.getPageData();
        // 查询所有股票版块
        List<PageData> stockList = shares_prodService.listAll(pd);
        // 循环查询当日成交量和当日交易额
        for (PageData map : stockList) {
            map.put("DAY_VOLUME", shares_prodService.getDayVolumeById(map).get("DAY_VOLUME"));
            map.put("DAY_TRADING", shares_prodService.getDayTradingById(map).get("DAY_TRADING"));
        }
        return R.ok().data("item", stockList);
    }

    /**
     * 功能描述：根据股票商品ID获取K线图数据
     *
     * @author Ajie
     * @date 2020年6月12日09:48:16
     */
    @RequestMapping(value = "getShareInfo", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "根据股票商品ID获取K线图数据")
    @ApiImplicitParam(name = "SHARES_PROD_ID", value = "股票商品ID", dataType = "Integer", required = true)
    public R getShareInfo() throws Exception {
        PageData pd = this.getPageData();
        // 根据股票商品ID查询每天最后一条数据
        List<PageData> stockList = line_chartService.listByDay(pd);
        return R.ok().data("item", stockList);
    }

    /**
     * 功能描述：添加用户想观看的股票版块
     *
     * @author Ajie
     * @date 2020年6月4日16:48:22
     */
    @RequestMapping(value = "addUserShowStock", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "添加用户想观看的股票版块")
    @CacheLock(prefix = "J0518")
    public R addUserShowStock() throws Exception {
        PageData pd = this.getPageData();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        pd.put("USER_ID", user.getACCOUNT_ID());
        PageData stock = user_show_stockService.findByUserIdAndStockId(pd);
        if (stock != null) {
            return R.error().message("已添加在首页，无需重新添加");
        }
        // 新增一个要展示的股票
        user_show_stockService.save(pd);
        return R.ok();
    }

    /**
     * 功能描述：获取用户想展示的股票版块信息
     *
     * @author Ajie
     * @date 2020年6月4日16:48:22
     */
    @RequestMapping(value = "getUserShowStockList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "添加用户想观看的股票版块")
    @CacheLock(prefix = "J0518")
    public R getUserShowStockList() throws Exception {
        PageData pd = this.getPageData();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        pd.put("USER_ID", user.getACCOUNT_ID());
        // 连表查询
        List<PageData> list = user_show_stockService.listByUserId(pd);

        // 把股票ID用,分割拼接在一个字段
        StringBuilder sb = new StringBuilder();
        for (PageData map : list) {
            sb.append(map.get("SHARES_PROD_ID")).append(",");
        }
        pd.put("stockIdList", sb.toString());
        pd.put("item", list);
        return R.ok().data(pd);
    }

    /**
     * 功能描述：获取所有余数大于0的股票商品
     *
     * @author Ajie
     * @date 2020年6月4日18:12:54
     */
    @RequestMapping(value = "getStockProd", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取所有余数大于0的股票商品")
    public R getStockProd() throws Exception {
        PageData pd = this.getPageData();
        List<PageData> list = shares_prodService.listAllByNum(pd);
        pd.put("item", list);
        return R.ok().data(pd);
    }

    /**
     * 功能描述：购买限量发售的股票商品
     *
     * @author Ajie
     * @date 2020年6月5日09:22:02
     */
    @RequestMapping(value = "stockProdBuyCheck", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "购买限量发售的股票商品")
    public R stockProdBuyCheck() throws Exception {
        PageData pd = this.getPageData();
        // 根据股票商品ID查询
        PageData stockProd = shares_prodService.findById(pd);
        // 购买数量
        int amount = Convert.toInt(pd.get("amount"));
        // 库存、开盘价
        int remainder = Convert.toInt(stockProd.get("REMAINDER"));
        double openPrice = Convert.toDouble(stockProd.get("OPEN_PRICE"));
        if (remainder < amount) {
            return R.error().message("库存不足");
        }
        // 计算总价
        double totalMoney = NumberUtil.mul(amount, openPrice);
        // 从缓存中获取当前登录用户信息、然后根据用户ID 查询最新信息
        MemUser memUser = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        pd.put("ACCOUNT_ID", memUser.getACCOUNT_ID());
        pd.put("PHONE", memUser.getPHONE());
        PageData user = accountService.findById(pd);
        // 获取用户余额
        double money = Convert.toDouble(user.get("MONEY"));
        // 验证余额
        if (totalMoney > money) {
            return R.error().message("用户余额不足");
        }
        // 执行购买。扣库存》创建购买记录》减用户余额》创建钱包消费记录
        pd.putAll(stockProd);
        pd.put("totalMoney", totalMoney);
        pd.put("PRICE", openPrice);
        shares_prodService.updateBuyStock(pd);
        return R.ok().data(pd);
    }

    /**
     * 功能描述：获取用户持有的股票商品
     *
     * @author Ajie
     * @date 2020年6月5日09:22:02
     */
    @RequestMapping(value = "listAllTakeGoods", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取用户持有的股票商品")
    public R getAllTakeGoods() throws Exception {
        PageData pd = this.getPageData();
        MemUser memUser = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        pd.put("PHONE", memUser.getPHONE());
        List<PageData> list = shares_orderService.listByPhone(pd);
        // 股票商品ID以,号分割拼接
        StringBuilder sb = new StringBuilder();
        for (PageData map : list) {
            int remainder = Convert.toInt(map.get("REMAINDER"));
            // 如果列表中已经包含则进入下一个循环
            String id = map.get("SHARES_PROD_ID").toString();
            if (StrUtil.contains(sb.toString(), id)) {
                continue;
            }
            sb.append(map.get("SHARES_PROD_ID")).append(",");
        }
        pd.put("holdProdIdList", sb.toString());
        pd.put("item", list);
        return R.ok().data(pd);
    }

    /**
     * 功能描述：根据ID查询提货股票商品
     *
     * @author Ajie
     * @date 2020年6月5日14:58:25
     */
    @RequestMapping(value = "getTakeGoodsOrder", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "根据ID查询提货股票商品")
    @ApiImplicitParam(name = "SHARES_ORDER_ID", value = "购买股票商品订单ID", dataType = "Integer", required = true)
    public R getTakeGoodsOrder() throws Exception {
        PageData pd = this.getPageData();
        PageData prodOrder = shares_orderService.findById(pd);
        pd.putAll(prodOrder);
        return R.ok().data(pd);
    }

    /**
     * 功能描述：确认提货
     *
     * @author Ajie
     * @date 2020年6月5日14:58:25
     */
    @RequestMapping(value = "takeProdCheck", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "确认提货")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SHARES_ORDER_ID", value = "股票订单ID", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "USER_ADDR_ID", value = "用户地址ID", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "num", value = "提货数量", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "pass", value = "交易密码", required = true),
    })
    public R takeProdCheck() throws Exception {
        PageData pd = this.getPageData();
        PageData prodOrder = shares_orderService.findById(pd);
        // 提货数量、股票商品订单库存
        int num = Convert.toInt(pd.get("num"));
        int orderStock = Convert.toInt(prodOrder.get("REMAINDER"));
        if (num > orderStock) {
            R.error().message("商品数量不足！非法操作");
        }
        // 验证交易密码
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        String pass = pd.getString("pass");
        pass = DigestUtil.md5Hex(user.getPHONE() + pass);
        if (!user.getSECURITY_PASSWORD().equals(pass)) {
            return R.error().message("交易密码错误");
        }
        PageData userAddress = user_addrService.findById(pd);

        // 减订单库存》创建订单收货地址》创建提货记录
        // 使用雪花算法生成的订单号
        String orderNumber = snowflake.nextIdStr();
        pd.put("ORDER_NUMBER", orderNumber);    //订购流水号
        pd.putAll(prodOrder);
        pd.putAll(userAddress);
        pd.put("PHONE", user.getPHONE());
        pd.put("USER_ID", user.getACCOUNT_ID());
        delivery_recordService.save(pd);
        return R.ok().message("等待后台发货");
    }

    /**
     * 功能描述：交易大厅-发布买入股票
     *
     * @author Ajie
     * @date 2020年6月8日15:37:21
     */
    @RequestMapping(value = "issueBuyStock", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "交易大厅-发布买入股票")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SHARES_PROD_ID", value = "股票商品ID", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "PRICE", value = "单价", dataType = "Double", required = true),
            @ApiImplicitParam(name = "TOTAL", value = "买入数量", dataType = "Integer", required = true),
    })
    public R issueBuyStock() throws Exception {
        PageData pd = this.getPageData();
        PageData buyStock = shares_prodService.findById(pd);
        if (buyStock == null) {
            return R.error().message("商品不存在");
        }
        // 单价、买入数量
        double price = Convert.toDouble(pd.get("PRICE"));
        int num = Convert.toInt(pd.get("TOTAL"));
        if (price < 1 || num < 1) {
            R.error().message("不允许输入负数");
        }
        // 开盘价
        double openPrice = Convert.toDouble(buyStock.get("OPEN_PRICE"));
        if (price < openPrice) {
            return R.error().message("请输入大于" + openPrice + "的单价");
        }
        // 总价
        double totalAmount = NumberUtil.mul(price, num);
        // 验证用户金额
        MemUser memUser = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        pd.put("ACCOUNT_ID", memUser.getACCOUNT_ID());
        pd.put("PHONE", memUser.getPHONE());
        PageData user = accountService.findById(pd);
        double money = Convert.toDouble(user.get("MONEY"));
        if (money < totalAmount) {
            return R.error().message("用户余额不足");
        }
        pd.put("money", totalAmount);
        pd.put("PROD_NAME", buyStock.get("PROD_NAME"));
        pd.put("NUMBER_CODE", buyStock.get("NUMBER_CODE"));
        // 创建买入订单>扣除用户钱包>创建钱包消费记录
        shares_orderService.saveBuyOrder(pd);
        return R.ok().message("发布成功");
    }

    /**
     * 功能描述：交易大厅-发布卖出股票
     *
     * @author Ajie
     * @date 2020年6月9日09:25:46
     */
    @RequestMapping(value = "issueSellStock", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "交易大厅-发布卖出股票")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SHARES_ORDER_ID", value = "股票订单ID", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "PRICE", value = "单价", dataType = "Double", required = true),
            @ApiImplicitParam(name = "TOTAL", value = "卖出数量", dataType = "Integer", required = true),
    })
    public R issueSellStock() throws Exception {
        PageData pd = this.getPageData();
        PageData sellStock = shares_orderService.findById(pd);
        PageData prod = shares_prodService.findById(sellStock);
        if (sellStock == null) {
            return R.error().message("订单不存在");
        }
        // 验证股票商品是否处于交易中状态
        if ("发行中".equals(prod.getString("STATUS"))) {
            return R.error().message("暂不能卖出，还处于发行阶段");
        }
        // 验证是否符合T+1规则
        boolean isIn = DateUtil.isEqualDate(sellStock.getString("GMT_MODIFIED"));
        if (!isIn) {
            return R.error().message("在" + sellStock.getString("GMT_MODIFIED") + "之后才能卖出");
        }
        // 单价、卖出数量
        double price = Convert.toDouble(pd.get("PRICE"));
        int num = Convert.toInt(pd.get("TOTAL"));
        if (price < 1 || num < 1) {
            R.error().message("不允许输入负数");
        }
        // 开盘价
        double openPrice = Convert.toDouble(prod.get("OPEN_PRICE"));
        if (price < openPrice) {
            return R.error().message("请输入大于" + openPrice + "的单价");
        }
        // 验证库存
        int stock = Convert.toInt(sellStock.get("REMAINDER"));
        if (num > stock) {
            return R.error().message("库存不足");
        }
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        // 创建卖出订单
        pd.put("PROD_NAME", sellStock.get("PROD_NAME"));
        pd.put("NUMBER_CODE", sellStock.get("NUMBER_CODE"));
        pd.put("PHONE", user.getPHONE());
        pd.put("SHARES_PROD_ID", sellStock.get("SHARES_PROD_ID"));
        shares_orderService.saveSellOrder(pd);
        return R.ok().message("发布成功");
    }

    /**
     * 功能描述：交易大厅-取消订单
     *
     * @author Ajie
     * @date 2020年6月9日15:52:33
     */
    @RequestMapping(value = "cancelOrderCheck", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "交易大厅-取消订单")
    @ApiImplicitParam(name = "SHARES_ORDER_ID", value = "股票订单ID", dataType = "Integer", required = true)
    @CacheLock(prefix = "J0518")
    public R cancelOrderCheck() throws Exception {
        PageData pd = this.getPageData();
        PageData order = shares_orderService.findById(pd);
        if (order == null) {
            return R.error().message("订单不存在");
        }
        // 验证类型
        String type = order.getString("TYPE");
        if ("买入".equals(type)) {
            // 调用取消买入订单的方法
            return shares_orderService.updateCancelOrder(order, true);
        }
        if ("卖出".equals(type)) {
            // 调用取消卖出订单的方法
            return shares_orderService.updateCancelOrder(order, false);
        }
        return R.error();
    }

    /**
     * 功能描述：获取最近10个买卖订单
     *
     * @author Ajie
     * @date 2020年6月11日16:21:32
     */
    @RequestMapping(value = "getLastBuyAndSellOrder", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取最近10个买卖订单")
    @ApiImplicitParam(name = "SHARES_PROD_ID", value = "股票商品ID", dataType = "Integer", required = true)
    public R getLastBuyAndSellOrder() throws Exception {
        PageData pd = this.getPageData();
        List<PageData> list = shares_orderService.listByBuyAndSellOrder(pd);
        return R.ok().data("item", list);
    }


    /**
     * 功能描述：获取用户提货记录列表-逻辑分页
     *
     * @author Ajie
     * @date 2020/6/1 0001
     */
    @RequestMapping(value = "getGoodsRecordList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取用户提货记录列表-逻辑分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "STATUS", value = "状态：null 查询所有，或者输入对应的状态查询", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "currentPage", value = "页码", example = "1", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "showCount", value = "页长", example = "10", required = true, dataType = "Integer"),
    })
    public R getUserOrderList() throws Exception {
        PageData pd = this.getPageData();
        if ("全部".equals(pd.getString("STATUS"))) {
            pd.remove("STATUS");
        }
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        pd.put("USER_ID", user.getACCOUNT_ID());
        List<PageData> orderList = delivery_recordService.listAllByUserId(pd);
        // 执行分页
        Pager<PageData> pager = new Pager<>();
        int currentPage = Convert.toInt(pd.get("currentPage"));
        int showCount = Convert.toInt(pd.get("showCount"));
        pager.setCurentPageIndex(currentPage);
        pager.setCountPerpage(showCount);
        // 传入大集合
        pager.setBigList(orderList);
        // 获取总页数
        int totalPage = pager.getPageCount();
        // 获取分页后的列表
        List<PageData> pageList = pager.getSmallList();
        pd.put("totalPage", totalPage);
        pd.put("curPageData", pageList);
        return R.ok().data(pd);
    }

    /**
     * 功能描述：根据提货ID获取订单详情
     *
     * @author Ajie
     * @date 2020/6/1 0001
     */
    @RequestMapping(value = "getGoodsOrderDetail", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "根据提货ID获取订单详情")
    @ApiImplicitParam(name = "DELIVERY_RECORD_ID", value = "订单ID", required = true, dataType = "Integer")
    public R getOrderDetail() throws Exception {
        PageData pd = this.getPageData();
        // 根据订单ID连表查询
        PageData orderDetail = delivery_recordService.findByIdLinkedTable(pd);
        return R.ok().data(orderDetail);
    }

    /**
     * 功能描述：股票商品提货-确认收货
     *
     * @author Ajie
     * @date 2020年6月17日15:52:19
     */
    @RequestMapping(value = "goodsConfirm", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "股票商品提货-确认收货")
    @ApiImplicitParam(name = "DELIVERY_RECORD_ID", value = "订单ID", required = true, dataType = "Integer")
    public R confirmSign() throws Exception {
        PageData pd = this.getPageData();
        // 更新订单状态为已完成
        pd.put("STATUS", "已完成");
        pd.put("FINALLY_TIME", cn.hutool.core.date.DateUtil.now());
        pd.put("GMT_MODIFIED", cn.hutool.core.date.DateUtil.now());
        delivery_recordService.edit(pd);
        return R.ok().message("收货成功");
    }

    /**
     * 功能描述：获取当前登录用户信息
     *
     * @author Ajie
     * @date 2020年6月19日15:00:49
     */
    @RequestMapping(value = "getUserInfo", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取当前登录用户信息")
    @ApiImplicitParam(name = "ACCOUNT_ID", value = "用户id", required = true, dataType = "Integer")
    public R getUserById() throws Exception {
        PageData pd = new PageData();
        MemUser memUser = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        pd.put("ACCOUNT_ID", memUser.getACCOUNT_ID());
        PageData user = accountService.findById(pd);
        return R.ok().data(user);
    }

}
