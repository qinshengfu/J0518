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
    <!--  -->
    <div class="login_top">
        <h4>Registered</h4>
        <div class="bg_dl">注册</div>
    </div>

    <form id="Form" method="post" autocomplete="off" class="mui-input-group login_form fort_pass">
        <!-- 地区隐藏域 -->
        <input type="text" hidden id="region" name="region" value="">
        <div class="mui-input-row">
            <label><i class="iconfont icon-dizhi"></i></label>
            <div class="select_address">
                <input id='showCityPicker3' type="button" value="">
                <div id='cityResult3' class="ui-alert">请选择地区</div>
            </div>
        </div>
        <div class="mui-input-row">
            <label><i class="iconfont icon-shouji3"></i></label>
            <input type="number" name="phone" onblur="checkPhone(this.value)"
                   oninput="if(value.length>11)value=value.slice(0,11)" placeholder="手机号">
        </div>
        <div class="mui-input-row">
            <label><i class="iconfont icon-mima1"></i></label>
            <input type="password" name="loginPass" placeholder="登录密码">
        </div>
        <div class="mui-input-row">
            <label><i class="iconfont icon-mima1"></i></label>
            <input type="password" name="securPass" placeholder="交易密码">
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
            <label><i class="iconfont icon-wode1"></i></label>
            <input type="text" name="inviter" placeholder="推荐码">
        </div>

    </form>
    <button type="button" data-loading-text="提交中" class="mui-btn mui-btn-blue mui-btn-block mui_btn_login">注册</button>
    <!--  -->
    <a class="bot_txt" href="release/to_login">登录</a>
</div>

</body>

<script>

    $(function () {
        var inviter = '${pd.code}';
        if (inviter != '') {
            $("[name = 'inviter']").val(inviter);
            $("[name = 'inviter']").attr("readonly", "readonly");
        }
    });


    // 注册
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
            if (checkPhone($("[name= 'phone']").val())) {
                mui(".mui-btn").button('reset'); // 释放
                return false;
            }
            // 验证密保问题
            if (checkQuestion()) {
                mui(".mui-btn").button('reset'); // 释放
                return false;
            }
            server_verification()
        }
    });

    // 服务端校验
    function server_verification() {
        var url = "release/register.do";
        //异步提交表单(先确保jquery.form.js已经引入了)
        var options = {
            url: url,
            success: function (result) {
                mui(".mui-btn").button('reset'); // 释放
                /*后台登录验证后*/
                if (result.success) {
                    mui.toast("注册成功");
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
            layer.tips('手机号格式错误', "[name='phone']", {
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

<script type="text/javascript">
    (function ($, doc) {
        $.init();
        $.ready(function () {
            //三级联示例
            var cityPicker3 = new $.PopPicker({
                layer: 3
            });
            cityPicker3.setData(cityData3);
            var showCityPickerButton = doc.getElementById('showCityPicker3');
            var cityResult3 = doc.getElementById('cityResult3');
            showCityPickerButton.addEventListener('tap', function (event) {
                cityPicker3.show(function (items) {
                    cityResult3.innerText = (items[0] || {}).text + " " + (items[1] || {}).text + " " + (items[2] ||
                        {}).text;
                    //返回 false 可以阻止选择框的关闭
                    /*console.log("省ID:" + items[0].value + "  省名称：" + items[0].text);
                    console.log("市ID:" + items[1].value + "  市名称：" + items[1].text);
                    console.log("区ID:" + items[2].value + "  区名称：" + items[2].text);*/

                    // 拼接地址写入 value
                    showCityPickerButton.value = items[0].text + "," + items[1].text + "," + items[2].text;
                    doc.getElementById("region").value = items[0].text + "," + items[1].text + "," + items[2].text;
                });
            }, false);
        });
    })(mui, document);
</script>

</html>
