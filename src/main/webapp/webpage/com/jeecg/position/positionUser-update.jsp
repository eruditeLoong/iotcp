<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>人员管理</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <t:base type="bootstrap,bootstrap-table,layer,validform,bootstrap-form"></t:base>
</head>
<body style="overflow:hidden;overflow-y:auto;">
<div class="container" style="width:100%;">
    <div class="panel-heading"></div>
    <div class="panel-body">
        <form class="form-horizontal" role="form" id="formobj" action="positionUserController.do?doUpdate"
              method="POST">
            <input type="hidden" id="btn_sub" class="btn_sub"/>
            <input type="hidden" id="id" name="id" value="${positionUser.id}"/>
            <div class="form-group">
                <label for="imei" class="col-sm-3 control-label">卡号：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <script src="plug-in/hplus/plugins/suggest/bootstrap-suggest.min.js"></script>
                        <input type="text" class="form-control" id="imei" name="imei" value="${positionUser.imei}">
                        <div class="input-group-btn">
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                                <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu dropdown-menu-right" role="menu">
                            </ul>
                        </div>
                        <script type="text/javascript">
                            var imeiList = {
                                value: []
                            };
                            $.ajax("positionModuleController.do?getModules", {
                                dataType: 'json',
                                type: 'GET',
                                success: function (result) {
                                    if (result.obj.length > 0) {
                                        var data = result.obj;
                                        for (var i = 0; i < data.length; i++) {
                                            imeiList.value.push({
                                                'id': data[i].id,
                                                'electricity': data[i].electricity,
                                            });
                                        }
                                        return imeiList;
                                    }
                                }
                            });
                            var testBsSuggest = $("#imei").bsSuggest({
                                showBtn: true,
                                clearable: true, //可清除已输入内容
                                indexId: 0, //data.value 的第几个数据，作为input输入框的内容
                                indexKey: 1, //data.value 的第几个数据，作为input输入框的内容
                                effectiveFields:["id","electricity"],
                                effectiveFieldsAlias:{id: "IMEI",electricity:"电量"},
                                showHeader: true,
                                data: imeiList,
                            }).on('onSetSelectValue', function (e, keyword) {
                                document.getElementById('imei').value = keyword.id;
                            });
                        </script>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-3 control-label">姓名：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <input id="name" name="name" value='${positionUser.name}' type="text" maxlength="32"
                               class="form-control input-sm" placeholder="请输入姓名"
                               datatype="*" ignore="checked"/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="company" class="col-sm-3 control-label">所属单位：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <input type="hidden" name="company" id="company" value="${positionUser.company}">
                        <input type="text" class="form-control" id="companyName" value="">
                        <div class="input-group-btn">
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                                <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu dropdown-menu-right" role="menu">
                            </ul>
                        </div>
                        <script type="text/javascript">
                            var dataList = {
                                value: []
                            };
                            $.ajax("cgReportController.do?datagrid&configId=pUserOrgMsg", {
                                dataType: 'json',
                                type: 'GET',
                                success: function (result) {
                                    if (result.rows.length > 0) {
                                        var data = result.rows;
                                        for (var i = 0; i < data.length; i++) {
                                            dataList.value.push({
                                                'id': data[i].id,
                                                'name': data[i].name,
                                                'work': data[i].work
                                            });
                                            if ('${positionUser.company}' == data[i].id) {
                                                document.getElementById('companyName').value = data[i].name;
                                            }
                                        }
                                        return dataList;
                                    }
                                }
                            });
                            var testBsSuggest = $("#companyName").bsSuggest({
                                showBtn: true,
                                clearable: true, //可清除已输入内容
                                indexId: 1, //data.value 的第几个数据，作为input输入框的内容
                                indexKey: 0, //data.value 的第几个数据，作为input输入框的内容
                                effectiveFields: ["name", "work"],
                                effectiveFieldsAlias: {name: "姓名", work: "工作内容"},
                                showHeader: true,
                                data: dataList,
                            }).on('onSetSelectValue', function (e, keyword) {
                                document.getElementById('company').value = keyword.key;
                                document.getElementById('companyName').value = keyword.id;
                            });
                        </script>
                    </div>
                    &nbsp;
                    <a href="javascript:;" onclick="javascript:addCompany();" class="ace_button">新增单位</a>
                    <script type="text/javascript">
                        function addCompany() {
                            $.dialog({
                                width: 400,
                                height: 300,
                                lock: true,
                                zIndex: 9999,
                                content: 'url:positionUserOrgController.do?goAdd',
                                ok: function () {
                                    var iframe = this.iframe.contentWindow;
                                    var form = iframe.document.forms[0];
                                    var formData = new FormData(form);
                                    iframe.document.getElementById("btn_sub").click();
                                    return false;
                                },
                                cancelVal: '关闭',
                                cancel: true
                            });
                            return false;
                        }
                    </script>
                </div>
            </div>
            <div class="form-group">
                <label for="useObject" class="col-sm-3 control-label">使用对象：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <t:dictSelect field="useObject" type="list" extendJson="{class:'form-control input-sm'}"
                                      datatype="*" typeGroupCode="pUseObj"
                                      hasLabel="false" title="使用对象"
                                      defaultVal="${positionUser.useObject}"></t:dictSelect>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="telephone" class="col-sm-3 control-label">电话：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <input id="telephone" name="telephone" value='${positionUser.telephone}' type="text"
                               maxlength="15" class="form-control input-sm"
                               placeholder="请输入电话" datatype="m" ignore="ignore"/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="sex" class="col-sm-3 control-label">性别：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <t:dictSelect field="sex" type="radio" extendJson="{class:'i-checks'}" typeGroupCode="sex"
                                      hasLabel="false" title="性别"
                                      defaultVal="${positionUser.sex}"></t:dictSelect>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="age" class="col-sm-3 control-label">年龄：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <input id="age" name="age" value='${positionUser.age}' type="text" maxlength="2"
                               class="form-control input-sm" placeholder="请输入年龄"
                               datatype="n" ignore="ignore"/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="age" class="col-sm-3 control-label">状态：</label>
                <div class="col-sm-7">
                    <t:dictSelect id="status" field="status" defaultVal = "${positionUser.status}" extendJson="{class:'i-checks'}" type="radio" hasLabel="false"  title="状态"  typeGroupCode="isValid" ></t:dictSelect>
                </div>
            </div>
            <div class="form-group">
                <label for="remark" class="col-sm-3 control-label">备注：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <textarea name="remark" class="form-control input-sm" rows="6"
                                  ignore="ignore">${positionUser.remark}</textarea>
                        <span class="Validform_checktip" style="float:left;height:0px;"></span>
                        <label class="Validform_label" style="display: none">备注</label>
                    </div>
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