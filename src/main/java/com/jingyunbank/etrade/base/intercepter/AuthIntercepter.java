package com.jingyunbank.etrade.base.intercepter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.JsonResponse;
import com.jingyunbank.core.web.ServletBox;

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
				JsonResponse.write(response, 
						Result.fail("cast exception when casting object " + handler + "to HandlerMethod").toString());
	        } catch (IOException e) {}
			return false;
		}
		
		AuthBeforeOperation requiredLogin = method.getMethodAnnotation(AuthBeforeOperation.class);
		if(requiredLogin != null){
			Object obj = ServletBox.getLoginUID(request);
			if(obj == null){
				try {
		           JsonResponse.write(response, 
		        		   Result.fail("please login first").toString());
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
