var deviceRelation = {
    // 层级线的数组
    lineObjs: [],
    isValid: false,
    /**
     * 显示设备关系线
     */
    display: function (isValid) {
        if (isValid) {
            var geometry = new THREE.Geometry();
            for (var i in objects) {
                var g = objects[i];
                if (g.userData.type == 'gateway') { // 网关设备 terminal
                    for (var j in objects) {
                        var t = objects[j];
                        if (g.userData.id === t.userData.parentBy) {
                            geometry.vertices.push(g.position, t.position);
                            let material = new THREE.LineBasicMaterial({
                                color: 0xff0000
                            });
                            var line = new THREE.Line(geometry, material);
                            scene.add(line);
                            this.lineObjs.push(line);
                        }
                    }
                }
                // 场景半透明
                if (g.userData.type == 'scene') {
                    var gs = device.getChildObjs(g);
                    for (var j in gs) {
                        var gms = gs[j].material;
                        if (gms instanceof Array) {
                            for (var k in gms) {
                                gms[k].opacity = 0.1;
                                gms[k].transparent = true;
                            }
                        } else {
                            gms.opacity = 0.1;
                            gms.transparent = true;
                        }
                    }
                }
            }
        } else {
            // 删除
            while (this.lineObjs.length > 0) {
                scene.remove(this.lineObjs[0]);
                this.lineObjs.splice(0, 1);
            }
            for (var i in objects) {
                var g = objects[i];
                // 场景半透明
                if (g.userData.type == 'scene') {
                    var gs = device.getChildObjs(g);
                    for (var j in gs) {
                        var gms = gs[j].material;
                        if (gms instanceof Array) {
                            for (var k in gms) {
                                gms[k].opacity = 1;
                                gms[k].transparent = false;
                            }
                        } else {
                            gms.opacity = 1;
                            gms.transparent = false;
                        }
                    }
                }
            }
        }
    },
}