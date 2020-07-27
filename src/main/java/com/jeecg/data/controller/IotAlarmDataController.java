package com.jeecg.data.controller;
import com.jeecg.data.entity.IotAlarmDataEntity;
import com.jeecg.data.service.IotAlarmDataServiceI;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;

import java.io.OutputStream;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.core.util.ResourceUtil;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.util.Map;
import java.util.HashMap;
import org.jeecgframework.core.util.ExceptionUtil;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.jwt.util.ResponseMessage;
import org.jeecgframework.jwt.util.Result;
import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**   
 * @Title: Controller  
 * @Description: 报警数据
 * @author onlineGenerator
 * @date 2019-08-20 20:19:10
 * @version V1.0   
 *
 */
@Api(value="IotAlarmData",description="报警数据",tags="iotAlarmDataController")
@Controller
@RequestMapping("/iotAlarmDataController")
public class IotAlarmDataController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(IotAlarmDataController.class);

	@Autowired
	private IotAlarmDataServiceI iotAlarmDataService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	


	/**
	 * 报警数据列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/data/iotAlarmDataList");
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
	public void datagrid(IotAlarmDataEntity iotAlarmData,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(IotAlarmDataEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, iotAlarmData, request.getParameterMap());
		try{
		//自定义追加查询条件
		
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.iotAlarmDataService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	@RequestMapping(params = "listAlarmData")
	@ResponseBody
	public AjaxJson listAlarmData(IotAlarmDataEntity iotAlarmData, DataGrid dataGrid){
		String message = "获取报警数据成功！";
		AjaxJson j = new AjaxJson();
		CriteriaQuery cq = new CriteriaQuery(IotAlarmDataEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, iotAlarmData);
		cq.addOrder("alarmDate", SortDirection.desc);
		cq.add();
		this.iotAlarmDataService.getDataGridReturn(cq, false);
		j.setMsg(message);
		j.setObj(dataGrid.getResults());
		return j;
	}
	
	/**
	 * 删除报警数据
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(IotAlarmDataEntity iotAlarmData, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		iotAlarmData = systemService.getEntity(IotAlarmDataEntity.class, iotAlarmData.getId());
		message = "报警数据删除成功";
		try{
			iotAlarmDataService.delete(iotAlarmData);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "报警数据删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除报警数据
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "报警数据删除成功";
		try{
			for(String id:ids.split(",")){
				IotAlarmDataEntity iotAlarmData = systemService.getEntity(IotAlarmDataEntity.class, 
				id
				);
				iotAlarmDataService.delete(iotAlarmData);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "报警数据删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加报警数据
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(IotAlarmDataEntity iotAlarmData, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "报警数据添加成功";
		try{
			iotAlarmDataService.save(iotAlarmData);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "报警数据添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新报警数据
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(IotAlarmDataEntity iotAlarmData, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "报警数据更新成功";
		IotAlarmDataEntity t = iotAlarmDataService.get(IotAlarmDataEntity.class, iotAlarmData.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(iotAlarmData, t);
			iotAlarmDataService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "报警数据更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 报警数据新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(IotAlarmDataEntity iotAlarmData, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(iotAlarmData.getId())) {
			iotAlarmData = iotAlarmDataService.getEntity(IotAlarmDataEntity.class, iotAlarmData.getId());
			req.setAttribute("iotAlarmData", iotAlarmData);
		}
		return new ModelAndView("com/jeecg/data/iotAlarmData-add");
	}
	/**
	 * 报警数据编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(IotAlarmDataEntity iotAlarmData, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(iotAlarmData.getId())) {
			iotAlarmData = iotAlarmDataService.getEntity(IotAlarmDataEntity.class, iotAlarmData.getId());
			req.setAttribute("iotAlarmData", iotAlarmData);
		}
		return new ModelAndView("com/jeecg/data/iotAlarmData-update");
	}

	/**
	 * 报警数据处理页面跳转
	 * @param iotAlarmData
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goDeal")
	public ModelAndView goDeal(IotAlarmDataEntity iotAlarmData, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(iotAlarmData.getId())) {
			iotAlarmData = iotAlarmDataService.getEntity(IotAlarmDataEntity.class, iotAlarmData.getId());
			req.setAttribute("iotAlarmData", iotAlarmData);
		}
		return new ModelAndView("com/jeecg/data/iotAlarmData-deal");
	}

	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","iotAlarmDataController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(IotAlarmDataEntity iotAlarmData,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(IotAlarmDataEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, iotAlarmData, request.getParameterMap());
		List<IotAlarmDataEntity> iotAlarmDatas = this.iotAlarmDataService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"报警数据");
		modelMap.put(NormalExcelConstants.CLASS,IotAlarmDataEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("报警数据列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,iotAlarmDatas);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(IotAlarmDataEntity iotAlarmData,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"报警数据");
    	modelMap.put(NormalExcelConstants.CLASS,IotAlarmDataEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("报警数据列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
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
				List<IotAlarmDataEntity> listIotAlarmDataEntitys = ExcelImportUtil.importExcel(file.getInputStream(),IotAlarmDataEntity.class,params);
				for (IotAlarmDataEntity iotAlarmData : listIotAlarmDataEntitys) {
					iotAlarmDataService.save(iotAlarmData);
				}
				j.setMsg("文件导入成功！");
			} catch (Exception e) {
				j.setMsg("文件导入失败！");
				logger.error(ExceptionUtil.getExceptionMessage(e));
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
	
	
	@RequestMapping(value="/list/{pageNo}/{pageSize}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="报警数据列表信息",produces="application/json",httpMethod="GET")
	public ResponseMessage<List<IotAlarmDataEntity>> list(@PathVariable("pageNo") int pageNo, @PathVariable("pageSize") int pageSize, HttpServletRequest request) {
		if(pageSize > Globals.MAX_PAGESIZE){
			return Result.error("每页请求不能超过" + Globals.MAX_PAGESIZE + "条");
		}
		CriteriaQuery query = new CriteriaQuery(IotAlarmDataEntity.class);
		query.setCurPage(pageNo<=0?1:pageNo);
		query.setPageSize(pageSize<1?1:pageSize);
		List<IotAlarmDataEntity> listIotAlarmDatas = this.iotAlarmDataService.getListByCriteriaQuery(query,true);
		return Result.success(listIotAlarmDatas);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="根据ID获取报警数据信息",notes="根据ID获取报警数据信息",httpMethod="GET",produces="application/json")
	public ResponseMessage<?> get(@ApiParam(required=true,name="id",value="ID")@PathVariable("id") String id) {
		IotAlarmDataEntity task = iotAlarmDataService.get(IotAlarmDataEntity.class, id);
		if (task == null) {
			return Result.error("根据ID获取报警数据信息为空");
		}
		return Result.success(task);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="创建报警数据")
	public ResponseMessage<?> create(@ApiParam(name="报警数据对象")@RequestBody IotAlarmDataEntity iotAlarmData, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<IotAlarmDataEntity>> failures = validator.validate(iotAlarmData);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//保存
		try{
			iotAlarmDataService.save(iotAlarmData);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("报警数据信息保存失败");
		}
		return Result.success(iotAlarmData);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="更新报警数据",notes="更新报警数据")
	public ResponseMessage<?> update(@ApiParam(name="报警数据对象")@RequestBody IotAlarmDataEntity iotAlarmData) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<IotAlarmDataEntity>> failures = validator.validate(iotAlarmData);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//保存
		try{
			iotAlarmDataService.saveOrUpdate(iotAlarmData);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("更新报警数据信息失败");
		}

		//按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return Result.success("更新报警数据信息成功");
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value="删除报警数据")
	public ResponseMessage<?> delete(@ApiParam(name="id",value="ID",required=true)@PathVariable("id") String id) {
		logger.info("delete[{}]" , id);
		// 验证
		if (StringUtils.isEmpty(id)) {
			return Result.error("ID不能为空");
		}
		try {
			iotAlarmDataService.deleteEntityById(IotAlarmDataEntity.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("报警数据删除失败");
		}

		return Result.success();
	}
}
