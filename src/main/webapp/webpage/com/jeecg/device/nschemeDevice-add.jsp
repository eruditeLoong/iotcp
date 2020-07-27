<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>设备绑定信息</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">
<t:base type="bootstrap,layer,validform,bootstrap-form,bootstrap-table"></t:base>
</head>
<body>
	<div class="container" style="padding: 20px;">
		<div class="panel panel-info">
			<div class="panel-heading"></div>
			<div class="panel-body">
				<form class="form-horizontal" role="form" id="formobj" action="nschemeDeviceController.do?doAdd" method="POST">
					<input type="hidden" id="btn_sub" class="btn_sub" />
					<input type="hidden" id="id" name="id" />
					<input type="text" id="baseDeviceType" name="baseDeviceType" value="${baseDeviceType }"/>
					<fieldset>
						<legend>用户信息</legend>
						<div class="form-group">
							<div class="bt-item col-md-6 col-sm-6">
								<div class="row">
									<div class="col-md-4 col-sm-4 col-xs-4 bt-label">创建人：</div>
									<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
										<input id="createName" value='${fns:getUserRealnameByUsername(nschemeDevice.createBy) }' type="text" readonly="readonly" class="form-control input-sm" />
									</div>
								</div>
							</div>
							<div class="bt-item col-md-6 col-sm-6">
								<div class="row">
									<div class="col-md-4 col-sm-4 col-xs-4 bt-label">创建时间：</div>
									<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
										<input id="createDate" name="createDate" value='<fmt:formatDate value='${nschemeDevice.createDate}' type="date" pattern="yyyy-MM-dd HH:mm:ss"/>' type="text"
											readonly="readonly" class="form-control input-sm" />
									</div>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="bt-item col-md-6 col-sm-6">
								<div class="row">
									<div class="col-md-4 col-sm-4 col-xs-4 bt-label">所属部门：</div>
									<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
										<input id="sysOrgName" value='${fns:getDepartnameByOrgcode(nschemeDevice.sysOrgCode) }' type="text" readonly="readonly" class="form-control input-sm" />
									</div>
								</div>
							</div>
							<div class="bt-item col-md-6 col-sm-6">
								<div class="row">
									<div class="col-md-4 col-sm-4 col-xs-4 bt-label">所属公司：</div>
									<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
										<input value="${fns:getDepartnameByOrgcode(nschemeDevice.sysCompanyCode)}" type="text" class="form-control input-sm" readonly="readonly" />
									</div>
								</div>
							</div>
						</div>
					</fieldset>
					<fieldset>
						<legend>设备绑定信息</legend>
						<div class="row">
							<div class="bt-item col-md-12 col-sm-12">
								<div class="row">
									<div class="col-md-2 col-sm-2 col-xs-4 bt-label">设备标题：</div>
									<div class="col-md-10 col-sm-10 col-xs-8 bt-content">
										<input name="title" type="text" hasLabel="false" title="设备标题" class="form-control input-sm" value="${nschemeDevice.title }" />
									</div>
								</div>
							</div>
							<div class="bt-item col-md-6 col-sm-6">
								<div class="row">
									<div class="col-md-4 col-sm-4 col-xs-4 bt-label">设备：</div>
									<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
										<div class="input-group">
											<span class="input-group-btn">
												<a class="btn btn-info input-sm">
													<span class="fa fa-hand-o-right"></span>
												</a>
											</span>
											<t:dictSelect id="deviceBy" field="deviceBy" type="select" hasLabel="false" title="设备" extendJson="{class:'form-control input-sm'}" datatype="*"
												dictTable="jform_base_device" dictField="id" dictText="name"></t:dictSelect>
										</div>
									</div>
								</div>
							</div>
							<div class="bt-item col-md-6 col-sm-6">
								<div class="row">
									<div class="col-md-4 col-sm-4 col-xs-4 bt-label">设备编号：</div>
									<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
										<input name="deviceCode" type="text" class="form-control input-sm" maxlength="36" ignore="ignore" />
									</div>
								</div>
							</div>
							<div class="bt-item col-md-6 col-sm-6">
								<div class="row">
									<div class="col-md-4 col-sm-4 col-xs-4 bt-label">所属方案：</div>
									<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
										<div class="input-group" style="width: 100%">
											<span class="input-group-btn">
												<button class="btn btn-info input-sm" type="button">
													<span class="fa fa-hand-o-right"></span>
												</button>
											</span>
											<t:dictSelect id="schemeBy" field="schemeBy" type="select" hasLabel="false" title="所属方案" extendJson="{class:'form-control input-sm'}" dictTable="jform_network_scheme" dictField="id"
												dictText="name" defaultVal="${nschemeDevice.schemeBy}"></t:dictSelect>
										</div>
									</div>
								</div>
							</div>
							<div class="bt-item col-md-6 col-sm-6">
								<div class="row">
									<div class="col-md-4 col-sm-4 col-xs-4 bt-label">所属网关：</div>
									<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
										<div class="input-group" style="width: 100%">
											<span class="input-group-btn">
												<button class="btn btn-info input-sm" type="button">
													<span class="fa fa-hand-o-right"></span>
												</button>
											</span>
											<t:dictSelect id="gatewayBy" field="gatewayBy" type="select" hasLabel="false" title="所属网关" extendJson="{class:'form-control input-sm'}" dictTable="jform_nscheme_device" dictField="id"
												dictText="title" defaultVal="${nschemeDevice.gatewayBy }"></t:dictSelect>
										</div>
									</div>
								</div>
							</div>
							<div class="bt-item col-md-6 col-sm-6">
								<div class="row">
									<div class="col-md-4 col-sm-4 col-xs-4 bt-label">位置信息：</div>
									<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
										<input name="gisInfo" type="text" class="form-control input-sm" maxlength="36" ignore="ignore" />
									</div>
								</div>
							</div>
							<div class="bt-item col-md-6 col-sm-6">
								<div class="row">
									<div class="col-md-4 col-sm-4 col-xs-4 bt-label">状态：</div>
									<div class="col-md-8 col-sm-8 col-xs-8 bt-content">
										<div style="padding-top: 5px">
											<t:dictSelect field="status" extendJson="{class:'i-checks'}" defaultVal="1" type="radio" hasLabel="false" title="状态" typeGroupCode="isActive"></t:dictSelect>
										</div>
									</div>
								</div>
							</div>
							<div class="bt-item col-md-12 col-sm-12">
								<div class="row">
									<div class="col-md-2 col-sm-2 col-xs-4 bt-label">备注：</div>
									<div class="col-md-10 col-sm-10 col-xs-8 bt-content">
										<textarea name="remark" class="form-control input-sm" rows="6" ignore="ignore"></textarea>
										<span class="Validform_checktip" style="float: left; height: 0px;"></span>
										<label class="Validform_label" style="display: none">备注</label>
									</div>
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
			
			// 	
			var baseDeviceType = $("#baseDeviceType").val();
			$("#gatewayBy").attr("disabled", true);
			$("#schemeBy").attr("disabled", true);

			// 设备选择后
			$("#deviceBy").on("change", function() {
				var deviceBy = this.value;
				$.ajax({
					url : "baseDeviceController.do?getDeviceById",
					data: {"id" : deviceBy},
					dataType:"json",
					error: function(xhr){
						
					},
					success : function(result) {
						if(result.success){
							var type = result.obj.type;
							if(type === "terminal"){
								if(baseDeviceType === "terminal"){	// 添加直连设备时，提示不能选择网关，并且方案和网关选择都继续禁用
									
								}else if(baseDeviceType === "gateway"){
									$("#schemeBy").attr("disabled", true);
									$("#gatewayBy").attr("disabled", false);
								}else{
									
								}
							}else if(type === "gateway"){
								if(baseDeviceType === "terminal"){	// 添加直连设备时，提示不能选择网关，并且方案和网关选择都继续禁用
									layer.alert("请选择终端设备！", {
										skin: 'layui-layer-molv',
										icon: 0	// icon
									});
									$("#deviceBy").val(-1);
									return 0;
								}else if(baseDeviceType === "gateway"){
								}else{
									
								}
							}
						}else{
							alert(result.msg);
						}
					}
				});
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
					/* 提交前把disabled的选项都解除，才能参加提交动作 */
					$("#gatewayBy").attr("disabled", false);
					$("#schemeBy").attr("disabled", false);
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