/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package org.jeecgframework.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.web.system.pojo.base.TSBaseUser;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户工具类
 * 
 * @author ThinkGem
 * @version 2013-12-05
 */
public class UserUtils {

	@Autowired
	private static SystemService systemService;

	public static final String USER_CACHE = "userCache";
	public static final String USER_CACHE_ID_ = "id_";
	public static final String USER_CACHE_LOGIN_NAME_ = "ln";
	public static final String USER_CACHE_LIST_BY_OFFICE_ID_ = "oid_";

	public static final String CACHE_AUTH_INFO = "authInfo";
	public static final String CACHE_ROLE_LIST = "roleList";
	public static final String CACHE_MENU_LIST = "menuList";
	public static final String CACHE_AREA_LIST = "areaList";
	public static final String CACHE_OFFICE_LIST = "officeList";
	public static final String CACHE_OFFICE_ALL_LIST = "officeAllList";

	/**
	 * 根据ID获取用户
	 * 
	 * @param id
	 * @return 取不到返回null
	 */
	public static TSUser get(String id) {
		systemService = ApplicationContextUtil.getContext().getBean(SystemService.class);
		TSUser user = systemService.get(TSUser.class, id);
		return user;
	}
	
	public static TSUser getUserByUsername(String username) {
		systemService = ApplicationContextUtil.getContext().getBean(SystemService.class);
		List<TSUser> users = systemService.findByProperty(TSUser.class, "userName", username);
		if(users!=null && users.size()>0){
			return users.get(0);
		}
		return new TSUser();
	}

	/**
	 * 根据ID获取用户realname
	 * 
	 * @param id
	 * @return 取不到返回null
	 */
	public static String getUserRealname(String id) {
		if(StringUtil.isBlank(id)) {
			System.out.println("UserUtil>>getUserRealname>>id isBlank");
			return "";
		}
		systemService = ApplicationContextUtil.getContext().getBean(SystemService.class);
		/* 解决编辑表单回显问题，以逗号分隔的多个数值转换 zhouwr 2018/07/26 */
		String id1[] = id.split(",");
		StringBuffer realname = new StringBuffer();
		for (String idd : id1) {
			TSUser user = systemService.get(TSUser.class, idd);
			if (user != null) {
				realname.append(user.getRealName()).append(",");
			}
		}
		return realname.toString().substring(0, realname.toString().length()-1);
	}
	

	/**
	 * 根据username获取用户realname
	 * 
	 * @param id
	 * @return 取不到返回null
	 */
	public static String getUserRealnameByUsername(String username) {
		if(StringUtil.isBlank(username)) {
			return "";
		}
		systemService = ApplicationContextUtil.getContext().getBean(SystemService.class);
		/* 解决编辑表单回显问题，以逗号分隔的多个数值转换 zhouwr 2018/07/26 */
		String usernames1[] = username.split(",");
		StringBuffer realname = new StringBuffer();
		for (String un : usernames1) {
			List<TSBaseUser> users = systemService.findByProperty(TSBaseUser.class, "userName", un);
			TSBaseUser user = new TSBaseUser();
			if (users != null && users.size() > 0) {
				user = users.get(0);
				realname.append(user.getRealName()).append(",");
			}else {
				return username;
			}
		}
		return realname.toString().substring(0, realname.toString().length()-1);
	}
	
	public static void main(String[] args) {
		String username = "admin";
		String usernames1[] = username.split(",");
		System.out.println(Arrays.toString(usernames1));
	}
	/**
	 * 根据orgCode获取用户departname
	 * 
	 * @param id
	 * @return 取不到返回null
	 */
	public static String getDepartnameByOrgcode(String orgcode) {
		if(StringUtil.isBlank(orgcode)) {
			return "";
		}
		systemService = ApplicationContextUtil.getContext().getBean(SystemService.class);
		List<TSDepart> departs = systemService.findByProperty(TSDepart.class, "orgCode", orgcode);
		TSDepart depart = new TSDepart();
		if (departs != null && departs.size() > 0) {
			depart = departs.get(0);
		}
		return depart.getDepartname();
	}
	
	/**
	 * 根据部门id获取部门名称
	 * @param id
	 * @return
	 */
	public static String getDepartnameById(String id) {
		systemService = ApplicationContextUtil.getContext().getBean(SystemService.class);
		List<TSDepart> departs = systemService.findByProperty(TSDepart.class, "id", id);
		TSDepart depart = new TSDepart();
		if (departs != null && departs.size() > 0) {
			depart = departs.get(0);
		}
		return depart.getDepartname();
	}

	/**
	 * 获取当前用户
	 * 
	 * @return 取不到返回 new User()
	 */
	public static TSUser getUser() {
		HttpSession session = ContextHolderUtils.getSession();
		TSUser user = (TSUser) session.getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		return user;
	}
}
