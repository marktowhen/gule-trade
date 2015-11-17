package com.jingyunbank.etrade.pay.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.bo.Orders;
import com.jingyunbank.etrade.api.pay.bo.OrderPayment;
import com.jingyunbank.etrade.api.pay.service.IPayService;
import com.jingyunbank.etrade.pay.dao.PayDao;
import com.jingyunbank.etrade.pay.entity.OrderPaymentEntity;

@Service("payService")
public class PayService implements IPayService{

	@Autowired
	private PayDao payDao;
	
	@Override
	@Transactional
	public void save(OrderPayment payment) throws DataSavingException {
		OrderPaymentEntity entity = new OrderPaymentEntity();
		BeanUtils.copyProperties(payment, entity);
		try {
			payDao.insertOne(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	@Transactional
	public void save(List<OrderPayment> payments) throws DataSavingException {
		List<OrderPaymentEntity> entities = new ArrayList<OrderPaymentEntity>();
		payments.forEach(order->{
			OrderPaymentEntity entity = new OrderPaymentEntity();
			BeanUtils.copyProperties(order, entity);
			entities.add(entity);
		});
		try {
			payDao.insertMany(entities);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public OrderPayment singlePayment(Orders order) {
		return null;
	}

	@Override
	public OrderPayment singlePayment(String oid) {
		return null;
	}

	@Override
	public OrderPayment singlePayment(long orderno) {
		return null;
	}

	@Override
	public List<OrderPayment> listPayments(List<String> oids) {
		
		List<OrderPaymentEntity> entities = payDao.selectPayments(oids);
		List<OrderPayment> bos = new ArrayList<OrderPayment>();
		entities.forEach(e -> {
			OrderPayment op = new OrderPayment();
			BeanUtils.copyProperties(e, op);
			bos.add(op);
		});
		return bos;
	}

	@Override
	public void refreshExtransno(List<OrderPayment> payments)
			throws DataSavingException {
		try {
			payDao.updateMany(payments.stream().map(bo->{
				OrderPaymentEntity entity = new OrderPaymentEntity();
				BeanUtils.copyProperties(bo, entity);
				return entity;
			}).collect(Collectors.toList()));
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

}
