package com.jingyunbank.etrade.base.util;

import javax.servlet.http.HttpServletRequest;

import com.jingyunbank.etrade.base.constant.Constant;
import com.jingyunbank.etrade.user.bean.UserVO;
/**
 * 测试用 等待正式工具类
 * qxs
 */
public class RequestUtil {

	/**
	 * 获取已登录的userid
	 * @param request
	 * @return
	 * 2015年11月5日 qxs
	 */
	public static String getLoginUserid(HttpServletRequest request){
		UserVO loginUser = getLoginUser(request);
		if(loginUser!=null){
			return loginUser.getID();
		}
		return null;
	}
	
	/**
	 * 获取已登录的userVO
	 * @param request
	 * @return
	 * 2015年11月5日 qxs
	 */
	public static UserVO getLoginUser(HttpServletRequest request){
		return (UserVO) request.getAttribute(Constant.SESSION_USER);
	}
	
	
	public static void main(String[] args) {
		
	}
}
