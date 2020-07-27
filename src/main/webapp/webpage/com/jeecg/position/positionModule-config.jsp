<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<t:base type="layer,validform,bootstrap-form"></t:base>

<style type="text/css" scoped="scoped">
    .form-horizontal {
        display: flex;
        flex-direction: column;
        flex: auto;
    }

    .conf-title {
        width: 70px;
    }

    .conf-info {
        flex: 1;
        margin: 5px;
        font-size: smaller;
        color: #909090;
        border-bottom: 1px solid #eee;
    }

    .conf-group {
        display: flex;
        flex-direction: row;
        flex: auto;
        padding: 5px 10px;
        border-bottom: 1px solid #eee;
    }
</style>
<form class="form-horizontal" role="form" id="formobj"
      action="positionModuleConfigController.do?${empty positionModuleConfig.id?'doAdd':'doUpdate'}" method="POST">
    <div id="confData">
        <input type="hidden" id="id" name="id" value="${positionModuleConfig.id}"/>
        <div class="conf-group">
            <div class="conf-title">IMEI：</div>
            <div class="conf-value">
                <input id="imei" name="imei" type="text" class="form-control" maxlength="36"
                       value="${positionModuleConfig.imei}" datatype="*" readonly ignore="checked"/>
            </div>
        </div>
        <div class="conf-group">
            <div class="conf-title">定位模式：</div>
            <div class="conf-value">
                <input id="positionMode" name="positionMode" type="hidden"
                       value="${positionModuleConfig.positionMode}"/>
                <input id="isGps" name="isGps" type="checkbox"/>
                GPS
                <input id="isWifi" name="isWifi" type="checkbox"/>
                WIFI
                <input id="isLbs" name="isLbs" type="checkbox"/>
                LBS
            </div>
        </div>
        <div class="conf-group">
            <div class="conf-title">飘逸机制：</div>
            <div class="conf-value">
                <t:dictSelect field="driftMode" type="radio" extendJson="{class:'i-checks'}" typeGroupCode="switchStat" hasLabel="false"
							  defaultVal="${positionModuleConfig.driftMode}" title="飘逸机制"></t:dictSelect>
            </div>
        </div>
        <div class="conf-info">
            飘逸机制，提示：定位太接近和mac地址相似不上传定位信息
        </div>
        <div class="conf-group">
            <div class="conf-title" title="定位数据上传间隔(秒：s)">上传间隔：</div>
            <div class="conf-value">
                <input id="autoLocalTime" name="autoLocalTime" type="text" class="form-control" maxlength="5"
                       value="${positionModuleConfig.autoLocalTime}" datatype="n" ignore="checked"/>
            </div>
        </div>
        <div class="conf-info">
            上传间隔，提示：设置时间越短，位置实时更新越快，轨迹越连贯，同时电量和流量消耗越快，建议不同的需求设置不同的时间，时间为0时停止上传数据。单位：秒（s）
        </div>
        <div class="conf-group">
            <div class="conf-title" title="eg:18:00-07:00">休眠时段：</div>
            <div class="conf-value">
                <input id="sleepTime" name="sleepTime" type="text" class="form-control" maxlength="32"
                       value="${positionModuleConfig.sleepTime}" ignore="ignore"/>
                <input id="isSleep" name="isSleep" type="checkbox" title="使能休眠" ${positionModuleConfig.isSleep?'checked':'' } />
            </div>
        </div>
        <div class="conf-info">休眠时段，提示：选择的时间段为休眠状态，在此时间段内设备将停止自动定位数据上传，但手动定位依然能正常获取到位置。</div>
        <div class="conf-group">
            <div class="conf-title">报警设置：</div>
            <div class="conf-value">
                <div style="padding: 5px" class="conf-alarm-group">
                    <input id="isShockAlarm" name="isShockAlarm" type="checkbox"
                    ${positionModuleConfig.isShockAlarm?'checked':'' } />
                    震动报警
                    <br>
                    <input id="isMoveAlarm" name="isMoveAlarm" type="checkbox"
                    ${positionModuleConfig.isMoveAlarm?'checked':'' } />
                    位移报警
                    <br>
                    <input id="isLowPowerAlarm" name="isLowPowerAlarm" type="checkbox"
                    ${positionModuleConfig.isLowPowerAlarm?'checked':'' } />
                    低电报警
                    <br>
                    <input id="isFenceAlarm" name="isFenceAlarm" type="checkbox"
                    ${positionModuleConfig.isFenceAlarm?'checked':'' } />
                    围栏报警
                    <br>
                    <input id="isSosAlarm" name="isSosAlarm" type="checkbox"
                    ${positionModuleConfig.isSosAlarm?'checked':'' } />
                    SOS报警
                    <br>
                    <input id="isOutTownAlarm" name="isOutTownAlarm" type="checkbox"
                    ${positionModuleConfig.isOutTownAlarm?'checked':'' } />
                    出省报警
                    <br>
                    <input id="isSeparationAlarm" name="isSeparationAlarm" type="checkbox"
                    ${positionModuleConfig.isSeparationAlarm?'checked':'' } />
                    分离报警
                    <br>
                    <input id="isRiskAlarm" name="isRiskAlarm" type="checkbox"
                    ${positionModuleConfig.isRiskAlarm?'checked':'' } />
                    风险报警
                    <br>
                </div>
            </div>
        </div>
        <div class="conf-group">
            <t:dictSelect field="restart" type="radio" extendJson="{class:'i-checks'}" typeGroupCode="restart" defaultVal="1" hasLabel="false"
                          title="重启选择"></t:dictSelect>
        </div>
        <div class="conf-group">
            <a id="btn_sub" class="btn_sub ace_button" href="javascript:;">
                保存并重启
            </a>&nbsp;
        </div>
    </div>
</form>
<script type="text/javascript">
    var subDlgIndex = '';

    function setPositionMode() {
        var pMode = $('#positionMode').val() | 0;
        (pMode & 0x01) === 1 ? $('#isGps').attr('checked', true) : $('#isGps').attr('checked', false);
        (pMode & 0x02) === 2 ? $('#isWifi').attr('checked', true) : $('#isWifi').attr('checked', false);
        (pMode & 0x04) === 4 ? $('#isLbs').attr('checked', true) : $('#isLbs').attr('checked', false);
    }

    $(document).ready(function () {
        setPositionMode();
        //单选框/多选框初始化
        $('.i-checks').iCheck({
            labelHover: false,
            cursor: true,
            checkboxClass: 'icheckbox_square-green',
            radioClass: 'iradio_square-green',
            increaseArea: '20%'
        });
        //表单提交
        $("#formobj").Validform({
            tiptype: function (msg, o, cssctl) {
                if (o.type == 3) {
                    validationMessage(o.obj, msg);
                } else {
                    removeMessage(o.obj);
                }
            },
            btnSubmit: "#btn_sub",
            btnReset: "#btn_reset",
            ajaxPost: true,
            beforeSubmit: function (curform) {
                var pMode = 0;
                $('#isGps').prop('checked') ? pMode += 1 : '';
                $('#isWifi').prop('checked') ? pMode += 2 : '';
                $('#isLbs').prop('checked') ? pMode += 4 : '';
                document.getElementById('positionMode').value = pMode;

                if ($('#isSleep').prop('checked') && $('#sleepTime').val() == "") {
                    alert('请填写休眠时间段！');
                    $('#sleepTime').focus();
                    return false;
                }
                if ($('#isShock').prop('checked') && $('#shockLevel').val() == "") {
                    alert('请填写震动等级！');
                    $('#shockLevel').focus();
                    return false;
                }
            },
            usePlugin: {
                passwordstrength: {
                    minLen: 6,
                    maxLen: 18,
                    trigger: function (obj, error) {
                        if (error) {
                            obj.parent().next().find(".Validform_checktip").show();
                            obj.find(".passwordStrength").hide();
                        } else {
                            $(".passwordStrength").show();
                            obj.parent().next().find(".Validform_checktip").hide();
                        }
                    }
                }
            },
            callback: function (data) {
                if (data.success == true) {
                    $.messager.alert('提示', data.msg);
                } else {
                    if (data.responseText == '' || data.responseText == undefined) {
                        $.messager.alert('错误', data.msg);
                        $.Hidemsg();
                    } else {
                        try {
                            var emsg = data.responseText.substring(data.responseText.indexOf('错误描述'), data.responseText.indexOf('错误信息'));
                            $.messager.alert('错误', emsg);
                            $.Hidemsg();
                        } catch (ex) {
                            $.messager.alert('错误', data.responseText + "");
                            $.Hidemsg();
                        }
                    }
                    return false;
                }
            }
        });
    });

    function saveConf() {
        let objs = {positionMode: 0, updateDate: ''};
        objs.updateDate = new Date().Format('yyyy-MM-dd hh:mm:ss');
        let doms = $('#confData').find(':input');
        for (const i in doms) {
            let dom = doms[i];
            if (dom.type === 'text') {
                objs[dom.name] = $(dom).val();
            } else if (dom.type === 'checkbox') {
                if (dom.name === 'isGps') {
                    objs.positionMode |= $(dom).prop('checked') ? 1 : 0;
                } else if (dom.name === 'isWifi') {
                    objs.positionMode |= $(dom).prop('checked') ? 2 : 0;
                } else if (dom.name === 'isLbs') {
                    objs.positionMode |= $(dom).prop('checked') ? 4 : 0;
                } else {
                    objs[dom.name] = $(dom).prop('checked') ? 1 : 0;
                }
            }
        }
        console.log(objs);
        const url = "positionModuleConfigController.do?${positionModuleConfig.id?'doUpdate':'doAdd'}";
        console.log(url);
        $.post(url, objs, res => {
            console.log(res);
        });
    }
</script>
<script>
    //Date的prototype 属性可以向对象添加属性和方法。
    Date.prototype.Format = function (fmt) {
        var o = {
            "M+": this.getMonth() + 1,
            "d+": this.getDate(),
            "H+": this.getHours(),
            "m+": this.getMinutes(),
            "s+": this.getSeconds(),
            "S+": this.getMilliseconds()
        };
        //因为date.getFullYear()出来的结果是number类型的,所以为了让结果变成字符串型，下面有两种方法：
        if (/(y+)/.test(fmt)) {
            //第一种：利用字符串连接符“+”给date.getFullYear()+""，加一个空字符串便可以将number类型转换成字符串。
            fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        }
        for (var k in o) {
            if (new RegExp("(" + k + ")").test(fmt)) {
                //第二种：使用String()类型进行强制数据类型转换String(date.getFullYear())，这种更容易理解。
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(String(o[k]).length)));
            }
        }
        return fmt;
    };
</script>