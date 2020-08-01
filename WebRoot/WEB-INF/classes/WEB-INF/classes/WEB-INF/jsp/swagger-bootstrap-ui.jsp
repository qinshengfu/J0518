<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
</head>
<body class="no-skin">

<!-- 页面底部js¨ -->
<%@ include file="system/index/foot.jsp"%>

<script type="text/javascript">
    $(top.hangge());//关闭加载状态

    window.onload=function(){
        window.location.href = "doc.html";
    };
</script>


</body>
</html>