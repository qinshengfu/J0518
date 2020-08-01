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
import com.fh.service.share.Line_chartManager;

/** 
 * 说明：K线图
 * 创建人：
 * 创建时间：2020-06-04
 */
@Controller
@RequestMapping(value="/line_chart")
public class Line_chartController extends BaseController {
	
	String menuUrl = "line_chart/list.do"; //菜单地址(权限用)
	@Resource(name="line_chartService")
	private Line_chartManager line_chartService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Line_chart");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd.put("LINE_CHART_ID", this.get32UUID());	//主键
		pd.put("GMT_CREATE", Tools.date2Str(new Date()));	//创建时间
		pd.put("GMT_MODIFIED", Tools.date2Str(new Date()));	//更新时间
		pd.put("SHARES_TAG_ID", "");	//股票类目ID
		pd.put("STOCK_NAME", "");	//股票名称
		line_chartService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除Line_chart");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = this.getPageData();
		line_chartService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Line_chart");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		line_chartService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表Line_chart");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = line_chartService.list(page);	//列出Line_chart列表
		mv.setViewName("share/line_chart/line_chart_list");
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
		PageData pd = this.getPageData();
		mv.setViewName("share/line_chart/line_chart_edit");
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
		PageData pd = this.getPageData();
		pd = line_chartService.findById(pd);	//根据ID读取
		mv.setViewName("share/line_chart/line_chart_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Line_chart");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = this.getPageData();
		Map<String,Object> map = new HashMap<String,Object>();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			line_chartService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出Line_chart到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("创建时间");	//1
		titles.add("更新时间");	//2
		titles.add("股票类目ID");	//3
		titles.add("股票名称");	//4
		titles.add("今日开盘价");	//5
		titles.add("昨日收盘价");	//6
		titles.add("当前价格");	//7
		titles.add("今日最高价");	//8
		titles.add("今日最低价");	//9
		titles.add("竞买价，即“买一”报价");	//10
		titles.add("竞卖价，即“卖一”报价");	//11
		titles.add("成交的股票数");	//12
		titles.add("成交金额，单位为“元”");	//13
		titles.add("买一，申报N股");	//32
		titles.add("买一，报价");	//33
		titles.add("买二");	//14
		titles.add("买二报价");	//15
		titles.add("买三");	//16
		titles.add("买三");	//17
		titles.add("买四");	//18
		titles.add("买四");	//19
		titles.add("买五");	//20
		titles.add("买五");	//21
		titles.add("卖一，申报N股");	//22
		titles.add("卖一，报价");	//23
		titles.add("卖二");	//24
		titles.add("卖二");	//25
		titles.add("卖三");	//26
		titles.add("卖三");	//27
		titles.add("卖四");	//28
		titles.add("卖四");	//29
		titles.add("卖五");	//30
		titles.add("卖五");	//31
		dataMap.put("titles", titles);
		List<PageData> varOList = line_chartService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("GMT_CREATE"));	    //1
			vpd.put("var2", varOList.get(i).getString("GMT_MODIFIED"));	    //2
			vpd.put("var3", varOList.get(i).getString("SHARES_TAG_ID"));	    //3
			vpd.put("var4", varOList.get(i).getString("STOCK_NAME"));	    //4
			vpd.put("var5", varOList.get(i).get("OPEN_PRICE").toString());	//5
			vpd.put("var6", varOList.get(i).get("CLOSING_PRICE").toString());	//6
			vpd.put("var7", varOList.get(i).get("PRICE").toString());	//7
			vpd.put("var8", varOList.get(i).get("MAX_PRICE").toString());	//8
			vpd.put("var9", varOList.get(i).get("MIN_PRICE").toString());	//9
			vpd.put("var10", varOList.get(i).get("BID_PRICE").toString());	//10
			vpd.put("var11", varOList.get(i).get("AUC_PRICE").toString());	//11
			vpd.put("var12", varOList.get(i).get("SHARES_TRADED_NUM").toString());	//12
			vpd.put("var13", varOList.get(i).get("TRADED_AMOUNT").toString());	//13
			vpd.put("var14", varOList.get(i).get("BUY1_NUM").toString());	//14
			vpd.put("var15", varOList.get(i).get("BUY1_PRICE").toString());	//15
			vpd.put("var16", varOList.get(i).get("BUY2_NUM").toString());	//16
			vpd.put("var17", varOList.get(i).get("BUY2_PRICE").toString());	//17
			vpd.put("var18", varOList.get(i).get("BUY3_NUM").toString());	//18
			vpd.put("var19", varOList.get(i).get("BUY3_PRICE").toString());	//19
			vpd.put("var20", varOList.get(i).get("BUY4_NUM").toString());	//20
			vpd.put("var21", varOList.get(i).get("BUY4_PRICE").toString());	//21
			vpd.put("var22", varOList.get(i).get("BUY5_NUM").toString());	//22
			vpd.put("var23", varOList.get(i).get("BUY5_PRICE").toString());	//23
			vpd.put("var24", varOList.get(i).get("SELL1_NUM").toString());	//24
			vpd.put("var25", varOList.get(i).get("SELL1_PRICE").toString());	//25
			vpd.put("var26", varOList.get(i).get("SELL2_NUM").toString());	//26
			vpd.put("var27", varOList.get(i).get("SELL2_PRICE").toString());	//27
			vpd.put("var28", varOList.get(i).get("SELL3_NUM").toString());	//28
			vpd.put("var29", varOList.get(i).get("SELL3_PRICE").toString());	//29
			vpd.put("var30", varOList.get(i).get("SELL4_NUM").toString());	//30
			vpd.put("var31", varOList.get(i).get("SELL4_PRICE").toString());	//31
			vpd.put("var32", varOList.get(i).get("SELL5_NUM").toString());	//32
			vpd.put("var33", varOList.get(i).get("SELL5_PRICE").toString());	//33
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
