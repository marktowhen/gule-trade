package com.jingyunbank.etrade.weixin.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.etrade.weixin.bean.SNSUserInfo;
import com.jingyunbank.etrade.weixin.bean.WeixinOauth2Token;
import com.jingyunbank.etrade.weixin.util.GetAccessToken;

@RestController
public class OAuthController {
	@RequestMapping("/api/get/user")
	public void getUserInfo(HttpServletRequest request, HttpServletResponse response){
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			//用户同意授权后，能获取到code
			 String code = request.getParameter("code");
		     String state = request.getParameter("state");
		     //用户同意授权
		     if(!"authdeny".equals(code)){
		    	 //获取授权的access_token
		    	 WeixinOauth2Token weixinOauth2Token = GetAccessToken.getOauth2AccessToken("123456", "123456", code);
		    	 //网页授权接口访问凭证
		    	 String accessToken=weixinOauth2Token.getAccessToken();
		    	 //用户标识
		    	 String openId = weixinOauth2Token.getOpenId();
		    	 //获取用户信息
		    	 SNSUserInfo snsUserInfo=GetAccessToken.getUserInfo(accessToken, openId);
		    	 System.out.println("得到用户的信息:"+snsUserInfo);
		    	 //设置传递参数
		    	 request.setAttribute("snsUserInfo", snsUserInfo);
		     }
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	     
	}

	

}
