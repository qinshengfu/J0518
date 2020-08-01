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
<style type="text/css">
    .mui-card {
        box-shadow: none;
        margin: 0;
    }

    .mui-card .mui-input-group .mui-input-row {
        display: flex;
    }

    .order_detail .order_detail_div2 .order_detail_div2-btn button:last-child {
        padding: 10px 20px;
    }

</style>
<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title">确认订单</h1>
</header>
<div class="mui-content">
    <!-- 地址 -->
    <div class="order_adress" onclick='toUserAddress()' id="userAddress">

        <i class="iconfont icon-dizhi"></i>
        <div>
            <a href="front/to_mallAddress">
                <h4>张三 12345678987</h4>
                <p>北京市XXX区XX街道XX号</p>
            </a>
        </div>

    </div>
    <!-- 订单详情 -->
    <div class="order_detail" id="order_detail">

        <%--<div class="order_detail_div1">
            <div class="order_detail_div1_1">
                <img src="static/front/images/01.png">
                <div>
                    <div class="huanhang">牛奶美肤乳幼嫩修复</div>
                    <p class="price">￥168.00</p>
                </div>
            </div>
            <div class="order_detail_div1_2">
                <p>X1</p>
            </div>
        </div>
        <div class="order_detail_div2">
            <div class="order_detail_div2_1">
                <p>订单号：201810101234656</p>
                <p>创建时间：2018-10-10 12:34:06</p>
            </div>
            <div class="order_detail_div2_2">
                <div>合计：<span>￥168.00</span></div>
                <div class="order_detail_div2-btn">
                    <button type="button" class="ljfk">立即付款</button>
                </div>
            </div>
        </div>--%>

    </div>
</div>


</body>

<script>

    // 初始化
    $(function () {
        getTakeGoodsOrder();

        var userAddressId = '${pd.USER_ADDR_ID}';

        if (userAddressId != '') {
            getUserAddressById(userAddressId);
        } else {
            getUserCommonAddress()
        }
    });

    // 获取提货订单信息
    function getTakeGoodsOrder() {
        var url = "front/getTakeGoodsOrder?SHARES_ORDER_ID=${pd.SHARES_ORDER_ID}";
        $.get(url, function (result) {
            if (result.success) {
                setOrderItemDate(result.data);
            }
        }, "json")
    }

    // 渲染订单信息
    function setOrderItemDate(data) {
        var divDom = $("#order_detail");
        // 先清空元素
        divDom.html("");

        var num = '${pd.num}'

        var str = '';
        str += "<div class=\"order_detail_div1\">\n" +
            "            <div class=\"order_detail_div1_1\">\n" +
            // "                <img src=\"" + data.PIC + "\">\n" +
            "                <div>\n" +
            "                    <div class=\"huanhang\">" + data.PROD_NAME + "</div>\n" +
            "                    <p class=\"price\">单价：￥" + data.PRICE + "</p>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "            <div class=\"order_detail_div1_2\">\n" +
            "                <p>X " + num + "</p>\n" +
            "            </div>\n" +
            "   </div>\n" +
            "   <div class=\"order_detail_div2\">\n" +
            "        <div class=\"order_detail_div2_2\">\n" +
            // "            <div>合计：<span>￥" + data.PRODUCT_TOTAL_AMOUNT + "</span></div>\n" +
            "            <div class=\"order_detail_div2-btn\">\n" +
            "                <button type=\"button\" onclick='comfirm()' class=\"ljfk\">确认</button>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>";
        divDom.append(str)
    }

    // 获取用户默认收货地址
    function getUserCommonAddress() {
        var url = "front/getUserCommonAddress?USER_ID=${user.ACCOUNT_ID}";
        $.get(url, function (result) {
            if (result.success) {
                setUserAddress(result.data)
            }
        }, "json")
    }

    // 根据地址DI获取收货地址
    function getUserAddressById(id) {
        var url = "front/getAddress?USER_ADDR_ID=" + id;
        $.get(url, function (result) {
            if (result.success) {
                setUserAddress(result.data)
            }
        }, "json")
    }

    // 渲染用户地址
    function setUserAddress(data) {
        var divDom = $("#userAddress");
        divDom.html("");

        var address = data.PROVINCE + data.CITY + data.AREA + data.ADDR;
        var str = '';
        str += "<i class=\"iconfont icon-dizhi\"></i>\n" +
            "   <div>\n" +
            "       <input hidden id='USER_ADDR_ID' value='" + data.USER_ADDR_ID + "'>\n" +
            "       <div>\n" +
            "           <h4>" + data.RECEIVER + "&nbsp;" + data.MOBILE + "</h4>\n" +
            "           <p>" + address + "</p>\n" +
            "       </div>\n" +
            "   </div>";
        // 从父级节点后面添加元素
        divDom.append(str)
    }

    // 重定向到用户地址列表
    function toUserAddress() {
        window.location.replace("front/to_mallAddress?tag=2&STOCK_PROD_BUY_ID=${pd.STOCK_PROD_BUY_ID}&num=${pd.num}");
    }

</script>

<script type="text/javascript">

    // 确认
    function comfirm() {
        layer.load();

        var orderId = '${pd.SHARES_ORDER_ID}';
        var num = '${pd.num}';
        var userAddressId = $("#USER_ADDR_ID").val();

        var content = '<div>\n' +
            '              <label>交易密码：</label>\n' +
            '              <input type="password" id="password">' +
            '          </div>';

        layer.open({
            title: [
                '请输入交易密码',
                'background-color:#207eff; color:#fff;'
            ]
            , anim: 'up'
            , content: content
            , btn: ['确认', '取消']
            , yes: function (index) {
                var password = $("#password").val();
                if (password == '') {
                    mui.toast("请输入交易密码")
                    return false;
                }
                server_veriflcation(orderId, num, userAddressId, password);
                layer.close(index)
            }
            , btn2: function () {
                layer.closeAll('loading')
            }
        });
    }

    // 服务端校验
    function server_veriflcation(orderId, num, userAddressId, password) {

        var url = "front/takeProdCheck";
        $.post(url, {
            "SHARES_ORDER_ID": orderId,
            "USER_ADDR_ID": userAddressId,
            "num": num,
            "pass": password
        }, function (result) {
            layer.closeAll('loading');
            mui.toast(result.message);
            if (result.success) {
                window.history.go(-1);
            }

        })
    }


</script>

</html>
