package com.fh.controller.front;



import com.fh.controller.base.BaseController;

import com.fh.dao.RedisDao;
import com.fh.entity.MemUser;
import com.fh.service.front.AccountManager;
import com.fh.service.front.Account_dataManager;
import com.fh.service.front.Sys_configManager;
import com.fh.service.front.User_addrManager;
import com.fh.service.mall.OrderManager;
import com.fh.service.mall.Order_itemManager;

import com.fh.service.record.*;
import com.fh.service.share.*;
import com.fh.service.system.FHlogManager;
import com.fh.util.Const;
import com.fh.util.Jurisdiction;
import com.fh.util.PageData;
import org.apache.shiro.session.Session;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 说明：前台基础控制器
 * 创建人：Ajie
 * 创建时间：2020年4月1日17:34:45
 */
public class BaseFrontController extends BaseController {

    /**
     * 系统参数
     */
    @Resource(name = "sys_configService")
    public Sys_configManager sys_configService;
    /**
     * 新闻公告
     */
    @Resource(name = "sys_newsService")
    public Sys_newsManager sys_newsService;
    /**
     * 轮播图
     */
    @Resource(name = "sys_chartService")
    public Sys_chartManager sys_chartService;
    /**
     * 日志管理
     */
    @Resource(name = "fhlogService")
    public FHlogManager FHLOG;
    /**
     * Redis
     */
    @Resource(name = "redisDaoImpl")
    public RedisDao redisDaoImpl;
    /**
     * 用户资料表
     */
    @Resource(name = "account_dataService")
    public Account_dataManager account_dataService;
    /**
     * 用户表
     */
    @Resource(name = "accountService")
    public AccountManager accountService;
    /**
     * 用户收货地址表
     */
    @Resource(name = "user_addrService")
    public User_addrManager user_addrService;
    /**
     * usdt钱包记录
     */
    @Resource(name = "usdt_recordService")
    public Usdt_recordManager usdt_recordService;
    /**
     * 申请代理记录表
     */
    @Resource(name = "agent_recordService")
    public Agent_recordManager agent_recordService;
    /**
     * 订单项
     */
    @Resource(name = "order_itemService")
    public Order_itemManager order_itemService;
    /**
     * 订单
     */
    @Resource(name = "orderService")
    public OrderManager orderService;
    /**
     * 用户订单配送地址
     */
    @Resource(name = "user_addr_orderService")
    public User_addr_orderManager user_addr_orderService;
    /**
     * K线图
     */
    @Resource(name = "line_chartService")
    public Line_chartManager line_chartService;
    /**
     * 用户想展示在首页的股票版块
     */
    @Resource(name = "user_show_stockService")
    public User_show_stockManager user_show_stockService;
    /**
     * 股票商品表
     */
    @Resource(name = "shares_prodService")
    public Shares_prodManager shares_prodService;
    /**
     * 提货记录表
     */
    @Resource(name = "delivery_recordService")
    public Delivery_recordManager delivery_recordService;
    /**
     * 股票订单表
     */
    @Resource(name = "shares_orderService")
    public Shares_orderManager shares_orderService;


    /**
     * 功能描述：新增usdt钱包记录
     *
     * @param phone         我的手机号
     * @param userID        我的用户ID
     * @param money         金额
     * @param status        状态
     * @param charge        手续费
     * @param actualReceipt 实际到账
     * @param hePhone       对方手机号
     * @param heUserId      对方用户ID
     * @param walletAddress 钱包地址
     * @param dataType      数据类型：充值、提现、消费等等
     * @param voucherPath   支付凭证
     * @param tag           + 或者 -
     * @author Ajie
     * @date 2020/5/26 0026
     */
    public void addUsdtWalletRecord(String phone, String userID, String money, String status, String charge, String actualReceipt
            , String hePhone, String heUserId, String walletAddress, String dataType, String voucherPath, String tag) throws Exception {

        PageData pd = new PageData();
        pd.put("PHONE", phone);
        pd.put("USER_ID", userID);
        pd.put("MONEY", money);
        pd.put("STATUS", status);
        pd.put("CHARGE", charge);
        pd.put("ACTUAL_RECEIPT", actualReceipt);
        pd.put("HE_PHONE", hePhone);
        pd.put("HE_USER_ID", heUserId);
        pd.put("WALLET_ADDRESS", walletAddress);
        pd.put("DATA_TYPE", dataType);
        pd.put("VOUCHER_PATH", voucherPath);
        pd.put("TAG", tag);
        usdt_recordService.save(pd);
    }

    /**
     * 功能描述：新增usdt钱包记录，默认手续费为0
     *
     * @param phone         我的手机号
     * @param userID        我的用户ID
     * @param money         金额
     * @param status        状态
     * @param walletAddress 钱包地址
     * @param dataType      数据类型：充值、提现、消费等等
     * @param voucherPath   支付凭证
     * @param tag           + 或者 -
     * @author Ajie
     * @date 2020/5/26 0026
     */
    public void addUsdtWalletRecord(String phone, String userID, String money, String status
            , String walletAddress, String dataType, String voucherPath, String tag) throws Exception {

        PageData pd = new PageData();
        pd.put("PHONE", phone);
        pd.put("USER_ID", userID);
        pd.put("MONEY", money);
        pd.put("STATUS", status);
        pd.put("CHARGE", 0);
        pd.put("ACTUAL_RECEIPT", money);
        pd.put("HE_PHONE", "");
        pd.put("HE_USER_ID", "");
        pd.put("WALLET_ADDRESS", walletAddress);
        pd.put("DATA_TYPE", dataType);
        pd.put("VOUCHER_PATH", voucherPath);
        pd.put("TAG", tag);
        usdt_recordService.save(pd);
    }


    /**
     * 功能描述：存入session和登录序列
     *
     * @param user 用户信息
     * @author Ajie
     * @date 2020/3/27 0027
     */
    public void addUserLogin(MemUser user) {
        Session session = Jurisdiction.getSession();
        session.setAttribute(Const.SESSION_MEMUSER, user);
        // 从上下文缓存取登录列表
        Map<String, String> loginMap = (Map<String, String>) applicati.getAttribute("loginMap");
        if (loginMap == null) {
            loginMap = new HashMap<>();
        }
        // 添加当前登录用户，并更新缓存
        loginMap.put(user.getACCOUNT_ID(), session.getId().toString());
        applicati.setAttribute("loginMap", loginMap);
    }


}
