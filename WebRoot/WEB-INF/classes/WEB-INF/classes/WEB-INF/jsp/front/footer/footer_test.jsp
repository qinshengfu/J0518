<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title></title>
</head>

<body>
<footer class="app-footer">
    <ul flex="box:mean cross:center" class="footer-list">
        <li id="index" flex="dir:top cross:center main:center" class="footer-list-item is-active">
            <a href="release/to_index">
                <p flex="main:center cross:center" class="icon"><img src="static/front/images/shouye.png" alt=""></p>
                <p class="name">首 页</p>
            </a>
        </li>
        <li id="chart" flex="dir:top cross:center main:center" class="footer-list-item">
            <a href="release/to_chartInfo">
                <p flex="main:center cross:center" class="icon"><img src="static/front/images/chart.png" alt=""></p>
                <p class="name">图表资料</p>
            </a>
        </li>
        <li id="news" flex="dir:top cross:center main:center" class="footer-list-item">
            <a href="release/to_news">
                <p flex="main:center cross:center" class="icon"><img src="static/front/images/gg.png" alt=""></p>
                <p class="name">开奖公告</p>
            </a>
        </li>
        <li id="faq" flex="dir:top cross:center main:center" class="footer-list-item">
            <a href="release/to_faq">
                <p flex="main:center cross:center" class="icon"><img src="static/front/images/faq.png" alt=""></p>
                <p class="name">FAQ</p>
            </a>
        </li>
        <li id="about" flex="dir:top cross:center main:center" class="footer-list-item">
            <a href="release/to_about">
                <p flex="main:center cross:center" class="icon"><img src="static/front/images/gywm.png" alt=""></p>
                <p class="name">关于我们</p>
            </a>
        </li>
    </ul>
</footer>

<script type="text/javascript">
    // 移除高亮样式
    $(".footer-list-item").removeClass("is-active");
    var flag = '${flag}';
    // 添加高亮样式
    $("#" + flag).addClass("is-active");

    /*单机切换*/
    $(".tab").on("click",".tab-item",function(){
        /*每个li下属的div*/
        var divShow = $(".xs");
        /*利用selected进行判断*/
        if (!$(this).hasClass("tab-active")) {
            /*li标签的顺序和div的顺序是对应的，获取索引*/
            var index = $(this).index();
            /*当前对象设置class属性*/
            $(this).addClass("tab-active");
            /*移除其他同级元素属性*/
            $(this).siblings(".tab-item").removeClass("tab-active");
            /*展示当前li对应的div内容,利用方法显示和隐藏*/
            $(divShow[index]).show();
            /*隐藏同级元素*/
            $(divShow[index]).siblings(".xs").hide();
        }
    });

    // FAQ显示隐藏
    $('.accordion-title').click(function () {
        $(this).next('.accordion-content').slideToggle()
        $(this).children('.icon').toggleClass('rotate') //图标旋转
    })

</script>
</body>

</html>
