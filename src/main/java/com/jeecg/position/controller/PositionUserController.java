package com.jeecg.position.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecg.nio.mina.position.GPSServerHandler;
import com.jeecg.position.entity.PositionModuleEntity;
import com.jeecg.position.entity.PositionUserEntity;
import com.jeecg.position.entity.PositionUserOrgEntity;
import com.jeecg.position.service.PositionModuleConfigServiceI;
import com.jeecg.position.service.PositionTrajectoryServiceI;
import com.jeecg.position.service.PositionUserServiceI;
import com.jeecg.position.utils.FenceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.buffer.IoBuffer;
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
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author onlineGenerator
 * @version V1.0
 * @Title: Controller
 * @Description: 人员管理
 * @date 2019-09-20 15:39:01
 */
@Api(value = "PositionUser", description = "人员管理", tags = "positionUserController")
@Controller
@RequestMapping("/positionUserController")
public class PositionUserController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PositionUserController.class);

    @Autowired
    private PositionUserServiceI positionUserService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private Validator validator;
    @Autowired
    private PositionTrajectoryServiceI positionTrajectoryService;
    @Autowired
    private PositionModuleConfigServiceI pmConfSevice;


    /**
     * 人员管理列表 页面跳转
     *
     * @return
     */
    @RequestMapping(params = "list")
    public ModelAndView list(HttpServletRequest request) {
        return new ModelAndView("com/jeecg/position/positionUserList");
    }

    /**
     * easyui AJAX请求数据
     *
     * @param positionUser
     * @param request
     * @param response
     * @param dataGrid
     */
    @RequestMapping(params = "datagrid")
    public void datagrid(PositionUserEntity positionUser, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(PositionUserEntity.class, dataGrid);
        //查询条件组装器
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, positionUser, request.getParameterMap());
        Map<String, Map<String, Object>> extMap = new HashMap<>();
        try {
            //自定义追加查询条件

        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        cq.add();
        this.positionUserService.getDataGridReturn(cq, true);
        List<PositionUserEntity> users = dataGrid.getResults();
        for (PositionUserEntity user : users) {
            Map<String, Object> map = new HashMap<>();
            PositionUserOrgEntity org = systemService.get(PositionUserOrgEntity.class, user.getCompany());
            map.put("color", org == null ? "#808080" : org.getColor());
            extMap.put(user.getId(), map);
        }
        TagUtil.datagrid(response, dataGrid, extMap);
    }

    @RequestMapping(params = "getUsers")
    @ResponseBody
    public AjaxJson getUsers() {
        AjaxJson j = new AjaxJson();
        String msg = "人员定位信息加载成功！";
        List<PositionUserEntity> users = positionUserService.getList(PositionUserEntity.class);
        JSONArray arr = new JSONArray();
        for (PositionUserEntity u : users) {
            JSONObject uJson = new JSONObject();
            uJson.put("id", u.getId());
            uJson.put("age", u.getAge());
            PositionUserOrgEntity org = systemService.get(PositionUserOrgEntity.class, u.getCompany());
            uJson.put("color", org != null ? org.getColor() : "#808080");
            uJson.put("companyName", org != null ? org.getName() : "");
            uJson.put("company", u.getCompany());
            uJson.put("createDate", u.getCreateDate());
            uJson.put("imei", u.getImei());
            uJson.put("name", u.getName());
            uJson.put("sex", u.getSex());
            uJson.put("status", u.getStatus() == 1 ? true : false);
            uJson.put("telephone", u.getTelephone());
            uJson.put("useObject", u.getUseObject());
            List<Map<String, Object>> list = systemService.findForJdbc("select longitude, latitude, create_date from position_trajectory where 1=1 and imei=? order by create_date desc", u.getImei());
            if (list.size() > 0) {
                double lon = (double) list.get(0).get("longitude");
                double lat = (double) list.get(0).get("latitude");
                double[] xy = positionTrajectoryService.lonlat2three(lon, lat);
                uJson.put("x", xy[0]);
                uJson.put("y", xy[1]);
                List<Point2D.Double> points = positionTrajectoryService.getDefaultSceneFence();
                boolean isInPoly = FenceUtil.contains(points, new Point2D.Double(uJson.getDouble("x"), uJson.getDouble("y")));
                uJson.put("datetime", ((Date) (list.get(0).get("create_date"))).getTime());
                if ((new Date().getTime() - ((Date) (list.get(0).get("create_date"))).getTime()) > 1000 * 60 * 10) {
                    // 如果定位数据在10分钟之前，隐藏不显示
                    isInPoly = false;
                }
                uJson.put("isInPoly", isInPoly);
            } else {
                uJson.put("x", 0);
                uJson.put("y", 0);
                uJson.put("datetime", new Date().getTime());
                uJson.put("isInPoly", false);
            }
            arr.add(uJson);
        }
        j.setObj(arr);
        j.setMsg(msg);
        return j;
    }

    @RequestMapping(params = "manualPosition")
    @ResponseBody
    public AjaxJson manualPosition(String imei) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "手动定位请求成功";
        try {
            PositionModuleEntity pModule = systemService.get(PositionModuleEntity.class, imei);
            if (pModule != null) {
                IoSession session = pmConfSevice.getIoSession(pModule.getIpPort());
                if (session != null) {
                    byte[] bmp = HexConvertUtil.hexStringToBytes("78 78 01 80 0D 0A");
                    session.write(IoBuffer.wrap(bmp));
                    logger.info(message);
                } else {
                    ConcurrentHashMap<String, IoSession> sMap = GPSServerHandler.sessionsConcurrentHashMap;
                    sMap.remove(pModule.getIpPort().split(":")[0]);
                    message = "IoSession null";
                    logger.error(message);
                }
            } else {
                message = "找不到模块：" + imei;
                logger.error(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "手动定位请求失败";
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 删除人员管理
     *
     * @return
     */
    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(PositionUserEntity positionUser, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        positionUser = systemService.getEntity(PositionUserEntity.class, positionUser.getId());
        message = "人员管理删除成功";
        try {
            positionUserService.delete(positionUser);
            systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "人员管理删除失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 批量删除人员管理
     *
     * @return
     */
    @RequestMapping(params = "doBatchDel")
    @ResponseBody
    public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "人员管理删除成功";
        try {
            for (String id : ids.split(",")) {
                PositionUserEntity positionUser = systemService.getEntity(PositionUserEntity.class,
                        id
                );
                positionUserService.delete(positionUser);
                systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "人员管理删除失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }


    /**
     * 添加人员管理
     *
     * @param positionUser
     * @return
     */
    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(PositionUserEntity positionUser, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "人员管理添加成功";
        try {
            positionUserService.save(positionUser);
            systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "人员管理添加失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 更新人员管理
     *
     * @param positionUser
     * @return
     */
    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(PositionUserEntity positionUser, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "人员管理更新成功";
        PositionUserEntity t = positionUserService.get(PositionUserEntity.class, positionUser.getId());
        try {
            MyBeanUtils.copyBeanNotNull2Bean(positionUser, t);
            positionUserService.saveOrUpdate(t);
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "人员管理更新失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    @RequestMapping(params = "doBatchSetStatus")
    @ResponseBody
    public AjaxJson doBatchSetStatus(String ids, Integer status, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "批量修改状态成功";
        try {
            String[] ids_ = ids.split(",");
            for (String id : ids_) {
                PositionUserEntity positionUser = positionUserService.get(PositionUserEntity.class, id);
                positionUser.setStatus(status);
                positionUserService.saveOrUpdate(positionUser);
            }
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "批量修改状态失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 人员管理新增页面跳转
     *
     * @return
     */
    @RequestMapping(params = "goAdd")
    public ModelAndView goAdd(PositionUserEntity positionUser, HttpServletRequest req) {
        if (StringUtil.isNotEmpty(positionUser.getId())) {
            positionUser = positionUserService.getEntity(PositionUserEntity.class, positionUser.getId());
            req.setAttribute("positionUser", positionUser);
        }
        return new ModelAndView("com/jeecg/position/positionUser-add");
    }

    /**
     * 人员管理编辑页面跳转
     *
     * @return
     */
    @RequestMapping(params = "goUpdate")
    public ModelAndView goUpdate(PositionUserEntity positionUser, HttpServletRequest req) {
        if (StringUtil.isNotEmpty(positionUser.getId())) {
            positionUser = positionUserService.getEntity(PositionUserEntity.class, positionUser.getId());
            req.setAttribute("positionUser", positionUser);
        }
        return new ModelAndView("com/jeecg/position/positionUser-update");
    }

    /**
     * 导入功能跳转
     *
     * @return
     */
    @RequestMapping(params = "upload")
    public ModelAndView upload(HttpServletRequest req) {
        req.setAttribute("controller_name", "positionUserController");
        return new ModelAndView("common/upload/pub_excel_upload");
    }

    /**
     * 导出excel
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "exportXls")
    public String exportXls(PositionUserEntity positionUser, HttpServletRequest request, HttpServletResponse response
            , DataGrid dataGrid, ModelMap modelMap) {
        CriteriaQuery cq = new CriteriaQuery(PositionUserEntity.class, dataGrid);
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, positionUser, request.getParameterMap());
        List<PositionUserEntity> positionUsers = this.positionUserService.getListByCriteriaQuery(cq, false);
        modelMap.put(NormalExcelConstants.FILE_NAME, "人员管理");
        modelMap.put(NormalExcelConstants.CLASS, PositionUserEntity.class);
        modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("人员管理列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(),
                "导出信息"));
        modelMap.put(NormalExcelConstants.DATA_LIST, positionUsers);
        return NormalExcelConstants.JEECG_EXCEL_VIEW;
    }

    /**
     * 导出excel 使模板
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "exportXlsByT")
    public String exportXlsByT(PositionUserEntity positionUser, HttpServletRequest request, HttpServletResponse response
            , DataGrid dataGrid, ModelMap modelMap) {
        modelMap.put(NormalExcelConstants.FILE_NAME, "人员管理");
        modelMap.put(NormalExcelConstants.CLASS, PositionUserEntity.class);
        modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("人员管理列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(),
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
                List<PositionUserEntity> listPositionUserEntitys = ExcelImportUtil.importExcel(file.getInputStream(), PositionUserEntity.class, params);
                for (PositionUserEntity positionUser : listPositionUserEntitys) {
                    positionUserService.save(positionUser);
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
    @ApiOperation(value = "人员管理列表信息", produces = "application/json", httpMethod = "GET")
    public ResponseMessage<List<PositionUserEntity>> list(@PathVariable("pageNo") int pageNo, @PathVariable("pageSize") int pageSize, HttpServletRequest request) {
        if (pageSize > Globals.MAX_PAGESIZE) {
            return Result.error("每页请求不能超过" + Globals.MAX_PAGESIZE + "条");
        }
        CriteriaQuery query = new CriteriaQuery(PositionUserEntity.class);
        query.setCurPage(pageNo <= 0 ? 1 : pageNo);
        query.setPageSize(pageSize < 1 ? 1 : pageSize);
        List<PositionUserEntity> listPositionUsers = this.positionUserService.getListByCriteriaQuery(query, true);
        return Result.success(listPositionUsers);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "根据ID获取人员管理信息", notes = "根据ID获取人员管理信息", httpMethod = "GET", produces = "application/json")
    public ResponseMessage<?> get(@ApiParam(required = true, name = "id", value = "ID") @PathVariable("id") String id) {
        PositionUserEntity task = positionUserService.get(PositionUserEntity.class, id);
        if (task == null) {
            return Result.error("根据ID获取人员管理信息为空");
        }
        return Result.success(task);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiOperation(value = "创建人员管理")
    public ResponseMessage<?> create(@ApiParam(name = "人员管理对象") @RequestBody PositionUserEntity positionUser, UriComponentsBuilder uriBuilder) {
        //调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
        Set<ConstraintViolation<PositionUserEntity>> failures = validator.validate(positionUser);
        if (!failures.isEmpty()) {
            return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
        }

        //保存
        try {
            positionUserService.save(positionUser);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("人员管理信息保存失败");
        }
        return Result.success(positionUser);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiOperation(value = "更新人员管理", notes = "更新人员管理")
    public ResponseMessage<?> update(@ApiParam(name = "人员管理对象") @RequestBody PositionUserEntity positionUser) {
        //调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
        Set<ConstraintViolation<PositionUserEntity>> failures = validator.validate(positionUser);
        if (!failures.isEmpty()) {
            return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
        }

        //保存
        try {
            positionUserService.saveOrUpdate(positionUser);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("更新人员管理信息失败");
        }

        //按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
        return Result.success("更新人员管理信息成功");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "删除人员管理")
    public ResponseMessage<?> delete(@ApiParam(name = "id", value = "ID", required = true) @PathVariable("id") String id) {
        logger.info("delete[{}]", id);
        // 验证
        if (StringUtils.isEmpty(id)) {
            return Result.error("ID不能为空");
        }
        try {
            positionUserService.deleteEntityById(PositionUserEntity.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("人员管理删除失败");
        }

        return Result.success();
    }
}
