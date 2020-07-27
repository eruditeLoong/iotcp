<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>主表</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">
<t:base type="bootstrap,layer,validform,bootstrap-form"></t:base>
<script src="${webRoot}/plug-in/lhgDialog/lhgdialog.min.js"></script>
<script src="${webRoot}/plug-in/jquery-plugs/i18n/jquery.i18n.properties.js"></script>
<script src="${webRoot}/plug-in/tools/curdtools.js"></script>
<style type="text/css">
td{
	margin:0px;
	padding:0px;
	width: 100px;
}
td input {
	height: 30px;
	padding: 5px 10px;
	font-size: 12px;
	line-height: 1.5;
	border-radius: 3px;
	border: 1px solid #ccc;
	margin:0px;
	width: 100%
}

td select {
	height: 30px;
	padding: 5px 10px;
	font-size: 12px;
	line-height: 1.5;
	border-radius: 3px;
	border: 1px solid #ccc;
	margin:0px;
	width: 100%
}

td textarea {
	height: 30px;
	padding: 5px 10px;
	font-size: 12px;
	line-height: 1.5;
	border-radius: 3px;
	border: 1px solid #ccc;
	margin:0px;
	width: 100%
}
.table td.form-ck{
	width: 20px;
}
</style>
</head>
<body>
	<div class="container" style="width: 100%; overflow-x: hidden">
		<div class="panel panel-default">
			<div class="panel-heading"></div>
			<div class="panel-body">
				<form class="form-horizontal" role="form" id="formobj" action="dataProtocolController.do?doAdd" method="POST">
					<input type="hidden" id="btn_sub" class="btn_sub" />
					<input type="hidden" id="id" name="id" />
					<fieldset>
						<legend>数据协议</legend>
						<div class="main-form">
							<div class="row">
								<div class="bt-item col-md-6 col-sm-6">
									<div class="row">
										<div class="col-md-3 col-sm-3 col-xs-3 bt-label">名称：</div>
										<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
											<input name="name" type="text" class="form-control input-sm" maxlength="50" datatype="*" ignore="checked" />
										</div>
									</div>
								</div>
								<div class="bt-item col-md-6 col-sm-6">
									<div class="row">
										<div class="col-md-3 col-sm-3 col-xs-3 bt-label">编码：</div>
										<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
											<input name="code" type="text" class="form-control input-sm" maxlength="32" validType="jform_data_protocol,code,id" datatype="*" ignore="checked" />
										</div>
									</div>
								</div>
								<div class="bt-item col-md-6 col-sm-6">
									<div class="row">
										<div class="col-md-3 col-sm-3 col-xs-3 bt-label">类型：</div>
										<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
											<div style="padding-top: 5px">
												<t:dictSelect field="type" id="type" extendJson="{class:'i-checks'}" type="radio" defaultVal="0" typeGroupCode="dataProType"></t:dictSelect>
											</div>
										</div>
									</div>
								</div>
								<div class="bt-item col-md-6 col-sm-6">
									<div class="row">
										<div class="col-md-3 col-sm-3 col-xs-3 bt-label">数据形式：</div>
										<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
											<div style="padding-top: 5px">
												<t:dictSelect field="dataShape" readonly="true" extendJson="{class:'i-checks'}" type="radio" defaultVal="string" typeGroupCode="dataShape"></t:dictSelect>
											</div>
										</div>
									</div>
								</div>
								<div class="bt-item col-md-6 col-sm-6">
									<div class="row">
										<div class="col-md-3 col-sm-3 col-xs-3 bt-label">状态：</div>
										<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
											<div style="padding-top: 5px">
												<t:dictSelect field="status" extendJson="{class:'i-checks'}" type="radio" typeGroupCode="isActive" defaultVal="1"></t:dictSelect>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</fieldset>
					<ul class="nav nav-tabs" style="margin-bottom: 0" id="subTabs">
						<li class="active">
							<a href="#dataCustom" data-toggle="tab">自定义格式</a>
						</li>
					</ul>
					<div class="tab-content" style="background-color: #fff; padding-top: 10px;">
						<div class="tab-pane fade in active" id="dataCustom">
							<div class="form-tb-toolbar">
								<button onclick="addOneRow('dataCustom_table')" type="button" class="btn btn-default btn-sm">
									<span class="glyphicon glyphicon-plus"></span>
									&nbsp;添加
								</button>
								<button onclick="deleteSelectRows('dataCustom_table')" type="button" class="btn btn-default btn-sm">
									<span class="glyphicon glyphicon-minus"></span>
									&nbsp;删除
								</button>
							</div>
							<div style="overflow-x: auto">
								<table class="table subinfo-table" id="dataCustom_table">
									<thead>
										<tr>
											<th align="center" style="width: 25px;"></th>
											<th>节点名称</th>
											<th>节点标识</th>
											<th>节点索引</th>
											<th>正则表达式</th>
											<th>基本类型</th>
											<th>备注</th>
									</thead>
								</table>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
	<table style="display: none">
		<tbody id="dataCustom_table_template">
			<tr>
				<td class="form-ck">
					<input type="checkbox" name="ck" />
				</td>
				<td>
					<input name="dataCustomList[#index#].nodeName" type="text" maxlength="32" datatype="*" ignore="checked" />
				</td>
				<td>
					<input name="dataCustomList[#index#].nodeField" type="text" maxlength="32" datatype="*" ignore="checked" />
				</td>
				<td>
					<input name="dataCustomList[#index#].nodeIndex" type="text" maxlength="10" datatype="*" ignore="ignore" />
				</td>
				<td>
					<input name="dataCustomList[#index#].regEx" type="text" maxlength="100" datatype="*" ignore="ignore" />
				</td>
				<td>
					<t:dictSelect field="dataCustomList[#index#].datatype" type="select" hasLabel="false" extendJson="{class:'w80'}" datatype="*" typeGroupCode="bDataType"></t:dictSelect>
				</td>
				<td>
					<input name="dataCustomList[#index#].remark" type="text" maxlength="120" ignore="ignore" />
				</td>
			</tr>
		</tbody>
	</table>
	<script type="text/javascript">
		$(document).ready(function() {
			formControlInit();
			//表单提交
			$("#formobj").Validform({
				tiptype : function(msg, o, cssctl) {
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
							setTimeout(function() {
								validationMessage(o.obj, msg);
							}, 366);
						} else {
							validationMessage(o.obj, msg);
						}
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
		function formControlInit() {
			//单选框/多选框初始化
			$('.i-checks').iCheck({
				labelHover : false,
				cursor : true,
				checkboxClass : 'icheckbox_square-green',
				radioClass : 'iradio_square-green',
				increaseArea : '20%'
			});

			//方法名称：获取当前选中的值；icheck-radio change事件；点击事件;
			$("input:radio").on('ifChecked', function(event){
				var name = $(this).attr('name');
				var value = $(this).val();
				if(name == 'type'){
					if(value == 1){
						$("input:radio[name='dataShape']").prop('disabled', false);
						addOneRow('dataCustom_table');
					}else{    // 固有格式，删除下面细节，数据形式只能选string
						$("#dataCustom_table").find("input[name$='ck']").parent().parent().remove();
						resetTrNum('dataCustom_table');
						$("input:radio[name='dataShape']:first").iCheck('check');
						$("input:radio[name='dataShape']").prop('disabled', true);
					}
				}else{
					
				}
			});
		}
		//初始化下标
		function resetTrNum(tableId) {
			$tbody = $("#" + tableId + "");
			$tbody.find('tbody > tr').each(
					function(i) {
						$(':input, select,button,a', this).each(
								function() {
									var $this = $(this), validtype_str = $this.attr('validType'), name = $this.attr('name'), id = $this.attr('id'), onclick_str = $this
											.attr('onclick'), val = $this.val();
									if (name != null) {
										if (name.indexOf("#index#") >= 0) {
											$this.attr("name", name.replace('#index#', i));
										} else {
											var s = name.indexOf("[");
											var e = name.indexOf("]");
											var new_name = name.substring(s + 1, e);
											$this.attr("name", name.replace(new_name, i));
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
											var s = onclick_str.indexOf("[");
											var e = onclick_str.indexOf("]");
											var new_onclick_str = onclick_str.substring(s + 1, e);
											$this.attr("onclick", onclick_str.replace(new_onclick_str, i));
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
		//通用弹出式文件上传
		function onetomanyUpload(inputName) {
			$.dialog({
				content : "url:${webRoot}/systemController.do?commonWebUpload",
				lock : true,
				title : "文件上传",
				zIndex : getzIndex(),
				width : 700,
				height : 200,
				parent : windowapi,
				cache : false,
				ok : function() {
					var iframe = this.iframe.contentWindow;
					var url = iframe.uploadCallback();
					$("input[name='" + inputName + "']").val(url);
					$("input[name='" + inputName + "']").next("a").attr("href", "img/server/" + url + "?down=true").html("下载");
					return true;
				},
				cancelVal : '关闭',
				cancel : function() {
				}
			});
		}
	</script>
</body>
</html>