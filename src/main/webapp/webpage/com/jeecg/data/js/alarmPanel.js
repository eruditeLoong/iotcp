var alarmPanel = {
	bottom: '0px',
	left: devicePanel.width + 'px',
	width: (((window.innerWidth - 0) - ((devicePanel.width - 0) * 2))) + 'px',
	height: '380px',
	z_index: 1000,
	isValid: true,
	sceneId: '',

	create: function (sceneId) {
		var panel = document.createElement('div');
		panel.id = "alarmPanel";
		panel.style.position = 'absolute';
		panel.style.bottom = this.bottom;
		panel.style.left = this.left;
		panel.style.width = this.width;
		panel.style['z-index'] = this.z_index;
		panel.style.height = this.height;
		panel.style.border = 'solid 0px #1ff797';
		panel.style.background = 'rgba(30, 50, 100, 0.7)';
		document.body.appendChild(panel);

		this.initTreegrid(panel, sceneId);
		$('#alarmPanel').hide();
		this.sceneId = sceneId;
	},

	display: function (isValid) {
		if (isValid) {
			$('#alarmPanel').show(300);
		} else {
			$('#alarmPanel').hide(300);
		}
	},

	initTreegrid: function (panel, sceneId) {
		var _this = this;

		var table = document.createElement('table');
		table.name = 'treegrid-alarm';
		table.id = 'treegrid-alarm';
		table.style['overflow-y'] = 'auto';
		table.style.cssText = "width:100%; background-color:transparent;";
		panel.appendChild(table);

		$('#treegrid-alarm').datagrid({
			title: '报警列表',
			height: (window.innerHeight - 10) + 'px',
			fit: true,
			border: true,
			idField: 'id',
			rownumbers: true,
			columns: [[{
				field: 'baseDeviceName',
				title: '报警设备',
				align: 'left',
			}, {
				field: 'deployDeviceCode',
				title: '设备编号',
				align: 'left',
			}, {
				field: 'dataLabel',
				title: '数据名称',
			}, {
				field: 'dataValue',
				title: '数据值',
			}, {
				field: 'normalDataRange',
				title: '正常数据范围'
			}, {
				field: 'alarmDate',
				title: '时间',
				width: 160
			}, {
				field: 'dealStatus',
				title: '处理状态'
			}, {
				field: 'opt',
				title: '操作',
				width: 100
			}]],
			onClickRow: function () {
				$('#treegrid-alarm').datagrid('clearSelections');
			},
		});
		this.getDeviceList(sceneId);
	},

	getDeviceList: function (sceneId) {
		var _this = this;
		$.getJSON("iotAlarmDataController.do?listAlarmData&dealStatus=0&sceneId=" + sceneId, function (result) {
			if (result.success) {
				var list = result.obj;
				for (var i in list) {
					var d = list[i];
					d.dealStatus = d.dealStatus==0?"未处理":"已处理";
					var opt = '<a href="javascript:;" class="fa fa-line-chart" title="曲线" onclick="viewIotData(\'' + d.type + '\',\'' + d.deployDeviceBy + '\');"></a>&nbsp'
						+ ' <a href="javascript:;" class="fa fa-map-marker" title="定位" onclick="alarmPanel.searchDeviceById(\'' + d.deployDeviceBy + '\');"></a>'
						+ ' <a href="javascript:;" class="fa fa-wrench" title="处理" onclick="alarmPanel.dealAlarmData(\'' + d.id + '\');"></a>';
					d['opt'] = opt;
				}
			}
			$('#treegrid-alarm').datagrid('loadData', list);
		});
	},

	// 处理报警数据
	dealAlarmData: function(id){
		var _this = this;
		$.dialog({
			width: 780,
			height: 400,
			lock: true,
			content: 'url:iotAlarmDataController.do?goDeal&id=' + id,
			ok: function () {
				var iframe = this.iframe.contentWindow;
				var form = iframe.document.forms[0];
				var formData = new FormData(form);
				iframe.document.getElementById("btn_sub").click();
				// _this.getDeviceList(_this.sceneId);
			},
			cancelVal: '关闭',
			cancel: true
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
	}
};