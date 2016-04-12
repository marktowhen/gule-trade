package com.jingyunbank.etrade.asyn.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.asyn.bo.AsynDefined;
import com.jingyunbank.etrade.api.asyn.service.IAsynDefinedService;
import com.jingyunbank.etrade.asyn.dao.AsynDefinedDao;
import com.jingyunbank.etrade.asyn.entity.AsynDefinedEntity;

@Service("asynDefinedService")
public class AsynDefinedService implements IAsynDefinedService {
	
	@Autowired
	private AsynDefinedDao asynDefinedDao;

	@Override
	public AsynDefined single(String id) {
		AsynDefinedEntity entity = asynDefinedDao.selectOne(id);
		if(entity!=null){
			AsynDefined bo = new AsynDefined();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}
		return null;
	}

}
