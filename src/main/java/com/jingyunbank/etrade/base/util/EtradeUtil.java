package com.jingyunbank.etrade.base.util;

import javax.servlet.http.HttpServletRequest;

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
}
