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
import com.fh.service.record.Delivery_recordManager;

/**
 * 说明：提货记录表
 * 创建人：
 * 创建时间：2020-05-21
 */
@Controller
@RequestMapping(value = "/delivery_record")
public class Delivery_recordController extends BaseController {

    String menuUrl = "delivery_record/list.do"; //菜单地址(权限用)
    @Resource(name = "delivery_recordService")
    private Delivery_recordManager delivery_recordService;

    /**
     * 保存
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/save")
    public ModelAndView save() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "新增Delivery_record");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("DELIVERY_RECORD_ID", this.get32UUID());    //主键
        pd.put("GMT_CREATE", Tools.date2Str(new Date()));    //创建时间
        pd.put("GMT_MODIFIED", Tools.date2Str(new Date()));    //更新时间
        pd.put("PHONE", "0");    //手机号
        pd.put("USER_ID", "");    //用户ID
        pd.put("PROD_NAME", "");    //商品名称
        pd.put("PROD_NUM", "0");    //商品数量
        pd.put("CURRENT_PRICE", "0");    //当前价格
        pd.put("ORDER_NUMBER", "");    //订购流水号
        pd.put("ADDR_ORDER_ID", "");    //用户订单地址Id
        pd.put("FINALLY_TIME", Tools.date2Str(new Date()));    //完成时间
        pd.put("DVY_TIME", Tools.date2Str(new Date()));    //发货时间
        delivery_recordService.save(pd);
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
        logBefore(logger, Jurisdiction.getUsername() + "删除Delivery_record");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
            return;
        } //校验权限
        PageData pd = new PageData();
        pd = this.getPageData();
        delivery_recordService.delete(pd);
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
        logBefore(logger, Jurisdiction.getUsername() + "修改Delivery_record");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        PageData prod = delivery_recordService.findById(pd);
        if ("待发货".equals(prod.getString("STATUS"))) {
            pd.put("STATUS", "待收货");
            pd.put("DVY_TIME", DateUtil.now());
        }
        delivery_recordService.edit(pd);
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
        logBefore(logger, Jurisdiction.getUsername() + "列表Delivery_record");
        //if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String keywords = pd.getString("keywords");                //关键词检索条件
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }
        page.setPd(pd);
        List<PageData> varList = delivery_recordService.list(page);    //列出Delivery_record列表
        mv.setViewName("record/delivery_record/delivery_record_list");
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
        mv.setViewName("record/delivery_record/delivery_record_edit");
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
        pd = delivery_recordService.findById(pd);    //根据ID读取
        mv.setViewName("record/delivery_record/delivery_record_edit");
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
        logBefore(logger, Jurisdiction.getUsername() + "批量删除Delivery_record");
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
            delivery_recordService.deleteAll(ArrayDATA_IDS);
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
        logBefore(logger, Jurisdiction.getUsername() + "导出Delivery_record到excel");
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
        titles.add("商品名称");    //5
        titles.add("商品数量");    //6
        titles.add("当前价格");    //7
        titles.add("状态");    //8
        titles.add("物流单号");    //9
        titles.add("订购流水号");    //10
        titles.add("用户订单地址Id");    //11
        titles.add("完成时间");    //12
        titles.add("发货时间");    //13
        dataMap.put("titles", titles);
        List<PageData> varOList = delivery_recordService.listAll(pd);
        List<PageData> varList = new ArrayList<PageData>();
        for (int i = 0; i < varOList.size(); i++) {
            PageData vpd = new PageData();
            vpd.put("var1", varOList.get(i).getString("GMT_CREATE"));        //1
            vpd.put("var2", varOList.get(i).getString("GMT_MODIFIED"));        //2
            vpd.put("var3", varOList.get(i).get("PHONE").toString());    //3
            vpd.put("var4", varOList.get(i).getString("USER_ID"));        //4
            vpd.put("var5", varOList.get(i).getString("PROD_NAME"));        //5
            vpd.put("var6", varOList.get(i).get("PROD_NUM").toString());    //6
            vpd.put("var7", varOList.get(i).get("CURRENT_PRICE").toString());    //7
            vpd.put("var8", varOList.get(i).getString("STATUS"));        //8
            vpd.put("var9", varOList.get(i).getString("DVY_FLOW_ID"));        //9
            vpd.put("var10", varOList.get(i).getString("ORDER_NUMBER"));        //10
            vpd.put("var11", varOList.get(i).getString("ADDR_ORDER_ID"));        //11
            vpd.put("var12", varOList.get(i).getString("FINALLY_TIME"));        //12
            vpd.put("var13", varOList.get(i).getString("DVY_TIME"));        //13
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
