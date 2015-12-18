package com.jingyunbank.etrade.order.postsale.service;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.postsale.bo.RefundCertificate;
import com.jingyunbank.etrade.api.order.postsale.service.IRefundCertificateService;
import com.jingyunbank.etrade.order.postsale.dao.RefundCertificateDao;
import com.jingyunbank.etrade.order.postsale.entity.RefundCertificateEntity;

@Service("refundCertificateService")
public class RefundCertificateService implements IRefundCertificateService {

	@Autowired
	private RefundCertificateDao refundCertificateDao;
	
	@Override
	public void save(List<RefundCertificate> bos) throws DataSavingException {
		List<RefundCertificateEntity> entities = new ArrayList<RefundCertificateEntity>();
		bos.forEach(bo->{
			RefundCertificateEntity entity = new RefundCertificateEntity();
			BeanUtils.copyProperties(bo, entity);
			entities.add(entity);
		});
		try {
			refundCertificateDao.insertMany(entities);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

}