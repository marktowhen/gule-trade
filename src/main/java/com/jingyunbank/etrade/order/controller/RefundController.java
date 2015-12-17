package com.jingyunbank.etrade.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.etrade.api.order.service.context.IOrderContextService;
import com.jingyunbank.etrade.order.service.context.OrderContextService;

@RestController
public class RefundController {
	
	@Autowired
	private IOrderContextService orderContextService;
	
	@AuthBeforeOperation
	@RequestMapping(value="/api/orders/refund{id}", method=RequestMethod.DELETE)
	public Result<String> remove(@PathVariable String id) throws Exception{
		
		//orderContextService.refund(refund);
		
		return Result.ok(id);
	}
}
