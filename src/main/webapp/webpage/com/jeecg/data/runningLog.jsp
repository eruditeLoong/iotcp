<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<title>平台实时通讯日志</title>
<script type="text/javascript" src="plug-in/jquery/jquery-1.8.3.js"></script>
<script src="plug-in/mqtt/mqttws31.js" type="text/javascript"></script>

<script type="text/javascript">

	$(document).ready(function() {
		mqttConnect()
		document.addEventListener("resume", function() {
			if (!mqttStatus) {
				logShowLine("后台回到前台重新连接mqtt服务");
				mqttConnect();
			}
		}, true);
	});

	/*
	 * connect mqtt server
	 * 2018.03.30 pw
	 */
	let clientId = "forallcn-iotcp-runningLog";
	function mqttConnect() {
		clientId += '-' + new Date().getTime();
		let client = new Paho.MQTT.Client("115.28.230.81", Number(8083), clientId); //建立客户端实例
		client.connect({
			userName : 'admin',
			password : 'public',
			onSuccess : onConnect
		}); //连接服务器并注册连接成功处理事件
		function onConnect() {
			console.log('mqtt[' + clientId + ']连接成功。。');
			client.subscribe("$forallcn/iotcp/#"); //订阅主题:任务办理和签收
		}
		client.onConnectionLost = onConnectionLost; // 注册连接断开处理事件
		client.onMessageArrived = onMessageArrived; // 注册消息接收处理事件
	}

	/*
	 *  mqtt断开连接
	 * 2018.03.30 pw
	 */
	function onConnectionLost(responseObject) {
		if (responseObject.errorCode !== 0) {
			console.log("mqtt[" + clientId + "]连接已断开");
			clientId = clientId.substring(0, clientId.lastIndexOf('-'));
			mqttConnect();
		}
	}

	/*
	 * mqtt 接收消息
	 * 2018.03.30 pw
	 */
	function onMessageArrived(message) {
		var topic = JSON.stringify(message.destinationName);
		/* var qos = message.qos;
		if (qos > 0) {
			mqttSend(topic, '');
		} */
		logShowLine(message.payloadString);
	}

	/*
	 * mqtt发送消息
	 * 2018.03.30 pw
	 *
	 * destination说明, "/deviceId"
	 * item说明, 灯:light ,空气进化器:air
	 * 例: mqttSend("/01-001-0001", "light:0");
	 */
	function mqttSend(destination, txt) {
		txt = txt.toString();
		if (txt != "") {
			message = new Paho.MQTT.Message(txt);
			message.destinationName = destination;
			client.send(message);
		}
	}

	function logShowLine(msg) {
		var li = document.createElement('li')
		li.innerHTML = "<span class='log-time'>[" + new Date().Format('yyyy-MM-dd hh:mm:ss') + "]</span> " + msg;
		$('#logs-ui').prepend(li);
	}

	function clearLog() {
		document.getElementById('logs-ui').innerHTML = "";
	}

	Date.prototype.Format = function(fmt) {
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
<style>
body {
	background-color: black;
	color: rgb(213, 230, 255);
	font-size: 12px;
}

.log-time {
	color: #fe3100;
}

#tools {
	position: absolute;
	top: 0px;
	right: 0px;
	border: 1px solid rgba(22, 233, 255, 0.5);
	border-radius: 3px;
	background-color: transparent;
	color: rgba(127, 255, 255, 1);
	padding: 5px;
}

#tools a {
	color: rgba(127, 255, 255, 1);
	text-decoration: none;
}
</style>
</head>
<body>
	<ul id="logs-ui">
	</ul>
	<div id="tools">
		<a href="javascript:;" onClick="clearLog();">清空</a>
		<a href="javascript:;" onClick="exportLog();">导出</a>
	</div>
</body>
</html>