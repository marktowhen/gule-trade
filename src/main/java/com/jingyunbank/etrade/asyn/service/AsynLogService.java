package com.jingyunbank.etrade.asyn.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jingyunbank.core.KeyGen;
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
		try {
			return asynLogDao.insert(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public boolean save(String scheduleID, String status, String remark)
			throws DataSavingException {
		AsynLogEntity entity = new AsynLogEntity();
		entity.setID(KeyGen.uuid());
		entity.setScheduleID(scheduleID);
		entity.setStatus(status);
		if(!StringUtils.isEmpty(remark)){
			remark = remark.length()>50? remark.substring(0, 50) :remark;
			entity.setRemark(remark);
		}
		try {
			return asynLogDao.insert(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

}
