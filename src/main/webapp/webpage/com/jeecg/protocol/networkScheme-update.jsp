<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>组网方案</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">
<t:base type="bootstrap,layer,validform,bootstrap-form"></t:base>
</head>
<body>
	<div class="container" style="padding-top: 20px;">
		<div class="panel panel-info">
			<div class="panel-heading"></div>
			<div class="panel-body">
				<form class="form-horizontal" role="form" id="formobj" action="networkSchemeController.do?doUpdate" method="POST">
					<input type="hidden" id="btn_sub" class="btn_sub" />
					<input type="hidden" id="id" name="id" value="${networkScheme.id}" />
					<fieldset>
						<legend>用户信息</legend>
						<div class="form-group">
							<div class="bt-item col-md-6 col-sm-6">
								<div class="row">
									<div class="col-md-3 col-sm-3 col-xs-3 bt-label">创建人：</div>
									<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
										<input id="createName" value='${fns:getUserRealnameByUsername(networkScheme.createBy) }' type="text" readonly="readonly" class="form-control input-sm" />
									</div>
								</div>
							</div>
							<div class="bt-item col-md-6 col-sm-6">
								<div class="row">
									<div class="col-md-3 col-sm-3 col-xs-3 bt-label">创建时间：</div>
									<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
										<input id="createDate" value='<fmt:formatDate value='${networkScheme.createDate}' type="date" pattern="yyyy-MM-dd HH:mm:ss"/>' class="form-control input-sm" type="text"
											readonly="readonly" maxlength="80" class="form-control input-sm" />
									</div>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="bt-item col-md-6 col-sm-6">
								<div class="row">
									<div class="col-md-3 col-sm-3 col-xs-3 bt-label">所属部门：</div>
									<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
										<input value="${fns:getDepartnameByOrgcode(networkScheme.sysOrgCode)}" type="text" class="form-control input-sm" ignore="ignore" readonly="readonly" />
									</div>
								</div>
							</div>
							<div class="bt-item col-md-6 col-sm-6">
								<div class="row">
									<div class="col-md-3 col-sm-3 col-xs-3 bt-label">所属公司：</div>
									<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
										<input value="${fns:getDepartnameByOrgcode(networkScheme.sysCompanyCode)}" type="text" class="form-control input-sm" ignore="ignore" readonly="readonly" />
									</div>
								</div>
							</div>
						</div>
						<c:if test="${not empty networkScheme.updateBy}">
							<div class="form-group">
								<div class="bt-item col-md-6 col-sm-6">
									<div class="row">
										<div class="col-md-3 col-sm-3 col-xs-3 bt-label">更新人：</div>
										<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
											<input type="text" class="form-control input-sm" readonly="readonly" value="${fns:getUserRealnameByUsername(networkScheme.updateBy)}" ignore="ignore" readonly="readonly" />
										</div>
									</div>
								</div>
								<div class="bt-item col-md-6 col-sm-6">
									<div class="row">
										<div class="col-md-3 col-sm-3 col-xs-3 bt-label">更新日期：</div>
										<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
											<input name="updateDate" type="text" class="form-control input-sm laydate-datetime"
												value="<fmt:formatDate pattern='yyyy-MM-dd HH:mm:ss' type='both' value='${networkScheme.updateDate}'/>" readonly="readonly" ignore="ignore" readonly="readonly"/>
										</div>
									</div>
								</div>
							</div>
						</c:if>
					</fieldset>
					<fieldset>
						<legend>方案信息</legend>
						<div class="form-group">
							<label for="name" class="col-sm-3 control-label">方案名称：</label>
							<div class="col-sm-7">
								<div class="input-group" style="width: 100%">
									<input id="name" name="name" value='${networkScheme.name}' type="text" maxlength="32" class="form-control input-sm" placeholder="请输入方案名称" datatype="*" ignore="checked" />
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="connectionModeBy" class="col-sm-3 control-label">接入模式：</label>
							<div class="col-sm-7">
								<div class="input-group" style="width: 100%">
									<t:dictSelect field="connectionModeBy" type="list" extendJson="{class:'form-control input-sm'}" datatype="*" dictTable="jform_connection_mode" dictField="code"
										dictText="name" hasLabel="false" title="接入模式" defaultVal="${networkScheme.connectionModeBy}"></t:dictSelect>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="netModeBy" class="col-sm-3 control-label">通讯方式：</label>
							<div class="col-sm-7">
								<div class="input-group" style="width: 100%">
									<t:dictSelect field="netModeBy" type="list" extendJson="{class:'form-control input-sm'}" datatype="*" dictTable="jform_net_mode" dictField="code" dictText="name"
										hasLabel="false" title="通讯方式" defaultVal="${networkScheme.netModeBy}"></t:dictSelect>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="remark" class="col-sm-3 control-label">备注：</label>
							<div class="col-sm-7">
								<div class="input-group" style="width: 100%">
									<textarea name="remark" class="form-control input-sm" rows="6" ignore="ignore">${networkScheme.remark}</textarea>
									<span class="Validform_checktip" style="float: left; height: 0px;"></span>
									<label class="Validform_label" style="display: none">备注</label>
								</div>
							</div>
						</div>
					</fieldset>
				</form>
			</div>
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