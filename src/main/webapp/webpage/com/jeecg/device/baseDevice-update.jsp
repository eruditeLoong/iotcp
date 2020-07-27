<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>基础设备-修改</title>
	<meta name="description" content="">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<t:base type="bootstrap,layer,validform,bootstrap-form,bootstrap-table"></t:base>
	<style type="text/css">
		td {
			margin: 0px;
			padding: 0px;
			width: 100px;
		}

		td input {
			height: 30px;
			padding: 5px 10px;
			font-size: 12px;
			line-height: 1.5;
			border-radius: 3px;
			border: 1px solid #ccc;
			margin: 0px;
			width: 100%
		}

		td select {
			height: 30px;
			padding: 5px 10px;
			font-size: 12px;
			line-height: 1.5;
			border-radius: 3px;
			border: 1px solid #ccc;
			margin: 0px;
			width: 100%
		}

		td textarea {
			height: 30px;
			padding: 5px 10px;
			font-size: 12px;
			line-height: 1.5;
			border-radius: 3px;
			border: 1px solid #ccc;
			margin: 0px;
			width: 100%
		}

		.table td.form-ck {
			width: 20px;
		}
	</style>
</head>
<body>
<div class="container" style="padding-top: 20px;">
	<div class="panel panel-info">
		<div class="panel-heading"></div>
		<div class="panel-body">
			<form class="form-horizontal" role="form" id="formobj" action="baseDeviceController.do?doUpdate" method="POST">
				<input type="hidden" id="btn_sub" class="btn_sub"/>
				<input type="hidden" id="id" name="id" value="${baseDevicePage.id}"/>
				<fieldset>
					<legend>用户信息</legend>
					<div class="bt-item col-md-6 col-sm-6">
						<div class="row">
							<div class="col-md-4 col-sm-4 col-xs-4 bt-label">创建人：</div>
							<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
								<input id="createName" name="createName" value='${fns:getUserRealnameByUsername(baseDevicePage.createBy) }' type="text" readonly="readonly"
									   class="form-control input-sm"/>
							</div>
						</div>
					</div>
					<div class="bt-item col-md-6 col-sm-6">
						<div class="row">
							<div class="col-md-4 col-sm-4 col-xs-4 bt-label">创建时间：</div>
							<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
								<input id="createDate" name="createDate" value='<fmt:formatDate value='${baseDevicePage.createDate}' type="date" pattern="yyyy-MM-dd HH:mm:ss"/>'
									   class="form-control input-sm" type="text" readonly="readonly" maxlength="80" class="form-control input-sm"/>
							</div>
						</div>
					</div>
					<c:if test="${not empty baseDevicePage.updateBy}">
						<div class="bt-item col-md-6 col-sm-6">
							<div class="row">
								<div class="col-md-4 col-sm-4 col-xs-4 bt-label">更新人：</div>
								<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
									<input id=updateBy name="updateBy" type="text" class="form-control" readonly="readonly" value="${fns:getUserRealnameByUsername(baseDevicePage.updateBy)}"
										   class="form-control input-sm" ignore="ignore"/>
								</div>
							</div>
						</div>
						<div class="bt-item col-md-6 col-sm-6">
							<div class="row">
								<div class="col-md-4 col-sm-4 col-xs-4 bt-label">更新日期：</div>
								<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
									<input id="updateDate" name="updateDate" type="text" class="form-control input-sm laydate-datetime"
										   value="<fmt:formatDate pattern='yyyy-MM-dd HH:mm:ss' type='both' value='${baseDevicePage.updateDate}'/>" readonly="readonly" ignore="ignore"/>
								</div>
							</div>
						</div>
					</c:if>
				</fieldset>
				<fieldset>
					<legend>基础设备信息</legend>
					<div class="main-form">
						<div class="row">
							<div class="bt-item col-md-6 col-sm-6">
								<div class="row">
									<div class="col-md-4 col-sm-4 col-xs-4 bt-label">设备名称：</div>
									<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
										<input name="name" value="${baseDevicePage.name}" type="text" class="form-control input-sm" maxlength="32" datatype="*" ignore="checked"/>
									</div>
								</div>
							</div>
							<div class="bt-item col-md-6 col-sm-6">
								<div class="row">
									<div class="col-md-4 col-sm-4 col-xs-4 bt-label">设备类型：</div>
									<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
										<div style="padding: 5px;">
											<t:dictSelect field="type" defaultVal="${baseDevicePage.type}" type="radio" hasLabel="false" title="设备类型" extendJson="{class:'i-checks'}" datatype="*"
														  typeGroupCode="bd_type"></t:dictSelect>
										</div>
									</div>
								</div>
							</div>
							<div class="bt-item col-md-6 col-sm-6">
								<div class="row">
									<div class="col-md-4 col-sm-4 col-xs-4 bt-label">通讯方式：</div>
									<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
										<t:dictSelect field="comMethod" defaultVal="${baseDevicePage.comMethod}" type="select" hasLabel="false" title="通讯方式"
													  extendJson="{class:'form-control input-sm'}"
													  datatype="*" ignore="ignore" dictTable="jform_net_mode" dictField="id" dictText="name"></t:dictSelect>
									</div>
								</div>
							</div>
							<div class="bt-item col-md-6 col-sm-6">
								<div class="row">
									<div class="col-md-4 col-sm-4 col-xs-4 bt-label">数据格式：</div>
									<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
										<t:dictSelect field="dataFormat" defaultVal="${baseDevicePage.dataFormat}" type="select" extendJson="{class:'form-control input-sm'}" datatype="*"
													  ignore="ignore" dictTable="jform_data_protocol" dictField="id" dictText="name"></t:dictSelect>
									</div>
								</div>
							</div>
							<div class="bt-item col-md-6 col-sm-6">
								<div class="row">
									<div class="col-md-4 col-sm-4 col-xs-4 bt-label">通讯协议：</div>
									<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
										<t:dictSelect field="comProtocol" defaultVal="${baseDevicePage.comProtocol}" type="select" hasLabel="false" title="通讯方式"
													  extendJson="{class:'form-control input-sm'}"
													  datatype="*" ignore="ignore" dictTable="jform_com_protocol" dictField="id" dictText="name"></t:dictSelect>
									</div>
								</div>
							</div>
							<div class="bt-item col-md-6 col-sm-6">
								<div class="row">
									<div class="col-md-4 col-sm-4 col-xs-4 bt-label">工作模式：</div>
									<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
										<div style="padding: 5px 0;">
											<t:dictSelect field="workModel" type="radio" hasLabel="false" defaultVal="${baseDevicePage.workModel}" title="工作模式"
														  extendJson="{class:'i-checks'}"
														  datatype="*" ignore="ignore" typeGroupCode="bd_workModel"></t:dictSelect>
										</div>
									</div>
								</div>
							</div>
							<div class="bt-item col-md-6 col-sm-6">
								<div class="row">
									<div class="col-md-4 col-sm-4 col-xs-4 bt-label">轮询时间（ms）：</div>
									<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
										<div class="input-group">
											<input id="requestTime" name="requestTime" value="${baseDevicePage.requestTime}" type="text" class="form-control input-sm" maxlength="10" datatype="n1-10"
												   ignore="ignore"/>
											<span class="input-group-btn" title="具有父子关系的子设备，把子设备的轮询时间作为网关遍历的延时时间">
												<span class="btn btn-info input-sm">
                                                    <span class="fa fa-info-circle fa-lg"></span>
												</span>
											</span>
										</div>
									</div>
								</div>
							</div>
							<div class="bt-item col-md-6 col-sm-6">
								<div class="row">
									<div class="col-md-4 col-sm-4 col-xs-4 bt-label">状态：</div>
									<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
										<div style="padding: 5px 0">
											<t:dictSelect field="status" extendJson="{class:'i-checks'}" type="radio" defaultVal="1" typeGroupCode="isValid"></t:dictSelect>
										</div>
									</div>
								</div>
							</div>
							<div class="bt-item col-md-12 col-sm-12">
								<div class="row">
									<div class="col-md-2 col-sm-2 col-xs-4 bt-label">模型文件：</div>
									<div class="col-md-10 col-sm-10 col-xs-8 bt-content">
										<t:webUploader name="modelFile" outJs="false" auto="false" fileNumLimit="1" pathValues="${baseDevicePage.modelFile }" datatype="*"></t:webUploader>
									</div>
								</div>
							</div>
						</div>
					</div>
				</fieldset>
				<fieldset>
					<legend>&nbsp</legend>
					<ul class="nav nav-tabs" style="margin-bottom: 0" id="subTabs">
						<li class="active">
							<a href="#baseDeviceData" data-toggle="tab">基础设备数据点</a>
						</li>
					</ul>
				</fieldset>
				<div class="tab-content" style="background-color: #fff; padding-top: 10px;">
					<div class="tab-pane fade in active" id="baseDeviceData">
						<div class="form-tb-toolbar">
							<button onclick="addOneRow('baseDeviceData_table')" type="button" class="btn btn-default btn-sm">
								<span class="glyphicon glyphicon-plus"></span>
								&nbsp;添加
							</button>
							<button onclick="deleteSelectRows('baseDeviceData_table')" type="button" class="btn btn-default btn-sm">
								<span class="glyphicon glyphicon-minus"></span>
								&nbsp;删除
							</button>
						</div>
						<div style="overflow-x: auto">
							<table class="table subinfo-table" id="baseDeviceData_table">
								<thead>
								<tr>
									<th align="center"></th>
									<th>显示名称</th>
									<th>数据标识</th>
									<th>数据单位</th>
									<th>读写类型</th>
									<th>数据类型</th>
									<th>数据范围</th>
									<th>正常数据范围</th>
									<th>精度</th>
									<th>枚举范围</th>
									<th>备注</th>
								</thead>
								<tbody>
								<c:if test="${fn:length(baseDevicePage.baseDeviceDataList)  > 0 }">
									<c:forEach items="${baseDevicePage.baseDeviceDataList}" var="poVal" varStatus="stuts">
										<tr>
											<td class="form-ck">
												<input style="" type="checkbox" name="ck"/>
												<input name="baseDeviceDataList[${stuts.index }].id" value="${poVal.id}" type="hidden"/>
											</td>
											<td>
												<input name="baseDeviceDataList[${stuts.index }].name" value="${poVal.name}" type="text" maxlength="32" datatype="*" ignore="checked"/>
											</td>
											<td>
												<input name="baseDeviceDataList[${stuts.index }].field" value="${poVal.field}" type="text" maxlength="32" datatype="*" ignore="checked"/>
											</td>
											<td>
												<input name="baseDeviceDataList[${stuts.index }].unit" value="${poVal.unit}" type="text" maxlength="32" datatype="*" ignore="checked"/>
											</td>
											<td>
												<t:dictSelect field="baseDeviceDataList[${stuts.index }].rwType" defaultVal="${poVal.rwType}" type="select" hasLabel="false" title="读写类型"
															  extendJson="{class:'w80'}"
															  datatype="*" typeGroupCode="readWrite"></t:dictSelect>
											</td>
											<td>
												<t:dictSelect field="baseDeviceDataList[${stuts.index }].dataType" defaultVal="${poVal.dataType}" type="select" hasLabel="false" title="数据类型"
															  extendJson="{class:'w80'}" datatype="*" typeGroupCode="bDataType"></t:dictSelect>
											</td>
											<td>
												<input name="baseDeviceDataList[${stuts.index }].dataRange" value="${poVal.dataRange}" type="text" maxlength="50" datatype="*" ignore="ignore"/>
											</td>
											<td>
												<input name="baseDeviceDataList[${stuts.index }].normalDataRange" value="${poVal.normalDataRange}" type="text" maxlength="50" datatype="*"
													   ignore="ignore"/>
											</td>
											<td>
												<input name="baseDeviceDataList[${stuts.index }].accuracy" value="${poVal.accuracy}" type="text" maxlength="5" datatype="n" ignore="ignore"/>
											</td>
											<td>
												<input name="baseDeviceDataList[${stuts.index }].enuRange" value="${poVal.enuRange}" type="text" maxlength="50" ignore="ignore"/>
											</td>
											<td>
												<textarea name="baseDeviceDataList[${stuts.index }].remarks" value="${poVal.remarks}" class="" ignore="ignore"></textarea>
											</td>
										</tr>
									</c:forEach>
								</c:if>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
<table style="display: none">
	<tbody id="baseDeviceData_table_template">
	<tr>
		<td class="form-ck">
			<input type="checkbox" name="ck"/>
		</td>
		<td>
			<input name="baseDeviceDataList[#index#].name" type="text" maxlength="32" datatype="*" ignore="checked"/>
		</td>
		<td>
			<input name="baseDeviceDataList[#index#].field" type="text" maxlength="32" datatype="*" ignore="checked"/>
		</td>
		<td>
			<input name="baseDeviceDataList[#index#].unit" type="text" maxlength="32" datatype="*" ignore="checked"/>
		</td>
		<td>
			<t:dictSelect field="baseDeviceDataList[#index#].rwType" type="select" hasLabel="false" title="读写类型" extendJson="{class:'w80'}" datatype="*" typeGroupCode="readWrite"></t:dictSelect>
		</td>
		<td>
			<t:dictSelect field="baseDeviceDataList[#index#].dataType" type="select" hasLabel="false" title="数据类型" extendJson="{class:'w80'}" datatype="*" typeGroupCode="bDataType"></t:dictSelect>
		</td>
		<td>
			<input name="baseDeviceDataList[#index#].dataRange" type="text" maxlength="50" datatype="*" ignore="ignore"/>
		</td>
		<td>
			<input name="baseDeviceDataList[#index#].normalDataRange" type="text" maxlength="50" datatype="*" ignore="ignore"/>
		</td>
		<td>
			<input name="baseDeviceDataList[#index#].accuracy" type="text" maxlength="5" datatype="n" ignore="ignore"/>
		</td>
		<td>
			<input name="baseDeviceDataList[#index#].enuRange" type="text" maxlength="50" ignore="ignore"/>
		</td>
		<td>
			<textarea name="baseDeviceDataList[#index#].remarks" class="" ignore="ignore"></textarea>
		</td>
	</tr>
	</tbody>
</table>
<script type="text/javascript">
	$(document).ready(function () {
		var type = $('input[name=type]:checked').val();
		if (type === 'nsdevice') {
			$('select[name=comMethod]').attr('disabled', 'disabled');
			$('select[name=dataFormat]').attr('disabled', 'disabled');
			$('select[name=comProtocol]').attr('disabled', 'disabled');
			$('select[name=workModel]').attr('disabled', 'disabled');
			$('input[name=requestTime]').attr('disabled', 'disabled');
            deleteAllRows('baseDeviceData_table');
		} else {
			$('select[name=comMethod]').removeAttr('disabled');
			$('select[name=dataFormat]').removeAttr('disabled');
			$('select[name=comProtocol]').removeAttr('disabled');
			$('select[name=workModel]').removeAttr('disabled');
			$('input[name=requestTime]').removeAttr('disabled');
		}
		formControlInit();
		//表单提交
		$("#formobj").Validform({
			tiptype: function (msg, o, cssctl) {
				if (o.type == 3) {
					var oopanel = $(o.obj).closest(".tab-pane");
					var a = 0;
					if (oopanel.length > 0) {
						var panelID = oopanel.attr("id");
						if (!!panelID) {
							var waitActive = $('#subTabs a[href="#' + panelID + '"]');
							if (!waitActive.hasClass("active")) {
								waitActive.tab('show')
								a = 1;
							}
						}
					}
					if (a == 1) {
						setTimeout(function () {
							validationMessage(o.obj, msg);
						}, 366);
					} else {
						validationMessage(o.obj, msg);
					}
				} else {
					removeMessage(o.obj);
				}
			},
			btnSubmit: "#btn_sub",
			btnReset: "#btn_reset",
			ajaxPost: true,
			beforeSubmit: function (curform) {
				var type = $('input[name=type]:checked').val();
				if (type === 'nsdevice') {
					$('select[name=comMethod]').removeAttr('disabled');
					$('select[name=dataFormat]').removeAttr('disabled');
					$('select[name=comProtocol]').removeAttr('disabled');
					$('select[name=workModel]').removeAttr('disabled');
					$('input[name=requestTime]').removeAttr('disabled');
				} else {
					if ($('select[name=comMethod]').val() === '') {
						validationMessage($('select[name=comMethod]'), '请选择通讯方式！');
						return false;
					}
					if ($('select[name=dataFormat]').val() === '') {
						validationMessage($('select[name=dataFormat]'), '请选择数据格式！');
						return false;
					}
					if ($('select[name=comProtocol]').val() === '') {
						validationMessage($('select[name=comProtocol]'), '请选择通讯协议！');
						return false;
					}
					if ($('select[name=workModel]').val() === '') {
						validationMessage($('select[name=workModel]'), '请选择工作模式！');
						return false;
					}
					if ($('input[name=requestTime]').val() === '') {
						validationMessage($('input[name=requestTime]'), '请输入轮询时间！');
						return false;
					}
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
		//查看模式情况下,删除和上传附件功能禁止使用
		if (location.href.indexOf("load=detail") != -1) {
			$(".form-tb-toolbar").hide();
			$("#formobj").find(":input").attr("readonly", "readonly");
		}
	});

	function formControlInit() {
		//单选框/多选框初始化
		$('.i-checks').iCheck({
			labelHover: false,
			cursor: true,
			checkboxClass: 'icheckbox_square-green',
			radioClass: 'iradio_square-green',
			increaseArea: '20%'
		});

		$('input[name=type]').on('ifChecked', function () {
			var type = $(this).val();
			if (type === 'nsdevice') {
				$('select[name=comMethod]').val('');
				$('select[name=dataFormat]').val('');
				$('select[name=comProtocol]').val('');
				$('select[name=workModel]').val('');
				$('input[name=requestTime]').val(0);
				$('input[name=workModel]').val('');
				$('select[name=comMethod]').attr('disabled', 'disabled');
				$('select[name=dataFormat]').attr('disabled', 'disabled');
				$('select[name=comProtocol]').attr('disabled', 'disabled');
				$('select[name=workModel]').attr('disabled', 'disabled');
				$('input[name=requestTime]').attr('disabled', 'disabled');
                deleteAllRows('baseDeviceData_table');
            } else {
				$('select[name=comMethod]').removeAttr('disabled');
				$('select[name=dataFormat]').removeAttr('disabled');
				$('select[name=comProtocol]').removeAttr('disabled');
				$('select[name=workModel]').removeAttr('disabled');
				$('input[name=requestTime]').removeAttr('disabled');
			}
		});
	}

	//初始化下标
	function resetTrNum(tableId) {
		$tbody = $("#" + tableId + "");
		$tbody.find('tbody > tr').each(
			function (i) {
				$(':input, select,button,a', this).each(
					function () {
						var $this = $(this), validtype_str = $this.attr('validType'), name = $this.attr('name'), id = $this.attr('id'), onclick_str = $this
						.attr('onclick'), val = $this.val();
						if (name != null) {
							if (name.indexOf("#index#") >= 0) {
								$this.attr("name", name.replace('#index#', i));
							} else {
								var s = name.indexOf("[");
								var e = name.indexOf("]");
								var new_name = name.substring(s + 1, e);
								$this.attr("name", name.replace("[" + new_name + "]", "[" + i + "]"));
							}
						}
						if (id != null) {
							if (id.indexOf("#index#") >= 0) {
								$this.attr("id", id.replace('#index#', i));
							} else {
								var s = id.indexOf("[");
								var e = id.indexOf("]");
								var new_id = id.substring(s + 1, e);
								$this.attr("id", id.replace(new_id, i));
							}
						}
						if (onclick_str != null) {
							if (onclick_str.indexOf("#index#") >= 0) {
								$this.attr("onclick", onclick_str.replace(/#index#/g, i));
							} else {
							}
						}
						if (validtype_str != null) {
							if (validtype_str.indexOf("#index#") >= 0) {
								$this.attr("validType", validtype_str.replace('#index#', i));
							}
						}
						var class_str = $this.attr("class");
						if (!!class_str && class_str.indexOf("i-checks-tpl") >= 0) {
							$this.attr("class", class_str.replace(/i-checks-tpl/, "i-checks"));
						}
					});
				//$(this).find('div[name=\'xh\']').html(i+1);
			});
	}

	//新增一行
	function addOneRow(tableId) {
		var tr = $("#" + tableId + "_template tr").clone();
		$("#" + tableId).append(tr);
		resetTrNum(tableId);
		formControlInit();
	}

    //删除所选行
    function deleteSelectRows(tableId) {
        $("#" + tableId).find("input[name$='ck']:checked").parent().parent().remove();
        resetTrNum(tableId);
    }

    //删除所有行
    function deleteAllRows(tableId) {
        $("#" + tableId).find(".form-ck").parent().remove();
        resetTrNum(tableId);
    }

	//通用弹出式文件上传
	function onetomanyUpload(inputName) {
		$.dialog({
			content: "url:${webRoot}/systemController.do?commonWebUpload",
			lock: true,
			title: "文件上传",
			zIndex: getzIndex(),
			width: 700,
			height: 200,
			parent: windowapi,
			cache: false,
			ok: function () {
				var iframe = this.iframe.contentWindow;
				var url = iframe.uploadCallback();
				$("input[name='" + inputName + "']").val(url);
				$("input[name='" + inputName + "']").next("a").attr("href", "img/server/" + url + "?down=true").html("下载");
				return true;
			},
			cancelVal: '关闭',
			cancel: function () {
			}
		});
	}
</script>
</body>
</html>