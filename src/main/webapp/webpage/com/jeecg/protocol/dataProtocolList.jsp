<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>数据协议</title>
<meta name="viewport" content="width=device-width" />
<t:base type="bootstrap,bootstrap-table,layer"></t:base>
</head>
<body>
	<t:datagrid name="dataProtocolList" component="bootstrap-table" checkbox="true" sortName="createDate" sortOrder="desc" pagination="false" fitColumns="true" title="数据协议"
		actionUrl="dataProtocolController.do?datagrid" idField="id" fit="true" queryMode="group">
		<t:dgCol title="主键" field="id" hidden="true" queryMode="single" width="120"></t:dgCol>
		<t:dgCol title="名称" field="name" query="true" queryMode="single" width="120"></t:dgCol>
		<t:dgCol title="编码" field="code" query="true" queryMode="single" width="120"></t:dgCol>
		<t:dgCol title="类型" field="type" queryMode="single" dictionary="dataProType" width="120"></t:dgCol>
		<t:dgCol title="数据形式" field="dataShape" queryMode="single" dictionary="dataShape" width="120"></t:dgCol>
		<t:dgCol title="状态" field="status" query="true" queryMode="single" dictionary="isActive" width="120"></t:dgCol>
		<t:dgCol title="创建人" field="createBy" hidden="true" queryMode="single" width="120"></t:dgCol>
		<t:dgCol title="创建日期" field="createDate" formatter="yyyy-MM-dd hh:mm:ss" queryMode="single" width="120"></t:dgCol>
		<t:dgCol title="更新人" field="updateBy" hidden="true" queryMode="single" width="120"></t:dgCol>
		<t:dgCol title="更新日期" field="updateDate" formatter="yyyy-MM-dd hh:mm:ss" hidden="true" queryMode="single" width="120"></t:dgCol>
		<t:dgCol title="备注" field="remark" hidden="true" queryMode="single" width="120"></t:dgCol>
		<t:dgCol title="操作" field="opt" width="100"></t:dgCol>
		<t:dgDelOpt title="删除" url="dataProtocolController.do?doDel&id={id}" urlclass="ace_button" urlfont="fa-trash-o" />
		<t:dgToolBar title="录入" icon="icon-add" url="dataProtocolController.do?goAdd" funname="add" width="768"></t:dgToolBar>
		<t:dgToolBar title="编辑" icon="icon-edit" url="dataProtocolController.do?goUpdate" funname="update" width="768"></t:dgToolBar>
		<t:dgToolBar title="批量删除" icon="icon-remove" url="dataProtocolController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
		<t:dgToolBar title="查看" icon="icon-search" url="dataProtocolController.do?goUpdate" funname="detail" width="768"></t:dgToolBar>
		<t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
		<t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
		<t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>
	</t:datagrid>
	<script type="text/javascript">
		$(document).ready(function() {
		});

		//导入
		function ImportXls() {
			openuploadwin('Excel导入', 'dataProtocolController.do?upload', "dataProtocolList");
		}

		//导出
		function ExportXls() {
			JeecgExcelExport("dataProtocolController.do?exportXls", "dataProtocolList");
		}

		//模板下载
		function ExportXlsByT() {
			JeecgExcelExport("dataProtocolController.do?exportXlsByT", "dataProtocolList");
		}
	</script>
</body>
</html>