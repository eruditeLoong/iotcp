<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>基础设备信息</title>
<meta name="viewport" content="width=device-width" />
<t:base type="bootstrap,bootstrap-table,layer"></t:base>
</head>
<body>
	<t:datagrid name="baseDeviceList" component="bootstrap-table" checkbox="true" sortName="createDate" sortOrder="desc" pagination="true" fitColumns="true" title="基础设备信息"
		actionUrl="baseDeviceController.do?datagrid" idField="id" fit="true" queryMode="group">
		<t:dgCol title="主键" field="id" hidden="true" queryMode="single"></t:dgCol>
		<t:dgCol title="设备名称" field="name" query="true" queryMode="single"></t:dgCol>
		<t:dgCol title="设备类型" field="type" query="true" queryMode="single" dictionary="bd_type"></t:dgCol>
		<t:dgCol title="通讯方式" field="comMethod" query="true" queryMode="single" dictionary="jform_net_mode,id,name"></t:dgCol>
		<t:dgCol title="通讯协议" field="comProtocol" query="true" queryMode="single" dictionary="jform_com_protocol,id,name"></t:dgCol>
		<t:dgCol title="数据格式" field="dataFormat" query="true" queryMode="single" dictionary="jform_data_protocol,id,name"></t:dgCol>
		<t:dgCol title="模型文件" field="modelFile" hidden="false" query="false" queryMode="single" formatterjs="fmtModelFile"></t:dgCol>
		<t:dgCol title="创建人" field="createBy" queryMode="single" dictionary="t_s_base_user,username,realname" popup="false"></t:dgCol>
		<t:dgCol title="创建日期" field="createDate" formatter="yyyy-MM-dd hh:mm:ss" query="true" queryMode="group"></t:dgCol>
		<t:dgCol title="更新人" field="updateBy" hidden="true" queryMode="single" dictionary="user_msg,updateby,account" popup="true"></t:dgCol>
		<t:dgCol title="更新日期" field="updateDate" formatter="yyyy-MM-dd hh:mm:ss" hidden="true" queryMode="single"></t:dgCol>
		<t:dgCol title="所属部门" field="sysOrgCode" hidden="true" queryMode="single" dictionary="depart_msg,sysorgcode,code" popup="true"></t:dgCol>
		<t:dgCol title="所属公司" field="sysCompanyCode" hidden="true" queryMode="single" dictionary="depart_msg,syscompanycode,code" popup="true"></t:dgCol>
		<t:dgCol title="描述" field="description" hidden="true" queryMode="single"></t:dgCol>
		<t:dgCol title="操作" field="opt" width="300"></t:dgCol>
		<t:dgDelOpt title="删除" url="baseDeviceController.do?doDel&id={id}" urlclass="ace_button" urlfont="fa-trash-o" />
		<t:dgFunOpt title="数据格式" funname="showDataProtocol(id)" operationCode="showDataProtocol" urlStyle="background-color:#8199ff;" urlclass="ace_button" urlfont="fa-eye"></t:dgFunOpt>
		<t:dgToolBar title="录入" icon="icon-add" url="baseDeviceController.do?goAdd" funname="add" width="1200" height="600"></t:dgToolBar>
		<t:dgToolBar title="编辑" icon="icon-edit" url="baseDeviceController.do?goUpdate" funname="update" width="1200" height="600"></t:dgToolBar>
		<t:dgToolBar title="批量删除" icon="icon-remove" url="baseDeviceController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
		<t:dgToolBar title="查看" icon="icon-search" url="baseDeviceController.do?goUpdate" funname="detail" width="1200" height="600"></t:dgToolBar>
		<t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
		<t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
		<t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>
	</t:datagrid>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		function fmtModelFile(value, row, index){
			return "<a href='#' onclick='viewModel(\""+value+"\");'>"+value+"<a/>";
		}
		
		function viewModel(url){
			var url = "instanceDeviceController.do?goViewModel&modelUrl=" + url;
			openwindow('模型展示', url, "", 600, 400);
		}
		// 数据格式查看
		function showDataProtocol(id){
			var url = "baseDeviceController.do?goShowDataProtocol&id=" + id;
			openwindow('数据格式查看', url, "", 768, 500);
		}

		//导入
		function ImportXls() {
			openuploadwin('Excel导入', 'baseDeviceController.do?upload', "baseDeviceList");
		}

		//导出
		function ExportXls() {
			JeecgExcelExport("baseDeviceController.do?exportXls", "baseDeviceList");
		}

		//模板下载
		function ExportXlsByT() {
			JeecgExcelExport("baseDeviceController.do?exportXlsByT", "baseDeviceList");
		}
	</script>
</body>
</html>