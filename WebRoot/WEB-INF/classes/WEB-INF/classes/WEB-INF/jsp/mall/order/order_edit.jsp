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
		<%--layui--%>
		<link rel="stylesheet" href="static/front/layui/css/layui.css"/>
		<script src="static/front/layui/layui.js"></script>
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
					
					<form action="order/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="ORDER_ID" id="ORDER_ID" value="${pd.ORDER_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">物流单号:</td>
								<td><input type="text" name="DVY_FLOW_ID" id="DVY_FLOW_ID" value="${pd.DVY_FLOW_ID}" maxlength="100" placeholder="这里输入物流单号" title="物流单号" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">订单运费:</td>
								<td><input type="number" name="FREIGHT_AMOUNT" id="FREIGHT_AMOUNT" value="${pd.FREIGHT_AMOUNT}" maxlength="32" placeholder="这里输入订单运费" title="订单运费" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">发货时间:</td>
								<td><input class="timeItem" name="DVY_TIME" id="DVY_TIME" value="${pd.DVY_TIME}" type="text" readonly placeholder="发货时间" title="发货时间" style="width:98%;"/></td>
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
			if($("#DVY_FLOW_ID").val()==""){
				$("#DVY_FLOW_ID").tips({
					side:3,
		            msg:'请输入物流单号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DVY_FLOW_ID").focus();
			return false;
			}
			if($("#FREIGHT_AMOUNT").val()==""){
				$("#FREIGHT_AMOUNT").tips({
					side:3,
		            msg:'请输入订单运费',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#FREIGHT_AMOUNT").focus();
			return false;
			}
			if($("#DVY_TIME").val()==""){
				$("#DVY_TIME").tips({
					side:3,
		            msg:'请输入发货时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DVY_TIME").focus();
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


		layui.use('laydate', function () {
			// 执行一个laydate实例
			var laydate = layui.laydate;

			//日期时间
			laydate.render({
				elem: '.timeItem'
				, type: 'datetime'
			});
		});


		</script>
</body>
</html>