package com.jingyunbank.etrade.postage.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.postage.service.IPostageService;

@RestController
public class PostageController {

	@Autowired
	private IPostageService postageService;
	
	@RequestMapping(value="/api/postage/calculation", method=RequestMethod.GET)
	public Result<BigDecimal> calculate(@RequestParam("price") BigDecimal price, @RequestParam("default") BigDecimal defaultp) throws Exception{
		return Result.ok(postageService.calculate("", price, BigDecimal.ZERO, defaultp));
	}
}
