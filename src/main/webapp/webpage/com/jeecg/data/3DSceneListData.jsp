<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<title>场景部署</title>
<style type="text/css">
frame {
	margin: 0px;
	padding: 0px;
}
</style>
</head>
<frameset id="sdFrameset" cols="15%,85%">
	<frame id="sceneListFrame" name="sceneListFrame" src="iotDataController.do?goSceneListFrame" frameborder="0" />
	<frame id="sceneDeployFrame" name="sceneDeployFrame" src="iotDataController.do?goSceneViewFrame" frameborder="0" />
</frameset>
</html>