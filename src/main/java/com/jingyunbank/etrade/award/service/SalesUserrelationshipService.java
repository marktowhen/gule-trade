package com.jingyunbank.etrade.award.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.award.bo.SalesUserrelationship;
import com.jingyunbank.etrade.api.award.service.ISalesUserrelationshipService;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.award.dao.SalesUserrelationshipDao;
import com.jingyunbank.etrade.award.entity.SalesUserrelationshipEntity;

@Service("salesUserrelationshipService")
public class SalesUserrelationshipService implements
		ISalesUserrelationshipService {
	
	@Autowired
	private SalesUserrelationshipDao salesUserrelationshipDao;

	@Override
	public boolean save(SalesUserrelationship salesUserrelationship)
			throws DataSavingException {
		SalesUserrelationshipEntity entity = new SalesUserrelationshipEntity();
		BeanUtils.copyProperties(salesUserrelationship, entity);
		return salesUserrelationshipDao.insert(entity);
	}

	@Override
	public SalesUserrelationship singleBySID(String SID) {
		SalesUserrelationshipEntity entity = salesUserrelationshipDao.selectBySID(SID);
		if(entity!=null){
			SalesUserrelationship bo = new SalesUserrelationship();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}
		return null;
	}

	@Override
	public SalesUserrelationship singleByUID(String UID) {
		SalesUserrelationshipEntity entity = salesUserrelationshipDao.singleByUID(UID);
		if(entity!=null){
			SalesUserrelationship bo = new SalesUserrelationship();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}
		return null;
	}

}
