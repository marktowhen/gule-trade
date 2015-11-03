package com.jingyunbank.etrade.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.order.bean.OrderVO;

@RestController
public class OrderController {

	@RequestMapping("/orders")
	public String invest(HttpServletRequest request, HttpSession session){
		HttpSession session0 = request.getSession();
		session0.setAttribute("abcdef", "abcdef");
		session.setAttribute("ghijk", "ghijk");
		System.out.println(session0.getAttribute("ghijk"));
		return "users";
	}
	
	@RequestMapping(value="/orders/new", method=RequestMethod.PUT)
	public Result submit(@Valid OrderVO order, BindingResult valid) throws Exception{
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			errors.forEach(error->System.out.println(error.getDefaultMessage()));
			return Result.fail("");
		}
		
		return Result.ok();
	}
}
