<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<title>场景列表</title>
<t:base type="jquery,easyui,tools"></t:base>
<link rel="stylesheet" type="text/css" href="plug-in/ztree/css/metroStyle.css">
<script type="text/javascript" src="plug-in/ztree/js/jquery.ztree.core-3.5.min.js"></script>
<script type="text/javascript" src="plug-in/ztree/js/ztreeCreator.js"></script>

<style type="text/css">
body {
	margin: 0px;
	padding: 0px;
	font-family: Monospace;
	font-size: 16px;
	line-height: 30px;
	width: 100%;
	height: 100%;
}
</style>
</head>
<body>
	<div id="deviceTitle" class="easyui-panel" title="场景列表" style="width: auto;">
		<ui id="sceneList" class="ztree"></ui>
	</div>
	<script type="text/javascript">
		var setting = {
			data : {
				simpleData : {
					enable : true,
					idKey : "id", //设置之后id为在简单数据模式中的父子节点关联的桥梁
					pIdKey : "pid", //设置之后pid为在简单数据模式中的父子节点关联的桥梁和id互相对应
					rootPId : null
				//pid为null的表示根节点
				}
			},
			view : {//表示tree的显示状态
				selectMulti : false, //表示禁止多选
				showLine : true, //是否显示节点间的连线
			},
			callback : {
				onClick : onClick
			}
		};

		function onClick(event, treeId, treeNode) {
			var type = treeNode.type;
			switch (type) {
			case 'scene':
				// 根据场景id获取场景3D部署文件的路径
				$.getJSON("sceneController.do?getDeployFileUrl&sceneId=" + treeNode.id, function(result) {
					if (result.success) {
						var sceneDeployFrame = window.parent.frames['sceneDeployFrame'];
						var url = result.obj;
						sceneDeployFrame.changeScene(treeNode.id, treeNode.name, treeNode.deployInfo, url);
					}
				});
				break;
			case 'deviceConf':
				break;
			case 'device':
				searchDevice(treeNode.id);
				break;
			default:
				break;
			}
		}

		function searchDevice(deviceBy) {
			sceneDeployFrame = window.parent.frames['sceneDeployFrame'];
			devicePanel.searchDeviceById(deviceBy);
		}

		//加载tree
		$(function() {
			$.getJSON("sceneController.do?getSceneDeviceDeployTree", function(result) {
				if (result.success) {
					$.fn.zTree.init($("#sceneList"), setting, result.obj);
				}
			});
		});
	</script>
</body>
</html>