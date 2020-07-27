<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<t:base type="jquery,easyui,tools,layui,DatePicker"></t:base>
<link rel="stylesheet" href="webpage/com/jeecg/position/css/electricity.css"/>
<div id="moduleList-layout" class="easyui-layout" fit="true">
    <div region="center" style="padding: 0px; border: 0px">
        <t:datagrid name="positionModuleList" checkbox="true" pagination="true" fitColumns="true" title="定位模块信息"
                    actionUrl="positionModuleController.do?datagrid" pageSize="100"
                    idField="id" sortName="createDate" fit="true" queryMode="group">
            <t:dgCol title="IMEI" field="id" query="true" queryMode="single" width="150"></t:dgCol>
            <t:dgCol title="卡号" field="cardNo" hidden="true" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="ICCID" field="iccid" hidden="true" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="地址" field="ipPort" hidden="false" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="硬件协议" field="protocol" hidden="true" query="true" queryMode="single" dictionary="isValid"
                     width="120"></t:dgCol>
            <t:dgCol title="电量" field="electricity" queryMode="single" formatterjs="electricityFmt"
                     width="120"></t:dgCol>
            <t:dgCol title="信号" field="signals" queryMode="single" formatterjs="signalsFmt" width="120"></t:dgCol>
            <t:dgCol title="软件版本" field="version" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="时区" field="timeZoom" hidden="true" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="心跳间隔" field="aliveTime" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="在线状态" field="onlineStatus" query="true" queryMode="single" formatterjs="onlineStatusFmt"
                     width="120"></t:dgCol>
            <t:dgCol title="是否有效" field="isValid" query="true" queryMode="single" dictionary="isValid"
                     width="120"></t:dgCol>
            <t:dgCol title="创建日期" field="createDate" formatter="yyyy-MM-dd hh:mm:ss" hidden="true" queryMode="single"
                     width="120"></t:dgCol>
            <t:dgCol title="更新时间" field="updateDate" formatter="yyyy-MM-dd hh:mm:ss" queryMode="single"
                     width="150"></t:dgCol>
            <t:dgCol title="操作" field="opt" width="300"></t:dgCol>
            <t:dgDelOpt title="删除" url="positionModuleController.do?doDel&id={id}" urlclass="ace_button"
                        urlfont="fa-trash-o"/>
            <t:dgFunOpt title=" 定位配置" funname="moduleConfig(id)" urlclass="ace_button"
                        urlStyle="background-color:#2BAAFF;" urlfont="fa-check-square-o"></t:dgFunOpt>
            <t:dgFunOpt title=" 事件统计" funname="eventCount(id)" urlclass="ace_button"
                        urlStyle="background-color:#3F9008;" urlfont="fa-calendar-check-o"></t:dgFunOpt>
            <t:dgFunOpt title=" 轨迹" funname="showTrajectory(id)" urlclass="ace_button"
                        urlStyle="background-color:#379008;" urlfont="fa-pie-chart"></t:dgFunOpt>
            <t:dgToolBar title="录入" icon="icon-add" url="positionModuleController.do?goAdd" funname="add"
                         width="768"></t:dgToolBar>
            <t:dgToolBar title="编辑" icon="icon-edit" url="positionModuleController.do?goUpdate" funname="update"
                         width="768"></t:dgToolBar>
            <t:dgToolBar title="批量删除" icon="icon-remove" url="positionModuleController.do?doBatchDel"
                         funname="deleteALLSelect"></t:dgToolBar>
            <t:dgToolBar title="查看" icon="icon-search" url="positionModuleController.do?goUpdate" funname="detail"
                         width="768"></t:dgToolBar>
            <t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
            <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
            <t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>
            <t:dgToolBar title="批量修改服务器" icon="icon-search" funname="batchConfService"></t:dgToolBar>
            <t:dgToolBar title="在线统计" icon="icon-search" funname="onlineTotal"></t:dgToolBar>
        </t:datagrid>
    </div>
</div>
<div
        data-options="region:'east', title:'定位操作', collapsed:true, split:true, border:true, ifram:true,
    onExpand : function(){
        li_east = 1;
    },
    onCollapse : function() {
        li_east = 0;
    }"
        style="width: 300px; overflow: hidden;">
    <div class="easyui-panel" style="padding: 0px; border: 0px" fit="true" border="false" id="modulePanel"></div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        var li_east = 0;
    });

    /**
     * 在线状态
     * 判断根据：数据更新时间和当前时间差值大于5分钟视为
     */
    function onlineStatusFmt(value, row, index) {
        return value == 1 ? "<a class='ace_button' style='background-color:green'>在线</a>" : "<a class='ace_button' style='background-color:grey'>离线</a>"
    }

    /**
     * 电池电量
     */
    function electricityFmt(value, row, index) {
        value = value || 0;
        var pElect = document.createElement("div");
        var elect = document.createElement("div");
        elect.style = "height:20px;border:0px solid red;display:flex;alignItems:flex-end"
        elect.title = value;
        pElect.appendChild(elect);
        var e = document.createElement("div");
        e.className = "battery";
        e.style.backgroundSize = (value == 100 ? 91 : value) + "% 80%"
        var v = document.createElement("div");
        v.style = "line-height:16px;margin-left:10px";
        v.innerText = value + "%";
        elect.appendChild(e);
        elect.appendChild(v);
        return pElect.innerHTML;
    }

    /**
     * 信号强度
     * @param value
     * @param row
     * @param index
     * @returns {string}
     */
    function signalsFmt(value, row, index) {
        var pSign = document.createElement("div");
        var sign = document.createElement("div");
        sign.style = "height:20px;border:0px solid red;display:flex;align-items:flex-end";
        sign.title = value || 0;
        pSign.appendChild(sign);
        for (let i = 1; i <= 10; i++) {
            var s = document.createElement("div");
            s.style = "width:3px;margin-left:1px";
            s.style.height = (i * 2) + "px";
            if (i <= value / 10)
                s.style.backgroundColor = "#18b549";
            else
                s.style.backgroundColor = "#eee";
            sign.appendChild(s);
        }
        var v = document.createElement("div");
        v.style = "line-height:20px;margin-left:10px";
        v.innerText = value + "%";
        sign.appendChild(v);
        return pSign.innerHTML;
    }

    function moduleConfig(id) {
        if (li_east == 0) {
            $('#moduleList-layout').layout('expand', 'east');
        }
        $('#modulePanel').panel("refresh", "positionModuleController.do?goConfig&id=" + id);
    }

    function eventCount(id) {
        if (li_east == 0) {
            $('#moduleList-layout').layout('expand', 'east');
        }
        $('#modulePanel').panel("refresh", "positionModuleController.do?goEventCount&id=" + id);
    }

    function showTrajectory(id) {
        openwindow("轨迹", "positionTrajectoryController.do?maps&imei=" + id, "name", "100%", "100%");
    }

    function batchConfService() {
        var ids = [];
        var rows = $("#positionModuleList").datagrid('getSelections');
        if (rows.length > 0) {
            for (var i = 0; i < rows.length; i++) {
                ids.push(rows[i].id);
            }
            $.dialog.prompt('输入有效服务器地址',
                function (val) {
                    $.ajax({
                        url: 'positionModuleController.do?doBatchConfService',
                        type: 'post',
                        data: {
                            ids: ids.join(','),
                            service: val
                        },
                        cache: false,
                        success: function (data) {
                            var d = $.parseJSON(data);
                            if (d.success) {
                                var msg = d.msg;
                                tip(msg);
                                ids = '';
                            }
                        }
                    });
                },
                'http://'
            );
        } else {
            tip('请选择一行数据！');
        }
    }

    function onlineTotal() {
        $.ajax({
            url: 'positionModuleController.do?getOnlineCount',
            type: 'get',
            cache: false,
            success: function (data) {
                var d = $.parseJSON(data);
                if (d.success) {
                    var msg = d.msg;
                    tip('在线总数：' + msg);
                }
            }
        });
    }

    //导入
    function ImportXls() {
        openuploadwin('Excel导入', 'positionModuleController.do?upload', "positionModuleList");
    }

    //导出
    function ExportXls() {
        JeecgExcelExport("positionModuleController.do?exportXls", "positionModuleList");
    }

    //模板下载
    function ExportXlsByT() {
        JeecgExcelExport("positionModuleController.do?exportXlsByT", "positionModuleList");
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
