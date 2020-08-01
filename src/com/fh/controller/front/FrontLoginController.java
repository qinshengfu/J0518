package com.fh.controller.front;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrSpliter;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.fh.entity.MemUser;
import com.fh.entity.result.R;
import com.fh.service.system.FHlogManager;
import com.fh.util.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.hamcrest.core.Is;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 说明：前台用户登录
 * 创建人：Ajie
 * 创建时间：2020年3月27日10:59:20
 */
@Controller
@Slf4j
@RequestMapping(value = "release")
public class FrontLoginController extends BaseFrontController {


    /**
     * 功能描述：访问注册页
     *
     * @author Ajie
     * @date 2020年5月22日17:40:45
     */
    @RequestMapping(value = "/to_register")
    public ModelAndView toRegister() {
        PageData pd = this.getPageData();
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("front/login/register");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 功能描述：访问登录页
     *
     * @author Ajie
     * @date 2020年5月22日17:42:06
     */
    @RequestMapping(value = "/to_login")
    public ModelAndView toFrontLogin() {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("front/login/login");
        return mv;
    }

    /**
     * 功能描述：访问忘记密码页
     *
     * @author Ajie
     * @date 2020年5月22日17:42:11
     */
    @RequestMapping(value = "/to_forgetpass")
    public ModelAndView toForgetpass() {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("front/login/forgetpass");
        return mv;
    }

    /**
     * 功能描述：请求登录
     *
     * @return 验证结果
     * @author Ajie
     * @date 2020年5月22日17:58:28
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PHONE", value = "手机号", required = true),
            @ApiImplicitParam(name = "loginPass", value = "登录密码", required = true),
    })
    public R login() throws Exception {
        // 获取系统参数
        PageData param = sys_configService.findById(new PageData());
        // 系统是否关闭登录
        if ("0".equals(param.getString("LOGIN_SWITCH"))) {
            return R.error().message("系统维护中");
        }
        // 获取请求参数
        PageData pd = this.getPageData();
        if (pd.size() != 2) {
            return R.parError();
        }
        // 验证手机号格式
        if (!Tools.checkMobileNumber(pd.getString("PHONE"))) {
            return R.error().message("手机号格式错误");
        }
        // 密码加密
        String passwrod = pd.getString("loginPass");
        pd.put("LOGIN_PASSWORD", DigestUtil.md5Hex(pd.get("PHONE").toString() + passwrod));
        // 根据用户名和密码开始校验
        MemUser user = accountService.findByPhoneAndPass(pd);
        // 账号或密码错误
        if (user == null) {
            return R.error().message("账号或密码错误");
        }
        // 账号被封禁
        if (user.getUSER_STATE() == 0) {
            return R.error().message("账号被封禁");
        }
        // 存入session 和 登录序列中
        addUserLogin(user);
        return R.ok();
    }


    /**
     * 功能描述：请求注册
     *
     * @author Ajie
     * @date 2020年5月22日17:59:17
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "region", value = "地区(省市区以【,】号分割)", example = "省,市,区", required = true),
            @ApiImplicitParam(name = "phone", value = "手机号", required = true),
            @ApiImplicitParam(name = "loginPass", value = "登录密码", required = true),
            @ApiImplicitParam(name = "securPass", value = "交易密码", required = true),
            @ApiImplicitParam(name = "question", value = "密保问题(根据页面选择框内容输入)", required = true),
            @ApiImplicitParam(name = "answer", value = "密保答案", required = true),
            @ApiImplicitParam(name = "inviter", value = "推荐码(填写用户ID)", required = true),
    })
    public R register() throws Exception {
        // 查询系统参数 验证是否开放注册功能
        PageData param = sys_configService.findById(new PageData());
        if ("0".equals(param.getString("REGISTER_SWITCH"))) {
            return R.error().message("系统未开放注册功能");
        }
        // 获取请求参数
        PageData pd = this.getPageData();
        // 循环Key
        for (Object key : pd.keySet()) {
            if (pd.getString(key).isEmpty()) {
                return R.parError();
            }
        }
        // 验证手机号格式
        if (!Tools.checkMobileNumber(pd.getString("phone"))) {
            return R.error().message("手机号格式错误");
        }
        // 验证手机号是否被注册
        pd.put("PHONE", pd.getString("phone"));
        PageData user = accountService.findByPhone(pd);
        if (user != null) {
            return R.error().message("用户已被注册");
        }
        // 验证推荐人是否存在
        String userid = pd.getString("inviter");
        pd.put("ACCOUNT_ID", userid);
        user = accountService.findById(pd);
        if (user == null) {
            return R.error().message("邀请码不存在");
        }
        // 保存到数据库中
        saveUser(pd, user);
        return R.ok();
    }

    /**
     * 功能描述：新增用户写入数据库
     *
     * @param pd   用户信息
     * @param user 推荐人信息
     * @author Ajie
     * @date 2020-5-23 16:17:47
     */
    public void saveUser(PageData pd, PageData user) throws Exception {
        // 切割省市区            //参数：被切分字符串，分隔符逗号，0表示无限制分片数，去除两边空格，忽略空白项
        List<String> region = StrSpliter.split(pd.getString("region"), ",", 0, true, true);
        PageData map = new PageData();
        // 定义新注册用户的推荐关系路径
        String path = Tools.isEmpty(user.getString("RE_PATH")) ? user.getString("ACCOUNT_ID") : user.getString("RE_PATH") + "," + user.getString("ACCOUNT_ID");
        map.put("GMT_CREATE", DateUtil.now());
        map.put("GMT_MODIFIED", DateUtil.now());
        map.put("PHONE", pd.getString("phone"));
        map.put("LOGIN_PASSWORD", DigestUtil.md5Hex(pd.getString("phone") + pd.getString("loginPass")));
        map.put("SECURITY_PASSWORD", DigestUtil.md5Hex(pd.getString("phone") + pd.getString("securPass")));
        map.put("USDT_WALLET", 0);
        map.put("MONEY", 0);
        map.put("SHOP_INTEGRAL", 0);
        map.put("RECOMMENDED_NUMBER", 0);
        map.put("TEAM_NUMBER", 0);
        map.put("RECOMMENDER_ID", user.getString("ACCOUNT_ID"));
        map.put("RECOMMENDER", user.get("PHONE"));
        map.put("RE_PATH", path);
        map.put("ALGEBRA", Convert.toInt(user.get("ALGEBRA")) + 1);
        map.put("USER_RANK", "普通会员");
        map.put("USER_STATE", 1);
        map.put("PROVINCE_ID", "");
        map.put("PROVINCE", region.get(0));
        map.put("CITY_ID", "");
        map.put("CITY", region.get(1));
        map.put("AREA_ID", "");
        map.put("AREA", region.get(2));
        map.put("MY_ADDRESS", pd.getString("region"));
        map.put("IS_SPECIAL", 1);
        map.put("IS_REAL", 0);
        // 密保问题、答案
        map.put("SECURITY_QUESTION", pd.getString("question"));
        map.put("SECURITY_ANSWER", pd.getString("answer"));
        // 存入数据库
        accountService.save(map);
        // 更新推荐人 推荐数量、团队数量
        user.put("GMT_MODIFIED", DateUtil.now());
        accountService.updateReAndTeamNumber(user);
    }

    /**
     * 功能描述：请求找回密码
     *
     * @author Ajie
     * @date 2020/5/25 0025
     */
    @RequestMapping(value = "retrievePassword", method = RequestMethod.POST)
    @ResponseBody
    public R retrievePassword() throws Exception {
        // 请求参数
        PageData pd = this.getPageData();
        if (pd.size() != 5) {
            return R.parError();
        }
        // 验证是否有此用户
        PageData user = accountService.findByPhone(pd);
        if (user.isEmpty()) {
            return R.parError().message("用户不存在");
        }
        // 验证密保问题
        boolean isConsistent = pd.getString("question").equals(user.getString("SECURITY_QUESTION"))
                && pd.getString("answer").equals(user.getString("SECURITY_ANSWER"));
        if (!isConsistent) {
            return R.error().message("密保错误");
        }
        // 验证密码是否一致
        if (!pd.getString("newPass").equals(pd.getString("confirmPass"))) {
            return R.error().message("密码不一致");
        }
        // 执行密码修改
        PageData map = new PageData();
        map.put("ACCOUNT_ID", user.getString("ACCOUNT_ID"));
        map.put("GMT_CREATE", DateUtil.now());
        map.put("LOGIN_PASSWORD", DigestUtil.md5Hex(pd.getString("PHONE") + pd.getString("newPass")));
        accountService.edit(map);
        return R.ok();
    }


}
