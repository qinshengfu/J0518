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
    .tran_nav_div {
        display: none;
        margin-top: 44px;
    }
</style>

<body>
<div class="mui-content">
    <!-- nav -->
    <ul class="tran_nav flex1">
        <li class="tran_nav_active">买入</li>
        <li>卖出</li>
        <li>撤单</li>
        <li onclick="toHold()">持有</li>
    </ul>
    <!-- 买入 -->
    <div class="tran_nav_div" style="display: block;">
        <div class="tran_cent" id="buy">
            <%--<div class="tran_left">
                <!-- 选择框 -->
                <select name="">
                    <option value="">30001-祥云林芝</option>
                    <option value="">30002-祥云林芝</option>
                </select>
                <!-- 加减 -->
                <div class="tran_inp">
                    <button class="mui-btn" type="button">-</button>
                    <input class="mui-numbox-input" type="number" placeholder="价格"/>
                    <button class="mui-btn" type="button">+</button>
                </div>
                <div class="tran_inp">
                    <button class="mui-btn" type="button">-</button>
                    <input class="mui-numbox-input" type="number" placeholder="数量"/>
                    <button class="mui-btn" type="button">+</button>
                </div>
                <p>可买数量：0</p>
                <ul class="tran_ul">
                    <li>涨停：7895</li>
                    <li>跌停：7895</li>
                </ul>
                <button type="button" class="mui-btn mui-btn-blue mui-btn-block mairu">买入</button>
            </div>

            <div class="tran_right">
                <ul>
                    <li>
                        <p>卖1</p>
                        <div>4538</div>
                        <span>68</span>
                    </li>
                    <li>
                        <p>卖1</p>
                        <div>4538</div>
                        <span>68</span>
                    </li>
                    <li>
                        <p>卖1</p>
                        <div>4538</div>
                        <span>68</span>
                    </li>
                    <li>
                        <p>卖1</p>
                        <div>4538</div>
                        <span>68</span>
                    </li>
                </ul>
            </div>--%>
        </div>
        <!-- bot -->
        <div class="tran_table">
            <table border="0" cellspacing="" cellpadding="">
                <tr>
                    <th>商品</th>
                    <th>余额/可卖量</th>
                    <th>成本价/最新价</th>
                    <th>盈亏</th>
                </tr>
                <tbody class="holdRecord">

                </tbody>
            </table>
        </div>
    </div>

    <!-- 卖出 -->
    <div class="tran_nav_div">
        <div class="tran_cent" id="sell">
            <%-- <div class="tran_left">
                 <!-- 选择框 -->
                 <select name="">
                     <option value="">30001-祥云林芝</option>
                     <option value="">30002-祥云林芝</option>
                 </select>
                 <!-- 加减 -->
                 <div class="tran_inp">
                     <button class="mui-btn" type="button">-</button>
                     <input class="mui-numbox-input" type="number" placeholder="价格"/>
                     <button class="mui-btn" type="button">+</button>
                 </div>
                 <div class="tran_inp">
                     <button class="mui-btn" type="button">-</button>
                     <input class="mui-numbox-input" type="number" placeholder="数量"/>
                     <button class="mui-btn" type="button">+</button>
                 </div>
                 <p>可买数量：0</p>
                 <ul class="tran_ul">
                     <li>涨停：7895</li>
                     <li>跌停：7895</li>
                 </ul>
                 <button type="button" class="mui-btn mui-btn-blue mui-btn-block maichu">卖出</button>
             </div>

             <div class="tran_right">
                 <ul>
                     <li>
                         <p>买1</p>
                         <div>4538</div>
                         <span>68</span>
                     </li>
                     <li>
                         <p>买1</p>
                         <div>4538</div>
                         <span>68</span>
                     </li>
                     <li>
                         <p>买1</p>
                         <div>4538</div>
                         <span>68</span>
                     </li>
                     <li>
                         <p>买1</p>
                         <div>4538</div>
                         <span>68</span>
                     </li>
                 </ul>
             </div>--%>
        </div>

        <!-- bot -->
        <div class="tran_table">
            <table border="0" cellspacing="" cellpadding="">
                <tr>
                    <th>商品</th>
                    <th>余额/可卖量</th>
                    <th>成本价/最新价</th>
                    <th>盈亏</th>
                </tr>
                <tbody class="holdRecord">

                </tbody>
            </table>
        </div>
    </div>

    <!-- 撤单 -->
    <div class="tran_nav_div">
        <!-- bot -->
        <div class="tran_table">
            <table border="0" cellspacing="" cellpadding="">
                <tr>
                    <th>名称</th>
                    <th>类型</th>
                    <th>数量</th>
                    <th>状态</th>
                    <th></th>
                </tr>
                <tbody id="cancelOrder">

                </tbody>
            </table>
        </div>
    </div>

    <!-- 持有 -->
    <div class="tran_nav_div">
        <!-- bot -->
        <div class="tran_table">
            <table border="0" cellspacing="" cellpadding="">
                <tr>
                    <th>名称</th>
                    <th>类型</th>
                    <th>余额/可卖量</th>
                    <th>状态</th>
                </tr>
                <tbody id="holdList">
                <tr>
                    <td>灵芝</td>
                    <td>100</td>
                    <td>交易中</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@include file="../../front/footer/footer.jsp" %>

</body>

<%-- 页面渲染 --%>
<script>

    // 用户持有股票ID列表
    var holdProdIdList = "";

    // 初始化
    $(function () {
        getStockProd();
        getHold();
    });

    // 获取所有余数大于0的股票商品
    function getStockProd() {
        var url = "front/getStockInfoList";
        $.get(url, function (result) {
            if (result.success) {
                setBuyProd(result.data.item)
            }
        })
    }

    // 获取最近10个买卖订单
    function getLastBuyAndSellOrder(id) {
        var url = "front/getLastBuyAndSellOrder?SHARES_PROD_ID=" + id;
        $.get(url, function (result) {
            if (result.success) {
                setLastBuyAndSellOrder(result.data.item)
            }
        })
    }

    // 渲染买入
    function setBuyProd(data) {
        var divDom = $('#buy');
        divDom.html("");

        var str = '';

        // 头部
        str += '<div class="tran_left">' +
            '     <select onchange="buyPullDownBox()" id="buyProdId" name="SHARES_PROD_ID">';

        // 选择框
        for (let i = 0; i < data.length; i++) {
            var pd = data[i];
            if (pd.STATUS != '交易中') {
                continue;
            }
            str += '<option id="' + pd.SHARES_PROD_ID + '" value="' + pd.OPEN_PRICE + '">' + pd.NUMBER_CODE + '- ' + pd.PROD_NAME + '</option>'
        }
        str += '</select>';

        // 加减框、可买数量
        str += '<span>价格：</span><div class="mui-numbox tran_inp" id="buy_tran_inp" data-numbox-step=\'1\' data-numbox-min=\'1\'>\n' +
            '       <button class="mui-btn mui-numbox-btn-minus" type="button">-</button>\n' +
            '       <input class="mui-numbox-input" type="number" value="" id="buyPrice" placeholder="价格"/>\n' +
            '       <button class="mui-btn mui-numbox-btn-plus" type="button">+</button>\n' +
            '   </div>\n' +
            '   <span>数量：</span><div class="mui-numbox tran_inp" data-numbox-step=\'1\' data-numbox-min=\'1\'>\n' +
            '       <button class="mui-btn mui-numbox-btn-minus" type="button">-</button>\n' +
            '       <input class="mui-numbox-input" type="number" value="" id="buyNum" placeholder="数量"/>\n' +
            '       <button class="mui-btn mui-numbox-btn-plus" type="button">+</button>\n' +
            '   </div>\n' +
            // '       <p>可买数量：' + data[0].REMAINDER + '</p>\n' +
            /*'       <ul class="tran_ul">\n' +
            '           <li>涨停：7895</li>\n' +
            '           <li>跌停：7895</li>\n' +
            '       </ul>\n' +*/
            '       <button type="button" onclick="buy()" class="mui-btn mui-btn-blue mui-btn-block mairu">买入</button>\n' +
            '   </div>';
        // 右侧交易记录
        str += '<div class="tran_right">\n' +
            '       <ul class="buyAndSellRec">\n' +
            '       </ul>\n' +
            '   </div>';
        divDom.append(str);

        // 初始化数字输入框
        mui('.mui-numbox').numbox();
        buyPullDownBox()
    }

    // 渲染卖出
    function setSellProd(data) {
        var divDom = $('#sell');
        divDom.html("");

        var str = '';

        // 头部
        str += '<div class="tran_left">' +
            '     <select onchange="sellPullDownBox()" id="sellProdId" name="SHARES_ORDER_ID">';

        // 选择框
        for (let i = 0; i < data.length; i++) {
            var pd = data[i];
            if (pd.STATUS == '持仓' && pd.REMAINDER > 0) {
                str += '<option id="' + pd.SHARES_ORDER_ID + '" value="' + pd.OPEN_PRICE + '">' + pd.NUMBER_CODE + '- ' + pd.PROD_NAME + '</option>'
            }
        }
        str += '</select>';

        // 加减框、可买数量
        str += '<span>价格：</span><div class="mui-numbox tran_inp" id="sell_tran_inp" data-numbox-step=\'1\' data-numbox-min=\'1\'>\n' +
            '       <button class="mui-btn mui-numbox-btn-minus" type="button">-</button>\n' +
            '       <input class="mui-numbox-input" type="number" value="" id="sellPrice" placeholder="价格"/>\n' +
            '       <button class="mui-btn mui-numbox-btn-plus" type="button">+</button>\n' +
            '   </div>\n' +
            '   <span>数量：</span><div class="mui-numbox tran_inp" data-numbox-step=\'1\' data-numbox-min=\'1\'>\n' +
            '       <button class="mui-btn mui-numbox-btn-minus" type="button">-</button>\n' +
            '       <input class="mui-numbox-input" type="number" value="" id="sellNum" placeholder="数量"/>\n' +
            '       <button class="mui-btn mui-numbox-btn-plus" type="button">+</button>\n' +
            '   </div>\n' +
            // '       <p>可买数量：' + data[0].REMAINDER + '</p>\n' +
            /* '       <ul class="tran_ul">\n' +
             '           <li>涨停：7895</li>\n' +
             '           <li>跌停：7895</li>\n' +
             '       </ul>\n' +*/
            '       <button type="button" onclick="sell()" class="mui-btn mui-btn-blue mui-btn-block maichu">卖出</button>\n' +
            '   </div>';
        // 右侧交易记录
        str += '<div class="tran_right">\n' +
            '       <ul class="buyAndSellRec">\n' +
            '       </ul>\n' +
            '   </div>';
        divDom.append(str);

        // 初始化数字输入框
        mui('.mui-numbox').numbox();
        sellPullDownBox()
    }

    // 渲染最新10条的买卖数量和单价
    function setLastBuyAndSellOrder(data) {
        var ulDom = $('.buyAndSellRec');
        ulDom.html("");
        var j = 1;
        var k = 1;
        for (let i = 0; i < data.length; i++) {
            var pd = data[i];
            var str = '';

            if ('买入' == pd.TYPE) {
                str += '<li>\n' +
                    '       <p>买' + j + '</p>\n' +
                    '       <div>' + pd.PRICE + '</div>\n' +
                    '       <span>' + pd.TOTAL + '</span>\n' +
                    '   </li>'
                ulDom.append(str);
                j++;
            }
            if ('卖出' == pd.TYPE) {
                str += '<li>\n' +
                    '       <p>卖' + k + '</p>\n' +
                    '       <div>' + pd.PRICE + '</div>\n' +
                    '       <span>' + pd.TOTAL + '</span>\n' +
                    '   </li>';
                ulDom.prepend(str);
                k++;
            }
        }
    }

    // 渲染取消订单
    function setCancelOrder(data) {
        var tbodyDom = $('#cancelOrder');
        tbodyDom.html("");

        var str = '';
        for (let i = 0; i < data.length; i++) {
            var pd = data[i];

            if (pd.STATUS != '交易中') {
                continue;
            }
            str += '<tr>\n' +
                '       <td>' + pd.PROD_NAME + '</td>\n' +
                '       <td>' + pd.TYPE + '</td>\n' +
                '       <td>' + pd.REMAINDER + '</td>\n' +
                '       <td>' + pd.STATUS + '</td>\n' +
                '       <td>\n' +
                '           <button type="button" onclick="cancelOrder(' + pd.SHARES_ORDER_ID + ',\'' + pd.PROD_NAME + '\')" class="mui-btn mui-btn-blue">撤单</button>\n' +
                '       </td>\n' +
                '   </tr>'

        }
        tbodyDom.append(str)
    }

    // 从服务器主动推送过来的最新价格
    function setStockList(data) {
        setLatestPrice(data)
    }

    // 渲染最新价格
    function setLatestPrice(data) {
        for (let i = 0; i < data.length; i++) {
            var pd = data[i];
            var SHARES_PROD_ID = pd.SHARES_PROD_ID;
            // 不属于用户持有的商品就进入下一个循环
            if (holdProdIdList.indexOf(SHARES_PROD_ID) == -1) {
                continue;
            }
            $('.price_' + SHARES_PROD_ID).html(pd.PRICE);
            // 有多少条数据
            var total = $('.price_' + SHARES_PROD_ID).parent().parent().children().children().length;
            // 下标
            var j = 4;
            while (total > j) {
                var oriPrice = $('.price_' + SHARES_PROD_ID).parent().parent().children().children()[j - 2].innerHTML;
                var remainder = $('.price_' + SHARES_PROD_ID).parent().parent().children().children()[j - 3].innerHTML;
                // 计算盈亏，四舍五入保留两位小数
                var result = (pd.PRICE - oriPrice) * remainder;
                result = result.toFixed(2);
                // 盈亏
                $('.price_' + SHARES_PROD_ID).parent().parent().children().children()[j].innerHTML = result;
                j += 5;
            }
        }
    }

    // 渲染底部持有记录
    function setBuyRecord(data) {
        var divDom = $('.holdRecord');
        divDom.html("");
        var str = '';
        for (let i = 0; i < data.length; i++) {
            var pd = data[i];
            if (pd.STATUS == '已取消' || pd.EVENT == '取消订单' || pd.REMAINDER < 1) {
                continue;
            }
            str += '<tr>\n' +
                '       <td>' + pd.NUMBER_CODE + '' +
                '           <div>' + pd.PROD_NAME + '</div>\n' +
                '       </td>\n' +
                '       <td>' + pd.TOTAL + '' +
                '           <div style="color: #00adff;">' + pd.REMAINDER + '</div>\n' +
                '       </td>\n' +
                '       <td> <span> ' + pd.ORI_PRICE + '</span>' +
                '           <div class="price_' + pd.SHARES_PROD_ID + '">' + pd.PRICE + '</div>\n' +
                '       </td>\n' +
                '       <td>\n' +
                '           <div style="color: red;">null</div>\n' +
                '       </td>\n' +
                '   </tr>';
        }
        divDom.append(str)
    }

    // 获取持有的股票
    function getHold() {
        var url = "front/listAllTakeGoods";
        $.get(url, function (result) {
            if (result.success) {
                setHold(result.data.item);
                // 底部持有商品
                setBuyRecord(result.data.item);
                holdProdIdList = result.data.holdProdIdList;
                setCancelOrder(result.data.item)
                setSellProd(result.data.item);
            }
        })
    }

    // 渲染持有的股票
    function setHold(data) {
        var tbodyDom = $('#holdList');
        tbodyDom.html("");

        var str = '';
        for (let i = 0; i < data.length; i++) {
            var pd = data[i];

            if (pd.REMAINDER < 1) {
                continue;
            }

            str += '<tr>\n' +
                '       <td>' + pd.PROD_NAME + '</td>\n' +
                '       <td>' + pd.TYPE + '</td>\n' +
                '       <td>' + pd.TOTAL + '/' + pd.REMAINDER + '</td>\n' +
                '       <td>' + pd.STATUS + '</td>\n' +
                '   </tr>';
        }
        tbodyDom.append(str)
    }


</script>

<%-- 操作相关 --%>
<script>

    // 访问持有页面
    function toHold() {
        window.location.href = "front/toHold"
    }

    // 买入客户端校验
    function buy() {
        layer.load();
        // 想买入的股票ID
        var buyProdId = $("#buyProdId option:checked").attr("id");
        // 单价、数量
        var price = $('#buyPrice').val();
        var num = $('#buyNum').val();

        if (!buyProdId) {
            layer.closeAll('loading');
            return;
        }
        if (price < 1 || num < 1) {
            mui.toast('不允许输入负数');
            layer.closeAll('loading');
            return false;
        }
        buyServerCheck(buyProdId, price, num)
    }

    // 卖出客户端校验
    function sell() {
        layer.load();
        // 想卖入的股票ID
        var sellProdId = $("#sellProdId option:checked").attr("id");
        // 单价、数量
        var price = $('#sellPrice').val();
        var num = $('#sellNum').val();
        if (!sellProdId) {
            layer.closeAll('loading');
            return false;
        }

        if (price < 1 || num < 1) {
            mui.toast('不允许输入负数');
            layer.closeAll('loading');
            return false;
        }
        sellServerCheck(sellProdId, price, num)
    }

    // 买入服务端校验
    function buyServerCheck(id, price, num) {
        var url = "front/issueBuyStock";
        $.post(url, {"SHARES_PROD_ID": id, "PRICE": price, "TOTAL": num}, function (result) {
            layer.closeAll('loading');
            mui.toast(result.message);
            if (result.success) {
                setTimeout(function () {
                    window.location.reload();
                }, 1200)
            }
        })

    }

    // 卖出服务端校验
    function sellServerCheck(id, price, num) {
        var url = "front/issueSellStock";
        $.post(url, {"SHARES_ORDER_ID": id, "PRICE": price, "TOTAL": num}, function (result) {
            layer.closeAll('loading');
            mui.toast(result.message);
            if (result.success) {
                setTimeout(function () {
                    window.location.reload();
                }, 1200)
            }
        })

    }

    // 取消订单客户端校验
    function cancelOrder(id, name) {
        layer.confirm('确认要取消 ' + name + ' 吗？', {
            btn: ['确认', '取消'] //按钮
        }, function () {
            cancelOrderServerCheck(id)
        }, function () {
            layer.closeAll()
        });


    }

    // 取消订单服务端校验
    function cancelOrderServerCheck(id) {
        layer.load();
        var url = "front/cancelOrderCheck";
        $.post(url, {"SHARES_ORDER_ID": id}, function (result) {
            layer.closeAll();
            mui.toast(result.message);
            if (result.success) {
                setTimeout(function () {
                    window.location.reload();
                }, 1000)
            }
        })

    }

</script>


<script type="text/javascript">

    // 下拉框联动
    function buyPullDownBox() {
        // 获取被选中的option标签的value值
        var buyPrice = $('#buyProdId').val();
        $('#buyPrice').val(buyPrice);
        mui("#buy_tran_inp").numbox().setOption('min', buyPrice);

        var buyProdId = $("#buyProdId option:checked").attr("id");
        getLastBuyAndSellOrder(buyProdId)
    }

    function sellPullDownBox() {
        // 获取被选中的option标签的value值
        var sellPrice = $('#sellProdId').val();
        $('#sellPrice').val(sellPrice);
        mui("#sell_tran_inp").numbox().setOption('min', sellPrice);
    }

    $(function () {


        $(".tran_nav li").click(function () {
            /*每个li下属的div*/
            var divShow = $(".tran_nav_div");
            /*利用selected进行判断*/
            if (!$(this).hasClass("tran_nav_active")) {
                /*li标签的顺序和div的顺序是对应的，获取索引*/
                var index = $(this).index();
                if (index == $(".tran_nav li").length - 1) {
                    return;
                }
                /*当前对象设置class属性*/
                $(this).addClass("tran_nav_active");
                /*移除其他同级元素属性*/
                $(this).siblings("li").removeClass("tran_nav_active");
                /*展示当前li对应的div内容,利用方法显示和隐藏*/
                $(divShow[index]).show();
                /*隐藏同级元素*/
                $(divShow[index]).siblings(".tran_nav_div").hide();
            }
        })
    })
</script>


</html>
