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
    <!-- login_top -->
    <div class="login_top">
        <h4>Login</h4>
        <div class="bg_dl">登录</div>
    </div>
    <!--  -->
    <form id="Form" method="post" class="mui-input-group login_form">
        <div class="mui-input-row">
            <label><i class="iconfont icon-shouji"></i><span>账号</span></label>
            <input type="number" id="PHONE" name="PHONE" onblur="checkPhone(this.value)"
                   oninput="if(value.length>11)value=value.slice(0,11)"
                   class="mui-input-clear" placeholder="手机号">
        </div>
        <div class="mui-input-row">
            <label><i class="iconfont icon-mima"></i><span>密码</span></label>
            <input type="password" id="loginPass" name="loginPass" class="mui-input-clear" placeholder="密码">
        </div>
        <div class="login_bot flex2">
            <div class="mui-input-row mui-checkbox mui-left">
                <label>记住密码</label>
                <input id="saveId" onclick="savePass()" type="checkbox">
            </div>
            <a href="release/to_forgetpass">忘记密码？</a>
        </div>
    </form>
    <button type="button" data-loading-text="登录中" class="mui-btn mui-btn-blue mui-btn-block mui_btn_login login">登录
    </button>
    <!--  -->
    <a class="bot_txt" href="release/to_register">注册</a>
</div>

<script type="text/javascript">

    // 初始化
    $(function () {
        var name = $.cookie("loginName");
        var password = $.cookie('loginPass');
        if (typeof (name) != "undefined" && typeof (password) != "undefined") {
            $("#PHONE").val(name);
            $("#loginPass").val(password);
            $("#saveId").attr("checked", true);
        }
    });

    // 解决a标签 和 导航栏 无法跳转
    mui('body').on('tap', 'a', function () {
        document.location.href = this.href;
    });

    // 登录验证
    mui(document.body).on('tap', '.login', function () {
        mui(".mui-btn").button('loading');//切换为loading状态
        check = true;
        mui(".mui-input-row input").each(function () {
            //若当前input为空，则alert提醒
            if (!this.value || this.value.trim() == "") {
                layer.tips('不允许为空', this, {
                    tips: [1, '#3595CC'],
                    time: 2000
                });
                check = false;
                mui(".mui-btn").button('reset'); // 释放
                return false;
            }
        }); //校验通过，继续执行业务逻辑
        if (check) {
            if (checkPhone($("[name = 'PHONE']").val())) {
                mui(".mui-btn").button('reset'); // 释放
                return false;
            }
            server_verification()
        }
    });

    // 服务端校验
    function server_verification() {
        var url = "release/login.do";
        //异步提交表单(先确保jquery.form.js已经引入了)
        var options = {
            url: url,
            success: function (result) {
                mui(".mui-btn").button('reset'); // 释放
                /*后台登录验证后*/
                if (result.success) {
                    saveCookie();
                    mui.toast("登录成功");
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
        document.activeElement.blur(); //收起键盘
        window.location.href = "front/to_index?tag=1"
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

    // 手机号正则
    function isPhone(str) {
        var pattern = /^1[3-9][0-9]{9}$/;
        return pattern.test(str);
    }

    // 清楚缓存密码
    function savePass() {
        if (!$("#saveId").prop("checked")) {
            $.cookie('loginName', '', {
                expires: -1
            });
            $.cookie('loginPass', '', {
                expires: -1
            });
        }
    }

    // 保存密码
    function saveCookie() {
        if ($("#saveId").prop("checked")) {
            $.cookie('loginName', $("[name = 'PHONE']").val(), {
                expires: 7
            });
            $.cookie('loginPass', $("[name = 'loginPass']").val(), {
                expires: 7
            });
        }
    }

</script>
</body>

</html>
