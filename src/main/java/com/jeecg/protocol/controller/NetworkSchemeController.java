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

import com.jeecg.protocol.entity.NetworkSchemeEntity;
import com.jeecg.protocol.service.NetworkSchemeServiceI;

/**
 * @Title: Controller
 * @Description: 组网方案
 * @author onlineGenerator
 * @date 2018-08-31 16:13:38
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/networkSchemeController")
public class NetworkSchemeController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(NetworkSchemeController.class);

	@Autowired
	private NetworkSchemeServiceI networkSchemeService;
	@Autowired
	private SystemService systemService;
	@Resource
	private ClientManager clientManager;

	/**
	 * 组网方案列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/protocol/networkSchemeList");
	}

	/**
	 * 组网方案-配置设备 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "deviceList")
	public ModelAndView deviceList(HttpServletRequest request) {
		request.setAttribute("schemeBy", request.getParameter("schemeBy"));
		request.setAttribute("model", request.getParameter("model"));
		if ("terminal".equals(request.getParameter("model"))) { // 跳转终端配置页面
			return new ModelAndView("com/jeecg/protocol/networkScheme-deviceList");
		} else if ("gateway".equals(request.getParameter("model"))) { // 跳转网关配置页面
			return new ModelAndView("com/jeecg/protocol/networkScheme-gatewayList");
		}
		return new ModelAndView();
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
	public void datagrid(NetworkSchemeEntity networkScheme, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(NetworkSchemeEntity.class, dataGrid);
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, networkScheme,
				request.getParameterMap());
		try {
			// 自定义追加查询条件

		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.networkSchemeService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除组网方案
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(NetworkSchemeEntity networkScheme, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		networkScheme = systemService.getEntity(NetworkSchemeEntity.class, networkScheme.getId());
		message = "组网方案删除成功";
		try {
			networkSchemeService.delete(networkScheme);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "组网方案删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量删除组网方案
	 * 
	 * @return
	 */
	@RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "组网方案删除成功";
		try {
			for (String id : ids.split(",")) {
				NetworkSchemeEntity networkScheme = systemService.getEntity(NetworkSchemeEntity.class, id);
				networkSchemeService.delete(networkScheme);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "组网方案删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加组网方案
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(NetworkSchemeEntity networkScheme, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "组网方案添加成功";
		try {
			networkSchemeService.save(networkScheme);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "组网方案添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 更新组网方案
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(NetworkSchemeEntity networkScheme, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "组网方案更新成功";
		NetworkSchemeEntity t = networkSchemeService.get(NetworkSchemeEntity.class, networkScheme.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(networkScheme, t);
			networkSchemeService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "组网方案更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 组网方案新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(NetworkSchemeEntity networkScheme, HttpServletRequest req) {
		networkScheme.setCreateDate(new Date());
		/* 当前登录用户 */
		HttpSession session = ContextHolderUtils.getSession();
		TSUser user = (TSUser) session.getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		networkScheme.setCreateBy(user.getUserName());
		// 当前组织信息
		String code = clientManager.getClient().getUser().getCurrentDepart().getOrgCode();
		String sysCompanyCode = clientManager.getClient().getUser().getCurrentDepart().getSysCompanyCode();
		networkScheme.setSysOrgCode(code);
		networkScheme.setSysCompanyCode(sysCompanyCode);
		req.setAttribute("networkScheme", networkScheme);
		return new ModelAndView("com/jeecg/protocol/networkScheme-add");
	}

	/**
	 * 组网方案编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(NetworkSchemeEntity networkScheme, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(networkScheme.getId())) {
			networkScheme = networkSchemeService.getEntity(NetworkSchemeEntity.class, networkScheme.getId());
			req.setAttribute("networkScheme", networkScheme);
		}
		return new ModelAndView("com/jeecg/protocol/networkScheme-update");
	}

	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name", "networkSchemeController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(NetworkSchemeEntity networkScheme, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid, ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(NetworkSchemeEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, networkScheme,
				request.getParameterMap());
		List<NetworkSchemeEntity> networkSchemes = this.networkSchemeService.getListByCriteriaQuery(cq, false);
		modelMap.put(NormalExcelConstants.FILE_NAME, "组网方案");
		modelMap.put(NormalExcelConstants.CLASS, NetworkSchemeEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,
				new ExportParams("组网方案列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST, networkSchemes);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(NetworkSchemeEntity networkScheme, HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid, ModelMap modelMap) {
		modelMap.put(NormalExcelConstants.FILE_NAME, "组网方案");
		modelMap.put(NormalExcelConstants.CLASS, NetworkSchemeEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,
				new ExportParams("组网方案列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST, new ArrayList<Object>());
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

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
				List<NetworkSchemeEntity> listNetworkSchemeEntitys = ExcelImportUtil.importExcel(file.getInputStream(),
						NetworkSchemeEntity.class, params);
				for (NetworkSchemeEntity networkScheme : listNetworkSchemeEntitys) {
					networkSchemeService.save(networkScheme);
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
