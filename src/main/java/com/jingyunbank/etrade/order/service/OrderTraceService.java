package com.jingyunbank.etrade.order.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.bo.OrderTrace;
import com.jingyunbank.etrade.api.order.service.IOrderTraceService;
import com.jingyunbank.etrade.order.dao.OrderTraceDao;
import com.jingyunbank.etrade.order.entity.OrderTraceEntity;

@Service("orderTraceService")
public class OrderTraceService implements IOrderTraceService {

	@Autowired
	private OrderTraceDao orderTraceDao;
	
	@Override
	public void save(OrderTrace trace) throws DataSavingException {
		try {
			List<OrderTraceEntity> otes = new ArrayList<OrderTraceEntity>();
			OrderTraceEntity oge = new OrderTraceEntity();
			BeanUtils.copyProperties(trace, oge);
			otes.add(oge);
			orderTraceDao.insertMany(otes);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public void save(List<OrderTrace> traces) throws DataSavingException {
		try {
			List<OrderTraceEntity> otes = new ArrayList<OrderTraceEntity>();
			traces.forEach(t -> {
				OrderTraceEntity oge = new OrderTraceEntity();
				BeanUtils.copyProperties(t, oge);
				otes.add(oge);
			});
			orderTraceDao.insertMany(otes);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

}
