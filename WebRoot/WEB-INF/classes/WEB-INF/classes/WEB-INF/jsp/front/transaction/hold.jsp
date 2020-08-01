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
    .hold_top {
        color: #878787;
        font-size: 14px;
        background-color: white;
        padding-bottom: 10px;
    }

    .hold_top label {
        color: #000000;
    }

    .hold_top span {
        color: red;
    }

    .hold_top .flex2 {
        padding: 10px 5px;
    }

    .quota_top table th, .quota_top table td {
        width: 25%;
        font-size: 14px;
        font-weight: normal;
    }

    .quota_top table td {
        font-size: 12px;
    }

    .quota_top table td label {
        color: red;
    }

    .quota_top table td span {
        color: blue;
    }
</style>
<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title">持有</h1>
</header>
<div class="mui-content">
    <!--  -->
    <div class="hold_top" id="hold_top">
        <div class="flex2">
            <div>可用：<label id="money">---</label></div>
            <div>盈亏：<span id="totalProfit">---</span></div>
        </div>
        <div class="flex2">
            <div>市值：<label id="totalMarketValue">---</label></div>
            <div>资产：<label id="totalAsset">---</label></div>
        </div>
    </div>
    <!--  -->
    <div class="quota_top">
        <table border="0" cellspacing="" cellpadding="">
            <tr>
                <th>商品</th>
                <th>余额/可卖量</th>
                <th>成本价/最新价</th>
                <th>盈亏/市值</th>
            </tr>
            <tbody id="dataList">
            <tr>
                <td>
                    <div>0001</div>
                    <div>测试</div>
                </td>
                <td>
                    <div>15</div>
                    <span>15</span>
                </td>
                <td>
                    <div>1000</div>
                    <div>1010</div>
                </td>
                <td>
                    <label>12321</label>
                    <div>1.23万</div>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>

</body>


<script type="text/javascript">

    // 用户持有股票ID列表
    var holdProdIdList = "";

    // 初始化
    $(function () {
        getHold();
        getUserInfo();
    });

    // 获取用户信息
    function getUserInfo() {
        $.get("front/getUserInfo", function (result) {
            if (result.success) {
                $('#money').text(result.data.MONEY)
            }
        })
    }

    // 获取持有的股票
    function getHold() {
        var url = "front/listAllTakeGoods";
        $.get(url, function (result) {
            if (result.success) {
                setHold(result.data.item);
                holdProdIdList = result.data.holdProdIdList;
            }
        })
    }

    // 渲染持有的股票
    function setHold(data) {
        var tbodyDom = $('#dataList');
        tbodyDom.html("");

        var str = '';
        for (let i = 0; i < data.length; i++) {
            var pd = data[i];
            if (pd.REMAINDER < 1) {
                continue;
            }

            str += '<tr> ' +
                '       <td>\n' +
                '           <div>' + pd.NUMBER_CODE + '</div>\n' +
                '           <div>' + pd.PROD_NAME + '</div>\n' +
                '       </td>\n' +
                '       <td>\n' +
                '           <div>' + pd.TOTAL + '</div>\n' +
                '           <span>' + pd.REMAINDER + '</span>\n' +
                '       </td>\n' +
                '       <td>\n' +
                '           <div>' + pd.ORI_PRICE + '</div>\n' +
                '           <div class="price_' + pd.SHARES_PROD_ID + '">' + pd.PRICE + '</div>\n' +
                '       </td>\n' +
                '       <td>\n' +
                '           <label>' + (pd.PRICE - pd.ORI_PRICE) * pd.REMAINDER + '</label>\n' +
                '           <div>' + pd.PRICE * pd.REMAINDER + '</div>\n' +
                '       </td>' +
                '   </tr>';
        }
        tbodyDom.append(str)
    }

    // 从服务器主动推送过来的最新价格
    function setStockList(data) {
        setLatestPrice(data)
    }

    // 渲染最新价格
    function setLatestPrice(data) {
        var money = Number($('#money').text());
        var totalProfit = 0;
        var totalMarketValue = 0;
        for (let i = 0; i < data.length; i++) {
            var pd = data[i];
            var SHARES_PROD_ID = pd.SHARES_PROD_ID;
            // 不属于用户持有的商品就进入下一个循环
            if (holdProdIdList.indexOf(SHARES_PROD_ID) == -1) {
                continue;
            }
            $('.price_' + SHARES_PROD_ID).html(pd.PRICE);
            // 节点
            var dom = $('.price_' + SHARES_PROD_ID).parent().parent().children().children();
            // 有多少条数据
            var total = dom.length;
            // 下标、
            var j = 4;
            while (total > j) {
                var oriPrice = dom[j].innerHTML;
                var remainder = dom[j - 1].innerHTML;
                // 计算盈亏，四舍五入保留两位小数
                var result = (pd.PRICE - oriPrice) * remainder;
                result = result.toFixed(2);
                // 累积总盈亏
                totalProfit += Number(result);
                var marketValue = pd.PRICE * remainder;
                marketValue = marketValue.toFixed(2);
                // 累积总市值
                totalMarketValue += Number(marketValue);
                // 盈亏
                dom[j + 2].innerHTML = result;
                // 市值
                dom[j + 3].innerHTML = marketValue;
                j += 8;
            }
        }
        // 更新持有顶部资产
        $('#totalProfit').text(totalProfit.toFixed(2));
        $('#totalMarketValue').text(totalMarketValue.toFixed(2));
        var totalAsset = money + totalMarketValue;
        $('#totalAsset').text(totalAsset.toFixed(2));
    }

</script>

</html>
