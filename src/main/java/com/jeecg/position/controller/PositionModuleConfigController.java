package com.jeecg.position.controller;

import com.jeecg.position.entity.PositionModuleConfigEntity;
import com.jeecg.position.service.PositionModuleConfigServiceI;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.ExceptionUtil;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 定位卡设置
 * @author onlineGenerator
 * @date 2019-09-06 16:26:24
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/positionModuleConfigController")
public class PositionModuleConfigController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PositionModuleConfigController.class);

    @Autowired
    private PositionModuleConfigServiceI positionModuleConfigService;
    @Autowired
    private SystemService systemService;

    /**
     * 定位卡设置列表 页面跳转
     * 
     * @return
     */
    @RequestMapping(params = "list")
    public ModelAndView list(HttpServletRequest request) {
        return new ModelAndView("com/jeecg/position/positionModuleConfigList");
    }

    /**
     * easyui AJAX请求数据
     * 
     * @param request
     * @param response
     * @param dataGrid
     * @param user
     */

    @RequestMapping(params = "datagrid")
    public void datagrid(PositionModuleConfigEntity positionModuleConfig, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(PositionModuleConfigEntity.class, dataGrid);
        // 查询条件组装器
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, positionModuleConfig, request.getParameterMap());
        try {
            // 自定义追加查询条件

        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        cq.add();
        this.positionModuleConfigService.getDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 删除定位卡设置
     * 
     * @return
     */
    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(PositionModuleConfigEntity positionModuleConfig, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        positionModuleConfig = systemService.getEntity(PositionModuleConfigEntity.class, positionModuleConfig.getId());
        message = "定位卡设置删除成功";
        try {
            positionModuleConfigService.delete(positionModuleConfig);
            systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "定位卡设置删除失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 批量删除定位卡设置
     * 
     * @return
     */
    @RequestMapping(params = "doBatchDel")
    @ResponseBody
    public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "定位卡设置删除成功";
        try {
            for (String id : ids.split(",")) {
                PositionModuleConfigEntity positionModuleConfig = systemService.getEntity(PositionModuleConfigEntity.class, id);
                positionModuleConfigService.delete(positionModuleConfig);
                systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "定位卡设置删除失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 添加定位卡设置
     * 
     * @param ids
     * @return
     */
    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(PositionModuleConfigEntity positionModuleConfig, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "定位卡设置添加成功";
        try {
            positionModuleConfig.setId(positionModuleConfig.getImei());
            positionModuleConfig.setUpdateDate(new Date());
            positionModuleConfigService.save(positionModuleConfig);
            positionModuleConfigService.setModuleConf(positionModuleConfig);
            systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "定位卡设置添加失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 更新定位卡设置
     * 
     * @param positionModuleConfig
     * @return
     */
    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(PositionModuleConfigEntity positionModuleConfig, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "定位卡设置更新成功";
        try {
            positionModuleConfig.setUpdateDate(new Date());
            positionModuleConfigService.saveOrUpdate(positionModuleConfig);
            positionModuleConfigService.setModuleConf(positionModuleConfig);
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            message = "定位卡设置更新失败，" + e.getMessage();
            e.printStackTrace();
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 定位卡设置新增页面跳转
     * 
     * @return
     */
    @RequestMapping(params = "goAdd")
    public ModelAndView goAdd(PositionModuleConfigEntity positionModuleConfig, HttpServletRequest req) {
        if (StringUtil.isNotEmpty(positionModuleConfig.getId())) {
            positionModuleConfig = positionModuleConfigService.getEntity(PositionModuleConfigEntity.class, positionModuleConfig.getId());
            req.setAttribute("positionModuleConfig", positionModuleConfig);
        }
        return new ModelAndView("com/jeecg/position/positionModuleConfig-add");
    }

    /**
     * 定位卡设置编辑页面跳转
     * 
     * @return
     */
    @RequestMapping(params = "goUpdate")
    public ModelAndView goUpdate(PositionModuleConfigEntity positionModuleConfig, HttpServletRequest req) {
        if (StringUtil.isNotEmpty(positionModuleConfig.getId())) {
            positionModuleConfig = positionModuleConfigService.getEntity(PositionModuleConfigEntity.class, positionModuleConfig.getId());
            req.setAttribute("positionModuleConfig", positionModuleConfig);
        }
        return new ModelAndView("com/jeecg/position/positionModuleConfig-update");
    }

    /**
     * 导入功能跳转
     * 
     * @return
     */
    @RequestMapping(params = "upload")
    public ModelAndView upload(HttpServletRequest req) {
        req.setAttribute("controller_name", "positionModuleConfigController");
        return new ModelAndView("common/upload/pub_excel_upload");
    }

    /**
     * 导出excel
     * 
     * @param request
     * @param response
     */
    @RequestMapping(params = "exportXls")
    public String exportXls(PositionModuleConfigEntity positionModuleConfig, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid,
            ModelMap modelMap) {
        CriteriaQuery cq = new CriteriaQuery(PositionModuleConfigEntity.class, dataGrid);
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, positionModuleConfig, request.getParameterMap());
        List<PositionModuleConfigEntity> positionModuleConfigs = this.positionModuleConfigService.getListByCriteriaQuery(cq, false);
        modelMap.put(NormalExcelConstants.FILE_NAME, "定位卡设置");
        modelMap.put(NormalExcelConstants.CLASS, PositionModuleConfigEntity.class);
        modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("定位卡设置列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
        modelMap.put(NormalExcelConstants.DATA_LIST, positionModuleConfigs);
        return NormalExcelConstants.JEECG_EXCEL_VIEW;
    }

    /**
     * 导出excel 使模板
     * 
     * @param request
     * @param response
     */
    @RequestMapping(params = "exportXlsByT")
    public String exportXlsByT(PositionModuleConfigEntity positionModuleConfig, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid,
            ModelMap modelMap) {
        modelMap.put(NormalExcelConstants.FILE_NAME, "定位卡设置");
        modelMap.put(NormalExcelConstants.CLASS, PositionModuleConfigEntity.class);
        modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("定位卡设置列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
        modelMap.put(NormalExcelConstants.DATA_LIST, new ArrayList());
        return NormalExcelConstants.JEECG_EXCEL_VIEW;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(params = "importExcel", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson importExcel(HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<PositionModuleConfigEntity> listPositionModuleConfigEntitys = ExcelImportUtil.importExcel(file.getInputStream(),
                        PositionModuleConfigEntity.class, params);
                for (PositionModuleConfigEntity positionModuleConfig : listPositionModuleConfigEntitys) {
                    positionModuleConfigService.save(positionModuleConfig);
                }
                j.setMsg("文件导入成功！");
            } catch (Exception e) {
                j.setMsg("文件导入失败！");
                logger.error(ExceptionUtil.getExceptionMessage(e));
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return j;
    }

}
