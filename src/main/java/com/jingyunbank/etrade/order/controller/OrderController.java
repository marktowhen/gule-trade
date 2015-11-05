package com.jingyunbank.etrade.order.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.util.RndBuilder;
import com.jingyunbank.etrade.api.order.bo.Orders;
import com.jingyunbank.etrade.api.order.service.IOrderService;
import com.jingyunbank.etrade.api.order.service.context.IOrderContextService;
import com.jingyunbank.etrade.base.intercepter.RequireLogin;
import com.jingyunbank.etrade.order.bean.OrderVO;

@RestController
public class OrderController {

	@Autowired
	private IOrderContextService orderContextService;
	@Autowired
	private IOrderService orderService;
	
	@RequestMapping(value="/orders", method=RequestMethod.GET)
	public Result listAll(HttpServletRequest request, HttpSession session){
		return Result.ok(orderService.list()
				.stream().map(bo-> {
					OrderVO vo = new OrderVO();
					BeanUtils.copyProperties(bo, vo);
					return vo;
				}).collect(Collectors.toList()));
	}
	
	@RequestMapping(value="/orders/{uid}", method=RequestMethod.GET)
	@RequireLogin
	public Result listUID(@PathVariable String uid, HttpServletRequest request, HttpSession session){
		return Result.ok(orderService.list((String)session.getAttribute("LOGIN_ID"))
				.stream().map(bo-> {
					OrderVO vo = new OrderVO();
					BeanUtils.copyProperties(bo, vo);
					return vo;
				}).collect(Collectors.toList()));
	}
	
	
	
	@RequireLogin
	@RequestMapping(value="/orders/submit", method=RequestMethod.PUT)
	public Result submit(@Valid OrderVO order, BindingResult valid, HttpSession session) throws Exception{
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
						.map(oe -> Arrays.asList(oe.getCodes()).toString())
						.collect(Collectors.joining(" ; ")));
		}
		order.setID(KeyGen.uuid());
		order.setOrderno(new String(new RndBuilder().hasletter(false).length(6).next()));
		order.setUID(session.getAttribute("LOGIN_ID").toString());
		order.setAddtime(new Date());
		Orders orderbo = new Orders();
		BeanUtils.copyProperties(order, orderbo);
		orderContextService.generate(orderbo);
		return Result.ok(order);
	}
}
