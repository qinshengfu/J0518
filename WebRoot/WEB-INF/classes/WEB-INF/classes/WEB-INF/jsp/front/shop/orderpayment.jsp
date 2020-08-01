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
    .mui-card {
        box-shadow: none;
        margin: 0;
    }

    .mui-card .mui-input-group .mui-input-row {
        display: flex;
    }
</style>
<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title">确认订单</h1>
</header>
<div class="mui-content">
    <!-- 地址 -->
    <div class="order_adress" onclick='toUserAddress()' id="userAddress">

        <i class="iconfont icon-dizhi"></i>
        <div>
            <a href="front/to_mallAddress">
                <h4>张三 12345678987</h4>
                <p>北京市XXX区XX街道XX号</p>
            </a>
        </div>

    </div>
    <!-- 订单详情 -->
    <div class="order_detail" id="order_detail">

        <div class="order_detail_div1">
            <div class="order_detail_div1_1">
                <img src="static/front/images/01.png">
                <div>
                    <div class="huanhang">牛奶美肤乳幼嫩修复</div>
                    <p class="price">￥168.00</p>
                </div>
            </div>
            <div class="order_detail_div1_2">
                <p>X1</p>
            </div>
        </div>
        <div class="order_detail_div2">
            <div class="order_detail_div2_1">
                <p>订单号：201810101234656</p>
                <p>创建时间：2018-10-10 12:34:06</p>
            </div>
            <div class="order_detail_div2_2">
                <div>合计：<span>￥168.00</span></div>
                <div class="order_detail_div2-btn">
                    <button type="button" class="ljfk">立即付款</button>
                </div>
            </div>
        </div>

    </div>
</div>

<!-- 选择支付方式 -->
<div class="tc_bg" id="payType">
    <div class="tc_zffs">
        <div class="tc_tit">确认付款<i class="iconfont icon-guanbi8"></i></div>
        <div class="zffs">
            <h4>￥999.00</h4>
            <div class="mui-card">
                <p>付款方式：</p>
                <div class="mui-input-group">
                    <input hidden id="ORDER_ITEM_ID" value="">
                    <div class="mui-input-row mui-radio">
                        <label>积分</label> <input name="payType" checked value="1" type="radio">
                    </div>
                    <div class="mui-input-row mui-radio">
                        <label>余额钱包</label><input name="payType" value="0" type="radio">
                    </div>
                </div>
            </div>
        </div>
        <button type="button" class="mui-btn mui-btn-blue mui-btn-block mui_btn">确认付款</button>
    </div>
</div>

</body>

<script>

    // 初始化
    $(function () {
        // 从商品详情页面跳转还是购物车
        var tag = '${pd.tag}';
        if (tag == 'shop') {
            getOrderItem();
        }
        if (tag == 'cart') {
            getOrderSku();
        }

        var userAddressId = '${pd.USER_ADDR_ID}';

        if (userAddressId != '') {
            getUserAddressById(userAddressId);
        } else {
            getUserCommonAddress()
        }
    });

    // 获取SKU信息
    function getOrderSku() {
        var url = "front/getOrderSku?BASKET_ID=${orderItemId}";
        $.get(url, function (result) {
            if (result.success) {
                setOrderSkuDate(result.data.item);
            }
        }, "json")
    }

    // 获取订单项信息
    function getOrderItem() {
        var url = "front/getOrderItem?ORDER_ITEM_ID=${orderItemId}";
        $.get(url, function (result) {
            if (result.success) {
                setOrderItemDate(result.data.item);
                setPayTypeData(result.data.item);
            }
        }, "json")
    }

    // 渲染订单项信息
    function setOrderItemDate(data) {
        var divDom = $("#order_detail");
        // 先清空元素
        divDom.html("");

        var str = '';
        str += "<div class=\"order_detail_div1\">\n" +
            "            <div class=\"order_detail_div1_1\">\n" +
            "                <img src=\"" + data.PIC + "\">\n" +
            "                <div>\n" +
            "                    <div class=\"huanhang\">" + data.PROD_NAME + "</div>\n" +
            "                    <p class=\"price\">￥" + data.PRICE + "</p>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "            <div class=\"order_detail_div1_2\">\n" +
            "                <p>X" + data.PROD_COUNT + "</p>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "        <div class=\"order_detail_div2\">\n" +
            "        <div class=\"order_detail_div2_1\">\n" +
            // "            <p>订单号：" + data.ORDER_NUMBER + "</p>\n" +
            // "            <p>创建时间：" + data.GMT_CREATE + "</p>\n" +
            "        </div>\n" +
            "        <div class=\"order_detail_div2_2\">\n" +
            "            <div>合计：<span>￥" + data.PRODUCT_TOTAL_AMOUNT + "</span></div>\n" +
            "            <div class=\"order_detail_div2-btn\">\n" +
            "                <button type=\"button\" class=\"ljfk\">立即付款</button>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>";

        divDom.append(str)
    }

    // 从购物车访问渲染订单SKU信息
    function setOrderSkuDate(data) {
        var divDom = $("#order_detail");
        // 先清空元素
        divDom.html("");

        var totalMoney = 0;

        var str = '';
        for (let i = 0; i < data.length; i++) {
            var pd = data[i];

            totalMoney += pd.PRICE * pd.BASKET_COUNT;

            str += "<div class=\"order_detail_div1\">\n" +
                "       <div class=\"order_detail_div1_1\">\n" +
                "           <img src=\"" + pd.PIC + "\">\n" +
                "           <div>\n" +
                "               <div class=\"huanhang\">" + pd.PROD_NAME + "</div>\n" +
                "               <p class=\"price\">￥" + pd.PRICE + "</p>\n" +
                "           </div>\n" +
                "       </div>\n" +
                "       <div class=\"order_detail_div1_2\">\n" +
                "           <p>X" + pd.BASKET_COUNT + "</p>\n" +
                "       </div>\n" +
                "   </div>";
        }
        str += "<div class=\"order_detail_div2\">" +
            "       <div class=\"order_detail_div2_1\">\n" +
            /*"           <p>订单号：" + pd.ORDER_NUMBER + "</p>\n" +
            "           <p>创建时间：" + pd.GMT_CREATE + "</p>\n" +*/
            "       </div>\n" +
            "       <div class=\"order_detail_div2_2\">\n" +
            "           <div>合计：<span>￥" + totalMoney + "</span></div>\n" +
            "           <div class=\"order_detail_div2-btn\">\n" +
            "               <button type=\"button\" class=\"ljfk\">立即付款</button>\n" +
            "           </div>\n" +
            "       </div>\n" +
            "   </div>";
        divDom.append(str)

        setCartPayTypeData(totalMoney);
    }

    // 渲染支付方式数据
    function setPayTypeData(data) {
        var divDom = $("#payType");
        divDom.html("");

        var str = '';
        str += "<div class=\"tc_zffs\">\n" +
            "        <div class=\"tc_tit\">确认付款<i class=\"iconfont icon-guanbi8\"></i></div>\n" +
            "        <div class=\"zffs\">\n" +
            "            <h4>￥" + data.PRODUCT_TOTAL_AMOUNT + "</h4>\n" +
            "            <div class=\"mui-card\">\n" +
            "                <p>付款方式：</p>\n" +
            "                <div class=\"mui-input-group\">\n" +
            "                    <input hidden id=\"ORDER_ITEM_ID\" value=\"" + data.ORDER_ITEM_ID + "\">\n" +
            "                    <div class=\"mui-input-row mui-radio\">\n" +
            "                        <label>积分</label> <input name=\"payType\" checked value=\"1\" type=\"radio\">\n" +
            "                    </div>\n" +
            "                    <div class=\"mui-input-row mui-radio\">\n" +
            "                        <label>余额钱包</label><input name=\"payType\" value=\"0\" type=\"radio\">\n" +
            "                    </div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "        <button type=\"button\" onclick='pay()' class=\"mui-btn mui-btn-blue mui-btn-block mui_btn\">确认付款</button>\n" +
            "    </div>";
        divDom.append(str);
    }

    // 从购物车访问渲染支付方式数据
    function setCartPayTypeData(totalMoney) {
        var divDom = $("#payType");
        divDom.html("");

        var cartIdList = '${orderItemId}';

        var str = '';
        str += "<div class=\"tc_zffs\">\n" +
            "        <div class=\"tc_tit\">确认付款<i class=\"iconfont icon-guanbi8\"></i></div>\n" +
            "        <div class=\"zffs\">\n" +
            "            <h4>￥" + totalMoney + "</h4>\n" +
            "            <div class=\"mui-card\">\n" +
            "                <p>付款方式：</p>\n" +
            "                <div class=\"mui-input-group\">\n" +
            "                    <input hidden id=\"cartIdList\" value=\"" + cartIdList + "\">\n" +
            "                    <div class=\"mui-input-row mui-radio\">\n" +
            "                        <label>积分</label> <input name=\"payType\" checked value=\"1\" type=\"radio\">\n" +
            "                    </div>\n" +
            "                    <div class=\"mui-input-row mui-radio\">\n" +
            "                        <label>余额钱包</label><input name=\"payType\" value=\"0\" type=\"radio\">\n" +
            "                    </div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "        <button type=\"button\" onclick='vartPay()' class=\"mui-btn mui-btn-blue mui-btn-block mui_btn\">确认付款</button>\n" +
            "    </div>";
        divDom.append(str);
    }

    // 获取用户默认收货地址
    function getUserCommonAddress() {
        var url = "front/getUserCommonAddress?USER_ID=${user.ACCOUNT_ID}";
        $.get(url, function (result) {
            if (result.success) {
                setUserAddress(result.data)
            }
        }, "json")
    }

    // 根据地址DI获取收货地址
    function getUserAddressById(id) {
        var url = "front/getAddress?USER_ADDR_ID=" + id;
        $.get(url, function (result) {
            if (result.success) {
                setUserAddress(result.data)
            }
        }, "json")
    }

    // 渲染用户地址
    function setUserAddress(data) {
        var divDom = $("#userAddress");
        divDom.html("");

        var address = data.PROVINCE + data.CITY + data.AREA + data.ADDR;
        var str = '';
        str += "<i class=\"iconfont icon-dizhi\"></i>\n" +
            "   <div>\n" +
            "       <input hidden id='USER_ADDR_ID' value='" + data.USER_ADDR_ID + "'>\n" +
            "       <div>\n" +
            "           <h4>" + data.RECEIVER + "&nbsp;" + data.MOBILE + "</h4>\n" +
            "           <p>" + address + "</p>\n" +
            "       </div>\n" +
            "   </div>";
        // 从父级节点后面添加元素
        divDom.append(str)
    }

    // 重定向到用户地址列表
    function toUserAddress() {
        window.location.replace("front/to_mallAddress?tag=1&ORDER_ITEM_ID=${orderItemId}");
    }

</script>

<script type="text/javascript">

    // 付款
    function pay() {
        mui(".mui_btn").button('loading');//切换为loading状态

        var orderItemId = $("#ORDER_ITEM_ID").val();
        var payType = $("input[name = 'payType']:checked").val();
        var userAddressId = $("#USER_ADDR_ID").val();

        var content = '<div>\n' +
            '              <label>交易密码：</label>\n' +
            '              <input type="password" id="password">' +
            '          </div>';

        layer.open({
            title: [
                '请输入交易密码',
                'background-color:#207eff; color:#fff;'
            ]
            , anim: 'up'
            , content: content
            , btn: ['确认', '取消']
            , yes: function (index) {
                var password = $("#password").val();
                if (password == '') {
                    mui.toast("请输入交易密码")
                    return false;
                }
                shop_pay_server_veriflcation(orderItemId, payType, userAddressId, password);
                layer.close(index)
            }
            , btn2: function () {
                mui(".mui_btn").button('reset');//释放
            }
        });
    }

    // 从购物车访问付款
    function vartPay() {
        mui(".mui_btn").button('loading');//切换为loading状态

        var cartIdList = $("#cartIdList").val();
        var payType = $("input[name = 'payType']:checked").val();
        var userAddressId = $("#USER_ADDR_ID").val();
        console.log("购物车ID列表：" + cartIdList);
        console.log("支付方式：" + payType);
        console.log("用户地址ID：" + userAddressId);

        var content = '<div>\n' +
            '              <label>交易密码：</label>\n' +
            '              <input type="password" id="password">' +
            '          </div>';

        layer.open({
            title: [
                '请输入交易密码',
                'background-color:#207eff; color:#fff;'
            ]
            , anim: 'up'
            , content: content
            , btn: ['确认', '取消']
            , yes: function (index) {
                var password = $("#password").val();
                cart_pay_server_veriflcation(cartIdList, payType, userAddressId, password);
                layer.close(index)
            }
            , btn2: function () {
                mui(".mui_btn").button('reset');//释放
            }
        });
    }

    // 从商品详情跳转支付 服务端校验
    function shop_pay_server_veriflcation(orderItemId, payType, userAddressId, password) {
        var url = "front/shopBuyPay";
        $.post(url, {
            "ORDER_ITEM_ID": orderItemId,
            "payType": payType,
            "USER_ADDR_ID": userAddressId,
            "password": password,
        }, function (result) {
            mui(".mui_btn").button('reset');//释放
            if (result.success) {
                // 访问我的订单页面
                window.location.replace("front/to_order");
            } else {
                mui.toast(result.message);

            }
        })

    }

    // 从购物车跳转支付 服务端校验
    function cart_pay_server_veriflcation(cartIdList, payType, userAddressId, password) {
        var url = "front/cartBuyPay";
        $.post(url, {
            "BASKET_ID": cartIdList,
            "payType": payType,
            "USER_ADDR_ID": userAddressId,
            "password": password,
        }, function (result) {
            mui(".mui_btn").button('reset');//释放
            if (result.success) {
                // 访问我的订单页面
                window.location.replace("front/to_order");
            } else {
                mui.toast(result.message);

            }
        })

    }

    // 支付弹窗
    //监听新增DOM元素的点击事件
    $('body').on('click', '.ljfk', function () {
        $('.tc_bg').fadeIn('fast');
        $('.tc_zffs').addClass('alert');
        $('.tc_zffs').removeClass('alert1');
    });
    $('body').on('click', '.tc_tit i', function () {
        $('.tc_bg').fadeOut('slow');
        $('.tc_zffs').addClass('alert1');
        $('.tc_zffs').removeClass('alert');
    })
</script>

</html>
