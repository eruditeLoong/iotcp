<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>数据校验</title>
<meta name="viewport" content="width=device-width" />
<t:base type="bootstrap,bootstrap-table,layer"></t:base>
</head>
<body>
	<t:datagrid name="comCheckList" component="bootstrap-table" checkbox="true" sortName="num" sortOrder="asc" pagination="true" fitColumns="true" title="数据校验"
		actionUrl="comCheckController.do?datagrid" idField="id" fit="true" queryMode="group">
		<t:dgCol title="主键" field="id" hidden="true" queryMode="single"></t:dgCol>
		<t:dgCol title="检验名称" field="name" query="true" queryMode="single"></t:dgCol>
		<t:dgCol title="校验CODE" field="code" queryMode="single"></t:dgCol>
		<t:dgCol title="顺序" field="num" queryMode="single" sortable="true" hidden="true"></t:dgCol>
		<t:dgCol title="描述" field="remark" queryMode="single" showLen="30"></t:dgCol>
		<t:dgCol title="创建人" field="createBy" query="true" queryMode="single" dictionary="t_s_base_user,username,realname"></t:dgCol>
		<t:dgCol title="创建日期" field="createDate" formatter="yyyy-MM-dd hh:mm:ss" hidden="true" queryMode="single"></t:dgCol>
		<t:dgCol title="更新人" field="updateBy" hidden="true" queryMode="single"></t:dgCol>
		<t:dgCol title="更新日期" field="updateDate" formatter="yyyy-MM-dd hh:mm:ss" hidden="true" query="true" queryMode="single"></t:dgCol>
		<t:dgCol title="操作" field="opt" width="200"></t:dgCol>

		<t:dgDelOpt title="删除" url="comCheckController.do?doDel&id={id}" urlclass="ace_button" urlfont="fa-trash-o" />
		<t:dgToolBar title="录入" icon="icon-add" url="comCheckController.do?goAdd" funname="add" width="768"></t:dgToolBar>
		<t:dgToolBar title="编辑" icon="icon-edit" url="comCheckController.do?goUpdate" funname="update" width="768"></t:dgToolBar>
		<t:dgToolBar title="批量删除" icon="icon-remove" url="comCheckController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
		<t:dgToolBar title="查看" icon="icon-search" url="comCheckController.do?goUpdate" funname="detail" width="768"></t:dgToolBar>
		<t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
		<t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
		<t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>
	</t:datagrid>
	<script type="text/javascript">
		$(document).ready(function() {
		});

		//导入
		function ImportXls() {
			openuploadwin('Excel导入', 'comCheckController.do?upload', "comCheckList");
		}

		//导出
		function ExportXls() {
			JeecgExcelExport("comCheckController.do?exportXls", "comCheckList");
		}

		//模板下载
		function ExportXlsByT() {
			JeecgExcelExport("comCheckController.do?exportXlsByT", "comCheckList");
		}
	</script>
</body>
</html>