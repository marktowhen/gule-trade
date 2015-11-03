package com.jingyunbank.etrade.order.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.exception.DataUpdatingException;
import com.jingyunbank.etrade.api.order.bo.Orders;
import com.jingyunbank.etrade.api.order.service.IOrderService;

@Service("orderService")
public class OrderService implements IOrderService{

	@Override
	public boolean save(Orders order) throws DataSavingException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Orders order) throws DataUpdatingException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Optional<Orders> getByOrderNo(String orderno) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Orders> list(String uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Orders> list(Date start, Date end) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
