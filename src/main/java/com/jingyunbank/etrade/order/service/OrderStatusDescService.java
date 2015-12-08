package com.jingyunbank.etrade.order.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.order.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.service.IOrderStatusDescService;

@Service("orderStatusDescService")
public class OrderStatusDescService implements IOrderStatusDescService {

	@Override
	public List<OrderStatusDesc> listVisible() {
		return new ArrayList<OrderStatusDesc>();
	}

}
