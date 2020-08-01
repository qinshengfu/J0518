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
import com.fh.service.mall.OrderManager;

/**
 * 说明：订单表
 * 创建人：
 * 创建时间：2020-05-21
 */
@Controller
@RequestMapping(value="/order")
public class OrderController extends BaseController {

	String menuUrl = "order/list.do"; //菜单地址(权限用)
	@Resource(name="orderService")
	private OrderManager orderService;

	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Order");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("ORDER_ID", this.get32UUID());	//主键
		pd.put("GMT_CREATE", Tools.date2Str(new Date()));	//创建时间
		pd.put("GMT_MODIFIED", Tools.date2Str(new Date()));	//更新时间
		pd.put("SHOP_ID", "");	//店铺id
		pd.put("PROD_NAME", "");	//产品名称,多个产品将会以逗号隔开
		pd.put("USER_ID", "");	//用户id
		pd.put("ORDER_NUMBER", "");	//订购流水号
		pd.put("TOTAL", "0");	//总值
		pd.put("ACTUAL_TOTAL", "0");	//实际总值
		pd.put("PAY_TYPE", "");	//支付方式 0：usdt 1：商城积分 2：金额
		pd.put("REMARKS", "");	//订单备注
		pd.put("STATUS", "");	//订单状态
		pd.put("ADDR_ORDER_ID", "");	//用户订单地址Id
		pd.put("PRODUCT_SUM", "0");	//订单商品总数
		pd.put("PAY_TIME", Tools.date2Str(new Date()));	//付款时间
		pd.put("FINALLY_TIME", Tools.date2Str(new Date()));	//完成时间
		pd.put("CANCEL_TIME", Tools.date2Str(new Date()));	//取消时间
		pd.put("IS_PAYED", "0");	//是否已经支付，1：已支付，0：未支付
		pd.put("DELETE_STATUS", "0");	//删除状态，0：没有删除， 1：回收站， 2：永久删除
		pd.put("REFUND_STS", "0");	//申请退款：0:默认,1:在处理,2:处理完成
		pd.put("REDUCE_AMOUNT", "0");	//优惠总额
		pd.put("ORDER_TYPE", "");	//订单类型
		pd.put("CLOSE_TYPE", "");	//订单关闭原因
		orderService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除Order");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		orderService.delete(pd);
		out.write("success");
		out.close();
	}

	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Order");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		PageData order = orderService.findById(pd);
		pd.put("GMT_MODIFIED", DateUtil.now());
		if ("待发货".equals(order.getString("STATUS"))){
			pd.put("STATUS", "待收货");
		}
		orderService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表Order");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = orderService.list(page);	//列出Order列表
		mv.setViewName("mall/order/order_list");
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
		mv.setViewName("mall/order/order_edit");
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
		pd = orderService.findById(pd);	//根据ID读取
		mv.setViewName("mall/order/order_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Order");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			orderService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出Order到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("创建时间");	//1
		titles.add("更新时间");	//2
		titles.add("店铺id");	//3
		titles.add("产品名称,多个产品将会以逗号隔开");	//4
		titles.add("用户id");	//5
		titles.add("订购流水号");	//6
		titles.add("总值");	//7
		titles.add("实际总值");	//8
		titles.add("支付方式 0：usdt 1：商城积分 2：金额");	//9
		titles.add("订单备注");	//10
		titles.add("订单状态");	//11
		titles.add("物流单号");	//12
		titles.add("订单运费");	//13
		titles.add("用户订单地址Id");	//14
		titles.add("订单商品总数");	//15
		titles.add("付款时间");	//16
		titles.add("发货时间");	//17
		titles.add("完成时间");	//18
		titles.add("取消时间");	//19
		titles.add("是否已经支付，1：已支付，0：未支付");	//20
		titles.add("删除状态，0：没有删除， 1：回收站， 2：永久删除");	//21
		titles.add("申请退款：0:默认,1:在处理,2:处理完成");	//22
		titles.add("优惠总额");	//23
		titles.add("订单类型");	//24
		titles.add("订单关闭原因");	//25
		dataMap.put("titles", titles);
		List<PageData> varOList = orderService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("GMT_CREATE"));	    //1
			vpd.put("var2", varOList.get(i).getString("GMT_MODIFIED"));	    //2
			vpd.put("var3", varOList.get(i).getString("SHOP_ID"));	    //3
			vpd.put("var4", varOList.get(i).getString("PROD_NAME"));	    //4
			vpd.put("var5", varOList.get(i).getString("USER_ID"));	    //5
			vpd.put("var6", varOList.get(i).getString("ORDER_NUMBER"));	    //6
			vpd.put("var7", varOList.get(i).get("TOTAL").toString());	//7
			vpd.put("var8", varOList.get(i).get("ACTUAL_TOTAL").toString());	//8
			vpd.put("var9", varOList.get(i).getString("PAY_TYPE"));	    //9
			vpd.put("var10", varOList.get(i).getString("REMARKS"));	    //10
			vpd.put("var11", varOList.get(i).getString("STATUS"));	    //11
			vpd.put("var12", varOList.get(i).getString("DVY_FLOW_ID"));	    //12
			vpd.put("var13", varOList.get(i).get("FREIGHT_AMOUNT").toString());	//13
			vpd.put("var14", varOList.get(i).getString("ADDR_ORDER_ID"));	    //14
			vpd.put("var15", varOList.get(i).get("PRODUCT_SUM").toString());	//15
			vpd.put("var16", varOList.get(i).getString("PAY_TIME"));	    //16
			vpd.put("var17", varOList.get(i).getString("DVY_TIME"));	    //17
			vpd.put("var18", varOList.get(i).getString("FINALLY_TIME"));	    //18
			vpd.put("var19", varOList.get(i).getString("CANCEL_TIME"));	    //19
			vpd.put("var20", varOList.get(i).getString("IS_PAYED"));	    //20
			vpd.put("var21", varOList.get(i).getString("DELETE_STATUS"));	    //21
			vpd.put("var22", varOList.get(i).getString("REFUND_STS"));	    //22
			vpd.put("var23", varOList.get(i).get("REDUCE_AMOUNT").toString());	//23
			vpd.put("var24", varOList.get(i).getString("ORDER_TYPE"));	    //24
			vpd.put("var25", varOList.get(i).getString("CLOSE_TYPE"));	    //25
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
