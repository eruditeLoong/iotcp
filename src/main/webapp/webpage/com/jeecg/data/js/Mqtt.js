var Mqtt = function (clientId, subscribe) {
    this.clientId = clientId;
    this.subscribe = subscribe;
}

Mqtt.prototype = {
    init: function () {
        var client = new Paho.MQTT.Client("127.0.0.1", Number(8083), this.clientId); //建立客户端实例
        client.connect({
            userName: "admin",
            password: "public",
            // reconnect : true,         // Enable automatic reconnect
            // reconnectInterval: 10,
            onSuccess: function () {
                console.log("mqtt.js连接成功。。");
                this.client.subscribe(this.subscribe); //订阅主题:实时数据数据
            }
        });
        client.onConnectionLost = this.connectionLost;
        return client;
    },

    // 连接断开处理事件
    connectionLost: function (responseObject) {
        if (responseObject.errorCode !== 0) {
            console.log("连接已断开");
            /* 连接断开后，定时重新连接 */
            // var timer = window.setTimeout(this.init(), 3000); //1000为1秒钟
            // window.clearTimeout(timer);
        }
    },

    // 消息接收处理事件
    messageArrived: function (message) {
        var topic = message.destinationName;
        var qos = message.qos;
        if (message.payloadString.length > 0) {
            var msg = JSON.parse(message.payloadString);
        }
    },
}
