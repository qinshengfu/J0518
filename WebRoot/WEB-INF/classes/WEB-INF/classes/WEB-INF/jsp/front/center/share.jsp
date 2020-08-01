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

<body style="background-color: white;">
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title">邀请好友</h1>
</header>
<div class="mui-content">
    <div class="erweima">
        <img src="front/invitationCode" alt="邀请码">
        <!-- 链接 -->
        <div class="lj"><%=basePath%>release/to_register?code=${user.ACCOUNT_ID}</div>
        <button data-clipboard-action="copy"
                data-clipboard-text="<%=basePath%>release/to_register?code=${user.ACCOUNT_ID}" id="d_clip_button"
                type="button"
                class="mui-btn mui-btn-blue mui-btn-block mui_btn">复制链接
        </button>
    </div>
</div>

</body>

<script>
    $(function () {
        var clipboard = new ClipboardJS('#d_clip_button');
        clipboard.on('success', function (e) {
            mui.toast("复制成功");
            e.clearSelection();
        });

    })
</script>

</html>
