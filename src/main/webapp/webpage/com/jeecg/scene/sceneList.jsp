<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<!-- <link rel="stylesheet" type="text/css" href="plug-in/easyui/themes/default/easyui.css"> -->
<div id="sceneList-layout" class="easyui-layout" fit="true">
    <div region="center" style="padding: 0px; border: 0px">
        <t:datagrid name="sceneList" title="场景管理" actionUrl="sceneController.do?datagrid" idField="id" fit="true" fitColumns="true" treegrid="true" pagination="false"
                    queryMode="group" checkbox="false" btnCls="bootstrap btn btn-normal btn-xs">
            <t:dgCol title="主键" field="id" hidden="true" queryMode="single" width="0"></t:dgCol>
            <t:dgCol title="名称" field="name" query="true" queryMode="single" width="0"></t:dgCol>
            <t:dgCol title="编码" field="code" queryMode="single" width="0"></t:dgCol>
            <t:dgCol title="展示类型" field="showType" query="true" queryMode="single" dictionary="showType" width="0"></t:dgCol>
            <t:dgCol title="2D场景" field="scene2d" hidden="true" queryMode="single" formatterjs="btListFileFormatter" width="0"></t:dgCol>
            <t:dgCol title="3D场景" field="scene3d" hidden="true" queryMode="single" formatterjs="btListFileFormatter" width="0"></t:dgCol>
            <t:dgCol title="位置" field="address" queryMode="single" width="0"></t:dgCol>
            <t:dgCol title="坐标" field="position" hidden="true" queryMode="single" width="0"></t:dgCol>
            <t:dgCol title="状态" field="status" query="true" queryMode="single" dictionary="isValid" width="0"></t:dgCol>
            <t:dgCol title="是否默认" field="isDefaultView" query="true" queryMode="single" formatterjs="isDefaultViewFmt" width="0"></t:dgCol>
            <t:dgCol title="创建人" field="createBy" hidden="true" queryMode="single" dictionary="user_msg,createBy,account" popup="true" width="0"></t:dgCol>
            <t:dgCol title="创建日期" field="createDate" formatter="yyyy-MM-dd hh:mm:ss" queryMode="single" width="0"></t:dgCol>
            <t:dgCol title="更新人" field="updateBy" hidden="true" queryMode="single" dictionary="user_msg,updateBy,account" popup="true" width="0"></t:dgCol>
            <t:dgCol title="更新日期" field="updateDate" formatter="yyyy-MM-dd hh:mm:ss" hidden="true" queryMode="single" width="0"></t:dgCol>
            <t:dgCol title="备注" field="remark" hidden="true" queryMode="single" width="0"></t:dgCol>
            <t:dgCol title="操作" field="opt" width="300"></t:dgCol>
            <t:dgFunOpt title="数据显示" funname="dataView(id, name)" urlclass="ace_button" urlStyle="background-color:green;" urlfont="fa-trash-o"></t:dgFunOpt>
            <%-- <t:dgFunOpt title="组网方案" funname="netScheme(id, name)" urlclass="ace_button" urlStyle="background-color:#1a7bb9;" urlfont="fa-trash-o"></t:dgFunOpt> --%>
            <t:dgFunOpt title="设备配置" funname="deviceConf(id)" urlclass="ace_button" urlStyle="background-color:#2BAAFF;" urlfont="fa-trash-o"></t:dgFunOpt>
            <t:dgFunOpt title="设备部署" funname="deviceDep(id)" urlclass="ace_button" urlStyle="background-color:#B255FF;" urlfont="fa-trash-o"></t:dgFunOpt>
            <t:dgFunOpt title="场景部署" funname="sceneDep(id, name)" urlclass="ace_button" urlStyle="background-color:#FF79BA;" urlfont="fa-trash-o"></t:dgFunOpt>
            <t:dgDelOpt title="删除" url="sceneController.do?doDel&id={id}" urlclass="ace_button" urlfont="fa-trash-o" operationCode="del"/>
            <t:dgToolBar title="录入" icon="fa fa-plus" url="sceneController.do?goAdd" funname="add" width="100%" height="100%"></t:dgToolBar>
            <t:dgToolBar title="编辑" icon="fa fa-edit" url="sceneController.do?goUpdate" funname="update" width="100%" height="100%"></t:dgToolBar>
            <t:dgToolBar title="批量删除" icon="fa fa-remove" url="sceneController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
            <t:dgToolBar title="查看" icon="fa fa-search" url="sceneController.do?goUpdate" funname="detail" width="100%" height="100%"></t:dgToolBar>
            <t:dgToolBar title="默认场景" icon="fa fa-search" url="sceneController.do?doSetDefectScene" funname="setDefault" width="100%" height="100%"></t:dgToolBar>
        </t:datagrid>
    </div>
</div>
<div
        data-options="region:'east',
		title:'场景操作',
		collapsed:true,
		split:true,
		border:false,
		onExpand : function(){
			li_east = 1;
		},
		onCollapse : function() {
			li_east = 0;
		}"
        style="width: 300px; overflow: hidden;">
    <div class="easyui-panel" style="padding: 0px; border: 0px" fit="true" border="false" id="sceneDetailpanel"></div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        var li_east = 0;
    });

    function setDefault(title, url, id) {
        gridname = id;
        var rowsData = $('#' + id).datagrid('getSelections');
        if (!rowsData || rowsData.length == 0) {
            tip('<t:mutiLang langKey="common.please.select.edit.item"/>');
            return;
        }

        url += '&id=' + rowsData[0].id;
        $.dialog.confirm('确定设置为默认场景吗？', function () {
            lockuploadify(url, '&id');
        }, function () {
        });
    }

    function lockuploadify(url, id) {
        $.ajax({
            async: false,
            cache: false,
            type: 'POST',
            url: url,// 请求的action路径
            error: function () {// 请求失败处理函数

            },
            success: function (data) {
                var d = $.parseJSON(data);
                if (d.success) {
                    var msg = d.msg;
                    tip(msg);
                    reloadTable();
                }
            }
        });
    }

    function dataView(id, name) {
        addOneTab('数据展示' + "[" + name + "]", "iotDataController.do?goSceneViewFrame&id=" + id);
    }

    function netScheme(id, name) {
        addOneTab('组网方案' + "[" + name + "]", "iotDataController.do?goSceneViewFrame&id=" + id);
    }

    function isDefaultViewFmt(value, row, index) {
        return value == 'true' ? "<p style='color:green;'>默认</p>" : "<p style='color:#aaa;'>非默认</p>"
    }

    function deviceConf(id) {
        if (li_east == 0) {
            $('#sceneList-layout').layout('expand', 'east');
        }
        $('#sceneDetailpanel').panel("refresh", "baseDeviceController.do?goDeviceSelect&sceneBy=" + id);
    }

    function deviceDep(id) {
        if (li_east == 0) {
            $('#sceneList-layout').layout('expand', 'east');
        }
        $('#sceneDetailpanel').panel("refresh", "sceneDeviceDepolyController.do?goDeployTree&sceneBy=" + id);
    }

    function sceneDep(id, name) {
        addOneTab('场景部署' + "[" + name + "]", "sceneController.do?goSceneDeployFrame&id=" + id);
    }

    //导入
    function ImportXls() {
        openuploadwin('Excel导入', 'sceneController.do?upload', "sceneList");
    }

    //导出
    function ExportXls() {
        JeecgExcelExport("sceneController.do?exportXls", "sceneList");
    }

    //模板下载
    function ExportXlsByT() {
        JeecgExcelExport("sceneController.do?exportXlsByT", "sceneList");
    }
</script>
