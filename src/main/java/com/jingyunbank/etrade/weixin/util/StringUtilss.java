package com.jingyunbank.etrade.weixin.util;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.jingyunbank.etrade.api.weixin.bo.SNSUserInfoBo;
import com.jingyunbank.etrade.api.weixin.service.IWeiXinUserService;
import com.jingyunbank.etrade.weixin.entity.Constants;
import com.jingyunbank.etrade.weixin.entity.TEA;


public class StringUtilss extends org.apache.commons.lang3.StringUtils{
	
	@Autowired
	private IWeiXinUserService weixinUserService;
	
	public Optional<SNSUserInfoBo> getUser(String uid){
		return weixinUserService.singles(uid);
	}
	/**
	 * 获得session中的userid
	 * @param request
	 * @return
	 */
	public static String getSessionId(HttpServletRequest request,HttpServletResponse resp){
		String userid="";
		try{
		    userid  = String.valueOf(request.getSession().getAttribute(Constants.IDBYSESSION));
		    
		    if(isNotBlank(userid) ){
		    	userid = TEA.Decrypt(userid);
			    // 查询一下userid是否为锁住的
			    /*DwUserService userService = (DwUserService) SpringUtil.getBean("dwUserService");*/
		    /*	=StringUtils.getUser(userid);
			    boolean islock = userService.checkIsLock(Integer.valueOf(userid));
			    // 被锁定的状态
			    if(islock == true){
			    	userid = "";
			    	// 跳转至首页return "redirect:/account/login";
			    	resp.sendRedirect(request.getContextPath() + "/account/login");
			    	return "";
			    }*/
		    }
		}catch(Exception e){
			/*SystemLogger.log("获得session中的用户id", "id为空");*/
			e.printStackTrace();
		}
		return userid;
	} 
	public static String getSessionId(HttpServletRequest request){
		String userid="";
		try{
			System.out.println(request.getSession().getAttribute(Constants.IDBYSESSION));
		    userid = String.valueOf(request.getSession().getAttribute(Constants.IDBYSESSION));
		    // 查询一下userid是否为锁住的
		   if(isNotBlank(userid) ){
		    	userid = TEA.Decrypt(userid);
		    	 /* DwUserService userService = (DwUserService) SpringUtil.getBean("dwUserService");
			    boolean islock = userService.checkIsLock(Integer.valueOf(userid));
			    // 被锁定的状态
			    if(islock == true){
			    	userid = "";
			    }*/
		    }

		}catch(Exception e){
			/*SystemLogger.log("获得session中的用户id", "id为空");*/
			e.printStackTrace();
		}
		return userid;
	} 
	public static boolean isNotBlank(CharSequence charSequence){
		return !isBlank(charSequence);
	}
	public static boolean isBlank(CharSequence charSequence){
		return (!org.springframework.util.StringUtils.hasText(charSequence)) || "null".equalsIgnoreCase(charSequence.toString());
	}

}
