package com.jingyunbank.etrade.order.postsale.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.postsale.bo.RefundTrace;
import com.jingyunbank.etrade.api.order.postsale.service.IRefundTraceService;
import com.jingyunbank.etrade.order.postsale.dao.RefundTraceDao;
import com.jingyunbank.etrade.order.postsale.entity.RefundTraceEntity;

@Service("refundTraceService")
public class RefundTraceService implements IRefundTraceService {

	@Autowired
	private RefundTraceDao refundTraceDao;
	
	@Override
	public void save(RefundTrace trace) throws DataSavingException {
		try {
			List<RefundTraceEntity> otes = new ArrayList<RefundTraceEntity>();
			RefundTraceEntity oge = new RefundTraceEntity();
			BeanUtils.copyProperties(trace, oge);
			otes.add(oge);
			refundTraceDao.insertMany(otes);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public void save(List<RefundTrace> traces) throws DataSavingException {
		try {
			List<RefundTraceEntity> otes = new ArrayList<RefundTraceEntity>();
			traces.forEach(t -> {
				RefundTraceEntity oge = new RefundTraceEntity();
				BeanUtils.copyProperties(t, oge);
				otes.add(oge);
			});
			refundTraceDao.insertMany(otes);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public List<RefundTrace> list(String rid) {
		return refundTraceDao.selectMany(rid)
				.stream().map(entity -> {
					RefundTrace bo = new RefundTrace();
					BeanUtils.copyProperties(entity, bo);
					return bo;
				}).collect(Collectors.toList());
	}

}
