<#if po.field??>
	 ,{field:'${po.field}'
	 ,title:'${po.title}'
<#else>
	,{title:'${po.title}'
</#if>
	 <#if po.hidden>
	 ,visible:false,cardVisible:false
	 </#if>
	 ,valign: 'middle'
	 <#if po.width??>
	 ,width:${po.width}
	 </#if>
	 <#if po.align??>
	 ,align:'${po.align}'
	 </#if>
	 <#if po.rowspan??>
	 //,rowspan:'${po.rowspan}'
	 </#if>
	 <#if po.colspan??>
	 //,colspan:'${po.colspan}'
	 </#if>
	 <#if po.sortable>
	 ,sortable:${po.sortable}
	 </#if>
	 <#if po.formatterjs??>
		 ,formatter:function(value,row,index){
		 	return ${po.formatterjs}(value,row,index);
		 }
	 <#else>
	 	<#if po.popup>
	 	<#elseif po.replace??>
	 		,formatter:function(value,row,index){
					<#list po.replace?split(",") as str> 
						if(value =='${str?split("_")[1]}'){
	 						return '${MutiLangUtil.getLang(str?split("_")[0])}';
	 					} 
					</#list> 
					return value;
	 		}
	 	<#elseif po.dictionary??>
	 		<#if po.dictionary?index_of(",")!=-1>
	 		<#-- update-begin-author:zhoujf date:20180712 for:TASK #2809 【bug】网友反馈问题，字典性能问题 -->
	 		    <#if po.isAjaxDict>
	 		    	${ComponentTools.getAjaxDict(po)}
	 		    <#else>
		 			,formatter:function(value,row,index){
		 				<#-- 解决list列表以逗号分隔的多个数值转换 zhouwr 2018/07/26 -->
		 				var vals = value.split(",");
		 				var text = '';
			 			for(var i=0;i<vals.length;i++){
			 				<#list ComponentTools.getTableDictData(po) as dict>
			 					if(vals[i] == '${dict.field}'){
			 						text += '${MutiLangUtil.getLang(dict.text)}' + ',';
			 						continue;
			 					}
							</#list>
						}
						if(text.length>0){
							return text.substring(0, text.length-1);;
						}
						<#-- 解决list列表以逗号分隔的多个数值转换 zhouwr 2018/07/26 -->
						return value;
		 			}
	 			</#if>
	 		<#-- update-end-author:zhoujf date:20180712 for:TASK #2809 【bug】网友反馈问题，字典性能问题 -->
	 		<#elseif dataGrid.columnValueList?size gt 0>
	 			<#list dataGrid.columnValueList as columnValue>  
			 	    <#if columnValue.name == po.field>
			 			${ComponentTools.getColumnValue(columnValue)}
					</#if>
				</#list>
	 		<#else>
	 			,formatter:function(value,row,index){
	 				<#list ComponentTools.getDictData(po) as dict>  
	 					if(value =='${dict.typecode}'){
	 						return '${MutiLangUtil.getLang(dict.typename)}';
	 					}
					</#list> 
					return value;
	 			}	
	 		</#if>
	 	<#elseif po.image>
	 		<#if po.imageSize??>
	 			,formatter:function(value,row,index){
	 			 	return '<img width="${po.imageSize?split(",")[0]}" '
	 			       +'height="${po.imageSize?split(",")[1]}" '
	 			       +'border="0" '
	 			       +'onclick="tipImg(this)" '
	                   +'src="'+value+'"/>';
	            }
	 		<#else>
	 			,formatter:function(value,row,index){
	 				 return '<img border="0" src="'+value+'"/>';}
	 			}
	 		</#if>
	 	<#elseif po.downloadName??>
	 	
	 	<#elseif po.url??>
	 		,formatter:function(value,row,index){
	 			var href="<a style='color:red' href='#' onclick=${po.funname}('${po.title}','${ComponentTools.formatUrlPlus(po.url)}')>\";
	 		}
	 	<#elseif po.formatter??>
	 		,formatter:function(value,row,index){
	 		 return new Date().format('${po.formatter}',value);
	 		}
	 	<#elseif po.showLen??>
	 		,formatter:function(value,row,index){
	 			if(value==undefined) {return ''} 
	 			if(value.length<=${po.showLen}) {
	 				return value
	 			}else{ 
	 				return '<span title= \"'+value+'\">'+ value.substring(0,${po.showLen})+'...</span>';}
	 		}
	 	<#elseif dataGrid.columnValueList?size gt 0>
	 		<#list dataGrid.columnValueList as columnValue>  
		 	    <#if columnValue.name == po.field>
		 			${ComponentTools.getColumnValue(columnValue)}
				</#if>
			</#list>
	 	<#elseif dataGrid.columnStyleList?size gt 0>
	 		<#list dataGrid.columnStyleList as columnValue>  
		 	    <#if columnValue.name == po.field>
		 			${ComponentTools.getCellStyle(columnValue)}
				</#if>
			</#list>
	 	</#if>
	 </#if>
 	
	 }								