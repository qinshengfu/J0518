package com.fh.controller.front;


import com.fh.entity.result.R;
import com.fh.util.PageData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 说明：前台首页控制器
 * 创建人：Ajie
 * 创建时间：2020年5月22日17:31:01
 */
@Controller
@RequestMapping("/front")
@Api(tags = "首页操作")
public class IndexController extends BaseFrontController {

    /**
     * 功能描述：访问前台【首页】页面
     *
     * @author Ajie
     * @date 2020年5月22日17:29:31
     */
    @RequestMapping(value = "to_index")
    public ModelAndView toIndex() {
        ModelAndView mv = getModelAndView();
        // 请求参数 判断从登陆页跳转的
        PageData pd = this.getPageData();
        mv.setViewName("front/index/index");
        mv.addObject("flag", "index");
        mv.addObject("tag", pd.getString("tag"));
        return mv;
    }

    /**
     * 功能描述：访问前台【K线图】页面
     *
     * @author Ajie
     * @date 2020年5月22日17:29:27
     */
    @RequestMapping(value = "to_kLine")
    public ModelAndView toKLine() {
        PageData pd = this.getPageData();
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/index/k-line");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 功能描述：访问前台【全部股票版本页面】页面
     *
     * @author Ajie
     * @date 2020年6月4日09:13:21
     */
    @RequestMapping(value = "to_sharesAll")
    public ModelAndView toSharesAll() {
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/index/shopde");
        return mv;
    }

    /**
     * 功能描述：获取首页所有轮播图
     *
     * @author Ajie
     * @date 2020/5/25 0025
     */
    @RequestMapping(value = "homeChart", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "首页轮播图")
    public R homeChart() throws Exception {
        // 查询所有轮播图
        List<PageData> chartAll = sys_chartService.listAll(new PageData());
        return R.ok().data("item", chartAll);
    }

    /**
     * 功能描述：获取所有新闻公告列表
     *
     * @author Ajie
     * @date 2020/5/25 0025
     */
    @RequestMapping(value = "allNewsList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "所有新闻公告列表")
    public R homeNewsList() throws Exception {
        // 查询所有轮播图
        List<PageData> newsAllList = sys_newsService.listAll(new PageData());
        return R.ok().data("item", newsAllList);
    }

    /**
     * 功能描述：获取首页新闻公告
     *
     * @author Ajie
     * @date 2020/5/25 0025
     */
    @RequestMapping(value = "homeNews", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "首页新闻公告")
    @ApiImplicitParam(name = "count", value = "查询多少条数据", example = "1", required = true, dataType = "Integer")
    public R homeNews() throws Exception {
        PageData pd = this.getPageData();
        // 查询最新N条新闻公告
        List<PageData> newsList = sys_newsService.listAppointResult(pd);
        return R.ok().data("item", newsList);
    }



}

