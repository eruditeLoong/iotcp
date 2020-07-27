<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>人员单位</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <t:base type="bootstrap,bootstrap-table,layer,validform,bootstrap-form"></t:base>
</head>
<body style="overflow:hidden;overflow-y:auto;">
<div class="container" style="width:100%;">
    <div class="panel-heading"></div>
    <div class="panel-body">
        <form class="form-horizontal" role="form" id="formobj" action="positionUserOrgController.do?doUpdate"
              method="POST">
            <input type="hidden" id="btn_sub" class="btn_sub"/>
            <input type="hidden" id="id" name="id" value="${positionUserOrg.id}"/>
            <div class="form-group">
                <label for="name" class="col-sm-3 control-label">单位名称：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <input id="name" name="name" value='${positionUserOrg.name}' type="text" maxlength="32"
                               class="form-control input-sm" placeholder="请输入单位名称" ignore="ignore"/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="work" class="col-sm-3 control-label">工作内容：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <input id="work" name="work" value='${positionUserOrg.work}' type="text" maxlength="32"
                               class="form-control input-sm" placeholder="请输入工作内容" ignore="ignore"/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="color" class="col-sm-3 control-label">颜色：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <input id="color" name="color" value='${positionUserOrg.color}' type="color" maxlength="8"
                               class="form-control input-sm" placeholder="请输入颜色"
                               datatype="*" ignore="checked"/>
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