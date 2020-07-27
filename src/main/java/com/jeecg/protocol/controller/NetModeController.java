package com.jeecg.protocol.controller;
import com.jeecg.protocol.entity.NetModeEntity;
import com.jeecg.protocol.service.NetModeServiceI;

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
 * @Description: 网络通讯方式
 * @author onlineGenerator
 * @date 2018-09-01 10:20:32
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/netModeController")
public class NetModeController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(NetModeController.class);

	@Autowired
	private NetModeServiceI netModeService;
	@Autowired
	private SystemService systemService;
	


	/**
	 * 网络通讯方式列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/protocol/netModeList");
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
	public void datagrid(NetModeEntity netMode,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(NetModeEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, netMode, request.getParameterMap());
		try{
		//自定义追加查询条件
		
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.netModeService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 删除网络通讯方式
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(NetModeEntity netMode, HttpServletRequest request, HttpServletResponse response) {
		String message = null;
		AjaxJson j = new AjaxJson();
		netMode = systemService.getEntity(NetModeEntity.class, netMode.getId());
		message = "网络通讯方式删除成功";
		try{
			String path = netMode.getIcon();
			if(StringUtil.isNotBlank(path)) {
				for (String p : path.split(",")) {
					if(StringUtil.isNotBlank(p)) {
						// 执行删除文件
						request.getRequestDispatcher("systemController/filedeal.do?isdel=1&swfTransform=false&path="+p).forward(request,response);
					}
				}
			}
			netModeService.delete(netMode);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "网络通讯方式删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除网络通讯方式
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "网络通讯方式删除成功";
		try{
			for(String id:ids.split(",")){
				NetModeEntity netMode = systemService.getEntity(NetModeEntity.class, 
				id
				);
				netModeService.delete(netMode);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "网络通讯方式删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加网络通讯方式
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(NetModeEntity netMode, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "网络通讯方式添加成功";
		try{
			netModeService.save(netMode);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "网络通讯方式添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新网络通讯方式
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(NetModeEntity netMode, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "网络通讯方式更新成功";
		NetModeEntity t = netModeService.get(NetModeEntity.class, netMode.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(netMode, t);
			netModeService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "网络通讯方式更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 网络通讯方式新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(NetModeEntity netMode, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(netMode.getId())) {
			netMode = netModeService.getEntity(NetModeEntity.class, netMode.getId());
			req.setAttribute("netMode", netMode);
		}
		return new ModelAndView("com/jeecg/protocol/netMode-add");
	}
	/**
	 * 网络通讯方式编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(NetModeEntity netMode, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(netMode.getId())) {
			netMode = netModeService.getEntity(NetModeEntity.class, netMode.getId());
			req.setAttribute("netMode", netMode);
		}
		return new ModelAndView("com/jeecg/protocol/netMode-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","netModeController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(NetModeEntity netMode,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(NetModeEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, netMode, request.getParameterMap());
		List<NetModeEntity> netModes = this.netModeService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"网络通讯方式");
		modelMap.put(NormalExcelConstants.CLASS,NetModeEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("网络通讯方式列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,netModes);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(NetModeEntity netMode,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"网络通讯方式");
    	modelMap.put(NormalExcelConstants.CLASS,NetModeEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("网络通讯方式列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
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
				List<NetModeEntity> listNetModeEntitys = ExcelImportUtil.importExcel(file.getInputStream(),NetModeEntity.class,params);
				for (NetModeEntity netMode : listNetModeEntitys) {
					netModeService.save(netMode);
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
