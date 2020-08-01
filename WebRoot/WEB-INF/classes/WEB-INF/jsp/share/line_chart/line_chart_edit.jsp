<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
	<head>
	<base href="<%=basePath%>">
	<!-- 下拉框 -->
	<link rel="stylesheet" href="static/ace/css/chosen.css" />
	<!-- jsp文件头和头部 -->
	<%@ include file="../../system/index/top.jsp"%>
	<!-- 日期框 -->
	<link rel="stylesheet" href="static/ace/css/datepicker.css" />
</head>
<body class="no-skin">
<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
	<!-- /section:basics/sidebar -->
	<div class="main-content">
		<div class="main-content-inner">
			<div class="page-content">
				<div class="row">
					<div class="col-xs-12">
					
					<form action="line_chart/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="LINE_CHART_ID" id="LINE_CHART_ID" value="${pd.LINE_CHART_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">今日开盘价:</td>
								<td><input type="number" name="OPEN_PRICE" id="OPEN_PRICE" value="${pd.OPEN_PRICE}" maxlength="32" placeholder="这里输入今日开盘价" title="今日开盘价" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">昨日收盘价:</td>
								<td><input type="number" name="CLOSING_PRICE" id="CLOSING_PRICE" value="${pd.CLOSING_PRICE}" maxlength="32" placeholder="这里输入昨日收盘价" title="昨日收盘价" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">当前价格:</td>
								<td><input type="number" name="PRICE" id="PRICE" value="${pd.PRICE}" maxlength="32" placeholder="这里输入当前价格" title="当前价格" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">今日最高价:</td>
								<td><input type="number" name="MAX_PRICE" id="MAX_PRICE" value="${pd.MAX_PRICE}" maxlength="32" placeholder="这里输入今日最高价" title="今日最高价" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">今日最低价:</td>
								<td><input type="number" name="MIN_PRICE" id="MIN_PRICE" value="${pd.MIN_PRICE}" maxlength="32" placeholder="这里输入今日最低价" title="今日最低价" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="text-align: center;" colspan="10">
									<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
									<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
								</td>
							</tr>
						</table>
						</div>
						<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
					</form>
					</div>
					<!-- /.col -->
				</div>
				<!-- /.row -->
			</div>
			<!-- /.page-content -->
		</div>
	</div>
	<!-- /.main-content -->
</div>
<!-- /.main-container -->


	<!-- 页面底部js¨ -->
	<%@ include file="../../system/index/foot.jsp"%>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		<script type="text/javascript">
		$(top.hangge());
		//保存
		function save(){
			if($("#OPEN_PRICE").val()==""){
				$("#OPEN_PRICE").tips({
					side:3,
		            msg:'请输入今日开盘价',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#OPEN_PRICE").focus();
			return false;
			}
			if($("#CLOSING_PRICE").val()==""){
				$("#CLOSING_PRICE").tips({
					side:3,
		            msg:'请输入昨日收盘价',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CLOSING_PRICE").focus();
			return false;
			}
			if($("#PRICE").val()==""){
				$("#PRICE").tips({
					side:3,
		            msg:'请输入当前价格',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PRICE").focus();
			return false;
			}
			if($("#MAX_PRICE").val()==""){
				$("#MAX_PRICE").tips({
					side:3,
		            msg:'请输入今日最高价',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#MAX_PRICE").focus();
			return false;
			}
			if($("#MIN_PRICE").val()==""){
				$("#MIN_PRICE").tips({
					side:3,
		            msg:'请输入今日最低价',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#MIN_PRICE").focus();
			return false;
			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		
		$(function() {
			//日期框
			$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
		});
		</script>
</body>
</html>