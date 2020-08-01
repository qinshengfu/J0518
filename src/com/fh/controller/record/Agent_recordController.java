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

import cn.hutool.core.date.DateUtil;
import com.fh.controller.front.BaseFrontController;
import com.fh.entity.result.R;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
import com.fh.service.record.Agent_recordManager;

/**
 * 说明：申请代理记录表
 * 创建人：
 * 创建时间：2020-05-21
 */
@Controller
@RequestMapping(value = "/agent_record")
public class Agent_recordController extends BaseFrontController {

    String menuUrl = "agent_record/list.do"; //菜单地址(权限用)

    /**
     * 保存
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/save")
    public ModelAndView save() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "新增Agent_record");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("AGENT_RECORD_ID", this.get32UUID());    //主键
        pd.put("GMT_CREATE", Tools.date2Str(new Date()));    //创建时间
        pd.put("GMT_MODIFIED", Tools.date2Str(new Date()));    //更新时间
        pd.put("PHONE", "0");    //手机号
        pd.put("USER_ID", "");    //用户ID
        pd.put("AGENT_TYPE", "");    //代理类型
        pd.put("PAY_VOUCHER", "");    //支付凭证
        pd.put("PAY_AMOUNT", "0");    //支付金额
        agent_recordService.save(pd);
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }

    /**
     * 驳回
     *
     * @throws Exception
     */
    @RequestMapping(value = "/approved")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public R approved() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "申请代理-通过审核");
        // 校验权限
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return R.error().message("您没有权限");
        }
        PageData pd = this.getPageData();
        pd = agent_recordService.findById(pd);
        if (!"待审核".equals(pd.getString("STATUS"))) {
            return R.error().message("订单已完成或已驳回");
        }
        // 修改用户等级
        PageData map = new PageData();
        map.put("ACCOUNT_ID", pd.getString("USER_ID"));
        map.put("USER_RANK", pd.getString("AGENT_TYPE"));
        accountService.edit(map);
        // 更新订单状态
        pd.put("STATUS", "已完成");
        agent_recordService.edit(pd);
        return R.ok().message("审核成功");
    }

    /**
     * 驳回
     *
     * @throws Exception
     */
    @RequestMapping(value = "/reject")
    @ResponseBody
    public R reject() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "申请代理-驳回");
        // 校验权限
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return R.error().message("您没有权限");
        }
        PageData pd = this.getPageData();
        pd = agent_recordService.findById(pd);
        if (!"待审核".equals(pd.getString("STATUS"))) {
            return R.error().message("订单已完成或已驳回");
        }
        // 更新订单状态
        pd.put("STATUS", "驳回");
        agent_recordService.edit(pd);
        return R.ok().message("驳回成功");
    }


    /**
     * 列表
     *
     * @param page
     * @throws Exception
     */
    @RequestMapping(value = "/list")
    public ModelAndView list(Page page) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "列表Agent_record");
        //if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String keywords = pd.getString("keywords");                //关键词检索条件
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }
        page.setPd(pd);
        List<PageData> varList = agent_recordService.list(page);    //列出Agent_record列表
        mv.setViewName("record/agent_record/agent_record_list");
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
        mv.setViewName("record/agent_record/agent_record_edit");
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
        pd = agent_recordService.findById(pd);    //根据ID读取
        mv.setViewName("record/agent_record/agent_record_edit");
        mv.addObject("msg", "edit");
        mv.addObject("pd", pd);
        return mv;
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
        logBefore(logger, Jurisdiction.getUsername() + "批量删除Agent_record");
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
            agent_recordService.deleteAll(ArrayDATA_IDS);
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
        logBefore(logger, Jurisdiction.getUsername() + "导出Agent_record到excel");
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
        titles.add("代理类型");    //5
        titles.add("支付凭证");    //6
        titles.add("支付金额");    //7
        titles.add("状态");    //8
        dataMap.put("titles", titles);
        List<PageData> varOList = agent_recordService.listAll(pd);
        List<PageData> varList = new ArrayList<PageData>();
        for (int i = 0; i < varOList.size(); i++) {
            PageData vpd = new PageData();
            vpd.put("var1", varOList.get(i).getString("GMT_CREATE"));        //1
            vpd.put("var2", varOList.get(i).getString("GMT_MODIFIED"));        //2
            vpd.put("var3", varOList.get(i).get("PHONE").toString());    //3
            vpd.put("var4", varOList.get(i).getString("USER_ID"));        //4
            vpd.put("var5", varOList.get(i).getString("AGENT_TYPE"));        //5
            vpd.put("var6", varOList.get(i).getString("PAY_VOUCHER"));        //6
            vpd.put("var7", varOList.get(i).get("PAY_AMOUNT").toString());    //7
            vpd.put("var8", varOList.get(i).getString("STATUS"));        //8
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
