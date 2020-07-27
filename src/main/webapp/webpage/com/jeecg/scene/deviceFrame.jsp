<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>设备列表</title>
<t:base type="jquery,easyui,tools"></t:base>
<link rel="stylesheet" type="text/css" href="plug-in/ztree/css/metroStyle.css">
<script type="text/javascript" src="plug-in/ztree/js/jquery.ztree.core-3.5.min.js"></script>
<script type="text/javascript" src="plug-in/ztree/js/ztreeCreator.js"></script>
<script type="text/javascript" src="plug-in/lhgDialog/lhgdialog.min.js?skin=metrole"></script>

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
	<input id="sceneId" value="" type="hidden" />
	<div id="deviceTitle" class="easyui-panel" title="设备列表" style="width: auto;">
		<a id="resetBtn" onclick="deployDevice();" class="ace_button" style="background-color: #DD00A4; display: none;">放 置</a>
		<a id="refreshBtn" onclick="getDeviceList();" class="ace_button" style="background-color: #49a874; display: none;">刷 新</a>
		<ul id="deviceList" class="ztree"></ul>
	</div>
	<div id="p" class="easyui-panel" title="设备数据" style="width: auto;">
		<table id="pg" class="easyui-propertygrid" data-options="method:'get',showGroup:true,scrollbarSize:0,columns: mycolumns"></table>
		<script>
			var mycolumns = [ [ {
				field : 'name',
				title : '属性名称',
				width : 80,
				sortable : true
			}, {
				field : 'value',
				title : '属性值',
				width : 120,
				resizable : false
			} ] ];
			function deployDevice() {
				var rows = $('#pg').propertygrid('getData').rows;
				if (rows.length <= 0) {
					$.dialog.alert('请选择一个设备！', function() {
					});
					return;
				}
				var obj = {};
				for ( var i in rows) {
					obj[rows[i].field] = rows[i].value;
				}
				if (obj.code == undefined || obj.code.trim() == "") {
					$.dialog.alert('设备编码为空，请填写！', function() {
					});
					return;
				}
				var context = window.parent.frames['sceneDeployFrame'];
				// 判断编码是否重复
				var isUniq = context.checkCodeUniq(obj);
				if (isUniq) {
					$.dialog.alert('模型对象的编码冲突，请修改对象编码！', function() {
					});
					return;
				}
				/* 如果是终端设备，弹框是否选择父级设备 */
				if (obj.type === "terminal") {
					obj.isGateway = false;
					console.log("obj.parentBy>>>", obj.parentBy);
					if (obj.parentBy == undefined || obj.parentBy == null || obj.parentBy == '') {
						$.dialog({
							id : 'deployDevice',
							content : '终端设备，需要配置父级网关设备吗!',
							max : false,
							min : false,
							skin:'chrome',
							button : [ {
								name : '配置',
								callback : function() {
									var objs = window.parent.frames['sceneDeployFrame'].objects;
									showParentObjs(objs, function(val){
										alert(val);
									});
								},
								focus : true
							}, {
								name : '不配置',
								callback : function() {
									// 点击添加设备
									window.parent.frames['sceneDeployFrame'].deployDevice(obj);
								}
							}, {
								name : '取消'
							} ]
						});
					} else {
						// 点击添加设备
						window.parent.frames['sceneDeployFrame'].deployDevice(obj);
					}
				} else { // 网关设备，直接放置
					obj.isGateway = true;
					// 点击添加设备
					window.parent.frames['sceneDeployFrame'].deployDevice(obj);
				}
			}
			
			function showParentObjs(objs, callback){
				var pobjs = "";
				for(var i in objs){
					if(objs[i].userData.isGateway === true){
						pobjs += "<option value="+objs[i].uuid+">"+objs[i].name+"["+objs[i].userData.code+"]</option>";
					}
				}
				$.dialog({
					content: "<select id='pobj-select' style='width:150px'>"+pobjs+"</select>",
					title: '选择网关设备',
					lock: true,
					width: 250,
					height: 100,
					button: [{
						name: '确定',
						callback: function (){
							var val = this.contentWindow.$('#pobj-select option:selected').val();
							alert(val);
							callback(val);
						},
						focus: true
					},{
						name: '取消',
					}]
				});
			}

			var setting = {
				view : {
					selectedMulti : true, //设置是否能够同时选中多个节点
					showIcon : true, //设置是否显示节点图标
					showLine : true, //设置是否显示节点与节点之间的连线
					showTitle : true, //设置是否显示节点的title提示信息
				},
				callback : {
					onClick : onClick
				}
			};

			function onClick(event, treeId, treeNode) {
				// 调用sceneDeployFrame的添加模型方法
				// 传入设备的类型和id
				var deviceEntityName = treeNode.entity;
				if (deviceEntityName == undefined) {
					return false;
				}
				// 显示放置按钮
				$('#resetBtn').css('display', 'inline-block');
				var data = {
					"total" : 16,
					"rows" : [ {
						field : 'id',
						name : '设备ID',
						value : treeNode.id,
						group : '设备属性'
					}, {
						field : 'entity',
						name : '设备类名',
						value : treeNode.entity,
						group : '设备属性'
					}, {
						field : 'confBy',
						name : '设备配置id',
						value : treeNode.confBy,
						group : '设备属性'
					}, {
						field : 'modelFile',
						name : '设备模型',
						value : treeNode.modelFile,
						group : '设备属性'
					}, {
						field : 'name',
						name : '设备名称',
						value : treeNode.name,
						group : '设备属性',
						editor : 'text'
					}, {
						field : 'type',
						name : '设备类型',
						value : treeNode.type,
						group : '设备属性'
					}, {
						field : 'code',
						name : '设备编码',
						value : treeNode.code,
						group : '设备属性',
						editor : 'text'
					},
					/* {field : 'remark', name : 'x坐标', value : 0, group : '位置信息', editor : {type : 'numberbox', options : {precision : 2}}},
					{field : 'remark', name : 'y坐标', value : 0, group : '位置信息', editor : {type : 'numberbox', options : {precision : 2}}},
					{field : 'remark', name : 'z坐标', value : 0, group : '位置信息', editor : {type : 'numberbox', options : {precision : 2}}},
					{field : 'remark', name : 'x轴旋转', value : 0, group : '位置信息', editor : {type : 'numberbox', options : {precision : 2}}},
					{field : 'remark', name : 'y轴旋转', value : 0, group : '位置信息', editor : {type : 'numberbox', options : {precision : 2}}},
					{field : 'remark', name : 'z轴旋转', value : 0, group : '位置信息', editor : {type : 'numberbox', options : {precision : 2}}},
					{field : 'remark', name : 'x轴缩放', value : 1, group : '位置信息', editor : {type : 'numberbox', options : {precision : 2}}},
					{field : 'remark', name : 'y轴缩放', value : 1, group : '位置信息', editor : {type : 'numberbox', options : {precision : 2}}},
					{field : 'remark', name : 'z轴缩放', value : 1, group : '位置信息', editor : {type : 'numberbox', options : {precision : 2}}}, */
					]
				};
				$('#pg').propertygrid('loadData', data);
				$('#pg').datagrid({
					toolbar : [ {
						text : '放置',
						iconCls : 'icon-edit',
						handler : function() {
							deployDevice()
						}
					}, '-', {
						text : '帮助',
						iconCls : 'icon-help',
						handler : function() {
							alert('help')
						}
					} ]
				});
				$('#pg').datagrid({
					onAfterEdit : function(index, data, changes) {
					}
				});
			}

			function loadDeviceList(sceneId) {
				// 把sceneId放到隐藏框中，已备刷新使用
				$('#sceneId').val(sceneId);
				getDeviceList();
			}

			function getDeviceList() {
				var sceneId = $('#sceneId').val();
				//加载tree
				$.getJSON("sceneController.do?getConfDevice&sceneId=" + sceneId, function(result) {
					if (result.success) {
						// 利用 节点数据的 icon / iconOpen / iconClose 属性实现自定义图标
						$.fn.zTree.init($("#deviceList"), setting, result.obj || []);
						// 显示刷新按钮
						$('#refreshBtn').css('display', 'inline-block');
						// 清楚属性列表数据
						$('#pg').propertygrid('loadData', []);
					}
				});
			}
		</script>
	</div>
</body>
</html>