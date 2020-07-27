<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title></title>
<t:base type="jquery,easyui,tools"></t:base>

<script src="plug-in/three/build/three.js"></script>

<script src="plug-in/three/examples/js/controls/OrbitControls.js"></script>
<script src="plug-in/three/examples/js/controls/TransformControls.js"></script>
<script src="plug-in/three/examples/js/Detector.js"></script>
<script src="plug-in/three/examples/js/renderers/CSS2DRenderer.js"></script>

<script src="plug-in/three/src/loaders/TextureLoader.js"></script>
<script src="plug-in/three/src/loaders/ObjectLoader.js"></script>
<script src="plug-in/three/src/loaders/JSONLoader.js"></script>
<script src="plug-in/three/examples/js/loaders/FBXLoader.js"></script>
<script src="plug-in/three/examples/js/libs/inflate.min.js"></script>

<style type="text/css">
body {
	margin: 0px;
	background-color: #000000;
	color: #fff;
	font-family: Monospace;
	text-align: center;
	font-size: 15px;
	line-height: 30px;
	overflow: hidden;
}
</style>
</head>
<body>
	<script type="text/javascript">
		//设置全局变量
		var scene, camera, renderer, labelRenderer, orbit, control;

		var SCREEN_WIDTH = window.innerWidth, SCREEN_HEIGHT = window.innerHeight;
		var VIEW_ANGLE = 45, ASPECT = SCREEN_WIDTH / SCREEN_HEIGHT, NEAR = 1, FAR = 1500;

		init();

		//1.渲染器
		function initRender() {
			renderer = new THREE.WebGLRenderer();
			renderer.setPixelRatio(window.devicePixelRatio);
			renderer.setSize(window.innerWidth, window.innerHeight);
			document.body.appendChild(renderer.domElement);
		}

		// 2.场景
		function initScene() {
			scene = new THREE.Scene();
		}

		//2.相机
		function initCamera() {
			camera = new THREE.PerspectiveCamera(VIEW_ANGLE, ASPECT, NEAR, FAR);
			camera.position.set(200, 150, 200);
			camera.lookAt(0, 20, 0);
			scene.add(camera);
		}

		//4.事件
		function initEvent() {
			// 添加鼠标点击事件，捕获点击时当前选中的物体
			window.addEventListener('resize', onWindowResize, false);
		}

		//5.控制
		function initControls() {
			orbit = new THREE.OrbitControls(camera, renderer.domElement);
			orbit.enableDamping = false;
			orbit.dampingFactor = 0.25;
			orbit.enableZoom = true;
			orbit.autoRotate = true;
			orbit.minDistance = 100;
			orbit.maxDistance = 1000;
			//是否开启右键拖拽
			orbit.enablePan = true;
			orbit.update();
			orbit.addEventListener('change', render);
		}

		//6.光源
		function initLight() {
			var ambientLight;
			var directionalLight;
			ambientLight = new THREE.AmbientLight(0x555555)
			scene.add(ambientLight); // 环境光源
			directionalLight = new THREE.DirectionalLight(0xffffff, 0.6); // 平行光源 例如太阳光
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
			directionalLight.shadow.camera.far = 1000;
			scene.add(directionalLight);
		}

		//7.初始化OBJ对象
		function initObject() {
			var loader;
			var ground = new THREE.Mesh(new THREE.PlaneBufferGeometry(1000, 1000), new THREE.MeshPhongMaterial({
				color : 0x999999,
				depthWrite : false
			}));
			ground.rotation.x = -Math.PI / 2;
			ground.receiveShadow = true;
			ground.name = "ground";
			scene.add(ground);

			var grid = new THREE.GridHelper(1000, 50, 0x555555, 0xaaaaaa);
			grid.material.opacity = 0.6;
			grid.material.transparent = true;
			scene.add(grid);

			var objUrl = '${modelUrl}';
			if ((objUrl.lastIndexOf('.json') != -1) || objUrl.lastIndexOf('.obj') != -1) {
				loader = new THREE.ObjectLoader();
			} else if ((objUrl.lastIndexOf('.fbx') != -1) || objUrl.lastIndexOf('.FBX') != -1) {
				var loader = new THREE.FBXLoader();
			} else {
				loader = new THREE.ObjectLoader();
			}
			loader.load('${webRoot}/img/server/' + objUrl, function(obj) {
				obj.scale.multiplyScalar(1); // 缩放模型大小
				obj.position.set(0, 0, 0);
				scene.add(obj);
			});
		}

		//初始化函数
		function init() {
			initRender();
			initScene();
			initCamera();
			initLight();
			initControls();
			initObject();
			initEvent();
			animation();
		}

		/**
		 * [addLabel 给对象添加悬浮标签]
		 * @param {[Object]} obj  [对象]
		 * @param {[int]}    x    [x坐标]
		 * @param {[int]}    y    [y坐标]
		 * @param {[int]}    z    [z坐标]
		 */
		function addLabel(obj, x, y, z) {
			var labelDiv = document.createElement('div');
			labelDiv.className = 'objLabel';
			labelDiv.textContent = obj.name;
			labelDiv.style.marginTop = '-1em';
			container.appendChild(labelDiv);
			var label = new THREE.CSS2DObject(labelDiv);
			label.position.set(x, y, z);
			obj.add(label);
		}

		// 帧循环、游戏循环
		function animation() {
			render();
			//更新控制器
			orbit.update();
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
			renderer.render(scene, camera);
		}
	</script>
</body>

</html>