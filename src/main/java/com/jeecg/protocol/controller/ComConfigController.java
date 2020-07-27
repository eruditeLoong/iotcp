package com.jeecg.protocol.controller;

import com.jeecg.protocol.entity.ComConfigEntity;
import com.jeecg.protocol.service.ComProtocolServiceI;
import com.jeecg.protocol.page.ComProtocolPage;

import java.util.ArrayList;
import java.util.List; 
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.ExceptionUtil;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.jwt.util.GsonUtil;
import org.jeecgframework.jwt.util.ResponseMessage;
import org.jeecgframework.jwt.util.Result;
import com.alibaba.fastjson.JSONArray;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**   
 * @Title: controller
 * @Description: 参数配置
 * @author onlineGenerator
 * @date 2018-08-31 09:58:18
 * @version V1.0   
 *
 */

@Api(value="ComConfig",description="参数配置",tags="comConfigController")
@Controller
@RequestMapping("/comConfigController")
public class ComConfigController extends BaseController {
	
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ComConfigController.class);

	@Autowired
	private ComProtocolServiceI comProtocolService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;

	/**
	 * 参数配置列表 页面跳转
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/protocol/comConfig-list");
	}


	/**
	 * easyui AJAX请求数据
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(ComConfigEntity comConfig,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(ComConfigEntity.class, dataGrid);
		String mainId = request.getParameter("mainId");
		if(oConvertUtils.isNotEmpty(mainId)){
			//查询条件组装器
			org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, comConfig,request.getParameterMap());
			try{
				//自定义追加查询条件
			 	cq.eq("comProtocolBy", mainId);
			}catch (Exception e) {
				throw new BusinessException(e.getMessage());
			}
			cq.add();
			this.comProtocolService.getDataGridReturn(cq, true);
		}
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 删除参数配置
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(ComConfigEntity comConfig, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		comConfig = systemService.getEntity(ComConfigEntity.class, comConfig.getId());
		String message = "参数配置删除成功";
		try{
			if(comConfig!=null){
				comProtocolService.delete(comConfig);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "参数配置删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除参数配置
	 * @return
	 */
	@RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		String message = "参数配置删除成功";
		try{
			for(String id:ids.split(",")){
				ComConfigEntity comConfig = systemService.getEntity(ComConfigEntity.class,
				id
				);
				if(comConfig!=null){
					comProtocolService.delete(comConfig);
					systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "参数配置删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 添加参数配置
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(ComConfigEntity comConfig, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String message = "添加成功";
		try{
			comProtocolService.save(comConfig);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "参数配置添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新参数配置
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(ComConfigEntity comConfig, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String message = "更新成功";
		ComConfigEntity t = comProtocolService.get(ComConfigEntity.class,comConfig.getId());
		try{
			MyBeanUtils.copyBeanNotNull2Bean(comConfig, t);
			comProtocolService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "参数配置更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 参数配置新增页面跳转
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(ComConfigEntity comConfig, HttpServletRequest request){
		if (StringUtil.isNotEmpty(comConfig.getId())) {
			comConfig = comProtocolService.getEntity(ComConfigEntity.class, comConfig.getId());
			request.setAttribute("comConfigPage", comConfig);
		}
		request.setAttribute("mainId", request.getParameter("mainId"));
		return new ModelAndView("com/jeecg/protocol/comConfig-add");
	}
	/**
	 * 参数配置编辑页面跳转
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(ComConfigEntity comConfig, HttpServletRequest request){
		if (StringUtil.isNotEmpty(comConfig.getId())) {
			comConfig = comProtocolService.getEntity(ComConfigEntity.class, comConfig.getId());
			request.setAttribute("comConfigPage", comConfig);
		}
		return new ModelAndView("com/jeecg/protocol/comConfig-update");
	}
	
	/**
	 * 行编辑保存操作
	 * @param page
	 * @return
	 */
	@RequestMapping(params = "saveRows")
	@ResponseBody
	public AjaxJson saveRows(ComProtocolPage page,HttpServletRequest req){
		String message = "操作成功！";
		List<ComConfigEntity> lists=page.getComConfigList();
		AjaxJson j = new AjaxJson();
		String mainId = req.getParameter("mainId");
		if(CollectionUtils.isNotEmpty(lists)){
			for(ComConfigEntity temp:lists){
				if (StringUtil.isNotEmpty(temp.getId())) {
					ComConfigEntity t =this.systemService.get(ComConfigEntity.class, temp.getId());
					try {
						MyBeanUtils.copyBeanNotNull2Bean(temp, t);
						systemService.saveOrUpdate(t);
						systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {
						//temp.setDelFlag(0);若有则需要加
						temp.setComProtocolBy(mainId);
						systemService.save(temp);
						systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
		}
		return j;
	}
	
	/**
	 * 导出excel
	 * @param request
	 * @param response
	 */
    @RequestMapping(params = "exportXls")
    public void exportXls(ComConfigEntity comConfig,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid,ModelMap map) throws Exception {
    	CriteriaQuery cq = new CriteriaQuery(ComConfigEntity.class, dataGrid);
    	//必须要有合同id
		String mainId = request.getParameter("mainId");
		List<ComConfigEntity> list =new ArrayList<ComConfigEntity>();
		if(oConvertUtils.isNotEmpty(mainId)){
			//查询条件组装器
			comConfig.setComProtocolBy(mainId);
			org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, comConfig, request.getParameterMap());
			try{
				//自定义追加查询条件
				//cq.eq("delFlag", 0);
			}catch (Exception e) {
				e.printStackTrace();
				throw new BusinessException(e.getMessage());
			}
			cq.add();
			list= this.systemService.getListByCriteriaQuery(cq, false);
			Workbook excel=ExcelExportUtil.exportExcel(new ExportParams(), ComConfigEntity.class, list);
			response.setContentType("application/x-msdownload;charset=utf-8");
			response.setHeader("Content-disposition", "attachment; filename="+new String("参数配置列表.xls".getBytes("UTF-8"), "iso-8859-1"));
			OutputStream outputStream = null;
			try {
				outputStream = response.getOutputStream();
				excel.write(outputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					if(outputStream!=null)outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
   /**
	* 导出excel 使模板
	*/
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(ModelMap map) {
		map.put(NormalExcelConstants.FILE_NAME,"参数配置");
		map.put(NormalExcelConstants.CLASS,ComConfigEntity.class);
		map.put(NormalExcelConstants.PARAMS,new ExportParams("参数配置列表", "导出人:"+ ResourceUtil.getSessionUser().getRealName(),"导出信息"));
		map.put(NormalExcelConstants.DATA_LIST,new ArrayList());
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

    /**
	 * 通过excel导入数据
	 * @param request
	 * @param
	 * @return
	 */
	@RequestMapping(params = "importExcel", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson importExcel(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		String mainId = request.getParameter("mainId");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();// 获取上传文件对象
			ImportParams params = new ImportParams();
			params.setTitleRows(2);
			params.setHeadRows(2);
			params.setNeedSave(true);
			try {
				List<ComConfigEntity> list =  ExcelImportUtil.importExcel(file.getInputStream(), ComConfigEntity.class, params);
				for (ComConfigEntity page : list) {
					page.setComProtocolBy(mainId);
		       		comProtocolService.save(page);
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
	
}
