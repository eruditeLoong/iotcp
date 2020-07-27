<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<title>显示数据格式</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">
<t:base type="bootstrap,layer,bootstrap-table,jquery"></t:base>
<style type="text/css">
#content .border {
	border: 0px solid #BCE7F1;
	padding: 0px;
	width: auto;
}

#content .border .column {
	display: inline-block;
	border-top: 1px solid #BCE7F1;
	border-left: 1px solid #BCE7F1;
}

#content .border .lable {
	width: 100px;
	height: 30px;
	text-align: center;
	line-height: 30px;
	padding-left: 10px;
	padding-right: 10px;
	background-color: #D8EDF6;
	border-right: 1px solid #BCE7F1;
	border-bottom: 1px solid #BCE7F1;
}

#content .border .index {
	width: 100px;
	height: 30px;
	text-align: center;
	line-height: 30px;
	padding-left: 10px;
	padding-right: 10px;
	border-right: 1px solid #BCE7F1;
	border-bottom: 1px solid #BCE7F1;
}

#content .border .type {
	width: 100px;
	height: 30px;
	text-align: center;
	line-height: 30px;
	padding-left: 10px;
	border-right: 1px solid #BCE7F1;
	border-bottom: 1px solid #BCE7F1;
}
</style>
</head>
<body>
	<div class="container" style="padding-top: 20px;">
		<div class="panel panel-info">
			<div class="panel-heading"></div>
			<div class="panel-body">
				<fieldset>
					<legend>
						<span id="dataType"></span>
					</legend>
					<div class="form-group">
						<div class="col-md-12 col-sm-12">
							<pre id="content"></pre>
						</div>
					</div>
				</fieldset>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(document).ready(function() {
			$.ajax({
				method : 'GET',
				dataType : "json",
				url : "baseDeviceController.do?showDataProtocol&id=" + getQueryString("id")+"&code=" + getQueryString("code"),
				error : function(xhr) {
					alert("error");
				},
				success : function(result) {
					var html = "";
					if (result.success) {
						if ("JSON" == result.obj.dataType) {
							document.getElementById("dataType").innerText = "JSON格式";
							document.getElementById('content').innerText= JSON.stringify(result.obj.dateProtocol);
							var text = document.getElementById('content').innerText; //获取json格式内容
							var result = JSON.stringify(JSON.parse(text), null, 2);//将字符串转换成json对象
							var pre = document.createElement('pre');
							document.getElementById('content').parentNode.appendChild(pre);
							pre.innerText= result;
						} else {
							document.getElementById("dataType").innerHTML = "自定义格式[" + result.obj.dataShape+"]";
							var dp = result.obj.dateProtocol;
							html += "<div class='border'>";
							$(dp).each(function(i, item) {
								html += "<div class='column'>";
								html += "<div class='lable'>" + item.lable + "</div>";
								html += "<div class='index'>" + item.index + "</div>";
								html += "<div class='type'>" + item.datatype + "</div>";
								html += "</div>";
							});
							html += "</div>";
							document.getElementById("content").innerHTML = html;
						}
					} else {
						html = result.success;
					}
					console.log(html);
				}
			});
		});
		function getQueryString(name) {
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
			var r = window.location.search.substr(1).match(reg);
			if (r != null)
				return unescape(r[2]);
			return null;
		}
		function formatJson (json, options) {
			var reg = null, formatted = '', pad = 0, PADDING = '    '; // one can also use '\t' or a different number of spaces
			// optional settings
			options = options || {};
			// remove newline where '{' or '[' follows ':'
			options.newlineAfterColonIfBeforeBraceOrBracket = (options.newlineAfterColonIfBeforeBraceOrBracket === true) ? true : false;
			// use a space after a colon
			options.spaceAfterColon = (options.spaceAfterColon === false) ? false : true;

			// begin formatting...

			// make sure we start with the JSON as a string
			if (typeof json !== 'string') {
				json = JSON.stringify(json);
			}
			// parse and stringify in order to remove extra whitespace
			json = JSON.parse(json);
			json = JSON.stringify(json);

			// add newline before and after curly braces
			reg = /([\{\}])/g;
			json = json.replace(reg, '\r\n$1\r\n');

			// add newline before and after square brackets
			reg = /([\[\]])/g;
			json = json.replace(reg, '\r\n$1\r\n');

			// add newline after comma
			reg = /(\,)/g;
			json = json.replace(reg, '$1\r\n');

			// remove multiple newlines
			reg = /(\r\n\r\n)/g;
			json = json.replace(reg, '\r\n');

			// remove newlines before commas
			reg = /\r\n\,/g;
			json = json.replace(reg, ',');

			// optional formatting...
			if (!options.newlineAfterColonIfBeforeBraceOrBracket) {
				reg = /\:\r\n\{/g;
				json = json.replace(reg, ':{');
				reg = /\:\r\n\[/g;
				json = json.replace(reg, ':[');
			}
			if (options.spaceAfterColon) {
				reg = /\:/g;
				json = json.replace(reg, ': ');
			}

			$.each(json.split('\r\n'), function(index, node) {
				var i = 0, indent = 0, padding = '';

				if (node.match(/\{$/) || node.match(/\[$/)) {
					indent = 1;
				} else if (node.match(/\}/) || node.match(/\]/)) {
					if (pad !== 0) {
						pad -= 1;
					}
				} else {
					indent = 0;
				}

				for (i = 0; i < pad; i++) {
					padding += PADDING;
				}

				formatted += padding + node + '\r\n';
				pad += indent;
			});

			return formatted;
		};
	</script>
</body>
</html>