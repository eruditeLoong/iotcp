<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>定位模块信息</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <t:base type="bootstrap,bootstrap-table,layer,validform,bootstrap-form"></t:base>
</head>
<body style="overflow:hidden;overflow-y:auto;">
<div class="container" style="width:100%;">
    <div class="panel-heading"></div>
    <div class="panel-body">
        <form class="form-horizontal" role="form" id="formobj" action="positionModuleController.do?doUpdate"
              method="POST">
            <input type="hidden" id="btn_sub" class="btn_sub"/>
            <div class="form-group">
                <label for="cardNo" class="col-sm-3 control-label">IMEI：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <input id="id" name="id" type="text" maxlength="16" value="${positionModule.id}"
                               class="form-control input-sm" placeholder="请输入卡号" validType="position_module,card_no,id"
                               datatype="*" ignore="ignore"/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="cardNo" class="col-sm-3 control-label">卡号：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <input id="cardNo" name="cardNo" value='${positionModule.cardNo}' type="text" maxlength="12"
                               class="form-control input-sm" placeholder="请输入卡号" validType="position_module,card_no,id"
                               datatype="*" ignore="ignore"/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="iccid" class="col-sm-3 control-label">ICCID：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <input id="iccid" name="iccid" value='${positionModule.iccid}' type="text" maxlength="25"
                               class="form-control input-sm" placeholder="请输入ICCID" validType="position_module,iccid,id"
                               datatype="*" ignore="ignore"/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="iccid" class="col-sm-3 control-label">地址：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <input id="ipPort" name="ipPort" value='${positionModule.ipPort}' type="text" maxlength="25" class="form-control input-sm"
                               placeholder="请输入ICCID" validType="position_module,ipPort,id" datatype="*"
                               ignore="ignore"/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="protocol" class="col-sm-3 control-label">硬件协议：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <t:dictSelect field="protocol" type="list" extendJson="{class:'form-control input-sm'}"
                                      typeGroupCode="isValid" hasLabel="false" title="硬件协议"
                                      defaultVal="${positionModule.protocol}"></t:dictSelect>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="electricity" class="col-sm-3 control-label">电量：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <input id="electricity" name="electricity" value='${positionModule.electricity}' type="text"
                               maxlength="3" class="form-control input-sm" placeholder="请输入电量" datatype="n"
                               ignore="ignore"/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="signals" class="col-sm-3 control-label">信号：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <input id="signals" name="signals" value='${positionModule.signals}' type="text" maxlength="3"
                               class="form-control input-sm" placeholder="请输入信号" datatype="n" ignore="ignore"/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="version" class="col-sm-3 control-label">软件版本：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <input id="version" name="version" value='${positionModule.version}' type="text" maxlength="3"
                               class="form-control input-sm" placeholder="请输入软件版本" datatype="n" ignore="ignore"/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="timeZoom" class="col-sm-3 control-label">时区：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <input id="timeZoom" name="timeZoom" value='${positionModule.timeZoom}' type="text"
                               maxlength="3" class="form-control input-sm" placeholder="请输入时区" datatype="n"
                               ignore="ignore"/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="aliveTime" class="col-sm-3 control-label">心跳间隔：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <input id="aliveTime" name="aliveTime" value='${positionModule.aliveTime}' type="text"
                               maxlength="3" class="form-control input-sm" placeholder="请输入心跳间隔" datatype="n"
                               ignore="ignore"/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="onlineStatus" class="col-sm-3 control-label">在线状态：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <t:dictSelect field="onlineStatus" type="radio" extendJson="{class:'i-checks'}"
                                      typeGroupCode="isValid" hasLabel="false" title="在线状态"
                                      defaultVal="${positionModule.onlineStatus}"></t:dictSelect>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="isValid" class="col-sm-3 control-label">是否有效：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <t:dictSelect field="isValid" type="radio" extendJson="{class:'i-checks'}"
                                      typeGroupCode="isValid" hasLabel="false" title="是否有效"
                                      defaultVal="${positionModule.isValid}"></t:dictSelect>
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