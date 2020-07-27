<jsp:useBean id="sceneDevice" scope="request" type="com.jeecg.scene.entity.SceneDeviceEntity"/>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>场景设备</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <t:base type="bootstrap,layer,validform,bootstrap-form"></t:base>
</head>
<body style="overflow: hidden; overflow-y: auto;">
<div class="container" style="width: 100%;">
    <div class="panel-heading"></div>
    <div class="panel-body">
        <form class="form-horizontal" role="form" id="formobj" action="sceneDeviceController.do?doUpdate" method="POST">
            <input type="hidden" id="btn_sub" class="btn_sub"/>
            <input type="hidden" id="id" name="id" value="${sceneDevice.id}"/>
            <div class="form-group">
                <label for="sceneBy" class="col-sm-3 control-label">场景：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width: 100%">
                        <t:dictSelect field="sceneBy" dictField="id" dictText="name" dictTable="jform_scene" defaultVal="${sceneDevice.sceneBy}" extendJson="form-control input-sm" datatype="*"
                                      ignore="checked"></t:dictSelect>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="deviceBy" class="col-sm-3 control-label">设备：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width: 100%">
                        <t:dictSelect field="deviceBy" dictField="id" dictText="name" dictTable="jform_base_device" defaultVal="${sceneDevice.deviceBy}" extendJson="form-control input-sm" datatype="*"
                                      ignore="checked"></t:dictSelect>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
<script type="text/javascript">
    var subDlgIndex = '';
    $(document).ready(function () {

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