<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>

<html>
<base href="<%=basePath%>">
<!-- 公共文件 -->
<%@ include file="../../front/public/unit.jsp" %>
<head>
    <title></title>
</head>

<style>
    /* 轮播图分页按钮 */
    .swiper-container-horizontal > .swiper-pagination-bullets, .swiper-pagination-custom, .swiper-pagination-fraction {
        bottom: 40px;
        left: 0;
        width: 100%;
    }
</style>

<body>
<div class="mui-content">
    <!-- 轮播图 -->
    <div class="swiper-container">
        <h4>首页</h4>
        <div class="swiper-wrapper">
            <%--轮播图内容 通过ajax异步加载--%>
        </div>
        <div class="swiper-pagination"></div>
    </div>
    <!-- 公告轮播 -->
    <div class="index_mquree">
        <a href="front/to_news">更多</a>
    </div>
    <!-- 普通团专区 -->
    <!--糖豆兑换区-->
    <div class="home_form">
        <h4>
            <div>热门商品</div>
            <div onclick="toShares()"><span>更多</span><i class="iconfont icon-you-"></i></div>
        </h4>
        <!--自选添加商品-->
        <div class="index_zix">
            <p>自选添加商品</p>
            <div class="index_zix_add" onclick="toShares()">
                <i class="iconfont icon-add"></i>
            </div>
        </div>
        <!-- 商品列表 -->
        <ul class="index_ul" id="stockList">

            <li>
                <div class="statebg">
                    <a href="front/to_kLine">
                        <h5>测试版块</h5>
                        <h5>涨跌幅：<span style="color:red;">0.02%</span></h5>
                        <div class="jyz_index">
                            <img src="static/front/images/transaction1.png">
                        </div>
                    </a>
                </div>
            </li>

        </ul>

    </div>

</div>

<%@include file="../../front/footer/footer.jsp" %>

</body>

<script>

    // 当前用户想展示的股票版块
    var stockIdList = "";

    // 初始化
    $(function () {
        getHomeChart();
        getHomeNews();


        getUserShowStockList()

    });

    // 访问添加自选版块页面
    function toShares() {
        window.location.href = "front/to_sharesAll";
    }

    // 首页轮播图
    function getHomeChart() {
        $.get("front/homeChart", function (result) {
            if (result.success) {
                // 设置轮播图内容
                setHomeChart(result.data.item)
            }
        }, "json")
    }

    // 设置轮播图
    function setHomeChart(data) {
        // 获取父节点
        var fatherNode = $(".swiper-wrapper");
        // 循环写入
        for (let i = 0; i < data.length; i++) {
            var pd = data[i];
            // 创建div 轮播图节点
            var divDom = $("<div class='swiper-slide'><img src=" + pd.PIC_PATH + "></div>");
            // 追加到父节点
            fatherNode.append(divDom);
        }
        //轮播图
        var mySwiper = new Swiper('.swiper-container', {
            loop: true, // 循环模式选项
            autoplay: {
                disableOnInteraction: false,
                delay: 2500,
            },
            pagination: {
                el: '.swiper-pagination',
            },
        })
    }

    // 获取首页新闻公告
    function getHomeNews() {
        $.get("front/homeNews?count=1", function (result) {
            if (result.success) {
                setHomeNews(result.data.item[0]);
            }
        }, "json");
    }

    // 设置滚动新闻公告
    function setHomeNews(data) {
        // 是否弹出新闻公告
        var tag = "${tag}";
        if (tag != "") {
            popUpNews(data)
        }
        // 获取父节点
        var fatherNode = $(".index_mquree");
        // 创建新闻公告内容
        var str = "<i class=\"iconfont icon-gonggao6\"></i>" +
            "<marquee behavior=\"\" direction=\"\">" + data.CONTENT + "</marquee>";
        var newsDom = $(str);
        // 追加到父节点
        fatherNode.prepend(newsDom)
    }

    // 弹出新闻公告
    function popUpNews(data) {
        //示范一个公告层
        layer.open({
            type: 1,
            title: false,// 不显示标题
            closeBtn: false,
            area: '300px;',
            shade: 0.8,
            id: 'popUpNews',//设定一个id，防止重复弹出
            resize: false,
            shadeClose: true,// 点击阴影关闭
            btn: ['确认'],
            btnAlign: 'c',
            moveType: 1,//拖拽模式，0或者1
            content: '<div style="padding: 50px; line-height: 22px; background-color: #393D49; color: #fff; font-weight: 300;"> ' + data.CONTENT + ' </div>',
            yes: function (index) {
                window.location.replace("front/to_index")
            },
            end: function () {
                window.location.replace("front/to_index")
            }
        });
    }

    // 获取用户想展示的股票版块信息
    function getUserShowStockList() {
        var url = "front/getUserShowStockList";
        $.get(url, function (result) {
            if (result.success) {
                stockIdList = result.data.stockIdList;
                setStockList(result.data.item)
            }
        })
    }

    // 渲染股票列表
    function setStockList(data) {
        var ulDom = $("#stockList");
        ulDom.html("")

        var str = '';
        for (let i = 0; i < data.length; i++) {
            var pd = data[i];
            // 判断是否为用户想展示的股票版块
            if (stockIdList.indexOf(pd.SHARES_PROD_ID) == -1) {
                continue;
            }

            str += "<li>\n" +
                "       <div class=\"statebg\">\n" +
                "           <a href=\"front/to_kLine?id=" + pd.SHARES_PROD_ID + "\">\n" +
                "               <h5>" + pd.PROD_NAME + "</h5>\n" +
                "               <h5>涨跌幅：<span style=\"color:red;\">" + pd.RANGE + "%</span></h5>\n" +
                "               <div class=\"jyz_index\">\n" +
                "                   <img src=\"static/front/images/transaction1.png\">\n" +
                "               </div>\n" +
                "           </a>\n" +
                "       </div>\n" +
                "   </li>";
        }


        ulDom.append(str)
    }

</script>


</html>
