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
    <h1 class="mui-title">充值提币</h1>
</header>
<div class="mui-content">
    <!-- nav -->
    <div class="profit_nav">
        <div class="mui-segmented-control">
            <a class="mui-control-item mui-active" href="#item1">充值</a>
            <a class="mui-control-item" href="#item2">提币</a>
        </div>
    </div>
    <!--  -->
    <div class="mui-content-padded profit_nav_table">

        <div id="item1" class="mui-control-content mui-active">
            <form id="Form1" method="post">
                <ul class="mui-table-view">
                    <li class="mui-table-view-cell">
                        <div class="setup_inp">
                            <div class="mui-input-row">
                                <label>钱包地址：</label>
                                <input type="text" name="walletAddress" id="walletAddress" value="">
                                <button data-clipboard-action="copy" data-clipboard-text="iipf " id="d_clip_button"
                                        type="button" class="mui-btn mui-btn-blue fuzhi">复制
                                </button>
                            </div>
                            <div class="mui-input-row">
                                <label>输入数量：</label>
                                <input type="number" id="num1" name="num" onblur="checkRechargeRange(this)"
                                       placeholder="请输入数量">
                            </div>
                            <div class="mui-input-row" style="border-bottom: 0;">
                                <label>转账凭证：</label>
                            </div>
                            <div class="mui-input-row follow_parent">
                                <input type="text" hidden name="voucher">
                                <img src="static/front/images/upload.png" id="voucher" alt="">
                                <input type="file" onchange="upload(this,'voucher')">
                            </div>
                        </div>
                    </li>
                </ul>
                <button type="button" onclick="recharge()" data-loading-text="提交中"
                        class="mui-btn mui-btn-blue mui-btn-block mui_btn">确定
                </button>
            </form>
        </div>

        <!-- 提现 -->
        <div id="item2" class="mui-control-content">
            <form id="Form2" method="post">
                <ul class="mui-table-view">
                    <li class="mui-table-view-cell">
                        <!--  -->
                        <div class="setup_inp">
                            <div class="mui-input-row">
                                <label>我的钱包：</label>
                                <input type="text" name="walletAddress" value="${user.WALLET_ADDRESS}" readonly
                                       placeholder="请输入钱包地址">
                            </div>
                            <div class="mui-input-row">
                                <label>输入数量：</label>
                                <input type="number" id="num2" name="num" onblur="checkWithdrawRange(this)"
                                       placeholder="请输入数量">
                            </div>
                            <div class="mui-input-row">
                                <label>交易密码：</label>
                                <input type="password" id="pass" name="pass" placeholder="请输入交易密码">
                            </div>
                        </div>
                    </li>
                </ul>
                <button type="button" onclick="withdrawal()" data-loading-text="提交中"
                        class="mui-btn mui-btn-blue mui-btn-block mui_btn">确定
                </button>
            </form>
        </div>
    </div>
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
                // 写入收款钱包地址
                $("#d_clip_button").attr("data-clipboard-text", result.data.RECEIVING_ADDRESS);
                $("#walletAddress").val(result.data.RECEIVING_ADDRESS);
            }
        })
    });

    // 充值客户端验证
    function recharge() {
        if (param.RECHARGE_SWITCH == 0) {
            return mui.toast("系统维护中")
        }
        mui(".mui_btn").button('loading');//切换为loading状态
        var element = $("#num1").get(0);
        var isOut = checkRechargeRange(element);
        if (isOut) {
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
        recharge_verification()
    }

    // 提现客户端验证
    function withdrawal() {
        if (param.WITHDRAWAL_SWITCH == 0) {
            return mui.toast("系统维护中")
        }
        var real = $("[name = 'walletAddress']").get(1);
        if (real.value == "") {
            layer.tips('请先去实名认证', real, {
                tips: [1, '#3595CC'],
                time: 2000
            });
            mui(".mui-btn").button('reset'); //切换为reset状态(即重置为原始的button)
            return true;
        }
        mui(".mui-btn").button('loading');//切换为loading状态
        if (checkWithdrawRange($("#num2").get(0))) {
            mui(".mui-btn").button('reset'); //切换为reset状态(即重置为原始的button)
            return true;
        }
        if ($("#pass").val() == '') {
            layer.tips('请输入交易密码', "#pass", {
                tips: [1, '#3595CC'],
                time: 2000
            });
            mui(".mui-btn").button('reset'); //切换为reset状态(即重置为原始的button)
            return true;
        }
        withdraw_verification()
    }

    // 充值服务端校验
    function recharge_verification() {
        var url = "front/rechargeVerification.do";
        //异步提交表单(先确保jquery.form.js已经引入了)
        var options = {
            url: url,
            success: function (result) {
                mui(".mui-btn").button('reset'); // 释放
                /*后台验证后*/
                if (result.success) {
                    mui.toast(result.message);
                    setTimeout(function () {
                        window.location.reload();
                    }, 1200);
                } else {
                    mui.toast(result.message);
                }
            }
        };
        $("#Form1").ajaxSubmit(options);
    }

    // 提现服务端校验
    function withdraw_verification() {
        var url = "front/withdrawVerification.do";
        //异步提交表单(先确保jquery.form.js已经引入了)
        var options = {
            url: url,
            success: function (result) {
                mui(".mui-btn").button('reset'); // 释放
                /*后台验证后*/
                if (result.success) {
                    mui.toast(result.message);
                    setTimeout(function () {
                        window.location.reload();
                    }, 1200);
                } else {
                    mui.toast(result.message);
                }
            }
        };
        $("#Form2").ajaxSubmit(options);
    }

    // 充值范围检查
    function checkRechargeRange(element) {
        var num = element.value;
        var min = Number(param.MIN_RECHARGE);
        var max = Number(param.MAX_RECHARGE);
        var multiple = Number(param.RECHARGE_MULTIPLE);
        if (num == '' || num < 1) {
            layer.tips('请输入大于0的数', element, {
                tips: [1, '#3595CC'],
                time: 2000
            });
            return true;
        }
        if (num < min) {
            layer.tips('最少充值 ' + min, element, {
                tips: [1, '#3595CC'],
                time: 2000
            });
            return true;
        }
        if (num > max) {
            layer.tips('最大充值 ' + max, element, {
                tips: [1, '#3595CC'],
                time: 2000
            });
            return true;
        }
        if (num % multiple !== 0 && multiple > 0) {
            layer.tips('请输入 ' + multiple + ' 的倍数', element, {
                tips: [1, '#3595CC'],
                time: 2000
            });
            return true;
        }
    }

    // 提现范围检查
    function checkWithdrawRange(element) {
        var num = element.value;
        var min = Number(param.MIN_CASH);
        var max = Number(param.MAX_CAHS);
        var multiple = Number(param.CASH_MULTIPLE);
        if (num == null || num <= 0) {
            layer.tips('请输入大于 0 的数', element, {
                tips: [1, '#3595CC'],
                time: 2000
            });
            return true;
        }
        if (num < min) {
            layer.tips('最少提现 ' + min, element, {
                tips: [1, '#3595CC'],
                time: 2000
            });
            return true;
        }
        if (num > max) {
            layer.tips('最大提现 ' + max, element, {
                tips: [1, '#3595CC'],
                time: 2000
            });
            return true;
        }
        if (num % multiple !== 0 && multiple > 0) {
            layer.tips('请输入 ' + multiple + ' 的倍数', element, {
                tips: [1, '#3595CC'],
                time: 2000
            });
            return true;
        }
    }

    $(document).ready(function () {
        var clipboard = new ClipboardJS('#d_clip_button');
        clipboard.on('success', function (e) {
            mui.toast("复制成功");
            e.clearSelection();
        });
    });
</script>

<%--图片压缩上传--%>
<script>
    //声明一个formdata 用来上传
    var UForm;
    // 定义图片原始大小、压缩后的大小
    var oldfilesize, newfilesize;

    // 当上传按钮内容发送改变后 获取文件并调用压缩图片的方法
    function upload(itself, id) {
        if (param.RECHARGE_SWITCH == 0) {
            return mui.toast("系统维护中")
        }
        UForm = new FormData();
        GetFile(itself.files, id);
        // 重新初始化了file的html
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
