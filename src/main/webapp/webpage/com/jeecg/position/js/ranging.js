var ranging = {

    addMouseMoveEvent: false,
    pointsArray: [],
    /**
     * 测距初始化
     */
    init: function () {
        let _this = this;
        toolbar.addBar('测距', 'crosshairs fa-2x', function (e) {
            console.log('测距初始化');
            document.body.style.cursor = 'crosshair';
            container.removeEventListener('mousemove', onDocumentMouseMove, false);
            container.removeEventListener("mousedown", onDocumentMouseDown, false);
            container.removeEventListener("mouseup", onDocumentMouseUp, false);
            // 添加新监听
            container.addEventListener("mousedown", ranging.onMouseDown, false);
            // 修改相机视角
            // orbit.maxPolarAngle = 0.0;
        });
    },

    /**
     * 鼠标点击
     * @param e
     */
    onMouseDown: function (e) {
        let intersect = ranging.getIntersects(e);

        let div = document.createElement('div');
        div.className = "fa fa-circle";
        div.style.color = '#FF0000';
        div.style.display = 'block';
        div.style.textShadow = '#00FF00 0px 0px 2px';
        let label = new THREE.CSS2DObject(div);
        label.position.copy(intersect);
        label.name = 'origin_point';
        scene.add(label);
        if (!ranging.addMouseMoveEvent) {
            window.addEventListener('mousemove', ranging.onMouseMove, false);
            /* 依据 addMouseMoveEvent 标识避免事件的重复添加 */
            ranging.addMouseMoveEvent = true;
        } else {
            window.removeEventListener('mousemove', ranging.onMouseMove, false);
            ranging.addMouseMoveEvent = false;
            // 获取两点
            let oPiont = scene.getObjectByName('origin_point');
            let dist = ranging.calcDistance(oPiont.position, intersect);
            layer.open({
                type: 1,
                title: false,
                closeBtn: 1,
                shadeClose: true,
                skin: 'layui-layer-nobg', //没有背景色
                content: '<div style="padding: 25px;margin: 0;border-radius: 5px;background-color:rgba(88, 130, 150, 0.76);' +
                    'border: 1px solid #85ffff;color:#85ffff">测量结果：' + dist + '米</div>'
            });
                    document.body.style.cursor = 'auto';
                    // 删除点线
                    scene.remove(scene.getObjectByName('ranging_move'));
                    while (scene.getObjectByName('origin_point')) {
                        scene.remove(scene.getObjectByName('origin_point'));
                    }
                    container.addEventListener('mousemove', onDocumentMouseMove, false);
                    container.addEventListener("mousedown", onDocumentMouseDown, false);
                    container.addEventListener("mouseup", onDocumentMouseUp, false);
                    // 添加新监听
                    container.removeEventListener("mousedown", ranging.onMouseDown, false);
                    // 修改相机视角
                    // orbit.maxPolarAngle = Math.PI;
        }
    },

    /**
     * 鼠标移动事件
     * @param e
     */
    onMouseMove: function (e) {
        let intersect = ranging.getIntersects(e);
        while (scene.getObjectByName('ranging_move')) {
            scene.remove(scene.getObjectByName('ranging_move'));
        }
        let oPoint = scene.getObjectByName('origin_point');
        let lineGeometry = new THREE.Geometry();
        let lineMaterial = new THREE.LineBasicMaterial({color: 0x00ff00});
        lineGeometry.vertices.push(oPoint.position);
        lineGeometry.vertices.push(intersect);
        let line = new THREE.Line(lineGeometry, lineMaterial);
        line.name = 'ranging_move';
        scene.add(line);
    },

    /**
     * 计算距离
     */
    calcDistance: function (piont1, piont2) {
        let zoom = sceneObj.conf.threeData.s;
        console.log(zoom);
        console.log(piont1.distanceTo(piont2));
        console.log((piont1.distanceTo(piont2) * zoom).toFixed(2));
        return (piont1.distanceTo(piont2) * zoom).toFixed(2);
    },

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
            ranging.interPointOld = intersection.point;
            return intersection.point;
        }else{
            return ranging.interPointOld;
        }
    },
}