package com.jingyunbank.etrade.order.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.jingyunbank.core.Result;
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
	public Result list(HttpServletRequest request, HttpSession session){
		return Result.ok(orderService.list((String)session.getAttribute("login-uid"))
				.stream().map(bo-> {
					OrderVO vo = new OrderVO();
					BeanUtils.copyProperties(bo, vo);
					return vo;
				}).collect(Collectors.toList()));
	}
	
	@RequireLogin
	@RequestMapping(value="/orders/submit", method=RequestMethod.PUT)
	public Result submit(@Valid OrderVO order, BindingResult valid) throws Exception{
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
						.map(oe -> Arrays.asList(oe.getCodes()).toString())
						.collect(Collectors.joining(" ; ")));
		}
		
		
		
		return Result.ok();
	}
}
