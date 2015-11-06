package com.jingyunbank.etrade.order.controller;

import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.order.service.ICartService;
import com.jingyunbank.etrade.base.intercepter.RequireLogin;
import com.jingyunbank.etrade.order.bean.CartVO;


@RestController
public class CartController {

	@Autowired
	private ICartService cartService;
	
	@RequireLogin
	@RequestMapping(value="/carts/{uid}", method=RequestMethod.GET)
	public Result list(@PathVariable String uid, 
			@RequestParam(required=false, defaultValue="0") long offset,
			@RequestParam(required=false, defaultValue="10") long size) throws Exception{
		
		final Range range = new Range();
		range.setFrom(offset);
		range.setTo(offset+size);
		
		return Result.ok(cartService.list(uid, range)
				.stream().map(bo->{
					CartVO vo = new CartVO();
					BeanUtils.copyProperties(bo, vo);
					return vo;
				}).collect(Collectors.toList()));
		
	}
	
	
}
