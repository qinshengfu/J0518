package com.fh.controller.mall;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
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
import com.fh.service.mall.SkuManager;

/** 
 * 说明：单品SKU表
 * 创建人：
 * 创建时间：2020-05-21
 */
@Controller
@RequestMapping(value="/sku")
public class SkuController extends BaseController {
	
	String menuUrl = "sku/list.do"; //菜单地址(权限用)
	@Resource(name="skuService")
	private SkuManager skuService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Sku");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("SKU_ID", this.get32UUID());	//主键
		pd.put("GMT_CREATE", Tools.date2Str(new Date()));	//创建时间
		pd.put("GMT_MODIFIED", Tools.date2Str(new Date()));	//更新时间
		pd.put("USER_ID", "");	//用户id
		pd.put("PROD_ID", "");	//商品ID
		pd.put("PROPERTIES", "");	//销售属性组合字符串 格式是 p1:v1;p2:v2
		pd.put("ORI_PRICE", "0");	//原价
		pd.put("PRICE", "0");	//价格
		pd.put("STOCKS", "0");	//商品在付款减库存的状态下，该sku上未付款的订单数量
		pd.put("ACTUAL_STOCKS", "0");	//实际库存
		pd.put("PIC", "");	//sku图片
		pd.put("SKU_NAME", "");	//sku名称
		pd.put("PROD_NAME", "");	//商品名称
		pd.put("VERSION", "0");	//版本号
		pd.put("STATUS", "");	//状态： 1 启用，0 禁用 
		pd.put("IS_DELETE", "");	//0 正常 1 已被删除
		skuService.save(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除Sku");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		skuService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Sku");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		skuService.edit(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表Sku");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = skuService.list(page);	//列出Sku列表
		mv.setViewName("mall/sku/sku_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
	
	/**去新增页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAdd")
	public ModelAndView goAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("mall/sku/sku_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	public ModelAndView goEdit()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = skuService.findById(pd);	//根据ID读取
		mv.setViewName("mall/sku/sku_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Sku");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			skuService.deleteAll(ArrayDATA_IDS);
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出Sku到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("创建时间");	//1
		titles.add("更新时间");	//2
		titles.add("用户id");	//3
		titles.add("商品ID");	//4
		titles.add("销售属性组合字符串 格式是 p1:v1;p2:v2");	//5
		titles.add("原价");	//6
		titles.add("价格");	//7
		titles.add("商品在付款减库存的状态下，该sku上未付款的订单数量");	//8
		titles.add("实际库存");	//9
		titles.add("sku图片");	//10
		titles.add("sku名称");	//11
		titles.add("商品名称");	//12
		titles.add("版本号");	//13
		titles.add("状态： 1 启用，0 禁用 ");	//14
		titles.add("0 正常 1 已被删除");	//15
		dataMap.put("titles", titles);
		List<PageData> varOList = skuService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("GMT_CREATE"));	    //1
			vpd.put("var2", varOList.get(i).getString("GMT_MODIFIED"));	    //2
			vpd.put("var3", varOList.get(i).getString("USER_ID"));	    //3
			vpd.put("var4", varOList.get(i).getString("PROD_ID"));	    //4
			vpd.put("var5", varOList.get(i).getString("PROPERTIES"));	    //5
			vpd.put("var6", varOList.get(i).get("ORI_PRICE").toString());	//6
			vpd.put("var7", varOList.get(i).get("PRICE").toString());	//7
			vpd.put("var8", varOList.get(i).get("STOCKS").toString());	//8
			vpd.put("var9", varOList.get(i).get("ACTUAL_STOCKS").toString());	//9
			vpd.put("var10", varOList.get(i).getString("PIC"));	    //10
			vpd.put("var11", varOList.get(i).getString("SKU_NAME"));	    //11
			vpd.put("var12", varOList.get(i).getString("PROD_NAME"));	    //12
			vpd.put("var13", varOList.get(i).get("VERSION").toString());	//13
			vpd.put("var14", varOList.get(i).getString("STATUS"));	    //14
			vpd.put("var15", varOList.get(i).getString("IS_DELETE"));	    //15
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
