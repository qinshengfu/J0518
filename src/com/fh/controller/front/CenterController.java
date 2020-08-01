package com.fh.controller.front;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrSpliter;
import cn.hutool.crypto.digest.DigestUtil;
import com.fh.annotation.Limit;
import com.fh.entity.MemUser;
import com.fh.entity.result.R;
import com.fh.service.record.Agent_recordManager;
import com.fh.util.*;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.sun.org.apache.regexp.internal.RE;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 功能描述：
 *
 * @author Ajie
 * @date 2020年5月22日17:30:58
 */
@Controller
@RequestMapping(value = "/front")
@Slf4j
@Api(tags = "个人中心操作")
public class CenterController extends BaseFrontController {

    /**
     * 功能描述：访问前台【申请代理】页面
     *
     * @author Ajie
     * @date 2020年5月22日17:29:27
     */
    @RequestMapping(value = "to_agent")
    public ModelAndView toAgent() {
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/center/agent");
        return mv;
    }

    /**
     * 功能描述：访问前台【我的】页面
     *
     * @author Ajie
     * @date 2020年5月22日17:29:27
     */
    @RequestMapping(value = "to_center")
    public ModelAndView toCenter() throws Exception {
        ModelAndView mv = getModelAndView();
        MemUser memUser = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        PageData user = new PageData();
        user.put("PHONE", memUser.getPHONE());
        user = accountService.findByPhone(user);
        PageData param = sys_configService.findById(new PageData());
        mv.setViewName("front/center/center");
        mv.addObject("flag", "center");
        mv.addObject("user", user);
        mv.addObject("par", param);
        return mv;
    }

    /**
     * 功能描述：访问前台【修改资料】页面
     *
     * @author Ajie
     * @date 2020年5月22日17:29:27
     */
    @RequestMapping(value = "to_changdata")
    public ModelAndView toChangdata() throws Exception {
        MemUser memUser = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        // 根据session缓存中的信息连表查询数据库
        PageData user = new PageData();
        user.put("PHONE", memUser.getPHONE());
        user = accountService.findByPhone(user);
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/center/changdata");
        mv.addObject("user", user);
        return mv;
    }

    /**
     * 功能描述：访问前台【我要提货】页面
     *
     * @author Ajie
     * @date 2020年5月22日17:29:27
     */
    @RequestMapping(value = "to_goods")
    public ModelAndView toGoods() {
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/center/goods");
        return mv;
    }

    /**
     * 功能描述：访问前台【提货记录】页面
     *
     * @author Ajie
     * @date 2020年6月17日15:10:41
     */
    @RequestMapping(value = "to_goodsRecord")
    public ModelAndView toGoodsRecord() {
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/center/goodsRecord");
        return mv;
    }

    /**
     * 功能描述：访问前台【提货订单详情】页面
     *
     * @author Ajie
     * @date 2020年6月17日15:31:56
     */
    @RequestMapping(value = "to_goodsDetail/{id}")
    public ModelAndView toGoodsDetail(@PathVariable(value = "id") String id) {
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/center/goodsDetail");
        mv.addObject("DELIVERY_RECORD_ID", id);
        return mv;
    }

    /**
     * 功能描述：访问前台【系统咨询】页面
     *
     * @author Ajie
     * @date 2020年5月22日17:29:27
     */
    @RequestMapping(value = "to_news")
    public ModelAndView toNews() {
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/center/news");
        return mv;
    }

    /**
     * 功能描述：访问前台【咨询详情】页面
     *
     * @author Ajie
     * @date 2020年5月22日17:29:27
     */
    @RequestMapping(value = "to_newsDetail/{id}")
    public ModelAndView toNewsDetail(@PathVariable("id") String id) throws Exception {
        PageData pd = new PageData();
        pd.put("SYS_NEWS_ID", id);
        pd = sys_newsService.findById(pd);
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/center/news-detail");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 功能描述：访问前台【我的订单】页面
     *
     * @author Ajie
     * @date 2020年5月22日17:29:27
     */
    @RequestMapping(value = "to_order")
    public ModelAndView toOrder() {
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/center/order");
        return mv;
    }

    /**
     * 功能描述：访问前台【订单详情】页面
     *
     * @author Ajie
     * @date 2020年5月22日17:29:27
     */
    @RequestMapping(value = "to_orderDetail/{id}")
    public ModelAndView toOrderDetail(@PathVariable(value = "id") String id) {
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/center/order_detail");
        mv.addObject("ORDER_ID", id);
        return mv;
    }

    /**
     * 功能描述：访问前台【充值提币】页面
     *
     * @author Ajie
     * @date 2020年5月22日17:29:27
     */
    @RequestMapping(value = "to_recharg")
    public ModelAndView toRecharg() throws Exception {
        MemUser memUser = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        // 根据session缓存中的信息连表查询数据库
        PageData user = new PageData();
        user.put("PHONE", memUser.getPHONE());
        user = accountService.findByPhone(user);
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/center/recharg");
        mv.addObject("user", user);
        return mv;
    }

    /**
     * 功能描述：访问前台【邀请好友】页面
     *
     * @author Ajie
     * @date 2020年5月22日17:29:27
     */
    @RequestMapping(value = "to_share")
    public ModelAndView toShare() {
        ModelAndView mv = getModelAndView();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        mv.setViewName("front/center/share");
        mv.addObject("user", user);
        return mv;
    }

    /**
     * 功能描述：访问前台【金额兑换】页面
     *
     * @author Ajie
     * @date 2020年5月27日11:40:00
     */
    @RequestMapping(value = "to_substitution")
    public ModelAndView toSubstitution() {
        ModelAndView mv = getModelAndView();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        mv.setViewName("front/center/substitution");
        mv.addObject("user", user);
        return mv;
    }

    /**
     * 功能描述：访问前台【usdt转账】页面
     *
     * @author Ajie
     * @date 2020年5月27日11:40:00
     */
    @RequestMapping(value = "to_transfer")
    public ModelAndView toTransfer() {
        ModelAndView mv = getModelAndView();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        mv.setViewName("front/center/transfer");
        mv.addObject("user", user);
        return mv;
    }

    /**
     * 功能描述：访问前台【安全中心】页面
     *
     * @author Ajie
     * @date 2020年5月27日14:18:33
     */
    @RequestMapping(value = "to_securityCenter")
    public ModelAndView toSecurityCenter() {
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/center/securityCenter");
        return mv;
    }

    /**
     * 功能描述：访问前台【修改密码】页面
     *
     * @author Ajie
     * @date 2020年5月27日14:18:33
     */
    @RequestMapping(value = "to_editPassword")
    public ModelAndView toEditPassword() {
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/center/editPassword");
        return mv;
    }

    /**
     * 功能描述：访问前台【修改密保】页面
     *
     * @author Ajie
     * @date 2020年5月27日14:18:33
     */
    @RequestMapping(value = "to_editSecret")
    public ModelAndView toEditSecret() {
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/center/editSecret");
        return mv;
    }

    /**
     * 功能描述：获取当前登录用户邀请二维码
     *
     * @author Ajie
     * @date 2020/5/26 0026
     */
    @RequestMapping(value = "invitationCode", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("获取当前登录用户邀请二维码")
    public void invitationCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        // 请求路径
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + path + "/";
        // 链接内容
        String content = basePath + "release/to_register.do?code=" + user.getACCOUNT_ID();
        // 通过输出流把邀请码输出到页面
        ServletOutputStream outputStream = response.getOutputStream();
        // 调用工具类
        TwoDimensionCode.encoderQRCode(content, outputStream);
    }

    /**
     * 功能描述：修改基础用户资料
     *
     * @author Ajie
     * @date 2020年5月27日14:44:33
     */
    @RequestMapping(value = {"/updateUserBaseData"}, method = {RequestMethod.POST})
    @ResponseBody
    @ApiOperation("修改基础用户资料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "USER_ID", value = "用户ID", required = true),
            @ApiImplicitParam(name = "FULL_NAME", value = "姓名"),
            @ApiImplicitParam(name = "ID_CARD_NO", value = "身份证号码"),
            @ApiImplicitParam(name = "WALLET_ADDRESS", value = "钱包地址"),
    })
    public R updateUserBaseData() throws Exception {
        // 请求的参数
        PageData pd = this.getPageData();
        // 循环Key
        for (Object key : pd.keySet()) {
            if (pd.getString(key).isEmpty()) {
                return R.parError();
            }
        }
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        pd.put("PHONE", user.getPHONE());
        // 特殊账号可以使用重复资料
        if ("1".equals(user.getIS_SPECIAL())) {
            // 验证资料是否重复
            PageData checkData = account_dataService.findByUserData(pd);
            if (checkData != null) {
                return R.error().message("资料已存在");
            }
        }
        pd.put("GMT_MODIFIED", DateUtil.now());
        // 更新用户资料
        account_dataService.editByUserId(pd);
        return R.ok();
    }

    /**
     * 功能描述：修改用户密码
     *
     * @author Ajie
     * @date 2020年5月27日14:44:26
     */
    @RequestMapping(value = {"/updateUserPass"}, method = {RequestMethod.POST})
    @ResponseBody
    @ApiOperation("修改当前登录用户密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "passwordType", value = "密码类型 1：登录密码、0：交易密码", required = true),
            @ApiImplicitParam(name = "oidPassword", value = "旧密码", required = true),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required = true),
    })
    public R updateUserPassword() throws Exception {
        // 请求的参数
        PageData pd = this.getPageData();
        // 循环Key
        for (Object key : pd.keySet()) {
            if (pd.getString(key).isEmpty()) {
                return R.parError();
            }
        }
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        String passwordType = pd.getString("passwordType");
        // 密码加密
        String oidPassword = DigestUtil.md5Hex(user.getPHONE() + pd.getString("oidPassword"));
        String newPassword = DigestUtil.md5Hex(user.getPHONE() + pd.getString("newPassword"));
        if ("1".equals(passwordType) && !oidPassword.equals(user.getLOGIN_PASSWORD())) {
            return R.error().message("旧密码错误");
        }
        if ("0".equals(passwordType) && !oidPassword.equals(user.getSECURITY_PASSWORD())) {
            return R.error().message("旧密码错误");
        }
        PageData map = new PageData();
        map.put("ACCOUNT_ID", user.getACCOUNT_ID());
        map.put("GMT_MODIFIED", DateUtil.now());
        if ("1".equals(passwordType)) {
            map.put("LOGIN_PASSWORD", newPassword);
        } else {
            map.put("SECURITY_PASSWORD", newPassword);
        }
        // 更新用户密码
        accountService.edit(map);
        return R.ok();
    }

    /**
     * 功能描述：修改用户密保
     *
     * @author Ajie
     * @date 2020年5月27日14:44:26
     */
    @RequestMapping(value = {"/updateUserSecurity"}, method = {RequestMethod.POST})
    @ResponseBody
    @ApiOperation("修改当前登录用户密保")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SECURITY_QUESTION", value = "密保问题", required = true),
            @ApiImplicitParam(name = "SECURITY_ANSWER", value = "密保答案", required = true),
    })
    @Limit(name = "限制访问次数", key = "updateUserSecurity", prefix = "J0518", period = 60, count = 2)
    public R updateUserSecurity() throws Exception {
        // 请求的参数
        PageData pd = this.getPageData();
        // 循环Key
        for (Object key : pd.keySet()) {
            if (pd.getString(key).isEmpty()) {
                return R.parError();
            }
        }
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        pd.put("USER_ID", user.getACCOUNT_ID());
        pd.put("GMT_MODIFIED", DateUtil.now());
        // 更新用户密保
        account_dataService.editByUserId(pd);
        return R.ok();
    }

    /**
     * 功能描述：货币兑换
     *
     * @author Ajie
     * @date 2020年5月27日15:33:43
     */
    @RequestMapping(value = {"/amountSwap"}, method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("货币兑换")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "exchangeType", value = "兑换类型 1：USDT兑换金额、0：金额兑换USDT", required = true),
            @ApiImplicitParam(name = "num", value = "兑换数量", required = true, dataType = "Double"),
            @ApiImplicitParam(name = "pass", value = "交易密码", required = true),
    })
    @Limit(name = "限制访问次数", key = "amountSwap", prefix = "J0518", period = 60, count = 10)
    public R amountSwap() throws Exception {
        // 请求的参数
        PageData pd = this.getPageData();
        // 循环Key
        for (Object key : pd.keySet()) {
            if (pd.getString(key).isEmpty()) {
                return R.parError();
            }
        }
        MemUser memUser = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        // 密码加密
        String pass = DigestUtil.md5Hex(memUser.getPHONE() + pd.getString("pass"));
        if (!pass.equals(memUser.getSECURITY_PASSWORD())) {
            return R.error().message("密码错误");
        }
        // 获取系统参数
        PageData param = sys_configService.findById(new PageData());
        // 切割兑换比例
        List<String> usdtToMoney = StrSpliter.split(param.getString("USDT_TO_MONEY"), ":", 0, true, true);
        List<String> moneyToUsdt = StrSpliter.split(param.getString("MONEY_TO_USDT"), ":", 0, true, true);
        double num = Convert.toDouble(pd.get("num"));
        if (num < 1) {
            return R.parError().message("请输入大于 0 的数");
        }
        // 获取最新用户信息
        pd.put("ACCOUNT_ID", memUser.getACCOUNT_ID());
        PageData user = accountService.findById(pd);
        pd.put("phone", memUser.getPHONE());
        // 取最新余额
        double usdt = Convert.toDouble(user.get("USDT_WALLET"));
        double money = Convert.toDouble(user.get("MONEY"));
        // 判断兑换类型
        if ("1".equals(pd.getString("exchangeType"))) {
            if (num > usdt) {
                return R.error().message("余额不足,最多可兑换 " + usdt);
            }
            pd.put("base", usdtToMoney.get(0));
            pd.put("multiple", usdtToMoney.get(1));
            accountService.updateMoneySwap(pd, true);
        }
        if ("0".equals(pd.getString("exchangeType"))) {
            if (num > money) {
                return R.error().message("余额不足,最多可兑换 " + money);
            }
            double base = Convert.toDouble(moneyToUsdt.get(0));
            if (num < base) {
                return R.error().message("最少兑换 " + base);
            }
            pd.put("base", moneyToUsdt.get(0));
            pd.put("multiple", moneyToUsdt.get(1));
            accountService.updateMoneySwap(pd, false);
        }
        return R.ok();
    }

    /**
     * 功能描述：修改用户头像
     *
     * @author Ajie
     * @date 2020年5月26日10:33:27
     */
    @RequestMapping(value = {"/updateUserProfile"}, method = {RequestMethod.POST})
    @ResponseBody
    @ApiOperation("修改用户头像")
    @ApiImplicitParam(name = "USER_PORTRAIT", value = "用户头像url链接")
    public R updateUserProfile() throws Exception {
        // 请求的参数
        PageData pd = this.getPageData();
        pd.put("GMT_MODIFIED", DateUtil.now());
        account_dataService.editByUserId(pd);
        return R.ok();
    }

    /**
     * 功能描述：退出登录
     *
     * @author Ajie
     * @date 2020/5/26 0026
     */
    @RequestMapping(value = "/outLogin")
    public String outLogin() {
        Session session = Jurisdiction.getSession();
        // 清空缓存
        session.removeAttribute(Const.SESSION_MEMUSER);
        return "redirect:freont/to_login";
    }

    /**
     * 功能描述：获取系统参数
     *
     * @author Ajie
     * @date 2020/5/26 0026
     */
    @RequestMapping(value = "/getSystemParam", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("获取系统参数")
    public R getSystemParam() throws Exception {
        PageData param = sys_configService.findById(new PageData());
        return R.ok().data(param);
    }

    /**
     * 功能描述：线下充值
     *
     * @author Ajie
     * @date 2020/5/26 0026
     */
    @RequestMapping(value = "/rechargeVerification", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("线下充值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "walletAddress", value = "后台设置的收款钱包地址", required = true),
            @ApiImplicitParam(name = "num", value = "充值数额", dataType = "Double"),
            @ApiImplicitParam(name = "voucher", value = "支付凭证url"),
    })
    public R offlineRecharge() throws Exception {
        // 获取系统参数
        PageData param = sys_configService.findById(new PageData());
        if ("0".equals(param.getString("RECHARGE_SWITCH"))) {
            return R.error().message("系统维护中");
        }
        // 用户信息
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        // 请求参数
        PageData pd = this.getPageData();
        // 循环Key
        for (Object key : pd.keySet()) {
            if (pd.getString(key).isEmpty()) {
                return R.parError();
            }
        }
        // 验证数额
        double num = Convert.toDouble(pd.get("num"));
        if (num <= 0) {
            return R.parError().message("请输入大于 0 的数");
        }
        // 验证充值范围
        double min = Convert.toDouble(param.get("MIN_RECHARGE"));
        double max = Convert.toDouble(param.get("MAX_RECHARGE"));
        double multiple = Convert.toDouble(param.get("RECHARGE_MULTIPLE"));
        if (num < min) {
            return R.parError().message("最少充值 " + min);
        }
        if (num > max) {
            return R.parError().message("最大充值 " + max);
        }
        if (num % multiple != 0 && multiple > 0) {
            return R.parError().message("请输入 " + multiple + " 的倍数");
        }
        // 新增一条待审核的充值记录
        addUsdtWalletRecord(user.getPHONE(), user.getACCOUNT_ID(), Convert.toStr(num), "待审核"
                , pd.getString("walletAddress"), "充值", pd.getString("voucher"), "+");
        return R.ok().message("等待后台审核");
    }

    /**
     * 功能描述：线下提现
     *
     * @author Ajie
     * @date 2020/5/26 0026
     */
    @RequestMapping(value = "/withdrawVerification", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("线下提现")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "walletAddress", value = "usdt收款地址", required = true),
            @ApiImplicitParam(name = "num", value = "提现数额", dataType = "Double"),
            @ApiImplicitParam(name = "pass", value = "交易密码"),
    })
    public R offlineWithdraw() throws Exception {
        // 获取系统参数
        PageData param = sys_configService.findById(new PageData());
        if ("0".equals(param.getString("WITHDRAWAL_SWITCH"))) {
            return R.error().message("系统维护中");
        }
        // 用户信息
        MemUser memUser = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        // 请求参数
        PageData pd = this.getPageData();
        // 循环Key
        for (Object key : pd.keySet()) {
            if (pd.getString(key).isEmpty()) {
                return R.parError();
            }
        }
        // 验证交易密码
        String password = DigestUtil.md5Hex(memUser.getPHONE() + pd.getString("pass"));
        if (!password.equals(memUser.getSECURITY_PASSWORD())) {
            return R.error().message("交易密码错误");
        }
        pd.put("ACCOUNT_ID", memUser.getACCOUNT_ID());
        // 验证数额
        double num = Convert.toDouble(pd.get("num"));
        if (num <= 0) {
            return R.parError().message("请输入大于 0 的数");
        }
        // 验证提现范围
        double min = Convert.toDouble(param.get("MIN_CASH"));
        double max = Convert.toDouble(param.get("MAX_CASH"));
        double multiple = Convert.toDouble(param.get("CASH_MULTIPLE"));
        double charge = Convert.toDouble(param.get("CASH_CHARGE"));
        // 查询最新用户信息 取用户usdt余额
        PageData user = accountService.findById(pd);
        double usdt = Convert.toDouble(user.get("USDT_WALLET"));
        if (usdt - charge < num) {
            return R.parError().message("余额不足,最多可提现" + usdt);
        }
        if (num < min) {
            return R.parError().message("最少提现 " + min);
        }
        if (num > max) {
            return R.parError().message("最大提现 " + max);
        }
        if (num % multiple != 0 && multiple > 0) {
            return R.parError().message("请输入 " + multiple + " 的倍数");
        }
        // 减少用户金额
        user.put("money", num);
        accountService.updateUsdt(user, false);
        // 新增一条待审核的提现记录
        addUsdtWalletRecord(memUser.getPHONE(), memUser.getACCOUNT_ID(), Convert.toStr(num), "待审核", Convert.toStr(charge)
                , Convert.toStr(num - charge), "", "", pd.getString("walletAddress"), "提现", "", "-");
        return R.ok().message("等待后台审核");
    }

    /**
     * 功能描述：获取货币互换历史记录 逻辑分页
     *
     * @author Ajie
     * @date 2020/5/27 0027
     */
    @ApiOperation("获取当前登录用户货币互换历史记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "num", value = "页码", required = true),
            @ApiImplicitParam(name = "size", value = "每页多少条数据", required = true),
    })
    @RequestMapping(value = "/getMoneySwapRecord", method = RequestMethod.GET)
    @ResponseBody
    public R getMoneySwapRecord() throws Exception {
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        // 请求出参数
        PageData pd = this.getPageData();
        int num = Convert.toInt(pd.get("num"));
        int size = Convert.toInt(pd.get("size"));
        pd.put("USER_ID", user.getACCOUNT_ID());
        // 获取全部货币互换记录
        List<PageData> listAll = usdt_recordService.listByMoneySwapAndUserId(pd);
        // 执行分页
        Pager<PageData> pager = new Pager<>();
        // 第 N 页
        pager.setCurentPageIndex(num);
        // 每页 N 条
        pager.setCountPerpage(size);
        // 查询到的大集合
        pager.setBigList(listAll);
        // 得到总页数
        int totalPage = pager.getPageCount();
        // 得到小的集合(分页的当前页的记录)
        List<PageData> curPageData = pager.getSmallList();
        pd.put("curPageData", curPageData);
        pd.put("totalPage", totalPage);
        return R.ok().data(pd);
    }

    /**
     * 功能描述：USDT转账
     *
     * @author Ajie
     * @date 2020年5月28日09:18:55
     */
    @RequestMapping(value = "/transferAccount", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("USDT转账")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PHONE", value = "对方手机号", required = true),
            @ApiImplicitParam(name = "num", value = "转账数额", dataType = "Double"),
            @ApiImplicitParam(name = "pass", value = "交易密码"),
    })
    @Limit(name = "限制访问次数", key = "transferAccount", prefix = "J0518", period = 60, count = 10)
    public R transferAccount() throws Exception {
        // 获取系统参数
        PageData param = sys_configService.findById(new PageData());
        if ("0".equals(param.getString("TRANSFER_SWITCH"))) {
            return R.error().message("系统维护中");
        }
        // 请求参数
        PageData pd = this.getPageData();
        // 循环Key
        for (Object key : pd.keySet()) {
            if (pd.getString(key).isEmpty()) {
                return R.parError();
            }
        }
        // 验证手机号格式
        if (!Tools.checkMobileNumber(pd.getString("PHONE"))) {
            return R.error().message("手机号格式错误");
        }
        // 根据对方手机号查询账号是否存在
        PageData heUser = accountService.findByPhone(pd);
        if (heUser == null) {
            return R.error().message("对方账号不存在");
        }
        // 用户信息
        MemUser memUser = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        if (pd.getString("PHONE").equals(memUser.getPHONE())) {
            return R.error().message("不能给自己转账");
        }
        // 验证交易密码
        String password = DigestUtil.md5Hex(memUser.getPHONE() + pd.getString("pass"));
        if (!password.equals(memUser.getSECURITY_PASSWORD())) {
            return R.error().message("交易密码错误");
        }
        // 验证数额
        double num = Convert.toDouble(pd.get("num"));
        if (num < 1) {
            return R.parError().message("请输入大于 0 的数");
        }
        // 验证转账范围
        double min = Convert.toDouble(param.get("MIN_TRANSFER"));
        double max = Convert.toDouble(param.get("MAX_TRANSFER"));
        double multiple = Convert.toDouble(param.get("TRANSFER_MULTIPLE"));
        // 查询最新用户信息 取用户usdt余额
        pd.put("ACCOUNT_ID", memUser.getACCOUNT_ID());
        PageData user = accountService.findById(pd);
        double usdt = Convert.toDouble(user.get("USDT_WALLET"));
        if (num > usdt) {
            return R.error().message("余额不足，最多可转账 " + usdt);
        }
        if (num < min) {
            return R.parError().message("最少转账 " + min);
        }
        if (num > max) {
            return R.parError().message("最大提现 " + max);
        }
        if (num % multiple != 0 && multiple > 0) {
            return R.parError().message("请输入 " + multiple + " 的倍数");
        }
        // 使用事务完成转账操作
        user.put("money", num);
        user.put("HE_PHONE", heUser.get("PHONE"));
        user.put("HE_USER_ID", heUser.get("ACCOUNT_ID"));
        accountService.updateTransfer(user);
        return R.ok().message("转账成功");
    }

    /**
     * 功能描述：获取货币互换历史记录 逻辑分页
     *
     * @author Ajie
     * @date 2020/5/27 0027
     */
    @ApiOperation("获取当前登录用户转账记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "num", value = "页码", required = true),
            @ApiImplicitParam(name = "size", value = "每页多少条数据", required = true),
    })
    @RequestMapping(value = "/getTransferRecord", method = RequestMethod.GET)
    @ResponseBody
    public R getTransferRecord() throws Exception {
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        // 请求出参数
        PageData pd = this.getPageData();
        int num = Convert.toInt(pd.get("num"));
        int size = Convert.toInt(pd.get("size"));
        pd.put("USER_ID", user.getACCOUNT_ID());
        pd.put("DATA_TYPE", "转账");
        // 获取全部转账记录
        List<PageData> listAll = usdt_recordService.listByDataTypeOnUserId(pd);
        // 执行分页
        Pager<PageData> pager = new Pager<>();
        // 第 N 页
        pager.setCurentPageIndex(num);
        // 每页 N 条
        pager.setCountPerpage(size);
        // 查询到的大集合
        pager.setBigList(listAll);
        // 得到总页数
        int totalPage = pager.getPageCount();
        // 得到小的集合(分页的当前页的记录)
        List<PageData> curPageData = pager.getSmallList();
        pd.put("curPageData", curPageData);
        pd.put("totalPage", totalPage);
        return R.ok().data(pd);
    }

    /**
     * 功能描述：申请代理
     *
     * @author Ajie
     * @date 2020年5月28日17:45:45
     */
    @RequestMapping(value = "/applyAgent", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("申请代理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "AGENT_TYPE", value = "代理名称", example = "市级代理", required = true),
            @ApiImplicitParam(name = "voucher", value = "支付凭证图片Url"),
            @ApiImplicitParam(name = "num", value = "支付金额"),
    })
    @Limit(name = "限制访问次数", key = "transferAccount", prefix = "J0518", period = 60, count = 10)
    public R applyAgent() throws Exception {
        // 请求参数
        PageData pd = this.getPageData();
        // 循环Key
        for (Object key : pd.keySet()) {
            if (pd.getString(key).isEmpty()) {
                return R.parError();
            }
        }
        // 用户信息
        MemUser memUser = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        if (pd.getString("AGENT_TYPE").equals(memUser.getUSER_RANK())) {
            return R.error().message("已经是" + pd.get("AGENT_TYPE"));
        }
        // 验证数额
        double num = Convert.toDouble(pd.get("num"));
        if (num < 1) {
            return R.parError().message("请输入大于 0 的数");
        }
        // 创建一条待审核的申请代理记录
        pd.put("PHONE", memUser.getPHONE());
        pd.put("USER_ID", memUser.getACCOUNT_ID());
        pd.put("PAY_VOUCHER", pd.get("voucher"));
        pd.put("PAY_AMOUNT", num);
        pd.put("STATUS", "待审核");
        agent_recordService.save(pd);
        return R.ok().message("等待后台审核");
    }

}


