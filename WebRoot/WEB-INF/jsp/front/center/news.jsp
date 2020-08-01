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
    <h1 class="mui-title">系统咨询</h1>
</header>
<div class="mui-content">
    <div class="news_list">
        <ul class="mui-table-view" id="dataList">

        </ul>
    </div>
</div>

</body>

<script>

    $(function () {
        getAllNewsList()
    });

    // 获取所有新闻列表
    function getAllNewsList() {
        $.get("front/allNewsList", function (result) {
            if (result.success) {
                setNewsList(result.data.item)
            }
        }, "json")
    }

    // 设置新闻列表
    function setNewsList(data) {
        // 获取父级节点
        var fatherNode = $("#dataList");
        // 设置内容
        var str = "";
        for (let i = 0; i < data.length; i++) {
            var pd = data[i];
            str += "<li class=\"mui-table-view-cell\">\n" +
                "       <a class=\"mui-navigate-right\" href=\"front/to_newsDetail/" + pd.SYS_NEWS_ID + "\">\n" +
                "            " + pd.TITLE + "\n" +
                "       </a>\n" +
                "   </li>";
        }
        fatherNode.append(str)

    }

</script>

</html>
