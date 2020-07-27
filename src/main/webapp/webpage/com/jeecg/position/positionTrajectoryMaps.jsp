<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">
    <title>轨迹回放</title>
    <link rel="stylesheet" href="https://a.amap.com/jsapi_demos/static/demo-center/css/demo-center.css"/>
    <style>
        html,
        body,
        #container {
            height: 100%;
            width: 100%;
        }

        .input-card .btn {
            margin-right: 1.2rem;
            width: 9rem;
        }

        .input-card .btn:last-child {
            margin-right: 0;
        }

        #mask {
            position: absolute;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            width: 100%;
            height: 100%;
            top: 0px;
            left: 0px;
            background-color: rgba(0, 0, 0, 0.3);
            z-index: 999;
        }

        #mask-title {
            color: #f9fffb;
            font-size: 26px;
        }
    </style>
</head>

<body>
<div id="container">
    <div id="mask" class="">
        <h5 id="mask-title">正在加载地图，请稍后。。。</h5>
        <div><a class="ace_button" href="javascript:location.reload()">重新加载</a></div>
    </div>
</div>
<div class="input-card">
    <h4>轨迹回放控制</h4>
    <div class="input-item">
        <input type="button" class="btn" value="开始动画" id="start" onclick="startAnimation()"/>
        <input type="button" class="btn" value="暂停动画" id="pause" onclick="pauseAnimation()"/>
    </div>
    <div class="input-item">
        <input type="button" class="btn" value="继续动画" id="resume" onclick="resumeAnimation()"/>
        <input type="button" class="btn" value="停止动画" id="stop" onclick="stopAnimation()"/>
    </div>
</div>
<script type="text/javascript"
        src="<%=basePath%>/webpage/com/jeecg/position/js/gaode-maps.js"></script>
<script>
    var marker, map, lineArr = new Array();
    window.onload = function () {
        var pts = [117.132187, 36.653958];
        map = new AMap.Map("container", {
            resizeEnable: true,
            center: lineArr[0],
            zoom: 60
        });
        $('#mask-title').text('正在加载轨迹，请稍后。。。');
        $.getJSON('positionTrajectoryController.do?getTrajectMaps&imei=${imei}', res => {
            lineArr = [[117.132187, 36.653958]];
            let maps = res.obj;
            for (let i = 0; i < maps.length; i++) {
                let o = maps[i];
                console.log(o);
                lineArr[i] = [o.longitude, o.latitude]
            }
            marker = new AMap.Marker({
                map: map,
                position: lineArr[0],
                icon: "https://webapi.amap.com/images/car.png",
                offset: new AMap.Pixel(-26, -13),
                autoRotation: true,
                angle: -90,
            });

            // 绘制轨迹
            var polyline = new AMap.Polyline({
                map: map,
                path: lineArr,
                showDir: true,
                strokeColor: "#28F",  //线颜色
                strokeOpacity: 0.5,     //线透明度
                strokeWeight: 5,      //线宽
                // strokeStyle: "solid"  //线样式
            });

            var passedPolyline = new AMap.Polyline({
                map: map,
                // path: lngLat,
                strokeColor: "#AF5",  //线颜色
                // strokeOpacity: 1,     //线透明度
                strokeWeight: 5,      //线宽
                // strokeStyle: "solid"  //线样式
            });

            marker.on('moving', function (e) {
                passedPolyline.setPath(e.passedPath);
            });

            map.setFitView();
            $('#mask').hide();
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

    function startAnimation() {
        console.log(lineArr || []);
        marker.moveAlong(lineArr || [], 200);
    }

    function pauseAnimation() {
        marker.pauseMove();
    }

    function resumeAnimation() {
        marker.resumeMove();
    }

    function stopAnimation() {
        marker.stopMove();
    }
</script>
</body>

</html>