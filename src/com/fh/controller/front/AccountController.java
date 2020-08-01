package com.fh.controller.front;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.crypto.digest.DigestUtil;
import com.fh.entity.MemUser;
import com.fh.entity.result.R;
import com.fh.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.codehaus.jackson.smile.Tool;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.service.front.AccountManager;

/**
 * 说明：用户表
 * 创建人：
 * 创建时间：2020-05-21
 */
@Controller
@RequestMapping(value = "/account")
public class AccountController extends BaseFrontController {

    String menuUrl = "account/list.do"; //菜单地址(权限用)

    /**
     * 功能描述：后台直接登录前台用户
     *
     * @author Ajie
     * @date 2020/3/30 0030
     */
    @RequestMapping(value = "adminLogin")
    @ResponseBody
    public R adminLogin() throws Exception {
        // 校验权限
        if (!Jurisdiction.buttonJurisdiction("account/list.do", "edit")) {
            return R.error().message("您沒有权限");
        }
        PageData pd = this.getPageData();
        MemUser user = accountService.findByPhoneAndPass(pd);
        if (user == null) {
            return R.error().message("非法操作！用户不存在");
        }
        addUserLogin(user);
        FHLOG.save(Jurisdiction.getUsername(), "登录：" + user.getPHONE());
        return R.ok();
    }

    /**
     * 保存
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/save")
    public ModelAndView save() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "新增Account");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("ACCOUNT_ID", this.get32UUID());    //主键
        pd.put("GMT_CREATE", Tools.date2Str(new Date()));    //创建时间
        pd.put("GMT_MODIFIED", Tools.date2Str(new Date()));    //更新时间
        pd.put("PHONE", "0");    //手机号
        pd.put("RECOMMENDER_ID", "");    //推荐人ID
        pd.put("RECOMMENDER", "");    //推荐人
        pd.put("RE_PATH", "");    //推荐路径
        pd.put("PROVINCE_ID", "");    //省ID
        pd.put("PROVINCE", "");    //省
        pd.put("CITY_ID", "");    //城市ID
        pd.put("CITY", "");    //城市
        pd.put("AREA_ID", "");    //区ID
        pd.put("AREA", "");    //区
        accountService.save(pd);
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }

    /**
     * 删除
     *
     * @param out
     * @throws Exception
     */
    @RequestMapping(value = "/delete")
    public void delete(PrintWriter out) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "删除Account");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
            return;
        } //校验权限
        PageData pd = new PageData();
        pd = this.getPageData();
        accountService.delete(pd);
        out.write("success");
        out.close();
    }

    /**
     * 修改
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/edit")
    public ModelAndView edit() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "修改Account");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        String phone = pd.get("PHONE").toString();
        if (Tools.notEmpty(pd.getString("LOGIN_PASSWORD"))) {
            // 密码加密
            String loginPass = pd.getString("LOGIN_PASSWORD");
            pd.put("LOGIN_PASSWORD", DigestUtil.md5Hex(phone + loginPass));
        } else {
            pd.remove("LOGIN_PASSWORD");
        }
        if (Tools.notEmpty(pd.getString("SECURITY_PASSWORD"))) {
            // 密码加密
            String securityPassword = pd.getString("SECURITY_PASSWORD");
            pd.put("SECURITY_PASSWORD", DigestUtil.md5Hex(phone + securityPassword));
        } else {
            pd.remove("SECURITY_PASSWORD");
        }
        accountService.edit(pd);
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }

    /**
     * 列表
     *
     * @param page
     * @throws Exception
     */
    @RequestMapping(value = "/list")
    public ModelAndView list(Page page) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "列表Account");
        //if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String keywords = pd.getString("keywords");                //关键词检索条件
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }
        page.setPd(pd);
        List<PageData> varList = accountService.list(page);    //列出Account列表
        mv.setViewName("front/account/account_list");
        mv.addObject("varList", varList);
        mv.addObject("pd", pd);
        mv.addObject("QX", Jurisdiction.getHC());    //按钮权限
        return mv;
    }


    /**
     * 财务汇总列表
     *
     * @param page
     * @throws Exception
     */
    @RequestMapping(value = "/financialSummaryList")
    public ModelAndView financialSummaryList(Page page) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "列表Usdt_record");
        //if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        String lastStart = pd.getString("lastStart");
        if (Tools.isEmpty(lastStart)) {
            pd.put("lastStart", DateUtil.today());
        }
        page.setPd(pd);
        List<PageData> varList = accountService.listByFinancialSummary(page);
        mv.setViewName("record/financialSummary");
        mv.addObject("varList", varList);
        mv.addObject("pd", pd);
        mv.addObject("QX", Jurisdiction.getHC());    //按钮权限
        return mv;
    }

    /**
     * 去新增页面
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/goAdd")
    public ModelAndView goAdd() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        mv.setViewName("front/account/account_edit");
        mv.addObject("msg", "save");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 去修改页面
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/goEdit")
    public ModelAndView goEdit() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd = accountService.findById(pd);    //根据ID读取
        mv.setViewName("front/account/account_edit");
        mv.addObject("msg", "edit");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 用户组织架构图
     */
    @RequestMapping(value = "/userChart")
    public ModelAndView userChart() {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("front/account/userChart");
        return mv;
    }

    /**
     * 功能描述：获取所有用户列表
     *
     * @author Ajie
     * @date 2020年6月17日17:24:21
     */
    @RequestMapping(value = "getUserList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取所有用户列表")
    public R getUserList() throws Exception {
        // 查询所有用户信息
        List<PageData> userList = accountService.listAll(new PageData());
        for (PageData user : userList) {
            // 查询个人持有的股票数量
            user.put("USER_SHARES_SUM", shares_orderService.findByPhoneSharesHeldNum(user).get("USER_SHARES_SUM"));
            // 查询团队每天的交易额、持有股票商品的数量


            // 查询团队每天的出金量（提现金额）、入金量（充值金额）

        }
        return R.ok().data("item", userList);
    }

    /**
     * 导出到excel
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/excel")
    public ModelAndView exportExcel() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "导出Account到excel");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "cha")) {
            return null;
        }
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<String> titles = new ArrayList<String>();
        titles.add("创建时间");    //1
        titles.add("更新时间");    //2
        titles.add("手机号");    //3
        titles.add("登录密码");    //4
        titles.add("安全密码");    //5
        titles.add("USDT钱包");    //6
        titles.add("金额");    //7
        titles.add("商城积分");    //8
        titles.add("推荐人数");    //9
        titles.add("团队人数");    //10
        titles.add("推荐人ID");    //11
        titles.add("推荐人");    //12
        titles.add("推荐路径");    //13
        titles.add("代数");    //14
        titles.add("会员等级");    //15
        titles.add("用户状态 1:正常、0：封号");    //16
        titles.add("省ID");    //17
        titles.add("省");    //18
        titles.add("城市ID");    //19
        titles.add("城市");    //20
        titles.add("区ID");    //21
        titles.add("区");    //22
        titles.add("我的地址");    //23
        titles.add("是否特殊账号 1:正常、0：特殊");    //24
        titles.add("是否实名认证 1:是、0：否");    //25
        dataMap.put("titles", titles);
        List<PageData> varOList = accountService.listAll(pd);
        List<PageData> varList = new ArrayList<PageData>();
        for (int i = 0; i < varOList.size(); i++) {
            PageData vpd = new PageData();
            vpd.put("var1", varOList.get(i).getString("GMT_CREATE"));        //1
            vpd.put("var2", varOList.get(i).getString("GMT_MODIFIED"));        //2
            vpd.put("var3", varOList.get(i).get("PHONE").toString());    //3
            vpd.put("var4", varOList.get(i).getString("LOGIN_PASSWORD"));        //4
            vpd.put("var5", varOList.get(i).getString("SECURITY_PASSWORD"));        //5
            vpd.put("var6", varOList.get(i).get("USDT_WALLET").toString());    //6
            vpd.put("var7", varOList.get(i).get("MONEY").toString());    //7
            vpd.put("var8", varOList.get(i).get("SHOP_INTEGRAL").toString());    //8
            vpd.put("var9", varOList.get(i).get("RECOMMENDED_NUMBER").toString());    //9
            vpd.put("var10", varOList.get(i).get("TEAM_NUMBER").toString());    //10
            vpd.put("var11", varOList.get(i).getString("RECOMMENDER_ID"));        //11
            vpd.put("var12", varOList.get(i).getString("RECOMMENDER"));        //12
            vpd.put("var13", varOList.get(i).getString("RE_PATH"));        //13
            vpd.put("var14", varOList.get(i).get("ALGEBRA").toString());    //14
            vpd.put("var15", varOList.get(i).getString("USER_RANK"));        //15
            vpd.put("var16", varOList.get(i).getString("USER_STATE"));        //16
            vpd.put("var17", varOList.get(i).getString("PROVINCE_ID"));        //17
            vpd.put("var18", varOList.get(i).getString("PROVINCE"));        //18
            vpd.put("var19", varOList.get(i).getString("CITY_ID"));        //19
            vpd.put("var20", varOList.get(i).getString("CITY"));        //20
            vpd.put("var21", varOList.get(i).getString("AREA_ID"));        //21
            vpd.put("var22", varOList.get(i).getString("AREA"));        //22
            vpd.put("var23", varOList.get(i).getString("MY_ADDRESS"));        //23
            vpd.put("var24", varOList.get(i).getString("IS_SPECIAL"));        //24
            vpd.put("var25", varOList.get(i).getString("IS_REAL"));        //25
            varList.add(vpd);
        }
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView();
        mv = new ModelAndView(erv, dataMap);
        return mv;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
    }
}
