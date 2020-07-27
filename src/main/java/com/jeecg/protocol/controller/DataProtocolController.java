package com.jeecg.protocol.controller;

import com.jeecg.protocol.entity.DataProtocolEntity;
import com.jeecg.protocol.service.DataProtocolServiceI;
import com.jeecg.protocol.page.DataProtocolPage;
import com.jeecg.protocol.entity.DataCustomEntity;

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
import org.jeecgframework.core.util.oConvertUtils;
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
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @Title: Controller
 * @Description: 数据协议
 * @author onlineGenerator
 * @date 2018-12-21 19:14:40
 * @version V1.0
 *
 */
@Api(value = "DataProtocol", description = "数据协议", tags = "dataProtocolController")
@Controller
@RequestMapping("/dataProtocolController")
public class DataProtocolController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(DataProtocolController.class);

	@Autowired
	private DataProtocolServiceI dataProtocolService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;

	/**
	 * 数据协议列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/protocol/dataProtocolList");
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
	public void datagrid(DataProtocolEntity dataProtocol, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(DataProtocolEntity.class, dataGrid);
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, dataProtocol,
				request.getParameterMap());
		try {
			// 自定义追加查询条件
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.dataProtocolService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除数据协议
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(DataProtocolEntity dataProtocol, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		dataProtocol = systemService.getEntity(DataProtocolEntity.class, dataProtocol.getId());
		String message = "数据协议删除成功";
		try {
			dataProtocolService.delMain(dataProtocol);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "数据协议删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量删除数据协议
	 * 
	 * @return
	 */
	@RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String message = "数据协议删除成功";
		try {
			for (String id : ids.split(",")) {
				DataProtocolEntity dataProtocol = systemService.getEntity(DataProtocolEntity.class, id);
				dataProtocolService.delMain(dataProtocol);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "数据协议删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加数据协议
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(HttpServletRequest request, DataProtocolPage dataProtocolPage) {
		AjaxJson j = new AjaxJson();
		String message = "添加成功";
		try {
			dataProtocolService.addMain(dataProtocolPage);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "数据协议添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 更新数据协议
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(HttpServletRequest request, DataProtocolPage dataProtocolPage) {
		AjaxJson j = new AjaxJson();
		String message = "更新成功";
		try {
			dataProtocolService.updateMain(dataProtocolPage);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "更新数据协议失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 数据协议新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(HttpServletRequest req) {
		return new ModelAndView("com/jeecg/protocol/dataProtocol-add");
	}

	/**
	 * 数据协议编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(HttpServletRequest req) {
		String id = req.getParameter("id");
		DataProtocolPage dataProtocolPage = new DataProtocolPage();
		if (StringUtil.isNotEmpty(id)) {
			DataProtocolEntity dataProtocol = dataProtocolService.getEntity(DataProtocolEntity.class, id);
			try {
				MyBeanUtils.copyBeanNotNull2Bean(dataProtocol, dataProtocolPage);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// ===================================================================================
			// 获取参数
			Object protocolBy0 = dataProtocol.getId();
			// ===================================================================================
			// 查询-自定义格式
			String hql0 = "from DataCustomEntity where 1 = 1 AND protocolBy = ? ";
			List<DataCustomEntity> dataCustomEntityList = systemService.findHql(hql0, protocolBy0);
			if (dataCustomEntityList == null || dataCustomEntityList.size() <= 0) {
				dataCustomEntityList = new ArrayList<DataCustomEntity>();
				dataCustomEntityList.add(new DataCustomEntity());
			}
			dataProtocolPage.setDataCustomList(dataCustomEntityList);
		}
		req.setAttribute("dataProtocolPage", dataProtocolPage);
		return new ModelAndView("com/jeecg/protocol/dataProtocol-update");
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(DataProtocolEntity dataProtocol, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid, ModelMap map) {
		CriteriaQuery cq = new CriteriaQuery(DataProtocolEntity.class, dataGrid);
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, dataProtocol);
		try {
			// 自定义追加查询条件
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		List<DataProtocolEntity> list = this.dataProtocolService.getListByCriteriaQuery(cq, false);
		List<DataProtocolPage> pageList = new ArrayList<DataProtocolPage>();
		if (list != null && list.size() > 0) {
			for (DataProtocolEntity entity : list) {
				try {
					DataProtocolPage page = new DataProtocolPage();
					MyBeanUtils.copyBeanNotNull2Bean(entity, page);
					Object protocolBy0 = entity.getId();
					String hql0 = "from DataCustomEntity where 1 = 1 AND protocolBy = ? ";
					List<DataCustomEntity> dataCustomEntityList = systemService.findHql(hql0, protocolBy0);
					page.setDataCustomList(dataCustomEntityList);
					pageList.add(page);
				} catch (Exception e) {
					logger.info(e.getMessage());
				}
			}
		}
		map.put(NormalExcelConstants.FILE_NAME, "数据协议");
		map.put(NormalExcelConstants.CLASS, DataProtocolPage.class);
		map.put(NormalExcelConstants.PARAMS, new ExportParams("数据协议列表", "导出人:Jeecg", "导出信息"));
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
				List<DataProtocolPage> list = ExcelImportUtil.importExcel(file.getInputStream(), DataProtocolPage.class,
						params);
				for (DataProtocolPage page : list) {
					dataProtocolService.addMain(page);
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
		map.put(NormalExcelConstants.FILE_NAME, "数据协议");
		map.put(NormalExcelConstants.CLASS, DataProtocolPage.class);
		map.put(NormalExcelConstants.PARAMS,
				new ExportParams("数据协议列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
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
		req.setAttribute("controller_name", "dataProtocolController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "数据协议列表信息", produces = "application/json", httpMethod = "GET")
	public ResponseMessage<List<DataProtocolPage>> list() {
		List<DataProtocolEntity> list = dataProtocolService.getList(DataProtocolEntity.class);
		List<DataProtocolPage> pageList = new ArrayList<DataProtocolPage>();
		if (list != null && list.size() > 0) {
			for (DataProtocolEntity entity : list) {
				try {
					DataProtocolPage page = new DataProtocolPage();
					MyBeanUtils.copyBeanNotNull2Bean(entity, page);
					Object protocolBy0 = entity.getId();
					String hql0 = "from DataCustomEntity where 1 = 1 AND protocolBy = ? ";
					List<DataCustomEntity> dataCustomOldList = this.dataProtocolService.findHql(hql0, protocolBy0);
					page.setDataCustomList(dataCustomOldList);
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
	@ApiOperation(value = "根据ID获取数据协议信息", notes = "根据ID获取数据协议信息", httpMethod = "GET", produces = "application/json")
	public ResponseMessage<?> get(@ApiParam(required = true, name = "id", value = "ID") @PathVariable("id") String id) {
		DataProtocolEntity task = dataProtocolService.get(DataProtocolEntity.class, id);
		if (task == null) {
			return Result.error("根据ID获取数据协议信息为空");
		}
		DataProtocolPage page = new DataProtocolPage();
		try {
			MyBeanUtils.copyBeanNotNull2Bean(task, page);
			Object protocolBy0 = task.getId();
			String hql0 = "from DataCustomEntity where 1 = 1 AND protocolBy = ? ";
			List<DataCustomEntity> dataCustomOldList = this.dataProtocolService.findHql(hql0, protocolBy0);
			page.setDataCustomList(dataCustomOldList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.success(page);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "创建数据协议")
	public ResponseMessage<?> create(@ApiParam(name = "数据协议对象") @RequestBody DataProtocolPage dataProtocolPage,
			UriComponentsBuilder uriBuilder) {
		// 调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<DataProtocolPage>> failures = validator.validate(dataProtocolPage);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}
		try {
			dataProtocolService.addMain(dataProtocolPage);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Result.error("保存数据协议失败");
		}
		return Result.success();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "更新数据协议", notes = "更新数据协议")
	public ResponseMessage<?> update(@RequestBody DataProtocolPage dataProtocolPage) {
		// 调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<DataProtocolPage>> failures = validator.validate(dataProtocolPage);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}
		try {
			dataProtocolService.updateMain(dataProtocolPage);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Result.error("更新数据协议失败");
		}
		// 按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return Result.success();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "删除数据协议")
	public ResponseMessage<?> delete(
			@ApiParam(name = "id", value = "ID", required = true) @PathVariable("id") String id) {
		logger.info("delete[{}]" + id);
		// 验证
		if (StringUtils.isEmpty(id)) {
			return Result.error("ID不能为空");
		}
		try {
			DataProtocolEntity dataProtocol = dataProtocolService.get(DataProtocolEntity.class, id);
			dataProtocolService.delMain(dataProtocol);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("数据协议删除失败");
		}

		return Result.success();
	}

}
