package com.jingyunbank.etrade.goods.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 商品管理控制器
 * @author liug
 *
 */
@RestController
public class GoodsController {

	@RequestMapping("/goods")
	public String invest(HttpServletRequest request, HttpSession session){
		HttpSession session0 = request.getSession();
		session0.setAttribute("abcdef", "abcdef");
		session.setAttribute("ghijk", "ghijk");
		System.out.println(session0.getAttribute("ghijk"));
		return "users";
	}
}
