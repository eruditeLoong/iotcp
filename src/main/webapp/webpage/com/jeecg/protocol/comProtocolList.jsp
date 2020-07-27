<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
	<div region="center" style="padding: 0px; border: 0px">
		<t:datagrid name="comProtocolList" checkbox="false" pagination="true" fitColumns="true" title="通讯协议" sortName="createDate" actionUrl="comProtocolController.do?datagrid"
			idField="id" fit="true" queryMode="group">
			<t:dgCol title="主键" field="id" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="协议名称" field="name" query="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="协议code" field="code" query="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="校验类型" field="checkType" queryMode="single" dictionary="jform_com_check,code,name" width="120"></t:dgCol>
			<t:dgCol title="数据模式" field="dataMode" queryMode="single" dictionary="data_mode" width="120"></t:dgCol>
			<t:dgCol title="创建人" field="createBy" query="true" queryMode="single" dictionary="user_msg,createby,account" popup="true" width="120"></t:dgCol>
			<t:dgCol title="创建日期" field="createDate" formatter="yyyy-MM-dd hh:mm:ss" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="更新人" field="updateBy" hidden="true" queryMode="single" dictionary="user_msg,updateby,account" popup="true" width="120"></t:dgCol>
			<t:dgCol title="更新日期" field="updateDate" formatter="yyyy-MM-dd hh:mm:ss" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="所属部门" field="sysOrgCode" hidden="true" queryMode="single" dictionary="depart_msg,sysorgcode,code" popup="true" width="120"></t:dgCol>
			<t:dgCol title="所属公司" field="sysCompanyCode" hidden="true" queryMode="single" dictionary="depart_msg,syscompanycode,code" popup="true" width="120"></t:dgCol>
			<t:dgCol title="协议描述" field="remark" hidden="true" queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="操作" field="opt" width="100"></t:dgCol>
			<t:dgDelOpt title="删除" url="comProtocolController.do?doDel&id={id}" urlclass="ace_button" urlfont="fa-trash-o" />
			<t:dgToolBar title="录入" icon="icon-add" url="comProtocolController.do?goAdd" funname="add" width="1000" height="500"></t:dgToolBar>
			<t:dgToolBar title="编辑" icon="icon-edit" url="comProtocolController.do?goUpdate" funname="update"  width="1000" height="500"></t:dgToolBar>
			<t:dgToolBar title="批量删除" icon="icon-remove" url="comProtocolController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
			<t:dgToolBar title="查看" icon="icon-search" url="comProtocolController.do?goUpdate" funname="detail"  width="1000" height="500"></t:dgToolBar>
			<t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
			<t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
			<t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>
		</t:datagrid>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function() {
	});

	//导入
	function ImportXls() {
		openuploadwin('Excel导入', 'comProtocolController.do?upload', "comProtocolList");
	}

	//导出
	function ExportXls() {
		JeecgExcelExport("comProtocolController.do?exportXls", "comProtocolList");
	}

	//模板下载
	function ExportXlsByT() {
		JeecgExcelExport("comProtocolController.do?exportXlsByT", "comProtocolList");
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
