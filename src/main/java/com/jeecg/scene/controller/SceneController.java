package com.jeecg.scene.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecg.device.entity.BaseDeviceDataEntity;
import com.jeecg.device.entity.BaseDeviceEntity;
import com.jeecg.scene.entity.SceneDeviceDepolyEntity;
import com.jeecg.scene.entity.SceneEntity;
import com.jeecg.scene.service.SceneServiceI;
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
import org.jeecgframework.web.cgform.entity.upload.CgUploadEntity;
import org.jeecgframework.web.cgform.service.config.CgFormFieldServiceI;
import org.jeecgframework.web.system.manager.ClientManager;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
 * @Title: Controller
 * @Description: 场景管理
 * @author onlineGenerator
 * @date 2018-12-29 13:04:32
 * @version V1.0
 *
 */
@Api(value = "Scene", description = "场景管理", tags = "sceneController")
@Controller
@RequestMapping("/sceneController")
public class SceneController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(SceneController.class);
	@Autowired
	private SceneServiceI sceneService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	@Autowired
	private CgFormFieldServiceI cgFormFieldService;
	@Resource
	private ClientManager clientManager;

	/**
	 * 场景管理列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/scene/sceneList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param scene
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(SceneEntity scene, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(SceneEntity.class, dataGrid);
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, scene, request.getParameterMap());
		try {
			// 自定义追加查询条件

		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.sceneService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除场景管理
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(SceneEntity scene, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		scene = systemService.getEntity(SceneEntity.class, scene.getId());
		message = "场景管理删除成功";
		try {
			// 1、判断是否可以删除
			String m = sceneService.isAllowDel(scene);
			if(StringUtil.isNotBlank(m)){
				message = m;
				j.setMsg(message);
				return j;
			}
			// 2、执行删除文件
			systemService.delFileByPath(scene.getScene3d());
			systemService.delFileByPath(scene.getScene2d());
			// 3、执行删除
			sceneService.delete(scene);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "场景管理删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量删除场景管理
	 * 
	 * @return
	 */
	@RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "场景管理删除成功";
		try {
			for (String id : ids.split(",")) {
				SceneEntity scene = systemService.getEntity(SceneEntity.class, id);
				// 1、判断是否可以删除
				String m = sceneService.isAllowDel(scene);
				if(StringUtil.isNotBlank(m)){
					message = m;
					j.setMsg(message);
					return j;
				}
				// 2、执行删除文件
				systemService.delFileByPath(scene.getScene3d());
				systemService.delFileByPath(scene.getScene2d());
				// 3、执行删除
				sceneService.delete(scene);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "场景管理删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加场景管理
	 * 
	 * @param scene
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(SceneEntity scene, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "场景管理添加成功";
		try {
			sceneService.save(scene);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "场景管理添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 更新场景管理
	 * 
	 * @param scene
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(SceneEntity scene, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "场景管理更新成功";
		SceneEntity t = sceneService.get(SceneEntity.class, scene.getId());
		t.setIsAutoRotate(false);
		t.setIsDevicePanel(false);
		t.setIsDeviceRelation(false);
		t.setIsDeviceStatus(false);
		t.setIsLabelDisplay(false);
		t.setIsShadowDisplay(false);
		t.setIsStatsDisplay(false);
		try {
			MyBeanUtils.copyBeanNotNull2Bean(scene, t);
			sceneService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "场景管理更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	@RequestMapping(params = "doSetDefectScene")
	@ResponseBody
	public AjaxJson doSetDefectScene(SceneEntity scene, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "设置默认场景成功";
		try {
			// 先取消所有设置
			sceneService.executeSql("update jform_scene set is_default_view=0 where is_default_view=1");
			// 再设置
			sceneService.executeSql("update jform_scene set is_default_view=1 where id=?", scene.getId());
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "设置默认场景失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	@RequestMapping(params = "doUpdateDevice")
	@ResponseBody
	public AjaxJson doUpdateDevice(@RequestBody JSONObject obj, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "场景设备更新成功";
		try {
			// 解析json数据
			String data = obj.toJSONString();
			JSONObject json = JSON.parseObject(data);
			JSONArray list = json.getJSONArray("deviceData");
			// 先删除，再添加
			String sceneBy = json.getString("sceneId");
//			List<SceneDeviceDepolyEntity> sddList = systemService.findByProperty(SceneDeviceDepolyEntity.class, "sceneBy", sceneBy);
//			for (SceneDeviceDepolyEntity e : sddList) {
			// systemService.delete(e);
			// System.out.println("删除-->" + e.getId());
//			}
			for (Object o : list) {
				JSONObject json1 = JSONObject.parseObject(o.toString());
				String threeData = json1.getString("threeData");
				SceneDeviceDepolyEntity sdd = new SceneDeviceDepolyEntity();
				sdd.setId(json1.getString("id"));
				sdd.setSceneBy(sceneBy);
				sdd.setDeviceBy(json1.getString("deviceBy"));
				sdd.setDeviceParentBy(json1.getString("deviceParentBy"));
				sdd.setDeviceCode(json1.getString("deviceCode"));
				sdd.setThreeData(threeData);
				SceneDeviceDepolyEntity t = sceneService.get(SceneDeviceDepolyEntity.class, json1.getString("id"));
				MyBeanUtils.copyBeanNotNull2Bean(sdd, t);
				sceneService.saveOrUpdate(t);
			}
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "场景设备更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 场景管理新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(SceneEntity scene, HttpServletRequest req) {
		scene.setCreateDate(new Date());
		/* 当前登录用户 */
		HttpSession session = ContextHolderUtils.getSession();
		TSUser user = (TSUser) session.getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		scene.setCreateBy(user.getUserName());
		req.setAttribute("scene", scene);
		return new ModelAndView("com/jeecg/scene/scene-add");
	}

	/**
	 * 场景管理编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(SceneEntity scene, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(scene.getId())) {
			scene = sceneService.getEntity(SceneEntity.class, scene.getId());
			req.setAttribute("scene", scene);
		}
		return new ModelAndView("com/jeecg/scene/scene-update");
	}

	@RequestMapping(params = "getDeployFileUrl")
	@ResponseBody
	public AjaxJson getDeployFileUrl(String sceneId, HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		SceneEntity scene = new SceneEntity();
		if (StringUtil.isNotEmpty(sceneId)) {
			scene = sceneService.getEntity(SceneEntity.class, sceneId);
			j.setObj(scene);
			j.setSuccess(true);
		} else {
			j.setSuccess(false);
			j.setObj("sceneId is null!");
		}
		return j;
	}

	/**
	 * 2D场景编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goDeploy2D")
	public ModelAndView goDeploy2D(SceneEntity scene, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(scene.getId())) {
			scene = sceneService.getEntity(SceneEntity.class, scene.getId());
			req.setAttribute("scene", scene);
		}
		return new ModelAndView("com/jeecg/scene/sceneDeploy2D");
	}

	/**
	 * 3D场景编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goSceneDeployFrame")
	public ModelAndView goSceneDeployFrame(SceneEntity scene, HttpServletRequest req) {
		if (StringUtil.isBlank(scene.getId())) {
			List<SceneEntity> sceneList = systemService.findByProperty(SceneEntity.class, "isDefaultView", true);
			if (sceneList != null && sceneList.size() > 0) {
				scene = systemService.get(SceneEntity.class, sceneList.get(0).getId());
			}
		} else {
			scene = systemService.get(SceneEntity.class, scene.getId());
		}
		JSONObject json = new JSONObject();
		json.put("id", scene.getId());
		json.put("name", scene.getName());
		json.put("modelFile3D", scene.getScene3d());
		json.put("threeData", JSONObject.parse(scene.getThreeData()));
		req.setAttribute("scene", json);
		return new ModelAndView("com/jeecg/scene/sceneDeployFrame");
	}

	/**
	 * 获取所有场景及所有设备，以树的形式返回
	 * 数据数据图表、场景部署页面调用
	 * @return
	 */
	@RequestMapping(params = "getSceneDeviceDeployTree")
	@ResponseBody
	public AjaxJson getSceneDeviceDeployTree() {
		List<SceneEntity> list = sceneService.findByProperty(SceneEntity.class, "status", 1);
		JSONArray jsonArr = new JSONArray();
		for (SceneEntity se : list) {
			JSONObject json = new JSONObject();
			json.put("id", se.getId());
			json.put("name", se.getName());
			json.put("type", "scene");
			json.put("deployInfo", JSONObject.parse(se.getThreeData()));
			json.put("modelFile3D", se.getScene3d());

			jsonArr.add(json);

			List<SceneDeviceDepolyEntity> sddList = systemService.findByProperty(SceneDeviceDepolyEntity.class, "sceneBy", se.getId());
			for (SceneDeviceDepolyEntity sdd : sddList) {
				JSONObject tree = new JSONObject();
				tree.put("id", sdd.getId());
				tree.put("pid", StringUtil.isBlank(sdd.getDeviceParentBy()) ? se.getId() : sdd.getDeviceParentBy());
				BaseDeviceEntity bd = systemService.get(BaseDeviceEntity.class, sdd.getDeviceBy());
				if(bd == null){
					break;
				}
				tree.put("name", bd.getName() + "[" + sdd.getDeviceCode() + "]");
				tree.put("type", bd.getType());
				tree.put("deviceDeploy", JSONObject.toJSON(sdd.getThreeData()));
				jsonArr.add(tree);
			}
		}
		AjaxJson j = new AjaxJson();
		j.setObj(jsonArr);
		return j;
	}

	/**
	 * 获取默认场景信息
	 * 
	 * @return
	 */
	@RequestMapping(params = "goDefaultScene", method = RequestMethod.GET)
	@ResponseBody
	public AjaxJson goDefaultScene() {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "获取默认场景信息成功";
		JSONObject json = new JSONObject();
		try {
			List<SceneEntity> sceneList = systemService.findByProperty(SceneEntity.class, "isDefaultView", true);
			if (sceneList != null && sceneList.size() > 0) {
				SceneEntity scene = systemService.get(SceneEntity.class, sceneList.get(0).getId());
				json.put("id", scene.getId());
				json.put("name", scene.getName());
				json.put("modelFile3D", scene.getScene3d());
				json.put("threeData", JSONObject.parse(scene.getThreeData()));

				json.put("isAutoRotate", scene.getIsAutoRotate());
				json.put("isDeviceRelation", scene.getIsDeviceRelation());
				json.put("isDevicePanel", scene.getIsDevicePanel());
				json.put("isLabelDisplay", scene.getIsLabelDisplay());
				json.put("isStatsDisplay", scene.getIsStatsDisplay());
				json.put("isDeviceStatus", scene.getIsDeviceStatus());
				json.put("isShadowDisplay", scene.getIsShadowDisplay());
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "获取默认场景信息失败";
			throw new BusinessException(e.getMessage());
		}
		j.setObj(json);
		j.setMsg(message);
		return j;
	}

	@RequestMapping(params = "goDeviceFrame")
	public ModelAndView goDeviceFrame(SceneEntity scene, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(scene.getId())) {
			scene = sceneService.getEntity(SceneEntity.class, scene.getId());
			req.setAttribute("scene", scene);
		}
		return new ModelAndView("com/jeecg/scene/deviceFrame");
	}

	/**
	 * 获取场景设备配置列表 zhouwr 2019/04/18
	 * 
	 * @param sceneId
	 * @return
	 */
//	@RequestMapping(params = "getConfDevice")
//	@ResponseBody
//	public AjaxJson getConfDevice(String sceneId) {
//		AjaxJson j = new AjaxJson();
//		JSONArray jsonList = new JSONArray();
//		// 查询场景下的设备，先查询跟节点，根据场景id和设备信息为空
//		List<SceneDeviceEntity> psdcList = systemService.findHql("from SceneDeviceEntity where sceneBy=? and deviceBy is null", sceneId);
//		for (SceneDeviceEntity psd : psdcList) {
//			// 查询设备配置信息
//			DeviceConfEntity dc = systemService.get(DeviceConfEntity.class, psd.getDeviceConfBy());
//			JSONObject json = new JSONObject();
//			json.put("id", dc.getId());
//			json.put("name", dc.getTitle());
//			json.put("open", true);
//			String entity = dc.getEntityName();
//			// 子节点
//			List<SceneDeviceEntity> csdcList = systemService.findHql("from SceneDeviceEntity where sceneBy=? and deviceConfBy=? and deviceBy is not null", sceneId, psd.getDeviceConfBy());
//			JSONArray jsonList1 = new JSONArray();
//			for (SceneDeviceEntity csd : csdcList) {
//				List<Object> objs = systemService.findHql("from " + entity + " where id=? and status=?", csd.getDeviceBy(), 1);
//				for (Object obj : objs) {
//					Map<String, Object> values = GenericsUtils.getKeyAndValue(obj);
//					JSONObject json1 = new JSONObject();
//					json1.put("id", values.get("id")); // 设备id
//					json1.put("name", values.get("name")); // 设备
//					json1.put("type", values.get("type")); // 设备
//					json1.put("confBy", dc.getId()); // 设备配置id
//					json1.put("modelFile", values.get("modelFile")); // 设备模型文件
//					json1.put("entity", entity); // 设备类名
//					jsonList1.add(json1);
//				}
//			}
//			json.put("children", jsonList1);
//			jsonList.add(json);
//		}
//		System.out.println(jsonList.toJSONString());
//		j.setObj(jsonList);
//		return j;
//	}

	/**
	 * 根据场景id，获取场景下所有部署设备
	 * 1、3D部署页面、3D展示页面调用：回显已经部署的设备
	 * 2、场景管理页面调用
	 * 3、报警添加和修改页面
	 * @param sceneId
	 * @return
	 */
	@RequestMapping(params = "getDeployDeviceListBySceneId")
	@ResponseBody
	public AjaxJson getDeployDeviceListBySceneId(String sceneId) {
		AjaxJson j = new AjaxJson();
		JSONArray jsonList = new JSONArray();
		String hql = "from SceneDeviceDepolyEntity where 1=1 and sceneBy=? order by CAST(deviceCode as integer)";
		List<SceneDeviceDepolyEntity> sddList = systemService.findHql(hql, sceneId);
//		List<SceneDeviceDepolyEntity> sddList = systemService.findByProperty(SceneDeviceDepolyEntity.class, "sceneBy", sceneId);
		for (SceneDeviceDepolyEntity sdd : sddList) {
			// 直接拿到 部署设备
			JSONObject json = new JSONObject();
			json.put("id", sdd.getId()); // id
//			json.put("pid", StringUtil.isBlank(sdd.getDeviceParentBy()) ? "" : sdd.getDeviceParentBy());
			json.put("parentBy", sdd.getDeviceParentBy()); // 父级设备
			json.put("deviceBy", sdd.getDeviceBy()); // 设备id
			json.put("threeData", JSONObject.parse(sdd.getThreeData())); // 设备位置部署信息
			json.put("code", sdd.getDeviceCode()); // 设备配置code
			// 基础设备: 根据id查询，返回唯一
			BaseDeviceEntity bDevice = systemService.get(BaseDeviceEntity.class, sdd.getDeviceBy());
			if (bDevice != null) {
				json.put("name", bDevice.getName()); // 设备名称
				json.put("modelFile", bDevice.getModelFile()); // 设备模型文件
				json.put("type", bDevice.getType()); // 判断设备类型是终端还是网关设备
				// 获取数据节点
				JSONArray bddArr = new JSONArray();
				List<BaseDeviceDataEntity> bddList = systemService.findByProperty(BaseDeviceDataEntity.class, "deviceBy", bDevice.getId());
				for (BaseDeviceDataEntity bdd : bddList){
					JSONObject bddJSON = (JSONObject) JSONObject.toJSON(bdd);
					bddArr.add(bddJSON);
				}
				json.put("dataNode", bddArr);
			}
			jsonList.add(json);
		}
		j.setObj(jsonList);
		return j;
	}

	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name", "sceneController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(SceneEntity scene, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(SceneEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, scene, request.getParameterMap());
		List<SceneEntity> scenes = this.sceneService.getListByCriteriaQuery(cq, false);
		modelMap.put(NormalExcelConstants.FILE_NAME, "场景管理");
		modelMap.put(NormalExcelConstants.CLASS, SceneEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("场景管理列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST, scenes);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(SceneEntity scene, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, ModelMap modelMap) {
		modelMap.put(NormalExcelConstants.FILE_NAME, "场景管理");
		modelMap.put(NormalExcelConstants.CLASS, SceneEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("场景管理列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
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
				List<SceneEntity> listSceneEntitys = ExcelImportUtil.importExcel(file.getInputStream(), SceneEntity.class, params);
				for (SceneEntity scene : listSceneEntitys) {
					sceneService.save(scene);
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
	 * 获取文件附件信息
	 * 
	 * @param id scene主键id
	 */
	@RequestMapping(params = "getFiles")
	@ResponseBody
	public AjaxJson getFiles(String id) {
		List<CgUploadEntity> uploadBeans = cgFormFieldService.findByProperty(CgUploadEntity.class, "cgformId", id);
		List<Map<String, Object>> files = new ArrayList<Map<String, Object>>(0);
		for (CgUploadEntity b : uploadBeans) {
			String title = b.getAttachmenttitle();// 附件名
			String fileKey = b.getId();// 附件主键
			String path = b.getRealpath();// 附件路径
			String field = b.getCgformField();// 表单中作为附件控件的字段
			Map<String, Object> file = new HashMap<String, Object>();
			file.put("title", title);
			file.put("fileKey", fileKey);
			file.put("path", path);
			file.put("field", field == null ? "" : field);
			files.add(file);
		}
		AjaxJson j = new AjaxJson();
		j.setObj(files);
		return j;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "场景管理列表信息", produces = "application/json", httpMethod = "GET")
	public ResponseMessage<List<SceneEntity>> list() {
		List<SceneEntity> listScenes = sceneService.getList(SceneEntity.class);
		return Result.success(listScenes);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据ID获取场景管理信息", notes = "根据ID获取场景管理信息", httpMethod = "GET", produces = "application/json")
	public ResponseMessage<?> get(@ApiParam(required = true, name = "id", value = "ID") @PathVariable("id") String id) {
		SceneEntity task = sceneService.get(SceneEntity.class, id);
		if (task == null) {
			return Result.error("根据ID获取场景管理信息为空");
		}
		return Result.success(task);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "创建场景管理")
	public ResponseMessage<?> create(@ApiParam(name = "场景管理对象") @RequestBody SceneEntity scene, UriComponentsBuilder uriBuilder) {
		// 调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
			Set<ConstraintViolation<SceneEntity>> failures = validator.validate(scene);
			if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		// 保存
		try {
			sceneService.save(scene);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("场景管理信息保存失败");
		}
		return Result.success(scene);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "更新场景管理", notes = "更新场景管理")
	public ResponseMessage<?> update(@ApiParam(name = "场景管理对象") @RequestBody SceneEntity scene) {
		// 调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SceneEntity>> failures = validator.validate(scene);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		// 保存
		try {
			sceneService.saveOrUpdate(scene);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("更新场景管理信息失败");
		}

		// 按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return Result.success("更新场景管理信息成功");
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "删除场景管理")
	public ResponseMessage<?> delete(@ApiParam(name = "id", value = "ID", required = true) @PathVariable("id") String id) {
		logger.info("delete[{}]", id);
		// 验证
		if (StringUtils.isEmpty(id)) {
			return Result.error("ID不能为空");
		}
		try {
			sceneService.deleteEntityById(SceneEntity.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("场景管理删除失败");
		}

		return Result.success();
	}
}
