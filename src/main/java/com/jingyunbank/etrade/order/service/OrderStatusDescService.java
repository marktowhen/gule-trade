package com.jingyunbank.etrade.order.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.order.presale.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.presale.service.IOrderStatusDescService;
import com.jingyunbank.etrade.order.dao.OrderStatusDescDao;

@Service("orderStatusDescService")
public class OrderStatusDescService implements IOrderStatusDescService {

	@Autowired
	private OrderStatusDescDao orderStatusDescDao;
	
	@Override
	public List<OrderStatusDesc> listVisible() {
		return orderStatusDescDao.selectVisible()
				.stream().map(entity -> {
					OrderStatusDesc bo = new OrderStatusDesc();
					BeanUtils.copyProperties(entity, bo);
					return bo;
				}).collect(Collectors.toList());
	}

}
