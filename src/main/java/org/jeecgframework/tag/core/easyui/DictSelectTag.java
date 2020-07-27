package org.jeecgframework.tag.core.easyui;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.MutiLangUtil;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.pojo.base.TSTypegroup;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 选择下拉框
 *
 * @version 1.0
 * @author: lianglaiyang
 * @date： 日期：2013-04-18
 */
public class DictSelectTag extends TagSupport {

    private static final long serialVersionUID = 1;
    @Autowired
    private static SystemService systemService;
    private String typeGroupCode; // 数据字典类型
    private String field; // 选择表单的Name EAMPLE:<select name="selectName" id = ""/>
    private String id; // 选择表单ID EAMPLE:<select name="selectName" id = "" />
    private String defaultVal; // 默认值
    private String divClass; // DIV样式
    private String labelClass; // Label样式
    private String title; // label显示值
    private boolean hasLabel = false; // 是否显示label
    private String type;// 控件类型select|radio|checkbox
    private String dictTable;// 自定义字典表
    private String dictField;// 自定义字典表的匹配字段-字典的编码值
    private String dictText;// 自定义字典表的显示文本-字典的显示值
    private String extendJson;//扩展参数
    private String readonly;// 只读属性
    private String dictCondition;// 查询条件属性
    private String datatype;// 校验类型 validform，必须输入规则：*
    private String ignore; // zhouwr

    public String getReadonly() {
        return readonly;
    }

    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

    public String getDictCondition() {
        return dictCondition;
    }

    public void setDictCondition(String dicCondition) {
        this.dictCondition = dicCondition;
    }

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
        } finally {
            try {
                out.clear();
                out.close();
                end().setLength(0);
            } catch (Exception e2) {
            }
        }
        return EVAL_PAGE;
    }

    public StringBuffer end() {

        StringBuffer sb = new StringBuffer();
        if (StringUtils.isBlank(divClass)) {
            divClass = "form"; // 默认form样式
        }
        if (StringUtils.isBlank(labelClass)) {
            labelClass = "Validform_label"; // 默认label样式
        }
        if (dictTable != null) {
            List<Map<String, Object>> list = queryDic();
            if ("radio".equals(type)) {
                for (Map<String, Object> map : list) {
                    radio(map.get("text").toString(), map.get("field")
                            .toString(), sb);
                }
            } else if ("checkbox".equals(type)) {
                for (Map<String, Object> map : list) {
                    checkbox(map.get("text").toString(), map.get("field")
                            .toString(), sb);
                }
            } else if ("text".equals(type)) {
                for (Map<String, Object> map : list) {
                    text(map.get("text").toString(), map.get("field")
                            .toString(), sb);
                }
            } else {
                sb.append("<select name=\"" + field + "\"");

                //增加扩展属性
                if (!StringUtils.isBlank(this.extendJson)) {

                    sb.append(this.getExtendJsonCommon(extendJson));

                }

                this.readonly(sb);

                if (!StringUtils.isBlank(this.id)) {
                    sb.append(" id=\"" + id + "\"");
                }
                this.datatype(sb);
                this.ignore(sb);
                sb.append(">");
                select("common.please.select", "", sb);
                for (Map<String, Object> map : list) {
                    select(map.get("text").toString(), map.get("field").toString(), sb);
                }
                sb.append("</select>");
            }
        } else {
            TSTypegroup typeGroup = ResourceUtil.getCacheTypeGroup(this.typeGroupCode.toLowerCase());
            List<TSType> types = ResourceUtil.getCacheTypes(this.typeGroupCode.toLowerCase());
            if (hasLabel) {
                sb.append("<div class=\"" + divClass + "\">");
                sb.append("<label class=\"" + labelClass + "\" >");
            }
            if (typeGroup != null) {
                if (hasLabel) {
                    if (StringUtils.isBlank(this.title)) {
                        this.title = MutiLangUtil.getLang(typeGroup.getTypegroupname());
                    }
                    sb.append(this.title + ":");
                    sb.append("</label>");
                }
                if ("radio".equals(type)) {
                    for (TSType type : types) {
                        radio(type.getTypename(), type.getTypecode(), sb);
                    }
                } else if ("checkbox".equals(type)) {
                    for (TSType type : types) {
                        checkbox(type.getTypename(), type.getTypecode(), sb);
                    }
                } else if ("text".equals(type)) {
                    for (TSType type : types) {
                        text(type.getTypename(), type.getTypecode(), sb);
                    }
                } else {
                    sb.append("<select name=\"" + field + "\"");

                    //增加扩展属性
                    if (!StringUtils.isBlank(this.extendJson)) {
                        sb.append(this.getExtendJsonCommon(extendJson));
                    }

                    this.readonly(sb);

                    if (!StringUtils.isBlank(this.id)) {
                        sb.append(" id=\"" + id + "\"");
                    }
                    this.datatype(sb);
                    this.ignore(sb);
//                    zhouwr 2019/08/11
//                    sb.append("style='appearance: none;-webkit-appearance: none;-moz-appearance: none;' ");
                    sb.append(">");
                    select("common.please.select", "", sb);
                    for (TSType type : types) {
                        select(type.getTypename(), type.getTypecode(), sb);
                    }
                    sb.append("</select>");
                }
                if (hasLabel) {
                    sb.append("</div>");
                }
            }
        }

        return sb;
    }

    private StringBuffer getExtendJsonCommon(String extendJson) {
        Gson gson = new Gson();
        Map<String, String> mp = gson.fromJson(extendJson, Map.class);
        StringBuffer sb = new StringBuffer();
        sb.append(" ");
        for (Map.Entry<String, String> entry : mp.entrySet()) {
            //判断select标签中是否含有style属性
            if (entry.getKey().equals("style")) {
                //并且readonly属性不为空
                if (StringUtils.isNotBlank(readonly) && readonly.equals("readonly")) {
                    //拼接Style属性
                    String entryValue = entry.getValue() + ";background-color:#eee;cursor:no-drop;";
                    //把拼接好的属性加入到sb字符串中
                    sb.append(entry.getKey() + "=\"" + entryValue + "\"");
                } else {
                    //如果readonly属性为空，走原来的样式
                    sb.append(entry.getKey() + "=\"" + entry.getValue() + "\"");
                }
            } else {
                //如果没有Style属性的话走原来的方法，readonly属性在下边readonly方法中已经默认添加了样式
                sb.append(entry.getKey() + "=\"" + entry.getValue() + "\"");
            }
        }
        return sb;
    }

    /**
     * 文本框方法
     *
     * @param name
     * @param code
     * @param sb
     */
    private void text(String name, String code, StringBuffer sb) {
        if (code.equals(this.defaultVal)) {
            sb.append("<input name='" + field + "'" + " id='" + id + "' value='" + MutiLangUtil.getLang(name) + "' readOnly = 'readOnly' />");
        } else {
        }
    }


    /**
     * 单选框方法
     *
     * @param name
     * @param code
     * @param sb
     * @作者：Alexander
     */
    private void radio(String name, String code, StringBuffer sb) {

        if (code.equals(this.defaultVal)) {
            sb.append("<input type=\"radio\" name=\"" + field + "\" checked=\"checked\" value=\"" + code + "\"");
        } else {
            sb.append("<input type=\"radio\" name=\"" + field + "\" value=\"" + code + "\"");
        }
        if (!StringUtils.isBlank(this.id)) {
            sb.append(" id=\"" + id + "\"");
        }

        this.readonly(sb);

        this.datatype(sb);
        this.ignore(sb);
        if (!StringUtils.isBlank(this.extendJson)) {
            sb.append(this.getExtendJsonCommon(extendJson));
        }
        sb.append(" />");
        sb.append(MutiLangUtil.getLang(name) + "&nbsp;&nbsp;");

    }

    /**
     * 复选框方法
     *
     * @param name
     * @param code
     * @param sb
     * @作者：Alexander
     */
    private void checkbox(String name, String code, StringBuffer sb) {

        if (this.defaultVal == null) {
            this.defaultVal = "";
        }

        String[] values = this.defaultVal.split(",");
        Boolean checked = false;
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            if (code.equals(value)) {
                checked = true;
                break;
            }
            checked = false;
        }

        if (checked) {
            sb.append("<input type=\"checkbox\" name=\"" + field
                    + "\" checked=\"checked\" value=\"" + code + "\"");
        } else {
            sb.append("<input type=\"checkbox\" name=\"" + field
                    + "\" value=\"" + code + "\"");
        }
        if (!StringUtils.isBlank(this.id)) {
            sb.append(" id=\"" + id + "\"");
        }

        this.readonly(sb);

        this.datatype(sb);
        this.ignore(sb);
        if (!StringUtils.isBlank(this.extendJson)) {
            sb.append(" " + this.getExtendJsonCommon(extendJson));
        }
        sb.append(" />");
        sb.append(MutiLangUtil.getLang(name) + "&nbsp;&nbsp;");

    }

    /**
     * 选择框方法
     *
     * @param name
     * @param code
     * @param sb
     * @作者：Alexander
     */
    private void select(String name, String code, StringBuffer sb) {

        if (code.equals(this.defaultVal)) {
            if (StringUtils.isNotBlank(readonly) && readonly.equals("readonly")) {
                sb.append(" <option style=\"display: none;\"  value=\"" + code + "\" selected=\"selected\">");
            } else {
                sb.append(" <option  value=\"" + code + "\" selected=\"selected\">");
            }
        } else {
            if (StringUtils.isNotBlank(readonly) && readonly.equals("readonly")) {
                sb.append(" <option style=\"display: none;\" value=\"" + code + "\">");
            } else {
                sb.append(" <option  value=\"" + code + "\">");
            }
        }

        sb.append(MutiLangUtil.getLang(name));
        sb.append(" </option>");
    }

    /**
     * 查询自定义数据字典
     *
     * @作者：Alexander
     */
    private List<Map<String, Object>> queryDic() {
        String sql = "select " + dictField + " as field," + dictText
                + " as text from " + dictTable;

        if (dictCondition != null) {
            sql += " " + dictCondition + " ";
        }

        systemService = ApplicationContextUtil.getContext().getBean(
                SystemService.class);
        List<Map<String, Object>> list = systemService.findForJdbc(sql);
        return list;
    }

    /**
     * 加入datatype属性,并加入非空验证作为默认值
     *
     * @param sb
     * @return
     */
    private StringBuffer datatype(StringBuffer sb) {
        if (!StringUtils.isBlank(this.datatype)) {
            sb.append(" datatype=\"" + datatype + "\"");
        }
        return sb;
    }

    /**
     * 加入ignore属性,并加入非空验证作为默认值
     *
     * @param sb
     * @return
     */
    private StringBuffer ignore(StringBuffer sb) {
        if (!StringUtils.isBlank(this.ignore)) {
            sb.append(" ignore=\"" + ignore + "\"");
        }
        return sb;
    }

    /**
     * 加入readonly 属性,当此属性值为 readonly时，设置控件只读
     *
     * @param sb
     * @return sb
     * @author jg_xugj
     */
    private StringBuffer readonly(StringBuffer sb) {

        if (StringUtils.isNotBlank(readonly) && readonly.equals("readonly")) {
            if ("radio".equals(type)) {
                sb.append(" readonly=\"readonly\" style=\"background-color:#eee;cursor:no-drop;\" disabled=\"true\" ");
            } else if ("checkbox".equals(type)) {
                sb.append(" readonly=\"readonly\" style=\"background-color:#eee;cursor:no-drop;\" disabled=\"true\" ");
            } else if ("text".equals(type)) {

            } else if ("list".equals(type)) {
                sb.append(" readonly=\"readonly\" style=\"background-color:#eee;cursor:no-drop;\" ");

            } else {
                sb.append(" readonly=\"readonly\" style=\"background-color:#eee;cursor:no-drop;\" ");
            }
        }

        return sb;
    }

    public String getTypeGroupCode() {
        return typeGroupCode;
    }

    public void setTypeGroupCode(String typeGroupCode) {
        this.typeGroupCode = typeGroupCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public String getDivClass() {
        return divClass;
    }

    public void setDivClass(String divClass) {
        this.divClass = divClass;
    }

    public String getLabelClass() {
        return labelClass;
    }

    public void setLabelClass(String labelClass) {
        this.labelClass = labelClass;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDictTable() {
        return dictTable;
    }

    public void setDictTable(String dictTable) {
        this.dictTable = dictTable;
    }

    public String getDictField() {
        return dictField;
    }

    public void setDictField(String dictField) {
        this.dictField = dictField;
    }

    public String getDictText() {
        return dictText;
    }

    public void setDictText(String dictText) {
        this.dictText = dictText;
    }

    public String getExtendJson() {
        return extendJson;
    }

    public void setExtendJson(String extendJson) {
        this.extendJson = extendJson;
    }
}
