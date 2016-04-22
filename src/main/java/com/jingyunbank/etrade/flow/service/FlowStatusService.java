package com.jingyunbank.etrade.flow.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.flow.bo.FlowStatus;
import com.jingyunbank.etrade.api.flow.service.IFlowStatusService;
import com.jingyunbank.etrade.flow.dao.FlowStatusDao;
import com.jingyunbank.etrade.flow.entity.FlowStatusEntity;

@Service("flowStatusService")
public class FlowStatusService implements IFlowStatusService {

	@Autowired
	private FlowStatusDao flowStatusDao;
	@Override
	public Optional<FlowStatus> single(String flowType, String currentStatus,
			String flag) {
		FlowStatusEntity entity = flowStatusDao.selectOne(flowType, currentStatus, flag);
		if(entity!=null){
			FlowStatus bo = new FlowStatus();
			BeanUtils.copyProperties(entity, bo);
			return Optional.of(bo);
		}
		return Optional.empty();
	}

}
