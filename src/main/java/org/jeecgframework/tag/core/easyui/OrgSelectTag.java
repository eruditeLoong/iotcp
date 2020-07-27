package org.jeecgframework.tag.core.easyui;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.util.MutiLangUtil;
import org.jeecgframework.core.util.oConvertUtils;

/**
 * 
 * 部门选择弹出框
 * 
 * @author: lijun
 * @date： 日期：2015-3-1
 * @version 1.0
 */
public class OrgSelectTag extends TagSupport {

	private static final long serialVersionUID = 1;
	private String selectedIdsInputId;      // 用于记录已选择部门编号的input的id
	private String selectedNamesInputId;    // 用于显示已选择部门名称的input的name
	private String inputWidth;     			//输入框宽度
	private String windowWidth; 			//弹出窗口宽度
	private String windowHeight; 			//弹出窗口高度
	private String departIdsDefalutVal; 	//部门ids 默认值
	private String departNamesDefalutVal;	//部门names 默认值
	private String readonly = "readonly";	// 只读属性
	private boolean hasLabel = false;       //是否显示lable,默认不显示
	private String title;			   		// 标题

	// zhouwr add 2018/07/10
	private String datatype = "*"; // 数据类型
	private String ignore = "checked"; // 是否忽略
	private String tagClass; // class

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getIgnore() {
		return ignore;
	}

	public void setIgnore(String ignore) {
		this.ignore = ignore;
	}

	public String getTagClass() {
		return tagClass;
	}

	public void setTagClass(String tagClass) {
		this.tagClass = tagClass;
	}
	// zhouwr add end 2018/07/10

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isHasLabel() {
		return hasLabel;
	}
	public void setHasLabel(boolean hasLabel) {
		this.hasLabel = hasLabel;
	}
	public String getReadonly() {
		return readonly;
	}
	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}
	
	public String getSelectedNamesInputId() {
		return selectedNamesInputId;
	}

	public void setSelectedNamesInputId(String _selectedNamesInputId) {
		this.selectedNamesInputId = _selectedNamesInputId;
	}

	public String getSelectedIdsInputId() {
		return selectedIdsInputId;
	}

	public void setSelectedIdsInputId(String _selectedIdsInputId) {
		this.selectedIdsInputId = _selectedIdsInputId;
	}
	
	public String getInputWidth() {
		return inputWidth;
	}
	public void setInputWidth(String inputWidth) {
		this.inputWidth = inputWidth;
	}
	public String getWindowWidth() {
		return windowWidth;
	}

	public void setWindowWidth(String windowWidth) {
		this.windowWidth = windowWidth;
	}
	
	public String getWindowHeight() {
		return windowHeight;
	}

	public void setWindowHeight(String windowHeight) {
		this.windowHeight = windowHeight;
	}
	
	public String getDepartIdsDefalutVal() {
		return departIdsDefalutVal;
	}
	public void setDepartIdsDefalutVal(String departIdsDefalutVal) {
		this.departIdsDefalutVal = departIdsDefalutVal;
	}
	public String getDepartNamesDefalutVal() {
		return departNamesDefalutVal;
	}
	public void setDepartNamesDefalutVal(String departNamesDefalutVal) {
		this.departNamesDefalutVal = departNamesDefalutVal;
	}
	public int doStartTag() throws JspTagException {
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {
		JspWriter out = null;
		try {
			out = this.pageContext.getOut();
			out.print(end().toString());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				out.clear();
				out.close();
			} catch (Exception e2) {
			}
		}
		return EVAL_PAGE;
	}

	public StringBuffer end() {
		
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isBlank(selectedNamesInputId)) {
			selectedNamesInputId = "orgNames"; // 默认id
		}
		if (StringUtils.isBlank(selectedIdsInputId)) {
			selectedIdsInputId = "orgIds"; // 默认id
		}
		if(StringUtils.isBlank(title)){
			title = "选择部门";
		}
		/* 将inputWidth选项提出来，判断不为空的时候设置宽度 zhouwr 2018/07/31 */
//		if (StringUtils.isBlank(inputWidth)) {
//			inputWidth = "150px";
//		}

		if (StringUtils.isBlank(windowWidth)) {
			windowWidth = "660px";
		}
		
		if(StringUtils.isBlank(windowHeight)){
			windowHeight = "350px";
		}
		if(hasLabel && oConvertUtils.isNotEmpty(title)){
			sb.append(title + "：");
		}
		sb.append("<input class=\"" + tagClass + "\" readonly=\"true\" type=\"text\" id=\"" + selectedNamesInputId + "\" name=\"" + selectedNamesInputId + "\" datatype=\""
				+ datatype + "\" ignore=\"" + ignore + "\" onclick=\"javacripet:"+selectedIdsInputId+"_openOrgSelect()\" ");

		/* 将inputWidth选项提出来，判断不为空的时候设置宽度 zhouwr 2018/07/31 */
		if (StringUtils.isNotBlank(inputWidth)) {
			sb.append("style=\"width: " + inputWidth);
		}

		if (StringUtils.isNotBlank(departNamesDefalutVal)) {
			sb.append(" value=\"" + departNamesDefalutVal + "\"");
		}
		sb.append(" />");
		
		String orgIds = "";
		
		sb.append("<input id=\"" + selectedIdsInputId + "\" name=\"" + selectedIdsInputId + "\" type=\"hidden\" ");
		if(StringUtils.isNotBlank(departIdsDefalutVal)){
			sb.append(" value=\""+departIdsDefalutVal+"\"");
			orgIds = "&orgIds=" + departIdsDefalutVal;
		}
		sb.append("/>");
		
		String commonDepartmentList = MutiLangUtil.getLang("common.department.list");
		String commonConfirm = MutiLangUtil.getLang("common.confirm");
		String commonCancel = MutiLangUtil.getLang("common.cancel");
		
		sb.append("<script type=\"text/javascript\">");
		/* 修改回调函数，解决页面有多个相同函数，set数据总是到最后一个输入框的问题 zhouwr2018/07/26 */
		StringBuffer cb_str = new StringBuffer();
		cb_str.append("function () {");
		cb_str.append("	var iframe = this.iframe.contentWindow;");
		cb_str.append("	var nodes = iframe.document.getElementsByClassName(\"departId\");");
		cb_str.append("	if(nodes.length>0){");
		cb_str.append("	var ids='',names='';");
		cb_str.append("	for(i=0;i<nodes.length;i++){");
		cb_str.append("		var node = nodes[i];");
		cb_str.append("		if(node.checked){");
		cb_str.append("			ids += node.value+',';");
		cb_str.append("			names += node.name+',';");
		cb_str.append("		}");
		cb_str.append("	}");
		cb_str.append("		$('#" + selectedNamesInputId + "').val(names.substring(0, names.length-1));");
		cb_str.append("		$('#" + selectedNamesInputId + "').blur();");
		if (oConvertUtils.isNotEmpty(selectedIdsInputId)) {
			cb_str.append("$('#" + selectedIdsInputId + "').val(ids.substring(0, ids.length-1));");	// 去掉最后的逗号2018-05-15 zhouwr
		}
		cb_str.append("	}");
		cb_str.append("}");
		/* 修改回调函数，解决页面有多个相同函数，set数据总是到最后一个输入框的问题 zhouwr2018/07/26 */
		sb.append("function "+selectedIdsInputId+"_openOrgSelect() {");
		sb.append("    $.dialog.setting.zIndex = 9999; ");// TODO
		sb.append("    $.dialog({content: 'url:departController.do?orgSelect" + orgIds + "', zIndex: 2100, title: '" + commonDepartmentList + "', lock: true, width: '"
				+ windowWidth + "', height: '" + windowHeight + "', opacity: 0.4, button: [");
		sb.append("       {name: '" + commonConfirm + "', callback: " + cb_str + ", focus: true},");		// 修改回调函数，解决页面有多个相同函数，set数据总是到最后一个输入框的问题 zhouwr2018/07/26
		sb.append("       {name: '" + commonCancel + "', callback: function (){}}");
		sb.append("   ]}).zindex();");
		sb.append("}");
		sb.append("</script>");
		return sb;
	}
}
