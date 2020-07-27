<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
	<title>3D场景部署</title>
	<script src="<%=basePath%>/plug-in/jquery/jquery.min-1.12.4.js"></script>
	<script src="<%=basePath%>/plug-in/jquery/jquery-migrate-1.2.1.js"></script>
	<script src="<%=basePath%>/plug-in/easyui/jquery.easyui.min.js"></script>
	<script src="<%=basePath%>/plug-in/easyui/locale/zh-cn.js"></script>
	<script src="<%=basePath%>/plug-in/jquery-plugs/i18n/jquery.i18n.properties.js"></script>
	<script src="<%=basePath%>/plug-in/tools/curdtools.js"></script>
	<script src="<%=basePath%>/plug-in/lhgDialog/lhgdialog.min.js?skin=metrole"></script>
	<link rel="stylesheet" href="<%=basePath%>/plug-in/easyui/themes/metrole/easyui.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=basePath%>/plug-in/ace/css/font-awesome.css" type="text/css"></link>

	<script src="plug-in/three/build/three.js"></script>
	<script src="plug-in/three/examples/js/libs/dat.gui.min.js"></script>
	<script src="plug-in/three/examples/js/libs/stats.min.js"></script>
	<script src="plug-in/three/examples/js/libs/tween.min.js"></script>

	<script src="plug-in/three/examples/js/controls/OrbitControls.js"></script>
	<script src="plug-in/three/examples/js/controls/TransformControls.js"></script>
	<script src="plug-in/three/examples/js/renderers/CSS2DRenderer.js"></script>

	<script src="plug-in/three/src/loaders/TextureLoader.js"></script>
	<script src="plug-in/three/src/loaders/ObjectLoader.js"></script>
	<script src="plug-in/three/src/loaders/JSONLoader.js"></script>
	<script src="plug-in/three/examples/js/loaders/FBXLoader.js"></script>
	<script src="plug-in/three/examples/js/libs/inflate.min.js"></script>
	<script src="plug-in/mqtt/mqttws31.js"></script>

	<script type="text/javascript" src="plug-in/echart/echarts.min.js"></script>

	<script src="plug-in/layui-v2.4.3/layui.all.js"></script>
	<link href="plug-in/layui-v2.4.3/css/layui.css" rel="stylesheet">

	<script type="text/javascript" src="plug-in/xuanzhuan/js/jquery.rotate.min.js"></script>

	<script type="text/javascript" src="webpage/com/jeecg/scene/js/scene.js"></script>
	<script type="text/javascript" src="webpage/com/jeecg/scene/js/label.js"></script>
	<script type="text/javascript" src="webpage/com/jeecg/scene/js/storage.js"></script>
	<script type="text/javascript" src="webpage/com/jeecg/scene/js/device.js"></script>
    <script type="text/javascript" src="webpage/com/jeecg/scene/js/loader.js"></script>
    <script type="text/javascript" src="webpage/com/jeecg/scene/js/scenePanel.js"></script>
    <script type="text/javascript" src="webpage/com/jeecg/scene/js/toolbar.js"></script>
    <script type="text/javascript" src="webpage/com/jeecg/scene/js/deploy.js"></script>

    <script type="text/javascript" src="webpage/com/jeecg/data/js/devicePanel.js"></script>
    <script type="text/javascript" src="webpage/com/jeecg/data/js/deviceStatus.js"></script>
    <script type="text/javascript" src="webpage/com/jeecg/data/js/deviceRelation.js"></script>

    <script type="text/javascript" src="webpage/com/jeecg/position/js/fence-position.js"></script>

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

		#toolBarBottom {
			position: absolute;
			bottom: 20px;
			width: 100%;
			text-align: center;
		}

		#toolBarBottom button {
			padding: 5px;
			color: rgba(0, 0, 0, 0.75);
			background-color: rgba(30, 144, 255, 0.5);
			border: 1px solid rgba(127, 255, 255, 0.75);
			cursor: pointer;
			border-radius: 5px
		}

		#toolBarBottom button:hover {
			background-color: rgba(46, 50, 246, 0.5);
		}

		#toolBarBottom button:active {
			color: #000000;
			background-color: rgba(0, 255, 255, 0.75);
		}

		.label-info {
			border-radius: 5px;
			border: 1px solid rgba(30, 255, 222, 0.75);
			color: #FFF;
			padding: 0px 5px;
			background: rgba(26, 47, 79, 0.5);
		}

		.parentSelect {
			z-index: 9999;
			position: absolute;
			width: 200px;
			height: 100px;
			color: rgba(127, 255, 255, 1);
			background-color: rgba(26, 47, 79, 0.75);
			border: 2px solid rgba(127, 255, 255, 0.75);
			border-radius: 10px;
			text-align: center;
			position: absolute;
			bottom: 0px;
			right: 0px;
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

		.datagrid-header, .datagrid-toolbar, .datagrid-pager, .datagrid-header, .datagrid-toolbar, .datagrid-pager, .datagrid-footer-inner, .datagrid-td-rownumber, .datalist,
		.datagrid-group, .m-list, .m-list-group, .grid-header-background-colo, .datagrid, .panel-body, .datagrid-header-row {
			background-color: transparent !important;
			background: transparent !important;
			color: #fff;
		}

		.datalist, .datagrid-group, .m-list li, .datagrid-header td, .datagrid-body td, .datagrid-footer td, .m-list, .m-list-group {
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

		.tree-folder, .tree-file {
			/*background: url('') no-repeat -208px 0 !important;*/
		}

		#mousePosion {
			z-index: 9999;
			position: absolute;
			width: 200px;
			height: 60px;
			color: rgba(127, 255, 255, 1);
			background-color: rgba(26, 47, 79, 0.5);
			border: 1px solid rgba(127, 255, 255, 0.5);
			/* border-radius: 5px; */
			text-align: center;
			position: absolute;
			bottom: 0px;
			right: 0px;
		}

		#mousePosion p {
			height: 20px;
			line-height: 20px;
		}
	</style>
</head>
<body>
<div id="container"></div>
<div id="mousePosion"></div>
<div id="imgCompass" title="场景在中心位置时指针最准确，点击还原场景" onclick="sceneObj.resetScene();"></div>

<!-- <div id="rightBtn">
    <button onclick="javascript:rightFrame();"></button>
</div> -->
<script type="text/javascript">
	//设置全局变量
	var scene, camera, renderer, labelRenderer, orbit, control;
	var stats, gui;

	var SCREEN_WIDTH = window.innerWidth, SCREEN_HEIGHT = window.innerHeight;
	var VIEW_ANGLE = 45, ASPECT = SCREEN_WIDTH / SCREEN_HEIGHT, NEAR = 1, FAR = 100000;

	var mouse, raycaster;
	var rollOverMesh, rollOverMaterial, isDeploy = false;
	var rollOverGeo;
	var attachObj;
	var objects = [];
	var selectObj;
	var container = document.getElementById("container");
	var leftOpen = true, rightOpen = true;
	var mouseDownBoxHelper, mouseMoveBoxHelper;

	// import { Line2 } from 'plug-in/three/examples/js/lines/Line2.js';
	// import { LineMaterial } from 'plug-in/three/examples/js/lines/LineMaterial.js';
	// import { LineGeometry } from 'plug-in/three/examples/js/lines/LineGeometry.js';
	// import { GeometryUtils } from 'plug-in/three/examples/js/utils/GeometryUtils.js';

	$('#toolBarBottom').hide();

	function detachObj() {
		control.detach();
		attachObj = null;
		setToolBar("hide");
	}

	function deleteObj() {
		scene.remove(attachObj);
		objects.splice(objects.indexOf(attachObj), 1);
		detachObj();
	}

	function btnTip(title, obj) {
		layer.tips(title, obj, {
			tips: [1, '#01AAED'],//弹出方向 上下左右(1-4) 颜色
			time: 10000,//3s后消失
		});
	}

	function setToolBar(status) {
		if (status === "show") {
			$('#toolBarBottom').show(500);
		} else if (status === "hide") {
			$('#toolBarBottom').hide(500);
		} else {
			$('#toolBarBottom').hide(500);
		}
	}

	function saveData() {
		var objs = objects || [];
		$.dialog.confirm("您确定要保存场景所有位置信息么吗？", function (data) {
			var deviceData = [];
			var sceneId;
			for (var i = 0; i < objs.length; i++) {
				if (objs[i].name !== "ground") {
					// 1、先保存场景数据
					if (objs[i].userData.type == 'scene') {
						sceneId = objs[i].userData.id;
						console.log(objs[i]);
						var threeData = {
							x: (objs[i].position.x-0).toFixed(2), // 位置坐标
							y: (objs[i].position.y-0).toFixed(2),
							z: (objs[i].position.z-0).toFixed(2),
							s: (objs[i].scale.x-0).toFixed(1), // 缩放比例，取x代表所有
							rx: (objs[i].rotation.x-0).toFixed(1), // 旋转
							ry: (objs[i].rotation.y-0).toFixed(1),
							rz: (objs[i].rotation.z-0).toFixed(1)
						};
						var data = {
							id: sceneId,
							threeData: JSON.stringify(threeData)
						};
						$.post("sceneController.do?doUpdate", data, function (result) {
							// tip(result.msg);
							$.dialog.tips(result.msg, 3, 'tips.gif', function () {
							});
						});
					} else {
						// 2、再保存设备数据
						var threeData = {
							x: (objs[i].position.x-0).toFixed(2), // 位置坐标
							y: (objs[i].position.y-0).toFixed(2),
							z: (objs[i].position.z-0).toFixed(2),
							s: (objs[i].scale.x-0).toFixed(1), // 缩放
							rx: (objs[i].rotation.x-0).toFixed(1), // 旋转
							ry: (objs[i].rotation.y-0).toFixed(1),
							rz: (objs[i].rotation.z-0).toFixed(1),
						}
						var data = {
							id: objs[i].uuid,
							deviceConfBy: objs[i].userData.confBy,
							deviceBy: objs[i].userData.deviceBy,
							deviceParentBy: objs[i].userData.parentBy,
							deviceCode: objs[i].userData.code,
							threeData: threeData
						};
						deviceData.push(data);
					}
				}
			}
			var json = {
				sceneId: sceneId,
				deviceData: deviceData
			};
			$.ajax({
				url: "sceneController.do?doUpdateDevice",
				type: "POST",
				dataType: 'json',
				data: JSON.stringify(json),
				contentType: "application/json;charsetset=UTF-8", //缺失会出现URL编码，无法转成json对象
				success: function (result) {
					// tip(result.msg);
					$.dialog.tips(result.msg, 3, 'tips.gif', function () {
					});
				}
			});
		}, function () {
			$.dialog.tips("操作取消！", 3, 'tips.gif', function () {
			});
		});
	}

	init();

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
		// document.body.appendChild( renderer.domElement );
		container.appendChild(renderer.domElement);
		renderer.shadowMap.enabled = true;
		renderer.shadowMap.type = THREE.PCFSoftShadowMap;

		labelRenderer = new THREE.CSS2DRenderer();
		labelRenderer.setSize(window.innerWidth, window.innerHeight);
		labelRenderer.domElement.style.position = 'absolute';
		labelRenderer.domElement.style.top = 0;
		container.appendChild(labelRenderer.domElement);
	}

	// 2.场景
	function initScene() {
		scene = new THREE.Scene();
		scene.background = new THREE.Color(0xcbe9f2);
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
		// container.addEventListener('keydown', onKeyDown, false);
	}

	//5.控制
	function initControls() {
		orbit = new THREE.OrbitControls(camera, labelRenderer.domElement, renderer.domElement);
		// orbit = new THREE.OrbitControls(camera, renderer.domElement);
		orbit.enableDamping = false;
		orbit.dampingFactor = 0;
		orbit.enableZoom = true;
		orbit.autoRotate = false;
		orbit.minDistance = 5;
		orbit.maxDistance = 100000;
		//是否开启右键拖拽
		orbit.enablePan = true;
		orbit.update();
		orbit.addEventListener('change', render);

		control = new THREE.TransformControls(camera, labelRenderer.domElement, renderer.domElement);
		// control = new THREE.TransformControls( camera, renderer.domElement );
		control.addEventListener('change', render);

		control.addEventListener('dragging-changed', function (event) {
			orbit.enabled = !event.value;
		});
		scene.add(control);
	}

	//6.光源
	var ambientLight;
	var directionalLight;

	function initLight() {
		/* scene.add(new THREE.AmbientLight(0xaaaaaa));
		light = new THREE.DirectionalLight(0xffffff, 0.6);
		light.position.set(0, 200, 0);
		scene.add(light); */
		ambientLight = new THREE.AmbientLight(0xaaaaaa)
		scene.add(ambientLight); // 环境光源
		directionalLight = new THREE.DirectionalLight(0xffffff, 1); // 平行光源 例如太阳光
		// directionalLight.castShadow = true;
		directionalLight.position.set(50, 200, 100);
		directionalLight.position.multiplyScalar(1.3);
		directionalLight.castShadow = true;
		directionalLight.shadow.mapSize.width = 1024;
		directionalLight.shadow.mapSize.height = 1024;
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
		stats.domElement.style.height = '50px';
		stats.domElement.style.width = '80px';
		stats.domElement.style.right = '0px';
		stats.domElement.style.bottom = '61px';
		// document.body.appendChild(stats.domElement);
		container.appendChild(stats.domElement);
	}

	var param;

	function initGUI() {
		gui = new dat.GUI();
		gui.open();
		param = {
			"name": '',
			"code": '',
			"positionX": '0',
			"positionY": '0',
			"positionZ": '0',
			"rotationX": '0',
			"rotationY": '0',
			"rotationZ": '0',
			"scale": '1.0',
			"save": function () {
				saveData();
			}
		};
		gui.add(param, 'save').name("保存场景");

		var objName = gui.addFolder('名称');
		objName.open();
		objName.add(param, 'name').name('对象名称').onChange(function (val) {
			attachObj.name = val;
			attachObj.userData.name = val;
			render();
		}).listen();
		objName.add(param, 'code').name('对象编码').onChange(function (val) {
			var objs = objects || [];
			for (var i in objs) {
				// 排除跟自己比较
				if (objs[i] != attachObj) {
					// 比较模型名称相同
					if (attachObj.name == objs[i].name) {
						if (val == objs[i].userData.code) {
							$.dialog.alert('模型对象的编码冲突，请修改对象编码！', function () {
							});
							return;
						}
					}
				}
			}
			attachObj.userData.code = val;
			render();
		}).listen();
		var position = gui.addFolder('位置');
		position.open();
		position.add(param, 'positionX').name('x').onChange(function (val) {
			attachObj.position.x = val - 0;
			render();
		}).listen();
		position.add(param, 'positionY').name('y').onChange(function (val) {
			attachObj.position.y = val - 0;
			render();
		}).listen();
		position.add(param, 'positionZ').name('z').onChange(function (val) {
			attachObj.position.z = val - 0;
			render();
		}).listen();

		var rotation = gui.addFolder('旋转');
		rotation.open();
		rotation.add(param, 'rotationX', -2, 2, 0.1).name('x').onChange(function (val) {
			attachObj.rotation.x = val * Math.PI; //-逆时针旋转,+顺时针
			render();
		}).listen();
		rotation.add(param, 'rotationY', -2, 2, 0.1).name('y').onChange(function (val) {
			attachObj.rotation.y = val * Math.PI;
			render();
		}).listen();
		rotation.add(param, 'rotationZ', -2, 2, 0.1).name('z').onChange(function (val) {
			attachObj.rotation.z = val * Math.PI;
			render();
		}).listen();

		var scale = gui.addFolder('缩放');
		scale.open();
		scale.add(param, 'scale', 0.1, 10, 0.1).name('x,y,z').onChange(function (val) {
			attachObj.scale.x = (val-0).toFixed(2);
			attachObj.scale.y = (val-0).toFixed(2);
			attachObj.scale.z = (val-0).toFixed(2);

			render();
		}).listen();
	}

	function setObjProperty(obj) {
		if (obj == undefined) {
			return false;
		}
		param['name'] = obj.name;
		param['code'] = obj.userData.code;
		param['positionX'] = parseFloat(obj.position.x).toFixed(2);
		param['positionY'] = parseFloat(obj.position.y).toFixed(2);
		param['positionZ'] = parseFloat(obj.position.z).toFixed(2);
		param['rotationX'] = (obj.rotation.x / Math.PI).toFixed(1);
		param['rotationY'] = (obj.rotation.y / Math.PI).toFixed(1);
		param['rotationZ'] = (obj.rotation.z / Math.PI).toFixed(1);
		param['scale'] = (obj.scale.x-0).toFixed(1);
		render();
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


	// 初始化OBJ对象
	var ground;

	function initObject() {
		// 加载地面
		/* THREE.ImageUtils.loadTexture("plug-in/three/examples/textures/terrain/grasslight-big.jpg", null, function(t) {
			t.wrapS = t.wrapT = THREE.RepeatWrapping;
			t.repeat.set(100, 100);

			var geometry = new THREE.PlaneGeometry(5000, 5000);
			var material = new THREE.MeshBasicMaterial({
				map : t
			});
			plane = new THREE.Mesh(geometry, material);
			plane.rotation.x -= Math.PI / 2;
			plane.receiveShadow = true;
			scene.add(plane);
			objects.push( plane );
		}); */

		/* ground = new THREE.Mesh(new THREE.PlaneBufferGeometry(100000, 100000), new THREE.MeshPhongMaterial({
			color : 0x999999,
			depthWrite : false
		}));
		ground.rotation.x = -Math.PI / 2;
		ground.receiveShadow = true;
		ground.name = "ground";
		scene.add(ground);
		objects.push(ground); */

		var grid = new THREE.GridHelper(100000, 50, 0x555555, 0xaaaaaa);
		grid.material.opacity = 0.6;
		grid.material.transparent = true;
		scene.add(grid);

		//坐标轴辅助
		var axes = new THREE.AxisHelper(100000);
		scene.add(axes);

		// 随鼠标移动的物体
		/* rollOverGeo = new THREE.BoxBufferGeometry(20, 20, 20);
		rollOverMaterial = new THREE.MeshBasicMaterial({
			color : 0xff5500,
			opacity : 0.5,
			transparent : true
		});
		rollOverMesh = new THREE.Mesh(rollOverGeo, rollOverMaterial); */

		/* let geometry = new THREE.Geometry();
		const p1 = new THREE.Vector3(0, -100, 0);
		const p2 = new THREE.Vector3(0, 1000, 0);
		geometry.vertices.push(p1, p2);
		let material = new THREE.LineBasicMaterial({
			color : 0xff0000
		});
		rollOverMesh = new THREE.Line(geometry, material);
		scene.add(rollOverMesh); */

		// 射线
		raycaster = new THREE.Raycaster();
		mouse = new THREE.Vector2();

		storage.init(function () {
			// 加载默认场景
			sceneObj.initScene(${scene});
		});
	}

	/* 获取射线与平面相交的交点 */
	function getGroudPosition() {
		/* 获取射线 */
		var ray = raycaster.ray;
		/* 创建平面 */
		var normal = new THREE.Vector3(0, 1, 0);
		var planeGround = new THREE.Plane(normal, 0);
		/* 计算相机到射线的对象，可能有多个对象，返回一个数组，按照距离相机远近排列 */
		var intersects = ray.intersectPlane(planeGround);
		if(intersects){
			$('#mousePosion').html("<p>x：" + intersects.x + "</p><p>y：" + intersects.y + "</p><p>z：" + intersects.z + "<p>");
		}
	}

	function onDocumentMouseMove(event) {
		num++;
		if (num > 2) {
			tFlag = false;
		}
		mouse.x = (event.clientX / window.innerWidth) * 2 - 1;
		mouse.y = -(event.clientY / window.innerHeight) * 2 + 1;
		raycaster.setFromCamera(mouse, camera);
		var intersects = raycaster.intersectObjects(objects);
		if (isMouseDown) {
			setObjProperty(attachObj);
		}
		// 获取鼠标位置坐标
		getGroudPosition();
		if (intersects.length > 0) {
			var intersect = intersects[0];
			if (isDeploy) {
				rollOverMesh.position.copy(intersect.point).add(intersect.face.normal);
				// rollOverMesh.position.divideScalar(1).floor().multiplyScalar(1).addScalar(1);
			} else {
			}
		}
		render();
	}

	var timmerHandle;
	var tFlag = false;
	var isMouseDown = false;
	var num = 0;

	function onDocumentMouseDown(event) {
		tFlag = true;
		isMouseDown = true;
		timmerHandle = setTimeout(function () {
			tFlag = false;
			event.stopPropagation();
		}, 300);
	}

	function onDocumentMouseUp(event) {
		isMouseDown = false;
		if (!tFlag) {
			return false;
		}
		mouse.x = (event.clientX / window.innerWidth) * 2 - 1;
		mouse.y = -(event.clientY / window.innerHeight) * 2 + 1;
		raycaster.setFromCamera(mouse, camera);
		var intersects = raycaster.intersectObjects(objects, true);
		if (intersects.length > 0) {
			var intersect = intersects[0];
			if (!isDeploy && intersect.object.name == "ground") {
				//control.detach();
				return;
			}
			if (1 == event.which) {
				// 左键
				if (isDeploy) {
					var voxel = rollOverMesh.clone();
					voxel.position.copy(intersect.point).add(intersect.face.normal);
					voxel.position.divideScalar(1).floor().multiplyScalar(1).addScalar(1);
					if (voxel.name == '') {
						voxel.name = '无名';
					}
					scene.add(voxel);
					objects.push(voxel);
					control.attach(voxel);
					attachObj = voxel;
					deploy.showBar();
					// 添加一个完成后，取消 - 暂时取消
					scene.remove(rollOverMesh);
					//addLabel(voxel, 0,100,0);
					rollOverMesh = null;
					isDeploy = false;
				} else {
					var obj = intersect.object;
					obj = device.getParentGroup(obj);
					obj.hasChecked = obj.hasChecked ? false : true;
					control.attach(obj);
					attachObj = obj;
					deploy.showBar();
					setObjProperty(obj);
					// 描边
					if (mouseDownBoxHelper != undefined) {
						scene.remove(mouseDownBoxHelper);
					}
				}
			} else if (3 == event.which) {
				// 右键
				if (isDeploy) {
					isDeploy = false;
					scene.remove(rollOverMesh);
					deploy.hideBar();
				} else {
				}
			} else {

			}
		} else {
			scene.remove(mouseDownBoxHelper);
		}
	}

	//初始化函数
	function init() {
		initRender();
		initCamera();
		initScene();
		initLight();
		initControls();
		initObject();
		toolbar.create();
		deploy.createDeploy();
        fencePosition.createFence();
		initEvent();
		initStats();
		initGUI();

		animation();
	}

	// 帧循环、游戏循环
	function animation() {
		render();
		//更新控制器
		orbit.update();
		// 动画
		if (TWEEN != null && typeof (TWEEN) != 'undefined') {
			TWEEN.update();
		}
		//更新性能插件
		stats.update();
		requestAnimationFrame(animation);
	}

	// 窗口大小改变
	function onWindowResize() {
		camera.aspect = window.innerWidth / window.innerHeight;
		camera.updateProjectionMatrix();
		renderer.setSize(window.innerWidth, window.innerHeight);
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
