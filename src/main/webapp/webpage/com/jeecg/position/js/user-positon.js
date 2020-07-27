let userPosition = {
    isLoadUsers: false,

    userListPanel: {
        width: '100px',
        height: '0px',
        top: '0px',
        left: '0px',
    },

    init: function () {
        // 创建按钮
        toolbar.addBar('人员定位', 'users', function () {
            if (userPosition.isLoadUsers) {
                // 已经加载，就隐藏

            } else {
                userPosition.loadUsers();
            }
        });
        
        storage.get("device",workStr,function(){});
        $('#userListPanel').hide();
    },

    display: function (isValid) {
        if (isValid) {
            $('#toolbar').show(300);
            $('#userStatus').show(300);
        } else {
            $('#toolbar').hide(300);
            $('#userStatus').hide(300);
        }
    },

    loadUsers: function () {
        let _this = this;
        let num = 0;
        userPosition.isLoadUsers = true;
        $.getJSON('positionUserController.do?getUsers', res => {
            let users = res.obj || [];
            for (let i = 0; i < users.length; i++) {
                let user = users[i];
                if (user.status) {  // 无效人员不添加到scene
                    let p = this.createLabel(user);
                }
                if (user.isInPoly) {
                    num++;
                }
            }
            _this.createToolbar();
            _this.showStatus(num, false);
            _this.mqttConnect();
            _this.getLocation(users);
        });
        ranging.init();

        // <ul id="deviceDepList" class="ztree"></ul>
        let userList = document.createElement('ul');
        userList.id = 'userList';
        userList.className = 'ztree';
        userList.style.cssText = 'padding: 10px;margin: 0;border-radius: 5px;background-color:rgba(88, 130, 150, 0.76);' +
            'width: 200px;height: auto;max-height: 600px;min-height: 150px;border: 1px solid #85ffff;overflow-x: hidden;overflow-y: auto;';
        let userListPanel = document.createElement('div');
        userListPanel.id = 'userListPanel';
        userListPanel.appendChild(userList);
        document.body.appendChild(userListPanel);
    },

    /* 主动请求后台获取定位 */
    getLocation: function (users) {
        let timer = setInterval(function () {
            for (let i = 0; i < users.length; i++) {
                let user = users[i];
                let uLabel = scene.getObjectByName('label-' + user.imei);
                if (uLabel != undefined) {
                    let time = parseInt(((new Date().getTime() - uLabel.userData.datetime) / 1000 / 60) + '');
                    if (time > 10) {
                        console.log(user.imei + ' 静止时间：' + time + ' 主动定位');
                        $.getJSON('positionUserController.do?manualPosition&imei=' + user.imei, res => {
                            console.log(res.msg);
                        });
                    }
                }
            }
        }, 1000 * 60 * 5);
    },

    createToolbar: function () {
        let toolbar = document.createElement("div");
        toolbar.id = 'userToolbar';
        toolbar.style.position = 'absolute';
        toolbar.style.top = '10px';
        toolbar.style.left = '10px';
        toolbar.style.width = '200px';
        toolbar.style.height = 'auto';
        toolbar.style.display = 'flex';
        toolbar.style.justifyContent = 'center';
        document.body.appendChild(toolbar);

        function addBar(barName, faIcon, onclick) {
            let toolbar = document.getElementById('userToolbar');
            let bar = document.createElement('a');
            bar.style.cssText = 'width:28px;height:28px;border:1px solid #85ffff;color:#85ffff;' +
                'background-color:rgba(125, 185, 214, 0.7);margin:0 2px;border-radius:28px';
            bar.title = barName;
            bar.href = 'javascript:;';
            bar.onclick = onclick;
            bar.innerHTML = '<i id="fa-' + faIcon + '" class="fa fa-' + faIcon + '"></i>'
            toolbar.appendChild(bar);
        }

        addBar('定位状态显示或隐藏', 'eye-slash', function () {
            let isStatusShow = document.getElementById('userStatus').style.display == 'block';
            if (isStatusShow) {
                document.getElementById('userStatus').style.display = 'none';
                document.getElementById('fa-eye-slash').setAttribute('class', 'fa fa-eye');
            } else {
                document.getElementById('fa-eye-slash').setAttribute('class', 'fa fa-eye-slash');
                document.getElementById('userStatus').style.display = 'block';
            }
        });
        addBar('刷新人数', 'refresh', function () {
            userPosition.resetIoSession();
        });
        
        
        /**
         * wwz
         * 2019/12/09
         */
        addBar('保存工作票','floppy-o', function(){
        	//let input = document.getElementById("text").innerHTML = "有值了";
      
        	let text = document.getElementById("text").value;
        	alert(text+"保存");
        	var workObj = {
    			id: "workStr",
    		    text: {text}
        	};
        	storage.set("device", workObj, function(){});
        }); 

        addBar('定位列表', 'list', function () {
            let zTree;
            let userTree = [];
            let setting = {
                data: {
                    simpleData: {
                        enable: true, // 简单数据模式
                        idKey: "id",
                        pIdKey: "pid",
                        rootPId: null
                    }
                },
            };
            var win = $.messager.progress({
                title: '请稍等',
                text: '正在加载设备部署列表...'
            });
            scene.traverse(obj => {
                if ((obj.type == "Object3D") && (obj.userData.isInPoly == true)) {
                    console.log(obj);
                    var tree = {
                        id: obj.userData.imei,
                        pid: obj.userData.company,
                        name: obj.userData.name
                    };
                    userTree.push(tree);
                }
            });
            if (userTree.length > 0)
                zTree = $.fn.zTree.init($('#userList'), setting, userTree);
            layer.open({
                type: 1,
                title: false,
                closeBtn: 1,
                shadeClose: true,
                skin: 'layui-layer-nobg', //没有背景色
                content: document.getElementById('userListPanel').innerHTML
            });
            $.messager.progress('close');
        });
    },

    createUsersPanel: function (id) {

    },

    showStatus: function (userNum, deviceStatus) {
        let panel = document.createElement('div');
        panel.id = 'userStatus';
        let h = document.getElementById('userToolbar').offsetHeight + 20;
        panel.style.cssText = 'position:absolute;top:' + h + 'px;left:10px;padding:0px';
        panel.style.display = deviceStatus ? 'block' : 'none';
        let dsPanel = document.createElement('div');
        dsPanel.style.cssText = 'width: 200px; height: 40px; border: 3px solid #006569a3; border-radius: 5px; border-top: 40px solid #006569a3; margin-bottom: 15px;background-color: #ffffff55';
        dsPanel.innerHTML = '<div style="position: relative;top:-40px;font-size: medium; font-family: fantasy;line-height:40px;">当前设备运行状态</div>' +
                            '<div style="position: relative;top:-40px;font-size: x-large;font-family: fantasy;line-height:40px;color: #00ff00;text-shadow: #4847ff 0px 0px 2px;"><i class="fa fa-cube" style="margin: 0 8px;"></i>正常</div>' +

                            '<div style="position: relative;top:-20px;font-size: medium; font-family: fantasy;line-height:40px;">当前在线人数&nbsp;</div>' +
                            '<div style="position: relative;top:-20px;font-size: x-large;font-family: fantasy;line-height:40px;color: rgba(219,9,0,0.71);text-shadow: #fff 0px 0px 2px;"><i class="fa fa-street-view" style="margin: 0 8px;"></i><span id="userNum">' + userNum + '</span>人</div>'+
                            /* start wwz 2019/12-09 增加工作票信息框 */
                            '<div style="position: relative;top: -5px;font-size: medium; font-family: fantasy;line-height:40px;">工作票&nbsp;</div>' +
                            '<div style="position: relative;top: -5px;font-size: x-large;font-family: fantasy;line-height:40px;text-shadow: #fff 0px 0px 2px;"><textarea id = "text"; style="resize:none;font-size:12px; color:#348526;background-color:#f2efe759;width:188px;height:140px;padding:5px;border:0"></textarea></div>';
                            /* end wwz 2019/12-09 增加工作票信息框 */
        let unPanel = document.createElement('div');
        unPanel.style.cssText = 'width: 200px;height: 40px;border: 3px solid #d40000a3;border-radius: 5px;border-top: 40px solid #d40000a3;background-color: #ffffff55;';
        let wrPanel = document.createElement('div');
        wrPanel.style.cssText = 'margin-top:15px;width: 200px;height: 150px;border: 3px solid #348526;border-radius: 5px;border-top: 40px solid #348526;background-color: #ffffff55';
      
        panel.appendChild(dsPanel);
        panel.appendChild(unPanel);
        panel.appendChild(wrPanel);
        
        
        document.body.appendChild(panel);
    },

    resetIoSession: function () {
        $.ajax({
            url: 'positionModuleController.do?resetIoSession',
            type: 'get',
            success: function (data) {
                let d = $.parseJSON(data);
                if (d.success) {
                    let msg = d.msg;
                    tip(msg);
                }
                userPosition.getInPolyNum();
            }
        });
    },

    createLabel: function (obj) {
        let aUser = document.createElement('a');
        aUser.href = "javascript:";
        aUser.className = "fa fa-map-marker fa-2x";
        aUser.style.color = obj.color;
        // aUser.setAttribute('onclick', 'userPosition.showUserInfo(e, ' + JSON.stringify(obj) + ')');
        aUser.setAttribute('onMouseOver', 'userPosition.showUserTip(' + JSON.stringify(obj) + ')');
        aUser.setAttribute('onmouseout', 'layer.close(layer.index)');
        let pName = document.createElement('p');
        pName.textContent = obj.name;
        pName.style.cssText = 'position: relative;top:-10px;color:' + obj.color;
        let div = document.createElement("div");
        div.id = "userLabelDiv-" + obj.imei;
        div.zIndex = 100;
        div.className = 'userLabelDiv';
        if (obj.isInPoly) {
            div.style.display = 'block';
        } else {
            div.style.display = 'none';
        }
        // 影子取反色
        let c = userPosition.colorReverse(obj.color);
        div.style.textShadow = c + ' 0px 0px 2px';
        div.appendChild(aUser);
        div.appendChild(pName);
        let label = new THREE.CSS2DObject(div);
        label.position.set(obj.x, 0, obj.y);
        label.name = "userLabel-" + obj.imei;
        label.userData['datetime'] = obj.datetime;
        label.userData['isInPoly'] = obj.isInPoly;
        label.userData['name'] = obj.name;
        label.userData['company'] = obj.company;
        label.userData['companyName'] = obj.companyName;
        label.userData['imei'] = obj.imei;
        scene.add(label);
        return label;
    },

    showUserTip: function (obj) {
        let label = scene.getObjectByName("userLabel-" + obj.imei);
        let time = fmtTime(new Date().getTime() - label.userData.datetime);
        let title = '<div style="text-align: left">单位：' + obj.companyName + '<br/>姓名：' + obj.name + '<br/>卡号：' + obj.imei + '<br/>静止时间：' + time + '</div>';
        layer.tips(title, '#userLabelDiv-' + obj.imei, {tips: [1, '#3595CC'],});

        function fmtTime(date) {
            let dayDiff = Math.floor(date / (24 * 3600 * 1000)); //计算出相差天数
            let leave1 = date % (24 * 3600 * 1000); //计算天数后剩余的毫秒数
            let hours = Math.floor(leave1 / (3600 * 1000)); //计算出小时数
            //计算相差分钟数
            let leave2 = leave1 % (3600 * 1000); //计算小时数后剩余的毫秒数
            let minutes = Math.floor(leave2 / (60 * 1000)); //计算相差分钟数
            //计算相差秒数
            let leave3 = leave2 % (60 * 1000); //计算分钟数后剩余的毫秒数
            let seconds = Math.round(leave3 / 1000);
            let time = dayDiff > 0 ? dayDiff + "天" : "" + hours > 0 ? hours + "时" : "" + minutes > 0 ? minutes + "分" : seconds + "秒";
            return time;
        }
    },

    showUserInfo: function (e, user) {
        e.stopPropagation();
        layer.open({
            type: 2,
            title: false,
            shade: 0.0001,
            offset: 'lb', //右下角弹出
            area: ['800px', '500px'],
            skin: 'userInfoDialog', //没有背景色
            closeBtn: 0,
            shadeClose: true,
            content: ['positionUserController.do?goUpdate&load=detail&id=' + user.id, 'no']
        });
    },

    clientId: 'forallcn-iotcp-userPosition',
    mqttConnect: function () {
        let _this = this;
        _this.clientId += '-' + new Date().getTime();
        let client = new Paho.MQTT.Client("115.28.230.81", Number(8083), _this.clientId); //建立客户端实例
        client.connect({
            userName: "admin",
            password: "public",
            onSuccess: function () {
                console.log('mqtt[' + _this.clientId + ']连接成功。。');
                client.subscribe('$forallcn/iotcp/position/#'); //订阅主题:实时数据数据
            }
        }); //连接服务器并注册连接成功处理事件

        // 注册连接断开处理事件
        client.onConnectionLost = _this.mqttConnectionLost;

        // 注册消息接收处理事件
        client.onMessageArrived = _this.mqttMessageArrived;
    },

    mqttConnectionLost: function (responseObject) {
        if (responseObject.errorCode !== 0) {
            console.log('mqtt[' + userPosition.clientId + ']连接已断开');
            userPosition.clientId = userPosition.clientId.substring(0, userPosition.clientId.lastIndexOf('-'));
            userPosition.mqttConnect();
        }
    },

    mqttMessageArrived: function (message) {
        let topic = message.destinationName;
        let qos = message.qos;
        let msg = message.payloadString;
        if (topic == '$forallcn/iotcp/position/location') {
            if (msg.length > 0) {
                let tJson = JSON.parse(msg);
                // console.log(JSON.stringify(tJson));
                let user = scene.getObjectByName('userLabel-' + tJson.imei);
                if (user != undefined) {
                    user.userData['isInPoly'] = tJson.isInPoly;
                    user.userData['datetime'] = tJson.datetime;
                    user.visible = tJson.isInPoly;
                    if (tJson.isInPoly) {
                        $("#userLabelDiv-" + tJson.imei).show(1000);
                        new TWEEN.Tween(user.position).to({
                            x: tJson.x, y: 0, z: tJson.y
                        }, 800).start();
                    } else {
                        new TWEEN.Tween(user.position).to({
                            x: tJson.x, y: 0, z: tJson.y
                        }, 800).start();
                        $("#userLabelDiv-" + tJson.imei).hide(1000);
                    }
                }
                userPosition.getInPolyNum();
            }
        } else if (topic === '$forallcn/iotcp/position/alart') {

        } else if (topic === '$forallcn/iotcp/position/sessionSize') {
            if (msg.length > 0) {
                console.log(msg);
                userPosition.getInPolyNum();
            }
        }
    },

    getInPolyNum: function () {
        let num = 0;
        $('.userLabelDiv').each(function (index, item) {
            let imei = item.id.split('-')[1];
            let label = scene.getObjectByName('userLabel-' + imei);
            let datetime = 0;
            if (label != undefined) {
                datetime = label.userData.datetime;
            }
            // 超过10分钟不更新数据的隐藏
            if (new Date().getTime() - datetime > 1000 * 60 * 10) {
                $(item).hide(1000);
                label.visible = false;
                label.userData.isInPoly = false;
            } else if ($(item).css('display') === 'block') {
                // 显示累计
                num++;
            }
            if ((index + 1) >= $('.userLabelDiv').length) {
                document.getElementById('userNum').innerText = num;
            }
        });
    },

    //颜色取反方法
    colorReverse: function (OldColorValue) {
        var OldColorValue = '0x' + OldColorValue.replace(/#/g, "");
        var str = '000000' + (0xFFFFFF - OldColorValue).toString(16);
        return '#' + str.substring(str.length - 6, str.length);
    }
}