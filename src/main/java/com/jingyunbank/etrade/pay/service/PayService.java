package com.jingyunbank.etrade.pay.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
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

//	@Override
//	public OrderPayment singlePayment(Orders order) {
//		return null;
//	}
//
//	@Override
//	public OrderPayment singlePayment(String oid) {
//		return null;
//	}
//
//	@Override
//	public OrderPayment singlePayment(long orderno) {
//		return null;
//	}

	@Override
	public List<OrderPayment> listPayable(List<String> oids) {
		
		List<OrderPaymentEntity> entities = payDao.selectPayable(oids);
		List<OrderPayment> bos = new ArrayList<OrderPayment>();
		entities.forEach(e -> {
			OrderPayment op = new OrderPayment();
			BeanUtils.copyProperties(e, op);
			bos.add(op);
		});
		return bos;
	}

	@Override
	public void refreshNOAndPipeline(List<OrderPayment> payments)
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

	@Override
	public boolean anyDone(List<String> orderpaymentids) {
		int count = payDao.countDone(orderpaymentids);
		return count > 0;
	}

	@Override
	public boolean allDone(List<String> orderpaymentids) {
		int count = payDao.countDone(orderpaymentids);
		return count == orderpaymentids.size();
	}

	@Override
	public void refreshStatus(String extransno, boolean done) throws DataRefreshingException {
		try {
			payDao.updateStatus(extransno, done);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	public List<OrderPayment> listPaid(List<String> oids) {
		List<OrderPaymentEntity> entities = payDao.selectPaid(oids);
		List<OrderPayment> bos = new ArrayList<OrderPayment>();
		entities.forEach(e -> {
			OrderPayment op = new OrderPayment();
			BeanUtils.copyProperties(e, op);
			bos.add(op);
		});
		return bos;
	}
}
