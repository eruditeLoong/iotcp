<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title></title>
<script src="plug-in/three/build/three.js"></script>
<script src="plug-in/three/build/threebsp.js"></script>
<script src="plug-in/three/build/jquery-1.8.0.min.js"></script>

<script src="plug-in/three/examples/js/libs/stats.min.js"></script>
<script src="plug-in/three/examples/js/libs/tween.min.js"></script>

<script src="plug-in/three/examples/js/controls/OrbitControls.js"></script>
<script src="plug-in/three/examples/js/Detector.js"></script>
<script src="plug-in/three/examples/js/renderers/CSS2DRenderer.js"></script>

<script src="plug-in/three/src/loaders/TextureLoader.js"></script>
<script src="plug-in/three/src/loaders/ObjectLoader.js"></script>
<script src="plug-in/three/src/loaders/JSONLoader.js"></script>
<script src="plug-in/three/examples/js/loaders/FBXLoader.js"></script>
<script src="plug-in/three/examples/js/libs/inflate.min.js"></script>

<script src="plug-in/layui-v2.4.3/layui.all.js"></script>
<link href="plug-in/layui-v2.4.3/css/layui.css" rel="stylesheet">

<t:base type="jquery,easyui"></t:base>
<link rel="stylesheet" type="text/css" href="plug-in/ztree/css/metroStyle.css">
<script type="text/javascript" src="plug-in/ztree/js/jquery.ztree.core-3.5.min.js"></script>
<script type="text/javascript" src="plug-in/ztree/js/ztreeCreator.js"></script>

<script type="text/javascript" src="plug-in/three/examples/js/libs/dat.gui.min.js"></script>

<style type="text/css" media="screen">
body {
	overflow-x: hidden;
	overflow-y: hidden;
	width: 100%;
	height: 100%;
}

div#ThreeJS {
	position: absolute;
	left: 0px;
	top: 0px;
	border: none;
	width: 100%;
	height: 100%;
	background-color: #EEEEEE;
}

#toolBar {
	position: absolute;
	bottom: 20px;
	width: 100%;
	text-align: center;
}

button {
	color: rgba(127, 255, 255, 0.75);
	background: transparent;
	outline: 1px solid rgba(127, 255, 255, 0.75);
	border: 0px;
	padding: 5px;
	margin-left:5px;
	cursor: pointer;
}

button:hover {
	background-color: rgba(0, 255, 255, 0.5);
}

button:active {
	color: #000000;
	background-color: rgba(0, 255, 255, 0.75);
}

.objLabel {
	color: #FFF;
	padding: 2px;
	background: rgba(0, 188, 212, 0.3);
}
</style>
</head>
<body>
	<div id="ThreeJS"></div>
	<div id="toolBar">
		<button title="刷新" onclick="window.location.reload();">
			<i class="layui-icon">&#xe669;</i>
		</button>
		<button title="搜索" onclick="seachObj();">
			<i class="layui-icon">&#xe615;</i>
		</button>
		<button title="自动旋转" onclick="autoRotate();">
			<i class="layui-icon">&#xe663;</i>
		</button>
		<button title="设备列表" onclick="listDevice()">
			<i class="layui-icon">&#xe669;</i>
		</button>
		<button title="" onclick="">
			<i class="layui-icon">&#xe669;</i>
		</button>
		<button title="" onclick="">
			<i class="layui-icon">&#xe669;</i>
		</button>
	</div>
	<script type="text/javascript">

		var setting = {
			data : {
				simpleData : {
					enable : true,
					idKey : "id", //设置之后id为在简单数据模式中的父子节点关联的桥梁
					pIdKey : "parentBy", //设置之后pid为在简单数据模式中的父子节点关联的桥梁和id互相对应
					rootPId : "0" //pid为null的表示根节点
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
			$("#deviceList").panel({
				content : deployDevice(treeNode)
			});
		}
	
		//加载tree
		$(function() {
			/* $.getJSON("instanceDeviceController.do?listAll", function(result) {
				if (result.ok) {
					var dbDate = result.data;
					var treeAry = [];
					// 设备信息中有位置信息的设备，初始化到地图上；没有位置信息的设备，显示在tree中
					for(var i=0;i<dbDate.length;i++){
						if(dbDate[i].coordinate == "" || dbDate[i].coordinate.length<=0){
							// 位置信息为空，tree显示
							treeAry.push(dbDate[i]);
						}else{
							// 有位置信息，初始化到地图
							initDeviceOnSence(dbDate[i]);
						}
					}
					// 利用 节点数据的 icon / iconOpen / iconClose 属性实现自定义图标
					$.fn.zTree.init($("#deviceList"), setting, treeAry);
					var devId = dbDate[0].id;
				}
			}); */
		});
	</script>
	<script>
		//设置全局变量
		var scene, camera, renderer, labelRenderer, controls, tween, door;
		var stats;

		var SCREEN_WIDTH = window.innerWidth, SCREEN_HEIGHT = window.innerHeight;
		//var VIEW_ANGLE = 45, ASPECT = SCREEN_WIDTH / SCREEN_HEIGHT, NEAR = 0.1, FAR = 20000;
		var VIEW_ANGLE = 45, ASPECT = SCREEN_WIDTH / SCREEN_HEIGHT, NEAR = 1, FAR = 5000;
		var materialArrayA = [];
		var materialArrayB = [];
		var matArray = []; // 墙纹理
		var dummy = new THREE.Object3D();//仿制品
		
		var mouse, raycaster;
		var rollOverMesh, rollOverMaterial, isDeploy = false;
		var cubeGeo, cubeMaterial;
		var panel;
		var rollOverGeo;
		var objects = [];
		init();
		
		function initDeviceOnSence(device){
			var loader = new THREE.ObjectLoader();
			loader.load('webpage/com/jeecg/scene/model.json', function(obj) {
				var dev = obj.clone();
				dev.position.set(0, 0, 0);
				dev.material.opacity = 1;
				dev.name = device.name;
				//dev.position.divideScalar(1).floor().multiplyScalar(1).addScalar(1);
				objects.push( dev );
				scene.add(dev);
			});
		}

		/**
		 * 创建时钟对象(注意是全局变量)
		 * 获取上次调用render()函数的时间
		 */
		// var clock = new THREE.Clock();
		//1.场景
		function initScene() {
			scene = new THREE.Scene();
			scene.background = new THREE.Color(0xcce0ff);
			// scene.fog = new THREE.Fog( 0xcce0ff, 500, 10000 );
			scene.background = new THREE.CubeTextureLoader().setPath('plug-in/three/examples/textures/cube/skyboxsun25deg/').load(
					[ 'px.jpg', 'nx.jpg', 'py.jpg', 'ny.jpg', 'pz.jpg', 'nz.jpg' ]);
		}

		//2.相机
		function initCamera() {
			/* camera = new THREE.PerspectiveCamera( VIEW_ANGLE, ASPECT, NEAR, FAR);
			camera.position.set(-500,800,500);
			camera.lookAt(100, 200, 200); */

			camera = new THREE.PerspectiveCamera(VIEW_ANGLE, ASPECT, NEAR, FAR);
			camera.position.set(0, 100, 150);
			scene.add(camera);
		}

		//3.渲染器
		function initRender() {
			if (Detector.webgl)
				renderer = new THREE.WebGLRenderer({
					antialias : true
				});
			else
				renderer = new THREE.CanvasRenderer();
			//设置渲染器的大小为窗口的内宽度，也就是内容区的宽度。
			renderer.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
			renderer.setPixelRatio(window.devicePixelRatio);
			container = document.getElementById('ThreeJS');
			container.appendChild(renderer.domElement);
			renderer.setClearColor(0x4682B4, 1.0);
			// 设置阴影允许
			renderer.shadowMapEnabled = true;

			labelRenderer = new THREE.CSS2DRenderer();
			labelRenderer.setSize(window.innerWidth, window.innerHeight);
			labelRenderer.domElement.style.position = 'absolute';
			labelRenderer.domElement.style.top = 0;
			container.appendChild(labelRenderer.domElement);
		}

		//4.事件
		function initEvent() {
			// 添加鼠标点击事件，捕获点击时当前选中的物体
			window.addEventListener('resize', onWindowResize, false);
			$("#ThreeJS").on("mousemove",onDocumentMouseMove);
			$("#ThreeJS").on("mousedown",onDocumentMouseDown);
			$("#ThreeJS").on("mouseup",onDocumentMouseUp);
		}

		//5.控制
		function initControls() {
			controls = new THREE.OrbitControls(camera, labelRenderer.domElement, renderer.domElement);
			controls.enableDamping = false;
			controls.dampingFactor = 0.25;
			controls.enableZoom = true;
			controls.autoRotate = false;
			controls.minDistance = 5;
			controls.maxDistance = 5000;
			controls.enablePan = true;
		}

		//6.光源
		function initLight() {
			/* scene.add(new THREE.AmbientLight(0xaaaaaa));
			light = new THREE.DirectionalLight(0xffffff, 0.6);
			light.position.set(0, 200, 0);
			scene.add(light); */

			scene.add(new THREE.AmbientLight(0x666666));
			var light = new THREE.DirectionalLight(0xdfebff, 1);
			light.position.set(50, 200, 100);
			light.position.multiplyScalar(1.3);
			light.castShadow = true;
			light.shadow.mapSize.width = 1024;
			light.shadow.mapSize.height = 1024;
			var d = 300;
			light.shadow.camera.left = -d;
			light.shadow.camera.right = d;
			light.shadow.camera.top = d;
			light.shadow.camera.bottom = -d;
			light.shadow.camera.far = 1000;
			scene.add(light);
		}

		// 7.性能工具
		function initStats() {
			stats = new Stats();
			stats.domElement.style.position = 'absolute';
			stats.domElement.style.left = '0px';
			stats.domElement.style.top = '0px';
			document.getElementById('ThreeJS').appendChild(stats.domElement);
		}
		
	//  创建调试界面
		function initGui(){
		    //  创建一个对象
		    gui = {
		        cube1X:30, //方块1 X轴位置
		        cube1Y:0, //方块1 Y轴的位置
		        cube1Z:0, //方块1 Z轴的位置
		        spotLightX:25, //聚光灯 X轴位置
		        spotLightZ:-5 //聚光灯 Z轴的位置
		    };
		    // 创建datGUI对象
		    var datGui = new dat.GUI();
		    //  添加栏目
		    var lightFolder = datGui.addFolder();
		    //  栏目中添加数值监听  gui.add(对象，属性，最小值，最大值）
		    lightFolder.add(gui, 'cube1X', 0.1, 100).onChange(function (val) {
		        //  数值变化时赋值给对象属性
		        cube1.position.x = val;
		 
		    });
		 
		}
		/**
		 * 通过x,y,z指定旋转中心，obj是要旋转的对象
		 * @param  {[type]} x   [description]
		 * @param  {[type]} y   [description]
		 * @param  {[type]} z   [description]
		 * @param  {[type]} obj [description]
		 * @return {[type]}     [description]
		 */
		function changePivot(x, y, z, obj) {
			var wrapper = new THREE.Object3D();
			wrapper.add(obj);
			wrapper.position.set(x, y, z);
			obj.position.set(-x, -y, -z);
			return wrapper;
		}

		var dummy;

		/**
		 * [objClone ]
		 * @param  {[type]}  obj        [description]
		 * @param  {[type]}  x          [description]
		 * @param  {[type]}  y          [description]
		 * @param  {[type]}  z          [description]
		 * @param  {[type]}  name       [description]
		 * @param  {Boolean} isAddScene [description]
		 * @return {[type]}             [description]
		 */
		function objClone(obj, x, y, z, angle, name, isAddScene) {
			var obj1 = obj.clone();
			obj1.name = name;
			obj1.position.set(x, y, z);
			obj1.rotation.y += angle * Math.PI;
			if (isAddScene) {
				scene.add(obj1);
			}
			addLabel(obj1, 0, 220, 0);
			return obj1;
		}

		//7.初始化OBJ对象
		function initObject() {
			var texture_dimian = THREE.ImageUtils.loadTexture("plug-in/three/examples/textures/terrain/grasslight-big.jpg", null, function(t) {
			});
			texture_dimian.wrapS = texture_dimian.wrapT = THREE.RepeatWrapping;
			texture_dimian.repeat.set(100, 100);
			// texture_dimian.anisotropy = 16;

			var geometry = new THREE.PlaneGeometry(5000, 5000);
			var material = new THREE.MeshBasicMaterial({
				map : texture_dimian
			});
			plane = new THREE.Mesh(geometry, material);
			plane.rotation.x -= Math.PI / 2;
			plane.receiveShadow = true;
			scene.add(plane);

			/* var fbx_loader = new THREE.FBXLoader();
			fbx_loader.load('webpage/com/jeecg/scene/DC.FBX', function(object) {
				object.scale.multiplyScalar(1);    // 缩放模型大小
				scene.add(object);
			}); */
			
			// 随鼠标移动的物体
			rollOverGeo = new THREE.BoxBufferGeometry( 2, 2, 2 );
			rollOverMaterial = new THREE.MeshBasicMaterial( { color: 0xff5500, opacity: 0.5, transparent: true } );
			rollOverMesh = new THREE.Mesh( rollOverGeo, rollOverMaterial );
			// scene.add(rollOverMesh);
			
			// 鼠标双击时，固定到地面上
			cubeGeo = new THREE.BoxBufferGeometry(2, 2, 2 );
			cubeMaterial = new THREE.MeshLambertMaterial( { color: 0xfeb74c } );
			
			raycaster = new THREE.Raycaster();
			mouse = new THREE.Vector2();

			var geometry = new THREE.PlaneBufferGeometry( 5000, 5000 );
			geometry.rotateX( - Math.PI / 2 );
			plane = new THREE.Mesh( geometry, new THREE.MeshBasicMaterial( { visible: false } ) );
			scene.add( plane );
			objects.push( plane );
			
			/* window.addEventListener( 'mousemove', onDocumentMouseMove, false );
			window.addEventListener( 'dblclick', onDocumentMouseDown, false );*/
			
			/* var timeout;//用于存储定时器的变量
			//#moveLeft 表示需要监听长按事件的元素
			$("#ThreeJS").mousedown(function() {
				timeout= setTimeout(function() {
					alert(2);
				}, 500);//鼠标按下0.5秒后发生alert事件
			});
			$("#ThreeJS").mouseup(function() {
				clearTimeout(timeout);//清理掉定时器
			});
			$("#ThreeJS").mouseout(function() {
				clearTimeout(timeout);//清理掉定时器
			}); */
		}
		
		function onDocumentMouseMove( event ) {
			event.preventDefault();
			mouse.set( ( event.clientX / window.innerWidth ) * 2 - 1, - ( event.clientY / window.innerHeight ) * 2 + 1 );
			raycaster.setFromCamera( mouse, camera );
			var intersects = raycaster.intersectObjects( objects );
			if ( intersects.length > 0 ) {
				var intersect = intersects[ 0 ];
				rollOverMesh.position.copy( intersect.point ).add( intersect.face.normal );
				//rollOverMesh.position.divideScalar(1).floor().multiplyScalar(1).addScalar(1);
			}
			renderer.render( scene, camera );
		}
		
		var downTime = 0;
		function onDocumentMouseDown(){
			
		}
		function onDocumentMouseUp( event ) {
			event.preventDefault();
			mouse.set( ( event.clientX / window.innerWidth ) * 2 - 1, - ( event.clientY / window.innerHeight ) * 2 + 1 );
			raycaster.setFromCamera( mouse, camera );
			var intersects = raycaster.intersectObjects( objects );
			if ( intersects.length > 0 ) {
				var intersect = intersects[ 0 ];
				if(1 == event.which){ 
					// 左键
					if(isDeploy){
						var voxel = rollOverMesh.clone();
						// voxel.material = cubeMaterial;
						voxel.position.copy( intersect.point ).add( intersect.face.normal );
						//voxel.position.divideScalar(1).floor().multiplyScalar(1).addScalar( 1 );
						if(voxel.name==''){
							voxel.name = '无名';
						}
						scene.add( voxel );
						objects.push( voxel );
						// 添加一个完成后，取消
						scene.remove(rollOverMesh);
						isDeploy = false;
					}else{
						var obj = intersect.object;
						if(obj != plane){
							obj.hasChecked = obj.hasChecked ? false : true;
							alert(obj.id);
						}
					}
				}else if(2 == event.which){
					// 中键
					if ( intersect.object !== plane ) {
						scene.remove( intersect.object );
						scene.remove( intersect.object );
						//objects.splice( objects.indexOf( intersect.object ), 1 );
					}
				}else if(3 == event.which){
					// 右键
					isDeploy = false;
					scene.remove(rollOverMesh);
				}else{
					
				}
				renderer.render( scene, camera );
			}
		}
		function callback(){
			alert('off');
		}

		//初始化函数
		function init() {
			initScene();
			initCamera();
			initRender();
			initEvent();
			initControls();
			initLight();
			initObject();
			initStats();
			initGui();

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

		// 改变相机视角
		function changeCameraPosition(obj, time, a) {
			new TWEEN.Tween(controls.target).to({
				x : obj.position.x,
				y : obj.position.y,
				z : obj.position.z
			}, time / 2).onComplete(function() {
				new TWEEN.Tween(camera.position).to({
					x : obj.position.x + a,
					y : obj.position.y,
					z : obj.position.z
				}, time / 2).start();
			}).start();
		}

		// 帧循环、游戏循环
		function animation() {
			renderer.render(scene, camera);
			labelRenderer.render(scene, camera);
			//更新控制器
			controls.update();
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
			labelRenderer.setSize(window.innerWidth, window.innerHeight);
		}
		
		function listDevice() {
			// 展开和关闭设备列表面板
		}
		
		function deployDevice(device){
			isDeploy = true;
			// scene.add(rollOverMesh);
			// 后台获取设备对象 和模型文件
			
			var loader = new THREE.ObjectLoader();
			loader.load('webpage/com/jeecg/scene/model.json', function(obj) {
				rollOverMesh = obj.clone();
				rollOverMesh.position.set(0,0,0);
				rollOverMesh.id = device.id;
				rollOverMesh.name = device.name;
				scene.add(rollOverMesh);
				// 添加完一个后，删除设备列表中的设备
				// 可以通过判断设备位置信息有无，决定tree是否显示
			});
		}

		function seachObj() {
			$("#toolBar").hide();
			//信息框-例2
			layer.msg('', {
				time : 0, //不自动关闭
				offset : 'lt', //具体配置参考：offset参数项
				content : '<div style="width:280px;min-height:150px;">\
					<table class="table">\
					<tr><td style="text-align:left;width:100px;">关键字：</td><td><input id="fskeyWord" class="form-control input-sm" value="" /></td></tr>\
					<tr><td style="text-align:left;width:100px;">结果：</td><td colspan=2 id="searchResuleTd"></td></tr>\
					</table></div>',
				btn : [ '搜索', '取消' ],
				yes : function(index) {
					var objName = $('#fskeyWord').val() || '';
					if (objName == '' || objName == undefined) {
						layer.tips('请输入关键字', '#fskeyWord', {
							tips : [ 1, '#0FA6D8' ]
						//还可配置颜色
						});
						return 0;
					}
					var obj = scene.getObjectByName(objName);
					if (obj) {
						$("#searchResuleTd").html("对象名称：" + obj.name + "</br>" + "对象ID：" + obj.id + "</br>");
						selectObj(obj);
						// changeCameraPosition(obj, 1000, -100);
					} else {
						$("#searchResuleTd").html("对象未找到！");
					}
				},
				end : function(index) {
					$("#toolBar").show();
					unselectObj();
				}
			});
		}

		selectedObjs = null;
		function selectObj(obj) {
			if (selectedObjs) {
				unselectObj();
			}
			var outlineMaterial2 = new THREE.MeshBasicMaterial({
				color : 0x00ff00,
				side : THREE.DoubleSide
			});
			var childs = obj.children;
			childs.splice(childs.length - 1, 1); // 减去最后一项
			if (childs.length > 0) {
				var childResult;
				var childBSP0;
				for (var i = 0; i < childs.length; i++) {
					var child = childs[i];
					console.log(child);
					if (child.type === "Object3D")
						continue;
					// var childMesh = new THREE.Mesh(child.geometry.clone(), outlineMaterial2);
					var baseGeometry = new THREE.BoxGeometry(child.geometry.parameters.width, child.geometry.parameters.height, child.geometry.parameters.depth);
					var childMesh = new THREE.Mesh(baseGeometry, outlineMaterial2);
					childMesh.scale.multiplyScalar(1.05);
					childMesh.position.x = child.position.x;
					childMesh.position.y = child.position.y;
					childMesh.position.z = child.position.z;

					if (i == 0) {
						childBSP0 = new ThreeBSP(childMesh);
						childResult = childBSP0.union(childBSP0);
					} else {
						var childBSP = new ThreeBSP(childMesh);
						childResult = childResult.union(childBSP);
					}
				}
				var objNew = childResult.toMesh(outlineMaterial2);
				objNew.material.flatshading = THREE.FlatShading;
				objNew.geometry.computeFaceNormals(); //重新计算几何体侧面法向量
				objNew.geometry.computeVertexNormals();
				objNew.material.needsUpdate = true; //更新纹理
				objNew.geometry.buffersNeedUpdate = true;
				objNew.geometry.uvsNeedUpdate = true;
				objNew.position.x = obj.position.x;
				objNew.position.y = obj.position.y;
				objNew.position.z = obj.position.z;
				objNew.rotation.x = obj.rotation.x;
				objNew.rotation.y = obj.rotation.y;
				objNew.rotation.z = obj.rotation.z;
				scene.add(objNew);
				selectedObjs = objNew;
			} else {
				var outlineMesh2 = new THREE.Mesh(obj.geometry.clone(), outlineMaterial2);
				outlineMesh2.scale.multiplyScalar(1.05);
				outlineMesh2.position.x = obj.position.x;
				outlineMesh2.position.y = obj.position.y;
				outlineMesh2.position.z = obj.position.z;

				outlineMesh2.rotation.x = obj.rotation.x;
				outlineMesh2.rotation.y = obj.rotation.y;
				outlineMesh2.rotation.z = obj.rotation.z;

				scene.add(outlineMesh2);
				selectedObjs = outlineMesh2;
			}
		}

		function unselectObj() {
			if (selectedObjs) {
				scene.remove(selectedObjs);
				selectedObjs = null;
			}
		}

		function autoRotate() {
			controls.autoRotate = controls.autoRotate ? false : true;
		}
	</script>
</body>
</html>