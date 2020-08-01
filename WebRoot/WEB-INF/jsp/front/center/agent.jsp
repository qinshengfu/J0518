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

<style>
    .follow_parent {
        position: relative;
        width: 200px;
        margin: 0 auto;
    }

    .follow_parent input {
        position: absolute;
        width: 100%;
        height: 100%;
        left: 0;
        top: 0;
        opacity: 0; /* 隐藏 */
    }
</style>

<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title">申请代理</h1>
</header>
<div class="mui-content">
    <form id="Form" method="post">
        <!-- 代理类型 -->
        <ul class="agen_nav flex1">
            <li>
                <div class="mui-input-row mui-radio mui-left"><label>市级代理</label>
                    <input name="AGENT_TYPE" type="radio" checked="checked" value="市级代理"></div>
            </li>
            <li>
                <div class="mui-input-row mui-radio mui-left"><label>省级代理</label>
                    <input name="AGENT_TYPE" type="radio" value="省级代理"></div>
            </li>
        </ul>
        <!-- 申请说明 -->
        <div class="agen_txt">
            <h4>申请代理说明：</h4>
            <div id="description">
            </div>
        </div>
        <!--  -->
        <div class="setup_inp">
            <div class="mui-input-row" style="border-bottom: 0;">
                <label>上传图片：</label>
            </div>
            <div class="mui-input-row follow_parent">
                <input type="text" hidden name="voucher">
                <img src="static/front/images/upload.png" id="voucher" alt="">
                <input type="file" onchange="upload(this,'voucher')">
            </div>
            <div class="mui-input-row">
                <label>支付金额：</label>
                <input type="number" name="num" id="num" placeholder="支付金额">
            </div>
        </div>
        <!--  -->
        <button type="button" onclick="checkClient()" data-loading-text="提交中" class="mui-btn mui-btn-blue mui-btn-block mui_btn">申请代理</button>
    </form>
</div>

</body>

<script type="text/javascript">

    // 系统参数
    var param;

    // 初始化
    $(function () {
        // 获取系统参数
        $.get("front/getSystemParam", function (result) {
            if (result.success) {
                param = result.data;
                // 写入代理说明
                $("#description").html(param.AGENT_DESCRIPTION);
            }
        })
    });


    // 客户端验证
    function checkClient() {
        mui(".mui-btn").button('loading');//切换为loading状态
        var num = $("#num").val();
        if (num <= 0) {
            layer.tips("请输入大于 0 的数", "#num", {
                tips: [1, '#3595CC'],
                time: 2000
            });
            setTimeout(function () {
                mui(".mui-btn").button('reset'); //切换为reset状态(即重置为原始的button)
            }, 1000);
            return true;
        }
        if ($("[name = 'voucher']").val() == '') {
            layer.tips('请上传支付凭证', "#voucher", {
                tips: [1, '#3595CC'],
                time: 2000
            });
            setTimeout(function () {
                mui(".mui-btn").button('reset'); //切换为reset状态(即重置为原始的button)
            }, 1000);
            return true;
        }
        server_verification()
    }


    // 服务端校验
    function server_verification() {
        var url = "front/applyAgent.do";
        //异步提交表单(先确保jquery.form.js已经引入了)
        var options = {
            url: url,
            success: function (result) {
                mui(".mui-btn").button('reset'); //切换为reset状态(即重置为原始的button)
                mui.toast(result.message);
                /*后台登录验证后*/
                if (result.success) {
                    setTimeout(function () {
                        window.location.reload();
                    }, 1200);
                }
            }
        };
        $("#Form").ajaxSubmit(options);
    }

</script>

<%--图片压缩上传--%>
<script>
    //声明一个formdata 用来上传
    var UForm;
    // 定义图片原始大小、压缩后的大小
    var oldfilesize, newfilesize;

    // 当上传按钮内容发送改变后 获取文件并调用压缩图片的方法
    function upload(itself, id) {
        UForm = new FormData();
        GetFile(itself.files, id);
        // 重新初始化了file的值
        itself.value = "";
    }

    // GetFile 处理获取到的file对象，并对它进行压缩处理, id 是显示图片的容器
    function GetFile(files, id) {
        // 用三目运算符频道文件是否存在
        var file = files ? files[0] : false;
        if (!file) {
            return;
        }
        if (file) {
            oldfilesize = Math.floor((file.size / 1024) * 100) / 100;
            // 如果图片少于3M 则不进行压缩
            if (oldfilesize < 5000) {
                UForm.append("files", file);
                ShowFile(file, id);
                return;
            }
            lrz(file, {
                width: 2048, //设置压缩后的最大宽
                height: 1080,
                quality: 0.8 //图片压缩质量，取值 0 - 1，默认为0.7
            }).then(function (rst) {
                newfilesize = Math.floor((rst.file.size / 1024) * 100) / 100;
                console.log("图片压缩成功，原为：" + oldfilesize + "KB,压缩后为：" + newfilesize + "KB");
                // 把压缩后的图片文件存入 formData中，这样用ajax传到后台才能接收
                UForm.append("files", rst.file);
                ShowFile(rst.file, id);
            }).catch(function (err) {
                alert("压缩图片时出错，请上传图片文件！");
                return false;
            });
        }
    }

    // ShowFile 把处理后的图片显示出来，实现图片的预览功能：
    function ShowFile(file, id) {
        // 使用fileReader对文件对象进行操作
        var reader = new FileReader();
        reader.onload = function (e) {
            var img = new Image();
            img.src = e.target.result;
            // console.log(img)
            // 图片本地回显
            $('#' + id).attr({src: img.src});
            location.href
        };
        reader.onerror = function (e, b, c) {
            //error
        };
        // 读取为数据url
        reader.readAsDataURL(file);
        // 上传到服务器
        DoUp(id);
    }

    // 使用AJAX上传数据到后台
    function DoUp(id) {
        mui.toast("上传服务器中");
        $.ajax({
            url: "front/addImg.do?filesName=J0518",
            type: "POST",
            data: UForm,
            contentType: false,//禁止修改编码
            processData: false,//不要把data转化为字符
            success: function (result) {
                if (result.success) {
                    mui.toast("上传成功");
                    // 上传成功 返回图片路径
                    var img_path = result.data.urls;
                    $("input[name=" + id + "]").attr({value: img_path});
                }
            },
            error: function (e) {
                mui.toast("上传出错！请检查是否选择了图片");
            }
        });
    }
</script>

</html>
