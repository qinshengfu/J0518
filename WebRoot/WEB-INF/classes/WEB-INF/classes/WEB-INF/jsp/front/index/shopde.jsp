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
    .shares li {
        margin: 10px;
        padding: 10px;
        background-color: white;
        border-radius: 4px;
        font-size: 12px;
        text-align: center;
    }

    .shares li p {
        font-size: 12px;
    }

    .shar_tit {
        color: #0062CC;
    }

    .zhang {
        color: red;
    }

    .die {
        color: #0070E7;
    }

    .shares li button {
        font-size: 12px;
    }
</style>

<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title">商品详情</h1>
</header>
<div class="mui-content">
    <ul class="shares" id="dataList">

        <%--        <li class="flex2">
                    <div class="shar_tit">邮票模板</div>
                    <div>
                        <span>最新</span>
                        <p class="zhang">5103.00</p>
                    </div>
                    <div>
                        <span>涨跌</span>
                        <p class="die">-4.00</p>
                    </div>
                    <div>
                        <span>幅度</span>
                        <p class="die">-0.08%</p>
                    </div>
                    <button type="button" class="mui-btn mui-btn-blue">添加</button>
                </li>--%>

    </ul>
</div>

</body>

<script type="text/javascript">
    // 初始化
    $(function () {
        getStockList()
    });

    // 获取所有股票版块信息
    function getStockList() {
        var url = "front/getStockInfoList";
        $.get(url, function (result) {
            if (result.success) {
                setStockList(result.data.item)
            }
        })
    }

    // 渲染行情的最新股票信息
    function setStockList(data) {
        var ulDom = $('#dataList');
        ulDom.html("")

        for (let i = 0; i < data.length; i++) {
            var pd = data[i];

            var str = '';
            str = '<li class="flex2">\n' +
                '     <div class="shar_tit">' + pd.PROD_NAME + '</div>\n' +
                '     <div>\n' +
                '         <span>最新</span>\n' +
                '         <p class="zhang">' + pd.PRICE + '</p>\n' +
                '     </div>\n' +
                '     <div>\n' +
                '         <span>涨跌</span>\n' +
                '         <p class="die">' + pd.WAVE + '</p>\n' +
                '     </div>\n' +
                '     <div>\n' +
                '         <span>幅度</span>\n' +
                '         <p class="die">' + pd.RANGE + '%</p>\n' +
                '     </div>\n' +
                '     <button type="button" onclick="addStock(\'' + pd.SHARES_PROD_ID + '\', \'' + pd.PROD_NAME + '\')" class="mui-btn mui-btn-blue">添加</button>\n' +
                '  </li>';
            ulDom.append(str)
        }
    }

    // 添加股票版本到用户自选
    function addStock(id, name) {
        var url = "front/addUserShowStock";
        $.post(url, {"SHARES_PROD_ID": id, "STOCK_NAME": name}, function (result) {
            mui.toast(result.message);
            setTimeout(function () {
                if (result.success) {
                    window.location.href = "front/to_index"
                }
            }, 1000);
        })


    }

</script>

</html>
