package com.jingyunbank.etrade.order.presale.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.presale.bo.OrderTrace;
import com.jingyunbank.etrade.api.order.presale.service.IOrderTraceService;
import com.jingyunbank.etrade.order.presale.dao.OrderTraceDao;
import com.jingyunbank.etrade.order.presale.entity.OrderTraceEntity;

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

	@Override
	public List<OrderTrace> list(String oid) {
		return orderTraceDao.selectMany(oid)
				.stream().map(entity -> {
					OrderTrace bo = new OrderTrace();
					BeanUtils.copyProperties(entity, bo);
					return bo;
				}).collect(Collectors.toList());
	}

}
