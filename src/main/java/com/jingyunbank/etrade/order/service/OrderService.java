package com.jingyunbank.etrade.order.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.exception.DataUpdatingException;
import com.jingyunbank.etrade.api.order.bo.Orders;
import com.jingyunbank.etrade.api.order.service.IOrderService;
import com.jingyunbank.etrade.order.dao.OrderDao;

@Service("orderService")
public class OrderService implements IOrderService{

	@Autowired
	private OrderDao orderDao;
	
	@Override
	public boolean save(Orders order) throws DataSavingException {
		return false;
	}

	@Override
	public boolean refresh(Orders order) throws DataUpdatingException {
		return false;
	}

	@Override
	public Optional<Orders> singleByOrderNo(String orderno) {
		return null;
	}

	@Override
	public List<Orders> list(String uid) {
		return orderDao.select(uid)
			.stream().map(entity -> {
				Orders bo = new Orders();
				BeanUtils.copyProperties(entity, bo);
				return bo;
			}).collect(Collectors.toList());
	}

	@Override
	public List<Orders> list(Date start, Date end) {
		return null;
	}

	

}
