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

<link rel="stylesheet" type="text/css" href="static/front/css/ectouch.css" />


<head>
	<title></title>
</head>

	<body>
		<header class="mui-bar mui-bar-nav">
			<h1 class="mui-title">分类</h1>
		</header>
		<div class="mui-content">
			<!--分类搜索-->
			<div class="con">
				<aside>
					<div class="menu-left scrollbar-none" id="sidebar">
						<ul>
							<li class="active">精品</li>
							<li>酒店</li>
							<li>汽车</li>
						</ul>
					</div>
				</aside>
				<section class="menu-right padding-all j-content">
					<ul>
						<li class="w-3">
							<a href="shopdetail.html"></a><img src="../../images/02.png" /><span>时尚女鞋</span><div>￥6900.00</div>
						</li>
						<li class="w-3">
							<a href="shopdetail.html"></a><img src="../../images/02.png" /><span>时尚女鞋</span><div>￥69.00</div>
						</li>
						<li class="w-3">
							<a href="shopdetail.html"></a><img src="../../images/02.png" /><span>时尚女鞋</span><div>￥69.00</div>
						</li>
					</ul>
				</section>
				<section class="menu-right padding-all j-content" style="display:none">
					<ul>
						<li class="w-3">
							<a href=""></a><img src="../../images/02.png" /><span>时尚女鞋</span><div>￥69.00</div>
						</li>
					</ul>
				</section>
				<section class="menu-right padding-all j-content" style="display:none">
					<ul>
						<li class="w-3">
							<a href=""></a><img src="../../images/02.png" /><span>时尚女鞋</span><div>￥78000.00</div>
						</li>
					</ul>
				</section>
			</div>

		</div>

		<script type="text/javascript">
			$(function($) {
				$('#sidebar ul li').click(function() {
					$(this).addClass('active').siblings('li').removeClass('active');
					var index = $(this).index();
					$('.j-content').eq(index).show().siblings('.j-content').hide();
				})
			})
		</script>
	</body>

</html>
