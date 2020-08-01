package com.fh.util.timer;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.NumberUtil;
import com.fh.controller.front.BaseFrontController;
import com.fh.plugin.OnlineChatServerPool;
import com.fh.service.front.Sys_configManager;
import com.fh.service.share.Line_chartManager;
import com.fh.service.share.Shares_prodManager;

import com.fh.util.PageData;
import com.fh.util.StringUtil;
import com.fh.util.Tools;
import net.sf.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 说明：行情价格变动定时任务
 * 创建人：Ajie
 * 创建时间：2020年6月4日15:51:57
 */
public class PriceTaskJob extends BaseFrontController implements Job {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        // 普通类从spring容器中拿出service
        WebApplicationContext webctx = ContextLoader.getCurrentWebApplicationContext();
        // 系统参数
        Sys_configManager sys_configService = (Sys_configManager) webctx.getBean("sys_configService");
        PageData pd = new PageData();
        try {
            // 获取系统参数
            PageData param = sys_configService.findById(pd);
            int interval = Convert.toInt(param.get("PRICE_CHANGE_INTERVAL"));
            if (interval < 1) {
                return;
            }
            latestStockPrice(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 功能描述：推送股票价格
     *
     * @author Ajie
     * @date 2020/6/3 0003
     */
    public void latestStockPrice(PageData param) {
        PageData pd = new PageData();
        // 普通类从spring容器中拿出service
        WebApplicationContext webctx = ContextLoader.getCurrentWebApplicationContext();
        try {
            // 定义推送结果
            JSONObject result = new JSONObject();
            result.element("type", "latestPrice");
            // 股票商品表
            Shares_prodManager shares_prodService = (Shares_prodManager) webctx.getBean("shares_prodService");
            // 股票商品查询
            List<PageData> varList = shares_prodService.listAll(pd);
            if (varList.size() <= 0) {
                return;
            }
            // 计算价格后的列表
            List<PageData> sharesList = calculatePrice(varList, param);
            if (sharesList == null) {
                return;
            }
            // 没有用户在线的情况下关闭推送
            if (OnlineChatServerPool.getOnlineUser().isEmpty()) {
                return;
            }
            // 推送
            result.element("CONTENT", sharesList);
            OnlineChatServerPool.sendMessage(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能描述：计算股票最新价格
     *
     * @author Ajie
     * @date 2020/6/4 0004
     */
    public List<PageData> calculatePrice(List<PageData> list, PageData param) throws Exception {
        Date date = new Date();
        // 检查是否为一天的开始
        Date startTime = DateUtil.beginOfDay(date);
        Date endTime = DateUtil.endOfDay(date);
        int interval = Convert.toInt(param.get("PRICE_CHANGE_INTERVAL"));
        // 普通类从spring容器中拿出service
        WebApplicationContext webctx = ContextLoader.getCurrentWebApplicationContext();
        /** 股票版块表 */
        Shares_prodManager shares_prodService = (Shares_prodManager) webctx.getBean("shares_prodService");
        // 定义返回结果列表
        List<PageData> newList = new ArrayList<>(18);
        for (PageData pd : list) {
            //开盘价、现价、幅度范围
            double openPrice = Convert.toDouble(pd.get("OPEN_PRICE"));
            double price = Convert.toDouble(pd.get("PRICE"));
            double max = Convert.toDouble(pd.get("SLIP_POINT"));
            if (max == 0) {
                continue;
            }
            // 最少值 是最大值的负数
            double min = max - max * 2;
            double random;
            //新价格围绕左右N%波动
            if (max > 1) {
                random = 1 + new Random().nextDouble() * max;
            } else if (max > 0 && max <= 1) {
                random = 1 + (new Random().nextDouble() * (max - min) + min);
            } else {
                random = new Random().nextDouble() * max;
            }
            double newPrice = NumberUtil.round(openPrice * random, 2).doubleValue();
            // 涨跌了多少
            double wave = NumberUtil.sub(newPrice, price);
            // 涨跌幅价格/原价=涨跌幅比例%
            double range = NumberUtil.div(wave, openPrice, 2);
            // 为了保护现场 需要修改什么单独新建一个map
            PageData map = new PageData();
            map.put("PRICE", newPrice);
            map.put("RANGE", range);
            map.put("WAVE", wave);
            map.put("SHARES_PROD_ID", pd.get("SHARES_PROD_ID"));
            // 验证最高价和最低价
            double maxPrice = Convert.toDouble(pd.get("MAX_PRICE"));
            double minPrice = Convert.toDouble(pd.get("MIN_PRICE"));
            if (maxPrice < newPrice) {
                map.put("MAX_PRICE", newPrice);
            }
            if (minPrice > newPrice) {
                map.put("MIN_PRICE", newPrice);
            }
            // 如果是一天的开始 误差在N秒内 记录开盘价
            if (DateUtil.isIn(date, startTime, DateUtil.offsetSecond(startTime, interval))) {
                logger.warn("股票商品：" + pd.get("PROD_NAME") + " 记录开盘价:" + newPrice);
                map.put("OPEN_PRICE", newPrice);
            }
            // 如果是一天的结束 误差在N秒内 记录收盘价
            if (DateUtil.isIn(date, endTime, DateUtil.offsetSecond(endTime, -interval))) {
                logger.warn("股票商品：" + pd.get("PROD_NAME") + " 记录收盘价:" + newPrice);
                map.put("CLOSING_PRICE", newPrice);
            }
            shares_prodService.edit(map);
            // 添加到推送列表中
            PageData latestData = new PageData();
            latestData.putAll(pd);
            latestData.putAll(map);
            // 查询当日成交量和当日交易额
            latestData.put("DAY_VOLUME", shares_prodService.getDayVolumeById(map).get("DAY_VOLUME"));
            latestData.put("DAY_TRADING", shares_prodService.getDayTradingById(map).get("DAY_TRADING"));
            newList.add(latestData);
            // 新增K线图信息
//            addLineChart(webctx, latestData);
        }
        return newList;
    }

    /**
     * 功能描述：新增K线图记录
     *
     * @author Ajie
     * @date 2020/6/4 0004
     */
    public void addLineChart(WebApplicationContext webctx, PageData pd) throws Exception {
        /** K线图 */
        Line_chartManager line_chartService = (Line_chartManager) webctx.getBean("line_chartService");
        PageData map = new PageData();
        map.putAll(pd);
        map.put("GMT_CREATE", DateUtil.now());
        map.put("GMT_MODIFIED", DateUtil.now());

        map.put("CLOSING_PRICE", "");
        map.put("BID_PRICE", "");
        map.put("AUC_PRICE", "");
        line_chartService.save(map);
    }
}
