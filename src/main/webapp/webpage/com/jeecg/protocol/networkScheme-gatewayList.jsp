<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
	<div region="center" style="padding: 0px; border: 0px">
		<t:datagrid name="networkSchemeDeviceList" actionUrl="nschemeDeviceController.do?datagrid&schemeBy=${schemeBy } "
			treegrid="true" treeField="title" fit="true" fitColumns="true" idField="id" btnCls="bootstrap btn btn-info btn-xs" checkbox="true">
			<t:dgCol title="主键" field="id" hidden="true" queryMode="single"></t:dgCol>
			<t:dgCol title="设备标题" field="title" query="true" queryMode="single"></t:dgCol>
			<t:dgCol title="所属方案" field="schemeBy" hidden="true" queryMode="single" dictionary="jform_network_scheme,id,name"></t:dgCol>
			<t:dgCol title="所属网关" field="gatewayBy" hidden="true" queryMode="single" dictionary="jform_nscheme_device,id,title"></t:dgCol>
			<t:dgCol title="网关名称" field="deviceBy" queryMode="single" dictionary="jform_base_device,id,name" width="120"></t:dgCol>
			<t:dgCol title="网关编号" field="deviceCode" hidden="true" queryMode="single" width="100"></t:dgCol>
			<t:dgCol title="位置信息" field="gisInfo" queryMode="single" width="150"></t:dgCol>
			<t:dgCol title="状态" field="status" queryMode="single" dictionary="isActive" width="80"></t:dgCol>

			<t:dgToolBar title="录入" icon="fa fa-plus-square-o" url="baseDeviceController.do?goAdd" funname="add" width="800" height="500"></t:dgToolBar>
			<t:dgToolBar title="导入现有设备" icon="fa fa-plus-square-o" url="nschemeDeviceController.do?goAdd&baseDeviceType=gateway&schemeBy=${schemeBy}" funname="add" width="800" height="500"></t:dgToolBar>
			<t:dgToolBar title="编辑" icon="fa fa-pencil-square-o" url="nschemeDeviceController.do?goUpdate&baseDeviceType=terminal" funname="update" width="800" height="500"></t:dgToolBar>
			<t:dgToolBar title="批量删除" icon="fa fa-minus-square-o" url="nschemeDeviceController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>

			<t:dgCol title="操作" field="opt" align="left" width="120"></t:dgCol>
			<t:dgFunOpt funname="goAddTerminalDrive(id,schemeBy)" title="添加终端终端" urlclass="ace_button" urlfont="fa-wrench" />

		</t:datagrid>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		$("#networkSchemeDeviceList").treegrid({
			onExpand : function(row) {
				var children = $("#networkSchemeDeviceList").treegrid('getChildren', row.id);
				if (children.length <= 0) {
					row.leaf = true;
					$("#networkSchemeDeviceList").treegrid('refresh', row.id);
				}
			}	
		});
	});
	//终端设备
	function goAddTerminalDrive(id,schemeBy) {
		add("添加终端设备", "nschemeDeviceController.do?goAdd&baseDeviceType=terminal&schemeBy="+schemeBy+"&gatewayBy="+id, '',  800,500);
	}
	/**
	 * 获取表格对象
	 * @return 表格对象
	 */
	function getDataGrid(){
		var datagrid = $('#'+gridname);
		return datagrid;
	}
</script>