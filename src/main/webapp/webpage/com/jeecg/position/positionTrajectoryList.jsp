<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
    <div region="center" style="padding:0px;border:0px">
        <t:datagrid name="positionTrajectoryList" checkbox="true" pagination="true" fitColumns="true" title="定位数据"
                    actionUrl="positionTrajectoryController.do?datagrid" idField="id" sortName="createDate" fit="true" queryMode="group">
            <t:dgCol title="主键" field="id" hidden="true" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="IMEI" field="imei" query="true" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="纬度" field="latitude" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="经度" field="longitude" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="GPS速度" field="gpsSpeed" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="航向" field="course" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="定位方式" field="locateType" query="true" queryMode="single" formatterjs="locateTypeFmt" width="120"></t:dgCol>
            <t:dgCol title="东西经" field="ewLongitude" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="南北纬" field="nsLatitude" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="是否越界" field="isCross" queryMode="single" dictionary="isBound" width="120"></t:dgCol>
            <t:dgCol title="创建日期" field="createDate" formatter="yyyy-MM-dd hh:mm:ss" query="true" queryMode="group" width="120"></t:dgCol>
            <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
            <t:dgDelOpt title="删除" url="positionTrajectoryController.do?doDel&id={id}" urlclass="ace_button" urlfont="fa-trash-o"/>
            <t:dgFunOpt title="定位" funname="position(id)" urlclass="ace_button" urlStyle="background-color:#2BAAFF;" urlfont="fa-trash-o"></t:dgFunOpt>
            <t:dgToolBar title="录入" icon="icon-add" url="positionTrajectoryController.do?goAdd" funname="add" width="768"></t:dgToolBar>
            <t:dgToolBar title="编辑" icon="icon-edit" url="positionTrajectoryController.do?goUpdate" funname="update" width="768"></t:dgToolBar>
            <t:dgToolBar title="批量删除" icon="icon-remove" url="positionTrajectoryController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
            <t:dgToolBar title="查看" icon="icon-search" url="positionTrajectoryController.do?goUpdate" funname="detail" width="768"></t:dgToolBar>
            <t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
            <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
            <t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>
        </t:datagrid>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
    });

    function locateTypeFmt(value) {
        var type = "";
        if (value == 1)
            type = "GSP定位";
        else if (value == 2)
            type = "WIFI定位";
        else if (value == 4)
            type = "LBS定位";
        else if (value == 6)
            type = "WIFI、LBS混合定位";
        else if (value == 7)
            type = "GPS、WIFI、WIFI混合定位";
        return type;
    }

    function position(id) {
        openwindow("地图定位", "positionTrajectoryController.do?goPosition&id=" + id, "name", "100%", "100%");
        // addOneTab('地图定位', "positionTrajectoryController.do?goPosition&id=" + id);
    }

    //导入
    function ImportXls() {
        openuploadwin('Excel导入', 'positionTrajectoryController.do?upload', "positionTrajectoryList");
    }

    //导出
    function ExportXls() {
        JeecgExcelExport("positionTrajectoryController.do?exportXls", "positionTrajectoryList");
    }

    //模板下载
    function ExportXlsByT() {
        JeecgExcelExport("positionTrajectoryController.do?exportXlsByT", "positionTrajectoryList");
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
