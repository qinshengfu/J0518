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
					
					<form action="shares_prod/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="SHARES_PROD_ID" id="SHARES_PROD_ID" value="${pd.SHARES_PROD_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover"
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">商品名称:</td>
								<td><input type="text" name="PROD_NAME" id="PROD_NAME" value="${pd.PROD_NAME}" maxlength="255" placeholder="这里输入商品名称" title="商品名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">单位:</td>
								<td><input type="text" name="UNIT" id="UNIT" value="${pd.UNIT}" maxlength="50" placeholder="这里输入单位" title="单位" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">状态:</td>
								<td>
									<select id="STATUS" name="STATUS">
										<c:if test="${pd.STATUS == null}">
											<option value="" selected hidden style="color: #715d3e;">请选择状态</option>
										</c:if>
										<c:if test="${pd.STATUS != null}">
											<option value="${pd.STATUS}" selected hidden style="color: #715d3e;">${pd.STATUS}</option>
										</c:if>
										<option value="发行中">发行中</option>
										<option value="交易中">交易中</option>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">开盘价:</td>
								<td><input type="number" name="OPEN_PRICE" id="OPEN_PRICE" value="${pd.OPEN_PRICE}" maxlength="22" placeholder="这里输入开盘价" title="开盘价" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">总数:</td>
								<td><input type="number" name="TOTAL" id="TOTAL" value="${pd.TOTAL}" maxlength="22" placeholder="这里输入总数" title="总数" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">余数:</td>
								<td><input type="number" name="REMAINDER" id="REMAINDER" value="${pd.REMAINDER}" maxlength="22" placeholder="这里输入余数" title="余数" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">排序:</td>
								<td><input type="number" name="SEQ" id="SEQ" value="${pd.SEQ}" maxlength="22" placeholder="这里输入排序" title="排序" style="width:98%;"/></td>
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
			if($("#UNIT").val()==""){
				$("#UNIT").tips({
					side:3,
		            msg:'请输入单位',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#UNIT").focus();
			return false;
			}
			if($("#STATUS").val()==''){
				$("#STATUS").tips({
					side:3,
		            msg:'请选择状态',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#STATUS").focus();
			return false;
			}
			if($("#OPEN_PRICE").val()==""){
				$("#OPEN_PRICE").tips({
					side:3,
		            msg:'请输入开盘价',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#OPEN_PRICE").focus();
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
			if($("#SEQ").val()==""){
				$("#SEQ").tips({
					side:3,
		            msg:'请输入排序',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SEQ").focus();
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