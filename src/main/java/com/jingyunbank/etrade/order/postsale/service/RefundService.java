package com.jingyunbank.etrade.order.postsale.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.Range;
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
	public void refresh(Refund refund) throws DataRefreshingException {
		RefundEntity entity = new RefundEntity();
		BeanUtils.copyProperties(refund, entity);
		try {
			refundDao.update(entity);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}
	
	@Override
	public void refreshStatus(List<String> RIDs, RefundStatusDesc status)
			throws DataRefreshingException {
		try {
			refundDao.updateStatus(RIDs, status);
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

	@Override
	public List<Refund> list(String uid, String mid, String statuscode, String keywords, 
			String fromdate, String enddate, Range range) {
		return refundDao.selectKeywords(uid, mid, statuscode, keywords, fromdate, enddate, range.getFrom(), (int)(range.getTo()-range.getFrom()))
				.stream().map(entity -> {
					Refund bo = new Refund();
					BeanUtils.copyProperties(entity, bo, "certificates");
					return bo;
				}).collect(Collectors.toList());
	}

	@Override
	public Optional<Refund> singleByOGID(String ogid) {
		RefundEntity entity = refundDao.selectOneByOGID(ogid);
		if(Objects.isNull(entity)){
			return Optional.ofNullable(null);
		}
		Refund bo = new Refund();
		BeanUtils.copyProperties(entity, bo, "certificates");
		return Optional.of(bo);
	}

	@Override
	public List<Refund> list(List<String> rids) {
		return refundDao.selectByRIDs(rids)
				.stream().map(entity -> {
					Refund bo = new Refund();
					BeanUtils.copyProperties(entity, bo, "certificates");
					return bo;
				}).collect(Collectors.toList());
	}

	@Override
	public List<Refund> listBefore(Date deadline, RefundStatusDesc status) {
		return refundDao.selectBefore(deadline, status.getCode())
				.stream().map(entity -> {
					Refund bo = new Refund();
					BeanUtils.copyProperties(entity, bo, "certificates");
					return bo;
				}).collect(Collectors.toList());
	}
}
