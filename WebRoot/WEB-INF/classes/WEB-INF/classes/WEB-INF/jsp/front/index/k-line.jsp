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
    <h1 class="mui-title">K线图</h1>
</header>
<div class="mui-content">
    <!-- 头部 -->
    <ul class="k_top" id="stockInfo">
        <li>
            <div>商品</div>
            <span>油票板块</span>
        </li>
        <li>
            <div>开盘价</div>
            <span style="color: #73EE16;">5147.00</span>
        </li>
        <li>
            <div>最低价</div>
            <span style="color: white;">4965.00</span>
        </li>
        <li>
            <div>最高价</div>
            <span style="color: #2D03C8;">5113.00</span>
        </li>
    </ul>
    <%--    <!-- 时间选择 -->
        <div class="k_time">
            <div><input type="radio" name="radio" id="" value="" checked="checked"/>分时线</div>
            <div><input type="radio" name="radio" id="" value=""/>5分钟</div>
            <div><input type="radio" name="radio" id="" value=""/>10分钟</div>
            <div><input type="radio" name="radio" id="" value=""/>30分钟</div>
            <div><input type="radio" name="radio" id="" value=""/>时刻线</div>
        </div>--%>
    <!-- K线图 -->
    <div id="main" style="width: 100%;height:400px;"></div>

    <%--<!-- 底部 -->
    <div class="k_bot">
        <div class="zhang">买涨</div>
        <div>155545421</div>
        <div class="die">买跌</div>
    </div>--%>

    <!-- 买涨弹窗 -->
    <div class="tc_bg maizhang">
        <div class="tc" style="width: 90%;">
            <h4>订单确认<i class="iconfont icon-guanbi"></i></h4>
            <div class="k_order">
                <div>投资金额</div>
                <ul>
                    <li>
                        <div class="k_order_active">100</div>
                        <div>1000</div>
                        <div>5000</div>
                        <div>10000</div>
                        <div><input type="text" value="1">手</div>
                    </li>
                </ul>
                <table border="0" cellspacing="" cellpadding="">
                    <tr>
                        <th>名称</th>
                        <th>盈亏值</th>
                        <th>方向</th>
                        <th>现价</th>
                        <th>金额</th>
                    </tr>
                    <tr>
                        <td>澳洲红酒</td>
                        <td>4</td>
                        <td style="color: red;">看涨</td>
                        <td>258698.00</td>
                        <td>100</td>
                    </tr>
                </table>
                <button type="button" class="mui-btn mui-btn-blue mui-btn-block mui_btn">确定</button>
            </div>
        </div>
    </div>
    <!-- 买跌弹窗 -->
    <div class="tc_bg maidie">
        <div class="tc" style="width: 90%;">
            <h4>订单确认<i class="iconfont icon-guanbi"></i></h4>
            <div class="k_order">
                <div>投资金额</div>
                <ul>
                    <li>
                        <div class="k_order_active">100</div>
                        <div>1000</div>
                        <div>5000</div>
                        <div>10000</div>
                        <div><input type="text" value="1">手</div>
                    </li>
                </ul>
                <table border="0" cellspacing="" cellpadding="">
                    <tr>
                        <th>名称</th>
                        <th>盈亏值</th>
                        <th>方向</th>
                        <th>现价</th>
                        <th>金额</th>
                    </tr>
                    <tr>
                        <td>澳洲红酒</td>
                        <td>4</td>
                        <td style="color: green;">看跌</td>
                        <td>258698.00</td>
                        <td>100</td>
                    </tr>
                </table>
                <button type="button" class="mui-btn mui-btn-blue mui-btn-block mui_btn">确定</button>
            </div>
        </div>
    </div>
</div>


</body>

<script type="text/javascript">
    $('.k_order li div').click(function () {
        $('.k_order li div').removeClass('k_order_active')
        $(this).addClass('k_order_active')
    })
    $('.zhang').click(function () {
        $('.maizhang').fadeIn()
    })
    $('.die').click(function () {
        $('.maidie').fadeIn()
    })
    $('.icon-guanbi').click(function () {
        $('.tc_bg').fadeOut()
    })
</script>

<%--渲染K线图--%>
<script type="text/javascript">

    $(function () {
        getData()
    });

    var times = [];
    var datas = [];
    var title = "";

    function getData() {
        var url = "front/getShareInfo?SHARES_PROD_ID=${pd.id}";

        $.get(url, function (result) {
            title = result.data.item[0].PROD_NAME;
            jsonToArray(result.data.item)
            setTopHtml(result.data.item[result.data.item.length - 1])
        })
    }

    // json转数组
    function jsonToArray(data) {
        for (let i = 0; i < data.length; i++) {
            var pd = data[i];
            times.push(pd.GMT_CREATE.substring(0, 10));
            var tempArray = [];
            tempArray.push(pd.OPEN_PRICE);
            tempArray.push(pd.CLOSING_PRICE);
            tempArray.push(pd.MAX_PRICE);
            tempArray.push(pd.MIN_PRICE);
            datas.push(tempArray);
        }
        setChrat()
    }

    // 渲染顶部信息
    function setTopHtml(pd) {
        var ulDom = $('#stockInfo');
        ulDom.html("");

        var str = '<li>\n' +
            '          <div>商品</div>\n' +
            '          <span>' + pd.PROD_NAME + '</span>\n' +
            '      </li>\n' +
            '      <li>\n' +
            '          <div>开盘价</div>\n' +
            '          <span style="color: #73EE16;">' + pd.OPEN_PRICE + '</span>\n' +
            '      </li>\n' +
            '      <li>\n' +
            '          <div>最低价</div>\n' +
            '          <span style="color: white;">' + pd.MIN_PRICE + '</span>\n' +
            '      </li>\n' +
            '      <li>\n' +
            '          <div>最高价</div>\n' +
            '          <span style="color: #2D03C8;">' + pd.MAX_PRICE + '</span>\n' +
            '      </li>';
        ulDom.append(str);
    }

    // 后台推送过来的最新价
    function setStockList(data) {
        var stockId = '${pd.id}';
        for (let i = 0; i < data.length; i++) {
            var pd = data[i];
            if (stockId.indexOf(pd.SHARES_PROD_ID) == -1) {
                continue;
            }
            setTopHtml(pd);
        }
    }

    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('main'));

    // 渲染K线图
    function setChrat () {
        // 颜色列表
        var colorList = ['#c23531', '#2f4554', '#61a0a8', '#d48265', '#91c7ae', '#749f83', '#ca8622', '#bda29a', '#6e7074',
            '#546570', '#c4ccd3'
        ];
        var labelFont = 'bold 12px Sans-serif';

        function calculateMA(dayCount, data) {
            var result = [];
            for (var i = 0, len = data.length; i < len; i++) {
                if (i < dayCount) {
                    result.push('-');
                    continue;
                }
                var sum = 0;
                for (var j = 0; j < dayCount; j++) {
                    sum += data[i - j][1];
                }
                result.push((sum / dayCount).toFixed(2));
            }
            return result;
        }

        /*
            var volumes = [86160000, 79330000, 102600000, 104890000, 85230000, 115230000, 99410000, 90120000, 79990000,
                107100000, 81020000, 91710000, 84510000, 118160000, 89390000, 89820000, 100210000, 102720000, 134120000, 83770000,
                92570000, 109090000, 100920000, 136670000, 80100000, 97060000, 95020000, 81530000, 80020000, 85590000, 75790000,
                87390000, 88560000, 86640000, 88440000, 103260000, 79120000, 95530000, 111990000, 87790000, 86480000, 79180000,
                68940000, 73190000, 147390000, 78530000, 75560000, 82270000, 71870000, 78750000, 71260000, 69690000, 90540000,
                101690000, 93740000, 94130000, 91950000, 248680000, 99380000, 85130000, 89440000
            ];
        */

        var dataMA5 = calculateMA(5, datas);
        var dataMA10 = calculateMA(10, datas);
        var dataMA20 = calculateMA(20, datas);



        option = {
            animation: false,
            color: colorList,
            title: {
                left: 'center',
                text: title
            },
            legend: {
                top: 30,
                data: ['日K', 'MA5', 'MA10', 'MA20', 'MA30']
            },
            tooltip: {
                triggerOn: 'none',
                transitionDuration: 0,
                confine: true,
                bordeRadius: 4,
                borderWidth: 1,
                borderColor: '#333',
                backgroundColor: 'rgba(255,255,255,0.9)',
                textStyle: {
                    fontSize: 12,
                    color: '#333'
                },
                position: function (pos, params, el, elRect, size) {
                    var obj = {
                        top: 60
                    };
                    obj[['left', 'right'][+(pos[0] < size.viewSize[0] / 2)]] = 5;
                    return obj;
                }
            },
            axisPointer: {
                link: [{
                    xAxisIndex: [0, 1]
                }]
            },
            dataZoom: [{
                type: 'slider',
                xAxisIndex: [0, 1],
                realtime: false,
                start: 20,
                end: 70,
                top: 65,
                height: 20,
                handleIcon: 'M10.7,11.9H9.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4h1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
                handleSize: '120%'
            }, {
                type: 'inside',
                xAxisIndex: [0, 1],
                start: 40,
                end: 70,
                top: 30,
                height: 20
            }],
            xAxis: [{
                type: 'category',
                data: times,
                boundaryGap: false,
                axisLine: {
                    lineStyle: {
                        color: '#777'
                    }
                },
                axisLabel: {
                    formatter: function (value) {
                        return echarts.format.formatTime('MM-dd', value);
                    }
                },
                min: 'dataMin',
                max: 'dataMax',
                axisPointer: {
                    show: true
                }
            }, {
                type: 'category',
                gridIndex: 1,
                data: times,
                scale: true,
                boundaryGap: false,
                splitLine: {
                    show: false
                },
                axisLabel: {
                    show: false
                },
                axisTick: {
                    show: false
                },
                axisLine: {
                    lineStyle: {
                        color: '#777'
                    }
                },
                splitNumber: 20,
                min: 'dataMin',
                max: 'dataMax',
                axisPointer: {
                    type: 'shadow',
                    label: {
                        show: false
                    },
                    triggerTooltip: true,
                    handle: {
                        show: true,
                        margin: 30,
                        color: '#B80C00'
                    }
                }
            }],
            yAxis: [{
                scale: true,
                splitNumber: 2,
                axisLine: {
                    lineStyle: {
                        color: '#777'
                    }
                },
                splitLine: {
                    show: true
                },
                axisTick: {
                    show: false
                },
                axisLabel: {
                    inside: true,
                    formatter: '{value}\n'
                }
            }, {
                scale: true,
                gridIndex: 1,
                splitNumber: 2,
                axisLabel: {
                    show: false
                },
                axisLine: {
                    show: false
                },
                axisTick: {
                    show: false
                },
                splitLine: {
                    show: false
                }
            }],
            grid: [{
                left: 20,
                right: 20,
                top: 110,
                height: 120
            }, {
                left: 20,
                right: 20,
                height: 40,
                top: 260
            }],
            graphic: [{
                type: 'group',
                left: 'center',
                top: 70,
                width: 300,
                bounding: 'raw',
                children: [{
                    id: 'MA5',
                    type: 'text',
                    style: {
                        fill: colorList[1],
                        font: labelFont
                    },
                    left: 0
                }, {
                    id: 'MA10',
                    type: 'text',
                    style: {
                        fill: colorList[2],
                        font: labelFont
                    },
                    left: 'center'
                }, {
                    id: 'MA20',
                    type: 'text',
                    style: {
                        fill: colorList[3],
                        font: labelFont
                    },
                    right: 0
                }]
            }],
            series: [/*{
            name: 'Volume',
            type: 'bar',
            xAxisIndex: 1,
            yAxisIndex: 1,
            itemStyle: {
                color: '#7fbe9e'
            },
            emphasis: {
                itemStyle: {
                    color: '#140'
                }
            },
            data: volumes
        },*/ {
                type: 'candlestick',
                name: '日K',
                data: datas,
                itemStyle: {
                    color: '#ef232a',
                    color0: '#14b143',
                    borderColor: '#ef232a',
                    borderColor0: '#14b143'
                },
                emphasis: {
                    itemStyle: {
                        color: 'black',
                        color0: '#444',
                        borderColor: 'black',
                        borderColor0: '#444'
                    }
                }
            }, {
                name: 'MA5',
                type: 'line',
                data: dataMA5,
                smooth: true,
                showSymbol: false,
                lineStyle: {
                    width: 1
                }
            }, {
                name: 'MA10',
                type: 'line',
                data: dataMA10,
                smooth: true,
                showSymbol: false,
                lineStyle: {
                    width: 1
                }
            }, {
                name: 'MA20',
                type: 'line',
                data: dataMA20,
                smooth: true,
                showSymbol: false,
                lineStyle: {
                    width: 1
                }
            }]
        };

        // 使用刚指定的配置项和数据显示图表
        myChart.setOption(option);
    }

</script>

</html>
