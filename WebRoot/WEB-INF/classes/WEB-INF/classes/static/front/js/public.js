mui.plusReady(function () {
    // window.onload = function(){
    var aniShow = "pop-in";
    var menu = null,
        showMenu = false;
    var isInTransition = false;
    var _self;

    //只有ios支持的功能需要在Android平台隐藏；
    if (mui.os.android) {
        var list = document.querySelectorAll('.ios-only');
        if (list) {
            for (var i = 0; i < list.length; i++) {
                list[i].style.display = 'none';
            }
        }
        //Android平台暂时使用slide-in-right动画
        if (parseFloat(mui.os.version) < 4.4) {
            aniShow = "slide-in-right";
        }
    }


    //读取本地存储，检查是否为首次启动
    var showGuide = plus.storage.getItem("lauchFlag");
    //仅支持竖屏显示
    plus.screen.lockOrientation("portrait-primary");
    if (showGuide) {
        //有值，说明已经显示过了，无需显示；
        //关闭splash页面；
        plus.navigator.closeSplashscreen();
        plus.navigator.setFullscreen(false);
    }

    mui('.scroll').scroll({
        scrollY: true, //是否竖向滚动
        scrollX: false, //是否横向滚动
        startX: 0, //初始化时滚动至x
        startY: 0, //初始化时滚动至y
        indicators: true, //是否显示滚动条
        deceleration: 0.0006, //阻尼系数,系数越小滑动越灵敏
        bounce: true //是否启用回弹
    });


    // 原生图片预览仅新版本runtime支持，若引擎不支持，则隐藏；
    if (!plus.nativeUI.previewImage) {
        var previewImageNativeElem = document.getElementById('preview_image_native');
        previewImageNativeElem.className = previewImageNativeElem.className.replace('mui-plus-visible', 'mui-hidden');
    }

    //更改顶部状态栏背景颜色
    if (mui.os.plus) {
        plus.navigator.setStatusBarStyle('light'); //light为白色字体。dark为黑色字体
        plus.navigator.setStatusBarBackground('#48786a'); //背景颜色。
    }

});

//页面跳转方式
mui('body').on('tap', 'a', function () {
    document.location.href = this.href;
});

//文本复制方法
function copy_fun(copy) { //参数copy是要复制的文本内容
    mui.plusReady(function () {
        //判断是安卓还是ios
        if (mui.os.ios) {
            //ios
            var UIPasteboard = plus.ios.importClass("UIPasteboard");
            var generalPasteboard = UIPasteboard.generalPasteboard();
            //设置/获取文本内容:
            generalPasteboard.plusCallMethod({
                setValue: copy,
                forPasteboardType: "public.utf8-plain-text"
            });
            generalPasteboard.plusCallMethod({
                valueForPasteboardType: "public.utf8-plain-text"
            });
            mui.toast("已成功复制", {
                type: 'div'
            });
        } else {
            //安卓
            var context = plus.android.importClass("android.content.Context");
            var main = plus.android.runtimeMainActivity();
            var clip = main.getSystemService(context.CLIPBOARD_SERVICE);
            plus.android.invoke(clip, "setText", copy);
            mui.toast("已成功复制", {
                type: 'div'
            });
        }
    });
}

