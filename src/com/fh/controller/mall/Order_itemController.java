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
import com.fh.service.mall.Order_itemManager;

/** 
 * 说明：订单项
 * 创建人：
 * 创建时间：2020-05-21
 */
@Controller
@RequestMapping(value="/order_item")
public class Order_itemController extends BaseController {
	
	String menuUrl = "order_item/list.do"; //菜单地址(权限用)
	@Resource(name="order_itemService")
	private Order_itemManager order_itemService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Order_item");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("ORDER_ITEM_ID", this.get32UUID());	//主键
		pd.put("GMT_CREATE", Tools.date2Str(new Date()));	//创建时间
		pd.put("GMT_MODIFIED", Tools.date2Str(new Date()));	//更新时间
		pd.put("SHOP_ID", "");	//店铺id
		pd.put("ORDER_NUMBER", "");	//订单号
		pd.put("PROD_ID", "");	//产品ID
		pd.put("SKU_ID", "");	//产品SkuID
		pd.put("PROD_COUNT", "0");	//购物车产品个数
		pd.put("PROD_NAME", "");	//产品名称
		pd.put("SKU_NAME", "");	//sku名称
		pd.put("PIC", "");	//产品主图片路径
		pd.put("PRICE", "0");	//产品价格
		pd.put("USER_ID", "");	//用户id
		pd.put("PRODUCT_TOTAL_AMOUNT", "0");	//商品总金额
		pd.put("BASKET_DATE", Tools.date2Str(new Date()));	//加入购物车时间
		order_itemService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除Order_item");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		order_itemService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Order_item");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		order_itemService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表Order_item");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = order_itemService.list(page);	//列出Order_item列表
		mv.setViewName("mall/order_item/order_item_list");
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
		mv.setViewName("mall/order_item/order_item_edit");
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
		pd = order_itemService.findById(pd);	//根据ID读取
		mv.setViewName("mall/order_item/order_item_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Order_item");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			order_itemService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出Order_item到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("创建时间");	//1
		titles.add("更新时间");	//2
		titles.add("店铺id");	//3
		titles.add("订单号");	//4
		titles.add("产品ID");	//5
		titles.add("产品SkuID");	//6
		titles.add("购物车产品个数");	//7
		titles.add("产品名称");	//8
		titles.add("sku名称");	//9
		titles.add("产品主图片路径");	//10
		titles.add("产品价格");	//11
		titles.add("用户id");	//12
		titles.add("商品总金额");	//13
		titles.add("加入购物车时间");	//14
		dataMap.put("titles", titles);
		List<PageData> varOList = order_itemService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("GMT_CREATE"));	    //1
			vpd.put("var2", varOList.get(i).getString("GMT_MODIFIED"));	    //2
			vpd.put("var3", varOList.get(i).getString("SHOP_ID"));	    //3
			vpd.put("var4", varOList.get(i).getString("ORDER_NUMBER"));	    //4
			vpd.put("var5", varOList.get(i).getString("PROD_ID"));	    //5
			vpd.put("var6", varOList.get(i).getString("SKU_ID"));	    //6
			vpd.put("var7", varOList.get(i).get("PROD_COUNT").toString());	//7
			vpd.put("var8", varOList.get(i).getString("PROD_NAME"));	    //8
			vpd.put("var9", varOList.get(i).getString("SKU_NAME"));	    //9
			vpd.put("var10", varOList.get(i).getString("PIC"));	    //10
			vpd.put("var11", varOList.get(i).get("PRICE").toString());	//11
			vpd.put("var12", varOList.get(i).getString("USER_ID"));	    //12
			vpd.put("var13", varOList.get(i).get("PRODUCT_TOTAL_AMOUNT").toString());	//13
			vpd.put("var14", varOList.get(i).getString("BASKET_DATE"));	    //14
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
