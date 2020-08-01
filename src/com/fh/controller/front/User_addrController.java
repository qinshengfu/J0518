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
import com.fh.service.front.User_addrManager;

/** 
 * 说明：用户收货地址表
 * 创建人：
 * 创建时间：2020-05-21
 */
@Controller
@RequestMapping(value="/user_addr")
public class User_addrController extends BaseController {
	
	String menuUrl = "user_addr/list.do"; //菜单地址(权限用)
	@Resource(name="user_addrService")
	private User_addrManager user_addrService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增User_addr");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("USER_ADDR_ID", this.get32UUID());	//主键
		pd.put("GMT_CREATE", Tools.date2Str(new Date()));	//创建时间
		pd.put("GMT_MODIFIED", Tools.date2Str(new Date()));	//更新时间
		pd.put("PHONE", "0");	//手机号
		pd.put("USER_ID", "");	//用户ID
		pd.put("RECEIVER", "");	//收货人姓名
		pd.put("MOBILE", "0");	//收货人手机号
		pd.put("COMMON_ADDR", "0");	//是否默认收货地址 1:是，0：否
		pd.put("PROVINCE_ID", "");	//省ID
		pd.put("PROVINCE", "");	//省
		pd.put("CITY_ID", "");	//城市ID
		pd.put("CITY", "");	//城市
		pd.put("AREA_ID", "");	//区ID
		pd.put("AREA", "");	//区
		pd.put("POST_CODE", "");	//邮编
		pd.put("ADDR", "");	//地址
		pd.put("STATUS", "");	//状态,1正常，0无效
		pd.put("VERSION", "0");	//版本号
		user_addrService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除User_addr");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		user_addrService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改User_addr");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		user_addrService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表User_addr");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = user_addrService.list(page);	//列出User_addr列表
		mv.setViewName("front/user_addr/user_addr_list");
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
		mv.setViewName("front/user_addr/user_addr_edit");
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
		pd = user_addrService.findById(pd);	//根据ID读取
		mv.setViewName("front/user_addr/user_addr_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除User_addr");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			user_addrService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出User_addr到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("创建时间");	//1
		titles.add("更新时间");	//2
		titles.add("手机号");	//3
		titles.add("用户ID");	//4
		titles.add("收货人姓名");	//5
		titles.add("收货人手机号");	//6
		titles.add("是否默认收货地址 1:是，0：否");	//7
		titles.add("省ID");	//8
		titles.add("省");	//9
		titles.add("城市ID");	//10
		titles.add("城市");	//11
		titles.add("区ID");	//12
		titles.add("区");	//13
		titles.add("邮编");	//14
		titles.add("地址");	//15
		titles.add("状态,1正常，0无效");	//16
		titles.add("版本号");	//17
		dataMap.put("titles", titles);
		List<PageData> varOList = user_addrService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("GMT_CREATE"));	    //1
			vpd.put("var2", varOList.get(i).getString("GMT_MODIFIED"));	    //2
			vpd.put("var3", varOList.get(i).get("PHONE").toString());	//3
			vpd.put("var4", varOList.get(i).getString("USER_ID"));	    //4
			vpd.put("var5", varOList.get(i).getString("RECEIVER"));	    //5
			vpd.put("var6", varOList.get(i).get("MOBILE").toString());	//6
			vpd.put("var7", varOList.get(i).getString("COMMON_ADDR"));	    //7
			vpd.put("var8", varOList.get(i).getString("PROVINCE_ID"));	    //8
			vpd.put("var9", varOList.get(i).getString("PROVINCE"));	    //9
			vpd.put("var10", varOList.get(i).getString("CITY_ID"));	    //10
			vpd.put("var11", varOList.get(i).getString("CITY"));	    //11
			vpd.put("var12", varOList.get(i).getString("AREA_ID"));	    //12
			vpd.put("var13", varOList.get(i).getString("AREA"));	    //13
			vpd.put("var14", varOList.get(i).getString("POST_CODE"));	    //14
			vpd.put("var15", varOList.get(i).getString("ADDR"));	    //15
			vpd.put("var16", varOList.get(i).getString("STATUS"));	    //16
			vpd.put("var17", varOList.get(i).get("VERSION").toString());	//17
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
