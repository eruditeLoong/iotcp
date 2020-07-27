<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
    <div region="center" style="padding:0px;border:0px">
        <t:datagrid name="positionUserList" checkbox="true" pagination="true" fitColumns="true" title="人员管理"
                    actionUrl="positionUserController.do?datagrid"
                    idField="id" sortName="createDate" fit="true" queryMode="group" pageSize="100">
            <t:dgCol title="主键" field="id" hidden="true" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="卡号" field="imei" query="true" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="姓名" field="name" query="true" queryMode="single" formatterjs="nameFmt" width="120"></t:dgCol>
            <t:dgCol title="所属单位" field="company" query="true" queryMode="single"
                     dictionary="position_user_org, id, name" width="120"></t:dgCol>
            <t:dgCol title="颜色" field="color" hidden="true" query="true" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="使用对象" field="useObject" queryMode="single" dictionary="pUseObj" width="120"></t:dgCol>
            <t:dgCol title="电话" field="telephone" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="性别" field="sex" queryMode="single" dictionary="sex" width="120"></t:dgCol>
            <t:dgCol title="年龄" field="age" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="状态" field="status" query="true" queryMode="single" dictionary="isValid" width="120"></t:dgCol>
            <t:dgCol title="创建日期" field="createDate" formatter="yyyy-MM-dd hh:mm:ss" queryMode="single"
                     width="120"></t:dgCol>
            <t:dgCol title="更新日期" field="updateDate" formatter="yyyy-MM-dd hh:mm:ss" hidden="true" queryMode="single"
                     width="120"></t:dgCol>
            <t:dgCol title="备注" field="remark" hidden="true" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
            <t:dgDelOpt title="删除" url="positionUserController.do?doDel&id={id}" urlclass="ace_button"
                        urlfont="fa-trash-o"/>
            <t:dgToolBar title="录入" icon="icon-add" url="positionUserController.do?goAdd" funname="add"
                         width="768"></t:dgToolBar>
            <t:dgToolBar title="编辑" icon="icon-edit" url="positionUserController.do?goUpdate" funname="update"
                         width="768"></t:dgToolBar>
            <t:dgToolBar title="批量删除" icon="icon-remove" url="positionUserController.do?doBatchDel"
                         funname="deleteALLSelect"></t:dgToolBar>
            <t:dgToolBar title="查看" icon="icon-search" url="positionUserController.do?goUpdate" funname="detail"
                         width="768"></t:dgToolBar>
            <t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
            <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
            <t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>
            <t:dgToolBar title="批量设置状态" icon="icon-search" funname="batchSetStatus"></t:dgToolBar>
        </t:datagrid>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
    });

    function nameFmt(value, row, index) {
        return "<span class='ace_button' style='background-color: " + row.color + "' >" + value + "</span>";
    }

    function batchSetStatus() {
        var ids = [];
        var rows = $("#positionUserList").datagrid('getSelections');
        if (rows.length > 0) {
            for (var i = 0; i < rows.length; i++) {
                ids.push(rows[i].id);
            }
            //询问框
            layer.confirm('请选择状态', {
                closeBtn: 0,
                shadeClose: true,
                btn: ['有效','无效'] //按钮
            }, function(){
                setStatus(1);
            }, function(){
                setStatus(0);
            });
        } else {
            tip('请选择一行数据！');
        }
        function setStatus(status){
            $.ajax({
                url: 'positionUserController.do?doBatchSetStatus',
                type: 'post',
                data: {
                    ids: ids.join(','),
                    status: status
                },
                cache: false,
                success: function (data) {
                    var d = $.parseJSON(data);
                    if (d.success) {
                        var msg = d.msg;
                        tip(msg);
                        ids = '';
                        reloadTable();
                    }
                }
            });
        }
    }

    //导入
    function ImportXls() {
        openuploadwin('Excel导入', 'positionUserController.do?upload', "positionUserList");
    }

    //导出
    function ExportXls() {
        JeecgExcelExport("positionUserController.do?exportXls", "positionUserList");
    }

    //模板下载
    function ExportXlsByT() {
        JeecgExcelExport("positionUserController.do?exportXlsByT", "positionUserList");
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
