<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
    <div region="center" style="padding:0px;border:0px">
        <t:datagrid name="positionUserOrgList" checkbox="true" pagination="true" fitColumns="true" title="人员单位"
                    actionUrl="positionUserOrgController.do?datagrid" idField="id" sortName="createDate" fit="true"
                    queryMode="single">
            <t:dgCol title="主键" field="id" hidden="false" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="创建日期" field="createDate" formatter="yyyy-MM-dd" hidden="true" queryMode="single"
                     width="120"></t:dgCol>
            <t:dgCol title="单位名称" field="name" queryMode="single" query="true" width="120"></t:dgCol>
            <t:dgCol title="工作内容" field="work" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="颜色" field="color" queryMode="single" formatterjs="colorFmt" width="120"></t:dgCol>
            <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
            <t:dgDelOpt title="删除" url="positionUserOrgController.do?doDel&id={id}" urlclass="ace_button"
                        urlfont="fa-trash-o"/>
            <t:dgToolBar title="录入" icon="icon-add" url="positionUserOrgController.do?goAdd" funname="add"
                         width="768"></t:dgToolBar>
            <t:dgToolBar title="编辑" icon="icon-edit" url="positionUserOrgController.do?goUpdate" funname="update"
                         width="768"></t:dgToolBar>
            <t:dgToolBar title="批量删除" icon="icon-remove" url="positionUserOrgController.do?doBatchDel"
                         funname="deleteALLSelect"></t:dgToolBar>
            <t:dgToolBar title="查看" icon="icon-search" url="positionUserOrgController.do?goUpdate" funname="detail"
                         width="768"></t:dgToolBar>
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
        return "<span class='ace_button' style='background-color: " + value + "' >" + value + "</span>";
    }

    //导入
    function ImportXls() {
        openuploadwin('Excel导入', 'positionUserOrgController.do?upload', "positionUserOrgList");
    }

    //导出
    function ExportXls() {
        JeecgExcelExport("positionUserOrgController.do?exportXls", "positionUserOrgList");
    }

    //模板下载
    function ExportXlsByT() {
        JeecgExcelExport("positionUserOrgController.do?exportXlsByT", "positionUserOrgList");
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
