let fencePosition = {

    enable: true,
    type: 0,
    pointsArray: [],
    addMouseMoveEvent: false,
    fenceColor: '#ff0000',

    changeColor: function (color) {
        fencePosition.fenceColor = color;
    }, changeHeight: function (height) {
        fencePosition.planeGroundHeight = height;
    },
    createFence: function () {
        let _this = this;
        toolbar.addBar('添加围栏', 'object-group', function (e) {
            const ul = document.createElement("ul");
            ul.style.padding = '10px';
            ul.innerHTML += '<li class="fa fa-object-ungroup fa-2x" style="color: red;padding: 10px;border:1px solid #FFF" onclick="fencePosition.initFenceEvent(1);" onmouseover="this.style.borderColor=\'#389fd6\'" onmouseout="this.style.borderColor=\'#FFF\'" title="矩形"></li>';
            ul.innerHTML += '<li class="fa fa-bullseye fa-2x" style="margin: 0px 10px; color: green;padding: 10px;border:1px solid #FFF" onclick="fencePosition.initFenceEvent(2);" onmouseover="this.style.borderColor=\'#389fd6\'" onmouseout="this.style.borderColor=\'#FFF\'" title="圆形"></li>';
            ul.innerHTML += '<li class="fa fa-skyatlas fa-2x" style="color: blue;padding: 10px;border:1px solid #FFF" onclick="fencePosition.initFenceEvent(3);" onmouseover="this.style.borderColor=\'#389fd6\'" onmouseout="this.style.borderColor=\'#FFF\'" title="不规则"></li>';
            const pDiv = document.createElement('div');
            pDiv.appendChild(ul);

            let dColor = document.createElement('div');
            dColor.id = 'fenceColor';
            dColor.style.paddingLeft = '25px';
            dColor.style.color = "#7b7b7b";
            dColor.innerHTML = '围栏颜色：<input id="fenceColor" type="color" style="width: 40px;border-radius: 3px;border: 1px solid #eee;padding: 2px;" value="' + fencePosition.fenceColor + '" onchange="fencePosition.changeColor(this.value);"/>';
            pDiv.appendChild(dColor);
            //询问框
            const index = layer.open({
                type: 1,
                anim: 1,
                title: false, //不显示标题
                content: pDiv.innerHTML,
                shadeClose: true,
                closeBtn: 0
            });
        });
        // container.addEventListener('mousedown', this.drowFence, false);
        this.showFence();
    },

    showFence: function () {
        let _this = this;
        // 异步请求围栏顶点数据集合
        $.getJSON('positionFenceController.do?getFencePoints', res => {
            let fences = res.obj || [];
            for (let i = 0; i < fences.length; i++) {
                let fence = fences[i];
                let lineMaterial = new THREE.LineBasicMaterial({color: fence.color});
                let lineGeometry;
                switch (fence.type) {
                    case 1:
                        lineGeometry = _this.getRactGeometry(fence.points);
                        break;
                    case 2:
                        break;
                    case 3:
                        lineGeometry = _this.getCustomizeGeometry(fence.points);
                        break;
                    default:
                        break;
                }
                let line = new THREE.Line(lineGeometry, lineMaterial);
                line.name = fence.name;
                line.id = fence.id;
                scene.add(line);
            }
        });
    },

    /**
     * 获取矩形几何形状
     * @param points 矩形的对角点
     */
    getRactGeometry: function (points) {
        points = points || [];
        let lineGeometry = new THREE.Geometry();
        lineGeometry.vertices.push(new THREE.Vector3(points[0].x, points[0].y, points[0].z));
        lineGeometry.vertices.push(new THREE.Vector3(points[0].x, points[0].y, points[1].z));
        lineGeometry.vertices.push(new THREE.Vector3(points[1].x, points[1].y, points[1].z));
        lineGeometry.vertices.push(new THREE.Vector3(points[1].x, points[1].y, points[0].z));
        lineGeometry.vertices.push(new THREE.Vector3(points[0].x, points[0].y, points[0].z));
        return lineGeometry;
    },

    /**
     * 获取自定义几何形状
     * @param points 所有顶点集合
     */
    getCustomizeGeometry: function (points) {
        points = points || [];
        let lineGeometry = new THREE.Geometry();
        for (let i = 0; i < points.length; i++) {
            lineGeometry.vertices.push(new THREE.Vector3(points[i].x, points[i].y, points[i].z));
        }
        lineGeometry.vertices.push(new THREE.Vector3(points[0].x, points[0].y, points[0].z));
        return lineGeometry;
    },

    removeEvent: function (e) {
        console.log(e);
    },

    saveFence: function (pointArr) {
        if (pointArr.length > 0) {
            console.log('保存围栏数据：' + fencePosition.pointsArray);
            console.log('围栏样式：' + fencePosition.type);
            console.log('基面高度：' + fencePosition.planeGroundHeight);
            console.log('颜色：' + fencePosition.fenceColor);
        }
    },

    initFenceEvent: function (type) {
        let _this = this;
        layer.closeAll();
        fencePosition.type = type;
        deploy.hideBar();
        container.removeEventListener('mousemove', onDocumentMouseMove, false);
        container.removeEventListener("mousedown", onDocumentMouseDown, false);
        container.removeEventListener("mouseup", onDocumentMouseUp, false);
        // 添加新监听
        container.addEventListener("mousedown", _this.onMouseDown, false);
    },
    drowRect: function (event) {
        if (event.type === "mousedown") {
            if (1 == event.which) {
                let intersect = fencePosition.getIntersects(event);
                let pointsGeometry = new THREE.Geometry();
                pointsGeometry.vertices.push(intersect);
                let pointsMaterial = new THREE.PointsMaterial({color: 0x0000FF, size: 2});
                let points = new THREE.Points(pointsGeometry, pointsMaterial);
                fencePosition.pointsArray.push(points);
                if (fencePosition.pointsArray.length == 1) {
                    points.name = 'fence_pre';
                    scene.add(points);
                } else if (fencePosition.pointsArray.length == 2) {
                    while (scene.getObjectByName('fence_move')) {
                        let o = scene.getObjectByName('fence_move');
                        o.name = '';
                        o.material.color = new THREE.Color(fencePosition.fenceColor.colorRgb());
                    }
                    fencePosition.addFence(sceneObj.conf.id, fencePosition.pointsArray);
                    fencePosition.pointsArray.splice(0, fencePosition.pointsArray.length);
                }
            }
        } else if (event.type === "mousemove") {
            let intersect = fencePosition.getIntersects(event);
            while (scene.getObjectByName('fence_move')) {
                scene.remove(scene.getObjectByName('fence_move'));
            }
            let lineGeometry = new THREE.Geometry();
            let lineMaterial = new THREE.LineBasicMaterial({color: 0x00ff00});
            if (fencePosition.pointsArray.length > 0) {
                lineGeometry.vertices.push(fencePosition.pointsArray[0].geometry.vertices[0]);
                // 两侧2
                let mouseVector32 = new THREE.Vector3(intersect.x, intersect.y, fencePosition.pointsArray[0].geometry.vertices[0].z);
                lineGeometry.vertices.push(mouseVector32);
                lineGeometry.vertices.push(intersect);
                let mouseVector33 = new THREE.Vector3(fencePosition.pointsArray[0].geometry.vertices[0].x, intersect.y, intersect.z);
                lineGeometry.vertices.push(mouseVector33);
                lineGeometry.vertices.push(fencePosition.pointsArray[0].geometry.vertices[0]);
                let line4 = new THREE.Line(lineGeometry, lineMaterial);
                line4.name = 'fence_move';
                scene.add(line4);
                // 对角点
                let pointsGeometry = new THREE.Geometry();
                pointsGeometry.vertices.push(intersect);
                let pointsMaterial = new THREE.PointsMaterial({color: fencePosition.fenceColor, size: 3});
                let points = new THREE.Points(pointsGeometry, pointsMaterial);
                points.name = 'fence_move';
                scene.add(points);
            }
        }
    },

    drowCircle: function (event) {
        if (event.type === "mousedown") {
            if (1 == event.which) {
                if (fencePosition.pointsArray.length <= 0) {
                    let intersects = fencePosition.getIntersects(event);
                    var shape = new THREE.Shape();
                    shape.absarc(intersects.x, intersects.z, 0, 0, Math.PI * 2, true);
                    var points = shape.getPoints();
                    var geometryPoints = new THREE.BufferGeometry().setFromPoints(points);
                    var material = new THREE.LineBasicMaterial({
                        color: fencePosition.fenceColor
                    });
                    var circle = new THREE.Line(geometryPoints, material);
                    circle.rotation.x = Math.PI / 2;
                    circle.name = 'fence_move';
                    scene.add(circle);
                    fencePosition.pointsArray.push(circle);
                } else {
                    while (scene.getObjectByName('fence_move')) {
                        var o = scene.getObjectByName('fence_move');
                        o.name = '';
                    }
                    fencePosition.pointsArray.splice(0, fencePosition.pointsArray.length);
                }
            }
        } else if (event.type === "mousemove") {
            if (fencePosition.pointsArray.length > 0) {
                let intersect = fencePosition.getIntersects(event);
                let c = fencePosition.pointsArray[0];
                // 计算两点的距离
                let x0 = c.geometry.boundingSphere.center.x;
                let y0 = c.geometry.boundingSphere.center.y;
                let x1 = intersect.x;
                let y1 = intersect.z;
                let r = Math.sqrt(((x0 - x1) * (x0 - x1)) + ((y0 - y1) * (y0 - y1)));
                c.geometry.boundingSphere.radius = r;
            }
        }
    },

    drowcustomize: function (event) {
        if (event.type === "mousedown") {
            if (1 == event.which) {
                let intersect = fencePosition.getIntersects(event);
                let pointsGeometry = new THREE.Geometry();
                pointsGeometry.vertices.push(intersect);
                let pointsMaterial = new THREE.PointsMaterial({color: fencePosition.fenceColor, size: 3});
                let points = new THREE.Points(pointsGeometry, pointsMaterial);
                fencePosition.pointsArray.push(points);
                /* 创建线段 */
                const lineGeometry = new THREE.Geometry();
                const lineMaterial = new THREE.LineBasicMaterial({
                    color: fencePosition.fenceColor,
                });
                let pLen = fencePosition.pointsArray.length;
                if (pLen >= 2) {
                    lineGeometry.vertices.push(fencePosition.pointsArray[pLen - 2].geometry.vertices[0], fencePosition.pointsArray[pLen - 1].geometry.vertices[0]);
                    let line = new THREE.Line(lineGeometry, lineMaterial);
                    scene.add(line);
                }
                scene.add(points);
            } else if (3 == event.which) {
                // 收尾连接
                const lineGeometry = new THREE.Geometry();
                let pLen = fencePosition.pointsArray.length;
                lineGeometry.vertices.push(fencePosition.pointsArray[0].geometry.vertices[0], fencePosition.pointsArray[pLen - 1].geometry.vertices[0]);
                const lineMaterial = new THREE.LineBasicMaterial({
                    color: fencePosition.fenceColor,
                });
                let line = new THREE.Line(lineGeometry, lineMaterial);
                scene.add(line);
                while (scene.getObjectByName('fence_move')) {
                    scene.remove(scene.getObjectByName('fence_move'));
                }
                fencePosition.addFence(sceneObj.conf.id, fencePosition.pointsArray);
                fencePosition.pointsArray.splice(0, fencePosition.pointsArray.length);
            }
        } else if (event.type === "mousemove") {
            let intersects = fencePosition.getIntersects(event);
            while (scene.getObjectByName('fence_move')) {
                scene.remove(scene.getObjectByName('fence_move'));
            }
            let lineGeometry = new THREE.Geometry();
            let lineMaterial = new THREE.LineBasicMaterial({color: 0x00ff00});
            if (fencePosition.pointsArray.length > 0) {
                lineGeometry.vertices.push(fencePosition.pointsArray[0].geometry.vertices[0]);
                let mouseVector3 = new THREE.Vector3(intersects.x, intersects.y, intersects.z);
                lineGeometry.vertices.push(mouseVector3);
                let line = new THREE.Line(lineGeometry, lineMaterial);
                line.name = 'fence_move';
                scene.add(line);
                lineGeometry.vertices.push(fencePosition.pointsArray[fencePosition.pointsArray.length - 1].geometry.vertices[0]);
                lineGeometry.vertices.push(mouseVector3);
                let line2 = new THREE.Line(lineGeometry, lineMaterial);
                line2.name = 'fence_move';
                scene.add(line2);
            }
        }
    },
    onMouseDown: function (event) {
        if (!fencePosition.addMouseMoveEvent) {
            window.addEventListener('mousemove', fencePosition.onMouseMove, false);
            /* 依据 windwo_mouse 标识避免事件的重复添加 */
            fencePosition.addMouseMoveEvent = true;
        }
        switch (fencePosition.type) {
            case 1:
                fencePosition.drowRect(event);
                break;
            case 2:
                fencePosition.drowCircle(event);
                break;
            case 3:
                fencePosition.drowcustomize(event);
                break;
            default:
                break;
        }
        if (event.which === 3) {
            // 修改监听
            fencePosition.eventChange();
            // 保存围栏顶点
            fencePosition.saveFence(fencePosition.pointsArray);
        }
    },

    /* 鼠标移动事件 */
    onMouseMove: function (event) {
        switch (fencePosition.type) {
            case 1:
                fencePosition.drowRect(event);
                break;
            case 2:
                fencePosition.drowCircle(event);
                break;
            case 3:
                fencePosition.drowcustomize(event);
                break;
            default:
                break;
        }
    },

    /* 获取射线与平面相交的交点 */
    interPointOld: null,
    getIntersects: function (event) {
        let raycaster = new THREE.Raycaster();
        let mouse = new THREE.Vector2();
        mouse.x = (event.clientX / window.innerWidth) * 2 - 1;
        mouse.y = -(event.clientY / window.innerHeight) * 2 + 1;
        raycaster.setFromCamera( mouse, camera );
        var intersections = raycaster.intersectObjects( scene.children, true );
        let intersection = ( intersections.length ) > 0 ? intersections[ 0 ] : null;
        if (intersection != null ) {
            fencePosition.interPointOld = intersection.point;
            return intersection.point;
        }else{
            return fencePosition.interPointOld;
        }
    },

    eventChange: function () {
        // 移除本页鼠标事件
        container.removeEventListener('mousemove', fencePosition.onMouseMove, false);
        container.removeEventListener('mousedown', fencePosition.onMouseDown, false);
        fencePosition.addMouseMoveEvent = false;
        // 开启其它事件
        container.addEventListener('mousemove', onDocumentMouseMove, false);
        container.addEventListener("mousedown", onDocumentMouseDown, false);
        container.addEventListener("mouseup", onDocumentMouseUp, false);
    },

    roundedRect: function (shape, x, y, width, height, radius) {
        shape.moveTo(x, y + radius);
        shape.lineTo(x, y + height - radius);
        shape.quadraticCurveTo(x, y + height, x + radius, y + height);
        shape.lineTo(x + width - radius, y + height);
        shape.quadraticCurveTo(x + width, y + height, x + width, y + height - radius);
        shape.lineTo(x + width, y + radius);
        shape.quadraticCurveTo(x + width, y, x + width - radius, y);
        shape.lineTo(x + radius, y);
        shape.quadraticCurveTo(x, y, x, y + radius);
        return shape
    },

    addFence: function (sceneBy, pointObjs = []) {
        let points = [];
        for (let i = 0; i < pointObjs.length; i++) {
            let v = {
                x: pointObjs[i].geometry.vertices[0].x,
                y: pointObjs[i].geometry.vertices[0].y,
                z: pointObjs[i].geometry.vertices[0].z
            }
            points.push(v);
        }
        let url = "positionFenceController.do?goAdd&sceneBy=" + sceneBy + "&color=" + fencePosition.fenceColor.substring(1, 7) + "&type=" + fencePosition.type + "&points=" + encodeURIComponent(JSON.stringify(points), 'utf-8');
        createwindow('表单修改', url, 780, 400);
    }
}


//十六进制颜色值域RGB格式颜色值之间的相互转换

//-------------------------------------
//十六进制颜色值的正则表达式
var reg = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/;
/*RGB颜色转换为16进制*/
String.prototype.colorHex = function () {
    var that = this;
    if (/^(rgb|RGB)/.test(that)) {
        var aColor = that.replace(/(?:\(|\)|rgb|RGB)*/g, "").split(",");
        var strHex = "#";
        for (var i = 0; i < aColor.length; i++) {
            var hex = Number(aColor[i]).toString(16);
            if (hex === "0") {
                hex += hex;
            }
            strHex += hex;
        }
        if (strHex.length !== 7) {
            strHex = that;
        }
        return strHex;
    } else if (reg.test(that)) {
        var aNum = that.replace(/#/, "").split("");
        if (aNum.length === 6) {
            return that;
        } else if (aNum.length === 3) {
            var numHex = "#";
            for (var i = 0; i < aNum.length; i += 1) {
                numHex += (aNum[i] + aNum[i]);
            }
            return numHex;
        }
    } else {
        return that;
    }
};

//-------------------------------------------------

/*16进制颜色转为RGB格式*/
String.prototype.colorRgb = function () {
    var sColor = this.toLowerCase();
    if (sColor && reg.test(sColor)) {
        if (sColor.length === 4) {
            var sColorNew = "#";
            for (var i = 1; i < 4; i += 1) {
                sColorNew += sColor.slice(i, i + 1).concat(sColor.slice(i, i + 1));
            }
            sColor = sColorNew;
        }
        //处理六位的颜色值
        var sColorChange = [];
        for (var i = 1; i < 7; i += 2) {
            sColorChange.push(parseInt("0x" + sColor.slice(i, i + 2)));
        }
        return "rgb(" + sColorChange.join(",") + ")";
    } else {
        return sColor;
    }
};