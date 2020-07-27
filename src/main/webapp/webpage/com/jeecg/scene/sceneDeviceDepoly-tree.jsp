<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<title>设备部署</title>
<link rel="stylesheet" type="text/css" href="plug-in/ztree/css/metroStyle.css">
<script type="text/javascript" src="plug-in/ztree/js/jquery.ztree.core-3.5.min.js"></script>
<script type="text/javascript" src="plug-in/ztree/js/ztreeCreator.js"></script>
<script type="text/javascript" src="plug-in/ztree/js/jquery.ztree.excheck-3.5.min.js"></script>
<script type="text/javascript" src="plug-in/ztree/js/jquery.ztree.exedit.min.js"></script>
<script type="text/javascript" src="plug-in/lhgDialog/lhgdialog.min.js?skin=metrole"></script>

</head>
<div class="easyui-panel" title="设备部署" style="padding: 5px;" fit="true" border="false" id="functionListPanel">
    <input type="hidden" name="sceneBy" value="${sceneBy}" id="sceneBy">
    <p style="color: #555;">
        <i class="fa fa-info-circle"></i>
        对所在场景下所有设备的管理，包括对场景设备的：增加、修改、删除、树显等。
    </p>
    <a id="resetBtn" onclick="expandAll();" class="ace_button" style="background-color: #AD3E74"> 折叠 </a>
    <a id="deployBtn" onclick="addDevice();" class="ace_button" style="background-color: #418214"> 添加 </a>
    <br>
    <ul id="deviceDepList" class="ztree"></ul>
    <script type="text/javascript">
        var zTree;
        var setting;
        var isExpand = true;

        function expandAll() {
            isExpand = !isExpand;
            if (isExpand) {
                zTree.expandAll(true);
                $('#resetBtn').text('折叠');
            } else {
                zTree.expandAll(false);
                $('#resetBtn').text('展开');
            }
        }

        $(document).ready(function () {
            setting = {
                data: {
                    simpleData: {
                        enable: true, // 简单数据模式
                        idKey: "id",
                        pIdKey: "pid",
                        rootPId: null
                    }
                },
                edit: {
                    enable: true,
                    editNameSelectAll: true,// 节点编辑名称 input 初次显示时,设置 txt 内容是否为全选状态。
                    removeTitle: "删除", // 删除按钮的 Title 辅助信息
                    renameTitle: "重命名" // 编辑名称按钮的 Title 辅助信息。
                },
                view: {
                    addHoverDom: addHoverDom, // 用于当鼠标移动到节点上时，显示用户自定义控件。务必与 setting.view.removeHoverDom 同时使用
                    removeHoverDom: removeHoverDom, // 用于当鼠标移出节点时，隐藏用户自定义控件。务必与 addHoverDom 同时使用
                    dblClickExpand: false,
                    selectedMulti: false,
                    showTitle: true, //设置是否显示节点的title提示信息
                },
                check: {
                    enable: false,
                    chkStyle: "radio",
                    radioType: "all"
                },
                callback: {
                    beforeDrag: beforeDrag,
                    beforeEditName: beforeEditName,
                    beforeRemove: beforeRemove,
                    beforeRename: beforeRename,
                    onRemove: onRemove,
                    onRename: onRename
                }
            };
            loaderTree();
        });

        function loaderTree() {
            var win = $.messager.progress({
                title: '请稍等',
                text: '正在加载设备部署列表...'
            });
            $.getJSON("sceneController.do?getDeployDeviceListBySceneId&sceneId=${sceneBy}", function (result) {
                var list = result.obj;
                var treeList = [];
                for (var i = 0; i < list.length; i++) {
                    var tree = {
                        id: list[i].id,
                        pid: list[i].parentBy,
                        name: list[i].name + '[' + list[i].code + ']'
                    };
                    treeList.push(tree);
                }
                zTree = $.fn.zTree.init($("#deviceDepList"), setting, treeList);
                zTree.expandAll(false);
                isExpand = false;
                $('#resetBtn').text('展开');
                $.messager.progress('close');
            });
        }

        function addHoverDom(treeId, treeNode) {
            var sObj = $("#" + treeNode.tId + "_span");
            if (treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0)
                return;
            var addStr = "<span class='button add' id='addBtn_" + treeNode.tId + "' title='添加' onfocus='this.blur();'></span>";
            sObj.after(addStr);
            var btn = $("#addBtn_" + treeNode.tId);
            if (btn)
                btn.bind("click", function () {
                });
        };

        // 用于当鼠标移出节点时，隐藏用户自定义控件
        function removeHoverDom(treeId, treeNode) {
            $("#addBtn_" + treeNode.tId).unbind().remove();
        };

        var zNodes = [];
        var log, className = "dark";

        function beforeDrag(treeId, treeNodes) {
            return false;
        }

        function beforeEditName(treeId, treeNode) {
            zTree.selectNode(treeNode);
            $.dialog({
                width: 780,
                height: 400,
                lock: true,
                content: 'url:sceneDeviceDepolyController.do?goUpdate&id=' + treeNode.id,
                ok: function () {
                    var iframe = this.iframe.contentWindow;
                    var form = iframe.document.forms[0];
                    var formData = new FormData(form);
                    iframe.document.getElementById("btn_sub").click();
                    return false;
                },
                cancelVal: '关闭',
                cancel: true
            });
            return false;
        }

        /*删除节点*/
        function beforeRemove(treeId, treeNode) {
            zTree.selectNode(treeNode);
            /* $.dialog.confirm("确认删除 节点 -- " + treeNode.name + " 吗？", function() {
                var parm = "sid=" + treeNode.id + "&fid=" + treeNode.pId + "&name=" + treeNode.name;
                $.dialog.tips('执行确定操作');
                flag = true;
            }, function() {
                $.dialog.tips('执行取消操作');
                flag = false;
            });
            return flag; */
            return true
        }

        function onRemove(e, treeId, treeNode) {
            $.getJSON("sceneDeviceDepolyController.do?doDel&id=" + treeNode.id, function (result) {
                if (result.success) {
                    // loaderTree();
                }
            });
        }

        /* 编辑节点名称 */
        function beforeRename(treeId, treeNode, newName, isCancel) {
            if (newName.length == 0) {
                setTimeout(function () {
                    zTree.cancelEditName();
                    alert("节点名称不能为空.");
                }, 0);
                return false;
            }
            return true;
        }

        function onRename(e, treeId, treeNode, isCancel) {
        }

        function showRemoveBtn(treeId, treeNode) {
            return !treeNode.isFirstNode;
        }

        function showRenameBtn(treeId, treeNode) {
            return !treeNode.isLastNode;
        }

        //获取当前节点的根节点(treeNode为当前节点)
        function getCurrentRoot(treeNode) {
            if (treeNode.getParentNode() != null) {
                var parentNode = treeNode.getParentNode();
                return getCurrentRoot(parentNode);
            } else {
                return treeNode.id;
            }
        }

        /*添加节点*/
        var newCount = 1;

        function addHoverDom(treeId, treeNode) {
            var sObj = $("#" + treeNode.tId + "_span");
            if (treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0)
                return;
            var addStr = "<span class='button add' id='addBtn_" + treeNode.tId + "' title='add node' onfocus='this.blur();'></span>";
            sObj.after(addStr);
            var btn = $("#addBtn_" + treeNode.tId);
            if (btn)
                btn.bind("click", function () {
                    var sceneBy = $('#sceneBy').val();
                    var deviceConfBy = getCurrentRoot(treeNode);
                    addDevice(deviceConfBy, treeNode.id)
                });
        }

        function addDevice(deviceConfBy, deviceParentBy) {
            var sceneBy = $('#sceneBy').val();
            $.dialog({
                width: 780,
                height: 400,
                lock: true,
                content: 'url:sceneDeviceDepolyController.do?goAdd&sceneBy=' + sceneBy + '&deviceConfBy=' + deviceConfBy + '&deviceParentBy=' + deviceParentBy,
                ok: function () {
                    var iframe = this.iframe.contentWindow;
                    var form = iframe.document.forms[0];
                    var formData = new FormData(form);
                    iframe.document.getElementById("btn_sub").click();
                    return false;
                },
                cancelVal: '关闭',
                cancel: true
            });
        }
    </script>
</div>
