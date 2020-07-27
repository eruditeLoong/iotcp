package com.jeecg.position.controller;
import com.jeecg.position.entity.PositionUserOrgEntity;
import com.jeecg.position.service.PositionUserOrgServiceI;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

/**   
 * @Title: Controller  
 * @Description: 人员单位
 * @author onlineGenerator
 * @date 2019-10-08 21:23:21
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/positionUserOrgController")
public class PositionUserOrgController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(PositionUserOrgController.class);

	@Autowired
	private PositionUserOrgServiceI positionUserOrgService;
	@Autowired
	private SystemService systemService;
	


	/**
	 * 人员单位列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/position/positionUserOrgList");
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
	public void datagrid(PositionUserOrgEntity positionUserOrg,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(PositionUserOrgEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, positionUserOrg, request.getParameterMap());
		try{
		//自定义追加查询条件
		
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.positionUserOrgService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 删除人员单位
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(PositionUserOrgEntity positionUserOrg, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		positionUserOrg = systemService.getEntity(PositionUserOrgEntity.class, positionUserOrg.getId());
		message = "人员单位删除成功";
		try{
			positionUserOrgService.delete(positionUserOrg);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "人员单位删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除人员单位
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "人员单位删除成功";
		try{
			for(String id:ids.split(",")){
				PositionUserOrgEntity positionUserOrg = systemService.getEntity(PositionUserOrgEntity.class, 
				id
				);
				positionUserOrgService.delete(positionUserOrg);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "人员单位删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加人员单位
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(PositionUserOrgEntity positionUserOrg, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "人员单位添加成功";
		try{
			positionUserOrgService.save(positionUserOrg);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "人员单位添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新人员单位
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(PositionUserOrgEntity positionUserOrg, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "人员单位更新成功";
		PositionUserOrgEntity t = positionUserOrgService.get(PositionUserOrgEntity.class, positionUserOrg.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(positionUserOrg, t);
			positionUserOrgService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "人员单位更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 人员单位新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(PositionUserOrgEntity positionUserOrg, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(positionUserOrg.getId())) {
			positionUserOrg = positionUserOrgService.getEntity(PositionUserOrgEntity.class, positionUserOrg.getId());
			req.setAttribute("positionUserOrg", positionUserOrg);
		}
		return new ModelAndView("com/jeecg/position/positionUserOrg-add");
	}
	/**
	 * 人员单位编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(PositionUserOrgEntity positionUserOrg, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(positionUserOrg.getId())) {
			positionUserOrg = positionUserOrgService.getEntity(PositionUserOrgEntity.class, positionUserOrg.getId());
			req.setAttribute("positionUserOrg", positionUserOrg);
		}
		return new ModelAndView("com/jeecg/position/positionUserOrg-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","positionUserOrgController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(PositionUserOrgEntity positionUserOrg,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(PositionUserOrgEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, positionUserOrg, request.getParameterMap());
		List<PositionUserOrgEntity> positionUserOrgs = this.positionUserOrgService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"人员单位");
		modelMap.put(NormalExcelConstants.CLASS,PositionUserOrgEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("人员单位列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,positionUserOrgs);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(PositionUserOrgEntity positionUserOrg,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"人员单位");
    	modelMap.put(NormalExcelConstants.CLASS,PositionUserOrgEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("人员单位列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
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
				List<PositionUserOrgEntity> listPositionUserOrgEntitys = ExcelImportUtil.importExcel(file.getInputStream(),PositionUserOrgEntity.class,params);
				for (PositionUserOrgEntity positionUserOrg : listPositionUserOrgEntitys) {
					positionUserOrgService.save(positionUserOrg);
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
