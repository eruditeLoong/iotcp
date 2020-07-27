<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>施工人员</title>
<meta name="viewport" content="width=device-width" />
<t:base type="bootstrap,layer,validform,webuploader,bootstrap-form"></t:base>
<script src="plug-in/vue/vue.js"></script>
<style type="text/css">
[v-cloak] {
	display: none;
}
#listDiv{
	border: 1px solid;
	border-color:rgb(63, 164, 204);
	background-color:rgb(218,243,254);
	width:100%;
	border-radius:4px;
}
#list tr{
	border-top:1px solid;
	border-bottom:1px solid;
	border-color:rgb(218,243,254);
	background-color:#fff;
	height:30px;
}
#list tr: hover{
	border-color:rgb(63, 164, 204);
}
</style>

</head>
<body style="padding:10px;">
	<div id="listDiv">
		<table style="width:100%;">
			<thead>
				<tr style="background-color:rgb(218,243,254);height:30px;">
					<td style="width:120px; text-align: center;">序号</td>
					<td style="width:120px; text-align: center;">姓名</td>
					<td style="width:120px; text-align: center;">职称</td>
				</tr>
			</thead>
			<tbody id="list">
				<tr v-for="(user, index) in users" v-cloak>
					<td style="text-align: center;">{{index+1}}</td>
					<td style="text-align: center;">{{user.name}}</td>
					<td style="text-align: center;">{{user.duty}}</td>
				</tr>
			</tbody>
		</table>
	</div>
</body>
<script type="text/javascript">
	var wid = '${workId}';
	var bid = '${branchId}';
	var vue = new Vue({
		el : '#list',
		data: {
			users: []
		}
	});
	$.ajax({
		type : "GET",
		url : "workOrderController.do?getWorkUser",
		data : {
			wid : wid,
			bid : bid,
		},
		dataType : "json",
		success : function(result) {
			console.log(result.obj);
			vue.users = result.obj;
		}
	});
</script>
</html>