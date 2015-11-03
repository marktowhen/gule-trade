package com.jingyunbank.etrade.base.intercepter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jingyunbank.core.Result;

public class AuthIntercepter implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		HandlerMethod method = null;
		if(handler instanceof HandlerMethod){
			method = (HandlerMethod) handler;
		}
		
		if(method == null){
			try {
	            response.setContentType("application/json");
	            response.setHeader("Cache-Control", "nocache");
	            response.setCharacterEncoding("utf-8");
	            PrintWriter writer = response.getWriter();
	            writer.write(Result.fail("cast exception when casting object " + handler + "to HandlerMethod").toString());
	            writer.flush();
	            writer.close();
	        } catch (IOException e) {}
			return false;
		}
		
		RequireLogin requiredLogin = method.getMethodAnnotation(RequireLogin.class);
		if(requiredLogin != null){
			Object obj = request.getSession().getAttribute("LOGIN_ID");
			if(obj == null){
				try {
		            response.setContentType("application/json;charset=UTF-8");
		            response.setHeader("Cache-Control", "nocache");
		            response.setCharacterEncoding("utf-8");
		            PrintWriter writer = response.getWriter();
		            writer.write(Result.fail("please login first.").toString());
		            writer.flush();
		            writer.close();
		        } catch (IOException e) {}
				return false;
			}
		}
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}


}
