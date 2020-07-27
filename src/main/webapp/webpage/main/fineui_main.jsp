<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
	<meta name="renderer" content="webkit|ie-comp|ie-stand">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<link rel="fineui icon" href="images/favicon.ico">
	<meta http-equiv="Cache-Control" content="no-siteapp"/>
	<meta name="keywords" content="IOTCP 物联网平台系统">
	<meta name="description" content="IOTCP 物联网平台系统，3D场景展示，终端设备灵活在线配置，实时报警机制">

	<title><t:mutiLang langKey="jeect.platform"/></title>
	<script src="plug-in/themes/adminlte/js/bootstrap.min.js"></script>
	<link href="plug-in/hplus/css/font-awesome.min.css?v=4.4.0" rel="stylesheet">
	<link rel="stylesheet" href="plug-in/themes/fineui/common/css/sccl.css">
	<link rel="stylesheet" type="text/css" href="plug-in/themes/fineui/common/skin/qingxin/skin.css" id="layout-skin"/>
	<link rel="stylesheet" href="plug-in/themes/fineui/common/iconfont/iconfont.css">
	<link rel="stylesheet" href="plug-in/themes/fineui/smart-menu/smartMenu.css">
	<t:base type="jquery,tools"></t:base>
	<style>
		.titlerow {
			display: table;
			width: 150px;
			position: absolute;
			top: 50%;
			margin-top: -13px;
			left: 450px;
			margin-bottom: 0;
			opacity: .50;
			filter: alpha(opacity=50);
			background-color: rgba(255, 255, 255, 0.20);
		}

		.titlecell {
			width: 100%;
			position: relative;
			vertical-align: middle;
			padding: 0;
			line-height: 24px;
		}

		.searchbox {
			border-radius: 0;
			color: #ddd;
			border-width: 1px;
			border-style: solid;
			line-height: 16px;
			outline: none;
			background-color: transparent;
			text-rendering: auto;
			letter-spacing: normal;
			word-spacing: normal;
			text-transform: none;
			text-indent: 0px;
			text-shadow: none;
			display: inline-block;
			text-align: start;
			margin: 0em 0em 0em 0em;
			font: 12.6667px Arial;
			padding: 4px 6px;
			width: 100%;
			box-sizing: border-box;
		}

		.searchbox-focus {
			color: #fff;
		}

		.ui-iconss-focus {
			color: #fff;
		}

		.searchbox-focusbg {
			background-color: rgba(255, 255, 255, 0.10);
		}

		.ui-iconss-focus i {
			color: #fff;
			font-size: 17px;
		}

		.ui-iconss {
			box-sizing: border-box;
			min-width: 20px;
			cursor: pointer;
			text-align: center;
			line-height: 16px;
			vertical-align: top;
			min-height: 16px;
			color: #ddd;
			display: inline-block;
			font: normal normal normal 16px/1 FontAwesome;
			font-weight: 900;
			text-rendering: auto;
			-webkit-font-smoothing: antialiased;
			-moz-osx-font-smoothing: grayscale;
			text-indent: 0;
		}

		.iconssdiv {
			position: absolute;
			right: 0;
			top: 50%;
			margin-top: -6.5px;
			margin-right: 3px;
			border-bottom-right-radius: 4px;
			font-size: 13px;
		}

		.header-bar li {
			margin-left: 5px;
		}

		a.active .tab-bottom-separator, a.active .sepmm {
			position: relative;
			top: -6px;
			width: 100%;
			display: block;
			height: 2px;
			background-color: #007465;
			padding: 0 15px;
			left: -15px;
		}

		.colorgray {
			color: #888;
			font-size: 16px !important;
		}

		.content-tab.active .colorgray {
			color: #007465
		}

		.f-tabstrip-header-inkbar {
			position: absolute;
			left: 27px;
			width: 78px;
			z-index: 1;
			bottom: -1px;
			display: block;
			height: 3px;
			background-color: #007465; /* #007465; */
			-webkit-transition: width .3s, left .3s;
			transition: width .3s, left .3s;
		}

		.mytabbtn:hover {
			color: #007465;
			background-color: #ddd;
		}

		@media (max-width: 767px) {
			.hiddenty-xs {
				display: none !important;
			}
		}

		/*.ccrame{
		 transition:all 1s ease-out 
		
		}*/
	</style>
</head>
<body style="overflow-y:hidden;">
<div class="layout-admin">
	<!-- top -->
	<header class="layout-header">
		<span class="header-logo"><img alt="image" width="190" height="68" src="plug-in/login/images/jeecg-aceplus.png"/></span>

		<div class="titlerow">
			<div class="titlecell">
				<input id="searchbox" name="functionName" placeholder="请输入搜索关键字" class="searchbox" style="padding-right: 23px;border:0">
				<div class="iconssdiv" onclick="checkput()">
					<i class="iconfont icon-close ui-iconss" style="font-weight:700;font-size:14px;display:none"></i>
					<i class="iconfont icon-sousuo ui-iconss"></i>
				</div>
			</div>
		</div>
		<ul class="header-bar">

			<li class="header-bar-nav personInfo hiddenty-xs" style="cursor:pointer;">
				<i class="icon-font">&#xe751;</i>&nbsp;
				<span>控制面板</span>
				<i class="icon-font adminIcon" style="margin-right:5px;">&#xe607;</i>
				<ul class="header-dropdown-menu" style="padding-right:4px">
					<li>
						<a href="javascript:createdetailwindow('<t:mutiLang langKey="common.ssms.getSysInfos"/>','tSSmsController.do?goMySmsList',800,400)" title="系统消息">系统消息</a>
					</li>
					<li><a href="javascript:clearLocalstorage()"><t:mutiLang langKey="common.clear.localstorage"/></a></li>
				</ul>
			</li>


			<li class="header-bar-nav hiddenty-xs">
				<a href="javascript:add('首页风格','userController.do?changestyle','',550,270)" title="换肤">
					<i class="icon-font">&#xe615;</i>&nbsp;风格切换
				</a>
			</li>


			<li class="header-bar-nav personInfo">
				<a href="javascript:;" id="personInfo">
					<span>
						<img src="plug-in/themes/fineui/common/image/head.jpg" style="width:24px;display:inline-block;border-radius:20px">
						<span>${userName}</span>
						<i class="icon-font adminIcon" style="margin-right:5px;">&#xe607;</i>
					</span>
				</a>

				<ul class="header-dropdown-menu" style="padding-right:4px">
					<li>
						<a href="javascript:openwindow('个人信息','userController.do?userinfo')">
							个人信息
						</a>
					</li>
					<li>
						<a href="javascript:add('<t:mutiLang langKey="common.change.password"/>','userController.do?changepassword','',550,200)">
							<t:mutiLang langKey="common.change.password"/>
						</a>
					</li>
					<li>
						<a href="javascript:logout()">
							<i class="icon-off"></i>
							<t:mutiLang langKey="common.logout"/>
						</a>
					</li>
				</ul>

			</li>
		</ul>
	</header>

	<!-- 左侧菜单 -->
	<aside class="layout-side">
		<ul class="side-menu">
			<t:menu style="fineui" menuFun="${menuMap}"></t:menu>
		</ul>
	</aside>

	<!-- 切换左侧菜单栏 -->
	<div id="toggleLeftMenu" class="layout-side-arrow" style="display:none">
		<div class="layout-side-arrow-icon">
			<i class="icon-font">&#xe60e;</i>
		</div>
	</div>

	<!-- 右侧home -->
	<section class="layout-main">
		<div class="layout-main-tab">
			<button onclick="toggleLeftMenu(this);" class="tab-btn btn-left" title="折叠菜单"><i class="icon-font">&#xe60e;</i></button>
			<!-- <button class="tab-btn btn-left" style="left:18px"><i class="icon-font">&#xe628;</i></button> -->
			<nav class="tab-nav">
				<div class="tab-nav-content" id="tab-contents-div">
					<div id="tytabbottomsepar" class="f-tabstrip-header-inkbar"></div>
					<a href="javascript:void(0);" id="myhomeAtag" class="content-tab active" data-id="home.html">
						<span class="fa fa-home colorgray"></span>首页</a>
				</div>
			</nav>

			<button id="activeTabToolRefresh" class="tab-btn mytabbtn" style="right:30px;" title="刷新本页"><i class="icon-font" style="font-size:16px;">&#xe60b;</i></button>
			<button class="tab-btn btn-right"><i class="icon-font">&#xe629;</i></button>
		</div>
		<div class="layout-main-body" style="margin:0;overflow-y: hidden;">
			<iframe class="body-iframe" name="iframe0" width="100%" height="100%"
					src="loginController.do?fineuiHome" frameborder="0" data-id="home.html" seamless></iframe>
		</div>
	</section>

</div>
</div>
<script src="plug-in/jquery/jquery-1.9.1.js"></script>
<script src="plug-in/jquery-plugs/i18n/jquery.i18n.properties.js"></script>
<script src="plug-in/lhgDialog/lhgdialog.min.js"></script>

<script type="text/javascript" src="plug-in/themes/fineui/common/js/sccl.js"></script>
<script type="text/javascript" src="plug-in/themes/fineui/common/js/sccl-util.js"></script>

<script type="text/javascript" src="plug-in/themes/fineui/smart-menu/jquery-smartMenu.js"></script>
<script src="plug-in/jquery-plugs/storage/jquery.storageapi.min.js"></script>
<!-- 自动补全 -->
<link rel="stylesheet" href="plug-in/jquery/jquery-autocomplete/jquery.autocomplete.css" type="text/css"></link>
<script type="text/javascript" src="plug-in/jquery/jquery-autocomplete/jquery.autocomplete.min.js"></script>

<%-- mqtt --%>
<script src="plug-in/mqtt/mqttws31.js" type="text/javascript"></script>
<%--Lobibox--%>
<script src="plug-in/lobibox/lobibox.min.js"></script>
<link href="plug-in/lobibox/Lobibox.min.css" rel="stylesheet">
<script type="text/javascript">
	//i18n前段国际化
	initI18nConfig();
	Lobibox.notify('info', {	//warning,error,success
		title: '提示信息',
		img: 'plug-in/lobibox/images/info.png',
		msg: '推荐谷歌浏览器，获得更快响应速度!'
	});

	function checkput() {
		var name = $("#searchbox").val();
		$.ajax({
			type: "POST",
			url: "loginController.do?getUrlpage",
			dataType: "text",
			data: "urlname=" + name,
			success: function (urlname) {
				var h = urlname;
				var options = {url: h, id: 99, title: name};
				addFineuiTab(options);
			}
		});
	}

	function getTremValueuserName() {
		return $("#searchbox").val();
	}

	// $("#searchbox").autocomplete("loginController.do?getAutocomplete", {
	// 	max: 8,
	// 	minChars: 1,
	// 	scroll: true,
	// 	width: 150,
	// 	scrollHeight: 180,
	// 	matchContains: true,
	// 	matchSubset: true,
	// 	autoFill: false,
	// 	extraParams: {
	// 		featureClass: "P",
	// 		style: "full",
	// 		maxRows: 10,
	// 		labelField: "functionName",//提示显示的字段
	// 		valueField: "functionName",//传递后台的字段
	// 		searchField: "functionName",//查询关键字字段
	// 		entityName: "TSFunction",//实体名称
	// 		trem: getTremValueuserName
	// 	},
	// 	parse: function (data) {
	// 		return jeecgAutoParse.call(this, data);
	// 	},
	// 	formatItem: function (row, i, max) {
	// 		return row['functionName'];
	// 	}
	// }).result(function (event, row, formatted) {
	// 	$("#searchbox").val(row['functionName']);
	// });
	//
	// //给输入框绑定按键事件
	// $("#searchbox").keydown(function () {
	// 	if (event.keyCode == "13") {
	// 		var name = $("#searchbox").val();
	// 		$.ajax({
	// 			type: "POST",
	// 			url: "loginController.do?getUrlpage",
	// 			dataType: "text",
	// 			data: "urlname=" + name,
	// 			success: function (urlname) {
	// 				var h = urlname;
	// 				var options = {url: h, id: 99, title: name};
	// 				addFineuiTab(options);
	// 			}
	// 		});
	// 	}
	// })


	function logout() {
		location.href = "loginController.do?logout";
	}

	function toSwagger() {
		window.open("swagger/index.html", "_blank");
	}

	$(function () {
		mqttConnect();
		//刷新本页面
		$("#activeTabToolRefresh").click(function () {
			var dataId = $("ul.side-menu").find("li.active").find("a").attr("href");
			if (!dataId) {
				dataId = "home.html";
			}
			var obj = $('.body-iframe[data-id="' + dataId + '"]');
			var obj_none = obj.css('display');
			if (obj_none == 'none') {
				obj = $('.body-iframe[data-id="home.html"]');
			}
			obj.attr('src', obj.attr('src'));
		});

		//搜索框样式效果
		// $("#searchbox").focus(function () {
		// 	$(this).addClass("searchbox-focus").addClass("searchbox-focusbg");
		// 	$(this).next("div").addClass("ui-iconss-focus");
		// });
		// $("#searchbox").blur(function () {
		// 	$(this).removeClass("searchbox-focus").removeClass("searchbox-focusbg");
		// 	$(this).next("div").removeClass("ui-iconss-focus");
		// });
		$("body").css("height", document.documentElement.clientHeight);
		//fineui首页菜单样式bug-临时方案---
		$(window).resize(function () {
			$("body").css("height", document.documentElement.clientHeight);
		});

	});

	/*
	 * connect mqtt server
	 * zhouwr
	 * 2019.08.20 aw
	 */
	let clientId = "forallcn-iotcp-fineuiMain";
	function mqttConnect() {
		clientId += '-' + new Date().getTime();
		client = new Paho.MQTT.Client("115.28.230.81", Number(8083), clientId); //建立客户端实例
		client.connect({
			userName: "admin",
			password: "public",
			onSuccess: onConnect
		}); //连接服务器并注册连接成功处理事件
		function onConnect() {
			console.log('mqtt[' + clientId + ']连接成功。。');
			client.subscribe("$forallcn/iotcp/alarm"); //订阅主题:数据报警
			client.subscribe("$forallcn/iotcp/welcome"); //订阅主题：欢迎使用APP
			client.subscribe("$forallcn/iotcp/sysMsg"); //订阅主题：系统其它消息
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
			mqttConnect();
		}
	}

	/*
	 * mqtt 接收消息
	 * 2018.03.30 pw
	 */
	function onMessageArrived(message) {
		var topic = message.destinationName;
		var qos = message.qos;
		if (qos > 0) {
			mqttSend(topic, '');
		}
		if (message.payloadString.length > 0) {
			Lobibox.notify("error", {
				title: '数据报警',
				delay: false,
				img: 'plug-in/lobibox/images/error.png',
				msg: message.payloadString
			});
		}
	}

	/*
	 * mqtt发送消息
	 * 2018.03.30 pw
	 */
	function mqttSend(destination, txt) {
		txt = txt.toString();
		if (txt != "") {
			message = new Paho.MQTT.Message(txt);
			message.destinationName = destination;
			client.send(message);
		}
	}

	$(".personInfo").hover(function () {
		$(this).find(".adminIcon").html("&#xe504;");
		$(this).children(".header-dropdown-menu").css("width", $(this).width() - 5);
	}, function () {
		$(this).find(".adminIcon").html("&#xe607;");
	});

	function clearLocalstorage() {
		var storage = $.localStorage;
		if (!storage)
			storage = $.cookieStorage;
		storage.removeAll();
		//bootbox.alert( "浏览器缓存清除成功!");
		layer.msg("浏览器缓存清除成功!");
	}

	//菜单折叠切换
	function toggleLeftMenu(obj) {
		if ($('#toggleLeftMenu').hasClass("close")) {
			$(obj).attr("title", "折叠菜单").find("i").html("&#xe60e;");
		} else {
			$(obj).attr("title", "展开菜单").find("i").html("&#xe501;");
		}
		$('#toggleLeftMenu').click();
	}

	/**
	 * i18n国际化配置
	 */
	function initI18nConfig() {
		var i18n_browser_Lang = getCookie("i18n_browser_Lang");
		if (i18n_browser_Lang == 'zh-cn') {
			i18n_browser_Lang = 'zh';
		}
		$.i18n.properties({
			name: 'jeecgs',    		//属性文件名     命名格式： 文件名_国家代号.properties
			path: 'plug-in/i18n/',   //注意这里路径是你属性文件的所在文件夹
			mode: 'map',
			language: i18n_browser_Lang,//这就是国家代号 name+language刚好组成属性文件名：strings+zh -> strings_zh.properties
			callback: function () {

			}
		});
	}
</script>
</body>
</html>

