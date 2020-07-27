<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
	<div region="center" style="padding: 0px; border: 0px">
		<t:datagrid name="sceneDeviceDepolyList" checkbox="true" pagination="true" fitColumns="true" title="场景部署设备" actionUrl="sceneDeviceDepolyController.do?datagrid" idField="id"
			sortName="id" fit="true" queryMode="group">
			<t:dgCol title="id" field="id" hidden="true" queryMode="group" width="0"></t:dgCol>
			<t:dgCol title="场景" field="sceneBy" queryMode="group" dictionary="jform_scene,id,name" width="0"></t:dgCol>
			<t:dgCol title="设备" field="deviceBy" queryMode="group" dictionary="jform_base_device,id,name" width="0"></t:dgCol>
			<t:dgCol title="设备编码" field="deviceCode" queryMode="group" width="0"></t:dgCol>
			<t:dgCol title="请求命令" field="requestCmd" queryMode="group" width="0"></t:dgCol>
			<t:dgCol title="父类设备" field="deviceParentBy" queryMode="group" dictionary="jform_base_device,id,name" width="0"></t:dgCol>
			<t:dgCol title="MQTT主题" field="mqtt" queryMode="group" formatterjs="mqttFmt" width="0"></t:dgCol>
			<t:dgCol title="设备位置" field="threeData" hidden="true" queryMode="group" width="0"></t:dgCol>
			<t:dgCol title="操作" field="opt" width="300"></t:dgCol>
			<t:dgDelOpt title="删除" url="sceneDeviceDepolyController.do?doDel&id={id}" urlclass="ace_button" urlfont="fa-trash-o" />
			<t:dgFunOpt title="数据格式" funname="showDataProtocol(deviceBy,deviceCode)" operationCode="showDataProtocol" urlStyle="background-color:#8199ff;" urlclass="ace_button"
				urlfont="fa-eye"></t:dgFunOpt>
			<t:dgToolBar title="录入" icon="icon-add" url="sceneDeviceDepolyController.do?goAdd" funname="add" width="768"></t:dgToolBar>
			<t:dgToolBar title="编辑" icon="icon-edit" url="sceneDeviceDepolyController.do?goUpdate" funname="update" width="768"></t:dgToolBar>
			<t:dgToolBar title="批量删除" icon="icon-remove" url="sceneDeviceDepolyController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
			<t:dgToolBar title="查看" icon="icon-search" url="sceneDeviceDepolyController.do?goUpdate" funname="detail" width="768"></t:dgToolBar>
			<t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
			<t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
			<t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>
		</t:datagrid>

		<!-- <table id="tt" data-options="fitColumns:true,singleSelect:true,rownumbers:true"></table> -->
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function() {
	});

	function translate (keys, data) {
		let [key, ...nextKeys] = keys;
		let hasNextKey = nextKeys && nextKeys.length;
		let map = {};
		
		data.forEach(item => {
			let k = item[key];
			if (k && !map[k]) {
				// 获取源数组中所有命中的`item`认为这些`item`为子项
				let childList = data.filter(item => item[key] === k).map(item => delete item[key] && item);
				map[k] = {
					id: map["id"],
					name: k,
					children: hasNextKey ? translate(nextKeys, childList) : childList  // 如果还有用来分组的key，继续执行，否则返回数组
				}
			}
		});
		return Object.values(map);
	}

	// MQTT
	function mqttFmt(value, row, index) {
		return '$forallcn/iotcp/' + row.id;
	}

	// 数据格式查看
	function showDataProtocol(id, code) {
		var url = "baseDeviceController.do?goShowDataProtocol&id=" + id + "&code=" + code;
		openwindow('数据格式查看', url, "", 768, 500);
	}

	//导入
	function ImportXls() {
		openuploadwin('Excel导入', 'sceneDeviceDepolyController.do?upload', "sceneDeviceDepolyList");
	}

	//导出
	function ExportXls() {
		JeecgExcelExport("sceneDeviceDepolyController.do?exportXls", "sceneDeviceDepolyList");
	}

	//模板下载
	function ExportXlsByT() {
		JeecgExcelExport("sceneDeviceDepolyController.do?exportXlsByT", "sceneDeviceDepolyList");
	}

	//bootstrap列表图片格式化
	function btListImgFormatter(value, row, index) {
		return listFileImgFormat(value, "image");
	}
	//bootstrap列表文件格式化
	function btListFileFormatter(value, row, index) {
		return listFileImgFormat(value);
	}

	//列表文件图片 列格式化方法
	function listFileImgFormat(value, type) {
		var href = '';
		if (value == null || value.length == 0) {
			return href;
		}
		var value1 = "img/server/" + value;
		if ("image" == type) {
			href += "<img src='" + value1 + "' width=30 height=30  onmouseover='tipImg(this)' onmouseout='moveTipImg()' style='vertical-align:middle'/>";
		} else {
			if (value.indexOf(".jpg") > -1 || value.indexOf(".gif") > -1 || value.indexOf(".png") > -1) {
				href += "<img src='" + value1 + "' onmouseover='tipImg(this)' onmouseout='moveTipImg()' width=30 height=30 style='vertical-align:middle'/>";
			} else {
				var value2 = "img/server/" + value + "?down=true";
				href += "<a href='"+value2+"' class='ace_button' style='text-decoration:none;' target=_blank><u><i class='fa fa-download'></i>点击下载</u></a>";
			}
		}
		return href;
	}
</script>
