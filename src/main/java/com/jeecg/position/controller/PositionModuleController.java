package com.jeecg.position.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecg.position.entity.PositionModuleConfigEntity;
import com.jeecg.position.entity.PositionModuleEntity;
import com.jeecg.position.service.PositionModuleConfigServiceI;
import com.jeecg.position.service.PositionModuleServiceI;
import com.sun.star.uno.RuntimeException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.*;
import org.jeecgframework.jwt.util.ResponseMessage;
import org.jeecgframework.jwt.util.Result;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
 * @Description: 定位模块信息
 * @date 2019-09-05 12:43:50
 */
@Api(value = "PositionModule", description = "定位模块信息", tags = "positionModuleController")
@Controller
@RequestMapping("/positionModuleController")
public class PositionModuleController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PositionModuleController.class);

    @Autowired
    private PositionModuleServiceI positionModuleService;
    @Autowired
    private PositionModuleConfigServiceI positionModuleConfigService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private Validator validator;

    /**
     * 定位模块信息列表 页面跳转
     *
     * @return
     */
    @RequestMapping(params = "list")
    public ModelAndView list(HttpServletRequest request) {
        return new ModelAndView("com/jeecg/position/positionModuleList");
    }

    /**
     * easyui AJAX请求数据
     *
     * @param positionModule
     * @param request
     * @param response
     * @param dataGrid
     */

    @RequestMapping(params = "datagrid")
    public void datagrid(PositionModuleEntity positionModule, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(PositionModuleEntity.class, dataGrid);
        // 查询条件组装器
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, positionModule, request.getParameterMap());
        try {
            // 自定义追加查询条件

        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        cq.add();
        this.positionModuleService.getDataGridReturn(cq, true);
        List<PositionModuleEntity> list = dataGrid.getResults();
        for (PositionModuleEntity pModule : list) {
            IoSession session = positionModuleConfigService.getIoSession(pModule.getIpPort());
            if (session != null) {
                pModule.setOnlineStatus(1);
            } else {
                pModule.setOnlineStatus(0);
            }
        }
        TagUtil.datagrid(response, dataGrid);
    }

    @RequestMapping(params = "getModules")
    @ResponseBody
    public AjaxJson getModules() {
        String message = null;
        AjaxJson j = new AjaxJson();
        String sql = "SELECT * FROM position_module where 1=1 and is_valid=? and id not in (SELECT imei FROM position_user)";
        List<Map<String, Object>> mlist = systemService.findForJdbc(sql, 1);
        message = "获取定位卡列表成功";
        try {
            JSONArray jsonArr = new JSONArray();
            for (Map<String, Object> map : mlist) {
                JSONObject json = (JSONObject) JSONObject.toJSON(map);
                jsonArr.add(json);
            }
            j.setObj(jsonArr);
        } catch (Exception e) {
            e.printStackTrace();
            message = "获取定位卡列表失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 删除定位模块信息
     *
     * @return
     */
    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(PositionModuleEntity positionModule, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        positionModule = systemService.getEntity(PositionModuleEntity.class, positionModule.getId());
        message = "定位模块信息删除成功";
        try {
            positionModuleService.delete(positionModule);
            systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "定位模块信息删除失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 批量删除定位模块信息
     *
     * @return
     */
    @RequestMapping(params = "doBatchDel")
    @ResponseBody
    public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "定位模块信息删除成功";
        try {
            for (String id : ids.split(",")) {
                PositionModuleEntity positionModule = systemService.getEntity(PositionModuleEntity.class, id);
                positionModuleService.delete(positionModule);
                systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "定位模块信息删除失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 添加定位模块信息
     *
     * @param positionModule
     * @return
     */
    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(PositionModuleEntity positionModule, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "定位模块信息添加成功";
        try {
            positionModule.setUpdateDate(new Date());
            positionModuleService.save(positionModule);
            systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "定位模块信息添加失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 更新定位模块信息
     *
     * @param positionModule
     * @return
     */
    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(PositionModuleEntity positionModule, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "定位模块信息更新成功";
        PositionModuleEntity t = positionModuleService.get(PositionModuleEntity.class, positionModule.getId());
        try {
            MyBeanUtils.copyBeanNotNull2Bean(positionModule, t);
            positionModuleService.saveOrUpdate(t);
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "定位模块信息更新失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 重置IoSession缓存
     *
     * @param positionModule
     * @return
     */
    @RequestMapping(params = "resetIoSession")
    @ResponseBody
    public AjaxJson resetIoSession(PositionModuleEntity positionModule, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        positionModuleConfigService.resetIoSession();
        j.setMsg("重置成功");
        j.setSuccess(true);
        return j;
    }

    /**
     * 批量修改模块后台服务器地址
     *
     * @param ids
     * @param service
     * @param request
     * @return
     */
    @RequestMapping(params = "doBatchConfService")
    @ResponseBody
    public AjaxJson doBatchConfService(String ids, String service, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "批量修改服务器成功";
        try {
            String[] ids_ = ids.split(",");
            for (String id : ids_) {
                PositionModuleEntity pModule = systemService.get(PositionModuleEntity.class, id);
                if (pModule != null) {
                    IoSession session = positionModuleConfigService.getIoSession(pModule.getIpPort());
                    positionModuleConfigService.confService(session, service);
                    positionModuleConfigService.restartModule(session, 1);
                }
            }
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "批量修改服务器失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 定位模块信息新增页面跳转
     *
     * @return
     */
    @RequestMapping(params = "goAdd")
    public ModelAndView goAdd(PositionModuleEntity positionModule, HttpServletRequest req) {
        if (StringUtil.isNotEmpty(positionModule.getId())) {
            positionModule = positionModuleService.getEntity(PositionModuleEntity.class, positionModule.getId());
            req.setAttribute("positionModule", positionModule);
        }
        return new ModelAndView("com/jeecg/position/positionModule-add");
    }

    /**
     * 定位模块信息编辑页面跳转
     *
     * @return
     */
    @RequestMapping(params = "goUpdate")
    public ModelAndView goUpdate(PositionModuleEntity positionModule, HttpServletRequest req) {
        if (StringUtil.isNotEmpty(positionModule.getId())) {
            positionModule = positionModuleService.getEntity(PositionModuleEntity.class, positionModule.getId());
            req.setAttribute("positionModule", positionModule);
        }
        return new ModelAndView("com/jeecg/position/positionModule-update");
    }

    @RequestMapping(params = "goConfig")
    public ModelAndView goConfig(PositionModuleEntity positionModule, HttpServletRequest req) {
        PositionModuleConfigEntity positionModuleConfig = systemService.get(PositionModuleConfigEntity.class, positionModule.getId());
        if (positionModuleConfig == null) {
            positionModuleConfig = new PositionModuleConfigEntity();
            positionModuleConfig.setImei(positionModule.getId());
        }
        req.setAttribute("positionModuleConfig", positionModuleConfig);
        return new ModelAndView("com/jeecg/position/positionModule-config");
    }

    @RequestMapping(params = "goEventCount")
    public ModelAndView goCount(PositionModuleEntity positionModule, HttpServletRequest req) {
        req.setAttribute("imei", positionModule.getId());
        return new ModelAndView("com/jeecg/position/positionModule-event");
    }

    @RequestMapping(params = "getEventCount")
    @ResponseBody
    public AjaxJson getEventCount(String imei, HttpServletRequest req) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "获取定位事件成功";
        String sql = "select name, code, count(code) as count from position_event where 1=1 and imei=? group by code";
        List<Map<String, Object>> list = systemService.findForJdbc(sql, imei);
        JSONArray arr = new JSONArray();
        for (Map<String, Object> map : list) {
            JSONObject json = new JSONObject();
            json.put("code", map.get("code"));
            json.put("name", map.get("name"));
            json.put("count", map.get("count"));
            arr.add(json);
        }
        j.setMsg(message);
        j.setObj(arr);
        return j;
    }

    @RequestMapping(params = "getOnlineCount")
    @ResponseBody
    public AjaxJson getOnlineCount() {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = positionModuleConfigService.getIoSessionTotal().toString();
        j.setMsg(message);
        return j;
    }


    @RequestMapping(params = "setting")
    @ResponseBody
    public AjaxJson setting(PositionModuleConfigEntity pModuleConf, HttpServletRequest req) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "";
        PositionModuleEntity pModule = systemService.get(PositionModuleEntity.class, pModuleConf.getImei());
        if(pModule!=null){
            IoSession session = positionModuleConfigService.getIoSession(pModule.getIpPort());
            if (session != null && session.isConnected()) {
                try {
                    this.sendCmds(session, pModuleConf);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        j.setMsg(message);
        return j;
    }

//    @RequestMapping(params = "setting")
//    @ResponseBody
//    public AjaxJson setting(String imei, String sCmd, HttpServletRequest req) {
//        String message = null;
//        AjaxJson j = new AjaxJson();
//        message = "";
//        byte[] bCmd = HexConvertUtil.hexStringToBytes(sCmd);
//        System.out.println(sCmd);
//        IoSession session = this.getIoSession(imei);
//        System.out.println(session);
//        if (session != null && session.isConnected()) {
//            session.write(IoBuffer.wrap(bCmd));
//            message = "命令发送成功";
//            List<PositionModuleConfigEntity> list = systemService.findByProperty(PositionModuleConfigEntity.class, "imei", imei);
//            if (list != null && list.size() == 1) {
//                // 修改
//
//            } else {
//                // 添加
//
//            }
//        } else {
//            message = "失败";
//        }
//        j.setMsg(message);
//        return j;
//    }

    private void sendCmds(IoSession session, PositionModuleConfigEntity pModuleConf) throws Exception {
        byte[] bCmd = null;
        if (pModuleConf.getAutoLocalTime() != null) {
            bCmd = HexConvertUtil.hexStringToBytes("78 78 03 97 00 00 0D 0A");
            bCmd[4] = (byte) (pModuleConf.getAutoLocalTime().byteValue() & 0xff00);
            bCmd[5] = (byte) (pModuleConf.getAutoLocalTime().byteValue() & 0x00ff);
            WriteFuture future = session.write(IoBuffer.wrap(bCmd));
            if (!future.isWritten()) {
                throw new RuntimeException("定位数据上传间隔设置失败！");
            }
        }
        if (pModuleConf.getPositionMode() != null) {
            bCmd = HexConvertUtil.hexStringToBytes("78 78 02 B0 00 0D 0A");
            bCmd[4] = (byte) (pModuleConf.getPositionMode().byteValue() & 0xff);
            WriteFuture future = session.write(IoBuffer.wrap(bCmd));
            if (!future.isWritten()) {
                throw new RuntimeException("定位模式设置失败！");
            }
        }
    }

    /**
     * 导入功能跳转
     *
     * @return
     */
    @RequestMapping(params = "upload")
    public ModelAndView upload(HttpServletRequest req) {
        req.setAttribute("controller_name", "positionModuleController");
        return new ModelAndView("common/upload/pub_excel_upload");
    }

    /**
     * 导出excel
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "exportXls")
    public String exportXls(PositionModuleEntity positionModule, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid,
                            ModelMap modelMap) {
        CriteriaQuery cq = new CriteriaQuery(PositionModuleEntity.class, dataGrid);
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, positionModule, request.getParameterMap());
        List<PositionModuleEntity> positionModules = this.positionModuleService.getListByCriteriaQuery(cq, false);
        modelMap.put(NormalExcelConstants.FILE_NAME, "定位模块信息");
        modelMap.put(NormalExcelConstants.CLASS, PositionModuleEntity.class);
        modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("定位模块信息列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
        modelMap.put(NormalExcelConstants.DATA_LIST, positionModules);
        return NormalExcelConstants.JEECG_EXCEL_VIEW;
    }

    /**
     * 导出excel 使模板
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "exportXlsByT")
    public String exportXlsByT(PositionModuleEntity positionModule, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid,
                               ModelMap modelMap) {
        modelMap.put(NormalExcelConstants.FILE_NAME, "定位模块信息");
        modelMap.put(NormalExcelConstants.CLASS, PositionModuleEntity.class);
        modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("定位模块信息列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
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
                List<PositionModuleEntity> listPositionModuleEntitys = ExcelImportUtil.importExcel(file.getInputStream(), PositionModuleEntity.class, params);
                for (PositionModuleEntity positionModule : listPositionModuleEntitys) {
                    positionModuleService.save(positionModule);
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

    @RequestMapping(value = "/list/{pageNo}/{pageSize}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "定位模块信息列表信息", produces = "application/json", httpMethod = "GET")
    public ResponseMessage<List<PositionModuleEntity>> list(@PathVariable("pageNo") int pageNo, @PathVariable("pageSize") int pageSize,
                                                            HttpServletRequest request) {
        if (pageSize > Globals.MAX_PAGESIZE) {
            return Result.error("每页请求不能超过" + Globals.MAX_PAGESIZE + "条");
        }
        CriteriaQuery query = new CriteriaQuery(PositionModuleEntity.class);
        query.setCurPage(pageNo <= 0 ? 1 : pageNo);
        query.setPageSize(pageSize < 1 ? 1 : pageSize);
        List<PositionModuleEntity> listPositionModules = this.positionModuleService.getListByCriteriaQuery(query, true);
        return Result.success(listPositionModules);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "根据ID获取定位模块信息信息", notes = "根据ID获取定位模块信息信息", httpMethod = "GET", produces = "application/json")
    public ResponseMessage<?> get(@ApiParam(required = true, name = "id", value = "ID") @PathVariable("id") String id) {
        PositionModuleEntity task = positionModuleService.get(PositionModuleEntity.class, id);
        if (task == null) {
            return Result.error("根据ID获取定位模块信息信息为空");
        }
        return Result.success(task);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiOperation(value = "创建定位模块信息")
    public ResponseMessage<?> create(@ApiParam(name = "定位模块信息对象") @RequestBody PositionModuleEntity positionModule, UriComponentsBuilder uriBuilder) {
        // 调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
        Set<ConstraintViolation<PositionModuleEntity>> failures = validator.validate(positionModule);
        if (!failures.isEmpty()) {
            return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
        }

        // 保存
        try {
            positionModuleService.save(positionModule);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("定位模块信息信息保存失败");
        }
        return Result.success(positionModule);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiOperation(value = "更新定位模块信息", notes = "更新定位模块信息")
    public ResponseMessage<?> update(@ApiParam(name = "定位模块信息对象") @RequestBody PositionModuleEntity positionModule) {
        // 调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
        Set<ConstraintViolation<PositionModuleEntity>> failures = validator.validate(positionModule);
        if (!failures.isEmpty()) {
            return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
        }

        // 保存
        try {
            positionModuleService.saveOrUpdate(positionModule);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("更新定位模块信息信息失败");
        }

        // 按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
        return Result.success("更新定位模块信息信息成功");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "删除定位模块信息")
    public ResponseMessage<?> delete(@ApiParam(name = "id", value = "ID", required = true) @PathVariable("id") String id) {
        logger.info("delete[{}]", id);
        // 验证
        if (StringUtils.isEmpty(id)) {
            return Result.error("ID不能为空");
        }
        try {
            positionModuleService.deleteEntityById(PositionModuleEntity.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("定位模块信息删除失败");
        }

        return Result.success();
    }
}
