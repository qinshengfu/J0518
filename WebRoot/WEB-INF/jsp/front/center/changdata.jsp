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
    <h1 class="mui-title">修改资料</h1>
</header>
<div class="mui-content">
    <form id="Form" method="post">
        <input hidden name="USER_ID" value="${user.ACCOUNT_ID}">
        <div class="setup_inp">
            <div class="mui-input-row">
                <label>姓名：</label>
                <input type="text" maxlength="18" name="FULL_NAME" value="${user.FULL_NAME}">
            </div>
            <div class="mui-input-row">
                <label>身份证号：</label>
                <input type="text" name="ID_CARD_NO" maxlength="18" value="${user.ID_CARD_NO}">
            </div>
            <div class="mui-input-row">
                <label>钱包地址：</label>
                <input type="text" name="WALLET_ADDRESS" value="${user.WALLET_ADDRESS}">
            </div>
        </div>
        <button type="button" data-loading-text="提交中" class="mui-btn mui-btn-blue mui-btn-block mui_btn">确认修改</button>
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
                check = false;
                mui(".mui-btn").button('reset'); //切换为reset状态(即重置为原始的button)
                return false;
            }
        }); //校验通过，继续执行业务逻辑
        if (check) {
            if (checIDcard($("[name = 'ID_CARD_NO']").val())) {
                mui(".mui-btn").button('reset'); //切换为reset状态(即重置为原始的button)
                return false;
            }
            server_verification()
        }
    });

    // 服务端校验
    function server_verification() {
        var url = "front/updateUserBaseData.do";
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
        window.location.reload();
    }

    // 检查身份证格式
    function checIDcard(str) {
        if (!IDCardRegular(str)) {
            layer.tips('身份证格式错误', "[name = 'ID_CARD_NO']", {
                tips: [1, '#3595CC'],
                time: 2000
            });
            return true;
        }
    }

    // 身份证正则表达式
    function IDCardRegular(str) {
        var pattern = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
        return pattern.test(str);
    }


</script>

</html>
