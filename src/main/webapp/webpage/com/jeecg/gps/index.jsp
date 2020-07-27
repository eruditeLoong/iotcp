<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<title>Title</title>
	<t:base type="jquery,tools"></t:base>
	<script type="text/javascript" src="plug-in/jquery/jquery-md5.js"></script>
	<script>
		$(function () {
			var appid = "190828874297";
			var imei = "363620190323031";
			var msgId = "dadf7a7fd3454e5facde2d450957a275";

			var stringA = "appid=" + appid + "&imei=" + imei + "&msgId=" + msgId;
			var stringSignTemp = stringA + "&key=010af310d15b4541900e0d22be7ddd4b";
			var sign = $.md5(stringSignTemp).toUpperCase();
			console.log(sign);

			var data = {
				"appid": appid,
				"imei": imei,
				"msgId": msgId,
				"sign": sign
			}
			var url = "https://api.youshusoft.com/gpscloud";
			var search = "/service/locate";

			console.log("url: " + url + search);
			console.log("data: " + JSON.stringify(data));

			$.ajax({
				type: "POST",
				url: url + search,
				contentType:'application/json;charset=utf-8',
				data: JSON.stringify(data),
				dataType: 'json',
				success: function (data) {
					console.log(data);
				},
				error: function (e) {
					console.log(e);
				}
			});

			$.getJSON('https://api.huixun.me/open/device-info/353520180310438?token=096fa65bfa9b4643af6054c566393f05_135', function (data) {
				console.log(data);
			});

		});
	</script>
</head>
<body>

</body>
</html>
