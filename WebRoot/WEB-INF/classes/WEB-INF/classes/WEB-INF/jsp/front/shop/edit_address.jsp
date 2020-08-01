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
<style type="text/css">
    .mui-poppicker-header .mui-btn {
        margin: 0;
        width: auto;
    }

    .select_address .ui-alert {
        color: #000000;
    }

    .mui-radio input[type='radio']:before, .mui-checkbox input[type='checkbox']:before {
        color: #aaa;
        font-size: 28px;
    }

    .mui-radio input[type='radio']:checked:before, .mui-checkbox input[type='checkbox']:checked:before {
        color: #007aff;
    }
</style>
<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title">编辑收货地址</h1>
</header>
<div class="mui-content">
    <form id="Form" method="post">
        <!-- 地址ID隐藏域 -->
        <input type="text" hidden name="USER_ADDR_ID" value="${id}">
        <!-- 地区隐藏域 -->
        <input type="text" hidden id="region" name="region">
        <!-- 输入框 -->
        <div class="login_pass">
            <div class="mui-input-row">
                <label>姓名</label>
                <input type="text" name="RECEIVER" id="RECEIVER" value="">
            </div>
            <div class="mui-input-row">
                <label>手机号</label>
                <input type="number" name="MOBILE" id="MOBILE" onblur="checkPhone(this)"
                       oninput="if(value.length>11)value=value.slice(0,11)" value="">
            </div>
            <div class="mui-input-row">
                <label>地区</label>
                <div class="select_address">
                    <input id='showCityPicker3' type="button" value="">
                    <div id='cityResult3' class="ui-alert">请选择</div>
                </div>
            </div>
            <div class="mui-input-row">
                <label>详细地址</label>
                <input type="text" name="ADDR" id="ADDR" value="">
            </div>
            <div class="mui-input-row mui-checkbox ">
                <label>设为默认</label>
                <input style="top: 10px;" name="isDefault" id="isDefault" type="checkbox" value="1">
            </div>
            <div onclick="delAddress()" class="price dele_address">删除收货地址</div>
        </div>
        <button type="button" data-loading-text="提交中" class="mui-btn mui-btn-blue mui-btn-block mui_btn">确认修改</button>
    </form>
</div>

</body>

<script>

    // 初始化
    $(function () {
        $.get("front/getAddress?USER_ADDR_ID=${id}", function (result) {
            if (result.success) {
                setData(result.data);
            }
        }, "json");
    });

    // 设置数据
    function setData(data) {
        $("#RECEIVER").val(data.RECEIVER);
        $("#MOBILE").val(data.MOBILE);
        var region = data.PROVINCE + " " + data.CITY + " " + data.AREA;
        $("#cityResult3").html(region);
        $("#showCityPicker3").val(region);
        $("#region").val(data.PROVINCE + "," + data.CITY + "," + data.AREA);
        $("#ADDR").val(data.ADDR);
        if (data.COMMON_ADDR == 1) {
            $("#isDefault").attr("checked", true)
        }
    }

    // 删除地址
    function delAddress() {
        //底部对话框
        layer.open({
            content: '确认要删除此收货地址吗？'
            , btn: ['删除', '取消']
            , skin: 'footer'
            , yes: function (index) {
                layer.close(index);
                $.post("front/delAddress", {"USER_ADDR_ID": '${id}'}, function (result) {
                    mui.toast(result.message);
                    if (result.success) {
                        setTimeout(function () {
                            window.location.href = "front/to_center";
                        }, 1000)
                    }
                })
            }
        });
    }

    // 当提交按钮被点击
    $(".mui-btn").click(function () {
        mui(".mui-btn").button('loading'); // 置灰
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
            if (checkPhone($("#MOBILE").get(0))) {
                mui(".mui-btn").button('reset'); // 释放
                return false;
            }
            server_verification()
        }
    });

    // 服务端校验
    function server_verification() {
        var url = "front/editUserAddress.do";
        //异步提交表单(先确保jquery.form.js已经引入了)
        var options = {
            url: url,
            success: function (result) {
                mui(".mui-btn").button('reset'); //切换为reset状态(即重置为原始的button)
                mui.toast(result.message);
                /*后台验证后*/
                if (result.success) {
                    setTimeout(function () {
                        // 返回上一页
                        window.history.go(-1);
                    }, 1000);
                }
            }
        };
        $("#Form").ajaxSubmit(options);
    }

    // 验证手机号格式
    function checkPhone(dom) {
        if (!isPhone(dom.value)) {
            layer.tips('手机号格式错误', dom, {
                tips: [1, '#3595CC'],
                time: 2000
            });
            return true;
        }
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

                    // 拼接地址写入 value
                    showCityPickerButton.value = items[0].text + "," + items[1].text + "," + items[2].text;
                    doc.getElementById("region").value = items[0].text + "," + items[1].text + "," + items[2].text;
                });
            }, false);
        });
    })(mui, document);
</script>

</html>
