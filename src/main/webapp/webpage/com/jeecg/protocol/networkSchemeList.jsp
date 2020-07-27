<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
</head>
<body>
	<div id="system_function_functionList" class="easyui-layout" fit="true">
		<div region="center" style="padding: 0px; border: 0px">
			<t:datagrid name="networkSchemeList" title="组网方案" actionUrl="networkSchemeController.do?datagrid" queryMode="group" btnCls="bootstrap btn btn-info btn-xs" idField="id"
				fit="true" fitColumns="true" pagination="true" treegrid="true" treeField="title">
				<t:dgCol title="主键" field="id" hidden="true" queryMode="single"></t:dgCol>
				<t:dgCol title="方案名称" field="name" query="true" queryMode="single" width="120"></t:dgCol>
				<t:dgCol title="接入模式" field="connectionModeBy" query="true" queryMode="single" width="50" dictionary="jform_connection_mode,code,name"></t:dgCol>
				<t:dgCol title="通讯方式" field="netModeBy" query="true" queryMode="single" width="50" dictionary="jform_net_mode,code,name"></t:dgCol>
				<t:dgCol title="创建人" field="createBy" queryMode="single" width="50" dictionary="t_s_base_user,username,realname"></t:dgCol>
				<t:dgCol title="创建日期" field="createDate" formatter="yyyy-MM-dd hh:mm:ss" queryMode="single" width="60"></t:dgCol>
				<t:dgCol title="更新人" field="updateBy" hidden="true" queryMode="single" width="50" dictionary="t_s_base_user,username,realname"></t:dgCol>
				<t:dgCol title="更新日期" field="updateDate" formatter="yyyy-MM-dd hh:mm:ss" hidden="true" queryMode="single" width="60"></t:dgCol>
				<t:dgCol title="所属部门" field="sysOrgCode" hidden="true" queryMode="single" width="60"></t:dgCol>
				<t:dgCol title="所属公司" field="sysCompanyCode" hidden="true" queryMode="single" width="60"></t:dgCol>
				<t:dgCol title="备注" field="remark" queryMode="single" hidden="true" width="150"></t:dgCol>

				<t:dgToolBar title="录入" icon="fa fa-plus-square-o" url="networkSchemeController.do?goAdd" funname="add" width="768"></t:dgToolBar>
				<t:dgToolBar title="编辑" icon="fa fa-pencil-square-o" url="networkSchemeController.do?goUpdate" funname="update" width="768"></t:dgToolBar>
				<t:dgToolBar title="批量删除" icon="fa fa-minus-square-o" url="networkSchemeController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
				<t:dgToolBar title="查看" icon="fa fa-search" url="networkSchemeController.do?goUpdate" funname="detail" width="768"></t:dgToolBar>
				<t:dgToolBar title="导入" icon="fa fa-put" funname="ImportXls"></t:dgToolBar>
				<t:dgToolBar title="导出" icon="fa fa-putout" funname="ExportXls"></t:dgToolBar>
				<t:dgToolBar title="模板下载" icon="fa fa-putout" funname="ExportXlsByT"></t:dgToolBar>

				<t:dgCol title="操作" field="opt" align="left" width="80"></t:dgCol>
				<t:dgDelOpt title="删除" url="networkSchemeController.do?doDel&id={id}" urlclass="ace_button" urlfont="fa-trash-o" />
				<t:dgFunOpt funname="doConfigDrive(id)" title="配置设备" urlclass="ace_button" urlfont="fa-wrench" exp="connectionModeBy#eq#device_connection,blend_connection" />
				<t:dgFunOpt funname="doConfigGateway(id)" title="配置网关" urlclass="ace_button" urlfont="fa-wrench" exp="connectionModeBy#eq#gateway_connection,blend_connection" />
			</t:datagrid>
		</div>
	</div>
	<div region="east" id="eastPanel"
		data-options="region:'east',
		title:'操作',
		collapsed:true,
		split:true,
		border:true,
		onExpand : function(){
			li_east = 1;
		},
		onCollapse : function() {
			li_east = 0;
		}"
		style="width: 600px; overflow: hidden;">
		<div class="easyui-panel" style="padding: 0px; border: 0px" fit="true" border="true" id="operationDetailpanel"></div>
	</div>

	<script type="text/javascript">
		$(function() {
			var li_east = 0;
		});

		//自定义按钮-配置设备
		function doConfigDrive(id, index) {
			configDrive(id);
		}
		//自定义按钮-配置网关
		function doConfigGateway(id, index) {
			configGateway(id);
		}

		//导入
		function ImportXls() {
			openuploadwin('Excel导入', 'networkSchemeController.do?upload', "networkSchemeList");
		}

		//导出
		function ExportXls() {
			JeecgExcelExport("networkSchemeController.do?exportXls", "networkSchemeList");
		}

		//模板下载
		function ExportXlsByT() {
			JeecgExcelExport("networkSchemeController.do?exportXlsByT", "networkSchemeList");
		}

		// 终端设备
		function doConfigDrive(id) {
			if (li_east == 0) {
				$('#system_function_functionList').layout('expand', 'east');
			}
			$('#operationDetailpanel').panel({
				title : "配置设备",
				href : "networkSchemeController.do?deviceList&model=terminal&schemeBy=" + id
			});
		}

		// 网关
		function doConfigGateway(id) {
			if (li_east == 0) {
				$('#system_function_functionList').layout('expand', 'east');
			}
			$('#operationDetailpanel').panel({
				title : "配置网关",
				href : "networkSchemeController.do?deviceList&model=gateway&schemeBy=" + id
			});
		}
	</script>
</body>
</html>