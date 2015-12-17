package com.jingyunbank.etrade.order.controller;

import java.util.List;
import java.util.stream.Collectors;



import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.order.presale.service.IOrderStatusDescService;
import com.jingyunbank.etrade.order.bean.OrderStatusDescVO;

@RestController
public class OrderStatusDescController {

	@Autowired
	private IOrderStatusDescService orderStatusDescService;
	
	@RequestMapping(value="/api/order/status/visible", method=RequestMethod.GET)
	public Result<List<OrderStatusDescVO>> listVisible() throws Exception{
		
		return Result.ok(orderStatusDescService.listVisible().stream()
				.map(bo -> {
					OrderStatusDescVO vo = new OrderStatusDescVO();
					BeanUtils.copyProperties(bo, vo);
					return vo;
				}).collect(Collectors.toList()));
	}
}
