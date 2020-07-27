package com.jeecg.scene.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecg.device.entity.BaseDeviceEntity;
import com.jeecg.scene.entity.SceneDeviceDepolyEntity;
import com.jeecg.scene.entity.SceneDeviceEntity;
import com.jeecg.scene.service.SceneDeviceServiceI;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version V1.0
 * @Title: Controller
 * @Description: 场景设备
 * @date 2019-04-17 22:32:11
 */
@Controller
@RequestMapping("/sceneDeviceController")
public class SceneDeviceController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(SceneDeviceController.class);

	@Autowired
	private SceneDeviceServiceI sceneDeviceService;
	@Autowired
	private SystemService systemService;


	/**
	 * 场景设备列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/scene/sceneDeviceList");
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
	public void datagrid(SceneDeviceEntity sceneDevice, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(SceneDeviceEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sceneDevice, request.getParameterMap());
		try {
			//自定义追加查询条件

		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.sceneDeviceService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 获取场景下的设备配置树状列表
	 *
	 * @param sceneId
	 * @return
	 */
	@RequestMapping(params = "getDeviceConfTree")
	@ResponseBody
	public AjaxJson getDeviceConfTree(String sceneId) {
		AjaxJson j = new AjaxJson();
		j.setMsg("设备配置更新成功");
		try {
			// 场景-设备配置列表
			List<BaseDeviceEntity> bdList = systemService.getList(BaseDeviceEntity.class);
			List<SceneDeviceEntity> sdList = sceneDeviceService.findByProperty(SceneDeviceEntity.class, "sceneBy", sceneId);
			JSONArray jsonArr = new JSONArray();
			// 设置是否选中状态
			for (BaseDeviceEntity bd : bdList) {
				JSONObject json = new JSONObject();
				json.put("id", bd.getId());
				json.put("name", bd.getName());
				// 设置是否选中状态
				Boolean isChecked = false;
				for (SceneDeviceEntity sd : sdList) {
					if (StringUtil.isNotBlank(sd.getDeviceBy())) {
						if (bd.getId().equals(sd.getDeviceBy())) {
							isChecked = true;
							break;
						}
					}
				}
				json.put("checked", isChecked);
				jsonArr.add(json);
			}
			j.setObj(jsonArr);
		} catch (Exception e) {
			logger.error(ExceptionUtil.getExceptionMessage(e));
			j.setMsg("设备配置更新失败");
		}
		return j;
	}

	/**
	 * 删除场景设备
	 *
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(SceneDeviceEntity sceneDevice, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		sceneDevice = systemService.getEntity(SceneDeviceEntity.class, sceneDevice.getId());
		message = "场景设备删除成功";
		try {
			sceneDeviceService.delete(sceneDevice);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "场景设备删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量删除场景设备
	 *
	 * @return
	 */
	@RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "场景设备删除成功";
		try {
			for (String id : ids.split(",")) {
				SceneDeviceEntity sceneDevice = systemService.getEntity(SceneDeviceEntity.class,
						id
				);
				sceneDeviceService.delete(sceneDevice);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "场景设备删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加场景设备
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(SceneDeviceEntity sceneDevice, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "场景设备添加成功";
		try {
			sceneDeviceService.save(sceneDevice);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "场景设备添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 更新场景设备
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(SceneDeviceEntity sceneDevice, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "场景设备更新成功";
		SceneDeviceEntity t = sceneDeviceService.get(SceneDeviceEntity.class, sceneDevice.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(sceneDevice, t);
			sceneDeviceService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "场景设备更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	@RequestMapping(params = "doBatchUpdate")
	@ResponseBody
	public AjaxJson doBatchUpdate(@RequestBody JSONObject obj, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			String data = obj.toJSONString();
			JSONObject o = JSONObject.parseObject(data);
			// 2、删除所有
			String sql = "delete from jform_scene_device where 1=1 and scene_by=?";
			systemService.executeSql(sql, o.getString("sceneId"));
			// 3、再插d入
			JSONArray list = o.getJSONArray("ids");
			for (Object oo : list) {
				JSONObject json = JSONObject.parseObject(oo.toString());
				SceneDeviceEntity entity = new SceneDeviceEntity();
				entity.setSceneBy(o.getString("sceneId"));
				entity.setDeviceBy(json.getString("deviceBy"));
				sceneDeviceService.save(entity);
			}
			j.setMsg("设备配置更新成功");
		} catch (Exception e) {
			logger.error(ExceptionUtil.getExceptionMessage(e));
			j.setMsg("设备配置更新失败");
		}
		return j;
	}

	/**
	 * 场景设备新增页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(SceneDeviceEntity sceneDevice, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sceneDevice.getId())) {
			sceneDevice = sceneDeviceService.getEntity(SceneDeviceEntity.class, sceneDevice.getId());
			req.setAttribute("sceneDevice", sceneDevice);
		}
		return new ModelAndView("com/jeecg/scene/sceneDevice-add");
	}

	/**
	 * 场景设备编辑页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(SceneDeviceEntity sceneDevice, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sceneDevice.getId())) {
			sceneDevice = sceneDeviceService.getEntity(SceneDeviceEntity.class, sceneDevice.getId());
			req.setAttribute("sceneDevice", sceneDevice);
		}
		return new ModelAndView("com/jeecg/scene/sceneDevice-update");
	}

	/**
	 * 导入功能跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name", "sceneDeviceController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(SceneDeviceEntity sceneDevice, HttpServletRequest request, HttpServletResponse response
			, DataGrid dataGrid, ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(SceneDeviceEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sceneDevice, request.getParameterMap());
		List<SceneDeviceEntity> sceneDevices = this.sceneDeviceService.getListByCriteriaQuery(cq, false);
		modelMap.put(NormalExcelConstants.FILE_NAME, "场景设备");
		modelMap.put(NormalExcelConstants.CLASS, SceneDeviceEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("场景设备列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(),
				"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST, sceneDevices);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

	/**
	 * 导出excel 使模板
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(SceneDeviceEntity sceneDevice, HttpServletRequest request, HttpServletResponse response
			, DataGrid dataGrid, ModelMap modelMap) {
		modelMap.put(NormalExcelConstants.FILE_NAME, "场景设备");
		modelMap.put(NormalExcelConstants.CLASS, SceneDeviceEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("场景设备列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(),
				"导出信息"));
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
				List<SceneDeviceEntity> listSceneDeviceEntitys = ExcelImportUtil.importExcel(file.getInputStream(), SceneDeviceEntity.class, params);
				for (SceneDeviceEntity sceneDevice : listSceneDeviceEntitys) {
					sceneDeviceService.save(sceneDevice);
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
