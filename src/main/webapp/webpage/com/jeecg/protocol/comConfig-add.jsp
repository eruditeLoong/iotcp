<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<html lang="zh-CN">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>参数配置</title>
  <meta name="description" content="">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <t:base type="jquery,aceform,DatePicker,validform,ueditor"></t:base>
  <style type="text/css">
  	.combo_self{height: 26px !important;width:164px !important;padding-top:0px !important;}
  	.layout-header .btn {
	    margin:0px;
	   float: none !important;
	}
	.btn-default {
	    height: 35px;
	    line-height: 35px;
	    font-size:14px;
	}
  </style>
  <script type="text/javascript">
	$(function(){
		$(".combo").removeClass("combo").addClass("combo combo_self");
		$(".combo").each(function(){
			$(this).parent().css("padding-top","10px !important;");
		});   
	});
  		
  		 /**树形列表数据转换**/
  function convertTreeData(rows, textField) {
      for(var i = 0; i < rows.length; i++) {
          var row = rows[i];
          row.text = row[textField];
          if(row.children) {
          	row.state = "open";
              convertTreeData(row.children, textField);
          }
      }
  }
  /**树形列表加入子元素**/
  function joinTreeChildren(arr1, arr2) {
      for(var i = 0; i < arr1.length; i++) {
          var row1 = arr1[i];
          for(var j = 0; j < arr2.length; j++) {
              if(row1.id == arr2[j].id) {
                  var children = arr2[j].children;
                  if(children) {
                      row1.children = children;
                  }
                  
              }
          }
      }
  }
  </script>
</head>
<body>
<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="comConfigController.do?doAdd" tiptype="1">
	<input type="hidden" id="btn_sub" class="btn_sub"/>
	<input type="hidden" name="id" value='${comConfigPage.id}' >
  	<input type="hidden" name="ComProtocolBy" value="${mainId}"/>
<div class="tab-wrapper">
	<!-- tab -->
	<ul class="nav nav-tabs">
		<li role="presentation" class="active"><a href="javascript:void(0);">参数配置</a></li>
	</ul>
	<!-- tab内容 -->	
	<div class="con-wrapper" id="con-wrapper1" style="display: block;">	
	<div class="row form-wrapper">
		<div class="row show-grid">
    	<div class="col-xs-3 text-center">
      		<b>参数标题：</b>
        </div>
      	<div class="col-xs-3">
			<input name="title"  style="width:150px" maxlength="32" type="text" class="form-control"   datatype="*" ignore="checked" />
			<span class="Validform_checktip" style="float:left;height:0px;"></span>
			<label class="Validform_label" style="display: none">参数标题</label>
        </div>
		</div>
		<div class="row show-grid">
    	<div class="col-xs-3 text-center">
      		<b>参数变量名：</b>
        </div>
      	<div class="col-xs-3">
			<input name="name"  style="width:150px" maxlength="32" type="text" class="form-control"   datatype="s" ignore="checked" />
			<span class="Validform_checktip" style="float:left;height:0px;"></span>
			<label class="Validform_label" style="display: none">参数变量名</label>
        </div>
		</div>
		<div class="row show-grid">
    	<div class="col-xs-3 text-center">
      		<b>参数变量值：</b>
        </div>
      	<div class="col-xs-3">
			<input name="value"  style="width:150px" maxlength="32" type="text" class="form-control"   datatype="*" ignore="checked" />
			<span class="Validform_checktip" style="float:left;height:0px;"></span>
			<label class="Validform_label" style="display: none">参数变量值</label>
        </div>
		</div>
		<div class="row show-grid">
          <div class="col-xs-3 text-center">
          	<b>备注：</b>
          </div>
          <div class="col-xs-6">
			  	 	<textarea id="remark" class="form-control" rows="6" style="width:95%" name="remark"  ignore="ignore" ></textarea>
			<span class="Validform_checktip" style="float:left;height:0px;"></span>
			<label class="Validform_label" style="display: none">备注</label>
          </div>
			</div>
            <div class="row" id = "sub_tr" style="display: none;">
	        <div class="col-xs-12 layout-header">
	          <div class="col-xs-6"></div>
	          <div class="col-xs-6"><button type="button" onclick="neibuClick();" class="btn btn-default">提交</button></div>
	        </div>
	      </div>
     </div>
   </div>
<div class="con-wrapper" id="con-wrapper2" style="display: block;"></div>
</div>
</t:formvalid>
<script type="text/javascript">
   $(function(){
    //查看模式情况下,删除和上传附件功能禁止使用
	if(location.href.indexOf("load=detail")!=-1){
		$(".jeecgDetail").hide();
	}
	
	if(location.href.indexOf("mode=read")!=-1){
		//查看模式控件禁用
		$("#formobj").find(":input").attr("disabled","disabled");
	}
	if(location.href.indexOf("mode=onbutton")!=-1){
		//其他模式显示提交按钮
		$("#sub_tr").show();
	}
   });

  var neibuClickFlag = false;
  function neibuClick() {
	  neibuClickFlag = true; 
	  $('#btn_sub').trigger('click');
  }

</script>
 </body>
</html>
