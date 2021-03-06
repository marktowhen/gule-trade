package com.jingyunbank.etrade.asyn.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.core.KeyGen;
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
		try {
			return asynParamDao.insertMutl(entityList);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public List<AsynParam> list(String schedule) {
		return asynParamDao.selectBySchedule(schedule).stream().map( entity ->{
			AsynParam bo = new AsynParam();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public boolean saveMutl(String scheduleID, Map<String, String> params) throws DataSavingException {
		if(params!=null){
			List<AsynParam> list = new ArrayList<AsynParam>();
			Set<String> keySet = params.keySet();
			for (String key : keySet) {
				AsynParam param = new AsynParam();
				param.setScheduleID(scheduleID);
				param.setID(KeyGen.uuid());
				param.setKey(key);
				param.setValue(params.get(key));
				list.add(param);
			}
			List<AsynParamEntity> entityList = list.stream().map( bo ->{
				AsynParamEntity entity = new AsynParamEntity();
				BeanUtils.copyProperties(bo, entity);
				return entity;
			}).collect(Collectors.toList());
			try {
				return asynParamDao.insertMutl(entityList);
			} catch (Exception e) {
				throw new DataSavingException(e);
			}
		}
		return false;
	}

	@Override
	public Map<String, String> getMap(String scheduleID) {
		List<AsynParam> list = this.list(scheduleID);
		Map<String, String> map = new HashMap<String, String>();
		if(list!=null && !list.isEmpty()){
			for (AsynParam asynParam : list) {
				map.put(asynParam.getKey(), asynParam.getValue());
			}
			return map;
		}
		return map;
	}

}
