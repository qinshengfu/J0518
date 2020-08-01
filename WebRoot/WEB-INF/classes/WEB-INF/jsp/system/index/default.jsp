<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" %>
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

<!-- jsp文件头和头部 -->
<%@ include file="../index/top.jsp"%>
<!-- 百度echarts -->
<script src="plugins/echarts/echarts.min.js"></script>
<script type="text/javascript">
setTimeout("top.hangge()",500);
</script>
	<style>
		.chart{
			width: 300px;
			height:300px;
			float:left;
		}
	</style>
</head>
<body class="no-skin">

	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="hr hr-18 dotted hr-double"></div>
					<div class="row">
						<div class="col-xs-12">

							<div class="alert alert-block alert-success">
								<button type="button" class="close" data-dismiss="alert">
									<i class="ace-icon fa fa-times"></i>
								</button>
								<i class="ace-icon fa fa-check green"></i>
								欢迎使用 微盘交易  后台管理&nbsp;&nbsp;
								<strong class="green">
									&nbsp;
									<a href="" target="_blank"><small></small></a>
								</strong>
							</div>
							<%--在线人数--%>
<%--							<div id="userCount" class="chart"></div>--%>



						</div>
						<!-- /.col -->
					</div>
					<!-- /.row -->
				</div>
				<!-- /.page-content -->
			</div>
		</div>
		<!-- /.main-content -->


		<!-- 返回顶部 -->
		<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
			<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
		</a>

	</div>
	<!-- /.main-container -->

	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="../index/foot.jsp"%>
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- inline scripts related to this page -->
	<script type="text/javascript">
		$(top.hangge());

		$(function() {
			online();
		});

		var websocketonline;//websocke对象
		var userCount = 0;	//在线总数
		function online(){
			if (window.WebSocket) {
				websocketonline = new WebSocket(encodeURI('ws://'+top.oladress)); //oladress在main.jsp页面定义
				websocketonline.onopen = function() {
					websocketonline.send('[QQ313596790]fhadmin');//连接成功
				};
				websocketonline.onerror = function() {
					//连接失败
				};
				websocketonline.onclose = function() {
					//连接断开
				};
				//消息接收
				websocketonline.onmessage = function(message) {
					var message = JSON.parse(message.data);
					if (message.type == 'count') {
						userCount = message.msg;
                        chart(userCount)
					}else if(message.type == 'userlist'){
						$.each(message.list, function(i, user){
							userCount = i+1;
                            chart(userCount)
						});
					}else if(message.type == 'addUser'){
						userCount = userCount+1;
                        chart(userCount)
					}
				};
			}
		}

		// 基于准备好的dom，初始化echarts实例
		var userCountEchart = echarts.init(document.getElementById('userCount'));

		// 图表
		function chart(count) {
            // 指定图表的配置项和数据
            var userCountOption = {
                title: {
                    text: '在线人数'
                },
                tooltip: {},
                xAxis: {
                    data: ["在线人数"]
                },
                yAxis: {},
                series: [
                    {
                        name: '',
                        type: 'bar',
                        <%--data: [${pd.onlineNumber}],--%>
                        data: [count],
                        itemStyle: {
                            normal: {
                                color: function(params) {
                                    // build a color map as your need.
                                    var colorList = ['#FF9900'];
                                    return colorList[params.dataIndex];
                                }
                            }
                        }
                    }
                ]
            };
            // 使用刚指定的配置项和数据显示图表。
            userCountEchart.setOption(userCountOption);
		}









	</script>
<script type="text/javascript" src="static/ace/js/jquery.js"></script>



</body>
</html>