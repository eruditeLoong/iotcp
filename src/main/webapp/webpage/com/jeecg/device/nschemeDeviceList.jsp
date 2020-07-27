<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
	<div region="center" style="padding: 0px; border: 1px">
		<t:datagrid name="nschemeDeviceList" actionUrl="nschemeDeviceController.do?datagrid" treegrid="true" treeField="title" fit="true" fitColumns="true" idField="id"
			btnCls="bootstrap btn btn-info btn-xs" checkbox="true">
			<%-- <t:datagrid name="nschemeDeviceList" component="bootstrap-table" checkbox="true" sortName="createDate" sortOrder="desc" pagination="true" fitColumns="true" title="组网方案绑定设备"
		actionUrl="nschemeDeviceController.do?datagrid" idField="id" fit="true" queryMode="group" treegrid="true" treeField="gatewayBy"> --%>
			<t:dgCol title="主键" field="id" hidden="true" queryMode="single"></t:dgCol>
			<t:dgCol title="设备标题" field="title" query="true" queryMode="single"></t:dgCol>
			<t:dgCol title="所属方案" field="schemeBy" query="true" queryMode="single" dictionary="jform_network_scheme,id,name"></t:dgCol>
			<t:dgCol title="所属网关" field="gatewayBy" query="true" queryMode="single" dictionary="jform_nscheme_device,id,title"></t:dgCol>
			<t:dgCol title="设备" field="deviceBy" query="true" queryMode="single" dictionary="jform_base_device,id,name"></t:dgCol>
			<t:dgCol title="设备编号" field="deviceCode" queryMode="single"></t:dgCol>
			<t:dgCol title="位置信息" field="gisInfo" queryMode="single"></t:dgCol>
			<t:dgCol title="状态" field="status" queryMode="single" dictionary="isActive"></t:dgCol>
			<t:dgCol title="创建人" field="createBy" queryMode="single" dictionary="t_s_base_user,username,realname"></t:dgCol>
			<t:dgCol title="创建日期" field="createDate" formatter="yyyy-MM-dd hh:mm:ss" queryMode="single"></t:dgCol>
			<t:dgCol title="更新人" field="updateBy" hidden="true" queryMode="single" dictionary="t_s_base_user,username,realname"></t:dgCol>
			<t:dgCol title="更新日期" field="updateDate" formatter="yyyy-MM-dd hh:mm:ss" hidden="true" queryMode="single"></t:dgCol>
			<t:dgCol title="所属部门" field="sysOrgCode" hidden="true" queryMode="single"></t:dgCol>
			<t:dgCol title="所属公司" field="sysCompanyCode" hidden="true" queryMode="single"></t:dgCol>
			<t:dgCol title="备注" field="remark" queryMode="single" hidden="true"></t:dgCol>
			<t:dgCol title="操作" field="opt" width="200"></t:dgCol>
			<t:dgDelOpt title="删除" url="nschemeDeviceController.do?doDel&id={id}" urlclass="ace_button" urlfont="fa-trash-o" />
			<t:dgToolBar title="录入" icon="icon-add" url="nschemeDeviceController.do?goAdd" funname="add" width="800" height="500"></t:dgToolBar>
			<t:dgToolBar title="编辑" icon="icon-edit" url="nschemeDeviceController.do?goUpdate" funname="update" width="800" height="500"></t:dgToolBar>
			<t:dgToolBar title="批量删除" icon="icon-remove" url="nschemeDeviceController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
			<t:dgToolBar title="查看" icon="icon-search" url="nschemeDeviceController.do?goUpdate" funname="detail" width="800" height="500"></t:dgToolBar>
			<t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
			<t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
			<t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>
		</t:datagrid>
		<script type="text/javascript">
			$(document).ready(function() {
				$("#nschemeDeviceList").treegrid({
					onExpand : function(row) {
						var children = $("#nschemeDeviceList").treegrid('getChildren', row.id);
						if (children.length <= 0) {
							row.leaf = true;
							$("#nschemeDeviceList").treegrid('refresh', row.id);
						}
					}
				});
			});

			//导入
			function ImportXls() {
				openuploadwin('Excel导入', 'nschemeDeviceController.do?upload', "nschemeDeviceList");
			}

			//导出
			function ExportXls() {
				JeecgExcelExport("nschemeDeviceController.do?exportXls", "nschemeDeviceList");
			}

			//模板下载
			function ExportXlsByT() {
				JeecgExcelExport("nschemeDeviceController.do?exportXlsByT", "nschemeDeviceList");
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