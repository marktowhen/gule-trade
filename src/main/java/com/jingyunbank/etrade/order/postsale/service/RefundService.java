package com.jingyunbank.etrade.order.postsale.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.postsale.bo.Refund;
import com.jingyunbank.etrade.api.order.postsale.service.IRefundService;
import com.jingyunbank.etrade.order.postsale.dao.RefundDao;
import com.jingyunbank.etrade.order.postsale.entity.RefundEntity;

@Service("refundService")
public class RefundService implements IRefundService {

	@Autowired
	private RefundDao refundDao;
	
	@Override
	public void save(Refund refund) throws DataSavingException {
		RefundEntity entity = new RefundEntity();
		BeanUtils.copyProperties(refund, entity);
		try {
			refundDao.insertOne(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

}
