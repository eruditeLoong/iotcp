<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
<title>3D场景部署</title>
<script type="text/javascript" src="plug-in/jquery/jquery-1.8.3.js"></script>
<script type="text/javascript" src="plug-in/echart/echarts.min.js"></script>
<style type="text/css">
#gauge {
	max-height: 200px;
	overflow: auto;
}

#gauge::-webkit-scrollbar {
	width: 8px;
	height: 8px;
	background-color: rgba(30, 47, 79, 0.5);
}

#gauge::-webkit-scrollbar-track {
	/* background: #213147; */
	background-color: rgba(30, 47, 79, 0.5);
	border-radius: 5px;
}

#gauge::-webkit-scrollbar-thumb {
	background-color: rgba(30, 144, 255, 0.5);
	border-radius: 5px;
}

#gauge::-webkit-scrollbar-thumb:hover {
	background-color: rgba(30, 47, 79, 0.5);
}

#gauge::-webkit-scrollbar-corner {
	background-color: rgba(30, 47, 79, 0.5);
	border-radius: 5px;
}

.gauge {
	display: inline-block;
	width: 200px;
	height: 200px;
}
</style>
</head>
<div id="iotDataDialog" style="font-size: 12px; color: rgba(127, 255, 255, 0.75);">
	<h5 style="text-align: center; margin: 0px; padding: 0px;">
		设备：
		<span id="deviceName"></span>
	</h5>
	<div style="margin-left: 15px; margin-right: 15px; height: 240px;">
		<div id="containerline" style="float: left; width: 70%; height: 100%;"></div>
		<div id="containerPie" style="float: left; width: 30%; height: 100%;"></div>
	</div>
	<div id="gauge" style="margin-left: 15px; margin-right: 15px; height: 200px; text-align: center; overflow-y: auto;">
		<script type="text/javascript">
			var curDate;
			$(function() {
				curDate = new Date().format("yyyy-MM-dd hh:mm:ss"); // 当前时间
				getDatas('${deployDeviceBy}');
			});
			var data = [];
			function randomData() {
				var value = Math.random() * 10;
				var now = new Date().getTime();
				return {
					name : now,
					value : [ now, Math.round(value) ]
				}
			}
			setInterval(function() {
				getNowDatas('${deployDeviceBy}', curDate);
				curDate = new Date().format("yyyy-MM-dd hh:mm:ss"); // 当前时间
			}, 1000 * 30);
			/* 折线图 */
			var containerline_option = {
				title : {
					text : '设备数据曲线',
					subtext : '24小时'
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
					subtext : '24小时'
				},
				tooltip : {
					trigger : 'item',
					formatter : "{a} <br/>{b}: {c} ({d}%)"
				},
				series : []
			};

			/* 表头 */
			var containerGauge = [];
			var containerGauge_option = {
				tooltip : { // 本系列特定的 tooltip 设定
					show : true,
					formatter : "",
					backgroundColor : "rgba(50,50,50,0.5)", // 提示框浮层的背景颜色。注意：series.tooltip 仅在 tooltip.trigger 为 'item' 时有效。
					padding : 5, // 提示框浮层内边距，单位px，默认各方向内边距为5，接受数组分别设定上右下左边距。...
					textStyle : { // 提示框浮层的文本样式。...
					// color ,fontStyle ,fontWeight ,fontFamily ,fontSize ,lineHeight ,.......
					},
				},
				series : [ {
					name : '',
					type : 'gauge',
					radius : '80%',
					center : [ "50%", "55%" ], // 仪表盘位置(圆心坐标)
					min : 0, // 最小的数据值,默认 0 。映射到 minAngle。
					max : 100, // 最大的数据值,默认 100 。映射到 maxAngle。
					splitNumber : 10, // 仪表盘刻度的分割段数,默认 10。
					axisLine : { // 仪表盘轴线(轮廓线)相关配置。
						show : true, // 是否显示仪表盘轴线(轮廓线),默认 true。
						lineStyle : { // 仪表盘轴线样式。
							color : [ [ 0.2, "rgba(65,22,225,0.6)" ], [ 0.8, "rgba(64,254,108,0.6)" ], [ 1, "rgba(255,0,0,0.6)" ] ],
							//仪表盘的轴线可以被分成不同颜色的多段。每段的  结束位置(范围是[0,1]) 和  颜色  可以通过一个数组来表示。默认取值：[[0.2, '#91c7ae'], [0.8, '#63869e'], [1, '#c23531']]
							opacity : 1, //图形透明度。支持从 0 到 1 的数字，为 0 时不绘制该图形。
							width : 10, //轴线宽度,默认 30。
							shadowBlur : 10, //(发光效果)图形阴影的模糊大小。该属性配合 shadowColor,shadowOffsetX, shadowOffsetY 一起设置图形的阴影效果。 
							shadowColor : "#1A2F4F", //阴影颜色。支持的格式同color。
						}
					},
					splitLine : { // 分隔线样式。
						show : true, // 是否显示分隔线,默认 true。
						length : 20, // 分隔线线长。支持相对半径的百分比,默认 30。
						lineStyle : { // 分隔线样式。
							color : "#eee", //线的颜色,默认 #eee。
							opacity : 0.8, //图形透明度。支持从 0 到 1 的数字，为 0 时不绘制该图形。
							width : 1, //线度,默认 2。
							type : "solid", //线的类型,默认 solid。 此外还有 dashed,dotted
							shadowBlur : 10, //(发光效果)图形阴影的模糊大小。该属性配合 shadowColor,shadowOffsetX, shadowOffsetY 一起设置图形的阴影效果。 
							shadowColor : "#1A2F4F", //阴影颜色。支持的格式同color。
						}
					},
					axisTick : { // 刻度(线)样式。
						show : true, // 是否显示刻度(线),默认 true。
						splitNumber : 5, // 分隔线之间分割的刻度数,默认 5。
						length : 8, // 刻度线长。支持相对半径的百分比,默认 8。
						lineStyle : { // 刻度线样式。	
							color : "#eee", //线的颜色,默认 #eee。
							opacity : 1, //图形透明度。支持从 0 到 1 的数字，为 0 时不绘制该图形。
							width : 1, //线度,默认 1。
							type : "solid", //线的类型,默认 solid。 此外还有 dashed,dotted
							shadowBlur : 10, //(发光效果)图形阴影的模糊大小。该属性配合 shadowColor,shadowOffsetX, shadowOffsetY 一起设置图形的阴影效果。 
							shadowColor : "#1A2F4F", //阴影颜色。支持的格式同color。
						},
					},
					axisLabel : { // 刻度标签。
						show : true, // 是否显示标签,默认 true。
						distance : 2, // 标签与刻度线的距离,默认 5。
						color : "#fff", // 文字的颜色,默认 #fff。
						fontSize : 12, // 文字的字体大小,默认 5。
						formatter : "{value}", // 刻度标签的内容格式器，支持字符串模板和回调函数两种形式。 示例:// 使用字符串模板，模板变量为刻度默认标签 {value},如:formatter: '{value} kg'; // 使用函数模板，函数参数分别为刻度数值,如formatter: function (value) {return value + 'km/h';}
					},
					pointer : { // 仪表盘指针。
						show : true, // 是否显示指针,默认 true。
						length : "70%", // 指针长度，可以是绝对数值，也可以是相对于半径的百分比,默认 80%。
						width : 5, // 指针宽度,默认 8。
					},
					itemStyle : { // 仪表盘指针样式。
						color : "auto", // 指针颜色，默认(auto)取数值所在的区间的颜色
						opacity : 1, // 图形透明度。支持从 0 到 1 的数字，为 0 时不绘制该图形。
						borderWidth : 1, // 描边线宽,默认 0。为 0 时无描边。
						borderType : "solid", // 柱条的描边类型，默认为实线，支持 'solid', 'dashed', 'dotted'。
						borderColor : "#000", // 图形的描边颜色,默认 "#000"。支持的颜色格式同 color，不支持回调函数。
						shadowBlur : 10, // (发光效果)图形阴影的模糊大小。该属性配合 shadowColor,shadowOffsetX, shadowOffsetY 一起设置图形的阴影效果。 
						shadowColor : "#fff", // 阴影颜色。支持的格式同color。
					},
					emphasis : { // 高亮的 仪表盘指针样式
						itemStyle : {
						//高亮 和正常  两者具有同样的配置项,只是在不同状态下配置项的值不同。
						}
					},
					title : { // 仪表盘标题。
						show : true, // 是否显示标题,默认 true。
						offsetCenter : [ 0, "20%" ],//相对于仪表盘中心的偏移位置，数组第一项是水平方向的偏移，第二项是垂直方向的偏移。可以是绝对的数值，也可以是相对于仪表盘半径的百分比。
						color : "#fff", // 文字的颜色,默认 #333。
						fontSize : 20, // 文字的字体大小,默认 15。
					},
					detail : {
						backgroundColor : "rgba(0,0,0,0)",
						show : true, // 是否显示详情,默认 true。
						offsetCenter : [ 0, "70%" ],// 相对于仪表盘中心的偏移位置，数组第一项是水平方向的偏移，第二项是垂直方向的偏移。可以是绝对的数值，也可以是相对于仪表盘半径的百分比。
						color : "red", // 文字的颜色,默认 auto。
						fontSize : 12, // 文字的字体大小,默认 15。
						formatter : '{value}℃'
					},
					data : [ {
						value : 50,
						name : ''
					} ]
				} ]
			};
			var chart = echarts.init(document.getElementById('containerline'));
			var containerPie = echarts.init(document.getElementById('containerPie'));
			function getDatas(devId) {
				var curDate = new Date();
				var beforeDate = new Date(curDate.getTime() - 24 * 60 * 60 * 1000).format("yyyy-MM-dd hh:mm:ss"); //前一天
				$.getJSON('iotDataController.do?getDataList&instanceDeviceBy=' + devId + "&createDate=" + beforeDate, function(result) {
					var obj = result.obj;
					$('#deviceName').text(obj.name + '[' + obj.code + ']');
					var datas = obj.echartData
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

						// 表头
						var gaugeDiv = document.createElement('div');
						gaugeDiv.id = datas[i].field;
						gaugeDiv.className = 'gauge';
						document.getElementById("gauge").appendChild(gaugeDiv);
						containerGauge[i] = echarts.init(gaugeDiv);
						containerGauge_option.tooltip.formatter = "{a} <br/>{b} : {c}" + datas[i].unit;
						containerGauge_option.series[0].name = obj.name + '[' + obj.code + ']';
						containerGauge_option.series[0].data[0].name = datas[i].label;
						containerGauge_option.series[0].data[0].value = datas[i].lineData[datas[i].lineData.length - 1].value;
						containerGauge_option.series[0].detail.formatter = '{value}' + datas[i].unit;
						var r = datas[i].dataRange.split(',');
						var nr = datas[i].normalDataRange.split(',');
						containerGauge_option.series[0].min = r[0];
						containerGauge_option.series[0].max = r[1];
						var a = r[1] - r[0];
						containerGauge_option.series[0].axisLine.lineStyle.color[0][0] = 1 / a * (nr[0] - r[0]);
						containerGauge_option.series[0].axisLine.lineStyle.color[1][0] = 1 / a * (nr[1] - r[0]);
						// 表头
						containerGauge[i].setOption(containerGauge_option);
					}
					// 折线图
					chart.setOption(containerline_option);
					// 饼图
					containerPie.setOption(containerPie_option);
				});
			}

			function getNowDatas(devId, beforeDate) {
				$.getJSON('iotDataController.do?getDataList&instanceDeviceBy=' + devId + "&createDate=" + beforeDate, function(result) {
					var obj = result.obj;
					var datas = obj.echartData
					for ( var i in datas) {
						// 折线图
						var ld = datas[i].lineData || [];
						for ( var j in ld) {
							var d = {
								name : ld[j].name,
								value : [ ld[j].name, ld[j].value ],
							};
							containerline_option.series[i].data.push(d);
						}
						// 饼图
						containerPie_option.series[i].data.push(datas[i].pieData);
						// 表头
						containerGauge_option.series[0].data[0].value = datas[i].lineData[datas[i].lineData.length - 1].value;
						containerGauge_option.series[0].detail.formatter = '{value}' + datas[i].unit;
						var r = datas[i].dataRange.split(',');
						var nr = datas[i].normalDataRange.split(',');
						containerGauge_option.series[0].min = r[0];
						containerGauge_option.series[0].max = r[1];
						var a = r[1] - r[0];
						containerGauge_option.series[0].axisLine.lineStyle.color[0][0] = 1 / a * (nr[0] - r[0]);
						containerGauge_option.series[0].axisLine.lineStyle.color[1][0] = 1 / a * (nr[1] - r[0]);
						// 表头
						containerGauge[i].setOption(containerGauge_option);
					}
					// 折线图
					chart.setOption(containerline_option);
					// 饼图
					containerPie.setOption(containerPie_option);
				});
			}

			Date.prototype.format = function(fmt) { //author: meizz   
				var o = {
					"M+" : this.getMonth() + 1, //月份   
					"d+" : this.getDate(), //日   
					"h+" : this.getHours(), //小时   
					"m+" : this.getMinutes(), //分   
					"s+" : this.getSeconds(), //秒   
					"S" : this.getMilliseconds()
				//毫秒   
				};
				if (/(y+)/.test(fmt))
					fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
				for ( var k in o)
					if (new RegExp("(" + k + ")").test(fmt))
						fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
				return fmt;
			}
		</script>
	</div>
</div>