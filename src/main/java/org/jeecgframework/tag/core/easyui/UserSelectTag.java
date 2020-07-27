package org.jeecgframework.tag.core.easyui;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.util.MutiLangUtil;
import org.jeecgframework.core.util.oConvertUtils;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * 
 * 用户选择弹出组件
 * 
 * @author: wangkun
 * @date： 日期：2015-3-27
 * @version 1.0
 */
public class UserSelectTag extends TagSupport {

	private static final long serialVersionUID = 1;

	private String title; // 标题
	private String selectedNamesInputId; // 用于显示已选择用户的用户名 input的id
	private String selectedIdsInputId; // 用于记录已选择用户的用户id input的id
	private boolean hasLabel = false; // 是否显示lable,默认不显示
	private String userNamesDefalutVal; // 用户名默认值
	private String userIdsDefalutVal; // 用户ID默认值
	private String readonly = "readonly"; // 只读属性
	private String inputWidth; // 输入框宽度
	private String windowWidth; // 弹出窗口宽度
	private String windowHeight; // 弹出窗口高度

	private String callback;// 自定义回掉函数
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

	public String getUserIdsDefalutVal() {
		return userIdsDefalutVal;
	}
	public void setUserIdsDefalutVal(String userIdsDefalutVal) {
		this.userIdsDefalutVal = userIdsDefalutVal;
	}
	public String getSelectedIdsInputId() {
		return selectedIdsInputId;
	}
	public void setSelectedIdsInputId(String selectedIdsInputId) {
		this.selectedIdsInputId = selectedIdsInputId;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSelectedNamesInputId() {
		return selectedNamesInputId;
	}

	public void setSelectedNamesInputId(String _selectedNamesInputId) {
		this.selectedNamesInputId = _selectedNamesInputId;
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
	
	public String getUserNamesDefalutVal() {
		return userNamesDefalutVal;
	}
	public void setUserNamesDefalutVal(String userNamesDefalutVal) {
		this.userNamesDefalutVal = userNamesDefalutVal;
	}

	public String getCallback() {
		if(oConvertUtils.isEmpty(callback)){
			callback = "callbackUserSelect";
		}
		return callback;
	}
	public void setCallback(String callback) {
		this.callback = callback;
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
			selectedNamesInputId = "userNames"; // 默认id
		}
		if(StringUtils.isBlank(title)){
			title = "用户名称";
		}
		/* 将inputWidth选项提出来，判断不为空的时候设置宽度 zhouwr 2018/07/31 */
		
		if (StringUtils.isBlank(windowWidth)) {
			windowWidth = "1000px";
		}

		if (StringUtils.isBlank(windowHeight)) {
			windowHeight = "700px";
		}
		if (StringUtils.isBlank(datatype)) {
			datatype = "*";
		}

		if (StringUtils.isBlank(tagClass)) {
			tagClass = "form-control";
		}
		if (StringUtils.isBlank(ignore)) {
			ignore = "ignore";
		}
		if(hasLabel && oConvertUtils.isNotEmpty(title)){
			sb.append(title + "：");
		}
		sb.append("<input class=\""+tagClass+"\" readonly=\"" + readonly + "\" type=\"text\" id=\"" + selectedNamesInputId
				+ "\" name=\"" + selectedNamesInputId + "\" datatype=\""+ datatype +"\" ignore=\""+ignore+"\""
				+ "\" onclick=\"javacripet:"+selectedIdsInputId+"_openUserSelect();\" ");
		/* 将inputWidth选项提出来，判断不为空的时候设置宽度 zhouwr 2018/07/31 */
		if (StringUtils.isNotBlank(inputWidth)) {
			sb.append("style=\"width: " + inputWidth);
		}

		if(StringUtils.isNotBlank(userNamesDefalutVal)){
			sb.append(" value=\""+userNamesDefalutVal+"\"");
		}
		sb.append(" />");
		
		if(oConvertUtils.isNotEmpty(selectedIdsInputId)){
			sb.append("<input class=\"inuptxt\" id=\"" + selectedIdsInputId + "\" name=\"" + selectedIdsInputId + "\" type=\"hidden\" ");
			if(StringUtils.isNotBlank(userIdsDefalutVal)){
				sb.append(" value=\""+userIdsDefalutVal+"\"");
			}
			sb.append("/>");
		}
		
		String commonConfirm = MutiLangUtil.getLang("common.confirm");
		String commonCancel = MutiLangUtil.getLang("common.cancel");
		
		sb.append("<script type=\"text/javascript\">");
		sb.append("function "+selectedIdsInputId+"_openUserSelect() {");

		sb.append("    $.dialog({content: 'url:userController.do?userSelect', zIndex: getzIndex(), title: '" + title
				+ "', lock: true, width: '" + windowWidth + "', height: '" + windowHeight
				+ "', opacity: 0.4, button: [");

		StringBuffer cb_str = new StringBuffer();
		cb_str.append("function () {");
		cb_str.append("var iframe = this.iframe.contentWindow;");
		cb_str.append("var rowsData = iframe.$('#userList1').datagrid('getSelections');");
		cb_str.append("if (!rowsData || rowsData.length==0) {");
		cb_str.append("tip('<t:mutiLang langKey=\"common.please.select.edit.item\"/>');");
		cb_str.append("return;");
		cb_str.append("}");

		cb_str.append(" var ids='',names='';");
		cb_str.append("for(i=0;i<rowsData.length;i++){");
		cb_str.append(" var node = rowsData[i];");
		cb_str.append(" ids += node.userName+',';");	// 修改为用户登录名ode 2018-05-15 zhouwr
		cb_str.append(" names += node.realName+',';");
		cb_str.append("}");

		cb_str.append("$('#" + selectedNamesInputId + "').val(names.substring(0, names.length-1));");	// 去掉最后的逗号2018-05-15 zhouwr
		cb_str.append("$('#" + selectedNamesInputId + "').blur();");
		if (oConvertUtils.isNotEmpty(selectedIdsInputId)) {
			cb_str.append("$('#" + selectedIdsInputId + "').val(ids.substring(0, ids.length-1));");	// 去掉最后的逗号2018-05-15 zhouwr
		}
		cb_str.append("}");

		sb.append("       {name: '" + commonConfirm + "', callback: " + cb_str + ", focus: true},");

		sb.append("       {name: '" + commonCancel + "', callback: function (){}}");
		sb.append("   ]});");

		sb.append("}");

		sb.append("</script>");
		return sb;
	}
}
