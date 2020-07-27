var devicePanel = {
	top: '0px',
	left: '0px',
	width: '380',
	height: '100px',
	z_index: 1000,
	isValid: true,
	sceneId: '',
	data: {
		total: 0,
		rows: []
	},

	create: function (sceneId) {
		this.sceneId = sceneId;
		var panel = document.createElement('div');
		panel.id = "devicePanel";
		panel.style.position = 'absolute';
		panel.style.top = this.top;
		panel.style.left = this.left;
		panel.style.width = this.width + 'px';
		panel.style['z-index'] = this.z_index;
		panel.style.height = (window.innerHeight) + 'px';
		panel.style.border = 'solid 0px #1ff797';
		panel.style.background = 'rgba(30, 50, 100, 0.7)';
		document.body.appendChild(panel);
		this.initTreegrid(panel, sceneId);
		$('#devicePanel').hide();
		this.mqttConnect();
	},

	display: function (isValid) {
		if (isValid) {
			$('#devicePanel').show(300);
		} else {
			$('#devicePanel').hide(300);
		}
	},

	initTreegrid: function (panel, sceneId) {
		var _this = this;

		var table = document.createElement('table');
		table.name = 'treegrid';
		table.id = 'treegrid';
		table.style['overflow'] = 'auto';
		table.style.cssText = "width:100%; background-color:transparent;";
		panel.appendChild(table);

		$('#treegrid').treegrid({
			title: '设备列表',
			height: (window.innerHeight - 10) + 'px',
			fit: true,
			fitColumns: true,
			border: true,
			idField: 'id',
			treeField: 'deviceName',
			parentField: 'parentBy',
			rownumbers: false,
			columns: [[{
				field: 'deviceName',
				title: '设备名称',
				halign: 'center'
			}, {
				field: 'type',
				title: '类型'
			}, {
				field: 'dataValue',
				title: '数据值',
			}, {
				field: 'opt',
				title: '操作',
			}]],
			onClickRow: function () {
				$('#treegrid').datagrid('clearSelections');
			},
		});
		this.getDeviceList(sceneId);
	},

	getDeviceList: function (sceneId) {
		var _this = this;
		$.getJSON("sceneController.do?getDeployDeviceListBySceneId&sceneId=" + sceneId, function (result) {
			if (result.success) {
				var list = result.obj;
				_this.data.total = list.length;
				for (var i in list) {
					var d = list[i];
					var type = '', icon = '', opt = '', dataValue = '';
					switch (d.type) {
						case 'terminal':
							type = '终端';
							icon = 'fa fa-codepen';
							for (let j = 0; j < d.dataNode.length; j++) {
								let douhao = j != 0 ? ', ' : '';
								dataValue += douhao + '<span id="' + d.dataNode[j].field + '-' + d.id + '" title="' + d.dataNode[j].name + '">0</span>' + d.dataNode[j].unit;
							}
// 							console.log(d.dataNode);
							opt = '<a href="javascript:;" class="fa fa-line-chart" title="曲线" onclick="viewIotData(\'' + d.type + '\',\'' + d.id + '\');"></a>&nbsp'
								+ '<a href="javascript:;" class="fa fa-map-marker" title="定位" onclick="devicePanel.searchDeviceById(\'' + d.id + '\');"></a>';
							break;
						case 'gateway':
							type = '网关';
							icon = 'fa fa-sitemap';
							opt = '<a href="javascript:;" class="fa fa-map-marker" title="定位" onclick="devicePanel.searchDeviceById(\'' + d.id + '\');"></a>';
							break;
						case 'nsdevice':
							type = '非设备';
							icon = 'fa fa-cube';
							opt = '<a href="javascript:;" class="fa fa-map-marker" title="定位" onclick="devicePanel.searchDeviceById(\'' + d.id + '\');"></a>';
							break;
						default:
							break;
					}
					var o = {
						id: d.id,
						deviceName: d.name + '[' + d.code + ']',
						type: type,
						dataValue: dataValue,
						iconCls: icon,
						_parentId: d.parentBy === '' ? null : d.parentBy,
						opt: opt
					}
					_this.data.rows.push(o);
				}
				$('#treegrid').datagrid('loadData', _this.data);
			}
		});
	},

	searchDeviceById: function (id) {
		var _this = this;
		for (var i in objects) {
			if (objects[i].uuid === id) {
				_this.changeCameraPosition(objects[i]);
				if (control) {
					control.attach(objects[i]);
				}
				break;
			}
		}
	},

	// 改变相机视角
	changeCameraPosition: function (obj) {
		var _this = this;
		var box = new THREE.Box3();
		var be = box.expandByObject(obj);
		new TWEEN.Tween(orbit.target).to({
			x: obj.position.x,
			y: obj.position.y,
			z: obj.position.z
		}, 500).onComplete(function () {
			// 描边
			if (mouseDownBoxHelper != undefined) {
				scene.remove(mouseDownBoxHelper);
			}
			mouseDownBoxHelper = new THREE.BoxHelper(obj, '#FF0000');
			scene.add(mouseDownBoxHelper);
			new TWEEN.Tween(camera.position).to({
				x: parseInt(be.max.x - be.min.x + be.max.y - be.min.y + be.max.z - be.min.z) * Math.sin(obj.rotation.y) + obj.position.x,
				y: parseInt(be.max.y - be.min.y + be.max.y - be.min.y + be.max.z - be.min.z) + obj.position.y,
				z: parseInt(be.max.z - be.min.z + be.max.y - be.min.y + be.max.z - be.min.z) * Math.cos(obj.rotation.y) + obj.position.z
			}, 500).start();
			// _this.cameraTween(obj, 500);
		}).start();
	},

	/*
	 * camera:相机, during：动画执行的时间
	 */
	cameraTween: function (obj, during) {

		var box = new THREE.Box3();
		var be = box.expandByObject(obj);

		var n = obj.position;
		var x = parseInt(be.max.z - be.min.z);
		var y = parseInt(be.max.z - be.min.z);
		var z = parseInt(be.max.z - be.min.z);

		var sinDelta = Math.sin(THREE.Math.degToRad(obj.rotation.y * 180));
		var cosDelta = Math.cos(THREE.Math.degToRad(obj.rotation.y * 180));

		new TWEEN.Tween(camera.position).to({
			x: x * (n.x * n.x * (1 - cosDelta) + cosDelta) + y * (n.x * n.y * (1 - cosDelta) - n.z * sinDelta) + z * (n.x * n.z * (1 - cosDelta) + n.y * sinDelta),
			y: x * (n.x * n.y * (1 - cosDelta) + n.z * sinDelta) + y * (n.y * n.y * (1 - cosDelta) + cosDelta) + z * (n.y * n.z * (1 - cosDelta) - n.x * sinDelta),
			z: x * (n.x * n.z * (1 - cosDelta) - n.y * sinDelta) + y * (n.y * n.z * (1 - cosDelta) + n.x * sinDelta) + z * (n.z * n.z * (1 - cosDelta) + cosDelta),
		}, during).start();
	},

	clientId: "forallcn-iotcp-devicePanel",
	mqttConnect: function () {
		var _this = this;
		_this.clientId += '-' + new Date().getTime();
		var client = new Paho.MQTT.Client("115.28.230.81", Number(8083), _this.clientId); //建立客户端实例
		client.connect({
			userName: "admin",
			password: "public",
			onSuccess: function () {
				console.log('mqtt[' + devicePanel.clientId + ']连接成功。。');
				client.subscribe("$forallcn/iotcp/iotData"); //订阅主题:实时数据数据
			}
		}); //连接服务器并注册连接成功处理事件

		// 注册连接断开处理事件
		client.onConnectionLost = _this.mqttConnectionLost;

		// 注册消息接收处理事件
		client.onMessageArrived = _this.mqttMessageArrived;
	},

	mqttConnectionLost: function (responseObject) {
		if (responseObject.errorCode !== 0) {
			console.log("mqtt[" + devicePanel.clientId + "]连接已断开");
			devicePanel.clientId = devicePanel.clientId.substring(0, devicePanel.clientId.lastIndexOf('-'));
			devicePanel.mqttConnect();
		}
	},

	mqttMessageArrived: function (message) {
		let topic = message.destinationName;
		let qos = message.qos;
		if (message.payloadString.length > 0) {
			let msg = JSON.parse(message.payloadString);
			let dom = document.getElementById(msg.data.field + "-" + msg.id);
			let labelDom = document.getElementById("label-" + msg.id);
			// 报警数据设置为红色
			if (msg.isAlarm) {
				dom.style.color = "#FF0000";
				labelDom.style['border-color'] = "#FF0000";
				labelDom.style['background-color'] = "rgba(255, 47, 79, 0.5)";
				// alarmPanel.getDeviceList(_this.sceneId);
			}else{
				dom.style.color = "#12EDB7";
				labelDom.style['background-color'] = "rgba(26, 47, 79, 0.5)";
				labelDom.style['border-color'] = "rgba(30, 255, 222, 0.75)";
			}
			dom.innerText = msg.data.value;
		}
	}
};