package com.jingyunbank.etrade.asyn.service;

import java.util.ArrayList;
import java.util.List;


import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.jingyunbank.etrade.api.asyn.bo.AsynParam;
import com.jingyunbank.etrade.api.asyn.service.IAsynParamService;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.asyn.dao.AsynParamDao;
import com.jingyunbank.etrade.asyn.entity.AsynParamEntity;

@Service("asynParamService")
public class AsynParamService implements IAsynParamService {
	
	@Autowired
	private AsynParamDao asynParamDao;

	@Override
	public boolean saveMutl(List<AsynParam> asymParams)
			throws DataSavingException {
		List<AsynParamEntity> entityList = asymParams.stream().map( bo ->{
			AsynParamEntity entity = new AsynParamEntity();
			BeanUtils.copyProperties(bo, entity);
			return entity;
		}).collect(Collectors.toList());
		return asynParamDao.insertMutl(entityList);
	}

	@Override
	public List<AsynParam> list(String schedule) {
		return asynParamDao.selectBySchedule(schedule).stream().map( entity ->{
			AsynParam bo = new AsynParam();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}).collect(Collectors.toList());
	}

}
