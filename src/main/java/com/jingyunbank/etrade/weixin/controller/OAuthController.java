package com.jingyunbank.etrade.weixin.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.etrade.api.weixin.bo.SNSUserInfoBo;
import com.jingyunbank.etrade.api.weixin.service.IWeiXinUserService;
import com.jingyunbank.etrade.weixin.entity.WeixinOauth2Token;
import com.jingyunbank.etrade.weixin.util.GetAccessToken;
import com.jingyunbank.etrade.weixin.vo.SNSUserInfoVo;

@RestController
public class OAuthController {
	
	@Autowired
	private IWeiXinUserService weixinUserService;
	
	/**
	 * 保存信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
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
		    	 WeixinOauth2Token weixinOauth2Token = GetAccessToken.getOauth2AccessToken("wx7881dc8f8b9645e1", "d2458d5cbe19d23490db8f0b1b8ca26b", code);
		    	 //网页授权接口访问凭证
		    	 String accessToken=weixinOauth2Token.getAccessToken();
		    	 //用户标识
		    	 String openId = weixinOauth2Token.getOpenId();
		    	 //获取用户信息
		    	 SNSUserInfoVo snsUserInfoVo=GetAccessToken.getUserInfo(accessToken, openId);
		    	 SNSUserInfoBo snsUserInfoBo = new SNSUserInfoBo();
		    	 snsUserInfoVo.setId(KeyGen.uuid());
		    	 BeanUtils.copyProperties(snsUserInfoVo, snsUserInfoBo);
		    	 if(weixinUserService.getUsers(openId)==null){
		    		 weixinUserService.addUser(snsUserInfoBo);
		    		 request.setAttribute("snsUserInfo", snsUserInfoVo);
		    		 response.sendRedirect("http://aaaaa.tunnel.qydev.com/#/");
		    	 }else{
		    		 System.out.println("得到用户的信息:"+snsUserInfoVo.getNickname());
			    	 //设置传递参数
			    	 request.setAttribute("snsUserInfo", snsUserInfoVo);
			    	 response.sendRedirect("http://aaaaa.tunnel.qydev.com/#/");
		    	 }
		    	
		     }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
