package com.jeecg.device.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecg.device.entity.BaseDeviceDataEntity;
import com.jeecg.device.entity.BaseDeviceEntity;
import com.jeecg.device.page.BaseDevicePage;
import com.jeecg.device.service.BaseDeviceServiceI;
import com.jeecg.device.service.InstanceDeviceServiceI;
import com.jeecg.protocol.entity.DataCustomEntity;
import com.jeecg.protocol.entity.DataProtocolEntity;
import com.jeecg.protocol.service.DataProtocolServiceI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
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
import org.jeecgframework.web.system.manager.ClientManager;
import org.jeecgframework.web.system.pojo.base.DictEntity;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.*;

/**
 * @author onlineGenerator
 * @version V1.0
 * @Title: Controller
 * @Description: 基础设备信息
 * @date 2018-08-24 18:08:53
 */
@Api(value = "BaseDevice", description = "基础设备信息", tags = "baseDeviceController")
@Controller
@RequestMapping("/baseDeviceController")
public class BaseDeviceController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(BaseDeviceController.class);

	@Autowired
	private BaseDeviceServiceI baseDeviceService;
	@Autowired
	private InstanceDeviceServiceI instanceDeviceService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	@Resource
	private ClientManager clientManager;
	@Autowired
	private DataProtocolServiceI dataProtocolService;

	/**
	 * 基础设备信息列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/device/baseDeviceList");
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
	public void datagrid(BaseDeviceEntity baseDevice, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(BaseDeviceEntity.class, dataGrid);
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, baseDevice, request.getParameterMap());
		try {
			// 自定义追加查询条件
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.baseDeviceService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	@RequestMapping(params = "getDeviceById")
	@ResponseBody
	public AjaxJson getDeviceById(BaseDeviceEntity baseDevice, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		baseDevice = systemService.getEntity(BaseDeviceEntity.class, baseDevice.getId());
		j.setObj(baseDevice);
		String message = "基础设备获取成功";
		j.setMsg(message);
		j.setSuccess(true);
		return j;
	}

	/**
	 * 根据场景id，获取基础设备列表
	 *
	 * @param sceneBy
	 * @param deviceConfBy
	 * @return
	 */
	@RequestMapping(params = "getSceneDeviceByScene")
	@ResponseBody
	public AjaxJson getSceneDeviceByScene(String sceneBy) {
		AjaxJson j = new AjaxJson();
		JSONArray jsonList = new JSONArray();
		String sql = "select bd.* from jform_scene_device sd left join jform_base_device bd on bd.id=sd.device_by where 1=1 and sd.scene_by=?";
		List<Map<String, Object>> sdList = systemService.findForJdbc(sql, sceneBy);
		for (Map<String, Object> sdMap : sdList) {
			JSONObject json = new JSONObject();
			json.put("id", sdMap.get("id")); // 设备id
			json.put("name", sdMap.get("name")); // 设备名称
			json.put("type", sdMap.get("type")); // 设备类型
			jsonList.add(json);
		}
		j.setObj(jsonList);
		return j;
	}

	/**
	 * 删除基础设备信息
	 *
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(BaseDeviceEntity baseDevice, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		baseDevice = systemService.getEntity(BaseDeviceEntity.class, baseDevice.getId());
		String message = "基础设备信息删除成功";
		try {
			// 判断关联
			String m = baseDeviceService.isAllowDel(baseDevice);
			if (StringUtil.isNotBlank(m)) {
				message = m;
				j.setMsg(message);
				return j;
			}
			baseDeviceService.delMain(baseDevice);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "基础设备信息删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量删除基础设备信息
	 *
	 * @return
	 */
	@RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String message = "基础设备信息删除成功";
		try {
			for (String id : ids.split(",")) {
				BaseDeviceEntity baseDevice = systemService.getEntity(BaseDeviceEntity.class, id);
				// 判断关联
				String m = baseDeviceService.isAllowDel(baseDevice);
				if (StringUtil.isNotBlank(m)) {
					message = m;
					j.setMsg(message);
					return j;
				}
				baseDeviceService.delMain(baseDevice);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "基础设备信息删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加基础设备信息
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(HttpServletRequest request, BaseDevicePage baseDevicePage) {
		AjaxJson j = new AjaxJson();
		String message = "添加成功";
		try {
			baseDeviceService.addMain(baseDevicePage);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "基础设备信息添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 更新基础设备信息
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(HttpServletRequest request, BaseDevicePage baseDevicePage) {
		AjaxJson j = new AjaxJson();
		String message = "更新成功";
		try {
			baseDeviceService.updateMain(baseDevicePage);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "更新基础设备信息失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 基础设备信息新增页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(BaseDeviceEntity baseDevice, HttpServletRequest req) {
		baseDevice.setCreateDate(new Date());
		/* 当前登录用户 */
		HttpSession session = ContextHolderUtils.getSession();
		TSUser user = (TSUser) session.getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		baseDevice.setCreateBy(user.getUserName());
		req.setAttribute("baseDevice", baseDevice);
		return new ModelAndView("com/jeecg/device/baseDevice-add");
	}

	/**
	 * 基础设备信息编辑页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(HttpServletRequest req) {
		String id = req.getParameter("id");
		BaseDevicePage baseDevicePage = new BaseDevicePage();
		if (StringUtil.isNotEmpty(id)) {
			BaseDeviceEntity baseDevice = baseDeviceService.getEntity(BaseDeviceEntity.class, id);
			try {
				MyBeanUtils.copyBeanNotNull2Bean(baseDevice, baseDevicePage);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// ===================================================================================
			// 获取参数
			Object deviceBy0 = baseDevice.getId();
			// ===================================================================================
			// 查询-基础设备数据点
			String hql0 = "from BaseDeviceDataEntity where 1 = 1 AND deviceBy = ? ";
			List<BaseDeviceDataEntity> baseDeviceDataEntityList = systemService.findHql(hql0, deviceBy0);
			if (baseDeviceDataEntityList == null || baseDeviceDataEntityList.size() <= 0) {
				baseDeviceDataEntityList = new ArrayList<BaseDeviceDataEntity>();
				baseDeviceDataEntityList.add(new BaseDeviceDataEntity());
			}
			baseDevicePage.setBaseDeviceDataList(baseDeviceDataEntityList);
		}
		req.setAttribute("baseDevicePage", baseDevicePage);
		return new ModelAndView("com/jeecg/device/baseDevice-update");
	}

	@RequestMapping(params = "goShowDataProtocol")
	public ModelAndView goShowDataProtocol(HttpServletRequest req) {
		req.setAttribute("id", req.getParameter("id"));
		req.setAttribute("code", req.getParameter("code"));
		return new ModelAndView("com/jeecg/device/showDataProtocol");
	}

	/**
	 * 跳转到设备选择页面，场景管理页面调用
	 *
	 * @param sceneId
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "goDeviceSelect")
	public ModelAndView goDeviceSelect(String sceneBy, HttpServletRequest request) {
		request.setAttribute("sceneBy", sceneBy);
		return new ModelAndView("com/jeecg/device/baseDevice-select");
	}

	@RequestMapping(params = "showDataProtocol")
	@ResponseBody
	public AjaxJson showDataProtocol(String id, String code, HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		String message = "成功";
		if (StringUtil.isNotEmpty(id)) {
			BaseDeviceEntity baseDevice = baseDeviceService.getEntity(BaseDeviceEntity.class, id);
			// 获取参数
			Object deviceBy0 = baseDevice.getId();
			// 查询-基础设备数据点
			String hql0 = "from BaseDeviceDataEntity where 1 = 1 AND deviceBy = ? ";
			List<BaseDeviceDataEntity> baseDeviceDataEntityList = systemService.findHql(hql0, deviceBy0);
			if (baseDeviceDataEntityList == null || baseDeviceDataEntityList.size() <= 0) {
				baseDeviceDataEntityList = new ArrayList<BaseDeviceDataEntity>();
				baseDeviceDataEntityList.add(new BaseDeviceDataEntity());
			}
			// 数据格式
			String tid = baseDevice.getDataFormat();
			DataProtocolEntity dpe = dataProtocolService.get(DataProtocolEntity.class, tid);
			JSONObject jsonObj = new JSONObject();
			JSONArray jsonList = new JSONArray();
			if ("JSON".equals(dpe.getCode())) {
				// JSON格式：根据基础设备的数据点解析数据
				JSONObject json = new JSONObject();
				// 放置编码
				json.put("code", code);
				for (BaseDeviceDataEntity bdDate : baseDeviceDataEntityList) {
					String value = "";
					if ("enum".equals(bdDate.getDataType())) {
						List<DictEntity> dictList = systemService.queryDict("", bdDate.getEnuRange(), "");
						StringBuffer sb = new StringBuffer();
						for (DictEntity dict : dictList) {
							sb.append(dict.getTypecode()).append(":").append(dict.getTypename()).append(";");
						}
						value = "value [" + sb.toString().substring(0, sb.toString().length() - 1) + "]";
					} else {
						value = "value [" + bdDate.getDataType() + "]";
					}
					json.put(bdDate.getField() + " [" + bdDate.getName() + "]", value);
				}
				jsonList.add(json);
				jsonObj.put("dataType", "JSON");
				jsonObj.put("dateProtocol", jsonList);
				j.setObj(jsonObj);
			} else {
				// 其它自定义格式：根据服务器定义的数据格式解析
				// 查询-自定义格式
				String hql1 = "from DataCustomEntity where 1 = 1 AND protocolBy = ? order by node_index asc";
				List<DataCustomEntity> customList = systemService.findHql(hql1, dpe.getId());
				for (DataCustomEntity custom : customList) {
					JSONObject json = new JSONObject();
					json.put("lable", custom.getNodeName());
					json.put("index", custom.getNodeIndex());
					json.put("datatype", custom.getDatatype()); // 数据基本类型
					jsonList.add(json);
				}
				jsonObj.put("dataShape", dpe.getDataShape()); // 数据传输形式：字符串、byte
				jsonObj.put("code", code);
				jsonObj.put("customName", "custom");
				jsonObj.put("dateProtocol", jsonList);
				j.setObj(jsonObj);
			}
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(BaseDeviceEntity baseDevice, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, ModelMap map) {
		CriteriaQuery cq = new CriteriaQuery(BaseDeviceEntity.class, dataGrid);
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, baseDevice);
		try {
			// 自定义追加查询条件
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		List<BaseDeviceEntity> list = this.baseDeviceService.getListByCriteriaQuery(cq, false);
		List<BaseDevicePage> pageList = new ArrayList<BaseDevicePage>();
		if (list != null && list.size() > 0) {
			for (BaseDeviceEntity entity : list) {
				try {
					BaseDevicePage page = new BaseDevicePage();
					MyBeanUtils.copyBeanNotNull2Bean(entity, page);
					Object deviceBy0 = entity.getId();
					String hql0 = "from BaseDeviceDataEntity where 1 = 1 AND deviceBy = ? ";
					List<BaseDeviceDataEntity> baseDeviceDataEntityList = systemService.findHql(hql0, deviceBy0);
					page.setBaseDeviceDataList(baseDeviceDataEntityList);
					pageList.add(page);
				} catch (Exception e) {
					logger.info(e.getMessage());
				}
			}
		}
		map.put(NormalExcelConstants.FILE_NAME, "基础设备信息");
		map.put(NormalExcelConstants.CLASS, BaseDevicePage.class);
		map.put(NormalExcelConstants.PARAMS, new ExportParams("基础设备信息列表", "导出人:Jeecg", "导出信息"));
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
				List<BaseDevicePage> list = ExcelImportUtil.importExcel(file.getInputStream(), BaseDevicePage.class, params);
				for (BaseDevicePage page : list) {
					baseDeviceService.addMain(page);
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
		map.put(NormalExcelConstants.FILE_NAME, "基础设备信息");
		map.put(NormalExcelConstants.CLASS, BaseDevicePage.class);
		map.put(NormalExcelConstants.PARAMS, new ExportParams("基础设备信息列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
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
		req.setAttribute("controller_name", "baseDeviceController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "基础设备信息列表信息", produces = "application/json", httpMethod = "GET")
	public ResponseMessage<List<BaseDevicePage>> list() {
		List<BaseDeviceEntity> list = baseDeviceService.getList(BaseDeviceEntity.class);
		List<BaseDevicePage> pageList = new ArrayList<BaseDevicePage>();
		if (list != null && list.size() > 0) {
			for (BaseDeviceEntity entity : list) {
				try {
					BaseDevicePage page = new BaseDevicePage();
					MyBeanUtils.copyBeanNotNull2Bean(entity, page);
					Object deviceBy0 = entity.getId();
					String hql0 = "from BaseDeviceDataEntity where 1 = 1 AND deviceBy = ? ";
					List<BaseDeviceDataEntity> baseDeviceDataOldList = this.baseDeviceService.findHql(hql0, deviceBy0);
					page.setBaseDeviceDataList(baseDeviceDataOldList);
					pageList.add(page);
				} catch (Exception e) {
					logger.info(e.getMessage());
				}
			}
		}
		return Result.success(pageList);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据ID获取基础设备信息信息", notes = "根据ID获取基础设备信息信息", httpMethod = "GET", produces = "application/json")
	public ResponseMessage<?> get(@ApiParam(required = true, name = "id", value = "ID") @PathVariable("id") String id) {
		BaseDeviceEntity task = baseDeviceService.get(BaseDeviceEntity.class, id);
		if (task == null) {
			return Result.error("根据ID获取基础设备信息信息为空");
		}
		BaseDevicePage page = new BaseDevicePage();
		try {
			MyBeanUtils.copyBeanNotNull2Bean(task, page);
			Object deviceBy0 = task.getId();
			String hql0 = "from BaseDeviceDataEntity where 1 = 1 AND deviceBy = ? ";
			List<BaseDeviceDataEntity> baseDeviceDataOldList = this.baseDeviceService.findHql(hql0, deviceBy0);
			page.setBaseDeviceDataList(baseDeviceDataOldList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.success(page);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "创建基础设备信息")
	public ResponseMessage<?> create(@ApiParam(name = "基础设备信息对象") @RequestBody BaseDevicePage baseDevicePage, UriComponentsBuilder uriBuilder) {
		// 调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<BaseDevicePage>> failures = validator.validate(baseDevicePage);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}
		try {
			baseDeviceService.addMain(baseDevicePage);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Result.error("保存基础设备信息失败");
		}
		return Result.success();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "更新基础设备信息", notes = "更新基础设备信息")
	public ResponseMessage<?> update(@RequestBody BaseDevicePage baseDevicePage) {
		// 调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<BaseDevicePage>> failures = validator.validate(baseDevicePage);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}
		try {
			baseDeviceService.updateMain(baseDevicePage);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Result.error("更新基础设备信息失败");
		}
		// 按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return Result.success();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(value = "删除基础设备信息")
	public ResponseMessage<?> delete(@ApiParam(name = "id", value = "ID", required = true) @PathVariable("id") String id) {
		logger.info("delete[{}]" + id);
		// 验证
		if (StringUtils.isEmpty(id)) {
			return Result.error("ID不能为空");
		}
		try {
			BaseDeviceEntity baseDevice = baseDeviceService.get(BaseDeviceEntity.class, id);
			baseDeviceService.delMain(baseDevice);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("基础设备信息删除失败");
		}

		return Result.success();
	}

}
