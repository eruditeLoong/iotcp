<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
	<div region="center" style="padding:0px;border:0px">
		<t:datagrid name="iotAlarmDataList" checkbox="true" pagination="true" fitColumns="true" title="报警数据" actionUrl="iotAlarmDataController.do?datagrid"
					fit="true" idField="id" sortName="alarmDate" sortOrder="desc" queryMode="group">
			<t:dgCol title="主键" field="id" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="流程定义ID" field="procInsId" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="场景" field="sceneBy" query="true" queryMode="single" dictionary="jform_scene,id,name" width="120"></t:dgCol>
			<t:dgCol title="部署设备" field="deployDeviceBy" hidden="true" query="true" queryMode="single" dictionary="jform_scene_device_depoly,id,id" width="120"></t:dgCol>
			<t:dgCol title="基础设备" field="baseDeviceBy" hidden="true" query="true" queryMode="single" dictionary="jform_base_device,id,name" width="120"></t:dgCol>
			<t:dgCol title="基础设备名称" field="baseDeviceName" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="部署设备编号" field="deployDeviceCode" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="数据名称" field="dataLabel" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="数据字段" field="dataField" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="数据值" field="dataValue" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="正常数据范围" field="normalDataRange" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="报警级别" field="alarmLevel" queryMode="single" dictionary="alarmLevel" width="120"></t:dgCol>
			<t:dgCol title="处理状态" field="dealStatus" query="true" queryMode="single" dictionary="dealStatus" width="120"></t:dgCol>
			<t:dgCol title="处理措施" field="dealMeasure" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="原因分析" field="causeAnalysis" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="处理人" field="dealUserBy" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="报警时间" field="alarmDate" formatter="yyyy-MM-dd hh:mm:ss" query="true" queryMode="group" width="120"></t:dgCol>
			<t:dgCol title="处理时间" field="dealDate" formatter="yyyy-MM-dd hh:mm:ss" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="流程状态" field="bpmStatus" hidden="true" queryMode="single" dictionary="bpm_status" width="120"></t:dgCol>
			<t:dgCol title="操作" field="opt" width="100"></t:dgCol>
			<t:dgDelOpt title="删除" url="iotAlarmDataController.do?doDel&id={id}" urlclass="ace_button" urlfont="fa-trash-o"/>
			<t:dgToolBar title="录入" icon="icon-add" url="iotAlarmDataController.do?goAdd" funname="add" width="800" height="500"></t:dgToolBar>
			<t:dgToolBar title="编辑" icon="icon-edit" url="iotAlarmDataController.do?goUpdate" funname="update" width="800" height="500"></t:dgToolBar>
			<t:dgToolBar title="批量删除" icon="icon-remove" url="iotAlarmDataController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
			<t:dgToolBar title="查看" icon="icon-search" url="iotAlarmDataController.do?goUpdate" funname="detail" width="800" height="500"></t:dgToolBar>
			<t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
			<t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
			<t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>
		</t:datagrid>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function () {
	});


	//导入
	function ImportXls() {
		openuploadwin('Excel导入', 'iotAlarmDataController.do?upload', "iotAlarmDataList");
	}

	//导出
	function ExportXls() {
		JeecgExcelExport("iotAlarmDataController.do?exportXls", "iotAlarmDataList");
	}

	//模板下载
	function ExportXlsByT() {
		JeecgExcelExport("iotAlarmDataController.do?exportXlsByT", "iotAlarmDataList");
	}

	//bootstrap列表图片格式化
	function btListImgFormatter(value, row, index) {
		return listFileImgFormat(value, "image");
	}

	//bootstrap列表文件格式化
	function btListFileFormatter(value, row, index) {
		return listFileImgFormat(value);
	}

</script>
