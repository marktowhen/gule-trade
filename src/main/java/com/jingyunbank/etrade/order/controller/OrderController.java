package com.jingyunbank.etrade.order.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
