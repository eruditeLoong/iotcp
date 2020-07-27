package com.jeecg.device.controller;

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

import com.jeecg.device.entity.BaseDeviceEntity;
import com.jeecg.device.entity.NschemeDeviceEntity;
import com.jeecg.device.service.NschemeDeviceServiceI;

/**
 * @Title: Controller
 * @Description: 组网方案绑定设备
 * @author onlineGenerator
 * @date 2018-08-31 18:47:28
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/nschemeDeviceController")
public class NschemeDeviceController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(NschemeDeviceController.class);

	@Autowired
	private NschemeDeviceServiceI nschemeDeviceService;
	@Autowired
	private SystemService systemService;
	@Resource
	private ClientManager clientManager;

	/**
	 * 组网方案绑定设备列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/device/nschemeDeviceList");
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
	public void datagrid(NschemeDeviceEntity nschemeDevice, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(NschemeDeviceEntity.class, dataGrid);
		if(StringUtil.isEmpty(nschemeDevice.getId())){
			cq.isNull("gatewayBy");
		}else{
			cq.eq("gatewayBy", nschemeDevice.getId());
			nschemeDevice.setId(null);
		}
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, nschemeDevice, request.getParameterMap());
		try {
			// 自定义追加查询条件

		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.nschemeDeviceService.getDataGridReturn(cq, true);
		TagUtil.treegrid(response, dataGrid, true);
	}

	/**
	 * 终端设备列表
	 * @param nschemeDevice
	 * @param model
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "nwdatagrid")
	public void nwdatagrid(NschemeDeviceEntity nschemeDevice, String model, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		String hql = "FROM NschemeDeviceEntity where 1=1 and schemeBy=? and deviceBy in (select id from BaseDeviceEntity where 1=1 and type=?)";
		List<NschemeDeviceEntity> list = new ArrayList<>();
		if (StringUtil.isNotBlank(nschemeDevice.getSchemeBy())) { // 方案属性不为空
			list = this.systemService.findHql(hql, new Object[] { nschemeDevice.getSchemeBy(), model });
		} else {
			
		}
		dataGrid.setResults(list);
		dataGrid.setOrder("deviceCode");
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 网关设备列表
	 * @param nschemeDevice
	 * @param model
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "ntdatagrid")
	public void ntdatagrid(NschemeDeviceEntity nschemeDevice, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		String hql = "FROM NschemeDeviceEntity where 1=1 and schemeBy=? and gatewayBy=?";
		List<NschemeDeviceEntity> list = new ArrayList<>();
		if (StringUtil.isNotBlank(nschemeDevice.getSchemeBy())) { // 方案属性不为空
			list = this.systemService.findHql(hql, new Object[] { nschemeDevice.getSchemeBy(), nschemeDevice.getId()});
		} else {
			
		}
		dataGrid.setResults(list);
		dataGrid.setOrder("deviceCode");
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除组网方案绑定设备
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(NschemeDeviceEntity nschemeDevice, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		nschemeDevice = systemService.getEntity(NschemeDeviceEntity.class, nschemeDevice.getId());
		message = "组网方案绑定设备删除成功";
		try {
			nschemeDeviceService.delete(nschemeDevice);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "组网方案绑定设备删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量删除组网方案绑定设备
	 * 
	 * @return
	 */
	@RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "组网方案绑定设备删除成功";
		try {
			for (String id : ids.split(",")) {
				NschemeDeviceEntity nschemeDevice = systemService.getEntity(NschemeDeviceEntity.class, id);
				nschemeDeviceService.delete(nschemeDevice);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "组网方案绑定设备删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加组网方案绑定设备
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(NschemeDeviceEntity nschemeDevice, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "组网方案绑定设备添加成功";
		try {
			if(StringUtil.isEmpty(nschemeDevice.getGatewayBy())){
				nschemeDevice.setGatewayBy(null);
			}
			nschemeDeviceService.save(nschemeDevice);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "组网方案绑定设备添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 更新组网方案绑定设备
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(NschemeDeviceEntity nschemeDevice, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "组网方案绑定设备更新成功";
		NschemeDeviceEntity t = nschemeDeviceService.get(NschemeDeviceEntity.class, nschemeDevice.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(nschemeDevice, t);
			if(StringUtil.isEmpty(nschemeDevice.getGatewayBy())){
				nschemeDevice.setGatewayBy(null);
			}
			nschemeDeviceService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "组网方案绑定设备更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 组网方案绑定设备新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(NschemeDeviceEntity nschemeDevice, HttpServletRequest req) {
		nschemeDevice.setCreateDate(new Date());
		/* 当前登录用户 */
		HttpSession session = ContextHolderUtils.getSession();
		TSUser user = (TSUser) session.getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		nschemeDevice.setCreateBy(user.getUserName());
		// 当前组织信息
		String code = clientManager.getClient().getUser().getCurrentDepart().getOrgCode();
		String sysCompanyCode = clientManager.getClient().getUser().getCurrentDepart().getSysCompanyCode();
		nschemeDevice.setSysOrgCode(code);
		nschemeDevice.setSysCompanyCode(sysCompanyCode);
		req.setAttribute("nschemeDevice", nschemeDevice);
		
		String baseDeviceType = req.getParameter("baseDeviceType");
		req.setAttribute("baseDeviceType", baseDeviceType);
		return new ModelAndView("com/jeecg/device/nschemeDevice-add");
	}

	/**
	 * 组网方案绑定设备编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(NschemeDeviceEntity nschemeDevice, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(nschemeDevice.getId())) {
			nschemeDevice = nschemeDeviceService.getEntity(NschemeDeviceEntity.class, nschemeDevice.getId());
			req.setAttribute("nschemeDevice", nschemeDevice);
			
			String baseDeviceType = req.getParameter("baseDeviceType");
			req.setAttribute("baseDeviceType", baseDeviceType);
		}
		return new ModelAndView("com/jeecg/device/nschemeDevice-update");
	}

	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name", "nschemeDeviceController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(NschemeDeviceEntity nschemeDevice, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(NschemeDeviceEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, nschemeDevice, request.getParameterMap());
		List<NschemeDeviceEntity> nschemeDevices = this.nschemeDeviceService.getListByCriteriaQuery(cq, false);
		modelMap.put(NormalExcelConstants.FILE_NAME, "组网方案绑定设备");
		modelMap.put(NormalExcelConstants.CLASS, NschemeDeviceEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("组网方案绑定设备列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST, nschemeDevices);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(NschemeDeviceEntity nschemeDevice, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, ModelMap modelMap) {
		modelMap.put(NormalExcelConstants.FILE_NAME, "组网方案绑定设备");
		modelMap.put(NormalExcelConstants.CLASS, NschemeDeviceEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("组网方案绑定设备列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
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
				List<NschemeDeviceEntity> listNschemeDeviceEntitys = ExcelImportUtil.importExcel(file.getInputStream(), NschemeDeviceEntity.class, params);
				for (NschemeDeviceEntity nschemeDevice : listNschemeDeviceEntitys) {
					nschemeDeviceService.save(nschemeDevice);
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
