<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <base href="<%=basePath%>">
    <!-- 下拉框 -->
    <link rel="stylesheet" href="static/ace/css/chosen.css"/>
    <!-- jsp文件头和头部 -->
    <%@ include file="../../system/index/top.jsp" %>
    <!-- 日期框 -->
    <link rel="stylesheet" href="static/ace/css/datepicker.css"/>
    <style>
        .hover:hover {
            color: redEnvelope !important;
            cursor: pointer;
        }

        html, body {
            margin: 0px;
            padding: 0px;
            width: 100%;
            height: 100%;
            overflow: hidden;
            font-family: Helvetica;
        }

        #tree {
            width: 100%;
            height: 100%;
        }

    </style>
</head>
<body class="no-skin">
<%--树形图--%>
<div id="tree"></div>

<!-- basic scripts -->
<!-- 页面底部js¨ -->
<%@ include file="../../system/index/foot.jsp" %>
<%--组织架构图--%>
<script src="static/front/js/orgchart.js" type="text/javascript" charset="utf-8"></script>

<!-- ace scripts -->
<script src="static/ace/js/ace/ace.js"></script>
<script type="text/javascript">
    $(top.hangge());//关闭加载状态

    window.onload = function () {
        $.get("account/getUserList", function (result) {
            if (result.success) {
                setDataList(result.data.item)
            }
        })
    };

    // 渲染数据列表
    function setDataList(data) {
        var nodes = [];

        for (let i = 0; i < data.length; i++) {
            var pd = data[i];
            var map = {};
            if (i == 0) {
                map["id"] = pd.ACCOUNT_ID;
                map["手机号"] = pd.PHONE;
                map["直推人数"] = pd.RECOMMENDED_NUMBER;
                map["持有股票数量"] = pd.USER_SHARES_SUM;
                map["金额"] = pd.MONEY;
                map["商城积分"] = pd.SHOP_INTEGRAL;
                map["USDT钱包"] = pd.USDT_WALLET;
                if (pd.USER_PORTRAIT) {
                    map["img"] = pd.USER_PORTRAIT;
                } else {
                    map["img"] = "http://placehold.it/150x150";
                }
            } else {
                map["id"] = pd.ACCOUNT_ID;
                map["pid"] = pd.RECOMMENDER_ID;
                map["手机号"] = pd.PHONE;
                map["直推人数"] = pd.RECOMMENDED_NUMBER;
                map["持有股票数量"] = pd.USER_SHARES_SUM;
                map["金额"] = pd.MONEY;
                map["商城积分"] = pd.SHOP_INTEGRAL;
                map["USDT钱包"] = pd.USDT_WALLET;
                if (pd.USER_PORTRAIT) {
                    map["img"] = pd.USER_PORTRAIT;
                } else {
                    map["img"] = "http://placehold.it/150x150";
                }
            }
            nodes.push(map)
        }

        for (var i = 0; i < nodes.length; i++) {
            nodes[i].field_number_children = childCount(nodes[i].id);
        }

        function childCount(id) {
            let count = 0;
            for (var i = 0; i < nodes.length; i++) {
                if (nodes[i].pid == id) {
                    count++;
                    count += childCount(nodes[i].id);
                }
            }
            return count;
        }

        OrgChart.templates.rony.field_number_children = '<circle cx="60" cy="110" r="15" fill="#F57C00"></circle><text fill="#ffffff" x="60" y="115" text-anchor="middle">{val}</text>';

        var chart = new OrgChart(document.getElementById("tree"), {
            template: "rony",
            collapse: {
                level: 3
            },
            nodeBinding: {
                field_0: "手机号",
                field_1: "直推人数",
                field_2: "持有股票数量",
                field_3: "金额",
                field_4: "商城积分",
                field_5: "USDT钱包",
                img_0: "img",
                field_number_children: "field_number_children"
            },
            nodes: nodes
        });
    }


</script>


</body>
</html>