package com.jingyunbank.etrade.order.postsale.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.postsale.bo.RefundLogistic;
import com.jingyunbank.etrade.api.order.postsale.service.IRefundLogisticService;
import com.jingyunbank.etrade.order.postsale.dao.RefundLogisticDao;
import com.jingyunbank.etrade.order.postsale.entity.RefundLogisticEntity;

@Service("refundLogisticService")
public class RefundLogisticService implements IRefundLogisticService {

	@Autowired
	private RefundLogisticDao refundLogisticDao;
	
	@Override
	public void save(RefundLogistic bo) throws DataSavingException {
		RefundLogisticEntity entity = new RefundLogisticEntity();
		BeanUtils.copyProperties(bo, entity);
		try {
			refundLogisticDao.insertOne(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public Optional<RefundLogistic> single(String rid) {
		RefundLogisticEntity entity = refundLogisticDao.selectRID(rid);
		if(Objects.isNull(entity)){
			return Optional.ofNullable(null);
		}
		RefundLogistic bo = new RefundLogistic();
		BeanUtils.copyProperties(entity, bo);
		return Optional.of(bo);
	}
}
