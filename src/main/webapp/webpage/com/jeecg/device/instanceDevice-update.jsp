<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>应用设备</title>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<style type="text/css">
.combo_self {
	height: 22px !important;
	width: 150px !important;
}

.layout-header .btn {
	margin: 0;
	float: none !important;
}

.btn-default {
	height: 35px;
	line-height: 35px;
	font-size: 14px;
}
</style>

<script type="text/javascript">
	$(function() {
		$(".combo").removeClass("combo").addClass("combo combo_self");
		$(".combo").each(function() {
			$(this).parent().css("line-height", "0px");
		});
	});

	/**树形列表数据转换**/
	function convertTreeData(rows, textField) {
		for (var i = 0; i < rows.length; i++) {
			var row = rows[i];
			row.text = row[textField];
			if (row.children) {
				row.state = "open";
				convertTreeData(row.children, textField);
			}
		}
	}
	/**树形列表加入子元素**/
	function joinTreeChildren(arr1, arr2) {
		for (var i = 0; i < arr1.length; i++) {
			var row1 = arr1[i];
			for (var j = 0; j < arr2.length; j++) {
				if (row1.id == arr2[j].id) {
					var children = arr2[j].children;
					if (children) {
						row1.children = children;
					}
				}
			}
		}
	}
</script>
</head>
<body>
	<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="div" action="instanceDeviceController.do?doUpdate">
		<input id="id" name="id" type="hidden" value="${instanceDevicePage.id }" />
		<input id="createBy" name="createBy" type="hidden" value="${instanceDevicePage.createBy }" />
		<input id="createDate" name="createDate" type="hidden" value="${instanceDevicePage.createDate }" />
		<input id="updateBy" name="updateBy" type="hidden" value="${instanceDevicePage.updateBy }" />
		<input id="updateDate" name="updateDate" type="hidden" value="${instanceDevicePage.updateDate }" />
		<input id="sysOrgCode" name="sysOrgCode" type="hidden" value="${instanceDevicePage.sysOrgCode }" />
		<input id="sysCompanyCode" name="sysCompanyCode" type="hidden" value="${instanceDevicePage.sysCompanyCode }" />
		<fieldset class="step">
			<div class="form">
				<label class="Validform_label">名称:</label>
				<input id="name" name="name" type="text" maxlength="32" style="width: 150px" class="inputxt" datatype="*" ignore="checked" value='${instanceDevicePage.name}' />
				<span class="Validform_checktip"></span>
			</div>
			<div class="form">
				<label class="Validform_label">编码:</label>
				<input id="code" name="code" type="text" maxlength="32" style="width: 150px" class="inputxt" validType="jform_instance_device,code,id" datatype="*" ignore="checked"
					value='${instanceDevicePage.code}' />
				<span class="Validform_checktip"></span>
			</div>
			<div class="form">
				<label class="Validform_label">设备层级:</label>
				<t:dictSelect field="level" id="level" type="select" datatype="*" typeGroupCode="devLevel" defaultVal="${instanceDevicePage.level}" hasLabel="false" title="设备层级"></t:dictSelect>
				<span class="Validform_checktip"></span>
			</div>
			<div class="form" id="pdevice">
				<label class="Validform_label">上级设备:</label>
				<input id="parentBy" name="parentBy" type="text" style="width: 150px" class="inputxt easyui-combotree" ignore="ignore"
					data-options="panelHeight:'220',
				                    url: 'instanceDeviceController.do?datagrid&field=id,name',  
				                    loadFilter: function(data) {
				                    	var rows = data.rows || data;
				                    	var win = frameElement.api.opener;
				                    	var listRows = win.getDataGrid().treegrid('getData');
				                    	joinTreeChildren(rows, listRows);
				                    	convertTreeData(rows, 'name');
				                    	return rows; 
				                    },
				                    onSelect:function(node){
				                    	$('#parentBy').val(node.id);
				                    },
				                    onLoadSuccess: function() {
				                    	var win = frameElement.api.opener;
				                    	var currRow = win.getDataGrid().treegrid('getSelected');
			                    		//编辑时，选择当前父菜单
			                    		if(currRow) {
			                    			$('#parentBy').combotree('setValue', currRow.parentBy);
				                    		if(currRow.parentBy.length==0){
				                    			$('#level').val(0);
				                    			$('#pdevice').hide();
				                    		}else{
				                    			$('#level').val(1);
				                    			$('#pdevice').show();
				                    		}
			                    		}else{
			                    			alert(88);
			                    		}
				                    } " />
				<span class="Validform_checktip"></span>
			</div>
			<div class="form">
				<label class="Validform_label">基础设备:</label>
				<input id="baseDeviceBy" name="baseDeviceBy" type="hidden" readonly="readonly" value='${instanceDevicePage.baseDeviceBy}' />
				<input id="baseDeviceName" name="baseDeviceName" type="text" class="form-control inputxt" ignore="checked" datatype="*"
					onClick="popupClick(this,'id,name,model_file','baseDeviceBy,baseDeviceName,modelFile','base_device_msg');"
					value='${fns:getBaseDeviceNameById(instanceDevicePage.baseDeviceBy)}' />
				<span class="Validform_checktip"></span>
			</div>
			<div class="form">
				<label class="Validform_label">模型文件：</label>
				<input id="modelFile" name="modelFile" type="text" readonly="readonly" onclick="viewModel(this.value);"
					style="width: 350px; border: 0; background-color: transparent; color: blue; cursor: pointer;" value='${instanceDevicePage.modelFile}' />
				<span class="Validform_checktip"></span>
				<script type="text/javascript">
					function viewModel(url) {
						var url = "instanceDeviceController.do?goViewModel&modelUrl=" + url;
						openwindow('模型展示', url, "", 600, 400);
					}
				</script>
			</div>
			<div class="form">
				<label class="Validform_label">位置坐标:</label>
				<input id="coordinate" name="coordinate" type="text" maxlength="50" style="width: 150px" class="inputxt" ignore="ignore" value='${instanceDevicePage.coordinate}' />
				<span class="Validform_checktip"></span>
			</div>
			<div class="form">
				<label class="Validform_label">状态:</label>
				<t:dictSelect field="status" type="radio" datatype="n" typeGroupCode="isActive" defaultVal="${instanceDevicePage.status}" hasLabel="false" title="状态"></t:dictSelect>
				<span class="Validform_checktip"></span>
			</div>
			<div class="form">
				<label class="Validform_label">备注:</label>
				<textarea id="remark" style="width: 600px;" class="inputxt" rows="6" name="remark" ignore="ignore">${instanceDevicePage.remark}</textarea>
				<span class="Validform_checktip"></span>
			</div>
		</fieldset>
	</t:formvalid>
</body>
<script src="webpage/com/jeecg/device/instanceDevice.js"></script>