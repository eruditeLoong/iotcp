<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<title>基础设备配置</title>
<link rel="stylesheet" type="text/css" href="plug-in/ztree/css/zTreeStyle.css">
<script type="text/javascript" src="plug-in/ztree/js/jquery.ztree.core-3.5.min.js"></script>
<script type="text/javascript" src="plug-in/ztree/js/ztreeCreator.js"></script>
<script type="text/javascript" src="plug-in/ztree/js/jquery.ztree.excheck-3.5.min.js"></script>

<script type="text/javascript">
	var zTree;
	function selecrAll() {
		zTree.checkAllNodes(true);
	}
	function reset() {
		zTree.checkAllNodes(false);
	}
	function save() {
		var confIds = "";
		var checkedNodes = zTree.getCheckedNodes() || [];
		console.log(checkedNodes);
		var objs = [];
		for (var i = 0; i < checkedNodes.length; i++) {
			var obj = {};
			/* 判断treeNode中是否有entity项，有说明是设备配置项 */
			if (checkedNodes[i].hasOwnProperty('entity')) {
				obj.deviceConfBy = checkedNodes[i].id;
			} else {
				// 没有说明是设备项
				obj.deviceConfBy = checkedNodes[i].pid;
				obj.deviceBy = checkedNodes[i].id;
			}
			objs.push(obj);
		}
		var sceneId = "${sceneBy}";
		$.ajax({
			url : "sceneDeviceController.do?doBatchUpdate",
			type : "POST",
			dataType : 'json',
			data : JSON.stringify({
				sceneId : sceneId,
				ids : objs
			}),
			contentType : "application/json;charsetset=UTF-8", //缺失会出现URL编码，无法转成json对象
			success : function(result) {
				var msg = result.msg;
				tip(msg);
				reloadTable();
			}
		});
	}

	$(document).ready(function() {
		var setting = {
			view : {
				selectedMulti : true, //设置是否能够同时选中多个节点
				showIcon : true, //设置是否显示节点图标
				showLine : true, //设置是否显示节点与节点之间的连线
				showTitle : true, //设置是否显示节点的title提示信息
			},
			check : {
				enable : true,
				chkStyle : "checkbox",
				chkboxType : {
					"Y" : "ps",
					"N" : "s"
				}
			},
			callback : {
				onClick : function(e, treeId, treeNode, clickFlag) { //用于捕捉勾选时触发
					zTree.checkNode(treeNode, !treeNode.checked, true);//勾选或取消勾选单个节点
				}
			}
		};

		var sceneId = document.getElementById("sceneId").value;
		//加载tree
		$.getJSON("sceneDeviceController.do?getDeviceConfTree&sceneId=" + sceneId, function(result) {
			if (result.success) {
				var objs = result.obj || [];
				zTree = $.fn.zTree.init($("#deviceConfList"), setting, objs);
			}
		});
	});
</script>
<div class="easyui-panel" title="基础设备配置" style="padding: 5px;" fit="true" border="false" id="functionListPanel">
	<input type="hidden" name="sceneId" value="${sceneBy}" id="sceneId">
	<p style="color: #555;">
		<i class="fa fa-info-circle"></i>
		该场景下需要用到哪些基础设备，请勾选并保存。
	</p>
	<a id="selecrAllBtn" onclick="selecrAll();" class="ace_button">
		<t:mutiLang langKey="select.all" />
	</a>
	<a id="resetBtn" onclick="reset();" class="ace_button" style="background-color: #A9AE99">
		<t:mutiLang langKey="common.reset" />
	</a>
	<a id="resetBtn" onclick="save();" class="ace_button" style="background-color: #AD3E74"> 保存 </a>
	<br>
	<ul id="deviceConfList" class="ztree"></ul>
</div>
