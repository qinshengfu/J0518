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
 * 说明：一天的开始记录股票K线图
 * 创建人：Ajie
 * 创建时间：2020年6月12日17:21:50
 */
public class BeginOfDayTaskJob extends BaseFrontController implements Job {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        String time = DateUtil.now();
        logger.info("开始记录K线图：" + time);
        // 普通类从spring容器中拿出service
        WebApplicationContext webctx = ContextLoader.getCurrentWebApplicationContext();
        // 股票商品表
        Shares_prodManager shares_prodService = (Shares_prodManager) webctx.getBean("shares_prodService");

        PageData pd = new PageData();
        try {
            // 股票商品查询
            List<PageData> varList = shares_prodService.listAll(pd);
            for (PageData map : varList) {
                addLineChart(webctx, map, time);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能描述：新增K线图记录
     *
     * @author Ajie
     * @date 2020/6/4 0004
     */
    public void addLineChart(WebApplicationContext webctx, PageData pd, String time) throws Exception {
        /** K线图 */
        Line_chartManager line_chartService = (Line_chartManager) webctx.getBean("line_chartService");
        PageData map = new PageData();
        map.putAll(pd);
        map.put("GMT_CREATE", time);
        map.put("GMT_MODIFIED", time);
        line_chartService.save(map);
    }
}
