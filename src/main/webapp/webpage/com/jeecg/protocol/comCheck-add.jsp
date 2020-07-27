<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>数据校验</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">
<t:base type="bootstrap,layer,validform,bootstrap-form"></t:base>
</head>
<body>
	<div class="container" style="padding: 20px;">
		<div class="panel panel-info">
			<div class="panel-heading"></div>
			<div class="panel-body">
				<form class="form-horizontal" role="form" id="formobj" action="comCheckController.do?doAdd" method="POST">
					<input type="hidden" id="btn_sub" class="btn_sub" />
					<input type="hidden" id="id" name="id" />
					<fieldset>
						<legend>用户信息</legend>
						<div class="form-group">
							<label for="title" class="col-sm-2 control-label">创建人：</label>
							<div class="col-sm-3">
								<div class="input-group" style="width: 100%">
									<input id="createName" value='${fns:getUserRealnameByUsername(comCheck.createBy) }' type="text" readonly="readonly" class="form-control input-sm" />
									<input id="createBy" name="createBy" value='${comCheck.createBy}' type="hidden" readonly="readonly" maxlength="80" class="form-control input-sm" datatype="*"
										ignore="checked" />
								</div>
							</div>
							<label for="title" class="col-sm-2 control-label">创建时间：</label>
							<div class="col-sm-3">
								<div class="input-group" style="width: 100%">
									<input id="createDate" name="createDate" value='<fmt:formatDate value='${comCheck.createDate}' type="date" pattern="yyyy-MM-dd HH:mm:ss"/>' type="text"
										readonly="readonly" maxlength="80" class="form-control input-sm" />
								</div>
							</div>
						</div>
					</fieldset>
					<fieldset>
						<legend>校验信息</legend>
						<div class="form-group">
							<label for="name" class="col-sm-3 control-label">检验名称：</label>
							<div class="col-sm-7">
								<div class="input-group" style="width: 100%">
									<input id="name" name="name" type="text" maxlength="32" class="form-control input-sm" placeholder="请输入检验名称" datatype="*" ignore="checked" />
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="code" class="col-sm-3 control-label">校验CODE：</label>
							<div class="col-sm-7">
								<div class="input-group" style="width: 100%">
									<input id="code" name="code" type="text" maxlength="32" class="form-control input-sm" placeholder="请输入校验CODE" datatype="s" ignore="checked" />
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="num" class="col-sm-3 control-label">顺序：</label>
							<div class="col-sm-7">
								<div class="input-group" style="width: 100%">
									<input id="num" name="num" type="text" maxlength="2" class="form-control input-sm" placeholder="请输入顺序" datatype="n" ignore="checked" />
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="remark" class="col-sm-3 control-label">描述：</label>
							<div class="col-sm-7">
								<textarea name="remark" class="form-control input-sm" rows="6" ignore="ignore"></textarea>
								<span class="Validform_checktip" style="float: left; height: 0px;"></span>
								<label class="Validform_label" style="display: none">描述</label>
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