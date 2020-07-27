<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="plug-in/LipZoomMark/example/style.css">
<link rel="stylesheet" type="text/css" href="plug-in/ace/css/font-awesome.css">
<t:base type="jquery"></t:base>
<script src="plug-in/vue/vue.js"></script>
<script src="plug-in/layer/layer.js"></script>
<link rel="stylesheet" type="text/css" href="plug-in/layer/skin/layer.css">

<style type="text/css">
.delbtn {
	color: #eee;
}

.delbtn :hover {
	color: #555;
	cursor: pointer;
}

[v-cloak] {
	display: none;
}

a.lipButton{
	width: 35px;
	height: 35px;
	border: 1px solid #aaa;
	color: #aaa;
	background-color: rgba(5, 5, 5, 0.6);
	border-radius: 8px;
}
</style>
</head>
<body>
	<div class="panel" id="panel">
		<div id="container" style="border: 1px solid #aaa;">
			<img id="zoomImg" src="plug-in/LipZoomMark/example/assets/demo.jpg">
		</div>
		<div class="colorSelect">
			<!-- <button id="reset" class="lipButton">重置</button>
			<button id="rotate" class="lipButton">旋转</button>
			<button id="save" class="lipButton">保存</button> -->
			<a class="fa fa-reply fa-2x lipButton" href="javascript:void(0);" title="重置" style="display: inline-block;" id="reset"></a>
			<a class="fa fa-repeat fa-2x lipButton" href="javascript:void(0);" title="顺时针旋转" style="display: inline-block;" id="rotate"></a>
			<a class="fa fa-save fa-2x lipButton" href="javascript:void(0);" title="保存" style="display: inline-block;" id="save"></a>
			<div style="padding: 10px;">
				<label for='blue' style="background-color: blue; color: white; width: 100%;">
					<input type="radio" id="blue" name="markColor" class="radioItem" markColor='blue' checked="checked" />
				</label>
				<label for='red' style="background-color: red; color: white; width: 100%;">
					<input type="radio" id="red" name="markColor" class="radioItem" markColor='red' />
				</label>
				<label for='yellow' style="background-color: green; color: white; width: 100%;">
					<input type="radio" id="yellow" name="markColor" class="radioItem" markColor='green' />
				</label>
			</div>
			<div id="naturalDimensions"></div>
		</div>
		<div class="content">
			<form id="markForm" action="">
				<table id='marksTable' class="table table-bordered table-striped dataTable">
					<thead>
						<tr>
							<th width="50">序号</th>
							<th width="100">位置</th>
							<th width="100">设备</th>
							<th width="50">操作</th>
						</tr>
					</thead>
					<tbody id="tbody">
						<tr v-for="(item,index) in items" v-cloak>
							<td>
								<input name="color" hidden="hidden" v-model="item.color"/>
								<span class="colorSpan" style="background-color: ${item.color}">{{index+1}}</span>
							</td>
							<td>
								<input name="markx" style="width: 40px;display: inline;" v-model="item.x" />
								<input name="marky" style="width: 40px;display: inline;" v-model="item.y" />
							</td>
							<td>
								<input name="deviceName" style="width: 100px" />
							</td>
							<td>
								<a class="fa fa-minus-square delbtn" href="javascript:void(0);" title="删除" style="display: inline-block;" v-on:click="del(index,item.id)"></a>
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
	</div>

	<script type="text/javascript" src="plug-in/LipZoomMark/src/lip-zoom-mark-jquery.js"></script>
	<script type="text/javascript">
		var angle = 0;

		function getImgNaturalDimensions(oImg, callback) {
			var nWidth, nHeight;
			if (!oImg.naturalWidth) { // 现代浏览器
				nWidth = oImg.naturalWidth;
				nHeight = oImg.naturalHeight;
				callback({w: nWidth, h:nHeight});
			} else { // IE6/7/8
				var nImg = new Image();
				nImg.onload = function() {
					var nWidth = nImg.width,
					nHeight = nImg.height;
					callback({w: nWidth, h:nHeight});
				}
				nImg.src = oImg.src;
			}
		}

		var img = document.getElementById("zoomImg");
		getImgNaturalDimensions(img, function(dimensions){
			document.getElementById("naturalDimensions").innerHTML = dimensions.w +", "+ dimensions.h;
		});
		
		// div的尺寸
		var panelW = 0, panelH = 0;
		var panel = document.getElementById("panel");
		panelW = panel.offsetWidth;
		panelH = panel.offsetHeight;
		console.log("panel的尺寸: "+panelW +", "+ panelH);
		var vue = new Vue({
			el : '#tbody',
			data : {
				items : [],
			},
			methods : {
				add : function(data) {
					this.items.push(data);
				},
				del : function(index, id) {
					if (index == -2) {
						this.items = "";
					}
					{
						this.items.splice(index, 1);
						deleteMark(id);
					}
				},
				submit: function() {
					console.log(this.items);
				}
			}
		});

		$('#container').ZoomMark({
			'markColor' : 'blue',
			'afterMark' : addRowToTabel,
			'showMarkMsg' : showMarkMsg
		});
		
/* 		var data = $('#container').ZoomMark('getData');
		console.log(JSON.stringify(data)); */
		
		$(function() {
			$('.radioItem').change(function() {
				$('#container').ZoomMark('changeSettings', {
					'markColor' : $('.colorSelect').find('input:checked').attr('markColor')
				});
			});
			$("#reset").click(function() {
				$('#container').ZoomMark('reset');
			});
			$("#rotate").click(function() {
				angle = angle + 90 >= 360 ? 0 : angle + 90;
				$('#container').ZoomMark('rotate', angle);
			});
			$("#save").click(function() {
				$('#container').ZoomMark('reset');
				setTimeout(function() {
					layer.confirm('您确定要保存场景设备信息吗？', {
						btn : [ '确定', '取消' ]
					//按钮
					}, function() {
						layer.msg('正在保存...', {
							time : 2000,//2秒自动关闭
							icon : 1
						});
						saveForm();
					}, function() {
					});
				}, 200);

			});
		});

		function addRowToTabel(marks) {
			vue.add(marks[marks.length - 1]);
		}
		
		function showMarkMsg(e){
			console.log(e);
			var w = 340;
			var h = 215;
			var x = e.clientX;
			var y = e.clientY;
			if(panelW-x < w){
				x = x-w;
			}
			if(panelH-y < h){
				y = y-h;
			}
			layer.msg('你确定你很帅么？', {
				time : 0, //不自动关闭
				area: [w+'px', h+'px'],
				offset: [y+'px',x+'px'],
				btn : [ '确定' ],
				yes : function(index) {
					layer.close(index);
				}
			});
		}

		function deleteMark(id) {
			$('#container').ZoomMark('deleteMark', id);
			$('#row_' + id).remove();
			updateTableId();
		}

		function updateTableId() {
			$('#marksTable').find('tbody').find('.colorSpan').each(function(index, value) {
				$(this).html(index + 1);
			})
		}

		function saveForm() {
			var formArray = new Array()
			var data = $("tbody tr").each(function() {
				var formData = new Object();
				$(this).find("input").each(function() {
					if (this.name != "") {
						if (this.name == "markx") {
							formData[this.name] = $(this).val() / panelH;
						} else if (this.name == "marky") {
							formData[this.name] = $(this).val() / panelW;
						} else {
							formData[this.name] = $(this).val() || "";
						}
					}
				});
				formArray.push(formData);
			});
			console.log(JSON.stringify(formArray));
			return false;
			$.ajax({
				method : 'POST',
				dataType : "json",
				url : localStorage.getItem("basePath") + "/rest/overtimeController",
				data : formData,
				error : function(xhr) {
					if (xhr.status == 401) {
						layer.msg('提交失败...', {
							time : 2000,//2秒自动关闭
							icon : 2
						});
					}
				},
				success : function(result) {
					if (result.ok == true) {
						layer.msg('保存成功...', {
							time : 2000,//2秒自动关闭
							icon : 1
						});
					} else {
						layer.msg('保存是被...', {
							time : 2000,//2秒自动关闭
							icon : 2
						});
					}
				}
			});
		}
	</script>

</body>
</html>