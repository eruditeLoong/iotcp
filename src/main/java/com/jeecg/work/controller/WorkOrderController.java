package com.jeecg.work.controller;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecg.position.entity.PositionUserEntity;
import com.jeecg.position.entity.PositionUserOrgEntity;
import com.jeecg.work.entity.WorkOrderEntity;
import com.jeecg.work.service.WorkOrderServiceI;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.*;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.cgform.entity.upload.CgUploadEntity;
import org.jeecgframework.web.cgform.service.config.CgFormFieldServiceI;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**   
 * @Title: Controller  
 * @Description: 工作票管理
 * @author onlineGenerator
 * @date 2018-10-24 15:41:52
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/workOrderController")
public class WorkOrderController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(WorkOrderController.class);

	@Autowired
	private WorkOrderServiceI workOrderService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private CgFormFieldServiceI cgFormFieldService;
	

	/**
	 * 工作票管理列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/work/workOrderList2");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param workOrder
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(WorkOrderEntity workOrder,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(WorkOrderEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, workOrder, request.getParameterMap());
		try{
		//自定义追加查询条件
		
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.workOrderService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 删除工作票管理
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(WorkOrderEntity workOrder, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		workOrder = systemService.getEntity(WorkOrderEntity.class, workOrder.getId());
		message = "工作票管理删除成功";
		try{
			workOrderService.delete(workOrder);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "工作票管理删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除工作票管理
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "工作票管理删除成功";
		try{
			for(String id:ids.split(",")){
				WorkOrderEntity workOrder = systemService.getEntity(WorkOrderEntity.class, 
				id
				);
				workOrderService.delete(workOrder);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "工作票管理删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加工作票管理
	 * 
	 * @param workOrder
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(WorkOrderEntity workOrder, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "工作票管理添加成功";
		try{
			workOrderService.save(workOrder);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "工作票管理添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新工作票管理
	 * 
	 * @param workOrder
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(WorkOrderEntity workOrder, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "工作票管理更新成功";
		WorkOrderEntity t = workOrderService.get(WorkOrderEntity.class, workOrder.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(workOrder, t);
			workOrderService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "工作票管理更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 工作票管理新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(WorkOrderEntity workOrder, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(workOrder.getId())) {
			workOrder = workOrderService.getEntity(WorkOrderEntity.class, workOrder.getId());
			req.setAttribute("workOrder", workOrder);
		}
		return new ModelAndView("com/jeecg/work/workOrder-add");
	}
	/**
	 * 工作票管理编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(WorkOrderEntity workOrder, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(workOrder.getId())) {
			workOrder = workOrderService.getEntity(WorkOrderEntity.class, workOrder.getId());
			String[] orgIds = workOrder.getSysOrgCode().split(",");
			StringBuffer sb = new StringBuffer();
			for (String orgId: orgIds){
				PositionUserOrgEntity org = systemService.get(PositionUserOrgEntity.class, orgId);
				if(org != null){
					String orgName = org.getName();
					sb.append(orgName).append(",");
				}
			}
			req.setAttribute("workOrder", workOrder);
			req.setAttribute("orgName", sb.length()>0?sb.toString().substring(0, sb.toString().length()-1):"");
		}
		return new ModelAndView("com/jeecg/work/workOrder-update");
	}
	
	@RequestMapping(params = "goDetail")
	public ModelAndView goDetail(WorkOrderEntity workOrder, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(workOrder.getId())) {
			workOrder = workOrderService.getEntity(WorkOrderEntity.class, workOrder.getId());
			req.setAttribute("workOrder", workOrder);
		}
		return new ModelAndView("com/jeecg/work/workOrder-detail");
	}

	@RequestMapping(params = "userList")
	public ModelAndView userList(String branchId,String workId, HttpServletRequest req) {
		req.setAttribute("branchId",branchId);
		req.setAttribute("workId",workId);
		return new ModelAndView("com/jeecg/work/userList");
	}

	/**
	 * 根据厂家id获取厂家下所有员工
	 * 工作票详情页面调用
	 * @param id
	 * @return
	 */
	@RequestMapping(params = "getDetailUser")
	@ResponseBody
	public AjaxJson getDetailUser(String id) {
		String message = null;
		AjaxJson j = new AjaxJson();
		// select p.id, p.name, d.duty_name from tb_person p, tb_duty d where p.branch_id=6 and d.id=p.duty_id;
		List<Map<String, Object>> list = DynamicDBUtil.findList("position","select p.id, p.name, d.duty_name from tb_person p, tb_duty d where p.branch_id=? and d.id=p.duty_id", id);
		JSONArray jsons = new JSONArray();
		for (Map<String, Object> map : list) {
			JSONObject json = new JSONObject();
			json.put("id", map.get("id"));
			json.put("name", map.get("name"));
			json.put("duty", map.get("duty_name"));
			jsons.add(json);
		}
		message = "获取厂家员工成功";
		j.setObj(jsons);
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 根据厂家id、工作票id获取厂家工作票人员
	 * 工作票详情页面调用
	 * @param wid
	 * @param bid
	 * @return
	 */
	@RequestMapping(params = "getWorkUser")
	@ResponseBody
	public AjaxJson getWorkUser(String wid, String bid) {
		String message = null;
		AjaxJson j = new AjaxJson();
		// 根据工作票表单id，获取工作票表单详情
		WorkOrderEntity wo = workOrderService.get(WorkOrderEntity.class, wid);
		// 工作票下所有人员id
		String pids = wo.getPersonIds();
		String pidsArr[] = pids.split(","); // 逗号拆分成数组
		// 厂家下所有员工
		List<Map<String, Object>> list = DynamicDBUtil.findList("position","select p.id, p.name, d.duty_name from tb_person p, tb_duty d where p.branch_id=? and d.id=p.duty_id", bid);
		JSONArray jsons = new JSONArray();
		for (Map<String, Object> map : list) {
			// 遍历厂家下所有员工中，在该工作票中的员工
			for(String pid :pidsArr) {
				if(map.get("id").toString().equals(pid)) {
					JSONObject json = new JSONObject();
					json.put("id", map.get("id"));
					json.put("name", map.get("name"));
					json.put("duty", map.get("duty_name"));
					jsons.add(json);
				}
			}
		}
		message = "获取厂家员工成功";
		j.setObj(jsons);
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 根据厂家的id获取厂家详情及所属所有员工
	 * 新增和编辑调用
	 * @param workOrder
	 * @return
	 */
	@RequestMapping(params = "getBranchUser")
	@ResponseBody
	public AjaxJson getBranchUser(WorkOrderEntity workOrder) {
		String message = null;
		AjaxJson j = new AjaxJson();
		JSONArray jsons = new JSONArray();
		String sql = "select id, name, work from position_user_org where id in (\""+workOrder.getSysOrgCode().replaceAll("\\,", "\",\"")+"\")";
		List<Map<String, Object>> orgList = systemService.findForJdbc(sql);
		JSONObject json = null;
		for (Map<String, Object> map : orgList) {
			json = new JSONObject();
			json.put("id", map.get("id"));
			json.put("name", map.get("name"));
			json.put("work", map.get("work"));
			// 根据厂家查询所属人员
			List<PositionUserEntity> userList = systemService.findByProperty(PositionUserEntity.class, "company", map.get("id"));
			WorkOrderEntity wo = systemService.get(WorkOrderEntity.class, workOrder.getId());
			String[] users = wo.getPersonIds().split(",");
			JSONArray userArr = new JSONArray();
			for (PositionUserEntity user : userList) {
				JSONObject jUser = new JSONObject();
				jUser.put("id", user.getId());
				jUser.put("name", user.getName());
				jUser.put("duty", user.getTelephone());
				for(String u : users){
					if(u.equals(user.getId())){
						jUser.put("checked", true);
						break;
					}
				}
				userArr.add(jUser);
			}
			// 添加到部门json
			json.put("person", userArr);
			jsons.add(json);
		}
		message = "获取厂家及人员列表成功";
		System.out.println(jsons.toJSONString());
		j.setObj(jsons);
		j.setMsg(message);
		return j;
	}

	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","workOrderController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(WorkOrderEntity workOrder,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(WorkOrderEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, workOrder, request.getParameterMap());
		List<WorkOrderEntity> workOrders = this.workOrderService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"工作票管理");
		modelMap.put(NormalExcelConstants.CLASS,WorkOrderEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("工作票管理列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,workOrders);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(WorkOrderEntity workOrder,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"工作票管理");
    	modelMap.put(NormalExcelConstants.CLASS,WorkOrderEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("工作票管理列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
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
				List<WorkOrderEntity> listWorkOrderEntitys = ExcelImportUtil.importExcel(file.getInputStream(),WorkOrderEntity.class,params);
				for (WorkOrderEntity workOrder : listWorkOrderEntitys) {
					workOrderService.save(workOrder);
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
	
	/**
	 * 获取文件附件信息
	 * 
	 * @param id workOrder主键id
	 */
	@RequestMapping(params = "getFiles")
	@ResponseBody
	public AjaxJson getFiles(String id){
		List<CgUploadEntity> uploadBeans = cgFormFieldService.findByProperty(CgUploadEntity.class, "cgformId", id);
		List<Map<String,Object>> files = new ArrayList<Map<String,Object>>(0);
		for(CgUploadEntity b:uploadBeans){
			String title = b.getAttachmenttitle();//附件名
			String fileKey = b.getId();//附件主键
			String path = b.getRealpath();//附件路径
			String field = b.getCgformField();//表单中作为附件控件的字段
			Map<String, Object> file = new HashMap<String, Object>();
			file.put("title", title);
			file.put("fileKey", fileKey);
			file.put("path", path);
			file.put("field", field==null?"":field);
			files.add(file);
		}
		AjaxJson j = new AjaxJson();
		j.setObj(files);
		return j;
	}
	
}
