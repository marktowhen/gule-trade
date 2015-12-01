package com.jingyunbank.etrade.base.util;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jingyunbank.etrade.user.controller.UserController;

public class EtradeUtil {

	/**
	 * 根据请求获取客户端ip
	 * @param request
	 * @return
	 * 2015年11月12日 qxs
	 */
	public static  String getIpAddr(HttpServletRequest request)  {  
	      String ip  =  request.getHeader( " x-forwarded-for " );  
	       if (ip  ==   null   ||  ip.length()  ==   0   ||   " unknown " .equalsIgnoreCase(ip))  {  
	          ip  =  request.getHeader( " Proxy-Client-IP " );  
	      }   
	       if (ip  ==   null   ||  ip.length()  ==   0   ||   " unknown " .equalsIgnoreCase(ip))  {  
	          ip  =  request.getHeader( " WL-Proxy-Client-IP " );  
	      }   
	       if (ip  ==   null   ||  ip.length()  ==   0   ||   " unknown " .equalsIgnoreCase(ip))  {  
	         ip  =  request.getRemoteAddr();  
	     }   
	      return  ip;  
	 }
	
	/**
	 * 获取当前时间(暂时)
	 * 应从数据库取
	 * @return
	 * 2015年11月20日 qxs
	 */
	public static Date getNowDate(){
		return new Date();
	}
	
	/**
	 * 获取4位随机数
	 * @return
	 * 2015年11月26日 qxs
	 */
	public static String getRandomCode(){
		return String.valueOf(Math.abs(Math.round( Math.random()*9000+1000)));
	}
	
	/**
	 * 获取项目根目录 带 / 
	 * @param request
	 * @return
	 * 2015年11月10日 qxs
	 */
	public static  String getBasePath(HttpServletRequest request){
		String basePath;
		if (request.getServerPort() == 80) {
			basePath = request.getScheme() + "://" + request.getServerName()
					+ "/";
		} else {
			basePath = request.getScheme() + "://" + request.getServerName()
					+ ":" + request.getServerPort() + "/";
		}
		
		return basePath;
	}
	
	/**
	 * 验证认证身份是否超时
	 * @param session
	 * @return
	 * 2015年12月1日 gyx
	 */
	public static boolean effectiveTime(HttpSession session){
		Calendar now=Calendar.getInstance();
		now.setTime(new Date());
		Object sessionDate=session.getAttribute(UserController.CHECK_CODE_PASS_DATE);
		if(sessionDate!=null && sessionDate instanceof Date ){
			Calendar checkDate  = Calendar.getInstance();
			checkDate.setTime((Date)sessionDate);
			//+2
			checkDate.add(Calendar.MINUTE, SystemConfigProperties.getInt(SystemConfigProperties.EFFECTIVE_TIME) );
			if(checkDate.after(now)){
				return true;
			}
		}
		return false;
		
	}
	
}
