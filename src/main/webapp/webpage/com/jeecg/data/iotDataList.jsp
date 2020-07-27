<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div id="sceneList-layout" class="easyui-layout" fit="true">
	<div region="center" style="padding: 0px; border: 0px">
		<t:datagrid name="iotDataList" title="物联网数据" actionUrl="iotDataController.do?datagrid" idField="id" fit="true" fitColumns="true" pagination="true"
					sortName="createDate" sortOrder="desc" queryMode="group" checkbox="true" btnCls="bootstrap btn btn-normal btn-xs">
			<t:dgCol title="主键" field="id" hidden="true" queryMode="single" width="0"></t:dgCol>
			<t:dgCol title="时间" field="createDate" formatter="yyyy-MM-dd hh:mm:ss" query="true" queryMode="group" width="0"></t:dgCol>
			<t:dgCol title="场景名称" field="sceneBy" query="true" queryMode="single" dictionary="jform_scene,id,name" width="0"></t:dgCol>
			<t:dgCol title="设备名称" field="deviceBy" query="true" queryMode="single" dictionary="jform_base_device,id,name" width="0"></t:dgCol>
			<t:dgCol title="设备编码" field="deviceCode" queryMode="single" width="0"></t:dgCol>
			<t:dgCol title="名称" field="label" queryMode="single" width="0"></t:dgCol>
			<t:dgCol title="变量" field="fieldBy" queryMode="single" width="0"></t:dgCol>
			<t:dgCol title="数值" field="data" queryMode="single" width="0"></t:dgCol>
			<t:dgCol title="数值类型" field="type" query="true" queryMode="single" dictionary="bDataType" width="0"></t:dgCol>
			<t:dgCol title="状态" field="status" query="true" queryMode="single" formatterjs="statusFmt" dictionary="datCountTy" width="0"></t:dgCol>
			<t:dgCol title="备注" field="remark" hidden="true" queryMode="single" width="0"></t:dgCol>
			<t:dgCol title="操作" field="opt" width="300"></t:dgCol>
			<t:dgDelOpt title="删除" url="iotDataController.do?doDel&id={id}" urlclass="ace_button" urlfont="fa-trash-o"/>
			<t:dgToolBar title="录入" icon="icon-add" url="iotDataController.do?goAdd" funname="add" width="768"></t:dgToolBar>
			<t:dgToolBar title="编辑" icon="icon-edit" url="iotDataController.do?goUpdate" funname="update" width="768"></t:dgToolBar>
			<t:dgToolBar title="批量删除" icon="icon-remove" url="iotDataController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
			<t:dgToolBar title="查看" icon="icon-search" url="iotDataController.do?goUpdate" funname="detail" width="768"></t:dgToolBar>
			<t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
			<t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
			<t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>
		</t:datagrid>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function () {
	});

	function statusFmt(value, row, index) {
		if (value == 0) {
			return "<span class='ace_button' style='background-color: red;color:#fff'>报 警</span>"
		} else {
			return "正常";
		}
	}

	//导入
	function ImportXls() {
		openuploadwin('Excel导入', 'iotDataController.do?upload', "iotDataList");
	}

	//导出
	function ExportXls() {
		JeecgExcelExport("iotDataController.do?exportXls", "iotDataList");
	}

	//模板下载
	function ExportXlsByT() {
		JeecgExcelExport("iotDataController.do?exportXlsByT", "iotDataList");
	}
</script>