package com.jingyunbank.etrade.asyn.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.asyn.bo.AsynSchedule;
import com.jingyunbank.etrade.api.asyn.service.IAsynScheduleService;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.asyn.dao.AsynScheduleDao;
import com.jingyunbank.etrade.asyn.entity.AsynScheduleEntity;

@Service("asynScheduleService")
public class AsynScheduleService implements IAsynScheduleService {
	
	@Autowired
	private AsynScheduleDao asynScheduleDao;

	@Override
	@Transactional
	public boolean save(AsynSchedule asynSchedule) throws DataSavingException {
		AsynScheduleEntity entity = new AsynScheduleEntity();
		BeanUtils.copyProperties(asynSchedule, entity);
		try {
			return asynScheduleDao.insert(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public boolean refreshStatus(String id, String status)
			throws DataRefreshingException {
		try {
			return asynScheduleDao.updateStatus(id, status);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	public boolean remove(String id) throws DataRemovingException {
		// TODO Auto-generated method stub
		try {
			return asynScheduleDao.delete(id);
		} catch (Exception e) {
			throw new DataRemovingException(e);
		}
	}

	@Override
	public AsynSchedule single(String id) {
		AsynScheduleEntity entity = asynScheduleDao.selectOne(id);
		if(entity!=null){
			AsynSchedule bo = new AsynSchedule();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}
		return null;
	}

	@Override
	public List<AsynSchedule> list(Range range) {
		return asynScheduleDao.selectList(range.getFrom(), range.getTo()-range.getFrom()).stream().map( entity->{
			AsynSchedule bo = new AsynSchedule();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}).collect(Collectors.toList());
	}

}
