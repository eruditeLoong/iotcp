<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<link rel="stylesheet" href="${webRoot}/plug-in/mutitables/datagrid.menu.css" type="text/css"></link>
<style>
.datagrid-toolbar {
	padding: 0 !important;
	border: 0
}
</style>
<div class="easyui-layout" fit="true">
	<div region="center" style="padding: 0px; border: 0px">
		<t:datagrid name="comConfigList" filterBtn="true" checkbox="true" fitColumns="true" title="" actionUrl="comConfigController.do?datagrid" idField="id" fit="true"
			queryMode="group" onDblClick="datagridDbclick" pagination="false" onLoadSuccess="optsMenuToggle">
			<t:dgCol title="主键" field="id" hidden="true" extendParams="editor:'text'"></t:dgCol>
			<t:dgCol title="通讯协议" field="comProtocolBy" hidden="true" extendParams="editor:'text'"></t:dgCol>
			<t:dgCol title="参数标题" field="title" extendParams="editor:'text'"></t:dgCol>
			<t:dgCol title="参数变量名" field="name" extendParams="editor:'text'"></t:dgCol>
			<t:dgCol title="参数变量值" field="value" extendParams="editor:'text'"></t:dgCol>
			<t:dgCol title="备注" field="remark" extendParams="editor:'text'"></t:dgCol>
			<t:dgCol title="操作" field="opt" optsMenu="true"></t:dgCol>
			<t:dgFunOpt funname="curd.addRow" urlfont="fa-plus" title="新增一行" />
			<t:dgFunOpt funname="curd.deleteOne(id)" urlfont="fa-minus" title="删除该行" />
			<t:dgFunOpt funname="curd.detail(id)" urlfont="fa-eye" title="查看详情" />
		</t:datagrid>
	</div>
	<input type="hidden" id="comConfigListMainId" />
</div>
<script type="text/javascript" src="${webRoot}/plug-in/mutitables/mutitables.urd.js"></script>
<script type="text/javascript" src="${webRoot}/plug-in/mutitables/mutitables.curdInIframe.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		curd = $.curdInIframe({
			name : "comConfig",
			describe : "参数配置",
			urls : {
				excelImport : 'comProtocolController.do?commonUpload'
			}
		});
		gridname = curd.getGridname();
	});

	//双击行编辑
	function datagridDbclick(index, row, dgname) {
		$("#comConfigList").datagrid('beginEdit', index);
		afterRowEdit(index);
	}

	/**
	 * dataGrid 操作菜单 切换显示onloadSuccess 调用*/
	function optsMenuToggle(data) {
		optsMenuToggleBydg($("#comConfigList").datagrid('getPanel'));
	}

	/**
	 * 行编辑开启后需要处理相关列的选择事件
	 * @editIndex 行编辑对应的行索引
	 */
	function afterRowEdit(editIndex) {
		//TODO 
	}

	//导出模板
	function exportExcelTemplate() {
		JeecgExcelExport("comConfigController.do?exportXlsByT", "comConfigList");
	}
	//导出
	function ExportXls() {
		JeecgExcelExport("comConfigController.do?exportXls", "comConfigList");
	}
</script>
