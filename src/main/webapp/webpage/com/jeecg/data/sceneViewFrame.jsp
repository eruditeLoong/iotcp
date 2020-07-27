<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>3D场景部署</title>
<meta name="description" content="">
<%-- <t:base type="jquery,easyui"></t:base> --%>

<script src="<%=basePath%>/plug-in/jquery/jquery.min-1.12.4.js"></script>
<script src="<%=basePath%>/plug-in/jquery/jquery-migrate-1.2.1.js"></script>
<script src="<%=basePath%>/plug-in/easyui/jquery.easyui.min.js"></script>
<script src="<%=basePath%>/plug-in/easyui/locale/zh-cn.js"></script>
<script src="<%=basePath%>/plug-in/jquery-plugs/i18n/jquery.i18n.properties.js"></script>
<script src="<%=basePath%>/plug-in/tools/curdtools.js"></script>
<script src="<%=basePath%>/plug-in/lhgDialog/lhgdialog.min.js?skin=metrole"></script>
<link rel="stylesheet" href="<%=basePath%>/plug-in/easyui/themes/metrole/easyui.css" type="text/css"></link>
<link rel="stylesheet" href="<%=basePath%>/plug-in/ace/css/font-awesome.css"></link>

<script src="<%=basePath%>/plug-in/three/build/three.js"></script>
<script src="<%=basePath%>/plug-in/three/examples/js/libs/dat.gui.min.js"></script>
<script src="<%=basePath%>/plug-in/three/examples/js/libs/stats.min.js"></script>
<script src="<%=basePath%>/plug-in/three/examples/js/libs/tween.min.js"></script>

<script src="<%=basePath%>/plug-in/three/examples/js/controls/OrbitControls.js"></script>
<script src="<%=basePath%>/plug-in/three/examples/js/controls/TransformControls.js"></script>
<script src="<%=basePath%>/plug-in/three/examples/js/renderers/CSS2DRenderer.js"></script>

<script src="<%=basePath%>/plug-in/three/src/loaders/TextureLoader.js"></script>
<script src="<%=basePath%>/plug-in/three/src/loaders/ObjectLoader.js"></script>
<script src="<%=basePath%>/plug-in/three/src/loaders/JSONLoader.js"></script>
<script src="<%=basePath%>/plug-in/three/examples/js/loaders/FBXLoader.js"></script>
<script src="<%=basePath%>/plug-in/three/examples/js/libs/inflate.min.js"></script>

<script type="text/javascript" src="<%=basePath%>/plug-in/echart/echarts.min.js"></script>

<script src="<%=basePath%>/plug-in/layui-v2.4.3/layui.all.js"></script>
<link href="<%=basePath%>/plug-in/layui-v2.4.3/css/layui.css" rel="stylesheet">

<link rel="stylesheet" type="text/css" href="plug-in/ztree/css/metroStyle.css">
<script type="text/javascript" src="plug-in/ztree/js/jquery.ztree.core-3.5.min.js"></script>
<script type="text/javascript" src="plug-in/ztree/js/ztreeCreator.js"></script>
<script type="text/javascript" src="plug-in/ztree/js/jquery.ztree.excheck-3.5.min.js"></script>
<script type="text/javascript" src="plug-in/ztree/js/jquery.ztree.exedit.min.js"></script>

<script type="text/javascript" src="<%=basePath%>/plug-in/xuanzhuan/js/jquery.rotate.min.js"></script>

<script type="text/javascript" src="<%=basePath%>/webpage/com/jeecg/scene/js/storage.js"></script>
<script type="text/javascript" src="<%=basePath%>/webpage/com/jeecg/scene/js/scene.js"></script>
<script type="text/javascript" src="<%=basePath%>/webpage/com/jeecg/scene/js/label.js"></script>
<script type="text/javascript" src="<%=basePath%>/webpage/com/jeecg/scene/js/device.js"></script>
<script type="text/javascript" src="<%=basePath%>/webpage/com/jeecg/scene/js/loader.js"></script>
<script type="text/javascript" src="<%=basePath%>/webpage/com/jeecg/scene/js/toolbar.js"></script>

<script type="text/javascript" src="<%=basePath%>/webpage/com/jeecg/data/js/devicePanel.js"></script>
<script type="text/javascript" src="<%=basePath%>/webpage/com/jeecg/data/js/alarmPanel.js"></script>
<script type="text/javascript" src="<%=basePath%>/webpage/com/jeecg/data/js/deviceStatus.js"></script>
<script type="text/javascript" src="<%=basePath%>/webpage/com/jeecg/data/js/deviceRelation.js"></script>
<script type="text/javascript" src="<%=basePath%>/webpage/com/jeecg/position/js/user-positon.js"></script>
<script type="text/javascript" src="<%=basePath%>/webpage/com/jeecg/data/js/Mqtt.js"></script>
<script type="text/javascript" src="<%=basePath%>/webpage/com/jeecg/position/js/fence-position.js"></script>
<script type="text/javascript" src="<%=basePath%>/webpage/com/jeecg/position/js/ranging.js"></script>

<%-- mqtt --%>
<script src="<%=basePath%>/plug-in/mqtt/mqttws31.js" type="text/javascript"></script>

<style type="text/css">
body {
	margin: 0px;
	background-color: #000000;
	color: #fff;
	text-align: center;
	font-size: 12px;
	line-height: 30px;
	overflow: hidden;
}

.label-warning {
	border-radius: 5px;
	border: 1px solid red;
	color: red;
	padding: 0px 5px;
	background: rgba(26, 47, 79, 0.5);
}

.iotDataDialog {
	width: 800px;
	height: 500px;
	margin: 0px;
	padding: 0px;
	color: rgba(127, 255, 255, 0.75);
	background-color: rgba(0, 0, 0, 0.13);
	background-image: url('images/window-bg.png');
	background-repeat: no-repeat;
	background-size: 100% 100%;
	-moz-background-size: 100% 100%;
}

/* 指南针 */
#imgCompass {
	width: 100px;
	height: 100px;
	background-image: url('images/compass.png');
	background-repeat: no-repeat;
	position: absolute;
	top: 80px;
	right: 280px;
}

.panel-header {
	background-color: rgba(30, 50, 100, 0.8) !important;
	background: rgba(30, 50, 100, 0.8) !important;
	color: rgba(18, 237, 183, 1);
}

.panel-title {
	color: rgba(18, 237, 183, 1);
}

.datagrid-header, .datagrid-toolbar, .datagrid-pager, .datagrid-header,
	.datagrid-toolbar, .datagrid-pager, .datagrid-footer-inner,
	.datagrid-td-rownumber, .datalist, .datagrid-group, .m-list,
	.m-list-group, .grid-header-background-colo, .datagrid, .panel-body,
	.datagrid-header-row {
	background-color: transparent !important;
	background: transparent !important;
	color: #fff;
}

.datalist, .datagrid-group, .m-list li, .datagrid-header td,
	.datagrid-body td, .datagrid-footer td, .m-list, .m-list-group {
	/* height: 18px !important; */
	border-color: #555 !important;
	color: rgba(18, 237, 183, 0.8);
}

.datagrid-row-over, .datagrid-header td.datagrid-header-over {
	background: rgba(30, 50, 100, 0.8);
	color: #FFF;
	cursor: default;
}

a {
	color: rgba(18, 237, 183, 0.8);
}

.device-status-true {
	width: 10px;
	height: 10px;
	border-radius: 12px;
	border-width: 2px;
	border-color: rgba(130, 150, 100, 0.5);
	background-color: green;
}

.device-status-false {
	width: 10px;
	height: 10px;
	border-radius: 12px;
	border-width: 2px;
	border-color: rgba(130, 150, 100, 0.5);
	background-color: red;
}

.tree-folder, .tree-file {
	background: url('') no-repeat -208px 0 !important;
}
</style>
</head>
<body>
 <div id="container"></div>
 <div id="iotDataDialog">
  <div style="width: 100%; height: 100%;">
   <div id="">
    <i class="fa fa-close" id="dialogTitle" style="text-align: center;">设备：</i>
    <i class="fa fa-close" style="float: right; margin-right: 25px; line-height: 25px; cursor: pointer; background-color: #475972" onclick="javascript:closeDialog();"> close</i>
   </div>
  </div>
 </div>
 <div id="imgCompass" title="场景在中心位置时指针最准确，点击还原场景" style="cursor: pointer;" onclick="sceneObj.resetScene();"></div>
 <script type="text/javascript">
	//设置全局变量
	var scene, camera, renderer, labelRenderer, orbit, control;
	var stats, gui, sceneObjConf;

	var SCREEN_WIDTH = window.innerWidth, SCREEN_HEIGHT = window.innerHeight;
	var VIEW_ANGLE = 45, ASPECT = SCREEN_WIDTH / SCREEN_HEIGHT, NEAR = 1, FAR = 100000;
	var mouse, raycaster;
	var objects = [];
	var selectObj;
	var container = document.getElementById("container");
	var leftOpen = true;
	var isLoaded = false;
	var mouseDownBoxHelper, mouseMoveBoxHelper;

	function closeDialog() {

		$('#iotDataDialog').hide(200);
	}

	$(document).ready(function () {
		$('#iotDataDialog').hide();
		init();
		isLoaded = true;
	});

	/**
	 * 创建时钟对象(注意是全局变量)
	 * 获取上次调用render()函数的时间
	 */
	// var clock = new THREE.Clock();
	//1.渲染器
	function initRender() {

		renderer = new THREE.WebGLRenderer();
		renderer.setPixelRatio(window.devicePixelRatio);
		renderer.setSize(window.innerWidth, window.innerHeight);
		document.body.appendChild(renderer.domElement);
		// container.appendChild(renderer.domElement);
		renderer.shadowMap.enabled = true;
		renderer.shadowMap.type = THREE.PCFSoftShadowMap;
		labelRenderer = new THREE.CSS2DRenderer();

		labelRenderer.setSize(window.innerWidth, window.innerHeight);
		labelRenderer.domElement.style.position = 'absolute';
		labelRenderer.domElement.style.top = 0;
		container.appendChild(labelRenderer.domElement);
	}

	function initScene() {

		scene = new THREE.Scene();
		scene.background = new THREE.Color(0x000000);
		// 2.场景
		scene.background = new THREE.CubeTextureLoader().setPath('webpage/com/jeecg/data/img/skyboxsun25deg/').load(['px.jpg', 'nx.jpg', 'py.jpg', 'ny.jpg', 'pz.jpg', 'nz.jpg']);
	}

	//2.相机
	function initCamera() {
		camera = new THREE.PerspectiveCamera(VIEW_ANGLE, ASPECT, NEAR, FAR);
		camera.position.set(1000, 500, 1000);
		camera.lookAt(0, 0, 0);
		//scene.add(camera);
	}

	//4.事件
	function initEvent() {
		// 添加鼠标点击事件，捕获点击时当前选中的物体
		window.addEventListener('resize', onWindowResize, false);
		container.addEventListener('mousemove', onDocumentMouseMove, false);
		container.addEventListener("mousedown", onDocumentMouseDown, false);
		container.addEventListener("mouseup", onDocumentMouseUp, false);
	}

	//5.控制
	function initControls() {
		orbit = new THREE.OrbitControls(camera, labelRenderer.domElement, renderer.domElement);
		orbit.enableDamping = false;
		orbit.dampingFactor = 0;
		orbit.enableZoom = true;
		orbit.autoRotate = false;
		orbit.minDistance = 5;
		orbit.maxDistance = 100000;
		//orbit.minPolarAngle = Math.PI / 4;
		orbit.maxPolarAngle = Math.PI / 2;
		//是否开启右键拖拽
		orbit.enablePan = true;
		orbit.update();
	}

	//6.光源
	var ambientLight;
	var directionalLight;

	function initLight() {
		ambientLight = new THREE.AmbientLight(0xaaaaaa)
		scene.add(ambientLight); // 环境光源
		directionalLight = new THREE.DirectionalLight(0xffffff, 1); // 平行光源 例如太阳光
		directionalLight.position.set(50, 200, 100);
		directionalLight.position.multiplyScalar(1.3);
		// directionalLight.castShadow = true;
		directionalLight.shadow.mapSize.width = 100000;
		directionalLight.shadow.mapSize.height = 100000;
		var d = 300;
		directionalLight.shadow.camera.left = -d;
		directionalLight.shadow.camera.right = d;
		directionalLight.shadow.camera.top = d;
		directionalLight.shadow.camera.bottom = -d;
		directionalLight.shadow.camera.far = 100000;
		scene.add(directionalLight);
	}

	// 7.性能工具
	function initStats() {
		stats = new Stats();
		stats.domElement.style.position = 'absolute';
		stats.domElement.style.left = '';
		stats.domElement.style.right = '280px';
		stats.domElement.style.top = '0px';
		stats.domElement.style.width = '80px';
		stats.domElement.id = 'stats';
		container.appendChild(stats.domElement);
	}

	function viewIotData(type, id) {
		if (type == "scene") {
			return;
		}
		if (type == "nsdevice") {
			return;
		}
		layer.open({
			type: 2,
			title: false,
			shade: 0.0001,
			offset: 'lb', //右下角弹出
			area: ['800px', '500px'],
			skin: 'iotDataDialog', //没有背景色
			closeBtn: 0,
			shadeClose: true,
			content: ['iotDataController.do?goDeviceDataView&deployDeviceBy=' + id, 'no']
		});
	}

	//7.初始化OBJ对象
	function initObject() {
		raycaster = new THREE.Raycaster();
		mouse = new THREE.Vector2();
		storage.init(function () {
			// 加载默认场景
			sceneObj.initScene(${scene});
		});
		toolbar.create();
		userPosition.loadUsers();
		fencePosition.showFence();
	}

	var timmerHandle;
	var tFlag = false;
	var num = 0;

	function onDocumentMouseDown(event) {
		event.stopPropagation();
		tFlag = true;
		num = 0
		timmerHandle = setTimeout(function () {
			tFlag = false;
			event.stopPropagation();
		}, 200);
	}

	function onDocumentMouseUp(event) {
		if (!tFlag) {
			return false;
		}
		event.preventDefault();
		mouse.x = (event.clientX / window.innerWidth) * 2 - 1;
		mouse.y = -(event.clientY / window.innerHeight) * 2 + 1;
		raycaster.setFromCamera(mouse, camera);
		var intersects = raycaster.intersectObjects(objects, true);
		if (intersects.length > 0) {
			var intersect = intersects[0];
			if (intersect.object.name == "ground") {
				return;
			}
			if (1 == event.which) {
				var obj = intersect.object;
				obj = device.getParentGroup(obj);
				obj.hasChecked = obj.hasChecked ? false : true;
				viewIotData(obj.userData.type, obj.uuid);
			}
		}
	}

	function onDocumentMouseMove(event) {
		event.preventDefault();
		num++;
		if (num > 2) {
			tFlag = false;
		}
		mouse.x = (event.clientX / window.innerWidth) * 2 - 1;
		mouse.y = -(event.clientY / window.innerHeight) * 2 + 1;
		raycaster.setFromCamera(mouse, camera);
		var intersects = raycaster.intersectObjects(objects, true);
		if (intersects.length > 0) {
			var obj = intersects[0].object;
			obj = device.getParentGroup(obj);
			// 描边
			if (mouseMoveBoxHelper != undefined) {
				scene.remove(mouseMoveBoxHelper);
			}
			mouseMoveBoxHelper = new THREE.BoxHelper(obj, '#4CFF24');
			scene.add(mouseMoveBoxHelper);
		} else {
			scene.remove(mouseMoveBoxHelper);
		}
	}

	//初始化函数
	function init() {
		console.log('init...');
		initRender();
		initCamera();
		initScene();
		initLight();
		initControls();
		initEvent();
		initStats();
		initObject();
		animation();
	}

	// 帧循环、游戏循环
	function animation() {
		render();
		//更新控制器
		orbit.update();
		//更新性能插件
		stats.update();
		// 动画
		if (TWEEN != null && typeof (TWEEN) != 'undefined') {
			TWEEN.update();
		}
		requestAnimationFrame(animation);

	}

	// 窗口大小改变
	function onWindowResize() {
		camera.aspect = window.innerWidth / window.innerHeight;
		camera.updateProjectionMatrix();
		renderer.setSize(window.innerWidth, window.innerHeight);
		labelRenderer.setSize(window.innerWidth, window.innerHeight);
		renderer.render(scene, camera);
	}

	function render() {
		//根据当前的位置计算与z轴负方向的夹角，即为正北方方向。如果使用camera的rotation.y你会发现得出的弧度制范围是-90到90，指南针就不能旋转360度了。
		var dir = new THREE.Vector3(-camera.position.x, 0, -camera.position.z).normalize();
		var theta = Math.atan2(-dir.x, -dir.z);
		//指南针旋转
		$('#imgCompass').rotate(THREE.Math.radToDeg(theta));
		renderer.render(scene, camera);
		labelRenderer.render(scene, camera);
	}
</script>
</body>

</html>