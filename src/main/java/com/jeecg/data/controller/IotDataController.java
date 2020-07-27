package com.jeecg.data.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecg.data.entity.IotDataEntity;
import com.jeecg.data.service.IotDataServiceI;
import com.jeecg.device.entity.BaseDeviceDataEntity;
import com.jeecg.device.entity.BaseDeviceEntity;
import com.jeecg.device.service.BaseDeviceServiceI;
import com.jeecg.protocol.service.DataProtocolServiceI;
import com.jeecg.scene.entity.SceneDeviceDepolyEntity;
import com.jeecg.scene.entity.SceneEntity;
import com.jeecg.scene.service.SceneDeviceDepolyServiceI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.ExceptionUtil;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.jwt.util.ResponseMessage;
import org.jeecgframework.jwt.util.Result;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.DictEntity;
import org.jeecgframework.web.system.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.*;

/**
 * @author onlineGenerator
 * @version V1.0
 * @Title: Controller
 * @Description: 物联网数据
 * @date 2018-08-24 17:26:23
 */
@Api(value = "IotData", description = "物联网数据", tags = "iotDataController")
@Controller
@RequestMapping("/iotDataController")
public class IotDataController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(IotDataController.class);

    @Autowired
    private IotDataServiceI iotDataService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private Validator validator;
    @Autowired
    private SceneDeviceDepolyServiceI depolyService;
    @Autowired
    private BaseDeviceServiceI baseDeviceService;
    @Autowired
    private DataProtocolServiceI dataProtocolService;

    /**
     * 物联网数据列表 页面跳转
     *
     * @return
     */
    @RequestMapping(params = "list")
    public ModelAndView list(HttpServletRequest request) {
        return new ModelAndView("com/jeecg/data/iotDataList");
    }

    @RequestMapping(params = "goSite")
    public ModelAndView goSite(HttpServletRequest request) {
        return new ModelAndView("com/jeecg/data/iotDataSite");
    }

    @RequestMapping(params = "listByInsDevice")
    public void listByInsDevice(IotDataEntity iotData, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(IotDataEntity.class, dataGrid);
        // 查询条件组装器
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, iotData, request.getParameterMap());
        cq.add();
        this.iotDataService.getDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    @RequestMapping(params = "getDataList")
    @ResponseBody
    public AjaxJson getDataList(IotDataEntity iotData, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        JSONObject json = new JSONObject();
        message = "物联网数据获取成功";
        try {
            SceneDeviceDepolyEntity sdd = systemService.get(SceneDeviceDepolyEntity.class, iotData.getInstanceDeviceBy());
            BaseDeviceEntity bDevice = systemService.get(BaseDeviceEntity.class, sdd.getDeviceBy());
            json.put("id", bDevice.getId());
            json.put("name", bDevice.getName());
            json.put("code", sdd.getDeviceCode());
            List<BaseDeviceDataEntity> bddList = systemService.findByProperty(BaseDeviceDataEntity.class, "deviceBy", bDevice.getId());
            JSONArray echartArr = new JSONArray();
            for (BaseDeviceDataEntity e : bddList) {
                JSONObject feildJson = new JSONObject();
                feildJson.put("field", e.getField());
                feildJson.put("label", e.getName());
                feildJson.put("unit", e.getUnit());
                feildJson.put("dataRange", e.getDataRange());
                feildJson.put("normalDataRange", e.getNormalDataRange());

                // lineArr
                List<Map<String, Object>> dataList = baseDeviceService.listLineData(iotData.getInstanceDeviceBy(), iotData.getCreateDate(), e.getField());
                JSONArray lineArr = JSONArray.parseArray(JSON.toJSONString(dataList));
                feildJson.put("lineData", lineArr);

                // pieArr
                List<Map<String, Object>> pieList = baseDeviceService.listPieData(iotData.getInstanceDeviceBy(), iotData.getCreateDate(), e.getField());
                JSONArray pieArr = JSONArray.parseArray(JSON.toJSONString(pieList));
                feildJson.put("pieData", pieArr);
                echartArr.add(feildJson);
            }
            json.put("echartData", echartArr);
            // 设备状态信息：正常运行、设备故障
            // MqttUtil.sendMsg("$forallcn/iotcp/logger", message);
            systemService.addLog(message, Globals.Log_Type_UPLOAD, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "物联网数据获取失败";
            throw new BusinessException(e.getMessage());
        }
        j.setObj(json);
        j.setMsg(message);
        return j;
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
    public void datagrid(IotDataEntity iotData, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(IotDataEntity.class, dataGrid);
        // 查询条件组装器
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, iotData, request.getParameterMap());
        Map<String, Map<String, Object>> extMap = new HashMap<String, Map<String, Object>>();
        try {
            // 自定义追加查询条件
            cq.add();
            this.iotDataService.getDataGridReturn(cq, true);
            /* 追加其它参数 周文荣 2018/07/17 */
            extMap = this.iotDataService.apendParams(dataGrid);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        // 处理枚举数据类型
        for (IotDataEntity data : (List<IotDataEntity>) dataGrid.getResults()) {
            if ("enum".equals(data.getType())) {
                // 1、输入：应用设备id，基本设备的数据field
                // 2、输出：枚举数值
                // 应用设备
                String sql = "SELECT bdd.enu_range FROM jform_instance_device ind left join jform_base_device bd on ind.base_device_by=bd.id left join jform_base_device_data bdd on bd.id=bdd.device_by where 1=1 and bdd.field='" + data.getFieldBy() + "' and ind.id='"
                        + data.getInstanceDeviceBy() + "'";
                List<String> enumList = systemService.findListbySql(sql);
                if (enumList != null && enumList.size() > 0) {
                    List<DictEntity> list = systemService.queryDict("", enumList.get(0), "");
                    for (DictEntity dictEntity : list) {
                        if (dictEntity.getTypecode().equals(data.getData())) {
                            data.setData(dictEntity.getTypename());
                        }
                    }
                }
            }
        }
        TagUtil.datagrid(response, dataGrid, extMap);
    }

    /**
     * 删除物联网数据
     *
     * @return
     */
    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(IotDataEntity iotData, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        iotData = systemService.getEntity(IotDataEntity.class, iotData.getId());
        message = "物联网数据删除成功";
        try {
            iotDataService.delete(iotData);
            systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "物联网数据删除失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 批量删除物联网数据
     *
     * @return
     */
    @RequestMapping(params = "doBatchDel")
    @ResponseBody
    public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "物联网数据删除成功";
        try {
            for (String id : ids.split(",")) {
                IotDataEntity iotData = systemService.getEntity(IotDataEntity.class, id);
                iotDataService.delete(iotData);
                systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "物联网数据删除失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 添加物联网数据
     *
     * @param ids
     * @return
     */
    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(IotDataEntity iotData, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "物联网数据添加成功";
        try {
            iotDataService.save(iotData);
            systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "物联网数据添加失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 更新物联网数据
     *
     * @param ids
     * @return
     */
    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(IotDataEntity iotData, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "物联网数据更新成功";
        IotDataEntity t = iotDataService.get(IotDataEntity.class, iotData.getId());
        try {
            MyBeanUtils.copyBeanNotNull2Bean(iotData, t);
            iotDataService.saveOrUpdate(t);
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "物联网数据更新失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 物联网数据新增页面跳转
     *
     * @return
     */
    @RequestMapping(params = "goAdd")
    public ModelAndView goAdd(IotDataEntity iotData, HttpServletRequest req) {
        if (StringUtil.isNotEmpty(iotData.getId())) {
            iotData = iotDataService.getEntity(IotDataEntity.class, iotData.getId());
            req.setAttribute("iotData", iotData);
        }
        return new ModelAndView("com/jeecg/data/iotData-add");
    }

    /**
     * 物联网数据编辑页面跳转
     *
     * @return
     */
    @RequestMapping(params = "goUpdate")
    public ModelAndView goUpdate(IotDataEntity iotData, HttpServletRequest req) {
        if (StringUtil.isNotEmpty(iotData.getId())) {
            iotData = iotDataService.getEntity(IotDataEntity.class, iotData.getId());
            req.setAttribute("iotData", iotData);
        }
        return new ModelAndView("com/jeecg/data/iotData-update");
    }

    /**
     * 3D数据展示-所有场景可选择
     */
    @RequestMapping(params = "go3DSceneListData")
    public ModelAndView go3DSceneListData() {
        return new ModelAndView("com/jeecg/data/3DSceneListData");
    }

    /**
     * 3D数据展示-默认配置场景
     */
    @RequestMapping(params = "goSceneViewFrame")
    public ModelAndView goSceneViewFrame(SceneEntity scene, HttpServletRequest req) {
        if (StringUtil.isBlank(scene.getId())) {
            List<SceneEntity> sceneList = systemService.findByProperty(SceneEntity.class, "isDefaultView", true);
            if (sceneList != null && sceneList.size() > 0) {
                scene = systemService.get(SceneEntity.class, sceneList.get(0).getId());
            }
        } else {
            scene = systemService.get(SceneEntity.class, scene.getId());
        }
        JSONObject json = new JSONObject();
        json.put("id", scene.getId());
        json.put("name", scene.getName());
        json.put("modelFile3D", scene.getScene3d());
        json.put("threeData", JSONObject.parse(scene.getThreeData()));
        req.setAttribute("scene", json);
        return new ModelAndView("com/jeecg/data/sceneViewFrame");
    }

    @RequestMapping(params = "goDeviceDataView")
    public ModelAndView goDeviceDataView(String deployDeviceBy, HttpServletRequest req) {
        JSONObject json = new JSONObject();
        try {
            SceneDeviceDepolyEntity sdd = systemService.get(SceneDeviceDepolyEntity.class, deployDeviceBy);
            BaseDeviceEntity bDevice = systemService.get(BaseDeviceEntity.class, sdd.getDeviceBy());
            json.put("id", bDevice.getId());
            json.put("name", bDevice.getName());
            json.put("code", sdd.getDeviceCode());
            List<BaseDeviceDataEntity> bddList = systemService.findByProperty(BaseDeviceDataEntity.class, "deviceBy", bDevice.getId());
            json.put("deviceData", bddList);
            json.put("code", sdd.getDeviceCode());
            req.setAttribute("device", json);
            req.setAttribute("deployDeviceBy", deployDeviceBy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("com/jeecg/data/deviceDataView");
    }

    @RequestMapping(params = "goRunningLog")
    public ModelAndView goRunningLog() {
        return new ModelAndView("com/jeecg/data/runningLog");
    }

    /**
     * 导入功能跳转
     *
     * @return
     */
    @RequestMapping(params = "upload")
    public ModelAndView upload(HttpServletRequest req) {
        req.setAttribute("controller_name", "iotDataController");
        return new ModelAndView("common/upload/pub_excel_upload");
    }

    /**
     * 导出excel
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "exportXls")
    public String exportXls(IotDataEntity iotData, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, ModelMap modelMap) {
        CriteriaQuery cq = new CriteriaQuery(IotDataEntity.class, dataGrid);
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, iotData, request.getParameterMap());
        List<IotDataEntity> iotDatas = this.iotDataService.getListByCriteriaQuery(cq, false);
        modelMap.put(NormalExcelConstants.FILE_NAME, "物联网数据");
        modelMap.put(NormalExcelConstants.CLASS, IotDataEntity.class);
        modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("物联网数据列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
        modelMap.put(NormalExcelConstants.DATA_LIST, iotDatas);
        return NormalExcelConstants.JEECG_EXCEL_VIEW;
    }

    /**
     * 导出excel 使模板
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "exportXlsByT")
    public String exportXlsByT(IotDataEntity iotData, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, ModelMap modelMap) {
        modelMap.put(NormalExcelConstants.FILE_NAME, "物联网数据");
        modelMap.put(NormalExcelConstants.CLASS, IotDataEntity.class);
        modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("物联网数据列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
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
                List<IotDataEntity> listIotDataEntitys = ExcelImportUtil.importExcel(file.getInputStream(), IotDataEntity.class, params);
                for (IotDataEntity iotData : listIotDataEntitys) {
                    iotDataService.save(iotData);
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

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "物联网数据列表信息", produces = "application/json", httpMethod = "GET")
    public ResponseMessage<List<IotDataEntity>> list() {
        List<IotDataEntity> listIotDatas = iotDataService.getList(IotDataEntity.class);
        return Result.success(listIotDatas);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "根据ID获取物联网数据信息", notes = "根据ID获取物联网数据信息", httpMethod = "GET", produces = "application/json")
    public ResponseMessage<?> get(@ApiParam(required = true, name = "id", value = "ID") @PathVariable("id") String id) {
        IotDataEntity task = iotDataService.get(IotDataEntity.class, id);
        if (task == null) {
            return Result.error("根据ID获取物联网数据信息为空");
        }
        return Result.success(task);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiOperation(value = "创建物联网数据")
    public ResponseMessage<?> create(@ApiParam(name = "物联网数据对象") @RequestBody IotDataEntity iotData, UriComponentsBuilder uriBuilder) {
        // 调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
        Set<ConstraintViolation<IotDataEntity>> failures = validator.validate(iotData);
        if (!failures.isEmpty()) {
            return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
        }

        // 保存
        try {
            iotDataService.save(iotData);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("物联网数据信息保存失败");
        }
        return Result.success(iotData);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiOperation(value = "更新物联网数据", notes = "更新物联网数据")
    public ResponseMessage<?> update(@ApiParam(name = "物联网数据对象") @RequestBody IotDataEntity iotData) {
        // 调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
        Set<ConstraintViolation<IotDataEntity>> failures = validator.validate(iotData);
        if (!failures.isEmpty()) {
            return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
        }

        // 保存
        try {
            iotDataService.saveOrUpdate(iotData);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("更新物联网数据信息失败");
        }

        // 按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
        return Result.success("更新物联网数据信息成功");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @ApiOperation(value = "删除物联网数据")
    public ResponseMessage<?> delete(@ApiParam(name = "id", value = "ID", required = true) @PathVariable("id") String id) {
        logger.info("delete[{}]", id);
        // 验证
        if (StringUtils.isEmpty(id)) {
            return Result.error("ID不能为空");
        }
        try {
            iotDataService.deleteEntityById(IotDataEntity.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("物联网数据删除失败");
        }

        return Result.success();
    }
}
