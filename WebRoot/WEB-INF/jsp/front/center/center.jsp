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
        width: 60px;
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
<div class="mui-content">
    <!-- 头像 -->
    <div class="mine_top">
        <div class="mine_top_head">
            我的
        </div>
        <div class="follow_parent">
            <input type="text" hidden name="userPortrait">
            <c:if test="${user.USER_PORTRAIT == null}">
                <img src="http://via.placeholder.com/300" alt="默认头像" id="userPortrait">
            </c:if>
            <c:if test="${user.USER_PORTRAIT != null}">
                <img src="${user.USER_PORTRAIT}" alt="" id="userPortrait">
            </c:if>
            <input type="file" onchange="upload(this,'userPortrait')">
        </div>
        <ul class="flex1">
            <li>
                <div>USDT</div>
                <div>${user.USDT_WALLET}</div>
            </li>
            <li>
                <div>金额</div>
                <div>${user.MONEY}</div>
            </li>
            <li>
                <div>商城积分</div>
                <div>${user.SHOP_INTEGRAL}</div>
            </li>
        </ul>
    </div>

    <!-- list -->
    <div class="mine_list">
        <ul class="mui-table-view">
            <li class="mui-table-view-cell">
                <a class="mui-navigate-right" href="front/to_order">
                    <i class="iconfont icon-dingdan4"></i>我的订单
                </a>
            </li>
            <li class="mui-table-view-cell">
                <a class="mui-navigate-right" href="front/to_mallAddress">
                    <i class="iconfont icon-dingdan4"></i>收货地址
                </a>
            </li>
            <li class="mui-table-view-cell">
                <a class="mui-navigate-right" href="front/to_mallCart">
                    <i class="iconfont icon-dingdan4"></i>购物车
                </a>
            </li>
            <li class="mui-table-view-cell" style="margin-bottom: 10px;">
                <a class="mui-navigate-right" href="front/to_recharg">
                    <i class="iconfont icon-yinhangqia11"></i>充值提币
                </a>
            </li>
            <li class="mui-table-view-cell" style="margin-bottom: 10px;">
                <a class="mui-navigate-right" href="front/to_substitution">
                    <i class="iconfont icon-yinhangqia11"></i>货币兑换
                </a>
            </li>
            <li class="mui-table-view-cell" style="margin-bottom: 10px;">
                <a class="mui-navigate-right" href="front/to_transfer">
                    <i class="iconfont icon-yinhangqia11"></i>USDT转账
                </a>
            </li>
            <li class="mui-table-view-cell">
                <a class="mui-navigate-right" href="front/to_share">
                    <i class="iconfont icon-yaoqing"></i>邀请好友
                </a>
            </li>
            <li class="mui-table-view-cell">
                <a class="mui-navigate-right" href="front/to_goods">
                    <i class="iconfont icon-tibi"></i>我要提货
                </a>
            </li>
            <li class="mui-table-view-cell">
                <a class="mui-navigate-right" href="front/to_goodsRecord">
                    <i class="iconfont icon-tibi"></i>提货记录
                </a>
            </li>
            <li class="mui-table-view-cell" style="margin-bottom: 10px;">
                <a class="mui-navigate-right" href="front/to_agent">
                    <i class="iconfont icon-shenqing4"></i>申请代理
                </a>
            </li>
            <li class="mui-table-view-cell">
                <a class="mui-navigate-right" href="front/to_changdata">
                    <i class="iconfont icon-ziliao1"></i>修改资料
                </a>
            </li>
            <li class="mui-table-view-cell">
                <a class="mui-navigate-right" href="front/to_securityCenter">
                    <i class="iconfont icon-ziliao1"></i>安全中心
                </a>
            </li>
            <li class="mui-table-view-cell">
                <a class="mui-navigate-right customer1" href="javascript:void(0)">
                    <i class="iconfont icon-kehufuwukefu"></i>客服
                </a>
            </li>
            <li class="mui-table-view-cell">
                <a class="mui-navigate-right" href="front/to_news">
                    <i class="iconfont icon-gonggao2"></i>系统咨询
                </a>
            </li>
            <li class="mui-table-view-cell">
                <a href="javascript:outLogin()" class="mui-navigate-right">
                    <i class="iconfont icon-tuichu13"></i>退出登录
                </a>
            </li>
        </ul>
    </div>

</div>

<!-- 客服 -->
<div class="tc_bg">
    <ul class="customer">
        <li>客服</li>
        <li>
            <span>${par.QQ}</span>
            <button data-clipboard-action="copy" data-clipboard-text="${par.QQ}" type="button"
                    class="mui-btn mui-btn-blue">复制
            </button>
        </li>
        <li>
            <span>${par.WECHAT}</span>
            <button data-clipboard-action="copy" data-clipboard-text="${par.WECHAT}" type="button"
                    class="mui-btn mui-btn-blue">复制
            </button>
        </li>
    </ul>
</div>


<%@include file="../../front/footer/footer.jsp" %>

</body>

<script>

    // 退出登录
    function outLogin() {
        //底部对话框
        layer.open({
            content: '你确定要退出吗？'
            , btn: ['确认', '取消']
            , skin: 'footer'
            , yes: function (index) {
                // 清空页面
                $("html body").empty();
                window.location.href = "front/outLogin"
            }
        });
    }

    $(function () {
        var clipboard = new ClipboardJS('.mui-btn-blue');
        clipboard.on('success', function (e) {
            mui.toast("复制成功");
            e.clearSelection();
        });
    });

    // 点击显示客服
    mui('body').on('tap', '.customer1', function () {
        event.stopPropagation();
        $('.tc_bg').fadeIn();
        $('.customer').addClass('alert');
        $('.customer').removeClass('alert1')
    });
    $(document).click(function (event) {
        var _con = $('.customer'); // 设置目标区域
        if (!_con.is(event.target) && _con.has(event.target).length === 0) { // Mark 1
            $('.tc_bg').fadeOut();
            $('.customer').addClass('alert1')
            $('.customer').removeClass('alert')
        }
    });


    // 保存用户资料
    function saveUserData() {
        // 头像url
        var userPortrait = $("[name = 'userPortrait']").val();
        $.post("front/updateUserProfile",
            {
                "USER_PORTRAIT": userPortrait,
                "USER_ID": '${user.ACCOUNT_ID}'
            },
            function (result) {
                if (result.success) {
                    mui.toast("修改成功");
                    window.location.reload();
                }
            }, "json")
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
        // 重新初始化了file的html
        itself.outerHTML;
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
        $.ajax({
            url: "front/addImg.do?filesName=J0518",
            type: "POST",
            data: UForm,
            contentType: false,//禁止修改编码
            processData: false,//不要把data转化为字符
            success: function (result) {
                if (result.success) {
                    // 上传成功 返回图片路径
                    var img_path = result.data.urls;
                    $("input[name=" + id + "]").attr({value: img_path});
                    // 保存到数据库
                    saveUserData();
                }
            },
            error: function (e) {
                mui.toast("上传出错！请检查是否选择了图片");
            }
        });
    }
</script>


</html>
