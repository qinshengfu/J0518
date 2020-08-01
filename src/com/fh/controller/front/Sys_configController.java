package com.fh.controller.front;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.StrSpliter;
import cn.hutool.core.thread.ThreadUtil;
import com.fh.entity.result.R;
import com.fh.util.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 说明：系统参数配置
 * 创建人：
 * 创建时间：2020-03-25
 */
@Controller
@RequestMapping(value = "/sys_config")
public class Sys_configController extends BaseFrontController {

    String menuUrl = "sys_config/list.do"; //菜单地址(权限用)


    /**
     * 功能描述：清空数据,保留顶点用户
     *
     * @author Ajie
     * @date 2019/12/23 0023
     */
    @RequestMapping(value = "/wipeAllData")
    @ResponseBody
    public R delete(HttpServletRequest request) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "清空数据");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
            return null;
        } //校验权限
        PageData pd = new PageData();
        // 删除轮播图
        String picPath = request.getServletContext().getRealPath(Const.FILEPATHTWODIMENSIONCODE);
        DelAllFile.delAllFile(picPath);
        // 需要清空数据的表名
        pd.put("FT_USER_ADDR", "FT_USER_ADDR");
        pd.put("FT_USER_ADDR_ORDER", "FT_USER_ADDR_ORDER");
        pd.put("FT_DELIVERY_RECORD", "FT_DELIVERY_RECORD");
        pd.put("FT_AGENT_RECORD", "FT_AGENT_RECORD");
        pd.put("FT_PROD_TAG", "FT_PROD_TAG");
        pd.put("FT_PROD", "FT_PROD");
        pd.put("FT_POINTS_RECORD", "FT_POINTS_RECORD");
        pd.put("FT_MONEY_RECORD", "FT_MONEY_RECORD");
        pd.put("FT_SHARES_PROD", "FT_SHARES_PROD");
        pd.put("FT_SHARES_ORDER", "FT_SHARES_ORDER");
        pd.put("FT_BASKET", "FT_BASKET");
        pd.put("FT_ORDER_ITEM", "FT_ORDER_ITEM");
        pd.put("FT_ORDER", "FT_ORDER");
        pd.put("FT_SKU", "FT_SKU");
        pd.put("FT_CATEGORY", "FT_CATEGORY");
        pd.put("FT_USDT_RECORD", "FT_USDT_RECORD");
        pd.put("FT_LINE_CHART", "FT_LINE_CHART");
        pd.put("FT_USER_SHOW_STOCK", "FT_USER_SHOW_STOCK");
        // 调用多线程清缓存清空表
        ThreadUtil.execute(() -> {
            try {
                // 清空数据保留顶点用户
                accountService.deleteAll();
                // 清空表
                sys_configService.deleteAllTable(pd);
                // 重置序列
                resetSeq();
            } catch (Exception e) {
                throw new RuntimeException(this.getClass().getName() + "_" + e.getMessage(), e);
            }
        });
        FHLOG.save(Jurisdiction.getUsername(), "清空数据");
        // 重置顶点账号信息
        accountService.resetAccount();
        // 清空Redis数据库
        redisDaoImpl.deleteAll();
        return R.ok().message("清空数据成功!");
    }

    /**
     * 功能描述：调用存储过程重置序列
     *
     * @author Ajie
     * @date 2019年12月23日11:28:14
     */
    private void resetSeq() throws Exception {
        PageData pd = new PageData();
        pd.put("seqName", "FT_ACCOUNT_SEQ");
        pd.put("seqStart", "10001");
        sys_configService.resetSeq(pd);
        pd.put("seqName", "FT_ACCOUNT_DATA_SEQ");
        pd.put("seqStart", "2");
        sys_configService.resetSeq(pd);
        pd.put("seqName", "FT_AGENT_RECORD_SEQ");
        pd.put("seqStart", "1");
        sys_configService.resetSeq(pd);
        pd.put("seqName", "FT_BASKET_SEQ");
        pd.put("seqStart", "1");
        sys_configService.resetSeq(pd);
        pd.put("seqName", "FT_CATEGORY_SEQ");
        pd.put("seqStart", "1");
        sys_configService.resetSeq(pd);
        pd.put("seqName", "FT_DELIVERY_RECORD_SEQ");
        pd.put("seqStart", "1");
        sys_configService.resetSeq(pd);
        pd.put("seqName", "FT_MONEY_RECORD_SEQ");
        pd.put("seqStart", "1");
        sys_configService.resetSeq(pd);
        pd.put("seqName", "FT_ORDER_ITEM_SEQ");
        pd.put("seqStart", "1");
        sys_configService.resetSeq(pd);
        pd.put("seqName", "FT_ORDER_SEQ");
        pd.put("seqStart", "1");
        sys_configService.resetSeq(pd);
        pd.put("seqName", "FT_POINTS_RECORD_SEQ");
        pd.put("seqStart", "1");
        sys_configService.resetSeq(pd);
        pd.put("seqName", "FT_PROD_SEQ");
        pd.put("seqStart", "1");
        sys_configService.resetSeq(pd);
        pd.put("seqName", "FT_PROD_TAG_SEQ");
        pd.put("seqStart", "1");
        sys_configService.resetSeq(pd);
        pd.put("seqName", "FT_RISK_SEQ");
        pd.put("seqStart", "1");
        sys_configService.resetSeq(pd);
        pd.put("seqName", "FT_SHARES_ORDER_SEQ");
        pd.put("seqStart", "1");
        sys_configService.resetSeq(pd);
        pd.put("seqName", "FT_SKU_SEQ");
        pd.put("seqStart", "1");
        sys_configService.resetSeq(pd);
        pd.put("seqName", "FT_USDT_RECORD_SEQ");
        pd.put("seqStart", "1");
        sys_configService.resetSeq(pd);
        pd.put("seqName", "FT_USER_ADDR_ORDER_SEQ");
        pd.put("seqStart", "1");
        sys_configService.resetSeq(pd);
        pd.put("seqName", "FT_USER_ADDR_SEQ");
        pd.put("seqStart", "1");
        sys_configService.resetSeq(pd);
        pd.put("seqName", "FT_USER_SHOW_STOCK_SEQ");
        pd.put("seqStart", "1");
        pd.put("seqName", "FT_LINE_CHART_SEQ");
        pd.put("seqStart", "1");
        sys_configService.resetSeq(pd);
    }

    /**
     * 修改
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/edit")
    @ResponseBody
    public String edit() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "修改Sys_config");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        } //校验权限
        PageData pd = this.getPageData();
        // 去掉富文本自带的Name
        pd.remove("editorValue");
        pd.put("GMT_MODIFIED", Tools.date2Str(new Date()));    //更新时间
        sys_configService.edit(pd);
        ThreadUtil.execute(() -> {
            try {
                FHLOG.save(Jurisdiction.getUsername(), "修改系统参数");
            } catch (Exception e) {
                throw new RuntimeException(this.getClass().getName() + "_" + e.getMessage(), e);
            }
        });

        // 更改刷新价格定时任务间隔 单位：秒
        String priceTime;
        int second = Convert.toInt(pd.get("PRICE_CHANGE_INTERVAL"));
        if (second > 59 && second / 60 < 59) {
            // 间隔 分钟
            priceTime = "0 0/" + second / 60 + " * * * ? *";
        } else if (second > 3599 & second / 60 / 60 > 1) {
            // 间隔 小时
            priceTime = "0 0 0/" + second / 60 / 60 + " * * ? *";
        } else {
            // 间隔 秒
            priceTime = "0/" + second + " * * * * ? *";
        }
        QuartzManager.modifyJobTime("priceTaskJob", priceTime);

        return "success";
    }

    /**
     * 参数列表
     *
     * @throws Exception
     */
    @RequestMapping(value = "/list")
    public ModelAndView list() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "列表Sys_config");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "cha")) {
            return null;
        } //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();
        PageData pd = sys_configService.findById(new PageData());
        // 把周范围分割成列表			参数：被切分字符串，分隔符逗号，0表示无限制分片数，去除两边空格，忽略空白项
        List<String> weekRange = StrSpliter.split(pd.getString("WEEKLY_TRADING_HOURS"), '-', 0, true, true);
        pd.put("week", weekRange);
        mv.setViewName("front/sys_config/sys_config_list");
        mv.addObject("pd", pd);
        mv.addObject("QX", Jurisdiction.getHC());    //按钮权限
        ThreadUtil.execute(() -> {
            try {
                FHLOG.save(Jurisdiction.getUsername(), "查看系统参数");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return mv;
    }

}
