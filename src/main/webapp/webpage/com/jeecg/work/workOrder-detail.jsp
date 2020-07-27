<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>工作票管理</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">
<t:base type="bootstrap,layer,validform,webuploader,bootstrap-form,bootstrap-table"></t:base>
<script src="plug-in/vue/vue.js"></script>

<style type="text/css">
[v-cloak] {
	display: none;
}
</style>
</head>
<body style="overflow: hidden; overflow-y: auto;">
	<div class="container" style="width: 100%;">
		<div class="panel-heading"></div>
		<div class="panel-body">
			<form class="form-horizontal" role="form" id="formobj" action="workOrderController.do?doUpdate" method="POST">
				<input type="hidden" id="btn_sub" class="btn_sub" />
				<input type="hidden" id="id" name="id" value="${workOrder.id}" />
				<div class="form-group">
					<label for="title" class="col-sm-3 control-label">标题：</label>
					<div class="col-sm-7">
						<div class="input-group" style="width: 100%">
							<input id="title" name="title" value='${workOrder.title}' type="text" readonly="readonly" maxlength="50" class="form-control input-sm" placeholder="请输入标题" datatype="*"
								ignore="checked" />
						</div>
					</div>
				</div>
				<div class="form-group">
					<label for="orderFiles" class="col-sm-3 control-label">工作票扫描件：</label>
					<div class="col-sm-7">
						<div class="input-group" style="width: 100%">
							<t:webUploader name="orderFiles" outJs="true" auto="false" swfTransform="true" readOnly="true" showImgDiv="filediv_orderFiles" type="image" buttonText='添加图片' displayTxt="false"
								pathValues="${workOrder.orderFiles}" datatype="*"></t:webUploader>
							<div class="form" id="filediv_orderFiles"></div>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label for="sysOrgCode" class="col-sm-3 control-label">厂家名称：</label>
					<div class="col-sm-7">
						<div style="width: 100%;padding:5px;border: 0;border: 1px solid;border-color:rgb(76,179,214);background-color:rgb(218,243,254);border-radius:3px;">
							<table style="color:rgb(76,179,214)">
								<tbody id="tableList">
									<tr v-for="(item, index) in items" v-cloak>
										<td style="width:20px;">{{index+1}}、</td>
										<td>
											<a href="javascript:;" v-on:click="detailUser(item.id)">{{item.name}}</a>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
						<script type="text/javascript">
							var wid = '${workOrder.id}';
							var orgs = '${workOrder.sysOrgCode}';
							var vue = new Vue({
								el : '#tableList',
								data: {
									items: []
								},
								methods: {
									detailUser: function (bid) {
										var url = "workOrderController.do?userList&branchId=" + bid + "&workId=" + wid;
										openwindow('厂家人员', url, '', 300, 400);
									}
								}
							});
							$.ajax({
								type : "GET",
								url : "workOrderController.do?getDepartList",
								data : {
									ids : orgs,
								},
								dataType : "json",
								success : function(result) {
									// console.log(result);
									vue.items = result.obj;
								}
							});
						</script>
					</div>
				</div>
				<div class="form-group">
					<label for="remark" class="col-sm-3 control-label">备注：</label>
					<div class="col-sm-7">
						<div class="input-group" style="width: 100%">
							<textarea name="remark" class="form-control input-sm" rows="6" readonly="readonly" ignore="ignore">${workOrder.remark}</textarea>
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