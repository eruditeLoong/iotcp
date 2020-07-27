<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<style type="text/css">
	.panel-body {
		/* width: 100%;
		height: 99%; */
		overflow-x: hidden;
		padding-bottom: 10px;
	}

	.count-box-parent {
		display: flex;
		/* flex: 1; */
		flex-wrap: wrap;
		justify-content: flex-start;
	}

	.count-box {
		width: 60px;
		height: 60px;
		margin-left: 10px;
		margin-top: 10px;
		border: 1px solid #e4edff;
		border-radius: 5px;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: space-evenly;
		background-color: #e0e0e033;
		-moz-box-shadow: 2px 2px 5px #aeb7af;
		-webkit-box-shadow: 2px 2px 5px #aeb7af;
		box-shadow: 2px 2px 5px #aeb7af;
	}

	.count-num {
		font-size: large;
		color: #00bf19;
		padding: 10px;
		font-weight: 600;
		align-items: end;
		font-weight: 600;
	}

	.count-title {}
</style>
<div class="container" style="width: 100%;">
	<div class="panel-body">
		<div class="">
			<div style="border: 0px;border-bottom:1px solid #e4edff; margin:0px;padding: 10px;">
				IMEI：<input type="text" id="imei" readonly value="${imei }" style="border: 0px;background-color: transparent;" />
			</div>
			<div class="count-box-parent" id="count-box-parent"></div>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function () {
		var imei = $('#imei').val();
		$.getJSON("positionModuleController.do?getEventCount&imei=" + imei, function (res) {
			if (res.success) {
				var eCount = res.obj || [];
				for (var c of eCount) {
					console.log(c);
					var a = document.createElement('a');
					a.href = 'javascript:eventView('+c.code+')';
					var box = document.createElement('div');
					box.className = 'count-box';
					var num = document.createElement('div');
					num.className = 'count-num';
					num.innerText = c.count;
					box.append(num);
					var title = document.createElement('div');
					title.className = 'count-title';
					title.innerText = c.name;
					box.append(title);
					a.append(box);
					$('#count-box-parent').append(a);
				}
			}
		});
	});

function eventView(code){
	alert(code);
}
</script>