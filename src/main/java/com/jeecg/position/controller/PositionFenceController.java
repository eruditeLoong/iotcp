package com.jeecg.position.controller;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecg.position.entity.PositionFenceEntity;
import com.jeecg.position.entity.PositionFencePointEntity;
import com.jeecg.position.page.PositionFencePage;
import com.jeecg.position.service.PositionFenceServiceI;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


/**   
 * @Title: Controller
 * @Description: 围栏信息
 * @author onlineGenerator
 * @date 2019-10-06 22:25:53
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/positionFenceController")
public class PositionFenceController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(PositionFenceController.class);

	@Autowired
	private PositionFenceServiceI positionFenceService;
	@Autowired
	private SystemService systemService;

	/**
	 * 围栏信息列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/position/positionFenceList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param positionFence
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(PositionFenceEntity positionFence,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(PositionFenceEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, positionFence,request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.positionFenceService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	@RequestMapping(params = "getFencePoints")
	@ResponseBody
	public AjaxJson getFencePoints() {
		AjaxJson j = new AjaxJson();
		String message = "获取围栏顶点集合成功";
		try{
			String sql = "select f.id id,f.name name, f.type type, f.color color, p.id pointId, p.point point from position_fence f left join jform_scene s on f.scene_by=s.id left join position_fence_point p on p.fence_by=f.id where 1=1 and s.is_default_view=1";
			List<Map<String, Object>> points = positionFenceService.findForJdbc(sql);
			JSONArray fenceArr = new JSONArray();
			Map<String, Map<String, Object>> map = new HashMap<>();
			for (Map<String, Object> fMap: points){
				JSONObject jObj = new JSONObject();
				if(map.get(fMap.get("id")) == null){
					JSONArray jArr = new JSONArray();
					jObj.put("x", Double.valueOf(fMap.get("point").toString().split(",")[0]));
					jObj.put("y", Double.valueOf(fMap.get("point").toString().split(",")[1]));
					jObj.put("z", Double.valueOf(fMap.get("point").toString().split(",")[2]));
					jArr.add(jObj);
					fMap.put("points", jArr);
					map.put(fMap.get("id").toString(), fMap);
				}else{
					Map<String, Object> pMap = map.get(fMap.get("id"));
					JSONArray jArr = (JSONArray) pMap.get("points");
					jObj.put("x", Double.valueOf(fMap.get("point").toString().split(",")[0]));
					jObj.put("y", Double.valueOf(fMap.get("point").toString().split(",")[1]));
					jObj.put("z", Double.valueOf(fMap.get("point").toString().split(",")[2]));
					jArr.add(jObj);
				}
			}
			JSONArray arr = new JSONArray();
			Set<String> set = map.keySet();
			for (String key : set){
				arr.add(net.sf.json.JSONObject.fromObject(map.get(key)));
			}
			j.setObj(arr);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "获取围栏顶点集合失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 删除围栏信息
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(PositionFenceEntity positionFence, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		positionFence = systemService.getEntity(PositionFenceEntity.class, positionFence.getId());
		String message = "围栏信息删除成功";
		try{
			positionFenceService.delMain(positionFence);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "围栏信息删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量删除围栏信息
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		String message = "围栏信息删除成功";
		try{
			for(String id:ids.split(",")){
				PositionFenceEntity positionFence = systemService.getEntity(PositionFenceEntity.class,
				id
				);
				positionFenceService.delMain(positionFence);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "围栏信息删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加围栏信息
	 * 
	 * @param positionFencePage
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(HttpServletRequest request,PositionFencePage positionFencePage) {
		AjaxJson j = new AjaxJson();
		String message = "添加成功";
		try {
			positionFenceService.addMain(positionFencePage);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "围栏信息添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	/**
	 * 更新围栏信息
	 * 
	 * @param positionFencePage
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(HttpServletRequest request,PositionFencePage positionFencePage) {
		AjaxJson j = new AjaxJson();
		String message = "更新成功";
		try{
			positionFenceService.updateMain(positionFencePage);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "更新围栏信息失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 围栏信息新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(String sceneBy, Integer type, String color, String points, HttpServletRequest req) {
		PositionFencePage positionFencePage = new PositionFencePage();
		positionFencePage.setSceneBy(sceneBy);
		positionFencePage.setType(type);
		positionFencePage.setColor("#"+color);
		JSONArray pointArr = JSONArray.parseArray(points);
		if(pointArr != null){
			List<PositionFencePointEntity> pointList = new ArrayList<>();
			for (int i=0;i< pointArr.size();i++){
				PositionFencePointEntity pFencePoint = new PositionFencePointEntity();
				String x = pointArr.getJSONObject(i).getString("x");
				String y = pointArr.getJSONObject(i).getString("y");
				String z = pointArr.getJSONObject(i).getString("z");
				pFencePoint.setPoint(x + "," + y + "," + z);
				pFencePoint.setSort(i);
				pointList.add(pFencePoint);
			}
			positionFencePage.setPotionFencePointList(pointList);
		}
		req.setAttribute("positionFencePage", positionFencePage);
		return new ModelAndView("com/jeecg/position/positionFence-add");
	}
	
	/**
	 * 围栏信息编辑页面跳转
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(HttpServletRequest req) {
		String id = req.getParameter("id");
		PositionFencePage positionFencePage = new PositionFencePage();
		if (StringUtil.isNotEmpty(id)) {
			PositionFenceEntity positionFence = positionFenceService.getEntity(PositionFenceEntity.class,id);
			try {
				MyBeanUtils.copyBeanNotNull2Bean(positionFence,positionFencePage);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//===================================================================================
		    //查询-围栏顶点集合
		    List<PositionFencePointEntity> positionFencePointEntityList = systemService.findByProperty(PositionFencePointEntity.class, "fenceBy",id);
		    if(positionFencePointEntityList ==null || positionFencePointEntityList.size()<=0){
		    	positionFencePointEntityList = new ArrayList<PositionFencePointEntity>();
		    	positionFencePointEntityList.add(new PositionFencePointEntity());
		    }
		    positionFencePage.setPotionFencePointList(positionFencePointEntityList);
		}
		req.setAttribute("positionFencePage", positionFencePage);
		return new ModelAndView("com/jeecg/position/positionFence-update");
	}
	
    /**
    * 导出excel
    *
    * @param request
    * @param response
    */
    @RequestMapping(params = "exportXls")
    public String exportXls(PositionFenceEntity positionFence,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid,ModelMap map) {
    	CriteriaQuery cq = new CriteriaQuery(PositionFenceEntity.class, dataGrid);
    	//查询条件组装器
    	org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, positionFence);
    	try{
    	//自定义追加查询条件
    	}catch (Exception e) {
    		throw new BusinessException(e.getMessage());
    	}
    	cq.add();
    	List<PositionFenceEntity> list=this.positionFenceService.getListByCriteriaQuery(cq, false);
    	List<PositionFencePage> pageList=new ArrayList<PositionFencePage>();
        if(list!=null&&list.size()>0){
        	for(PositionFenceEntity entity:list){
        		try{
        			PositionFencePage page=new PositionFencePage();
        			MyBeanUtils.copyBeanNotNull2Bean(entity,page);
           			Object fenceBy0 = entity.getId();
					String hql0 = "from PotionFencePointEntity where 1 = 1 AND fenceBy = ? ";
					List<PositionFencePointEntity> positionFencePointEntityList = systemService.findHql(hql0,fenceBy0);
					page.setPotionFencePointList(positionFencePointEntityList);
            		pageList.add(page);
            	}catch(Exception e){
            		logger.info(e.getMessage());
            	}
            }
        }
        map.put(NormalExcelConstants.FILE_NAME,"围栏信息");
        map.put(NormalExcelConstants.CLASS,PositionFencePage.class);
        map.put(NormalExcelConstants.PARAMS,new ExportParams("围栏信息列表", "导出人:Jeecg", "导出信息"));
        map.put(NormalExcelConstants.DATA_LIST,pageList);
        return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

    /**
	 * 通过excel导入数据
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
				List<PositionFencePage> list =  ExcelImportUtil.importExcel(file.getInputStream(), PositionFencePage.class, params);
				for (PositionFencePage page : list) {
		            positionFenceService.addMain(page);
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
	* 导出excel 使模板
	*/
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(ModelMap map) {
		map.put(NormalExcelConstants.FILE_NAME,"围栏信息");
		map.put(NormalExcelConstants.CLASS,PositionFencePage.class);
		map.put(NormalExcelConstants.PARAMS,new ExportParams("围栏信息列表", "导出人:"+ ResourceUtil.getSessionUser().getRealName(),
		"导出信息"));
		map.put(NormalExcelConstants.DATA_LIST,new ArrayList());
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	* 导入功能跳转
	*
	* @return
	*/
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name", "positionFenceController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

 	
	
}
