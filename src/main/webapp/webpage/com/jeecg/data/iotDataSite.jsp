<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<title>数据统计</title>
<meta name="viewport" content="width=device-width" />
<t:base type="jquery,easyui"></t:base>
<link rel="stylesheet" type="text/css" href="plug-in/ztree/css/metroStyle.css">
<script type="text/javascript" src="plug-in/ztree/js/jquery.ztree.core-3.5.min.js"></script>
<script type="text/javascript" src="plug-in/ztree/js/ztreeCreator.js"></script>
<script type="text/javascript" src="plug-in/echart/echarts.min.js"></script>
<style type="text/css">
body {
	
}

#listTable {
	width: 100%;
	height: 100%;
	border: 0px;
}

#listTable thead {
	width: 100%;
	padding: 0px;
	margin: 0px;
}

#listTable tbody tr {
	background-color: #E0F5FD;
	height: 25px;
}

.lineStyle {
	background-color: #E0F5FD;
	height: 25px;
}
</style>
</head>
<body>
	<div class="easyui-layout" fit="true">
		<div region="west" split="true" title="应用设备列表" style="width: 280px;">
			<ul id="siteSelect" class="ztree"></ul>
		</div>
		<div id="sitePanel" region="center" title="数据">
			<div class="easyui-layout" fit="true">
				<div region="center">
					<div class="easyui-layout" fit="true">
						<div id="containerPie" region="east" style="width: 300px"></div>
						<div id="containerline" region="center"></div>
					</div>
				</div>
			</div>
		</div>
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
				onClick : function(event, treeId, treeNode) {
					var type = treeNode.type;
					switch (type) {
					case 'scene':
						break;
					case 'terminal':
						$("#sitePanel").panel({
							title : treeNode.name
						});
						getDatas(treeNode.id);
						break;
					default:
						break;
					}
				}
			}
		};

		/* 折线图 */
		var containerline_option = {
			title : {
				text : '设备数据曲线',
				subtext : ''
			},
			tooltip : {
				trigger : 'axis'
			},
			legend : {
				data : []
			},
			calculable : true,
			xAxis : [ {
				type : 'time',
				axisTick : {
					alignWithLabel : true
				},
				boundaryGap : false,
				data : [],
				axisLabel : {
					interval : 0,//横轴信息全部显示
					rotate : -30,//-10角度倾斜展示
				}
			} ],
			yAxis : [ {
				type : 'value',
			} ],
			dataZoom : [ {
				type : 'slider',
				xAxisIndex : 0,
				filterMode : 'empty'
			}, {
				type : 'slider',
				yAxisIndex : 0,
				filterMode : 'empty'
			}, {
				type : 'inside',
				xAxisIndex : 0,
				filterMode : 'empty'
			}, {
				type : 'inside',
				yAxisIndex : 0,
				filterMode : 'empty'
			} ],
			series : []
		};

		var containerPie_option = {
			title : {
				text : '数值统计',
				subtext : ''
			},
			tooltip : {
				trigger : 'item',
				formatter : "{a} <br/>{b}: {c} ({d}%)"
			},
			series : []
		};
		function getDatas(devId) {
			var containerLine = echarts.init(document.getElementById('containerline'));
			var containerPie = echarts.init(document.getElementById('containerPie'));
			$.getJSON('iotDataController.do?getDataList&instanceDeviceBy=' + devId, function(result) {
				var obj = result.obj;
				var datas = obj.echartData;
				containerline_option.series = [];
				containerPie_option.series = [];
				for ( var i in datas) {
					// 折线图
					var sery = {
						name : datas[i].label,
						type : 'line',
						smooth : true,
						data : []
					};
					var ld = datas[i].lineData || [];
					for ( var j in ld) {
						var d = {
							name : ld[j].name,
							value : [ ld[j].name, ld[j].value ],
						};
						sery.data.push(d);
					}
					containerline_option.series.push(sery);
					containerline_option.legend.data.push(datas[i].label);
					// 饼图
					var pieSery = {
						name : datas[i].label,
						type : "pie",
						selectedMode : 'single',
						radius : [],
						data : []
					}
					var n = datas.length;
					var a = (i * 2 - 0) * (50 / n);
					var b = (i * 2 + 1) * (50 / n);
					pieSery.radius.push(a + '%');
					pieSery.radius.push(b + '%');
					pieSery.data = datas[i].pieData || [];
					containerPie_option.series.push(pieSery);
				}
				// 折线图
				containerLine.setOption(containerline_option);
				// 饼图
				containerPie.setOption(containerPie_option);
			});
		}
		$(function() {
			//加载tree
			$.getJSON("sceneController.do?getSceneDeviceDeployTree", function(result) {
				if (result.success) {
					$.fn.zTree.init($("#siteSelect"), setting, result.obj);
				}
			});
			// 加载数据
			<%--getDatas('${deployDeviceBy}');--%>
		});
	</script>
</body>
</html>