<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>工作票管理</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <%-- <t:base type="bootstrap,layer,validform,webuploader,bootstrap-form"></t:base>
     --%>
    <t:base type="bootstrap,layer,validform,webuploader,bootstrap-form,bootstrap-table"></t:base>
    <script src="plug-in/vue/vue.js"></script>
    <style type="text/css">
        [v-cloak] {
            display: none;
        }

        #listDiv {
            border: 1px solid;
            border-color: rgb(204, 204, 204);
            background-color: #FCFCFC;
            width: 100%;
            border-radius: 4px;
        }

        table #person {
            border: 1px solid;
            border-color: rgb(204, 204, 204);
            background-color: #FFF;
            width: 100%;
            border-radius: 4px;
        }

        #list tr {
            border-top: 1px solid;
            border-bottom: 1px solid;
            border-color: #FFF;
            background-color: #fff;
            height: 30px;
        }
    </style>
</head>
<body style="overflow: hidden; overflow-y: auto;">
<div class="container" style="width: 100%;">
    <div class="panel-heading"></div>
    <div class="panel-body">
        <form class="form-horizontal" role="form" id="formobj" action="workOrderController.do?doUpdate" method="POST">
            <input type="hidden" id="btn_sub" class="btn_sub"/>
            <input type="hidden" id="id" name="id" value="${workOrder.id}"/>
            <div class="form-group">
                <label for="title" class="col-sm-3 control-label">标题：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width: 100%">
                        <input id="title" name="title" value='${workOrder.title}' type="text" maxlength="50"
                               class="form-control input-sm" placeholder="请输入标题" datatype="*" ignore="checked"/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="orderFiles" class="col-sm-3 control-label">工作票扫描件：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width: 100%">
                        <t:webUploader name="orderFiles" outJs="true" auto="false" swfTransform="true"
                                       showImgDiv="filediv_orderFiles" type="image" buttonText='添加图片' displayTxt="false"
                                       pathValues="${workOrder.orderFiles}" datatype="*"></t:webUploader>
                        <div class="form" id="filediv_orderFiles"></div>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="sysOrgCode" class="col-sm-3 control-label">厂家名称：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width: 100%">
                        <input id="sysOrgCode" name="sysOrgCode" value='${workOrder.sysOrgCode}' type="hidden"
                               maxlength="512" class="form-control input-sm" placeholder="请输入厂家名称" datatype="*"
                               ignore="checked"/>
                        <input id="sysOrgName" name="sysOrgName" type="text" value="${orgName}" readonly="readonly"
                               class="form-control input-sm" onClick="popupClick(this,'id,name','sysOrgCode,sysOrgName','pUserOrgMsg');"/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="sysOrgCode" class="col-sm-3 control-label">厂家人员：</label>
                <!-- 隐藏的人员ids -->
                <input id="personIds" name="personIds" type="hidden" value="${workOrder.personIds}" datatype="*"
                       ignore="checked"/>
                <div class="col-sm-7">
                    <div class="input-group" style="width: 100%">
                        <div id="listDiv">
                            <table style="width:100%;">
                                <thead>
                                <a href="javascript:void(0);" class="ace_button" id="parentIds"
                                   onclick="javascript:userSelect()">人员选择</a>
                                <tr style="height:30px;">
                                    <td style="width:120px; text-align: left;">序号</td>
                                    <td style="width:120px; text-align: left;">厂家名称</td>
                                    <td style="display: none">人员ID</td>
                                    <td style="width:120px; text-align: left;">人员姓名</td>
                                    <td style="width:120px; text-align: left;">人员职称</td>
                                </tr>
                                </thead>
                                <tbody id="list">
                                <tr v-for="(branch, index) in users" v-cloak>
                                    <td style="text-align: left;">{{index+1}}</td>
                                    <td style="text-align: left;">{{branch.name}}</td>
                                    <td colspan="3">
                                        <table id="person" style="width:100%">
                                            <tr v-for="(user,index) in branch.person" v-cloak>
                                                <td style="display: none">{{user.id}}</td>
                                                <td style="text-align: left;">
                                                    <input type="checkbox" class="userCheckbox" v-model="user.checked"
                                                           onchange="userSelect1();"/>
                                                    {{user.name}}
                                                </td>
                                                <td style="text-align: left;">{{user.duty}}</td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <script type="text/javascript">
                //userSelect();
                function userSelect1() {
                    var personIds = '';
                    var users = $("input:checked").each(function () {
                        personIds += $(this).parent().prev().html() + ',';
                    });
                    if (personIds.length > 0) {
                        personIds = personIds.substring(0, personIds.length - 1);
                    }
                    $('#personIds').val(personIds);
                }

                var vue = new Vue({
                    el: '#list',
                    data: {
                        users: []
                    },
                });

                function userSelect() {
                    var sysOrgCode = document.getElementById("sysOrgCode").value;
                    var id = document.getElementById("id").value;

                    vue.users = [];
                    if (sysOrgCode.length < 1) {
                        alert("请选择厂家");
                        return false;
                    }
                    $.ajax({
                        type: "GET",
                        url: "workOrderController.do?getBranchUser",
                        data: {
                            sysOrgCode: sysOrgCode,
                            id:id
                        },
                        dataType: "json",
                        success: function (result) {
                            if (result.obj.length < 1) {
                                alert("该厂家暂无人员录入，请联系系统管理员！");
                            }
                            console.log(result.obj);
                            vue.users = result.obj;
                        }
                    });
                }

                userSelect();
            </script>
            <div class="form-group">
                <label class="col-sm-3 control-label">备注：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width: 100%">
                        <textarea name="remark" class="form-control input-sm" rows="6"
                                  ignore="ignore">${workOrder.remark}</textarea>
                        <span class="Validform_checktip" style="float: left; height: 0px;"></span>
                        <label class="Validform_label" style="display: none">备注</label>
                    </div>
                </div>
        </form>
    </div>
</div>
<script type="text/javascript">
    var subDlgIndex = '';
    $(document).ready(function () {
        //单选框/多选框初始化
        $('.i-checks').iCheck({
            labelHover: false,
            cursor: true,
            checkboxClass: 'icheckbox_square-green',
            radioClass: 'iradio_square-green',
            increaseArea: '20%'
        });

        //表单提交
        $("#formobj").Validform({
            tiptype: function (msg, o, cssctl) {
                if (o.type == 3) {
                    validationMessage(o.obj, msg);
                } else {
                    removeMessage(o.obj);
                }
            },
            btnSubmit: "#btn_sub",
            btnReset: "#btn_reset",
            ajaxPost: true,
            beforeSubmit: function (curform) {
            },
            usePlugin: {
                passwordstrength: {
                    minLen: 6,
                    maxLen: 18,
                    trigger: function (obj, error) {
                        if (error) {
                            obj.parent().next().find(".Validform_checktip").show();
                            obj.find(".passwordStrength").hide();
                        } else {
                            $(".passwordStrength").show();
                            obj.parent().next().find(".Validform_checktip").hide();
                        }
                    }
                }
            },
            callback: function (data) {
                var win = frameElement.api.opener;
                if (data.success == true) {
                    frameElement.api.close();
                    win.reloadTable();
                    win.tip(data.msg);
                } else {
                    if (data.responseText == '' || data.responseText == undefined) {
                        $.messager.alert('错误', data.msg);
                        $.Hidemsg();
                    } else {
                        try {
                            var emsg = data.responseText.substring(data.responseText.indexOf('错误描述'), data.responseText.indexOf('错误信息'));
                            $.messager.alert('错误', emsg);
                            $.Hidemsg();
                        } catch (ex) {
                            $.messager.alert('错误', data.responseText + "");
                            $.Hidemsg();
                        }
                    }
                    return false;
                }
            }
        });
    });
</script>
</body>
</html>