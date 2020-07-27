package com.jeecg.scene.controller;

import com.alibaba.fastjson.JSONObject;
import com.jeecg.nio.mina.CRCUtil;
import com.jeecg.scene.entity.SceneDeviceDepolyEntity;
import com.jeecg.scene.service.SceneDeviceDepolyServiceI;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.*;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version V1.0
 * @Title: Controller
 * @Description: 场景部署设备
 * @date 2019-04-18 22:50:38
 */
@Controller
@RequestMapping("/sceneDeviceDepolyController")
public class SceneDeviceDepolyController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(SceneDeviceDepolyController.class);

    @Autowired
    private SceneDeviceDepolyServiceI sceneDeviceDepolyService;
    @Autowired
    private SystemService systemService;

    /**
     * 场景部署设备列表 页面跳转
     *
     * @return
     */
    @RequestMapping(params = "list")
    public ModelAndView list(HttpServletRequest request) {
        return new ModelAndView("com/jeecg/scene/sceneDeviceDepolyList");
    }

    /**
     * easyui AJAX请求数据
     *
     * @param request
     * @param response
     * @param dataGrid
     * @param sceneDeviceDepoly
     */

    @RequestMapping(params = "datagrid")
    public void datagrid(SceneDeviceDepolyEntity sceneDeviceDepoly, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(SceneDeviceDepolyEntity.class, dataGrid);
        // 查询条件组装器
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sceneDeviceDepoly, request.getParameterMap());
        try {
            // 自定义追加查询条件

        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        cq.add();
        this.sceneDeviceDepolyService.getDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 删除场景部署设备
     *
     * @return
     */
    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(SceneDeviceDepolyEntity sceneDeviceDepoly, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        sceneDeviceDepoly = systemService.getEntity(SceneDeviceDepolyEntity.class, sceneDeviceDepoly.getId());
        message = "场景部署设备删除成功";
        try {
            sceneDeviceDepolyService.delete(sceneDeviceDepoly);
            systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "场景部署设备删除失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 批量删除场景部署设备
     *
     * @return
     */
    @RequestMapping(params = "doBatchDel")
    @ResponseBody
    public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "场景部署设备删除成功";
        try {
            for (String id : ids.split(",")) {
                SceneDeviceDepolyEntity sceneDeviceDepoly = systemService.getEntity(SceneDeviceDepolyEntity.class, id);
                sceneDeviceDepolyService.delete(sceneDeviceDepoly);
                systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "场景部署设备删除失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 添加场景部署设备
     *
     * @param sceneDeviceDepoly
     * @return
     */
    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(SceneDeviceDepolyEntity sceneDeviceDepoly, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "场景部署设备添加成功";
        try {
            Serializable id = sceneDeviceDepolyService.save(sceneDeviceDepoly);
            sceneDeviceDepoly.setId(id.toString());
            j.setObj(sceneDeviceDepoly);
            systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "场景部署设备添加失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 更新场景部署设备
     *
     * @param sceneDeviceDepoly
     * @return
     */
    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(SceneDeviceDepolyEntity sceneDeviceDepoly, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "场景部署设备更新成功";
        SceneDeviceDepolyEntity t = sceneDeviceDepolyService.get(SceneDeviceDepolyEntity.class, sceneDeviceDepoly.getId());
        try {
            MyBeanUtils.copyBeanNotNull2Bean(sceneDeviceDepoly, t);
            sceneDeviceDepolyService.saveOrUpdate(t);
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "场景部署设备更新失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    @RequestMapping(params = "checkCode")
    @ResponseBody
    public AjaxJson checkCode(SceneDeviceDepolyEntity entity, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        try {
            String hql = "from SceneDeviceDepolyEntity where 1=1 and sceneBy=? and deviceBy=? and deviceParentBy=? and deviceCode=?";
            List<SceneDeviceDepolyEntity> list = sceneDeviceDepolyService.findHql(hql, entity.getSceneBy(), entity.getDeviceBy(),entity.getDeviceParentBy(), entity.getDeviceCode());
            if (list == null || list.size() == 0) {
                j.setSuccess(true);
            } else {
                // 修改时，查询出自己
                if (StringUtil.isNotBlank(entity.getId()) && entity.getId().equals(list.get(0).getId())) {
                    j.setSuccess(true);
                } else {
                    j.setSuccess(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            message = "失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 场景部署设备新增页面跳转
     *
     * @return
     */
    @RequestMapping(params = "goAdd")
    public ModelAndView goAdd(SceneDeviceDepolyEntity sceneDeviceDepoly, HttpServletRequest req) {
        if (StringUtil.isNotEmpty(sceneDeviceDepoly.getId())) {
            sceneDeviceDepoly = sceneDeviceDepolyService.getEntity(SceneDeviceDepolyEntity.class, sceneDeviceDepoly.getId());
            req.setAttribute("sceneDeviceDepoly", sceneDeviceDepoly);
        } else {
            req.setAttribute("sceneDeviceDepoly", sceneDeviceDepoly);
        }
        return new ModelAndView("com/jeecg/scene/sceneDeviceDepoly-add");
    }

    /**
     * 场景部署设备编辑页面跳转
     *
     * @return
     */
    @RequestMapping(params = "goUpdate")
    public ModelAndView goUpdate(SceneDeviceDepolyEntity sceneDeviceDepoly, HttpServletRequest req) {
        if (StringUtil.isNotEmpty(sceneDeviceDepoly.getId())) {
            sceneDeviceDepoly = sceneDeviceDepolyService.getEntity(SceneDeviceDepolyEntity.class, sceneDeviceDepoly.getId());
            req.setAttribute("sceneDeviceDepoly", sceneDeviceDepoly);
        } else {
            req.setAttribute("sceneDeviceDepoly", sceneDeviceDepoly);
        }
        return new ModelAndView("com/jeecg/scene/sceneDeviceDepoly-update");
    }

    /**
     * 场景管理模块中，设备组网tree
     *
     * @param sceneBy
     * @param req
     * @return
     */
    @RequestMapping(params = "goDeployTree")
    public ModelAndView goDeployTree(String sceneBy, HttpServletRequest req) {
        if (StringUtil.isNotEmpty(sceneBy)) {
            List<SceneDeviceDepolyEntity> list = sceneDeviceDepolyService.findByProperty(SceneDeviceDepolyEntity.class, "sceneBy", sceneBy);
            req.setAttribute("sceneDeviceDepolyList", list);
        }
        req.setAttribute("sceneBy", sceneBy);
        return new ModelAndView("com/jeecg/scene/sceneDeviceDepoly-tree");
    }

    /**
     * 获取部署设备列表，根据场景和配置，表单父类设备下拉选调用
     *
     * @param entity
     * @param request
     * @return
     */
    @RequestMapping(params = "getParentList")
    @ResponseBody
    public AjaxJson getParentList(SceneDeviceDepolyEntity entity, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "场景部署设备获取成功";
        List<Map<String, Object>> list = null;
        try {
            String sql = "select sdd.id id, bd.name name, sdd.device_code code from jform_scene_device_depoly sdd, jform_base_device bd where 1=1 and sdd.device_by=bd.id and sdd.scene_by=?";
            list = systemService.findForJdbc(sql, entity.getSceneBy());
            JSONObject json = new JSONObject();
            json.put("value", list);
            json.put("code", 200);
            json.put("redirect", "");
            json.put("message", "");
            j.setMsg(message);
            j.setObj(json);
            return j;
        } catch (Exception e) {
            e.printStackTrace();
            message = "场景部署设备获取失败";
            throw new BusinessException(e.getMessage());
        }
    }

    @RequestMapping(params = "appendCRC")
    @ResponseBody
    public AjaxJson appendCRC(SceneDeviceDepolyEntity entity, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "追加CRC校验码成功！";
        try {
            if (StringUtil.isNotBlank(entity.getRequestCmd())) {
                String crc = CRCUtil.getCRC(HexConvertUtil.hexStringToBytes(entity.getRequestCmd()));
                crc = HexConvertUtil.BinaryToHexString(HexConvertUtil.hexStringToBytes(crc));
                entity.setRequestCmd(entity.getRequestCmd() + " " + crc);
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "追加CRC校验码失败";
        }
        j.setMsg(message);
        j.setObj(entity);
        return j;
    }

    /**
     * 导入功能跳转
     *
     * @return
     */
    @RequestMapping(params = "upload")
    public ModelAndView upload(HttpServletRequest req) {
        req.setAttribute("controller_name", "sceneDeviceDepolyController");
        return new ModelAndView("common/upload/pub_excel_upload");
    }

    /**
     * 导出excel
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "exportXls")
    public String exportXls(SceneDeviceDepolyEntity sceneDeviceDepoly, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, ModelMap modelMap) {
        CriteriaQuery cq = new CriteriaQuery(SceneDeviceDepolyEntity.class, dataGrid);
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sceneDeviceDepoly, request.getParameterMap());
        List<SceneDeviceDepolyEntity> sceneDeviceDepolys = this.sceneDeviceDepolyService.getListByCriteriaQuery(cq, false);
        modelMap.put(NormalExcelConstants.FILE_NAME, "场景部署设备");
        modelMap.put(NormalExcelConstants.CLASS, SceneDeviceDepolyEntity.class);
        modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("场景部署设备列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
        modelMap.put(NormalExcelConstants.DATA_LIST, sceneDeviceDepolys);
        return NormalExcelConstants.JEECG_EXCEL_VIEW;
    }

    /**
     * 导出excel 使模板
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "exportXlsByT")
    public String exportXlsByT(SceneDeviceDepolyEntity sceneDeviceDepoly, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, ModelMap modelMap) {
        modelMap.put(NormalExcelConstants.FILE_NAME, "场景部署设备");
        modelMap.put(NormalExcelConstants.CLASS, SceneDeviceDepolyEntity.class);
        modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("场景部署设备列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
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
                List<SceneDeviceDepolyEntity> listSceneDeviceDepolyEntitys = ExcelImportUtil.importExcel(file.getInputStream(), SceneDeviceDepolyEntity.class, params);
                for (SceneDeviceDepolyEntity sceneDeviceDepoly : listSceneDeviceDepolyEntitys) {
                    sceneDeviceDepolyService.save(sceneDeviceDepoly);
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
