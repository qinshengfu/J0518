<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <base href="<%=basePath%>">
    <!-- 下拉框 -->
    <link rel="stylesheet" href="static/ace/css/chosen.css"/>
    <!-- jsp文件头和头部 -->
    <%@ include file="../../system/index/top.jsp" %>
    <!-- 日期框 -->
    <link rel="stylesheet" href="static/ace/css/datepicker.css"/>
</head>
<style>
    .file_pic {
        display: inline;
        vertical-align: top;

    }

    .add_img {
        display: flex;
        align-items: center;
        flex-wrap: wrap;

    }

    .add_img img {
        width: 100px;
        height: 100px;
        margin-bottom: 10px;
        margin-left: 10px;
    }

    .add_pic {
        position: relative;
        width: 100px;
        height: 100px;
        margin-bottom: 10px;
        margin-left: 10px;
    }

    .add_pic img {
        width: 100px;
        height: 100px;

    }

    .add_pic input {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        opacity: 0;
    }

</style>
<body class="no-skin">
<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
    <!-- /section:basics/sidebar -->
    <div class="main-content">
        <div class="main-content-inner">
            <div class="page-content">
                <div class="row">
                    <div class="col-xs-12">

                        <form action="prod/${msg }.do" name="Form" id="Form" method="post">
                            <input type="hidden" name="PROD_ID" id="PROD_ID" value="${pd.PROD_ID}"/>
                            <div id="zhongxin" style="padding-top: 13px;">
                                <table id="table_report" class="table table-striped table-bordered table-hover">
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">商品图片:</td>
                                        <td>
                                            <div class="file_pic">
                                                <div class="add_pic">
                                                    <img src="static/images/upload1.png" alt="">
                                                    <input type="file" onchange="upload(this, 'IMGS')">
                                                </div>
                                                <%-- 图片预览区 --%>
                                                <div class="add_img" id="IMGS">
                                                    <c:forTokens items="${pd.IMGS}" delims="," var="imgUrl">
                                                        <img src="${imgUrl}" alt="">
                                                    </c:forTokens>
                                                </div>
                                            </div>
                                            <%-- 商品轮播图列表 隐藏域 --%>
                                            <input type="text" HIDDEN name="IMGS" value="${pd.IMGS}"
                                                   placeholder="商品图片，以,分割" style="width:98%;"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">商品名称:</td>
                                        <td><input type="text" name="PROD_NAME" id="PROD_NAME" value="${pd.PROD_NAME}"
                                                   maxlength="55" placeholder="这里输入商品名称" title="商品名称"
                                                   style="width:98%;"/></td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">原价:</td>
                                        <td><input type="number" name="ORI_PRICE" id="ORI_PRICE" value="${pd.ORI_PRICE}"
                                                   maxlength="32" placeholder="这里输入原价" title="原价" style="width:98%;"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">现价:</td>
                                        <td><input type="number" name="PRICE" id="PRICE" value="${pd.PRICE}"
                                                   maxlength="32" placeholder="这里输入现价" title="现价" style="width:98%;"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">简要描述,卖点等:</td>
                                        <td><input type="text" name="BRIEF" id="BRIEF" value="${pd.BRIEF}"
                                                   maxlength="1000" placeholder="这里输入简要描述,卖点等" title="简要描述,卖点等"
                                                   style="width:98%;"/></td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">状态：</td>
                                        <td>
                                            <label>
                                                <input name="STATUS" type="radio" checked class="ace" value="1">
                                                <span class="lbl">上架</span>
                                            </label>
                                            <label>
                                                <input name="STATUS" type="radio" class="ace" value="0">
                                                <span class="lbl">下架</span>
                                            </label>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">销量:</td>
                                        <td><input type="number" name="SOLD_NUM" id="SOLD_NUM" value="${pd.SOLD_NUM}"
                                                   maxlength="32" placeholder="这里输入销量" title="销量" style="width:98%;"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">总库存:</td>
                                        <td><input type="number" name="TOTAL_STOCKS" id="TOTAL_STOCKS"
                                                   value="${pd.TOTAL_STOCKS}" maxlength="32" placeholder="这里输入总库存"
                                                   title="总库存" style="width:98%;"/></td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">商品详情:</td>
                                        <td>
                                            <textarea hidden name="CONTENT" id="CONTENT">${pd.CONTENT}</textarea>
                                            <!-- 加载编辑器的容器 -->
                                            <script id="editor" type="text/plain"
                                                    style="width:98%;height:260px;"></script>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td style="text-align: center;" colspan="10">
                                            <a class="btn btn-mini btn-primary" onclick="save();">保存</a>
                                            <a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img
                                    src="static/images/jiazai.gif"/><br/><h4 class="lighter block green">提交中...</h4>
                            </div>
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
</div>
<!-- /.main-container -->


<!-- 页面底部js¨ -->
<%@ include file="../../system/index/foot.jsp" %>
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

    var content = $("#CONTENT").text();
    // 初始化内容
    ue.ready(function () {
        ue.setContent(content);
    });

    // 当百度富文本编辑器失去焦点后 获取内容添加到长文本中
    ue.addListener("blur", function () {
        // 获取富文本的内容设置到 多行长文本里面
        var contentHtml = ue.getContent();
        console.log(contentHtml)
        $("#CONTENT").text(contentHtml);
    })

</script>

<script type="text/javascript">
    $(top.hangge());

    //保存
    function save() {
        console.log($("#IMGS img").length)
        if ($("#IMGS img").length < 1) {
            $("#IMGS").tips({
                side: 3,
                msg: '请上传商品轮播图',
                bg: '#AE81FF',
                time: 2
            });
            $("#IMGS").focus();
            return false;
        }
        if ($("#PROD_NAME").val() == "") {
            $("#PROD_NAME").tips({
                side: 3,
                msg: '请输入商品名称',
                bg: '#AE81FF',
                time: 2
            });
            $("#PROD_NAME").focus();
            return false;
        }
        if ($("#ORI_PRICE").val() == "") {
            $("#ORI_PRICE").tips({
                side: 3,
                msg: '请输入原价',
                bg: '#AE81FF',
                time: 2
            });
            $("#ORI_PRICE").focus();
            return false;
        }
        if ($("#PRICE").val() == "") {
            $("#PRICE").tips({
                side: 3,
                msg: '请输入现价',
                bg: '#AE81FF',
                time: 2
            });
            $("#PRICE").focus();
            return false;
        }
        if ($("#BRIEF").val() == "") {
            $("#BRIEF").tips({
                side: 3,
                msg: '请输入简要描述,卖点等',
                bg: '#AE81FF',
                time: 2
            });
            $("#BRIEF").focus();
            return false;
        }
        if ($("#SOLD_NUM").val() == "") {
            $("#SOLD_NUM").tips({
                side: 3,
                msg: '请输入销量',
                bg: '#AE81FF',
                time: 2
            });
            $("#SOLD_NUM").focus();
            return false;
        }
        if ($("#TOTAL_STOCKS").val() == "") {
            $("#TOTAL_STOCKS").tips({
                side: 3,
                msg: '请输入总库存',
                bg: '#AE81FF',
                time: 2
            });
            $("#TOTAL_STOCKS").focus();
            return false;
        }
        if ($("#CONTENT").val() == "") {
            $("#CONTENT").tips({
                side: 3,
                msg: '请输入详细描述',
                bg: '#AE81FF',
                time: 2
            });
            $("#CONTENT").focus();
            return false;
        }
        $("#Form").submit();
        $("#zhongxin").hide();
        $("#zhongxin2").show();
    }

    $(function () {
        //日期框
        $('.date-picker').datepicker({autoclose: true, todayHighlight: true});
    });
</script>
</body>

<%--图片压缩上传--%>
<script>
    //声明一个formdata 用来上传
    var UForm;
    // 定义图片原始大小、压缩后的大小
    var oldfilesize, newfilesize;

    // 当上传按钮内容发送改变后 获取文件并调用压缩图片的方法
    function upload(itself, id) {
        UForm = new FormData();
        GetFile(itself.files, id);
        // 重新初始化了file的html
        itself.value = "";
    }

    // GetFile 处理获取到的file对象，并对它进行压缩处理, id 是显示图片的容器
    function GetFile(files, id) {
        // 用三目运算符频道文件是否存在
        var file = files ? files[0] : false;
        if (!file) {
            return;
        }
        if (file) {
            oldfilesize = Math.floor((file.size / 1024) * 100) / 100;
            // 如果图片少于3M 则不进行压缩
            if (oldfilesize < 5000) {
                UForm.append("files", file);
                ShowFile(file, id);
                return;
            }
            lrz(file, {
                width: 2048, //设置压缩后的最大宽
                height: 1080,
                quality: 0.8 //图片压缩质量，取值 0 - 1，默认为0.7
            }).then(function (rst) {
                newfilesize = Math.floor((rst.file.size / 1024) * 100) / 100;
                console.log("图片压缩成功，原为：" + oldfilesize + "KB,压缩后为：" + newfilesize + "KB");
                // 把压缩后的图片文件存入 formData中，这样用ajax传到后台才能接收
                UForm.append("files", rst.file);
                ShowFile(rst.file, id);
            }).catch(function (err) {
                alert("压缩图片时出错，请上传图片文件！");
                return false;
            });
        }
    }

    // ShowFile 把处理后的图片显示出来，实现图片的预览功能：
    function ShowFile(file, id) {
        // 使用fileReader对文件对象进行操作
        var reader = new FileReader();
        reader.onload = function (e) {
            var img = new Image();
            img.src = e.target.result;
            // console.log(img)
            // 图片本地回显
            // $('#' + id).attr({src: img.src});
        };
        reader.onerror = function (e, b, c) {
            //error
        };
        // 读取为数据url
        reader.readAsDataURL(file);
        // 上传到服务器
        DoUp(id);
    }

    // 使用AJAX上传数据到后台
    function DoUp(id) {
        $(".add_pic").tips({side: 3, msg: '上传中', bg: '#AE81FF', time: 2});
        $.ajax({
            url: "release/addImg.do?filesName=J0518",
            type: "POST",
            data: UForm,
            contentType: false,//禁止修改编码
            processData: false,//不要把data转化为字符
            success: function (result) {
                if (result.success) {
                    $(".add_pic").tips({side: 3, msg: '上传成功', bg: '#AE81FF', time: 2});
                    // 上传成功 返回图片路径
                    var img_path = result.data.urls;
                    // 拼接到图片预览区
                    var showImg = $("#" + id);
                    var str = "<img src=\"" + img_path + "\" alt=\"图片\">";
                    showImg.append(str);
                    // 拼接图片地址已[,]号分分割
                    var imgs = $("input[name = " + id + "]");
                    imgs.val(imgs.val() + img_path + ",");
                }
            },
            error: function (e) {
                alert("上传出错！请检查是否选择了图片");
                console.log(e)
            }
        });

    }
</script>


</html>