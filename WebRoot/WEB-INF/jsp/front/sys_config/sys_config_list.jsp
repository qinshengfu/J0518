<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <base href="<%=basePath%>">
    <!-- 下拉框 -->
    <link rel="stylesheet" href="static/ace/css/chosen.css"/>
    <!-- jsp文件头和头部 -->
    <%@ include file="../../system/index/top.jsp" %>
    <%--layui--%>
    <link rel="stylesheet" href="static/front/layui/css/layui.css"/>
    <script src="static/front/layui/layui.all.js"></script>
</head>

<body class="no-skin">

<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
    <!-- /section:basics/sidebar -->
    <div class="main-content">
        <div class="main-content-inner">
            <div class="page-content">
                <div class="row">
                    <div class="col-xs-12">

                        <!-- 检索  -->
                        <form action="sys_config/list.do" method="post" name="Form" id="Form">
                            <c:if test="${QX.cha == 1 }">
                                <table class="table table-striped table-bordered table-hover">

                                    <th colspan="99">基础设置：</th>

                                    <tr>
                                        <th class='center'>充值开关：</th>
                                        <td>
                                            <select name="RECHARGE_SWITCH">
                                                <c:if test="${pd.RECHARGE_SWITCH == ''}">
                                                    <option disabled selected hidden style="color: #715d3e;">请选择状态
                                                    </option>
                                                </c:if>
                                                <c:if test="${pd.RECHARGE_SWITCH == 1}">
                                                    <option value="1" disabled selected hidden>开启</option>
                                                </c:if>
                                                <c:if test="${pd.RECHARGE_SWITCH == 0}">
                                                    <option value="0" disabled selected hidden>关闭</option>
                                                </c:if>
                                                <option value="1">开启</option>
                                                <option value="0">关闭</option>
                                            </select>
                                        </td>
                                        <th class='center'>提现开关：</th>
                                        <td>
                                            <select name="WITHDRAWAL_SWITCH">
                                                <c:if test="${pd.WITHDRAWAL_SWITCH == ''}">
                                                    <option disabled selected hidden style="color: #715d3e;">请选择状态
                                                    </option>
                                                </c:if>
                                                <c:if test="${pd.WITHDRAWAL_SWITCH == 1}">
                                                    <option value="1" disabled selected hidden>开启</option>
                                                </c:if>
                                                <c:if test="${pd.WITHDRAWAL_SWITCH == 0}">
                                                    <option value="0" disabled selected hidden>关闭</option>
                                                </c:if>
                                                <option value="1">开启</option>
                                                <option value="0">关闭</option>
                                            </select>
                                        </td>
                                        <th class='center'>登录开关：</th>
                                        <td>
                                            <select name="LOGIN_SWITCH">
                                                <c:if test="${pd.LOGIN_SWITCH == ''}">
                                                    <option disabled selected hidden style="color: #715d3e;">请选择状态
                                                    </option>
                                                </c:if>
                                                <c:if test="${pd.LOGIN_SWITCH == 1}">
                                                    <option value="1" disabled selected hidden>开启</option>
                                                </c:if>
                                                <c:if test="${pd.LOGIN_SWITCH == 0}">
                                                    <option value="0" disabled selected hidden>关闭</option>
                                                </c:if>
                                                <option value="1">开启</option>
                                                <option value="0">关闭</option>
                                            </select>
                                        </td>
                                        <th class='center'>注册开关：</th>
                                        <td>
                                            <select name="REGISTER_SWITCH">
                                                <c:if test="${pd.REGISTER_SWITCH == ''}">
                                                    <option disabled selected hidden style="color: #715d3e;">请选择状态
                                                    </option>
                                                </c:if>
                                                <c:if test="${pd.REGISTER_SWITCH == 1}">
                                                    <option value="1" disabled selected hidden>开启</option>
                                                </c:if>
                                                <c:if test="${pd.REGISTER_SWITCH == 0}">
                                                    <option value="0" disabled selected hidden>关闭</option>
                                                </c:if>
                                                <option value="1">开启</option>
                                                <option value="0">关闭</option>
                                            </select>
                                        </td>
                                        <th class='center'>交易开关：</th>
                                        <td>
                                            <select name="TRADE_SWITCH">
                                                <c:if test="${pd.TRADE_SWITCH == ''}">
                                                    <option disabled selected hidden style="color: #715d3e;">请选择状态
                                                    </option>
                                                </c:if>
                                                <c:if test="${pd.TRADE_SWITCH == 1}">
                                                    <option value="1" disabled selected hidden>开启</option>
                                                </c:if>
                                                <c:if test="${pd.TRADE_SWITCH == 0}">
                                                    <option value="0" disabled selected hidden>关闭</option>
                                                </c:if>
                                                <option value="1">开启</option>
                                                <option value="0">关闭</option>
                                            </select>
                                        </td>
                                        <th class='center'>转账开关：</th>
                                        <td>
                                            <select name="TRANSFER_SWITCH">
                                                <c:if test="${pd.TRANSFER_SWITCH == ''}">
                                                    <option disabled selected hidden style="color: #715d3e;">请选择状态
                                                    </option>
                                                </c:if>
                                                <c:if test="${pd.TRANSFER_SWITCH == 1}">
                                                    <option value="1" disabled selected hidden>开启</option>
                                                </c:if>
                                                <c:if test="${pd.TRANSFER_SWITCH == 0}">
                                                    <option value="0" disabled selected hidden>关闭</option>
                                                </c:if>
                                                <option value="1">开启</option>
                                                <option value="0">关闭</option>
                                            </select>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td colspan="99">USDT钱包收款地址：
                                            <input class="inspect" type="text" name="RECEIVING_ADDRESS"
                                                   placeholder="请输入USDT钱包地址"
                                                   id="RECEIVING_ADDRESS"
                                                   value="${pd.RECEIVING_ADDRESS}"
                                                   style="width: 66%;"/>

                                        </td>
                                    </tr>
                                </table>

                                <%-- <table>
                                     <th class='center'>提现时间段：</th>
                                     <td><input class="inspect" class="forminput" type="text" name="CASH_TIME" id="CASH_TIME"
                                                value="${pd.CASH_TIME}"
                                                style="width:98%;"/></td>
                                 </table>
                                 <br>--%>

                                <table class="table table-striped table-bordered table-hover">
                                    <tr>
                                        <th colspan="9"> 客服设置：</th>
                                    </tr>
                                    <tr>
                                        <th class='center'>客服QQ</th>
                                        <td><input class="inspect" type="text" name="QQ"
                                                   id="QQ"
                                                   value="${pd.QQ}" maxlength="32"
                                                   placeholder="请输入客服QQ" style="width:98%;"/>
                                        </td>
                                        <th class='center'>客服微信</th>
                                        <td><input class="inspect" type="text" name="WECHAT"
                                                   id="WECHAT"
                                                   value="${pd.WECHAT}" maxlength="32"
                                                   placeholder="请输入客服微信" style="width:98%;"/>
                                        </td>
                                    </tr>
                                </table>

                                <table class="table table-striped table-bordered table-hover">
                                    <tr>
                                        <th colspan="9"> 板块基础设置：</th>
                                    </tr>
                                    <tr>
                                        <th class='center'>每周交易时间</th>
                                        <td><input class="inspect" type="text" name="WEEKLY_TRADING_HOURS"
                                                   id="WEEKLY_TRADING_HOURS"
                                                   value="${pd.WEEKLY_TRADING_HOURS}" hidden/>
                                            <br>
                                            <%-- 周范围滑块 --%>
                                            <div id="weekRange" class="demo-slider"></div>
                                        </td>
                                        <th class='center'>上午交易时间</th>
                                        <td><input class="inspect timeItem" type="text" name="MORNING_TRADING_HOURS"
                                                   id="MORNING_TRADING_HOURS"
                                                   value="${pd.MORNING_TRADING_HOURS}" maxlength="32"
                                                   placeholder="请输入上午交易时间" style="width:98%;"/>
                                        </td>
                                        <th class='center'>下午交易时间</th>
                                        <td><input class="inspect timeItem" type="text" name="AFTERNOON_TRADING_TIME"
                                                   id="AFTERNOON_TRADING_TIME"
                                                   value="${pd.AFTERNOON_TRADING_TIME}" maxlength="32"
                                                   placeholder="请输入下午交易时间" style="width:98%;"/>
                                        </td>
                                    </tr>

                                    <tr>
                                        <th class='center'>价格变动间隔单位</th>
                                        <td><input class="inspect" type="number" name="PRICE_CHANGE_INTERVAL"
                                                   id="PRICE_CHANGE_INTERVAL"
                                                   value="${pd.PRICE_CHANGE_INTERVAL}" maxlength="32"
                                                   placeholder="请输入时间" style="width:88%;"/> 秒
                                        </td>
                                        <th class='center'>交易平台手续费</th>
                                        <td><input class="inspect" type="number" name="TRANSACTION_FEE"
                                                   id="TRANSACTION_FEE"
                                                   value="${pd.TRANSACTION_FEE}" maxlength="32"
                                                   placeholder="请输入交易手续费" style="width:88%;"/>%
                                        </td>
                                        <th class='center'>T+N天数规则</th>
                                        <td><input class="inspect" type="number" name="LOCKED_DAY"
                                                   id="LOCKED_DAY"
                                                   value="${pd.LOCKED_DAY}" maxlength="32"
                                                   placeholder="请输入天数" style="width:88%;"/>天
                                        </td>
                                    </tr>
                                </table>

                                <table class="table table-striped table-bordered table-hover">
                                    <tr>
                                        <th colspan="9"> 收益相关设置：</th>
                                    </tr>
                                    <tr>
                                        <th class='center'>普通会员可拿一代的交易手续费</th>
                                        <td><input class="inspect" type="number" name="GENERAL_MEMBERS"
                                                   id="GENERAL_MEMBERS"
                                                   value="${pd.GENERAL_MEMBERS}" maxlength="32"
                                                   placeholder="请输入收益" style="width:88%;"/> %
                                        </td>
                                        <th class='center'>市级代理拿整个团队伞的交易手续费</th>
                                        <td><input class="inspect" type="number" name="MUNICIPAL_AGENT"
                                                   id="MUNICIPAL_AGENT"
                                                   value="${pd.MUNICIPAL_AGENT}" maxlength="32"
                                                   placeholder="请输入收益" style="width:88%;"/> %
                                        </td>
                                        <th class='center'>省级代理拿整个团队伞的交易手续费</th>
                                        <td><input class="inspect" type="number" name="PROVINCIAL_AGENT"
                                                   id="PROVINCIAL_AGENT"
                                                   value="${pd.PROVINCIAL_AGENT}" maxlength="32"
                                                   placeholder="请输入收益" style="width:88%;"/> %
                                        </td>
                                    </tr>

                                    <tr>
                                        <th class='center'>金额转USDT比例</th>
                                        <td><input class="inspect" type="text" name="MONEY_TO_USDT"
                                                   id="MONEY_TO_USDT"
                                                   value="${pd.MONEY_TO_USDT}" maxlength="32"
                                                   placeholder="请输入金额转USDT比例" style="width:88%;"/>
                                        </td>
                                        <th class='center'>USDT转金额比例</th>
                                        <td><input class="inspect" type="text" name="USDT_TO_MONEY"
                                                      id="USDT_TO_MONEY"
                                                      value="${pd.USDT_TO_MONEY}" maxlength="32"
                                                      placeholder="请输入USDT转金额比例" style="width:88%;"/>
                                        </td>
                                        <th class='center'>商城积分比例</th>
                                        <td><input class="inspect" type="number" name="MALL_POINTS" id="MALL_POINTS"
                                                   value="${pd.MALL_POINTS}" maxlength="32"
                                                   placeholder="请输入商城积分比例" style="width:88%;"/>%
                                        </td>
                                    </tr>
                                </table>

                                <table style="table-layout:fixed"
                                       class="table table-striped table-bordered table-hover">
                                    <tr>
                                        <th colspan="9"> 充值/提现/转账设置：</th>
                                    </tr>
                                    <tr>
                                        <th class='center'>充值范围：</th>
                                        <td><input class="inspect center" type="number" name="MIN_RECHARGE"
                                                   id="MIN_RECHARGE"
                                                   value="${pd.MIN_RECHARGE}" maxlength="32"
                                                   placeholder="最少值" style="width:40%;"/>
                                            ~
                                            <input class="inspect center" type="number" name="MAX_RECHARGE"
                                                   id="MAX_RECHARGE"
                                                   value="${pd.MAX_RECHARGE}" maxlength="32"
                                                   placeholder="最大值" style="width:40%;"/>
                                        </td>
                                        <th class='center'>充值倍数：</th>
                                        <td><input class="inspect" type="number" name="RECHARGE_MULTIPLE"
                                                   id="RECHARGE_MULTIPLE"
                                                   value="${pd.RECHARGE_MULTIPLE}" maxlength="32"
                                                   placeholder="请输入倍数" style="width:98%;"/></td>
                                    </tr>

                                    <tr>
                                        <th class='center'>提现范围：</th>
                                        <td><input class="inspect center" type="number" name="MIN_CASH"
                                                   id="MIN_CASH"
                                                   value="${pd.MIN_CASH}" maxlength="32"
                                                   placeholder="最少值" style="width:40%;"/>
                                            ~
                                            <input class="inspect center" type="number" name="MAX_CASH"
                                                   id="MAX_CAHS"
                                                   value="${pd.MAX_CASH}" maxlength="32"
                                                   placeholder="最大值" style="width:40%;"/>
                                        </td>
                                        <th class='center'>提现倍数：</th>
                                        <td><input class="inspect" type="number" name="CASH_MULTIPLE"
                                                   id="CASH_MULTIPLE"
                                                   value="${pd.CASH_MULTIPLE}" maxlength="32"
                                                   placeholder="请输入倍数" style="width:98%;"/></td>
                                        <th class='center'>提现手续费：</th>
                                        <td><input class="inspect" type="number" name="CASH_CHARGE"
                                                   id="CASH_CHARGE"
                                                   value="${pd.CASH_CHARGE}" maxlength="32"
                                                   placeholder="请输入提现手续费" style="width:98%;"/>
                                        </td>
                                    </tr>

                                    <tr>
                                        <th class='center'>转账范围：</th>
                                        <td><input class="inspect center" type="number" name="MIN_TRANSFER"
                                                   id="MIN_TRANSFER"
                                                   value="${pd.MIN_TRANSFER}" maxlength="32"
                                                   placeholder="最少值" style="width:40%;"/>
                                            ~
                                            <input class="inspect center" type="number" name="MAX_TRANSFER"
                                                   id="MAX_TRANSFER"
                                                   value="${pd.MAX_TRANSFER}" maxlength="32"
                                                   placeholder="最大值" style="width:40%;"/>
                                        </td>
                                        <th class='center'>转账倍数：</th>
                                        <td><input class="inspect" type="number" name="TRANSFER_MULTIPLE"
                                                   id="TRANSFER_MULTIPLE"
                                                   value="${pd.TRANSFER_MULTIPLE}" maxlength="32"
                                                   placeholder="请输入倍数" style="width:98%;"/></td>
                                    </tr>
                                </table>

                                <table class="table table-striped table-bordered table-hover">
                                    <tr>
                                        <th colspan="9"> 其他设置：</th>
                                    </tr>
                                    <tr>
                                        <th class='center'>代理说明</th>
                                        <td>
                                        <textarea hidden name="AGENT_DESCRIPTION"
                                                  id="AGENT_DESCRIPTION">${pd.AGENT_DESCRIPTION}</textarea>
                                            <!-- 加载编辑器的容器 -->
                                            <script id="editor" type="text/plain"
                                                    style="width:98%;height:260px;"></script>
                                        </td>
                                    </tr>

                                </table>

                                <div class="page-header position-relative">
                                    <table style="width:100%;">
                                        <tr>
                                            <td style="vertical-align:top;" class="center" colspan="6">
                                                <c:if test="${QX.edit == 1 }">
                                                    <a class="btn btn-mini btn-primary" onclick="edit();">保存</a>
                                                    <a class="btn btn-mini btn-success" onclick="formReset()">取消</a>
                                                    <c:if test="${QX.del == 1 }">
                                                        <a class="btn btn-mini btn-danger"
                                                           onclick="wipeData();">清空数据</a>
                                                    </c:if>
                                                </c:if>

                                            </td>
                                        </tr>
                                    </table>
                                </div>

                            </c:if>

                            <c:if test="${QX.cha == 0 }">
                                <table>
                                    <tr>
                                        <td colspan="100" class="center">您无权查看</td>
                                    </tr>
                                </table>
                            </c:if>

                        </form>

                    </div>
                    <!-- /.col -->
                </div>
                <!-- /.row -->
            </div>
            <!-- /.page-content -->
        </div>
    </div>
    <!-- /.main-content -->

    <!-- 返回顶部 -->
    <a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
        <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
    </a>

</div>
<!-- /.main-container -->

<!-- basic scripts -->
<!-- 页面底部js¨ -->
<%@ include file="../../system/index/foot.jsp" %>
<!-- 删除时确认窗口 -->
<script src="static/ace/js/bootbox.js"></script>
<!-- ace scripts -->
<script src="static/ace/js/ace/ace.js"></script>
<!-- 下拉框 -->
<script src="static/ace/js/chosen.jquery.js"></script>
<!-- 日期框 -->
<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
<!--提示框-->
<script type="text/javascript" src="static/js/jquery.tips.js"></script>
<!-- 编辑器配置文件 -->
<script type="text/javascript" charset="utf-8" src="plugins/ueditor/ueditor.config.js"></script>
<!-- 编辑器源码文件 -->
<script type="text/javascript" charset="utf-8" src="plugins/ueditor/ueditor.all.js"></script>

<!-- 实例化编辑器 -->
<script type="text/javascript">
    var ue = UE.getEditor('editor');

    var content = $("#AGENT_DESCRIPTION").text();
    // 初始化内容
    ue.ready(function () {
        ue.setContent(content);
    })

</script>

<script type="text/javascript">
    $(top.hangge());//关闭加载状态
    //检索
    function tosearch() {
        top.jzts();
        $("#Form").submit();
    }

    layui.use('slider', function () {
        var slider = layui.slider;

        //开启范围选择
        slider.render({
            elem: '#weekRange'
            , min: 1
            , max: 7
            // 设置初始值
            , value: [${pd.week[0]}, ${pd.week[1]}]
            , range: true //范围选择
            , change: function (vals) {
                // 写入input里面
                $('#WEEKLY_TRADING_HOURS').val(vals[0] + '-' + vals[1]);
            }
            , setTips: function (value) { //自定义提示文本
                return promptText(value);
            }
        });

    })

    // 传入1~7 返回对应的星期
    function promptText(num) {
        if (num === 1) {
            return "星期天";
        }
        if (num === 2) {
            return "星期一";
        }
        if (num === 3) {
            return "星期二";
        }
        if (num === 4) {
            return "星期三";
        }
        if (num === 5) {
            return "星期四";
        }
        if (num === 6) {
            return "星期五";
        }
        if (num === 7) {
            return "星期六";
        }
        // 都不等于返回 未定义
        return "参数未定义";
    }

    layui.use('laydate', function () {
        // 执行一个laydate实例
        var laydate = layui.laydate;

        // 时间范围
        // 同时绑定多个
        lay('.timeItem').each(function () {
            laydate.render({
                elem: this
                , type: 'time'
                , range: true
                , trigger: 'click'
            });
        });


        //日期时间
        laydate.render({
            elem: '#TASK_TIME'
            , type: 'time'
        });
    });

    //清空数据
    function wipeData() {
        bootbox.confirm("确定要清空数据吗?", function (r) {
            if (r) {
                top.jzts();
                var url = "sys_config/wipeAllData.do";
                $.get(url, function (data) {
                    alert(data.message);
                    location.reload(); //刷新页面
                });
            }
        });
    }

    //复位
    function formReset() {
        document.getElementById("Form").reset();
    }

    //判断不能为空
    function check() {  //Form是表单的ID
        for (var i = 0; i < document.Form.getElementsByClassName("inspect").length - 1; i++) {
            var r = document.getElementsByClassName("inspect")[i].value.trim();
            if ('' == r) {
                $(document.getElementsByClassName("inspect")[i]).tips({
                    side: 1,
                    msg: '不能为空',
                    bg: '#AE81FF',
                    time: 2
                });
                document.getElementsByClassName("inspect")[i].focus();
                return false;
            }
        }

        return true;
    }

    // 只能输入 0 或者 1
    function isNum(num) {
        //RegExp 对象表示正则表达式，它是对字符串执行模式匹配的强大工具。
        return (new RegExp(/^[01]$/).test(num));
    }

    // 获取from表单数据并传到后台
    function edit() {
        layer.load(1);
        // 获取富文本的内容设置到 多行长文本里面
        var contentHtml = ue.getContent();
        $("#AGENT_DESCRIPTION").text(contentHtml);

        // 取表单值
        var finalRes = $("#Form").serializeArray().reduce(function (r, item) {
            r[item.name] = item.value;
            return r;
        }, {});

        // 通过ajax传到后台
        if (check()) {
            $.ajax({
                url: "sys_config/edit.do",
                type: "post",
                data: finalRes,
                timeout: 10000, //超时时间设置为10秒
                success: function (data) { //回调函数
                    layer.closeAll('loading');
                    if (data === "success") {
                        alert("参数更改成功~");
                        location.reload(); //刷新页面
                    } else {
                        alert("参数更改失败~");
                        location.reload(); //刷新页面
                    }
                }
            });
        }
    }

</script>


</body>
</html>