package com.jingyunbank.etrade.base.util;

import javax.servlet.http.HttpServletRequest;

import com.jingyunbank.etrade.base.constant.Constant;
/**
 * 处理请求信息
 */
public class RequestUtil {

	/**
	 * 获取已登录的uid
	 * @param request
	 * @return
	 * 2015年11月5日 qxs
	 */
	public static String getLoginId(HttpServletRequest request){
		return (String) request.getSession().getAttribute(Constant.LOGIN_ID);
	}
	
	/**
	 * 获取已登录的username
	 * @param request
	 * @return
	 * 2015年11月5日 qxs
	 */
	public static String getLoginUsername(HttpServletRequest request){
		return (String) request.getSession().getAttribute(Constant.LOGIN_USERNAME);
	}
	
}
