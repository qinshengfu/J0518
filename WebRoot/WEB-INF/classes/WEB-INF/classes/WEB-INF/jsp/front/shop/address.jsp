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
    <h1 class="mui-title">收货地址</h1>
</header>
<div class="mui-content">

    <div id="dataList">
        <div class="adress">
            <div class="adress_div">
                <button type="button">默认</button>
                <div class="adress_div_2">
                    <div>张丽丽 <p>12345678974</p></div>
                    <p>北京市城东区新华路168号</p>
                </div>
            </div>
            <a href="front/to_mallEditAddress"><i class="iconfont icon-bianji"></i></a>
        </div>
    </div>

    <!-- 按钮 -->
    <a href="front/to_mallAddAddress">
        <button type="button" class="mui-btn mui-btn-blue mui-btn-block mui_btn"><i
                class="iconfont icon-weibiaoti37"></i>新增
        </button>
    </a>
</div>
<script type="text/javascript">

    $(function () {
        $.get("front/getAddressList", function (result) {
            if (result.success) {
                setDataList(result.data.item)
            }
        }, "json");
    });

    function setDataList(data) {
        // 父级容器
        var divDom = $("#dataList");
        divDom.html("");
        // 循环渲染
        for (let i = 0; i < data.length; i++) {
            var pd = data[i];
            var str = "";
            if ('${pd.tag}' == 1) {
                str += "<div class=\"adress\" onclick='toOrderPayment(" + pd.USER_ADDR_ID + ")'>"
            } else if ('${pd.tag}' == 2) {
                str += "<div class=\"adress\" onclick='toTakeProd(" + pd.USER_ADDR_ID + ")'>"
            } else {
                str += "<div class=\"adress\">"
            }
            str += "        <div class=\"adress_div\">\n";
            if (pd.COMMON_ADDR == 1) {
                str += "<button type=\"button\">默认</button>\n"
            }
            str += "        <div class=\"adress_div_2\">\n" +
                "               <div>" + pd.RECEIVER + " <p>" + pd.MOBILE + "</p></div>\n" +
                "               <p>" + pd.PROVINCE + pd.CITY + pd.AREA + pd.ADDR + "</p>\n" +
                "           </div>\n" +
                "        </div>\n" +
                "        <a href=\"front/to_mallEditAddress/" + pd.USER_ADDR_ID + "\"><i class=\"iconfont icon-bianji\"></i></a>\n" +
                "    </div>";
            divDom.append(str);
        }
    }

    // 重定向订单付款页面
    function toOrderPayment(addrId) {
        window.location.replace("front/to_mallOrderDetails/${pd.ORDER_ITEM_ID}?USER_ADDR_ID=" + addrId);
    }

    // 重定向提货订单确认页面
    function toTakeProd(addrId) {
        window.location.replace("front/toConfirmOrder/?USER_ADDR_ID=" + addrId + "&num=${pd.num}&STOCK_PROD_BUY_ID=${pd.STOCK_PROD_BUY_ID}");
    }



</script>
</body>

</html>
