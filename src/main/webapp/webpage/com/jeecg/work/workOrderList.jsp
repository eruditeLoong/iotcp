<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>工作票管理</title>
<meta name="viewport" content="width=device-width" />
<t:base type="bootstrap,bootstrap-table,layer"></t:base>
</head>
<body>
	<t:datagrid name="workOrderList" component="bootstrap-table" checkbox="true" sortName="createDate" sortOrder="desc" pagination="true" fitColumns="true" title="工作票管理"
		actionUrl="workOrderController.do?datagrid" idField="id" fit="true" queryMode="group">
		<t:dgCol title="主键" field="id" hidden="true" queryMode="single" width="120"></t:dgCol>
		<t:dgCol title="标题" field="title" query="true" queryMode="single" width="120"></t:dgCol>
		<t:dgCol title="工作票扫描件" field="orderFiles" hidden="true" queryMode="single" image="true" imageSize="50,50" formatterjs="btListImgFormatter" width="120"></t:dgCol>
		<t:dgCol title="厂家名称" field="sysOrgCode" hidden="true" queryMode="single" dictionary="depart_msg,sysOrgCode,code" popup="true" width="120"></t:dgCol>
		<t:dgCol title="创建日期" field="createDate" formatter="yyyy-MM-dd hh:mm:ss" query="true" queryMode="group" width="120"></t:dgCol>
		<t:dgCol title="备注" field="remark" hidden="true" queryMode="single" width="120"></t:dgCol>
		<t:dgCol title="操作" field="opt" width="100"></t:dgCol>
		<t:dgFunOpt funname="doDetail(id)" title="详情" urlclass="ace_button" urlfont="fa-wrench" />
		<%-- <t:dgToolBar title="..." icon="icon-put" funname="ImportXls"></t:dgToolBar> --%>
		<t:dgToolBar title="查看" icon="icon-search" url="workOrderController.do?goUpdate" funname="detail" width="768"></t:dgToolBar>
	</t:datagrid>
	<script type="text/javascript">
		$(document).ready(function() {
		});

		//自定义按钮-修改
		function doEdit(id, index) {
			edit(id);
		}
		//自定义按钮-详情
		function doDetail(id, index) {
			detail(id);
		}

		function ImportXls() {
			window.open("http://127.0.0.1:8080/wtms");
		}

		//导出
		function ExportXls() {
			JeecgExcelExport("workOrderController.do?exportXls", "workOrderList");
		}

		//模板下载
		function ExportXlsByT() {
			JeecgExcelExport("workOrderController.do?exportXlsByT", "workOrderList");
		}
		//JS增强
		// 列表修改
		function edit(id) {
			var url = "workOrderController.do?goUpdate&id=" + id;
			add('表单修改', url, '', 500, 300);
		}

		// 列表详情
		function detail(id) {
			var url = "workOrderController.do?goDetail&id=" + id;
			add('工作票详情', url, '', 500, 300);
		}
	</script>
</body>
</html>