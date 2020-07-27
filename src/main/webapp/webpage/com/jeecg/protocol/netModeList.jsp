<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>网络通讯方式</title>
<meta name="viewport" content="width=device-width" />
<t:base type="bootstrap,bootstrap-table,layer"></t:base>
</head>
<body>
	<t:datagrid name="netModeList" component="bootstrap-table" checkbox="true" sortName="id" sortOrder="asc" pagination="true" fitColumns="true" title="网络通讯方式"
		actionUrl="netModeController.do?datagrid" idField="id" fit="true" queryMode="group">
		<t:dgCol title="主键" field="id" hidden="true" queryMode="single" width="120"></t:dgCol>
		<t:dgCol title="模式名称" field="name" queryMode="single" width="120"></t:dgCol>
		<t:dgCol title="模式编码" field="code" queryMode="single" width="120"></t:dgCol>
		<t:dgCol title="图标" field="icon" queryMode="single" image="true" imageSize="20,20" width="120" formatterjs="btListImgFormatter"></t:dgCol>
		<t:dgCol title="顺序" field="num" queryMode="single" hidden="true" width="120"></t:dgCol>
		<t:dgCol title="操作" field="opt" width="100"></t:dgCol>
		<t:dgDelOpt title="删除" url="netModeController.do?doDel&id={id}" urlclass="ace_button" urlfont="fa-trash-o" />
		<t:dgToolBar title="录入" icon="icon-add" url="netModeController.do?goAdd" funname="add" width="768"></t:dgToolBar>
		<t:dgToolBar title="编辑" icon="icon-edit" url="netModeController.do?goUpdate" funname="update" width="768"></t:dgToolBar>
		<t:dgToolBar title="批量删除" icon="icon-remove" url="netModeController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
		<t:dgToolBar title="查看" icon="icon-search" url="netModeController.do?goUpdate" funname="detail" width="768"></t:dgToolBar>
		<t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
		<t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
		<t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>
	</t:datagrid>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		//bootstrap列表图片格式化
		function btListImgFormatter(value, row, index) {
			return listFileImgFormat(value, "image");
		}
		//导入
		function ImportXls() {
			openuploadwin('Excel导入', 'netModeController.do?upload', "netModeList");
		}

		//导出
		function ExportXls() {
			JeecgExcelExport("netModeController.do?exportXls", "netModeList");
		}

		//模板下载
		function ExportXlsByT() {
			JeecgExcelExport("netModeController.do?exportXlsByT", "netModeList");
		}
	</script>
</body>
</html>