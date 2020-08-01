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

import cn.hutool.core.text.StrSpliter;
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
import com.fh.service.mall.ProdManager;

/** 
 * 说明：商品表
 * 创建人：
 * 创建时间：2020-05-21
 */
@Controller
@RequestMapping(value="/prod")
public class ProdController extends BaseController {
	
	String menuUrl = "prod/list.do"; //菜单地址(权限用)
	@Resource(name="prodService")
	private ProdManager prodService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Prod");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		// 以【,】号切割商品轮播图
		List<String> imgList = StrSpliter.split(pd.getString("IMGS"), ",", 0, true, true);
		pd.put("GMT_CREATE", Tools.date2Str(new Date()));	//创建时间
		pd.put("GMT_MODIFIED", Tools.date2Str(new Date()));	//更新时间
		pd.put("DELETE_TIME", "");	//删除时间
		pd.put("SHOP_ID", "");	//店铺id
		pd.put("CATEGORY_ID", "");	//商品分类
		// 主图 切割商品图片取第一张
		pd.put("PIC", imgList.get(0));
		// 版本号
		pd.put("VERSION", 0);
		prodService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除Prod");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		prodService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Prod");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		// 去掉百度富文本
		pd.remove("editorValue");
		prodService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表Prod");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = prodService.list(page);	//列出Prod列表
		mv.setViewName("mall/prod/prod_list");
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
		mv.setViewName("mall/prod/prod_edit");
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
		pd = prodService.findById(pd);	//根据ID读取
		mv.setViewName("mall/prod/prod_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Prod");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			prodService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出Prod到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("创建时间");	//1
		titles.add("更新时间");	//2
		titles.add("删除时间");	//3
		titles.add("商品名称");	//4
		titles.add("店铺id");	//5
		titles.add("原价");	//6
		titles.add("现价");	//7
		titles.add("简要描述,卖点等");	//8
		titles.add("详细描述");	//9
		titles.add("商品主图");	//10
		titles.add("商品图片，以,分割");	//11
		titles.add("默认是1，表示正常状态, -1表示删除, 0下架");	//12
		titles.add("商品分类");	//13
		titles.add("销量");	//14
		titles.add("总库存");	//15
		titles.add("版本 乐观锁");	//16
		dataMap.put("titles", titles);
		List<PageData> varOList = prodService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("GMT_CREATE"));	    //1
			vpd.put("var2", varOList.get(i).getString("GMT_MODIFIED"));	    //2
			vpd.put("var3", varOList.get(i).getString("DELETE_TIME"));	    //3
			vpd.put("var4", varOList.get(i).getString("PROD_NAME"));	    //4
			vpd.put("var5", varOList.get(i).getString("SHOP_ID"));	    //5
			vpd.put("var6", varOList.get(i).get("ORI_PRICE").toString());	//6
			vpd.put("var7", varOList.get(i).get("PRICE").toString());	//7
			vpd.put("var8", varOList.get(i).getString("BRIEF"));	    //8
			vpd.put("var9", varOList.get(i).getString("CONTENT"));	    //9
			vpd.put("var10", varOList.get(i).getString("PIC"));	    //10
			vpd.put("var11", varOList.get(i).getString("IMGS"));	    //11
			vpd.put("var12", varOList.get(i).getString("STATUS"));	    //12
			vpd.put("var13", varOList.get(i).getString("CATEGORY_ID"));	    //13
			vpd.put("var14", varOList.get(i).get("SOLD_NUM").toString());	//14
			vpd.put("var15", varOList.get(i).get("TOTAL_STOCKS").toString());	//15
			vpd.put("var16", varOList.get(i).get("VERSION").toString());	//16
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
