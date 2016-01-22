package com.jingyunbank.etrade.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.etrade.api.user.bo.QQLogin;
import com.jingyunbank.etrade.api.user.service.IQQLoginService;

@RestController
@RequestMapping("/api/login/qq")
public class QQLoginController {
	
	@Autowired
	private IQQLoginService qqloginService;

	@RequestMapping(value = "/jump", method = RequestMethod.GET)
	public void changeState(HttpServletRequest request, HttpServletResponse response,@RequestParam("access_token") String accessToken) throws Exception{
		QQLogin qqLogin = qqloginService.single(accessToken);
		if(qqLogin!=null){
			//跳到首页吧
			response.sendRedirect("http://other-login.ngrok.cc/#/login");
		}else{
			//跳到绑定页
			response.sendRedirect("http://other-login.ngrok.cc/#/login");
		}
	}
}
