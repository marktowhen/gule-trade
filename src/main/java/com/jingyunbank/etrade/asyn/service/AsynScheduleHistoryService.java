package com.jingyunbank.etrade.asyn.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.asyn.bo.AsynScheduleHistory;
import com.jingyunbank.etrade.api.asyn.service.IAsynScheduleHistoryService;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.asyn.dao.AsynScheduleHistoryDao;
import com.jingyunbank.etrade.asyn.entity.AsynScheduleHistoryEntity;

@Service("asynScheduleHistoryService")
public class AsynScheduleHistoryService implements IAsynScheduleHistoryService {

	@Autowired
	private AsynScheduleHistoryDao asynScheduleHistoryDao;
	
	@Override
	public boolean save(AsynScheduleHistory asynHistory)
			throws DataSavingException {
		AsynScheduleHistoryEntity entity = new AsynScheduleHistoryEntity();
		BeanUtils.copyProperties(asynHistory, entity);
		return asynScheduleHistoryDao.insert(entity);
	}

	@Override
	public boolean saveFromAsynSchedule(String scheduleID)
			throws DataSavingException {
		return asynScheduleHistoryDao.insertFromAsynSchedule(scheduleID);
	}

}
