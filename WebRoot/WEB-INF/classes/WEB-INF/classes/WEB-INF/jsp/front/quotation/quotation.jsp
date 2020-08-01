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
    .table_wid{
        overflow-x: scroll;
        width: 100%;
        background: white;
    }
    .table_wid .flex{
        display:-moz-inline-box;
        display:-webkit-inline-box;
    }
    .biaoti div,.xinxi div{
        width: 100px;
    }
</style>
<body>
<header class="mui-bar mui-bar-nav">
    <h1 class="mui-title">行情</h1>
</header>
<div class="mui-content">
    <!--  -->
    <div class="quota_top">
        <h4>限量发行</h4>
        <table border="0" cellspacing="" cellpadding="">
            <tr>
                <th>编号</th>
                <th>名称</th>
                <th>价格</th>
                <th>总数/余量</th>
            </tr>

            <tbody id="stockProdList">
            <tr>
                <td>测试股票商品</td>
                <td>￥300/盒</td>
                <td class="flex">1000/522
                    <button type="button" class="mui-btn mui-btn-blue">即抢</button>
                </td>
            </tr>
            </tbody>

        </table>
    </div>
    <!--  -->
    <div class="table_wid">
        <div class="biaoti flex">
            <div>编号</div>
            <div>名称</div>
            <div>最新</div>
            <div>涨跌</div>
            <div>幅度</div>
            <div>开盘价</div>
            <div>昨收价</div>
            <div>总发行量</div>
            <div>当日成交量</div>
            <div>当日交易额</div>
        </div>
        <!--  -->
        <div id="stockList">

        </div>
    </div>
</div>

<%@include file="../../front/footer/footer.jsp" %>

</body>

<script type="text/javascript">

    // 初始化
    $(function () {
        getStockList();
        getStockProd();
    });

    // 获取所有股票版块信息
    function getStockList() {
        var url = "front/getStockInfoList";
        $.get(url, function (result) {
            if (result.success) {
                setStockList(result.data.item)
            }
        })
    }

    // 渲染行情的最新股票信息
    function setStockList(data) {
        var divDom = $('#stockList');
        divDom.html("")

        for (let i = 0; i < data.length; i++) {
            var pd = data[i];

            var str = '';
            str = '<div class="xinxi flex" onclick="toKLine(' + pd.SHARES_PROD_ID + ')">\n' +
                '      <div><span>' + pd.NUMBER_CODE + '</span></div>\n' +
                '      <div><span>' + pd.PROD_NAME + '</span></div>\n' +
                '      <div><span class="red">' + pd.PRICE + '</span></div>\n' +
                '      <div><span>' + pd.WAVE + '</span></div>\n' +
                '      <div><span>' + pd.RANGE + '%</span></div>\n' +

                '      <div><span>' + pd.OPEN_PRICE + '</span></div>\n' +
                '      <div><span>' + pd.CLOSING_PRICE + '</span></div>\n' +
                '      <div><span>' + pd.TOTAL + '</span></div>\n' +
                '      <div><span>' + pd.DAY_VOLUME + '</span></div>\n' +
                '      <div><span>' + pd.DAY_TRADING + '</span></div>\n' +
                '  </div>';
            divDom.append(str)
        }
        setStockProd(data)
    }

    // 访问K线图
    function toKLine(id) {
        window.location.href = "front/to_kLine?id=" + id;
    }

    // 获取所有余数大于0的股票商品
    function getStockProd() {
        var url = "front/getStockProd";
        $.get(url, function (result) {
            if (result.success) {
                setStockProd(result.data.item)
            }
        })
    }

    // 渲染限量发售（股票商品）
    function setStockProd(data) {
        var tbodyDom = $('#stockProdList');
        tbodyDom.html("")

        var str = '';
        for (let i = 0; i < data.length; i++) {
            var pd = data[i];
            if (pd.REMAINDER < 1 || pd.STATUS != '发行中') {
                continue;
            }

            str += '<tr>\n' +
                '       <td>' + pd.NUMBER_CODE + '</td>\n' +
                '       <td>' + pd.PROD_NAME + '</td>\n' +
                '       <td>￥' + pd.OPEN_PRICE + '/' + pd.UNIT + '</td>\n' +
                '       <td class="flex">' + pd.TOTAL + '/' + pd.REMAINDER + '\n' +
                '           <button type="button" onclick="buyProd(\'' + pd.SHARES_PROD_ID + '\',\'' + pd.PROD_NAME + '\')" class="mui-btn mui-btn-blue">即抢</button>\n' +
                '       </td>\n' +
                '   </tr>';
        }
        tbodyDom.append(str)
    }

    // 购买股票商品
    function buyProd(id, name) {
        layer.load();
        var content = '<div>\n' +
            '              <label>数量：</label>\n' +
            '              <input type="number" id="amount">' +
            '          </div>';

        layer.open({
            title: [
                '购买 ' + name,
                'background-color:#207eff; color:#fff;'
            ]
            , anim: 'up'
            , content: content
            , btn: ['确认', '取消']
            , yes: function (index) {
                var amount = $("#amount").val();
                prod_buy_server_veriflcation(id, amount);
                layer.close(index)
            }
            , btn2: function () {
                layer.closeAll('loading')
            }
            , end: function () {
                layer.closeAll('loading')
            }
        });

    }

    // 购买股票商品服务器验证
    function prod_buy_server_veriflcation(prodId, num) {
        var url = "front/stockProdBuyCheck";
        $.post(url, {"SHARES_PROD_ID": prodId, "amount": num}, function (result) {
            layer.closeAll('loading');
            mui.toast(result.message);
            if (result.success) {
                setTimeout(function () {
                    window.location.reload()
                }, 1200)
            }
        })

    }


</script>

</html>
