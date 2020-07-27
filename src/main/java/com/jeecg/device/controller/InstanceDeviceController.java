package com.jeecg.device.controller;
import com.jeecg.device.entity.InstanceDeviceEntity;
import com.jeecg.device.service.InstanceDeviceServiceI;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.common.TreeChildCount;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;

import java.io.OutputStream;
import org.jeecgframework.core.util.BrowserUtils;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.vo.TemplateExcelConstants;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jeecgframework.core.util.ResourceUtil;
import java.io.IOException;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.util.Map;
import java.util.HashMap;
import org.jeecgframework.core.util.ExceptionUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.jwt.util.GsonUtil;
import org.jeecgframework.jwt.util.ResponseMessage;
import org.jeecgframework.jwt.util.Result;
import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**   
 * @Title: Controller  
 * @Description: 应用设备
 * @author onlineGenerator
 * @date 2018-12-21 16:14:00
 * @version V1.0   
 *
 */
@Api(value="InstanceDevice",description="应用设备",tags="instanceDeviceController")
@Controller
@RequestMapping("/instanceDeviceController")
public class InstanceDeviceController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(InstanceDeviceController.class);

	@Autowired
	private InstanceDeviceServiceI instanceDeviceService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	


	/**
	 * 应用设备列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/device/instanceDeviceList");
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
	public void datagrid(InstanceDeviceEntity instanceDevice,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(InstanceDeviceEntity.class, dataGrid);
		if(StringUtil.isEmpty(instanceDevice.getId())){
			cq.isNull("parentBy");
		}else{
			cq.eq("parentBy", instanceDevice.getId());
			instanceDevice.setId(null);
		}
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, instanceDevice, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.instanceDeviceService.getDataGridReturn(cq, true);
		TagUtil.treegrid(response, dataGrid, true);
	}
	
	@RequestMapping(params = "listAll", method = RequestMethod.GET)
	@ResponseBody
	public ResponseMessage<List<InstanceDeviceEntity>> listAll() {
		List<InstanceDeviceEntity> listInstanceDevices = instanceDeviceService.getList(InstanceDeviceEntity.class);
		return Result.success(listInstanceDevices);
	}
	
	/**
	 * 删除应用设备
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(InstanceDeviceEntity instanceDevice, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		instanceDevice = systemService.getEntity(InstanceDeviceEntity.class, instanceDevice.getId());
		message = "应用设备删除成功";
		try{
			instanceDeviceService.delete(instanceDevice);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "应用设备删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除应用设备
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "应用设备删除成功";
		try{
			for(String id:ids.split(",")){
				InstanceDeviceEntity instanceDevice = systemService.getEntity(InstanceDeviceEntity.class, 
				id
				);
				instanceDeviceService.delete(instanceDevice);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "应用设备删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加应用设备
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(InstanceDeviceEntity instanceDevice, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "应用设备添加成功";
		try{
			if(StringUtil.isEmpty(instanceDevice.getParentBy())){
				instanceDevice.setParentBy(null);
			}
			instanceDeviceService.save(instanceDevice);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "应用设备添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新应用设备
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(InstanceDeviceEntity instanceDevice, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "应用设备更新成功";
		InstanceDeviceEntity t = instanceDeviceService.get(InstanceDeviceEntity.class, instanceDevice.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(instanceDevice, t);
			if(StringUtil.isEmpty(t.getParentBy())){
				t.setParentBy(null);
			}
			instanceDeviceService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "应用设备更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 应用设备新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(InstanceDeviceEntity instanceDevice, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(instanceDevice.getId())) {
			instanceDevice = instanceDeviceService.getEntity(InstanceDeviceEntity.class, instanceDevice.getId());
			req.setAttribute("instanceDevicePage", instanceDevice);
		}
		return new ModelAndView("com/jeecg/device/instanceDevice-add");
	}
	/**
	 * 应用设备编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(InstanceDeviceEntity instanceDevice, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(instanceDevice.getId())) {
			instanceDevice = instanceDeviceService.getEntity(InstanceDeviceEntity.class, instanceDevice.getId());
			req.setAttribute("instanceDevicePage", instanceDevice);
		}
		return new ModelAndView("com/jeecg/device/instanceDevice-update");
	}
	
	@RequestMapping(params = "goViewModel")
	public ModelAndView goViewModel(String modelUrl, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(modelUrl)) {
			req.setAttribute("modelUrl", modelUrl);
		}
		return new ModelAndView("com/jeecg/device/deviceModelView");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","instanceDeviceController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(InstanceDeviceEntity instanceDevice,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(InstanceDeviceEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, instanceDevice, request.getParameterMap());
		List<InstanceDeviceEntity> instanceDevices = this.instanceDeviceService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"应用设备");
		modelMap.put(NormalExcelConstants.CLASS,InstanceDeviceEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("应用设备列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,instanceDevices);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(InstanceDeviceEntity instanceDevice,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"应用设备");
    	modelMap.put(NormalExcelConstants.CLASS,InstanceDeviceEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("应用设备列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
    	"导出信息"));
    	modelMap.put(NormalExcelConstants.DATA_LIST,new ArrayList());
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
				List<InstanceDeviceEntity> listInstanceDeviceEntitys = ExcelImportUtil.importExcel(file.getInputStream(),InstanceDeviceEntity.class,params);
				for (InstanceDeviceEntity instanceDevice : listInstanceDeviceEntitys) {
					instanceDeviceService.save(instanceDevice);
				}
				j.setMsg("文件导入成功！");
			} catch (Exception e) {
				j.setMsg("文件导入失败！");
				logger.error(e.getMessage());
			}finally{
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
	@ApiOperation(value="应用设备列表信息",produces="application/json",httpMethod="GET")
	public ResponseMessage<List<InstanceDeviceEntity>> list() {
		List<InstanceDeviceEntity> listInstanceDevices=instanceDeviceService.getList(InstanceDeviceEntity.class);
		return Result.success(listInstanceDevices);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="根据ID获取应用设备信息",notes="根据ID获取应用设备信息",httpMethod="GET",produces="application/json")
	public ResponseMessage<?> get(@ApiParam(required=true,name="id",value="ID")@PathVariable("id") String id) {
		InstanceDeviceEntity task = instanceDeviceService.get(InstanceDeviceEntity.class, id);
		if (task == null) {
			return Result.error("根据ID获取应用设备信息为空");
		}
		return Result.success(task);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="创建应用设备")
	public ResponseMessage<?> create(@ApiParam(name="应用设备对象")@RequestBody InstanceDeviceEntity instanceDevice, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<InstanceDeviceEntity>> failures = validator.validate(instanceDevice);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//保存
		try{
			instanceDeviceService.save(instanceDevice);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("应用设备信息保存失败");
		}
		return Result.success(instanceDevice);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="更新应用设备",notes="更新应用设备")
	public ResponseMessage<?> update(@ApiParam(name="应用设备对象")@RequestBody InstanceDeviceEntity instanceDevice) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<InstanceDeviceEntity>> failures = validator.validate(instanceDevice);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//保存
		try{
			instanceDeviceService.saveOrUpdate(instanceDevice);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("更新应用设备信息失败");
		}

		//按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return Result.success("更新应用设备信息成功");
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value="删除应用设备")
	public ResponseMessage<?> delete(@ApiParam(name="id",value="ID",required=true)@PathVariable("id") String id) {
		logger.info("delete[{}]" , id);
		// 验证
		if (StringUtils.isEmpty(id)) {
			return Result.error("ID不能为空");
		}
		try {
			instanceDeviceService.deleteEntityById(InstanceDeviceEntity.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("应用设备删除失败");
		}

		return Result.success();
	}
}
