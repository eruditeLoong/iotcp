<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
	<div region="center" style="padding: 0px; border: 0px">
		<t:datagrid name="instanceDeviceList" checkbox="true" pagination="false" treegrid="true" treeField="name" fitColumns="true" title="应用设备" sortName="createDate"
			actionUrl="instanceDeviceController.do?datagrid" idField="id" fit="true" queryMode="group">
			<t:dgCol title="主键" field="id" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="名称" field="name" query="true" queryMode="single" width="240"></t:dgCol>
			<t:dgCol title="编码" field="code" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="设备层级" field="level" queryMode="single" dictionary="devLevel" width="120"></t:dgCol>
			<t:dgCol title="上级设备" field="parentBy" hidden="true" queryMode="single" dictionary="jform_instance_device,id,name" width="120"></t:dgCol>
			<t:dgCol title="基础设备" field="baseDeviceBy" queryMode="single" dictionary="jform_base_device,id,name" width="120"></t:dgCol>
			<t:dgCol title="位置坐标" field="coordinate" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="状态" field="status" query="true" queryMode="single" dictionary="isActive" width="120"></t:dgCol>
			<t:dgCol title="创建人" field="createBy" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="创建日期" field="createDate" formatter="yyyy-MM-dd hh:mm:ss" query="true" queryMode="group" width="120"></t:dgCol>
			<t:dgCol title="更新人" field="updateBy" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="更新日期" field="updateDate" formatter="yyyy-MM-dd" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="所属部门" field="sysOrgCode" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="所属公司" field="sysCompanyCode" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="备注" field="remark" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="操作" field="opt" width="100"></t:dgCol>
			<t:dgDelOpt title="删除" url="instanceDeviceController.do?doDel&id={id}" urlclass="ace_button" urlfont="fa-trash-o" />
			<t:dgToolBar title="录入" icon="icon-add" url="instanceDeviceController.do?goAdd" funname="add"></t:dgToolBar>
			<t:dgToolBar title="编辑" icon="icon-edit" url="instanceDeviceController.do?goUpdate" funname="updatetree"></t:dgToolBar>
			<t:dgToolBar title="批量删除" icon="icon-remove" url="instanceDeviceController.do?doBatchDel" funname="deleteALLSelecttree"></t:dgToolBar>
			<t:dgToolBar title="查看" icon="icon-search" url="instanceDeviceController.do?goUpdate" funname="detailtree"></t:dgToolBar>
			<t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
			<t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
			<t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>
		</t:datagrid>
	</div>
</div>
<script src="webpage/com/jeecg/device/instanceDeviceList.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#instanceDeviceList").treegrid({
			onExpand : function(row) {
				var children = $("#instanceDeviceList").treegrid('getChildren', row.id);
				if (children.length <= 0) {
					row.leaf = true;
					$("#instanceDeviceList").treegrid('refresh', row.id);
				}
			}
		});
	});

	//导入
	function ImportXls() {
		openuploadwin('Excel导入', 'instanceDeviceController.do?upload', "instanceDeviceList");
	}

	//导出
	function ExportXls() {
		JeecgExcelExport("instanceDeviceController.do?exportXls", "instanceDeviceList");
	}

	//模板下载
	function ExportXlsByT() {
		JeecgExcelExport("instanceDeviceController.do?exportXlsByT", "instanceDeviceList");
	}

	/**
	 * 获取表格对象
	 * @return 表格对象
	 */
	function getDataGrid() {
		var datagrid = $('#' + gridname);
		return datagrid;
	}
</script>