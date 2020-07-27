<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
    <div region="center" style="padding:0px;border:0px">
        <t:datagrid name="positionFenceList" checkbox="false" pagination="true" fitColumns="true" title="围栏信息"
                    sortName="createDate" actionUrl="positionFenceController.do?datagrid" idField="id" fit="true" queryMode="group">
            <t:dgCol title="主键" field="id" hidden="true" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="所属场景" field="sceneBy" query="true" queryMode="single" dictionary="jform_scene,id,name" width="120"></t:dgCol>
            <t:dgCol title="名称" field="name" query="true" queryMode="single" formatterjs="colorFmt" width="120"></t:dgCol>
            <t:dgCol title="围栏" field="type" query="true" queryMode="single" dictionary="fenceType" width="120"></t:dgCol>
            <t:dgCol title="颜色" field="color" hidden="true" query="true" queryMode="single"  width="120"></t:dgCol>
            <t:dgCol title="是否场景边界" field="isSceneBoundary" query="true" queryMode="single" dictionary="isBound" width="120"></t:dgCol>
            <t:dgCol title="创建日期" field="createDate" formatter="yyyy-MM-dd hh:mm:ss" queryMode="single"
                     width="120"></t:dgCol>
            <t:dgCol title="备注" field="remark" hidden="true" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
            <t:dgDelOpt title="删除" url="positionFenceController.do?doDel&id={id}" urlclass="ace_button" urlfont="fa-trash-o"/>
            <t:dgToolBar title="录入" icon="icon-add" url="positionFenceController.do?goAdd" funname="add"></t:dgToolBar>
            <t:dgToolBar title="编辑" icon="icon-edit" url="positionFenceController.do?goUpdate" funname="update"></t:dgToolBar>
            <t:dgToolBar title="批量删除" icon="icon-remove" url="positionFenceController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
            <t:dgToolBar title="查看" icon="icon-search" url="positionFenceController.do?goUpdate" funname="detail"></t:dgToolBar>
            <t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
            <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
            <t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>
        </t:datagrid>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
    });

    function colorFmt(value, row, index) {
        return "<span class='ace_button' style='background-color: " + row.color + "' >"+value+"</span>";
    }

    //导入
    function ImportXls() {
        openuploadwin('Excel导入', 'positionFenceController.do?upload', "positionFenceList");
    }

    //导出
    function ExportXls() {
        JeecgExcelExport("positionFenceController.do?exportXls", "positionFenceList");
    }

    //模板下载
    function ExportXlsByT() {
        JeecgExcelExport("positionFenceController.do?exportXlsByT", "positionFenceList");
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
