package com.jingyunbank.etrade.order.postsale.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.postsale.bo.Refund;
import com.jingyunbank.etrade.api.order.postsale.bo.RefundStatusDesc;
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

	@Override
	public void refreshStatus(String RID, RefundStatusDesc status)
			throws DataRefreshingException {
		try {
			refundDao.updateStatus(RID, status);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	public Optional<Refund> single(String rid) {
		RefundEntity entity = refundDao.selectOne(rid);
		if(Objects.isNull(entity)){
			return Optional.ofNullable(null);
		}
		Refund bo = new Refund();
		BeanUtils.copyProperties(entity, bo, "certificates");
		return Optional.of(bo);
	}

}
