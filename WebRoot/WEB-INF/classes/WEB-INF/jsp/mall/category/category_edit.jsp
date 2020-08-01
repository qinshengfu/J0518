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
					
					<form action="category/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="CATEGORY_ID" id="CATEGORY_ID" value="${pd.CATEGORY_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">产品类目名称:</td>
								<td><input type="text" name="CATEGORY_NAME" id="CATEGORY_NAME" value="${pd.CATEGORY_NAME}" maxlength="100" placeholder="这里输入产品类目名称" title="产品类目名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">类目图标:</td>
								<td><input type="text" name="ICON" id="ICON" value="${pd.ICON}" maxlength="255" placeholder="这里输入类目图标" title="类目图标" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">类目的显示图片:</td>
								<td><input type="text" name="PIC" id="PIC" value="${pd.PIC}" maxlength="255" placeholder="这里输入类目的显示图片" title="类目的显示图片" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">排序:</td>
								<td><input type="number" name="SEQ" id="SEQ" value="${pd.SEQ}" maxlength="32" placeholder="这里输入排序" title="排序" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">状态 1：正常,0：下线:</td>
								<td><input type="text" name="STATUS" id="STATUS" value="${pd.STATUS}" maxlength="2" placeholder="这里输入状态 1：正常,0：下线" title="状态 1：正常,0：下线" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">分类层级:</td>
								<td><input type="number" name="GRADE" id="GRADE" value="${pd.GRADE}" maxlength="32" placeholder="这里输入分类层级" title="分类层级" style="width:98%;"/></td>
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
			if($("#CATEGORY_NAME").val()==""){
				$("#CATEGORY_NAME").tips({
					side:3,
		            msg:'请输入产品类目名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CATEGORY_NAME").focus();
			return false;
			}
			if($("#ICON").val()==""){
				$("#ICON").tips({
					side:3,
		            msg:'请输入类目图标',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ICON").focus();
			return false;
			}
			if($("#PIC").val()==""){
				$("#PIC").tips({
					side:3,
		            msg:'请输入类目的显示图片',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PIC").focus();
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
			if($("#STATUS").val()==""){
				$("#STATUS").tips({
					side:3,
		            msg:'请输入状态 1：正常,0：下线',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#STATUS").focus();
			return false;
			}
			if($("#GRADE").val()==""){
				$("#GRADE").tips({
					side:3,
		            msg:'请输入分类层级',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#GRADE").focus();
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