package com.jingyunbank.etrade.order.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.bo.OrderLogistic;
import com.jingyunbank.etrade.api.order.service.IOrderLogisticService;
import com.jingyunbank.etrade.order.dao.OrderLogisticDao;
import com.jingyunbank.etrade.order.entity.OrderLogisticEntity;

@Service("orderLogisticeService")
public class OrderLogisticService implements IOrderLogisticService {

	@Autowired
	private OrderLogisticDao orderLogisticDao;
	
	@Override
	public void save(OrderLogistic bo) throws DataSavingException {
		OrderLogisticEntity entity = new OrderLogisticEntity();
		BeanUtils.copyProperties(bo, entity);
		try {
			orderLogisticDao.insertOne(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

}
