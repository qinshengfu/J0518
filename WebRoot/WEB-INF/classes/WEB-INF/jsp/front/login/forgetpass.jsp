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

<body class="login_bg">
<div class="mui-content content_bg">
    <!-- logo -->
    <div class="login_top">
        <h4>Retrieve password</h4>
        <div class="bg_dl">找回密码</div>
    </div>
    <form id="Form" method="post" class="mui-input-group login_form fort_pass">
        <div class="mui-input-row">
            <label><i class="iconfont icon-shouji3"></i></label>
            <input type="number" name="PHONE" onblur="checkPhone(this.value)"
                   oninput="if(value.length>11)value=value.slice(0,11)" placeholder="手机号">
        </div>
        <div class="mui-input-row">
            <label>密保问题：</label>
            <select name="question">
                <option value="" hidden>请选择</option>
                <option value="生日">生日</option>
                <option value="身份证">身份证</option>
            </select>
        </div>
        <div class="mui-input-row">
            <label><i class="iconfont icon-mima1"></i></label>
            <input type="text" name="answer" placeholder="密保答案">
        </div>
        <div class="mui-input-row">
            <label><i class="iconfont icon-mima"></i></label>
            <input type="password" name="newPass" placeholder="请输入新密码">
        </div>
        <div class="mui-input-row">
            <label><i class="iconfont icon-mima"></i></label>
            <input type="password" name="confirmPass" placeholder="确认新密码">
        </div>
    </form>
    <button type="button" class="mui-btn mui-btn-blue mui-btn-block mui_btn_login">提交</button>
    <!--  -->
    <a class="bot_txt" href="release/to_login">登录</a>
</div>

</body>

<script type="text/javascript">

    // 提交
    $(".mui-btn").click(function () {
        mui(this).button('loading'); // 置灰
        var check = true;
        mui(".mui-input-row input").each(function () {
            //若当前input为空，则alert提醒
            if (!this.value || this.value.trim() == "") {
                layer.tips('不允许为空', this, {
                    tips: [1, '#3595CC'],
                    time: 2000
                });
                mui(".mui-btn").button('reset'); // 释放
                check = false;
                return false;
            }
        });

        //校验通过，继续执行业务逻辑
        if (check) {

            // 验证手机号格式
            if (checkPhone($("[name= 'PHONE']").val())) {
                mui(".mui-btn").button('reset'); // 释放
                return false;
            }
            // 验证密保问题
            if (checkQuestion()) {
                mui(".mui-btn").button('reset'); // 释放
                return false;
            }
            // 验证密码是否一致
            if (checkPassword()) {
                mui(".mui-btn").button('reset'); // 释放
                return false;
            }
            server_verification()
        }
    });

    // 服务端校验
    function server_verification() {
        var url = "release/retrievePassword.do";
        //异步提交表单(先确保jquery.form.js已经引入了)
        var options = {
            url: url,
            success: function (result) {
                mui(".mui-btn").button('reset'); // 释放
                /*后台登录验证后*/
                if (result.success) {
                    mui.toast("修改成功");
                    toPage();
                } else {
                    mui.toast(result.message);
                }
            }
        };
        $("#Form").ajaxSubmit(options);
    }

    // 跳转页面
    function toPage() {
        window.location.href = "release/to_login";
    }

    // 验证手机号格式
    function checkPhone(str) {
        if (!isPhone(str)) {
            layer.tips('手机号格式错误', "[name='PHONE']", {
                tips: [1, '#3595CC'],
                time: 2000
            });
            return true;
        }
    }

    // 验证密码是否一致
    function checkPassword() {
        var newPass = $("[name = 'newPass']").val();
        var confirmPass = $("[name = 'confirmPass']").val();
        if (newPass != confirmPass) {
            layer.tips('密码不一致', "[name = 'confirmPass']", {
                tips: [1, '#3595CC'],
                time: 2000
            });
            return true;
        }
    }

    // 验证密保问题
    function checkQuestion() {
        var cntent = $("[name = 'question'] option:selected").val();
        if (cntent == '') {
            layer.tips('请选择密保问题', "[name = 'question']", {
                tips: [1, '#3595CC'],
                time: 2000
            });
            return true;
        }
    }

    // 手机号正则
    function isPhone(str) {
        var pattern = /^1[3|4|5|6|7|8|9][0-9]{9}$/;
        return pattern.test(str);
    }

</script>

</html>
