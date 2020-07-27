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
</head>
<body>
	<div class="container" style="width: 100%; overflow-x: hidden">
		<div class="panel panel-default">
			<div class="panel-heading"></div>
			<div class="panel-body">
				<form class="form-horizontal" role="form" id="formobj" action="comProtocolController.do?doUpdate" method="POST">
					<input type="hidden" id="btn_sub" class="btn_sub" />
					<input type="hidden" id="id" name="id" value="${comProtocolPage.id}" />
					<fieldset>
						<legend>通讯协议</legend>
						<div class="main-form">
							<div class="row">
								<div class="bt-item col-md-6 col-sm-6">
									<div class="row">
										<div class="col-md-3 col-sm-3 col-xs-3 bt-label">协议名称：</div>
										<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
											<input name="name" value="${comProtocolPage.name}" type="text" class="form-control input-sm" maxlength="32" datatype="*" ignore="checked" />
										</div>
									</div>
								</div>
								<div class="bt-item col-md-6 col-sm-6">
									<div class="row">
										<div class="col-md-3 col-sm-3 col-xs-3 bt-label">协议code：</div>
										<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
											<input name="code" value="${comProtocolPage.code}" type="text" class="form-control input-sm" maxlength="32" validType="jform_com_protocol,code,id" datatype="*"
												ignore="checked" />
										</div>
									</div>
								</div>
								<div class="bt-item col-md-6 col-sm-6">
									<div class="row">
										<div class="col-md-3 col-sm-3 col-xs-3 bt-label">校验类型：</div>
										<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
											<t:dictSelect field="checkType" defaultVal="${comProtocolPage.checkType}" type="select" hasLabel="false" title="校验类型" extendJson="{class:'form-control input-sm'}"
												datatype="*" dictTable="jform_com_check" dictField="code" dictText="name"></t:dictSelect>
										</div>
									</div>
								</div>
								<div class="bt-item col-md-6 col-sm-6">
									<div class="row">
										<div class="col-md-3 col-sm-3 col-xs-3 bt-label">数据模式：</div>
										<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
											<div style="padding-top: 5px">
												<t:dictSelect field="dataMode" defaultVal="${comProtocolPage.dataMode}" extendJson="{class:'i-checks'}" type="radio" hasLabel="false" title="数据模式"
													typeGroupCode="data_mode"></t:dictSelect>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</fieldset>

					<ul class="nav nav-tabs" style="margin-bottom: 0" id="subTabs">
						<li class="active">
							<a href="#comConfig" data-toggle="tab">协议配置</a>
						</li>
					</ul>
					<div class="tab-content" style="background-color: #fff; padding-top: 10px;">
						<div class="tab-pane fade in active" id="comConfig">
							<div class="form-tb-toolbar">
								<button onclick="addOneRow('comConfig_table')" type="button" class="btn btn-default btn-sm">
									<span class="glyphicon glyphicon-plus"></span>
									&nbsp;添加
								</button>
								<button onclick="deleteSelectRows('comConfig_table')" type="button" class="btn btn-default btn-sm">
									<span class="glyphicon glyphicon-minus"></span>
									&nbsp;删除
								</button>
							</div>
							<div style="overflow-x: auto">
								<table class="table subinfo-table" id="comConfig_table">
									<thead>
										<tr>
											<th align="center" style="width: 25px;"></th>
											<th>参数标题</th>
											<th>参数变量名</th>
											<th>参数变量值</th>
											<th>备注</th>
									</thead>
									<tbody>
										<c:if test="${fn:length(comProtocolPage.comConfigList)  > 0 }">
											<c:forEach items="${comProtocolPage.comConfigList}" var="poVal" varStatus="stuts">
												<tr>
													<td class="form-ck">
														<input style="" type="checkbox" name="ck" />
														<input name="comConfigList[${stuts.index }].id" value="${poVal.id}" type="hidden" />
													</td>
													<td>
														<input name="comConfigList[${stuts.index }].title" value="${poVal.title}" type="text" class="form-control input-sm" maxlength="32" datatype="*" ignore="checked" />
													</td>
													<td>
														<input name="comConfigList[${stuts.index }].name" value="${poVal.name}" type="text" class="form-control input-sm" maxlength="32" datatype="s" ignore="checked" />
													</td>
													<td>
														<input name="comConfigList[${stuts.index }].value" value="${poVal.value}" type="text" class="form-control input-sm" maxlength="32" datatype="*" ignore="checked" />
													</td>
													<td>
														<textarea name="comConfigList[${stuts.index }].remark" value="${poVal.remark}" class="form-control input-sm" rows="1" ignore="ignore"></textarea>
													</td>
												</tr>
											</c:forEach>
										</c:if>
										<c:if test="${fn:length(comProtocolPage.comConfigList) <= 0 }">
											<tr>
												<td class="form-ck">
													<input type="checkbox" name="ck" />
												</td>
												<td>
													<input name="comConfigList[0].title" type="text" class="form-control input-sm" maxlength="32" datatype="*" ignore="checked" />
												</td>
												<td>
													<input name="comConfigList[0].name" type="text" class="form-control input-sm" maxlength="32" datatype="s" ignore="checked" />
												</td>
												<td>
													<input name="comConfigList[0].value" type="text" class="form-control input-sm" maxlength="32" datatype="*" ignore="checked" />
												</td>
												<td>
													<textarea name="comConfigList[0].remark" class="form-control input-sm" rows="1" ignore="ignore"></textarea>
												</td>
											</tr>
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
		<tbody id="comConfig_table_template">
			<tr>
				<td class="form-ck">
					<input type="checkbox" name="ck" />
				</td>
				<td>
					<input name="comConfigList[#index#].title" type="text" class="form-control input-sm" maxlength="32" datatype="*" ignore="checked" />
				</td>
				<td>
					<input name="comConfigList[#index#].name" type="text" class="form-control input-sm" maxlength="32" datatype="s" ignore="checked" />
				</td>
				<td>
					<input name="comConfigList[#index#].value" type="text" class="form-control input-sm" maxlength="32" datatype="*" ignore="checked" />
				</td>
				<td>
					<textarea name="comConfigList[#index#].remark" class="form-control input-sm" rows="1" ignore="ignore"></textarea>
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
			//查看模式情况下,删除和上传附件功能禁止使用
			if (location.href.indexOf("load=detail") != -1) {
				$(".form-tb-toolbar").hide();
				$("#formobj").find(":input").attr("readonly", "readonly");
			}
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