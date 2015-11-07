package com.jingyunbank.etrade.order.controller;

import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.order.bo.Cart;
import com.jingyunbank.etrade.api.order.service.ICartService;
import com.jingyunbank.etrade.base.intercepter.RequireLogin;
import com.jingyunbank.etrade.order.bean.CartVO;


@RestController
public class CartController {

	@Autowired
	private ICartService cartService;
	
	@RequireLogin
	@RequestMapping(value="/api/carts/{uid}", method=RequestMethod.GET)
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
	
	@RequireLogin
	@RequestMapping(value="/api/carts", method=RequestMethod.PUT)
	public Result put(@Valid CartVO cartVo, HttpSession session) throws Exception{
		
		cartVo.setID(KeyGen.uuid());
		cartVo.setAddtime(new Date());
		cartVo.setUID((String)session.getAttribute("LOGIN_ID"));
		Cart cart = new Cart();
		BeanUtils.copyProperties(cartVo, cart);
		cartService.save(cart);
		return Result.ok(cartVo);
	}
	
	@RequireLogin
	@RequestMapping(value="/api/carts/{id}", method=RequestMethod.DELETE)
	public Result delete(@PathVariable String id) throws Exception{
		
		cartService.remove(id);
		
		return Result.ok(id);
	}
	
	@RequireLogin
	@RequestMapping(value="/api/carts", method=RequestMethod.POST)
	public Result update(@Valid CartVO cartVo, HttpSession session) throws Exception{
		
		Cart cart = new Cart();
		BeanUtils.copyProperties(cartVo, cart);
		cartService.refresh(cart);
		return Result.ok(cartVo);
	}
}
