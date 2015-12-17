package com.jingyunbank.etrade.order.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.presale.bo.OrderLogistic;
import com.jingyunbank.etrade.api.order.presale.service.IOrderLogisticService;
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

	@Override
	public Optional<OrderLogistic> single(String oid) {
		OrderLogisticEntity entity = orderLogisticDao.selectOID(oid);
		if(Objects.isNull(entity)){
			return Optional.ofNullable(null);
		}
		OrderLogistic bo = new OrderLogistic();
		BeanUtils.copyProperties(entity, bo);
		return Optional.of(bo);
	}

}
