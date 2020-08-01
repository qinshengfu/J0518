package com.fh.util.express;


import com.fh.util.DateUtil;
import com.fh.util.QuartzManager;
import com.fh.util.timer.BeginOfDayTaskJob;
import com.fh.util.timer.PriceTaskJob;
import com.fh.util.timer.StockAutoMatchTaskJob;

/**
 * 定时任务控制
 *
 * @author liming
 */
public class TimerManager {


    public TimerManager() {

        // 隔5秒刷新一次行情价格
        System.out.println("启动隔5秒刷新一次行情价格：" + DateUtil.getTime());
        String priceTime = "0/5 * * * * ? *";
        QuartzManager.addJob("priceTaskJob", PriceTaskJob.class, priceTime);

        // 隔10秒执行一次匹配系统
        System.out.println("启动隔10秒执行一次匹配系统：" + DateUtil.getTime());
        String matchTime = "0/10 * * * * ? *";
        QuartzManager.addJob("stockAutoMatchTaskJob", StockAutoMatchTaskJob.class, matchTime);

        // 添加每日0点记录股票K线图定时任务
        System.out.println("启动添加每日0点记录股票K线图定时任务：" + DateUtil.getTime());
        String beginDayTime = "0 0 0 * * ?";
        QuartzManager.addJob("beginOfDayTaskJob", BeginOfDayTaskJob.class, beginDayTime);

    }
}
