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
    <h1 class="mui-title">修改密保</h1>
</header>
<div class="mui-content">
    <form id="Form" method="post">
        <div class="setup_inp">
            <div class="mui-input-row">
                <label>密保问题：</label>
                <select name="SECURITY_QUESTION">
                    <option value="" hidden>请选择</option>
                    <option value="生日">生日</option>
                    <option value="身份证">身份证</option>
                </select>
            </div>
            <div class="mui-input-row">
                <label>密保答案：</label>
                <input type="text" name="SECURITY_ANSWER" value="">
            </div>
        </div>
        <button type="button" class="mui-btn mui-btn-blue mui-btn-block mui_btn">确认修改</button>
    </form>
</div>

</body>

<script>
    // 客户端验证
    mui(document.body).on('tap', '.mui-btn', function () {
        check = true;
        mui(".mui-input-row input").each(function () {
            //若当前input为空，则alert提醒
            if (!this.value || this.value.trim() == "") {
                layer.tips('不允许为空', this, {
                    tips: [1, '#3595CC'],
                    time: 2000
                });
                check = false;
                return false;
            }
        }); //校验通过，继续执行业务逻辑
        if (check) {
            if (checkQuestion()) {
                mui(".mui-btn").button('reset'); // 释放
                return false;
            }
            server_verification()
        }
    });

    // 服务端校验
    function server_verification() {
        var url = "front/updateUserSecurity.do";
        //异步提交表单(先确保jquery.form.js已经引入了)
        var options = {
            url: url,
            success: function (result) {
                mui(".mui-btn").button('reset'); // 释放
                /*后台登录验证后*/
                if (result.success) {
                    mui.toast("修改成功");
                    setTimeout(function() {
                        toPage();
                    },1200)
                } else {
                    mui.toast(result.message);
                }
            }
        };
        $("#Form").ajaxSubmit(options);
    }

    // 跳转页面
    function toPage() {
        window.location.reload();
    }

    // 验证密保问题
    function checkQuestion() {
        var cntent = $("[name = 'SECURITY_QUESTION'] option:selected").val();
        if (cntent == '') {
            layer.tips('请选择密保问题', "[name = 'SECURITY_QUESTION']", {
                tips: [1, '#3595CC'],
                time: 2000
            });
            return true;
        }
    }

</script>

</html>
