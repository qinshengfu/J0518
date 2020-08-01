package com.fh.controller.share;

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
import com.fh.service.share.Shares_prodManager;

/** 
 * 说明：股票商品表
 * 创建人：
 * 创建时间：2020-05-21
 */
@Controller
@RequestMapping(value="/shares_prod")
public class Shares_prodController extends BaseController {
	
	String menuUrl = "shares_prod/list.do"; //菜单地址(权限用)
	@Resource(name="shares_prodService")
	private Shares_prodManager shares_prodService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Shares_prod");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd.put("SHARES_PROD_ID", this.get32UUID());	//主键
		pd.put("GMT_CREATE", Tools.date2Str(new Date()));	//创建时间
		pd.put("GMT_MODIFIED", Tools.date2Str(new Date()));	//更新时间
		pd.put("DELETE_TIME", "");	//删除时间
		pd.put("TYPE", "");	//类型
		pd.put("SLIP_POINT", 0.1);
		pd.put("PRICE", pd.get("OPEN_PRICE"));
		pd.put("MAX_PRICE", pd.get("OPEN_PRICE"));
		pd.put("MIN_PRICE", pd.get("OPEN_PRICE"));
		pd.put("ORI_PRICE", pd.get("OPEN_PRICE"));
		pd.put("WAVE", 0);
		pd.put("RANGE", 0);
		pd.put("CLOSING_PRICE", "");

		shares_prodService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除Shares_prod");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		shares_prodService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Shares_prod");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		shares_prodService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表Shares_prod");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = shares_prodService.list(page);	//列出Shares_prod列表
		mv.setViewName("share/shares_prod/shares_prod_list");
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
		mv.setViewName("share/shares_prod/shares_prod_edit");
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
		pd = shares_prodService.findById(pd);	//根据ID读取
		mv.setViewName("share/shares_prod/shares_prod_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Shares_prod");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			shares_prodService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出Shares_prod到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("创建时间");	//1
		titles.add("更新时间");	//2
		titles.add("删除时间");	//3
		titles.add("商品名称");	//4
		titles.add("单位");	//5
		titles.add("价格");	//6
		titles.add("状态");	//7
		titles.add("类型");	//8
		titles.add("总数");	//9
		titles.add("余数");	//10
		titles.add("排序");	//11
		titles.add("ID");	//12
		titles.add("编号");	//13
		titles.add("涨跌");	//14
		titles.add("幅度");	//15
		titles.add("滑点");	//16
		titles.add("原价");	//17
		titles.add("最高价");	//18
		titles.add("最低价");	//19
		dataMap.put("titles", titles);
		List<PageData> varOList = shares_prodService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("GMT_CREATE"));	    //1
			vpd.put("var2", varOList.get(i).getString("GMT_MODIFIED"));	    //2
			vpd.put("var3", varOList.get(i).getString("DELETE_TIME"));	    //3
			vpd.put("var4", varOList.get(i).getString("PROD_NAME"));	    //4
			vpd.put("var5", varOList.get(i).getString("UNIT"));	    //5
			vpd.put("var6", varOList.get(i).getString("PRICE"));	    //6
			vpd.put("var7", varOList.get(i).getString("STATUS"));	    //7
			vpd.put("var8", varOList.get(i).getString("TYPE"));	    //8
			vpd.put("var9", varOList.get(i).getString("TOTAL"));	    //9
			vpd.put("var10", varOList.get(i).getString("REMAINDER"));	    //10
			vpd.put("var11", varOList.get(i).getString("SEQ"));	    //11
			vpd.put("var12", varOList.get(i).getString("SHARES_PROD_ID"));	    //12
			vpd.put("var13", varOList.get(i).getString("NUMBER_CODE"));	    //13
			vpd.put("var14", varOList.get(i).getString("WAVE"));	    //14
			vpd.put("var15", varOList.get(i).getString("RANGE"));	    //15
			vpd.put("var16", varOList.get(i).getString("SLIP_POINT"));	    //16
			vpd.put("var17", varOList.get(i).getString("ORI_PRICE"));	    //17
			vpd.put("var18", varOList.get(i).getString("MAX_PRICE"));	    //18
			vpd.put("var19", varOList.get(i).getString("MIN_PRICE"));	    //19
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
