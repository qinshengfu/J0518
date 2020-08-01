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

<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title">修改密码</h1>
</header>
<div class="mui-content">
    <form id="Form" method="post">
        <div class="setup_inp">
            <div class="mui-input-row">
                <label>密码类型：</label>
                <select name="passwordType">
                    <option value="" hidden>请选择</option>
                    <option value="1">登录密码</option>
                    <option value="0">交易密码</option>
                </select>
            </div>

            <div class="mui-input-row">
                <label>旧密码：</label>
                <input type="password" name="oidPassword" class="mui-input-password" value="">
            </div>
            <div class="mui-input-row">
                <label>新密码：</label>
                <input type="password" name="newPassword" class="mui-input-password" value="">
            </div>
        </div>
        <button type="button" class="mui-btn mui-btn-blue mui-btn-block mui_btn">确认修改</button>
    </form>
</div>

</body>

<script>
    // 客户端验证
    mui(document.body).on('tap', '.mui-btn', function () {
        mui(".mui-btn").button('loading');//切换为loading状态
        check = true;
        mui(".mui-input-row input").each(function () {
            //若当前input为空，则alert提醒
            if (!this.value || this.value.trim() == "") {
                layer.tips('不允许为空', this, {
                    tips: [1, '#3595CC'],
                    time: 2000
                });
                mui(".mui-btn").button('reset'); //切换为reset状态(即重置为原始的button)
                check = false;
                return false;
            }
        }); //校验通过，继续执行业务逻辑
        if (check) {
            if (checkType()){
                mui(".mui-btn").button('reset'); //切换为reset状态(即重置为原始的button)
                return false;
            }
            server_verification()
        }
    });

    // 服务端校验
    function server_verification() {
        var url = "front/updateUserPass.do";
        //异步提交表单(先确保jquery.form.js已经引入了)
        var options = {
            url: url,
            success: function (result) {
                mui(".mui-btn").button('reset'); //切换为reset状态(即重置为原始的button)
                /*后台登录验证后*/
                if (result.success) {
                    mui.toast("修改成功");
                    setTimeout(function() {
                        toPage();
                    },1200);
                } else {
                    mui.toast(result.message);
                }
            }
        };
        $("#Form").ajaxSubmit(options);
    }

    // 跳转页面
    function toPage() {
        window.location.href= "front/outLogin";
    }

    // 验证密码类型
    function checkType() {
        var cntent = $("[name = 'passwordType'] option:selected").val();
        if (cntent == '') {
            layer.tips('请选择要修改的密码', "[name = 'passwordType']", {
                tips: [1, '#3595CC'],
                time: 2000
            });
            return true;
        }
    }


</script>

</html>
