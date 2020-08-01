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
					
					<form action="shares_order/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="SHARES_ORDER_ID" id="SHARES_ORDER_ID" value="${pd.SHARES_ORDER_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">创建时间:</td>
								<td><input type="text" name="GMT_CREATE" id="GMT_CREATE" value="${pd.GMT_CREATE}" maxlength="32" placeholder="这里输入创建时间" title="创建时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">更新时间:</td>
								<td><input type="text" name="GMT_MODIFIED" id="GMT_MODIFIED" value="${pd.GMT_MODIFIED}" maxlength="32" placeholder="这里输入更新时间" title="更新时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">商品名称:</td>
								<td><input type="text" name="PROD_NAME" id="PROD_NAME" value="${pd.PROD_NAME}" maxlength="255" placeholder="这里输入商品名称" title="商品名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">价格:</td>
								<td><input type="text" name="PRICE" id="PRICE" value="${pd.PRICE}" maxlength="22" placeholder="这里输入价格" title="价格" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">状态:</td>
								<td><input type="text" name="STATUS" id="STATUS" value="${pd.STATUS}" maxlength="30" placeholder="这里输入状态" title="状态" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">总数:</td>
								<td><input type="text" name="TOTAL" id="TOTAL" value="${pd.TOTAL}" maxlength="22" placeholder="这里输入总数" title="总数" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">余数:</td>
								<td><input type="text" name="REMAINDER" id="REMAINDER" value="${pd.REMAINDER}" maxlength="22" placeholder="这里输入余数" title="余数" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">编号:</td>
								<td><input type="text" name="NUMBER_CODE" id="NUMBER_CODE" value="${pd.NUMBER_CODE}" maxlength="100" placeholder="这里输入编号" title="编号" style="width:98%;"/></td>
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
			if($("#GMT_CREATE").val()==""){
				$("#GMT_CREATE").tips({
					side:3,
		            msg:'请输入创建时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#GMT_CREATE").focus();
			return false;
			}
			if($("#GMT_MODIFIED").val()==""){
				$("#GMT_MODIFIED").tips({
					side:3,
		            msg:'请输入更新时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#GMT_MODIFIED").focus();
			return false;
			}
			if($("#PROD_NAME").val()==""){
				$("#PROD_NAME").tips({
					side:3,
		            msg:'请输入商品名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PROD_NAME").focus();
			return false;
			}
			if($("#PRICE").val()==""){
				$("#PRICE").tips({
					side:3,
		            msg:'请输入价格',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PRICE").focus();
			return false;
			}
			if($("#STATUS").val()==""){
				$("#STATUS").tips({
					side:3,
		            msg:'请输入状态',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#STATUS").focus();
			return false;
			}
			if($("#TOTAL").val()==""){
				$("#TOTAL").tips({
					side:3,
		            msg:'请输入总数',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TOTAL").focus();
			return false;
			}
			if($("#REMAINDER").val()==""){
				$("#REMAINDER").tips({
					side:3,
		            msg:'请输入余数',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#REMAINDER").focus();
			return false;
			}
			if($("#NUMBER_CODE").val()==""){
				$("#NUMBER_CODE").tips({
					side:3,
		            msg:'请输入编号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#NUMBER_CODE").focus();
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