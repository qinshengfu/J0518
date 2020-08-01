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
    <h1 class="mui-title">安全中心</h1>
</header>
<div class="mui-content">
    <!-- list -->
    <div class="mine_list">
        <ul class="mui-table-view">
            <li class="mui-table-view-cell">
                <a class="mui-navigate-right" href="front/to_editPassword">
                    <i class="iconfont icon-dingdan4"></i>修改密码
                </a>
            </li>
            <li class="mui-table-view-cell">
                <a class="mui-navigate-right" href="front/to_editSecret">
                    <i class="iconfont icon-dingdan4"></i>修改密保
                </a>
            </li>
        </ul>
    </div>

</div>

</body>

</html>
