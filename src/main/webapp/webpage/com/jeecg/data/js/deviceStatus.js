/*
 * #deviceStatus {
	z-index: 9999;
	position: absolute;
	width: 240px;
	height: 200px;
	color: rgba(127, 255, 255, 1);
	background-color: rgba(26, 47, 79, 0.75);
	border: 2px solid rgba(127, 255, 255, 0.75);
	border-radius: 10px;
	text-align: center;
	position: absolute;
	bottom: 0px;
	right: 0px;
}

 * */

var deviceStatus = {
    bottom: '0px',
    right: '0px',
    width: '240px',
    height: '200px',
    isValid: false,

    create: function (sceneId) {
        var statusDiv = document.createElement('div');
        statusDiv.id = "deviceStatus";
        statusDiv.style.position = 'absolute';
        statusDiv.style.bottom = this.bottom;
        statusDiv.style.right = this.right;
        statusDiv.style.width = this.width;
        statusDiv.style.height = this.height;
        statusDiv.style['text-align'] = 'center';
        statusDiv.style.border = '1px solid rgba(127, 255, 255, 0.75)';
        statusDiv.style.background = 'rgba(26, 47, 79, 0.5)';
        document.body.appendChild(statusDiv);

        var chartDiv = document.createElement('div');
        chartDiv.style.cssText = 'width:100%;height:100%';
        chartDiv.id = "statusChart";
        statusDiv.appendChild(chartDiv);

        var chart = echarts.init(chartDiv);
        var option = {
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b}: {c} ({d}%)"
            },
            series: [{
                name: '设备情况',
                type: 'pie',
                radius: [0, '30%'],
                label: {
                    normal: {
                        position: 'inner'
                    }
                },
                data: [{
                    value: 98,
                    name: '正常'
                }, {
                    value: 2,
                    name: '离线'
                }]
            }, {
                name: '设备数据',
                type: 'pie',
                radius: ['40%', '55%'],
                data: [{
                    value: 135,
                    name: '正常'
                }, {
                    value: 108,
                    name: '警告'
                }, {
                    value: 251,
                    name: '错误'
                }]
            }]
        };
        chart.setOption(option);
        this.display(this.isValid);
    },

    display: function (isValid) {
        if (isValid) {
            $('#deviceStatus').show();
        } else {
            $('#deviceStatus').hide();
        }
    }
}