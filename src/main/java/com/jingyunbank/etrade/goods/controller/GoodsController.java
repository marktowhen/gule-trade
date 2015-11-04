package com.jingyunbank.etrade.goods.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.test.web.servlet.result.RequestResultMatchers;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;

import jdk.nashorn.internal.ir.RuntimeNode.Request;
/**
* Title:     商品controller
* @author    duanxf
* @date      2015年11月4日
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@RequestMapping(value="/query/{name}",method=RequestMethod.POST)
	public Result  queryGoodsByName(HttpServletRequest request, @PathVariable String name){
		System.out.println("-0------------------"+name);
		return Result.ok("OK");
	}
}
