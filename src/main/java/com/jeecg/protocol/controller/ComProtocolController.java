package com.jeecg.protocol.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
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

import com.jeecg.protocol.entity.ComConfigEntity;
import com.jeecg.protocol.entity.ComProtocolEntity;
import com.jeecg.protocol.page.ComProtocolPage;
import com.jeecg.protocol.service.ComProtocolServiceI;

/**
 * @Title: Controller
 * @Description: 通讯协议
 * @author onlineGenerator
 * @date 2019-05-21 19:07:32
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/comProtocolController")
public class ComProtocolController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(ComProtocolController.class);

	@Autowired
	private ComProtocolServiceI comProtocolService;
	@Autowired
	private SystemService systemService;

	/**
	 * 通讯协议列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/protocol/comProtocolList");
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
	public void datagrid(ComProtocolEntity comProtocol, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(ComProtocolEntity.class, dataGrid);
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, comProtocol, request.getParameterMap());
		try {
			// 自定义追加查询条件
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.comProtocolService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除通讯协议
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(ComProtocolEntity comProtocol, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		comProtocol = systemService.getEntity(ComProtocolEntity.class, comProtocol.getId());
		String message = "通讯协议删除成功";
		try {
			comProtocolService.delMain(comProtocol);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "通讯协议删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量删除通讯协议
	 * 
	 * @return
	 */
	@RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String message = "通讯协议删除成功";
		try {
			for (String id : ids.split(",")) {
				ComProtocolEntity comProtocol = systemService.getEntity(ComProtocolEntity.class, id);
				comProtocolService.delMain(comProtocol);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "通讯协议删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加通讯协议
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(HttpServletRequest request, ComProtocolPage comProtocolPage) {
		AjaxJson j = new AjaxJson();
		String message = "添加成功";
		try {
			comProtocolService.addMain(comProtocolPage);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "通讯协议添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 更新通讯协议
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(HttpServletRequest request, ComProtocolPage comProtocolPage) {
		AjaxJson j = new AjaxJson();
		String message = "更新成功";
		try {
			comProtocolService.updateMain(comProtocolPage);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "更新通讯协议失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 通讯协议新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(HttpServletRequest req) {
		return new ModelAndView("com/jeecg/protocol/comProtocol-add");
	}

	/**
	 * 通讯协议编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(HttpServletRequest req) {
		String id = req.getParameter("id");
		ComProtocolPage comProtocolPage = new ComProtocolPage();
		if (StringUtil.isNotEmpty(id)) {
			ComProtocolEntity comProtocol = comProtocolService.getEntity(ComProtocolEntity.class, id);
			try {
				MyBeanUtils.copyBeanNotNull2Bean(comProtocol, comProtocolPage);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// ===================================================================================
			// 获取参数
			Object comProtocolBy0 = comProtocol.getId();
			// ===================================================================================
			// 查询-协议配置
			String hql0 = "from ComConfigEntity where 1 = 1 AND comProtocolBy = ? ";
			List<ComConfigEntity> comConfigEntityList = systemService.findHql(hql0, comProtocolBy0);
			if (comConfigEntityList == null || comConfigEntityList.size() <= 0) {
				comConfigEntityList = new ArrayList<ComConfigEntity>();
				comConfigEntityList.add(new ComConfigEntity());
			}
			comProtocolPage.setComConfigList(comConfigEntityList);
		}
		req.setAttribute("comProtocolPage", comProtocolPage);
		return new ModelAndView("com/jeecg/protocol/comProtocol-update");
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(ComProtocolEntity comProtocol, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, ModelMap map) {
		CriteriaQuery cq = new CriteriaQuery(ComProtocolEntity.class, dataGrid);
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, comProtocol);
		try {
			// 自定义追加查询条件
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		List<ComProtocolEntity> list = this.comProtocolService.getListByCriteriaQuery(cq, false);
		List<ComProtocolPage> pageList = new ArrayList<ComProtocolPage>();
		if (list != null && list.size() > 0) {
			for (ComProtocolEntity entity : list) {
				try {
					ComProtocolPage page = new ComProtocolPage();
					MyBeanUtils.copyBeanNotNull2Bean(entity, page);
					Object comProtocolBy0 = entity.getId();
					String hql0 = "from ComConfigEntity where 1 = 1 AND comProtocolBy = ? ";
					List<ComConfigEntity> comConfigEntityList = systemService.findHql(hql0, comProtocolBy0);
					page.setComConfigList(comConfigEntityList);
					pageList.add(page);
				} catch (Exception e) {
					logger.info(e.getMessage());
				}
			}
		}
		map.put(NormalExcelConstants.FILE_NAME, "通讯协议");
		map.put(NormalExcelConstants.CLASS, ComProtocolPage.class);
		map.put(NormalExcelConstants.PARAMS, new ExportParams("通讯协议列表", "导出人:Jeecg", "导出信息"));
		map.put(NormalExcelConstants.DATA_LIST, pageList);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

	/**
	 * 通过excel导入数据
	 * 
	 * @param request
	 * @param
	 * @return
	 */
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
			params.setHeadRows(2);
			params.setNeedSave(true);
			try {
				List<ComProtocolPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ComProtocolPage.class, params);
				for (ComProtocolPage page : list) {
					comProtocolService.addMain(page);
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

	/**
	 * 导出excel 使模板
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(ModelMap map) {
		map.put(NormalExcelConstants.FILE_NAME, "通讯协议");
		map.put(NormalExcelConstants.CLASS, ComProtocolPage.class);
		map.put(NormalExcelConstants.PARAMS, new ExportParams("通讯协议列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
		map.put(NormalExcelConstants.DATA_LIST, new ArrayList());
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

	/**
	 * 导入功能跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name", "comProtocolController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

}
