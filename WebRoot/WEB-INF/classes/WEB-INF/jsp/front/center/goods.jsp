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
    <h1 class="mui-title">我要提货</h1>
</header>
<div class="mui-content">
    <!--  -->
    <div class="quota_top">
        <table border="0" cellspacing="" cellpadding="">
            <tr>
                <th>商品目录</th>
                <th>数量</th>
                <th>当前价值</th>
            </tr>
            <tbody id="dataList">
            <%--<tr onclick="takeGoods('1','长白山林芝')">
                <td>测试</td>
                <td>100</td>
                <td>￥200.00</td>
            </tr>--%>
            </tbody>
        </table>
    </div>
</div>

<!-- 输入弹窗 -->
<div class="tc_bg">
    <div class="tc">
        <h4>长白山林芝</h4>
        <div class="setup_inp">
            <div class="mui-input-row">
                <label>提货数量：</label>
                <input type="text" placeholder="提货数量">
            </div>
            <div class="mui-input-row">
                <label>收货地址：</label>
                <input type="text" placeholder="收货地址">
            </div>
        </div>
        <button type="button" class="mui-btn mui-btn-blue mui-btn-block mui_btn">确认提货</button>
    </div>
</div>


</body>

<script type="text/javascript">

    $(function () {
        getAllTakeGoodsInfo()
    })

    // 获取用户持有的股票商品
    function getAllTakeGoodsInfo() {
        var url = "front/listAllTakeGoods";
        $.get(url, function (result) {
            if (result.success) {
                setTakeGoodsList(result.data.item)
            }
        })
    }

    // 渲染所有大于0的提货商品
    function setTakeGoodsList(data) {
        var tdobyDom = $('#dataList');
        tdobyDom.html("");
        console.log(data)

        var str = '';
        for (let i = 0; i < data.length; i++) {
            var pd = data[i];
            if (pd.REMAINDER < 1) {
                continue;
            }

            str += '<tr onclick="takeGoods(\'' + pd.SHARES_ORDER_ID + '\',\'' + pd.PROD_NAME + '\',\'' + pd.REMAINDER + '\')">\n' +
                '       <td>' + pd.PROD_NAME + '</td>\n' +
                '       <td>' + pd.REMAINDER + '</td>\n' +
                '       <td>￥' + pd.PRICE + '</td>\n' +
                '   </tr>';
        }
        tdobyDom.append(str)
    }

    // 提货
    function takeGoods(id, name, num) {
        layer.load();
        var content = '<div class="">\n' +
            '               <label>提货数量：</label>\n' +
            '               <input type="text" id="amount" onblur="numCheck(this,' + num + ')" value="' + num + '" placeholder="提货数量">\n' +
            '          </div>';

        layer.open({
            title: [
                name,
                'background-color:#207eff; color:#fff;'
            ]
            , anim: 'up'
            , content: content
            , btn: ['确认', '取消']
            , yes: function (index) {
                var amount = $("#amount").val();
                layer.close(index)
                layer.closeAll('loading');
                toConfirmOrder(id, amount)
            }
            , btn2: function () {
                layer.closeAll('loading')
            }
        });
    }

    // 访问确认订单页面
    function toConfirmOrder(id, num) {
        window.location.href = "front/toConfirmOrder?SHARES_ORDER_ID=" + id +"&num=" + num;
    }

    // 输入范围检查
    function numCheck(element, max) {
        var num = element.value;

        if (num <= 0) {
            element.value = 1;
        } else if (num > max) {
            element.value = max;
        }
    }


</script>

</html>
