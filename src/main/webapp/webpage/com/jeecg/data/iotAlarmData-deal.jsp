<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>报警数据处理</title>
	<meta name="description" content="">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<t:base type="bootstrap,bootstrap-table,layer,validform,bootstrap-form"></t:base>
</head>
<body style="overflow:hidden;overflow-y:auto;">
<div class="container" style="width:100%;">
	<div class="panel-heading"></div>
	<div class="panel-body">
		<form class="form-horizontal" role="form" id="formobj" action="iotAlarmDataController.do?doUpdate" method="POST">
			<input type="hidden" id="btn_sub" class="btn_sub"/>
			<input type="hidden" id="id" name="id" value="${iotAlarmData.id}"/>
			<div class="row">
				<div class="bt-item col-md-6 col-sm-6">
					<div class="row">
						<div class="col-md-4 col-sm-4 col-xs-3 bt-label">
							场景：
						</div>
						<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
							<t:dictSelect id="sceneBy" field="sceneBy" defaultVal="${iotAlarmData.sceneBy}" type="select" hasLabel="false" title="场景" extendJson="{class:'form-control input-sm'}" datatype="*"
										  dictTable="jform_scene" dictField="id" dictText="name"></t:dictSelect>
						</div>
					</div>
				</div>
				<div class="bt-item col-md-6 col-sm-6">
					<div class="row">
						<div class="col-md-4 col-sm-4 col-xs-3 bt-label">
							部署设备：
						</div>
						<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
							<input id="baseDeviceBy" name="baseDeviceBy" type="hidden" value="${iotAlarmData.baseDeviceBy}" class="form-control input-sm" maxlength="36" datatype="*" ignore="checked"/>
							<input id="baseDeviceName" name="baseDeviceName" type="hidden" value="${iotAlarmData.baseDeviceName}" class="form-control input-sm" maxlength="32" datatype="*" ignore="checked"/>
							<input id="deployDeviceCode" name="deployDeviceCode" type="hidden" value="${iotAlarmData.deployDeviceCode}" class="form-control input-sm" maxlength="10" datatype="*" ignore="checked"/>
							<input id="dataLabel" name="dataLabel" type="hidden" readonly value="${iotAlarmData.dataLabel}" class="form-control input-sm" maxlength="32" datatype="*" ignore="checked"/>
							<select class="form-control input-sm" name="deployDeviceBy" id="deployDeviceBy"></select>
						</div>
					</div>
				</div>
				<div class="bt-item col-md-6 col-sm-6">
					<div class="row">
						<div class="col-md-4 col-sm-4 col-xs-3 bt-label">
							数据名称：
						</div>
						<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
							<select class="form-control input-sm" name="dataField" id="dataField" datatype="*" ignore="checked"></select>
						</div>
					</div>
				</div>
				<div class="bt-item col-md-6 col-sm-6">
					<div class="row">
						<div class="col-md-4 col-sm-4 col-xs-3 bt-label">
							正常数据范围：
						</div>
						<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
							<input id="normalDataRange" name="normalDataRange" type="text" readonly class="form-control input-sm" maxlength="32" value="${iotAlarmData.normalDataRange}" datatype="*" ignore="checked"/>
						</div>
					</div>
				</div>
				<div class="bt-item col-md-6 col-sm-6">
					<div class="row">
						<div class="col-md-4 col-sm-4 col-xs-3 bt-label">
							数据值：
						</div>
						<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
							<input id="dataValue" name="dataValue" type="text" class="form-control input-sm" maxlength="32" value="${iotAlarmData.dataValue}" datatype="*" ignore="checked"/>
						</div>
					</div>
				</div>
				<div class="bt-item col-md-6 col-sm-6">
					<div class="row">
						<div class="col-md-4 col-sm-4 col-xs-3 bt-label">
							报警级别：
						</div>
						<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
							<t:dictSelect id="alarmLevel" field="alarmLevel" defaultVal="${iotAlarmData.alarmLevel}" type="select" hasLabel="false" title="报警级别" extendJson="{class:'form-control'}" datatype="n"
										  typeGroupCode="alarmLevel"></t:dictSelect>
						</div>
					</div>
				</div>
				<div class="bt-item col-md-6 col-sm-6">
					<div class="row">
						<div class="col-md-4 col-sm-4 col-xs-3 bt-label">
							处理状态：
						</div>
						<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
							<div style="padding-top:5px">
								<t:dictSelect field="dealStatus" defaultVal="${iotAlarmData.dealStatus}" extendJson="{class:'i-checks'}" type="radio" hasLabel="false" title="处理状态"
											  typeGroupCode="dealStatus"></t:dictSelect>
							</div>
						</div>
					</div>
				</div>
				<div class="bt-item col-md-6 col-sm-6">
					<div class="row">
						<div class="col-md-4 col-sm-4 col-xs-3 bt-label">
							处理人：
						</div>
						<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
							<input name="dealUserBy" type="text" class="form-control input-sm" maxlength="32" value="${iotAlarmData.dealUserBy}" ignore="ignore"/>
						</div>
					</div>
				</div>
				<div class="bt-item col-md-6 col-sm-6">
					<div class="row">
						<div class="col-md-4 col-sm-4 col-xs-3 bt-label">
							报警时间：
						</div>
						<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
							<input id="alarmDate" name="alarmDate" type="text" class="form-control input-sm laydate-datetime"
								   value="<fmt:formatDate pattern='yyyy-MM-dd HH:mm:ss' type='both' value='${iotAlarmData.alarmDate}'/>" datatype="*" ignore="checked"/>
						</div>
					</div>
				</div>
				<div class="bt-item col-md-12 col-sm-12">
					<div class="row">
						<div class="col-md-2 col-sm-2 col-xs-3 bt-label">
							处理措施：
						</div>
						<div class="col-md-10 col-sm-10 col-xs-8 bt-content">
							<textarea name="dealMeasure" class="form-control input-sm" rows="6" ignore="ignore">${iotAlarmData.dealMeasure}</textarea>
							<span class="Validform_checktip" style="float:left;height:0px;"></span>
							<label class="Validform_label" style="display: none">处理措施</label>
						</div>
					</div>
				</div>
				<div class="bt-item col-md-12 col-sm-12">
					<div class="row">
						<div class="col-md-2 col-sm-2 col-xs-3 bt-label">
							原因分析：
						</div>
						<div class="col-md-10 col-sm-10 col-xs-8 bt-content">
							<textarea name="causeAnalysis" class="form-control input-sm" rows="6" ignore="ignore">${iotAlarmData.causeAnalysis}</textarea>
							<span class="Validform_checktip" style="float:left;height:0px;"></span>
							<label class="Validform_label" style="display: none">原因分析</label>
						</div>
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

		// 判断场景是否被选择
		var dDeviceBy = initDepolyDevice($('#sceneBy').find('option:selected').val());
		var deployDevice = getDeployDevice(dDeviceBy);
		initDeviceData(deployDevice.dataNode);

		$('#sceneBy').attr('disabled','disabled');
		$('#deployDeviceBy').attr('disabled','disabled');
		$('#dataField').attr('disabled','disabled');
		$('#normalDataRange').attr('disabled','disabled');
		$('#dataValue').attr('disabled','disabled');
		$('#alarmLevel').attr('disabled','disabled');
		$('#alarmDate').attr('disabled','disabled');

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
				// var win = frameElement.api.opener;
				if (data.success == true) {
					frameElement.api.close();
					alert(data.msg);
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
		// 场景选择改变
		$('#sceneBy').on('change', function (e) {
			var sceneBy = e.target.value;
			initDepolyDevice(sceneBy);
			console.log(deployDeviceList);
			initDeviceData();
			$('#baseDeviceName').val('');
			$('#baseDeviceBy').val('');
			$('#deployDeviceCode').val('');
			$('#dataField').val('');
			$('#normalDataRange').val('');
		});

		// 部署设备改变
		$('#deployDeviceBy').on('change', function (e) {
			var dDeviceBy = e.target.value;
			var deployDevice = getDeployDevice(dDeviceBy);
			$('#dataField').removeAttr('disabled');
			$('#baseDeviceName').val(deployDevice.name);
			$('#baseDeviceBy').val(deployDevice.deviceBy);
			$('#deployDeviceCode').val(deployDevice.code);
			initDeviceData(deployDevice.dataNode);
		});

		// 数据改变
		$('#dataField').on('change', function (e) {
			var field = e.target.value;
			var text = $('#dataField').find("option:selected").text();
			$('#dataLabel').val(text);
			// 根据当前部署设备的id，获取deployDeviceList中的部署设备对象，再拿到设备的数据节点
			var deployDeviceBy = $('#deployDeviceBy').val();
			var deployDevice = getDeployDevice(deployDeviceBy);
			var nodes = deployDevice.dataNode || [];
			for (let i = 0; i < nodes.length; i++) {
				if(nodes[i].field == field){
					$('#normalDataRange').val(nodes[i].normalDataRange);
					break;
				}
			}
		});
	});
	var deployDeviceList = [];
	function getDeployDevice(dDeviceBy){
		for (let i = 0; i < deployDeviceList.length; i++) {
			console.log((deployDeviceList[i].id == dDeviceBy))
			if(deployDeviceList[i].id == dDeviceBy){
				return deployDevice = deployDeviceList[i];
				break;
			}
		}
		return null;
	}
	function initDeviceData(dataNodes){
		if(dataNodes){
			var dataNode = "${iotAlarmData.dataField}"
			var options = "<option>-请选择-</option>";
			for (var i = 0; i < dataNodes.length; i++) {
				if(dataNode == dataNodes[i].field){
					options += "<option value='" + dataNodes[i].field + "' selected>" + dataNodes[i].name + "</option>"
				}else{
					options += "<option value='" + dataNodes[i].field + "'>" + dataNodes[i].name + "</option>"
				}
			}
			document.getElementById('dataField').innerHTML = options;
		}else{
			$('#dataField').val('');
			$('#dataField').attr('disabled', 'disabled');
		}
	}
	function initDepolyDevice(sceneBy) {
		var dDeviceBy = '';
		if (sceneBy) {
			$('#deployDeviceBy').removeAttr('disabled');
			$.ajax("sceneController.do?getDeployDeviceListBySceneId&sceneId=" + sceneBy, {
				dataType: 'json',
				type: 'GET',
				async: false,
				success: function (result) {
					if (result.obj.length > 0) {
						deployDeviceList = [];
						deployDeviceList = result.obj;
						var options = "<option>-请选择-</option>";
						var value = "${iotAlarmData.deployDeviceBy}"
						for (var i = 0; i < deployDeviceList.length; i++) {
							if(deployDeviceList[i].type === 'terminal'){
								if(value == deployDeviceList[i].id){
									dDeviceBy = deployDeviceList[i].id;
									options += "<option value='" + deployDeviceList[i].id + "' selected>" + deployDeviceList[i].name + "[" + deployDeviceList[i].code + "]</option>"
								}else{
									options += "<option value='" + deployDeviceList[i].id + "'>" + deployDeviceList[i].name + "[" + deployDeviceList[i].code + "]</option>"
								}
							}
						}
						document.getElementById('deployDeviceBy').innerHTML = options;
					}
				}
			});
		} else {
			$('#deployDeviceBy').attr('disabled', 'disabled');
			$('#dataField').attr('disabled', 'disabled');
		}
		return dDeviceBy;
	}
</script>
</body>
</html>