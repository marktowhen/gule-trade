package com.jingyunbank.etrade.weixin.controller;

import java.io.UnsupportedEncodingException;

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
		    	 WeixinOauth2Token weixinOauth2Token = GetAccessToken.getOauth2AccessToken("wx104070783e1981df", "8010ab6f0896f4a5f8ee39f289d52609", code);
		    	 //网页授权接口访问凭证
		    	 String accessToken=weixinOauth2Token.getAccessToken();
		    	 //用户标识
		    	 String openId = weixinOauth2Token.getOpenId();
		    	 //获取用户信息
		    	 SNSUserInfoVo snsUserInfoVo=GetAccessToken.getUserInfo(accessToken, openId);
		    	 SNSUserInfoBo snsUserInfoBo = new SNSUserInfoBo();
		    	 snsUserInfoVo.setId(KeyGen.uuid());
		    	 BeanUtils.copyProperties(snsUserInfoVo, snsUserInfoBo);
		    	 weixinUserService.addUser(snsUserInfoBo);
		    	 
		    	 System.out.println("得到用户的信息:"+snsUserInfoVo);
		    	 //设置传递参数
		    	 request.setAttribute("snsUserInfo", snsUserInfoVo);
		     }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
