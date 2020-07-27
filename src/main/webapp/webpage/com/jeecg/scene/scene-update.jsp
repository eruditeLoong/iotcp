<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>场景管理</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <t:base type="bootstrap,layer,validform,webuploader,bootstrap-form"></t:base>
    <link rel="stylesheet" href="plug-in/ace/css/font-awesome.css" type="text/css"></link>

    <script type="text/javascript" src="https://webapi.amap.com/maps?v=1.4.15&key=02320f02a21e212cadda66c6cf3105c7&plugin=AMap.Geocoder"></script>
    <link rel="stylesheet" href="plug-in/easyui/themes/metrole/easyui.css" type="text/css"></link>
    <script type="text/javascript" src="plug-in/easyui/jquery.easyui.min.1.3.2.js"></script>

    <link rel="stylesheet" href="plug-in/bootstrap3.3.5/css/bootstrap-switch.min.css" type="text/css"></link>
    <script type="text/javascript" src="plug-in/bootstrap3.3.5/js/bootstrap-switch.min.js"></script>
    <style type="text/css">
        legend > h5 {
            color: #18A688;
        }

        .bt-label {
            color: #5AC0DD;
        }

        .BMap_cpyCtrl {
            display: none;
        }

        /* 去掉地图那行，添加CSS即可 */
        .anchorBL {
            display: none;
        }

        #container {
            border: 1px solid #18A688;
            width: 100%;
            height: 100%;
            margin: 0px;
            padding: 0px;
        }

        .info {
            padding: .75rem 1.25rem;
            margin-bottom: 1rem;
            border-radius: .25rem;
            position: fixed;
            top: 1rem;
            background-color: rgba(169, 215, 255, .5);
            width: 26rem;
            min-width: 22rem;
            border-width: 0;
            right: 1rem;
            box-shadow: 0 2px 6px 0 rgba(114, 124, 245, .5);
        }

        #panel {
            position: absolute;
            background-color: white;
            max-height: 90%;
            overflow-y: auto;
            top: 10px;
            right: 10px;
            width: 280px;
        }
    </style>
</head>
<body>
<div class="easyui-layout" fit="true">
    <div region="west" split="true" style="width: 500px;">
        <form class="form-horizontal" role="form" id="formobj" action="sceneController.do?doUpdate" method="POST">
            <input type="hidden" id="btn_sub" class="btn_sub"/>
            <input type="hidden" id="id" name="id" value="${scene.id}"/>
            <legend>
                <h5>用户信息</h5>
            </legend>
            <div class="bt-item col-md-12 col-sm-12">
                <div class="row">
                    <div class="col-md-3 col-sm-3 col-xs-3 bt-label">创建人：</div>
                    <div class="col-md-9 col-sm-9 col-xs-9 bt-content">
                        <input id="createName" name="createName" value='${fns:getUserRealnameByUsername(scene.createBy) }' type="text" readonly="readonly"
                               class="form-control input-sm"/>
                        <input id="createBy" name="createBy" value='${scene.createBy}' type="hidden" readonly="readonly"/>
                    </div>
                </div>
            </div>
            <div class="bt-item col-md-12 col-sm-12">
                <div class="row">
                    <div class="col-md-3 col-sm-3 col-xs-3 bt-label">创建日期：</div>
                    <div class="col-md-9 col-sm-9 col-xs-9 bt-content">
                        <input id="createDate" name="createDate" type="text" class="form-control input-sm laydate-datetime" readonly="readonly"
                               value="<fmt:formatDate pattern='yyyy-MM-dd HH:mm:ss' type='both' value='${scene.createDate}'/>" datatype="*" ignore="checked"/>
                    </div>
                </div>
            </div>
            <c:if test="${not empty scene.updateBy}">
                <div class="bt-item col-md-12 col-sm-12">
                    <div class="row">
                        <div class="col-md-3 col-sm-3 col-xs-3 bt-label">更新人：</div>
                        <div class="col-md-9 col-sm-9 col-xs-9 bt-content">
                            <input id="updateName" name="updateName" value='${fns:getUserRealnameByUsername(scene.updateBy) }' type="text" readonly="readonly"
                                   class="form-control input-sm"/>
                            <input id="updateBy" name="updateBy" value='${scene.updateBy}' type="hidden" readonly="readonly"/>
                        </div>
                    </div>
                </div>
                <div class="bt-item col-md-12 col-sm-12">
                    <div class="row">
                        <div class="col-md-3 col-sm-3 col-xs-3 bt-label">更新日期：</div>
                        <div class="col-md-9 col-sm-9 col-xs-9 bt-content">
                            <input id="updateDate" name="updateDate" type="text" class="form-control input-sm laydate-datetime" readonly="readonly"
                                   value="<fmt:formatDate pattern='yyyy-MM-dd HH:mm:ss' type='both' value='${scene.updateDate}'/>" ignore="ignore"/>
                        </div>
                    </div>
                </div>
            </c:if>
            <legend>
                <h5>场景信息</h5>
            </legend>
            <div class="bt-item col-md-12 col-sm-12">
                <div class="row">
                    <div class="col-md-3 col-sm-3 col-xs-3 bt-label">名称：</div>
                    <div class="col-md-9 col-sm-9 col-xs-9 bt-content">
                        <input name="name" type="text" class="form-control input-sm" maxlength="32" value="${scene.name}" datatype="*" ignore="checked"/>
                    </div>
                </div>
            </div>
            <div class="bt-item col-md-12 col-sm-12">
                <div class="row">
                    <div class="col-md-3 col-sm-3 col-xs-3 bt-label">编码：</div>
                    <div class="col-md-9 col-sm-9 col-xs-9 bt-content">
                        <input name="code" type="text" class="form-control input-sm" maxlength="32" value="${scene.code}" datatype="*" ignore="checked"/>
                    </div>
                </div>
            </div>
            <div class="bt-item col-md-12 col-sm-12">
                <div class="row">
                    <div class="col-md-3 col-sm-3 col-xs-3 bt-label" title="模型缩放比例，如1:100填写100）">比例：</div>
                    <div class="col-md-9 col-sm-9 col-xs-9 bt-content">
                        <input name="scale" type="text" class="form-control input-sm" value="${scene.scale}" maxlength="32" datatype="n" ignore="checked"/>
                    </div>
                </div>
            </div>
            <%-- <div class="bt-item col-md-12 col-sm-12">
                <div class="row">
                    <div class="col-md-3 col-sm-3 col-xs-3 bt-label">展示类型：</div>
                    <div class="col-md-9 col-sm-9 col-xs-9 bt-content">
                        <div style="padding-top: 5px">
                            <t:dictSelect id="showType" field="showType" defaultVal="${scene.showType}" extendJson="{class:'i-checks'}" type="checkbox" hasLabel="false" title="展示类型"
                                typeGroupCode="showType"></t:dictSelect>
                        </div>
                    </div>
                </div>
            </div>
            <div class="bt-item col-md-12 col-sm-12" id="scene2d-div">
                <div class="row">
                    <div class="col-md-3 col-sm-3 col-xs-3 bt-label">2D场景：</div>
                    <div class="col-md-9 col-sm-9 col-xs-9 bt-content">
                        <t:webUploader name="scene2d" outJs="true" auto="false" fileNumLimit="1" showImgDiv="filediv_scene2d" pathValues="${scene.scene2d}" fileSingleSizeLimit="102400" datatype=""></t:webUploader>
                        <div class="form" id="filediv_scene2d"></div>
                    </div>
                </div>
            </div> --%>
            <input name="showType" type="hidden" value="3D"/>
            <input name="scene2d" type="hidden" value="${scene.scene2d}"/>
            <div class="bt-item col-md-12 col-sm-12" id="scene3d-div">
                <div class="row">
                    <div class="col-md-3 col-sm-3 col-xs-3 bt-label">3D场景：</div>
                    <div class="col-md-9 col-sm-9 col-xs-9 bt-content">
                        <t:webUploader name="scene3d" outJs="true" auto="false" fileNumLimit="10" showImgDiv="filediv_scene3d" pathValues="${scene.scene3d}"
                                       fileSingleSizeLimit="209715200"
                                       datatype="*"></t:webUploader>
                        <div class="form" id="filediv_scene3d"></div>
                    </div>
                </div>
            </div>
            <div class="bt-item col-md-12 col-sm-12">
                <div class="row">
                    <div class="col-md-3 col-sm-3 col-xs-3 bt-label">位置：</div>
                    <div class="col-md-9 col-sm-9 col-xs-9 bt-content">
                        <input id="address" name="address" type="text" readonly="readonly" class="form-control input-sm" maxlength="50" value="${scene.address}"
                               ignore="ignore"/>
                    </div>
                </div>
            </div>
            <div class="bt-item col-md-12 col-sm-12">
                <div class="row">
                    <div class="col-md-3 col-sm-3 col-xs-3 bt-label">坐标：</div>
                    <div class="col-md-9 col-sm-9 col-xs-9 bt-content">
                        <input id="position" name="position" type="text" class="form-control" maxlength="32" value="${scene.position}" ignore="ignore"/>
                    </div>
                </div>
            </div>
            <div class="bt-item col-md-12 col-sm-12">
                <div class="row">
                    <div class="col-md-3 col-sm-3 col-xs-3 bt-label">状态：</div>
                    <div class="col-md-9 col-sm-9 col-xs-9 bt-content">
                        <div style="padding-top: 5px">
                            <t:dictSelect field="status" defaultVal="${scene.status}" extendJson="{class:'i-checks'}" type="radio" hasLabel="false" title="状态"
                                          typeGroupCode="isValid"></t:dictSelect>
                        </div>
                    </div>
                </div>
            </div>
            <div class="bt-item col-md-12 col-sm-12">
                <div class="row">
                    <div class="col-md-3 col-sm-3 col-xs-3 bt-label">备注：</div>
                    <div class="col-md-9 col-sm-9 col-xs-9 bt-content">
                        <textarea name="remark" class="form-control input-sm" rows="6" ignore="ignore">${scene.remark}</textarea>
                        <span class="Validform_checktip" style="float: left; height: 0px;"></span>
                        <label class="Validform_label" style="display: none">备注</label>
                    </div>
                </div>
            </div>
            <legend>
                <h5>
                    配置信息&nbsp;
                    <i class="fa fa-question-circle" style="cursor: pointer;" title="3D展示页面和部署页面的默认配置信息。"></i>
                </h5>
            </legend>
            <div class="bt-item col-md-4 col-sm-4">
                <div class="row">
                    <div class="col-md-9 col-sm-9 col-xs-9 bt-label">自动旋转：</div>
                    <div class="col-md-3 col-sm-3 col-xs-3 bt-content">
                        <div style="padding-top: 5px">
                            <input id="isAutoRotate" name="isAutoRotate" type="checkbox" ${scene.isAutoRotate?'checked':'' } />
                        </div>
                    </div>
                </div>
            </div>
            <div class="bt-item col-md-4 col-sm-4">
                <div class="row">
                    <div class="col-md-9 col-sm-9 col-xs-9 bt-label">设备关系：</div>
                    <div class="col-md-3 col-sm-3 col-xs-3 bt-content">
                        <div style="padding-top: 5px">
                            <input id="isDeviceRelation" name="isDeviceRelation" type="checkbox" ${scene.isDeviceRelation?'checked':'' } />
                        </div>
                    </div>
                </div>
            </div>
            <div class="bt-item col-md-4 col-sm-4">
                <div class="row">
                    <div class="col-md-9 col-sm-9 col-xs-9 bt-label">设备列表：</div>
                    <div class="col-md-3 col-sm-3 col-xs-3 bt-content">
                        <div style="padding-top: 5px">
                            <input id="isDevicePanel" name="isDevicePanel" type="checkbox" ${scene.isDevicePanel?'checked':'' } />
                        </div>
                    </div>
                </div>
            </div>
            <div class="bt-item col-md-4 col-sm-4">
                <div class="row">
                    <div class="col-md-9 col-sm-9 col-xs-9 bt-label">显示标签：</div>
                    <div class="col-md-3 col-sm-3 col-xs-3 bt-content">
                        <div style="padding-top: 5px">
                            <input id="isLabelDisplay" name="isLabelDisplay" type="checkbox" ${scene.isLabelDisplay?'checked':'' } />
                        </div>
                    </div>
                </div>
            </div>
            <div class="bt-item col-md-4 col-sm-4">
                <div class="row">
                    <div class="col-md-9 col-sm-9 col-xs-9 bt-label">性能控件：</div>
                    <div class="col-md-3 col-sm-3 col-xs-3 bt-content">
                        <div style="padding-top: 5px">
                            <input id="isStatsDisplay" name="isStatsDisplay" type="checkbox" ${scene.isStatsDisplay?'checked':'' } />
                        </div>
                    </div>
                </div>
            </div>
            <div class="bt-item col-md-4 col-sm-4">
                <div class="row">
                    <div class="col-md-9 col-sm-9 col-xs-9 bt-label">设备统计：</div>
                    <div class="col-md-3 col-sm-3 col-xs-3 bt-content">
                        <div style="padding-top: 5px">
                            <input id="isDeviceStatus" name="isDeviceStatus" type="checkbox" ${scene.isDeviceStatus?'checked':'' } />
                        </div>
                    </div>
                </div>
            </div>
            <div class="bt-item col-md-4 col-sm-4">
                <div class="row">
                    <div class="col-md-9 col-sm-9 col-xs-9 bt-label">显示阴影：</div>
                    <div class="col-md-3 col-sm-3 col-xs-3 bt-content">
                        <div style="padding-top: 5px">
                            <input id="isShadowDisplay" name="isShadowDisplay" type="checkbox" ${scene.isShadowDisplay?'checked':'' } />
                        </div>
                    </div>
                </div>
            </div>
            <div class="bt-item col-md-4 col-sm-4">
                <div class="row">
                    <div class="col-md-9 col-sm-9 col-xs-9 bt-label">人员定位：</div>
                    <div class="col-md-3 col-sm-3 col-xs-3 bt-content">
                        <div style="padding-top: 5px">
                            <input id="isUserPosition" name="isUserPosition" type="checkbox" ${scene.isUserPosition?'checked':'' } />
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
    <div region="center">
        <div id="container"></div>
        <div class="info">
            <div class="col-md-9">
                <input id="where" name="where" type="text" class="form-control input-sm">
            </div>
            <div class="col-md-3">
                <a href="javascript:;" class="ace_button" style="line-height: 30px;" onClick="javascript:search();">搜 索</a>
                <script>
                    //关键字查询
                    function search() {
                        var addr = document.getElementById('where').value;
                        placeSearch.search(addr);
                    }
                </script>
            </div>
        </div>
        <div id="panel"></div>
    </div>
</div>
<script type="text/javascript">
    var subDlgIndex = '';
    $(document).ready(function () {
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
                var win = frameElement.api.opener;
                if (data.success == true) {
                    frameElement.api.close();
                    win.reloadTable();
                    win.tip(data.msg);
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
</script>
<script type="text/javascript">
    var map = new AMap.Map("container", {
        resizeEnable: true, //是否监控地图容器尺寸变化
        zoom: 99, //初始地图级别
        showIndoorMap: false //关闭室内地图
    });
    var geocoder = new AMap.Geocoder({
        city: "010", //城市设为北京，默认：“全国”
        radius: 1000 //范围，默认：500
    });
    var marker = new AMap.Marker();

    function regeoCode() {
        var lnglat = document.getElementById('position').value.split(',');
        map.add(marker);
        marker.setPosition(lnglat);
        map.setCenter(lnglat); //设置地图中心点
        geocoder.getAddress(lnglat, function (status, result) {
            if (status === 'complete' && result.regeocode) {
                var address = result.regeocode.formattedAddress;
                document.getElementById('address').value = address;
            } else {
                document.getElementById('address').value = '根据经纬度查询地址失败';
                log.error('根据经纬度查询地址失败')
            }
        });
    }

    map.on('click', function (e) {
        document.getElementById('position').value = e.lnglat;
        regeoCode();
    });
    regeoCode();
</script>
</body>
</html>