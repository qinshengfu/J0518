<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
	<title></title>
</head>

<style type="text/css">
    .mui-tab-item i {
        display: block;
        font-size: 25px;
        margin-top: 3px;
        line-height: normal;
    }

    .mui-tab-label {
        font-size: 12px;
    }

    .mui-bar {
        -webkit-box-shadow: none;
        box-shadow: none;
        background-color: crimson;
        border-top: 1px solid #efefef;
    }

    .mui-bar-tab .mui-tab-item.mui-active {
        color: #fff;
    }

    .mui-bar-tab .mui-tab-item {
        color: #d6d6d6;
    }
</style>

<div class="mui-content">
    <nav class="mui-bar mui-bar-tab">
        <a href="front/to_index" id="index" class="mui-tab-item mui-active">
            <i class="iconfont icon-shouye"></i>
            <span class="mui-tab-label">首页</span>
        </a>
        <a href="front/to_quotation" id="quotation" class="mui-tab-item">
            <i class="iconfont icon-hangqing"></i>
            <span class="mui-tab-label">行情</span>
        </a>
        <a href="front/to_transaction" id="transaction" class="mui-tab-item">
            <i class="iconfont icon-jiaoyi"></i>
            <span class="mui-tab-label">交易</span>
        </a>
        <a href="release/to_mall" id="mall" class="mui-tab-item">
            <i class="iconfont icon-shangcheng3"></i>
            <span class="mui-tab-label">商城</span>
        </a>
        <a href="front/to_center" id="center" class="mui-tab-item">
            <i class="iconfont icon-gerenzhongxin1"></i>
            <span class="mui-tab-label">我的</span>
        </a>
    </nav>
</div>
<body>
<script type="text/javascript">
    mui.init();

    // 移除高亮样式
    $(".mui-tab-item").removeClass("mui-active");
    var flag = '${flag}';
    // 添加高亮样式
    $("#" + flag).addClass("mui-active");


    // 解决a标签 和 导航栏 无法跳转
    mui('.mui-content').on('tap', 'a', function () {
        document.location.href = this.href;
    });

    //物理返回按键监听
    var first = null;
    mui.back = function () {
        //首次按键，提示‘再按一次退出应用’
        if (!first) {
            first = new Date().getTime();
            mui.toast('再按一次退出应用');
            setTimeout(function () {
                first = null;
            }, 1000);
        } else {
            if (new Date().getTime() - first < 1000) {
                plus.runtime.quit();
            }
        }
    };

    //解决mui 键盘弹出 将底部选项卡顶上来
    mui.plusReady(function () {
        var height = document.documentElement.clientHeight || document.body.clientHeight;
        plus.webview.currentWebview().setStyle({
            height: height
        });
        window.onresize = function () {
            plus.webview.currentWebview().setStyle({
                height: height
            })
        }
    });


</script>
</body>

</html>
