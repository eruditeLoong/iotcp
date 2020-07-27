<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">
    <link rel="stylesheet" href="https://a.amap.com/jsapi_demos/static/demo-center/css/demo-center.css"/>
    <title>地图显示</title>
    <style>
        html,
        body,
        #container {
            width: 100%;
            height: 100%;
        }
    </style>
</head>
<body>
<div id="container">
</div>
<!-- 加载地图JSAPI脚本 -->
<script src="<%=basePath%>/webpage/com/jeecg/position/js/gaode-maps.js"></script>
<script>
    var longitude = "${positionTrajectory.longitude}";
    var latitude = "${positionTrajectory.latitude}";
    var lnglat = [longitude, latitude];
    var map;
    window.onload = function () {
        map = new AMap.Map('container', {
            resizeEnable: true, //是否监控地图容器尺寸变化
            zoom: 60, //初始化地图层级
            center: lnglat //初始化地图中心点
        });
        regeoCode(lnglat, function (address) {
            addMarker(lnglat, address);
        });
    }

    // 实例化点标记
    function addMarker(lnglat, title) {
        marker = new AMap.Marker({
            position: lnglat,
        });
        marker.setMap(map);
        // 设置标签
        marker.setLabel({
            offset: new AMap.Pixel(20, 20),
            content: title
        });
    }

    function regeoCode(lnglat, callback) {
        var geocoder = new AMap.Geocoder({
            city: "010", //城市设为北京，默认：“全国”
            radius: 1000 //范围，默认：500
        });
        geocoder.getAddress(lnglat, function (status, result) {
            if (status === 'complete' && result.regeocode) {
                var address = result.regeocode.formattedAddress;
                callback(address);
            } else {
                log.error('根据经纬度查询地址失败');
            }
        });
    }

    // 坐标转换
    function convertFrom(lnglat, type, callback) {
        AMap.convertFrom(lnglat, type, function (status, result) {
            if (result.info === 'ok') {
                var resLnglat = result.locations[0];
                callback(resLnglat);
            }
        });
    }
</script>
</body>
</html>