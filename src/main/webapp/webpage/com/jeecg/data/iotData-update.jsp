<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>物联网数据</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">
<t:base type="bootstrap,layer,validform,bootstrap-form"></t:base>
</head>
<body style="overflow: hidden; overflow-y: auto;">
	<div class="container" style="width: 100%;">
		<div class="panel-heading"></div>
		<div class="panel-body">
			<form class="form-horizontal" role="form" id="formobj" action="iotDataController.do?doUpdate" method="POST">
				<input type="hidden" id="btn_sub" class="btn_sub" />
				<input type="hidden" id="id" name="id" value="${iotData.id}" />
				<div class="form-group">
					<label for="createDate" class="col-sm-3 control-label">创建日期：</label>
					<div class="col-sm-7">
						<div class="input-group" style="width: 100%">
							<input id="createDate" name="createDate" type="text" class="form-control input-sm laydate-datetime" placeholder="请输入创建日期" datatype="*" ignore="checked"
								value="<fmt:formatDate pattern='yyyy-MM-dd HH:mm:ss' type='both' value='${iotData.createDate}'/>" />
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label for="gisDeviceBy" class="col-sm-3 control-label">设备：</label>
					<div class="col-sm-7">
						<div class="input-group" style="width: 100%">
							<t:dictSelect field="gisDeviceBy" type="select" extendJson="{class:'form-control input-sm'}" datatype="*" dictTable="jform_instance_device" dictField="id" dictText="name"
								defaultVal="${iotData.instanceDeviceBy}"></t:dictSelect>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label for="fieldBy" class="col-sm-3 control-label">测量项目：</label>
					<div class="col-sm-7">
						<div class="input-group" style="width: 100%">
							<input id="fieldBy" name="fieldBy" value='${iotData.fieldBy}' type="text" maxlength="32" class="form-control input-sm" placeholder="请输入测量项目" datatype="*" ignore="checked" />
						</div>
					</div>
				</div>
				<div class="form-group">
					<label for="data" class="col-sm-3 control-label">数值：</label>
					<div class="col-sm-7">
						<div class="input-group" style="width: 100%">
							<input id="data" name="data" value='${iotData.data}' type="text" maxlength="32" class="form-control input-sm" placeholder="请输入数值" datatype="*" ignore="checked" />
						</div>
					</div>
				</div>
				<div class="form-group">
					<label for="type" class="col-sm-3 control-label">数值类型：</label>
					<div class="col-sm-7">
						<div class="input-group" style="width: 100%">
							<t:dictSelect field="type" type="select" extendJson="{class:'form-control input-sm'}" datatype="*" typeGroupCode="bDataType" defaultVal="${iotData.type}"></t:dictSelect>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label for="starts" class="col-sm-3 control-label">状态：</label>
					<div class="col-sm-7">
						<div class="input-group" style="width: 100%">
							<input id="status" name="status" value='${iotData.status}' type="text" maxlength="1" class="form-control input-sm" placeholder="请输入状态" datatype="*" ignore="checked" />
						</div>
					</div>
				</div>
				<div class="form-group">
					<label for="remark" class="col-sm-3 control-label">备注：</label>
					<div class="col-sm-7">
						<div class="input-group" style="width: 100%">
							<textarea name="remark" class="form-control input-sm" rows="6" ignore="ignore">${iotData.remark}</textarea>
							<span class="Validform_checktip" style="float: left; height: 0px;"></span>
							<label class="Validform_label" style="display: none">备注</label>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
		var subDlgIndex = '';
		$(document).ready(function() {
			//单选框/多选框初始化
			$('.i-checks').iCheck({
				labelHover : false,
				cursor : true,
				checkboxClass : 'icheckbox_square-green',
				radioClass : 'iradio_square-green',
				increaseArea : '20%'
			});

			//表单提交
			$("#formobj").Validform({
				tiptype : function(msg, o, cssctl) {
					if (o.type == 3) {
						validationMessage(o.obj, msg);
					} else {
						removeMessage(o.obj);
					}
				},
				btnSubmit : "#btn_sub",
				btnReset : "#btn_reset",
				ajaxPost : true,
				beforeSubmit : function(curform) {
				},
				usePlugin : {
					passwordstrength : {
						minLen : 6,
						maxLen : 18,
						trigger : function(obj, error) {
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
				callback : function(data) {
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