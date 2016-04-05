package com.jingyunbank.etrade.asyn.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.asyn.bo.AsynLog;
import com.jingyunbank.etrade.api.asyn.service.IAsynLogService;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.asyn.dao.AsynLogDao;
import com.jingyunbank.etrade.asyn.entity.AsynLogEntity;

@Service("asynLogService")
public class AsynLogService implements IAsynLogService {

	@Autowired
	private AsynLogDao asynLogDao;
	
	@Override
	public boolean save(AsynLog asynLog) throws DataSavingException {
		AsynLogEntity entity = new AsynLogEntity();
		BeanUtils.copyProperties(asynLog, entity);
		return asynLogDao.insert(entity);
	}

}
