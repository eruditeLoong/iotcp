<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<title>场景部署</title>
<style type="text/css">
	frame{
		/* border: 1px solid #aaa; */
		margin: 0px;
		padding: 0px;
	}
</style>
</head>
<frameset id="sdFrameset" cols="15%,80%">
	<frame id="sceneListFrame"   name="sceneListFrame"   src="sceneController.do?goSceneListFrame" frameborder="0"/>
	<frame id="sceneDeployFrame" name="sceneDeployFrame" src="sceneController.do?goSceneDeployFrame" frameborder="0"/>
	<!-- <frame id="deviceFrame"      name="deviceFrame"      src="sceneController.do?goDeviceFrame" frameborder="0"/> -->
</frameset>
<!-- <div class="easyui-layout" fit="true">
		<div data-options="region:'east',title:'设备列表', border:true" style="width: 150px; overflow: hidden">
			<div id="deviceList"></div>
		</div>
		<div data-options="region:'west',title:'场景列表'" style="width: 150px;">
			<div id="sceneList"></div>
		</div>
		<div data-options="region:'center',title:'部署面板'">
			<div id="ThreeJS"></div>
		</div>
	</div> -->

</html>