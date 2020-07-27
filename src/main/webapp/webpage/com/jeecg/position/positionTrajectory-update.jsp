<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>定位数据</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <t:base type="bootstrap,bootstrap-table,layer,validform,bootstrap-form"></t:base>
</head>
<body style="overflow:hidden;overflow-y:auto;">
<div class="container" style="width:100%;">
    <div class="panel-heading"></div>
    <div class="panel-body">
        <form class="form-horizontal" role="form" id="formobj" action="positionTrajectoryController.do?doUpdate"
              method="POST">
            <input type="hidden" id="btn_sub" class="btn_sub"/>
            <input type="hidden" id="id" name="id" value="${positionTrajectory.id}"/>
            <div class="form-group">
                <label for="imei" class="col-sm-3 control-label">IMEI：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <input id="imei" name="imei" value='${positionTrajectory.imei}' type="text" maxlength="36"
                               class="form-control input-sm" placeholder="请输入IMEI" datatype="*" ignore="checked"/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="latitude" class="col-sm-3 control-label">纬度：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <input id="latitude" name="latitude" value='${positionTrajectory.latitude}' type="text"
                               maxlength="32" class="form-control input-sm" placeholder="请输入纬度" datatype="s"
                               ignore="checked"/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="longitude" class="col-sm-3 control-label">经度：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <input id="longitude" name="longitude" value='${positionTrajectory.longitude}' type="text"
                               maxlength="32" class="form-control input-sm" placeholder="请输入经度" datatype="s"
                               ignore="checked"/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="gpsSpeed" class="col-sm-3 control-label">GPS速度：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <input id="gpsSpeed" name="gpsSpeed" value='${positionTrajectory.gpsSpeed}' type="text"
                               maxlength="3" class="form-control input-sm" placeholder="请输入GPS速度" datatype="n"
                               ignore="ignore"/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="course" class="col-sm-3 control-label">航向：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <input id="course" name="course" value='${positionTrajectory.course}' type="text" maxlength="3"
                               class="form-control input-sm" placeholder="请输入航向" datatype="n" ignore="ignore"/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="locateType" class="col-sm-3 control-label">定位方式：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <input id="locateType" name="locateType" value='${positionTrajectory.locateType}' type="text"
                               maxlength="1" class="form-control input-sm" placeholder="请输入定位方式" datatype="*"
                               ignore="checked"/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="ewLongitude" class="col-sm-3 control-label">东西经：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <input id="ewLongitude" name="ewLongitude" value='${positionTrajectory.ewLongitude}' type="text"
                               maxlength="1" class="form-control input-sm" placeholder="请输入东西经" datatype="n"
                               ignore="ignore"/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="nsLatitude" class="col-sm-3 control-label">南北纬：</label>
                <div class="col-sm-7">
                    <div class="input-group" style="width:100%">
                        <input id="nsLatitude" name="nsLatitude" value='${positionTrajectory.nsLatitude}' type="text"
                               maxlength="1" class="form-control input-sm" placeholder="请输入南北纬" datatype="n"
                               ignore="ignore"/>
                    </div>
                </div>
            </div>
			<div class="form-group">
				<label for="nsLatitude" class="col-sm-3 control-label">是否越界：</label>
				<div class="col-sm-7">
					<div class="input-group" style="width:100%">
						<t:dictSelect id="isCross" field="isCross" defaultVal="${positionTrajectory.isCross}" extendJson="{class:'i-checks'}"
									  type="radio" hasLabel="false" title="是否越界" typeGroupCode="isBound"></t:dictSelect>
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