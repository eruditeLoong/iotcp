package org.jeecgframework.core.interceptors;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 字符集拦截器
 * 
 * @author  张代浩
 * 
 */
public class ActivitiInterceptor implements HandlerInterceptor {

	/**
	 * 在controller后拦截
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception) throws Exception {

	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 在controller前拦截
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		String requestPath = request.getRequestURI().substring(request.getContextPath().length());
		if(requestPath.indexOf("/activiti/")==-1 || excludeUrls.contains(requestPath) ||moHuContain(excludeContainUrls, requestPath)){
			return true;
		} else if(requestPath.indexOf(".jsp")==-1) {
			return true;
		}
		return false;
	}
	
	private List<String> excludeUrls;
	/**
	 * 包含匹配（请求链接包含该配置链接，就进行过滤处理）
	 */
	private List<String> excludeContainUrls;

	public List<String> getExcludeUrls() {
		return excludeUrls;
	}

	public void setExcludeUrls(List<String> excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	public List<String> getExcludeContainUrls() {
		return excludeContainUrls;
	}

	public void setExcludeContainUrls(List<String> excludeContainUrls) {
		this.excludeContainUrls = excludeContainUrls;
	}
	
	/**
	 * 模糊匹配字符串
	 * @param list
	 * @param key
	 * @return
	 */
	private boolean moHuContain(List<String> list,String key){
		for(String str : list){
			if(key.contains(str)){
				return true;
			}
		}
		return false;
	}

}
