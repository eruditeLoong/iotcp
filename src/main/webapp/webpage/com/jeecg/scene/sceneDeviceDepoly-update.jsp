<jsp:useBean id="sceneDeviceDepoly" scope="request" type="com.jeecg.scene.entity.SceneDeviceDepolyEntity"/>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>场景部署设备</title>
	<meta name="description" content="">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<t:base type="bootstrap,layer,validform,bootstrap-form"></t:base>
</head>
<body style="overflow: hidden; overflow-y: auto;">
<div class="container" style="width: 100%;">
	<div class="panel-heading"></div>
	<div class="panel-body">
		<form class="form-horizontal" role="form" id="formobj" action="sceneDeviceDepolyController.do?doUpdate" method="POST">
			<input type="hidden" id="btn_sub" class="btn_sub"/>
			<input type="hidden" id="id" name="id" value="${sceneDeviceDepoly.id}"/>
			<div class="form-group">
				<label for="sceneBy" class="col-sm-3 control-label">场景：</label>
				<div class="col-sm-7">
					<div class="input-group" style="width: 100%">
						<t:dictSelect id="sceneBy" field="sceneBy" type="select" hasLabel="false" title="场景" extendJson="{class:'form-control input-sm'}" datatype="*" dictTable="jform_scene"
									  dictField="id" dictText="name" defaultVal="${sceneDeviceDepoly.sceneBy }"></t:dictSelect>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="deviceBy" class="col-sm-3 control-label">基础设备：</label>
				<div class="col-sm-7">
					<div class="input-group" style="width: 100%">
						<t:dictSelect id="deviceBy" field="deviceBy" type="select" hasLabel="false" title="设备" extendJson="{class:'form-control input-sm'}" datatype="*"
									  dictTable="jform_base_device" dictField="id" dictText="name" defaultVal="${sceneDeviceDepoly.deviceBy }"></t:dictSelect>
					</div>
				</div>
			</div>
			<div class="form-group" style="">
				<label for="deviceParentBy" class="col-sm-3 control-label">父级设备：</label>
				<div class="col-sm-7">
					<div class="input-group" style="width: 100%">
						<select id="deviceParentBy" name="deviceParentBy" class="form-control input-sm"></select>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="deviceCode" class="col-sm-3 control-label">设备编码：</label>
				<div class="col-sm-7">
					<div class="input-group" style="width: 100%">
						<input id="deviceCode" name="deviceCode" value='${sceneDeviceDepoly.deviceCode}' type="text" maxlength="10" class="form-control input-sm" placeholder="请输入设备编码" datatype="*"
							   ignore="checked"/>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="deviceCode" class="col-sm-3 control-label">网关IP地址：</label>
				<div class="col-sm-7">
					<div class="input-group" style="width: 100%">
						<input id="deviceAddress" name="deviceAddress" type="text" value='${sceneDeviceDepoly.deviceAddress}' maxlength="15" class="form-control input-sm" placeholder="请输入网关设备IP地址"
							   datatype="*" ignore="ignore"/>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="deviceCode" class="col-sm-3 control-label">是否请求命令：</label>
				<div class="col-sm-7">
					<div class="input-group" style="width: 100%">
						<t:dictSelect id="isSendCmd" field="isSendCmd" defaultVal="${sceneDeviceDepoly.isSendCmd}" type="radio" hasLabel="false" title="" extendJson="{class:'i-checks'}" datatype="*"
									  typeGroupCode="sf_yn"></t:dictSelect>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="deviceCode" class="col-sm-3 control-label">命令数据类型：</label>
				<div class="col-sm-7">
					<t:dictSelect field="cmdDataType" extendJson="{class:'form-control input-sm'}" type="select" defaultVal="${sceneDeviceDepoly.cmdDataType}" typeGroupCode="dataShape"></t:dictSelect>
				</div>
			</div>
			<div class="form-group">
				<label for="deviceCode" class="col-sm-3 control-label">请求命令：</label>
				<div class="col-sm-7">
					<div class="input-group" style="width: 100%">
						<input id="requestCmd" name="requestCmd" type="text" value='${sceneDeviceDepoly.requestCmd}' maxlength="50" class="form-control input-sm" placeholder="请输入请求命令" datatype="*"
							   ignore="ignore"/>
						<a href="javascript:;" onclick="javascript:calcCRC();">CRC计算</a>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="threeData" class="col-sm-3 control-label">设备位置：</label>
				<div class="col-sm-7">
					<div class="input-group" style="width: 100%">
						<input id="threeData" name="threeData" value='${sceneDeviceDepoly.threeData}' type="text" maxlength="200" class="form-control input-sm" placeholder="请输入设备位置" datatype="*"
							   ignore="checked"/>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>
</body>
</html>


<script type="text/javascript">
	var subDlgIndex = '';

	function calcCRC() {
		var cmd = $('#requestCmd').val();
		$.getJSON("sceneDeviceDepolyController.do?appendCRC&requestCmd=" + cmd, function (result) {
			if (result.success) {
				var c = result.obj.requestCmd;
				$('#requestCmd').val(c);
			}
		});
	}

	function initIsSendCmd(isSendCmd){
		if (isSendCmd === 'Y') {
			$('select[name=cmdDataType]').removeAttr('disabled');
			$('input[name=requestCmd]').removeAttr('disabled');
		} else {
			$('select[name=cmdDataType]').attr('disabled', 'disabled');
			$('input[name=requestCmd]').attr('disabled', 'disabled');
		}
	}

	function deviceByChange() {
		var deviceBy = $('#deviceBy').val();
		$.getJSON("baseDeviceController.do?getDeviceById&id=" + deviceBy, function (result) {
			if (result.success) {
				var type = result.obj.type;
				switch (type) {
					case "gateway":  // 网关设备
						// 输入IP，不输入请求指令
						$('#deviceAddress').attr('readonly', false);
						// $('#requestCmd').val("");
						// $('#requestCmd').attr('readonly', true);
						break;
					case "terminal":  // 终端设备
						$('#deviceAddress').val("");
						$('#deviceAddress').attr('readonly', true);
						// $('#requestCmd').attr('readonly', false);
						break;
					default:
						$('#deviceAddress').val("");
						$('#deviceAddress').attr('readonly', true);
						// $('#requestCmd').val("");
						// $('#requestCmd').attr('readonly', true);
						break;
				}
			}
		});
	}

	$('input[name=isSendCmd]').on('ifChecked', function (){initIsSendCmd($(this).val())});
	initIsSendCmd('${sceneDeviceDepoly.isSendCmd}');

	$(document).ready(function () {
		//单选框/多选框初始化
		$('.i-checks').iCheck({
			labelHover: false,
			cursor: true,
			checkboxClass: 'icheckbox_square-green',
			radioClass: 'iradio_square-green',
			increaseArea: '20%'
		});
		initDeviceParentBySuggest();
		deviceByChange();
		$('#sceneBy').on('change', initDeviceParentBySuggest);
		$('#deviceBy').on('change', deviceByChange);
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
				var id = $('#id').val();
				var sceneBy = $('#sceneBy').val();
				var deviceBy = $('#deviceBy').val();
				var deviceParentBy = $('#deviceParentBy').val();
				var deviceCode = $('#deviceCode').val();
				var flag = false;
				$.ajax({
					url: 'sceneDeviceDepolyController.do?checkCode',
					data: {
						'id': id,
						'sceneBy': sceneBy,
						'deviceBy': deviceBy,
						'deviceParentBy': deviceParentBy,
						'deviceCode': deviceCode
					},
					async: false,
					dataType: 'json',
					success: function (result) {
						if (!result.success) {
							alert('设备编码冲突，请修改编码！');
							$('#deviceCode').focus();
							$('#deviceCode').val('');
							flag = false;
						} else {
							flag = true;
						}
					}
				});
				if (!flag) {
					return false;
				}
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
					win.loaderTree();
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

	function initDeviceParentBySuggest() {
		var sceneBy = $('#sceneBy').val();
		var deviceBy = '${sceneDeviceDepoly.deviceBy}';
		var deviceParentBy = '${sceneDeviceDepoly.deviceParentBy}';
		// 设备
		$.ajax("baseDeviceController.do?getSceneDeviceByScene&sceneBy=" + sceneBy, {
			dataType: 'json',
			type: 'GET',
			success: function (result) {
				var obj = JSON.parse(JSON.stringify(result.obj));
				var options = "<option></option>";
				for (var i in obj) {
					var o = obj[i];
					if (o.id == deviceBy) {
						options += '<option value="' + o.id + '" selected>' + o.name + '</option>';
					} else {
						options += '<option value="' + o.id + '">' + o.name + '</option>';
					}
				}
				$('#deviceBy').html(options);
			}
		});
		// 父级设备
		$.ajax("sceneDeviceDepolyController.do?getParentList&sceneBy=" + sceneBy, {
			dataType: 'json',
			type: 'GET',
			success: function (result) {
				var obj = JSON.parse(JSON.stringify(result.obj.value));
				var options = "<option></option>";
				for (var i in obj) {
					var o = obj[i];
					if (o.id == deviceParentBy) {
						options += '<option value="' + o.id + '" selected>' + o.name + '[' + o.code + ']' + '</option>';
					} else {
						options += '<option value="' + o.id + '">' + o.name + '[' + o.code + ']' + '</option>';
					}
				}
				$('#deviceParentBy').html(options);
			}
		});
	}
</script>
