package com.jingyunbank.etrade.order.presale.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.order.presale.service.IOrderTraceService;
import com.jingyunbank.etrade.order.presale.bean.OrderTraceVO;

@RestController
public class OrderTraceController {

	@Autowired
	private IOrderTraceService orderTraceService;
	
	@RequestMapping(value="/api/orders/{oid}/traces", method=RequestMethod.GET)
	//@AuthBeforeOperation
	public Result<List<OrderTraceVO>> listTraces(@PathVariable("oid") String oid, HttpSession session){
		
		return Result.ok(orderTraceService.list(oid)
				.stream().map(bo->{
					OrderTraceVO vo = new OrderTraceVO();
					BeanUtils.copyProperties(bo, vo);
					vo.setOrderno(String.valueOf(bo.getOrderno()));
					return vo;
				}).collect(Collectors.toList()));
	}
}
