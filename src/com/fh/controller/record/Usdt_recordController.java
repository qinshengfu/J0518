package com.fh.controller.record;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import cn.hutool.core.convert.Convert;
import com.fh.controller.front.BaseFrontController;
import com.fh.entity.result.R;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.util.AppUtil;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.Jurisdiction;
import com.fh.util.Tools;
import com.fh.service.record.Usdt_recordManager;

/**
 * 说明：usdt钱包记录
 * 创建人：
 * 创建时间：2020-05-21
 */
@Controller
@RequestMapping(value = "/usdt_record")
public class Usdt_recordController extends BaseFrontController {

    String menuUrl = "usdt_record/list.do"; //菜单地址(权限用)

    /**
     * 保存
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/save")
    public ModelAndView save() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "新增Usdt_record");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("USDT_RECORD_ID", this.get32UUID());    //主键
        pd.put("GMT_CREATE", Tools.date2Str(new Date()));    //创建时间
        pd.put("GMT_MODIFIED", Tools.date2Str(new Date()));    //更新时间
        pd.put("PHONE", "0");    //手机号
        pd.put("USER_ID", "");    //用户ID
        pd.put("MONEY", "0");    //金额
        pd.put("CHARGE", "0");    //手续费
        pd.put("ACTUAL_RECEIPT", "0");    //实际到账
        pd.put("HE_PHONE", "0");    //对方手机号
        pd.put("HE_USER_ID", "");    //对方用户ID
        pd.put("WALLET_ADDRESS", "");    //钱包地址
        pd.put("VERSION", "0");    //版本号
        usdt_recordService.save(pd);
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
        logBefore(logger, Jurisdiction.getUsername() + "删除Usdt_record");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
            return;
        } //校验权限
        PageData pd = new PageData();
        pd = this.getPageData();
        usdt_recordService.delete(pd);
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
        logBefore(logger, Jurisdiction.getUsername() + "修改Usdt_record");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        usdt_recordService.edit(pd);
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
        logBefore(logger, Jurisdiction.getUsername() + "列表Usdt_record");
        //if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        String keywords = pd.getString("keywords");                //关键词检索条件
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }
        page.setPd(pd);
        List<PageData> varList = usdt_recordService.list(page);    //列出Usdt_record列表
        mv.setViewName("record/usdt_record/usdt_record_list");
        mv.addObject("varList", varList);
        mv.addObject("pd", pd);
        mv.addObject("QX", Jurisdiction.getHC());    //按钮权限
        return mv;
    }

    /**
     * 充值列表
     *
     * @param page
     * @throws Exception
     */
    @RequestMapping(value = "/rechargeList")
    public ModelAndView rechargeList(Page page) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "充值列表Usdt_record");
        //if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        String keywords = pd.getString("keywords");                //关键词检索条件
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }
        pd.put("DATA_TYPE", "充值");
        page.setPd(pd);
        List<PageData> varList = usdt_recordService.listByType(page);    //列出Usdt_record列表
        mv.setViewName("record/usdt_record/usdt_recharge_list");
        mv.addObject("varList", varList);
        mv.addObject("pd", pd);
        mv.addObject("QX", Jurisdiction.getHC());    //按钮权限
        return mv;
    }

    /**
     * 提现列表
     *
     * @param page
     * @throws Exception
     */
    @RequestMapping(value = "/withdrawalList")
    public ModelAndView withdrawalList(Page page) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "提现列表Usdt_record");
        //if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        String keywords = pd.getString("keywords");                //关键词检索条件
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }
        pd.put("DATA_TYPE", "提现");
        page.setPd(pd);
        List<PageData> varList = usdt_recordService.listByType(page);    //列出Usdt_record列表
        mv.setViewName("record/usdt_record/usdt_withdrawal_list");
        mv.addObject("varList", varList);
        mv.addObject("pd", pd);
        mv.addObject("QX", Jurisdiction.getHC());    //按钮权限
        return mv;
    }

    /**
     * 充值 通过审核
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/rechargeApproved")
	@ResponseBody
    public R rechargeApproved() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "通过审核Usdt_record");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        } //校验权限
        PageData pd = this.getPageData();
        pd = usdt_recordService.findById(pd);
        if (!"待审核".equals(pd.getString("STATUS"))) {
        	return R.error().message("该订单已驳回或已完成");
		}
        // 更改订单状态和增加用户usdt用事务完成
        usdt_recordService.rechargeEdit(pd, true);
		return R.ok().message("审核成功");
    }

    /**
     * 充值 驳回
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/rechargeRejection")
	@ResponseBody
    public R rechargeRejection() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "驳回Usdt_record");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        } //校验权限
        PageData pd = this.getPageData();
        pd = usdt_recordService.findById(pd);
        if (!"待审核".equals(pd.getString("STATUS"))) {
			return R.error().message("该订单已驳回或已完成");
		}
        // 更改订单状态
        usdt_recordService.rechargeEdit(pd, false);
		return R.ok().message("驳回成功");
    }

    /**
     * 提现 通过审核
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/withdrawalApproved")
	@ResponseBody
    public R withdrawalApproved() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "通过审核Usdt_record");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        } //校验权限
        PageData pd = this.getPageData();
        pd = usdt_recordService.findById(pd);
        if (!"待审核".equals(pd.getString("STATUS"))) {
        	return R.error().message("该订单已驳回或已完成");
		}
        // 更改订单状态
        usdt_recordService.withdrawalEdit(pd, true);
		return R.ok().message("审核成功");
    }

    /**
     * 提现 驳回
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/withdrawalRejection")
	@ResponseBody
    public R withdrawalRejection() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "驳回Usdt_record");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        } //校验权限
        PageData pd = this.getPageData();
        pd = usdt_recordService.findById(pd);
        if (!"待审核".equals(pd.getString("STATUS"))) {
			return R.error().message("该订单已驳回或已完成");
		}
        // 更改订单状态和增加用户usdt用事务完成
        usdt_recordService.withdrawalEdit(pd, false);
		return R.ok().message("驳回成功");
    }

    /**
     * 批量删除
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/deleteAll")
    @ResponseBody
    public Object deleteAll() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "批量删除Usdt_record");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
            return null;
        } //校验权限
        PageData pd = new PageData();
        Map<String, Object> map = new HashMap<String, Object>();
        pd = this.getPageData();
        List<PageData> pdList = new ArrayList<PageData>();
        String DATA_IDS = pd.getString("DATA_IDS");
        if (null != DATA_IDS && !"".equals(DATA_IDS)) {
            String ArrayDATA_IDS[] = DATA_IDS.split(",");
            usdt_recordService.deleteAll(ArrayDATA_IDS);
            pd.put("msg", "ok");
        } else {
            pd.put("msg", "no");
        }
        pdList.add(pd);
        map.put("list", pdList);
        return AppUtil.returnObject(pd, map);
    }

    /**
     * 导出到excel
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/excel")
    public ModelAndView exportExcel() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "导出Usdt_record到excel");
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
        titles.add("用户ID");    //4
        titles.add("金额");    //5
        titles.add("状态");    //6
        titles.add("手续费");    //7
        titles.add("实际到账");    //8
        titles.add("对方手机号");    //9
        titles.add("对方用户ID");    //10
        titles.add("钱包地址");    //11
        titles.add("版本号");    //12
        dataMap.put("titles", titles);
        List<PageData> varOList = usdt_recordService.listAll(pd);
        List<PageData> varList = new ArrayList<PageData>();
        for (int i = 0; i < varOList.size(); i++) {
            PageData vpd = new PageData();
            vpd.put("var1", varOList.get(i).getString("GMT_CREATE"));        //1
            vpd.put("var2", varOList.get(i).getString("GMT_MODIFIED"));        //2
            vpd.put("var3", varOList.get(i).get("PHONE").toString());    //3
            vpd.put("var4", varOList.get(i).getString("USER_ID"));        //4
            vpd.put("var5", varOList.get(i).get("MONEY").toString());    //5
            vpd.put("var6", varOList.get(i).getString("STATUS"));        //6
            vpd.put("var7", varOList.get(i).get("CHARGE").toString());    //7
            vpd.put("var8", varOList.get(i).get("ACTUAL_RECEIPT").toString());    //8
            vpd.put("var9", varOList.get(i).get("HE_PHONE").toString());    //9
            vpd.put("var10", varOList.get(i).getString("HE_USER_ID"));        //10
            vpd.put("var11", varOList.get(i).getString("WALLET_ADDRESS"));        //11
            vpd.put("var12", varOList.get(i).get("VERSION").toString());    //12
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
