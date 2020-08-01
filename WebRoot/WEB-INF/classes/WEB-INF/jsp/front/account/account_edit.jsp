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
					
					<form action="account/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="ACCOUNT_ID" id="ACCOUNT_ID" value="${pd.ACCOUNT_ID}"/>
						<input type="hidden" name="PHONE" value="${pd.PHONE}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">登录密码:</td>
								<td><input type="password" name="LOGIN_PASSWORD" id="LOGIN_PASSWORD" value="" maxlength="155" placeholder="这里输入登录密码" title="登录密码" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">安全密码:</td>
								<td><input type="password" name="SECURITY_PASSWORD" id="SECURITY_PASSWORD" value="" maxlength="155" placeholder="这里输入安全密码" title="安全密码" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">USDT钱包:</td>
								<td><input type="number" name="USDT_WALLET" id="USDT_WALLET" value="${pd.USDT_WALLET}" maxlength="32" placeholder="这里输入USDT钱包" title="USDT钱包" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">金额:</td>
								<td><input type="number" name="MONEY" id="MONEY" value="${pd.MONEY}" maxlength="32" placeholder="这里输入金额" title="金额" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">商城积分:</td>
								<td><input type="number" name="SHOP_INTEGRAL" id="SHOP_INTEGRAL" value="${pd.SHOP_INTEGRAL}" maxlength="32" placeholder="这里输入商城积分" title="商城积分" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">会员等级:</td>
								<td><input type="text" name="USER_RANK" id="USER_RANK" value="${pd.USER_RANK}" maxlength="55" placeholder="这里输入会员等级" title="会员等级" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">用户状态:</td>
								<td>
									<select name="USER_STATE">
										<c:if test="${pd.USER_STATE == 1}">
											<option value="1" disabled selected hidden style="color: #715d3e;">正常</option>
										</c:if>
										<c:if test="${pd.USER_STATE == 0}">
											<option value="0" disabled selected hidden style="color: #715d3e;">封号</option>
										</c:if>
										<option value="1">正常</option>
										<option value="0">封号</option>
									</select>
								</td>


							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">是否特殊账号:</td>
								<td>
									<select name="IS_SPECIAL">
										<c:if test="${pd.IS_SPECIAL == 1}">
											<option value="1" disabled selected hidden style="color: #715d3e;">正常</option>
										</c:if>
										<c:if test="${pd.IS_SPECIAL == 0}">
											<option value="0" disabled selected hidden style="color: #715d3e;">特殊</option>
										</c:if>
										<option value="1">正常</option>
										<option value="0">特殊</option>
									</select>
								</td>
							</tr>
							<%--<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">是否实名认证:</td>
								<td>
									<select name="IS_REAL">
										<c:if test="${pd.IS_REAL == 1}">
											<option value="1" disabled selected hidden style="color: #715d3e;">是</option>
										</c:if>
										<c:if test="${pd.IS_REAL == 0}">
											<option value="0" disabled selected hidden style="color: #715d3e;">否</option>
										</c:if>
										<option value="1">是</option>
										<option value="0">否</option>
									</select>
								</td>
							</tr>--%>
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
			if($("#USDT_WALLET").val()==""){
				$("#USDT_WALLET").tips({
					side:3,
		            msg:'请输入USDT钱包',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#USDT_WALLET").focus();
			return false;
			}
			if($("#MONEY").val()==""){
				$("#MONEY").tips({
					side:3,
		            msg:'请输入金额',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#MONEY").focus();
			return false;
			}
			if($("#SHOP_INTEGRAL").val()==""){
				$("#SHOP_INTEGRAL").tips({
					side:3,
		            msg:'请输入商城积分',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SHOP_INTEGRAL").focus();
			return false;
			}
			if($("#USER_RANK").val()==""){
				$("#USER_RANK").tips({
					side:3,
		            msg:'请输入会员等级',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#USER_RANK").focus();
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