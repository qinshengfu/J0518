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


<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title">商品详情</h1>
</header>
<div class="mui-content">
    <!-- 轮播图 -->
    <div class="swiper-container">
        <div class="swiper-wrapper">
            <c:forTokens items="${pd.IMGS}" delims="," var="imgUrl">
                <div class="swiper-slide"><img src="${imgUrl}"></div>
            </c:forTokens>
        </div>
        <div class="swiper-pagination"></div>
    </div>
    <!-- 名称 -->
    <div class="shop_detai">
        <div>名称：${pd.PROD_NAME}</div>
        <p class="price">价格：￥${pd.PRICE}</p>
        <p>已销：${pd.SOLD_NUM} &nbsp;&nbsp; 库存：${pd.TOTAL_STOCKS} </p>
    </div>

    <!-- 选择数量 -->
    <div class="shop_num flex2">
        <label>请选择数量</label>
        <div id="data_num" class="mui-numbox" data-numbox-step='1' data-numbox-min='1'
             data-numbox-max='${pd.TOTAL_STOCKS}'>
            <button class="mui-btn mui-numbox-btn-minus" type="button">-</button>
            <input class="mui-numbox-input" id="num" value="" type="number"/>
            <button class="mui-btn mui-numbox-btn-plus" type="button">+</button>
        </div>
    </div>

    <!-- 商品详情 -->
    <div class="shop_neir">
        <h4>【商品详情】</h4>
        <div>
            ${pd.CONTENT}
        </div>
    </div>

    <!-- 底部 -->
    <div class="shop_bot">
        <div class="shop_bot_div1">
            <a href="front/to_mallCart">
                <i class="iconfont icon-gouwuche7"></i>购物车
            </a>
        </div>
        <div class="shop_bot_div2">
            <button type="button" onclick="addCart()" class="mui-btn mui-btn-blue">加入购物车</button>
            <a href="javascript:purchase()">
                <button type="button" class="mui-btn mui-btn-blue">立即购买</button>
            </a>
        </div>
    </div>
</div>
<script type="text/javascript">
    mui.init()

    // 加入购物车
    function addCart() {
        //加载层-默认风格
        layer.load();
        var num = $("#num").val();
        $.post("front/addToCart", {"PROD_ID": '${pd.PROD_ID}', "num": num}, function (result) {
            // 关闭加载层
            layer.closeAll('loading');
            mui.toast(result.message);
        })
    }

    // 购买商品
    function purchase() {
        var num = $("#num").val();
        $.post("front/placeAnOrder", {"PROD_ID": '${pd.PROD_ID}', "num": num}, function (result) {
            if (result.success) {
                // 跳转订单详情
                window.location.href = "front/to_mallOrderDetails/" + result.data.orderItemId + "?tag=shop";
            } else {
                mui.toast(result.message);
                setTimeout(function () {
                    if (result.message == '请先添加收货地址') {
                        window.location.href = "front/to_mallAddAddress";
                        return false;
                    } else {
                        window.location.reload();

                    }
                }, 1200);
            }
        })
    }

    //轮播图
    var mySwiper = new Swiper('.swiper-container', {
        autoplay: true,
        pagination: {
            el: '.swiper-pagination',
        },
    })
</script>
</body>

</html>
