package com.jeecg.protocol.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.ExceptionUtil;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.manager.ClientManager;
import org.jeecgframework.web.system.pojo.base.TSUser;
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

import com.jeecg.protocol.entity.ComCheckEntity;
import com.jeecg.protocol.service.ComCheckServiceI;

/**
 * @Title: Controller
 * @Description: 数据校验
 * @author onlineGenerator
 * @date 2018-08-30 15:51:37
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/comCheckController")
public class ComCheckController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(ComCheckController.class);

	@Autowired
	private ComCheckServiceI comCheckService;
	@Autowired
	private SystemService systemService;
	@Resource
	private ClientManager clientManager;

	/**
	 * 数据校验列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/protocol/comCheckList");
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
	public void datagrid(ComCheckEntity comCheck, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(ComCheckEntity.class, dataGrid);
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, comCheck, request.getParameterMap());
		try {
			// 自定义追加查询条件

		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.comCheckService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除数据校验
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(ComCheckEntity comCheck, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		comCheck = systemService.getEntity(ComCheckEntity.class, comCheck.getId());
		message = "数据校验删除成功";
		try {
			comCheckService.delete(comCheck);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "数据校验删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量删除数据校验
	 * 
	 * @return
	 */
	@RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "数据校验删除成功";
		try {
			for (String id : ids.split(",")) {
				ComCheckEntity comCheck = systemService.getEntity(ComCheckEntity.class, id);
				comCheckService.delete(comCheck);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "数据校验删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加数据校验
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(ComCheckEntity comCheck, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "数据校验添加成功";
		try {
			comCheckService.save(comCheck);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "数据校验添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 更新数据校验
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(ComCheckEntity comCheck, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "数据校验更新成功";
		ComCheckEntity t = comCheckService.get(ComCheckEntity.class, comCheck.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(comCheck, t);
			comCheckService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "数据校验更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 数据校验新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(ComCheckEntity comCheck, HttpServletRequest req) {
		comCheck.setCreateDate(new Date());
		/* 当前登录用户 */
		HttpSession session = ContextHolderUtils.getSession();
		TSUser user = (TSUser) session.getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		comCheck.setCreateBy(user.getUserName());
		// 当前组织信息
		String code = clientManager.getClient().getUser().getCurrentDepart().getOrgCode();
		req.setAttribute("comCheck", comCheck);
		return new ModelAndView("com/jeecg/protocol/comCheck-add");
	}

	/**
	 * 数据校验编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(ComCheckEntity comCheck, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(comCheck.getId())) {
			comCheck = comCheckService.getEntity(ComCheckEntity.class, comCheck.getId());
			req.setAttribute("comCheck", comCheck);
		}
		return new ModelAndView("com/jeecg/protocol/comCheck-update");
	}

	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name", "comCheckController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(ComCheckEntity comCheck, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(ComCheckEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, comCheck, request.getParameterMap());
		List<ComCheckEntity> comChecks = this.comCheckService.getListByCriteriaQuery(cq, false);
		modelMap.put(NormalExcelConstants.FILE_NAME, "数据校验");
		modelMap.put(NormalExcelConstants.CLASS, ComCheckEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("数据校验列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST, comChecks);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(ComCheckEntity comCheck, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, ModelMap modelMap) {
		modelMap.put(NormalExcelConstants.FILE_NAME, "数据校验");
		modelMap.put(NormalExcelConstants.CLASS, ComCheckEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("数据校验列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
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
				List<ComCheckEntity> listComCheckEntitys = ExcelImportUtil.importExcel(file.getInputStream(), ComCheckEntity.class, params);
				for (ComCheckEntity comCheck : listComCheckEntitys) {
					comCheckService.save(comCheck);
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
