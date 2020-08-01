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
    <h1 class="mui-title">订单详情</h1>
</header>
<div class="mui-content">
    <div id="dataList">
        <%-- <!-- 地址 -->
         <div class="ordeti_top">
             <div><span>阿姐</span>12345678987</div>
             <p>广西南宁市西乡塘区168号</p>
         </div>
         <!-- 商品 -->
         <div class="ordeti_shop">
             <p class="ordeti_shop_p">待支付</p>
             <div class="ordeti_shop_1 flex">
                 <img src="static/front/images/mine_bg.png" alt="">
                 <div class="ordeti_shop_11">
                     <h4 class="txt-cut1">打开卡顿哦我我等我ID久哦我我都懒得卡死的骄傲了大理石的敬爱啊实打实</h4>
                     <p>数量：1 迪士尼联名款-美女与野兽</p>
                     <div>￥<span>1000.00</span></div>
                 </div>
             </div>
         </div>
         <!-- 订单信息 -->
         <ul class="ordeti_ul">
             <li>
                 <div><span>订单编号：</span>113255488888888968</div>
                 <button data-clipboard-action="copy" data-clipboard-text="iipf " id="d_clip_button" type="button"
                         class="mui-btn mui-btn-blue">复制
                 </button>
             </li>
             <li>
                 <div><span>下单时间：</span>2020-05-21 00:00:00</div>
             </li>
         </ul>
         <ul class="ordeti_ul">
             <li>
                 <div><span>支付方式：</span>微信支付</div>
             </li>
             <li>
                 <div><span>配送方式：</span>普通配送</div>
             </li>
             <li>
                 <div><span>订单备注：</span>啥卡和开始的话奥斯卡的骄傲斯柯达</div>
             </li>
         </ul>
         <ul class="ordeti_ul1">
             <li>
                 <p>订单总额：</p>
                 <div>￥<span>100.00</span></div>
             </li>
             <li>
                 <p>运费：</p>
                 <div>￥<span>15.00</span></div>
             </li>
             <li>
                 <p>总优惠金额：</p>
                 <div>-￥<span>15.00</span></div>
             </li>
             <li>
                 <h4>实际总额：<span>￥</span>150.00</h4>
             </li>
         </ul>--%>
    </div>
    <!-- 联系客服 -->
    <%-- <div class="ordeti_kf">
         <button type="button" class="mui-btn mui-btn-blue">联系客服</button>
     </div>--%>
</div>

</body>


<script type="text/javascript">

    // 初始化
    $(function () {
        getOrderDetail();

        var clipboard = new ClipboardJS('.d_clip_button');
        clipboard.on('success', function (e) {
            mui.toast("复制成功");
            e.clearSelection();
        });
    });

    // 获取订单详情
    function getOrderDetail() {
        var url = "front/getOrderDetail?ORDER_ID=${ORDER_ID}";
        $.get(url, function (result) {
            if (result.success) {
                setDataList(result.data.item)
            }
        })
    }

    // 设置数据列表
    function setDataList(data) {
        var listDom = $("#dataList");
        listDom.html("");


        var str = '';
        // 地址
        str += " <div class=\"ordeti_top\">\n" +
            "        <div><span>" + data[0].RECEIVER + "</span>" + data[0].MOBILE + "</div>\n" +
            "        <p>" + data[0].PROVINCE + data[0].CITY + data[0].AREA + data[0].ADDR + "</p>\n" +
            "    </div>";

        str += "<div class=\"ordeti_shop\">\n" +
            "      <p class=\"ordeti_shop_p\">" + data[0].STATUS + "</p>";

        for (let i = 0; i < data.length; i++) {
            var pd = data[i];
            // 商品
            str += "      <div class=\"ordeti_shop_1 flex\">\n" +
                "          <img src=\"" + pd.PIC + "\" alt=\"\">\n" +
                "          <div class=\"ordeti_shop_11\">\n" +
                "              <h4 class=\"txt-cut1\">" + pd.PROD_NAME + "</h4>\n" +
                "              <p>数量：" + pd.PROD_COUNT + " <!-- 此处预留SKU名称--></p>\n" +
                "              <div>￥<span>" + pd.PRICE + "</span></div>\n" +
                "          </div>\n" +
                "      </div>";
        }
        str += "  </div>";
        // 订单信息
        str += "<ul class=\"ordeti_ul\">\n" +
            "       <li>\n" +
            "           <div><span>订单编号：</span>" + data[0].ORDER_NUMBER + "</div>\n" +
            "           <button data-clipboard-action=\"copy\" data-clipboard-text=\"" + data[0].ORDER_NUMBER + "\" type=\"button\"\n" +
            "                   class=\"mui-btn mui-btn-blue d_clip_button\">复制\n" +
            "           </button>\n" +
            "       </li>\n" +
            "       <li>\n" +
            "           <div><span>物流单号：</span>" + data[0].DVY_FLOW_ID + "</div>\n" +
            "           <button data-clipboard-action=\"copy\" data-clipboard-text=\"" + data[0].DVY_FLOW_ID + "\" type=\"button\"\n" +
            "                   class=\"mui-btn mui-btn-blue d_clip_button\">复制\n" +
            "           </button>\n" +
            "       </li>\n" +
            "       <li>\n" +
            "           <div><span>下单时间：</span>" + data[0].GMT_CREATE + "</div>\n" +
            "       </li>\n" +
            "   </ul>\n" +
            "   <ul class=\"ordeti_ul\">\n" +
            "       <li>";
        if (data[0].PAY_TYPE == 1) {
            str += "<div><span>支付方式：</span>商城积分</div>";
        }
        if (data[0].PAY_TYPE == 0) {
            str += "<div><span>支付方式：</span>金额</div>";
        }
        str += "    </li>\n" +
            "       <li>\n" +
            "           <div><span>配送方式：</span>普通配送</div>\n" +
            "       </li>\n" +
            "       <li>\n" +
            "           <div><span>订单备注：</span>" + data[0].REMARKS + "</div>\n" +
            "       </li>\n" +
            "   </ul>\n" +
            "   <ul class=\"ordeti_ul1\">\n" +
            "       <li>\n" +
            "           <p>订单总额：</p>\n" +
            "           <div>￥<span>" + data[0].TOTAL + "</span></div>\n" +
            "       </li>\n" +
            "       <li>\n" +
            "           <p>运费：</p>\n" +
            "           <div>￥<span>" + data[0].FREIGHT_AMOUNT + "</span></div>\n" +
            "       </li>\n" +
            "       <li>\n" +
            "           <p>总优惠金额：</p>\n" +
            "           <div>-￥<span>" + data[0].REDUCE_AMOUNT + "</span></div>\n" +
            "       </li>\n" +
            "       <li>\n" +
            "           <h4>实际总额：<span>￥</span>" + data[0].ACTUAL_TOTAL + "</h4>\n" +
            "       </li>\n" +
            "   </ul>";
        listDom.append(str);
    }

</script>


</html>
