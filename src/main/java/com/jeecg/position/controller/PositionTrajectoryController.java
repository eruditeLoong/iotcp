package com.jeecg.position.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecg.position.entity.PositionTrajectoryEntity;
import com.jeecg.position.service.PositionTrajectoryServiceI;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author onlineGenerator
 * @version V1.0
 * @Title: Controller
 * @Description: 定位数据
 * @date 2019-09-06 15:31:09
 */
@Api(value = "PositionTrajectory", description = "定位数据", tags = "positionTrajectoryController")
@Controller
@RequestMapping("/positionTrajectoryController")
public class PositionTrajectoryController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PositionTrajectoryController.class);

    @Autowired
    private PositionTrajectoryServiceI positionTrajectoryService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private Validator validator;


    @RequestMapping(params = "tabs")
    public ModelAndView tabs(HttpServletRequest request) {
        return new ModelAndView("com/jeecg/position/positionTrajectoryTabs");
    }

    /**
     * 定位数据列表 页面跳转
     *
     * @return
     */
    @RequestMapping(params = "list")
    public ModelAndView list(HttpServletRequest request) {
        return new ModelAndView("com/jeecg/position/positionTrajectoryList");
    }

    @RequestMapping(params = "maps")
    public ModelAndView maps(HttpServletRequest request) {
        request.setAttribute("imei", request.getParameter("imei").toString());
        return new ModelAndView("com/jeecg/position/positionTrajectoryMaps");
    }

    @RequestMapping(params = "getTrajectMaps")
    @ResponseBody
    public AjaxJson getTrajectMaps(PositionTrajectoryEntity positionTrajectory) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "定位数据获取成功";
        JSONArray arr = new JSONArray();
        if (StringUtil.isNotEmpty(positionTrajectory.getImei())) {
            String sql = "select latitude,longitude from position_trajectory where 1=1 and imei=? order by create_date asc";
            List<Map<String, Object>> list = positionTrajectoryService.findForJdbc(sql, positionTrajectory.getImei());
            for (Map<String, Object> pTrajectory : list) {
                JSONObject json = new JSONObject();
                json.put("latitude", pTrajectory.get("latitude"));
                json.put("longitude", pTrajectory.get("longitude"));
                arr.add(json);
            }
        }
        j.setObj(arr);
        j.setMsg(message);
        return j;
    }

    /**
     * easyui AJAX请求数据
     *
     * @param request
     * @param response
     * @param dataGrid
     */

    @RequestMapping(params = "datagrid")
    public void datagrid(PositionTrajectoryEntity positionTrajectory, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(PositionTrajectoryEntity.class, dataGrid);
        //查询条件组装器
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, positionTrajectory, request.getParameterMap());
        try {
            //自定义追加查询条件

        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        cq.add();
        this.positionTrajectoryService.getDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 删除定位数据
     *
     * @return
     */
    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(PositionTrajectoryEntity positionTrajectory, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        positionTrajectory = systemService.getEntity(PositionTrajectoryEntity.class, positionTrajectory.getId());
        message = "定位数据删除成功";
        try {
            positionTrajectoryService.delete(positionTrajectory);
            systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "定位数据删除失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 批量删除定位数据
     *
     * @return
     */
    @RequestMapping(params = "doBatchDel")
    @ResponseBody
    public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "定位数据删除成功";
        try {
            for (String id : ids.split(",")) {
                PositionTrajectoryEntity positionTrajectory = systemService.getEntity(PositionTrajectoryEntity.class,
                        id
                );
                positionTrajectoryService.delete(positionTrajectory);
                systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "定位数据删除失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }


    /**
     * 添加定位数据
     * @param positionTrajectory
     * @param request
     * @return
     */
    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(PositionTrajectoryEntity positionTrajectory, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "定位数据添加成功";
        try {
            positionTrajectoryService.save(positionTrajectory);
            systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "定位数据添加失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 更新定位数据
     * @param positionTrajectory
     * @param request
     * @return
     */
    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(PositionTrajectoryEntity positionTrajectory, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "定位数据更新成功";
        PositionTrajectoryEntity t = positionTrajectoryService.get(PositionTrajectoryEntity.class, positionTrajectory.getId());
        try {
            MyBeanUtils.copyBeanNotNull2Bean(positionTrajectory, t);
            positionTrajectoryService.saveOrUpdate(t);
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "定位数据更新失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }


    /**
     * 定位数据新增页面跳转
     *
     * @return
     */
    @RequestMapping(params = "goAdd")
    public ModelAndView goAdd(PositionTrajectoryEntity positionTrajectory, HttpServletRequest req) {
        if (StringUtil.isNotEmpty(positionTrajectory.getId())) {
            positionTrajectory = positionTrajectoryService.getEntity(PositionTrajectoryEntity.class, positionTrajectory.getId());
            req.setAttribute("positionTrajectory", positionTrajectory);
        }
        return new ModelAndView("com/jeecg/position/positionTrajectory-add");
    }

    /**
     * 定位数据编辑页面跳转
     *
     * @return
     */
    @RequestMapping(params = "goUpdate")
    public ModelAndView goUpdate(PositionTrajectoryEntity positionTrajectory, HttpServletRequest req) {
        if (StringUtil.isNotEmpty(positionTrajectory.getId())) {
            positionTrajectory = positionTrajectoryService.getEntity(PositionTrajectoryEntity.class, positionTrajectory.getId());
            req.setAttribute("positionTrajectory", positionTrajectory);
        }
        return new ModelAndView("com/jeecg/position/positionTrajectory-update");
    }

    @RequestMapping(params = "goPosition")
    public ModelAndView goPosition(PositionTrajectoryEntity positionTrajectory, HttpServletRequest req) {
        if (StringUtil.isNotEmpty(positionTrajectory.getId())) {
            positionTrajectory = positionTrajectoryService.getEntity(PositionTrajectoryEntity.class, positionTrajectory.getId());
            req.setAttribute("positionTrajectory", positionTrajectory);
        }
        return new ModelAndView("com/jeecg/position/positionTrajectory-map");
    }

    @RequestMapping(params = "goHisTraject3D")
    public ModelAndView goHisTraject3D(HttpServletRequest req) {
        return new ModelAndView("com/jeecg/position/positionTrajectory3D");
    }

    /**
     * 导入功能跳转
     *
     * @return
     */
    @RequestMapping(params = "upload")
    public ModelAndView upload(HttpServletRequest req) {
        req.setAttribute("controller_name", "positionTrajectoryController");
        return new ModelAndView("common/upload/pub_excel_upload");
    }

    /**
     * 导出excel
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "exportXls")
    public String exportXls(PositionTrajectoryEntity positionTrajectory, HttpServletRequest request, HttpServletResponse response
            , DataGrid dataGrid, ModelMap modelMap) {
        CriteriaQuery cq = new CriteriaQuery(PositionTrajectoryEntity.class, dataGrid);
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, positionTrajectory, request.getParameterMap());
        List<PositionTrajectoryEntity> positionTrajectorys = this.positionTrajectoryService.getListByCriteriaQuery(cq, false);
        modelMap.put(NormalExcelConstants.FILE_NAME, "定位数据");
        modelMap.put(NormalExcelConstants.CLASS, PositionTrajectoryEntity.class);
        modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("定位数据列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(),
                "导出信息"));
        modelMap.put(NormalExcelConstants.DATA_LIST, positionTrajectorys);
        return NormalExcelConstants.JEECG_EXCEL_VIEW;
    }

    /**
     * 导出excel 使模板
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "exportXlsByT")
    public String exportXlsByT(PositionTrajectoryEntity positionTrajectory, HttpServletRequest request, HttpServletResponse response
            , DataGrid dataGrid, ModelMap modelMap) {
        modelMap.put(NormalExcelConstants.FILE_NAME, "定位数据");
        modelMap.put(NormalExcelConstants.CLASS, PositionTrajectoryEntity.class);
        modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("定位数据列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(),
                "导出信息"));
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
                List<PositionTrajectoryEntity> listPositionTrajectoryEntitys = ExcelImportUtil.importExcel(file.getInputStream(), PositionTrajectoryEntity.class, params);
                for (PositionTrajectoryEntity positionTrajectory : listPositionTrajectoryEntitys) {
                    positionTrajectoryService.save(positionTrajectory);
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
    @ApiOperation(value = "定位数据列表信息", produces = "application/json", httpMethod = "GET")
    public ResponseMessage<List<PositionTrajectoryEntity>> list(@PathVariable("pageNo") int pageNo, @PathVariable("pageSize") int pageSize, HttpServletRequest request) {
        if (pageSize > Globals.MAX_PAGESIZE) {
            return Result.error("每页请求不能超过" + Globals.MAX_PAGESIZE + "条");
        }
        CriteriaQuery query = new CriteriaQuery(PositionTrajectoryEntity.class);
        query.setCurPage(pageNo <= 0 ? 1 : pageNo);
        query.setPageSize(pageSize < 1 ? 1 : pageSize);
        List<PositionTrajectoryEntity> listPositionTrajectorys = this.positionTrajectoryService.getListByCriteriaQuery(query, true);
        return Result.success(listPositionTrajectorys);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "根据ID获取定位数据信息", notes = "根据ID获取定位数据信息", httpMethod = "GET", produces = "application/json")
    public ResponseMessage<?> get(@ApiParam(required = true, name = "id", value = "ID") @PathVariable("id") String id) {
        PositionTrajectoryEntity task = positionTrajectoryService.get(PositionTrajectoryEntity.class, id);
        if (task == null) {
            return Result.error("根据ID获取定位数据信息为空");
        }
        return Result.success(task);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiOperation(value = "创建定位数据")
    public ResponseMessage<?> create(@ApiParam(name = "定位数据对象") @RequestBody PositionTrajectoryEntity positionTrajectory, UriComponentsBuilder uriBuilder) {
        //调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
        Set<ConstraintViolation<PositionTrajectoryEntity>> failures = validator.validate(positionTrajectory);
        if (!failures.isEmpty()) {
            return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
        }

        //保存
        try {
            positionTrajectoryService.save(positionTrajectory);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("定位数据信息保存失败");
        }
        return Result.success(positionTrajectory);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiOperation(value = "更新定位数据", notes = "更新定位数据")
    public ResponseMessage<?> update(@ApiParam(name = "定位数据对象") @RequestBody PositionTrajectoryEntity positionTrajectory) {
        //调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
        Set<ConstraintViolation<PositionTrajectoryEntity>> failures = validator.validate(positionTrajectory);
        if (!failures.isEmpty()) {
            return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
        }

        //保存
        try {
            positionTrajectoryService.saveOrUpdate(positionTrajectory);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("更新定位数据信息失败");
        }

        //按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
        return Result.success("更新定位数据信息成功");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "删除定位数据")
    public ResponseMessage<?> delete(@ApiParam(name = "id", value = "ID", required = true) @PathVariable("id") String id) {
        logger.info("delete[{}]", id);
        // 验证
        if (StringUtils.isEmpty(id)) {
            return Result.error("ID不能为空");
        }
        try {
            positionTrajectoryService.deleteEntityById(PositionTrajectoryEntity.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("定位数据删除失败");
        }

        return Result.success();
    }
}
