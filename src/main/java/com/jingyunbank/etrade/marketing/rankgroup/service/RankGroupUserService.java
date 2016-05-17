package com.jingyunbank.etrade.marketing.rankgroup.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.flow.bo.FlowStatus;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupUser;
import com.jingyunbank.etrade.api.marketing.rankgroup.bo.RankGroupUser;
import com.jingyunbank.etrade.api.marketing.rankgroup.service.IRankGroupUserService;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.flow.service.FlowStatusService;
import com.jingyunbank.etrade.marketing.rankgroup.dao.RankGroupUserDao;
import com.jingyunbank.etrade.marketing.rankgroup.entity.RankGroupUserEntity;

@Service("rankGroupUserService")
public class RankGroupUserService implements IRankGroupUserService{
	@Autowired
	RankGroupUserDao rankGroupUserDao;
	@Autowired
	FlowStatusService flowStatusService;

	@Override
	public Integer count(String groupID, String status) {
		
		return rankGroupUserDao.count(groupID, status);
	}

	@Override
	public boolean sendMessage(Users user, String message) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean save(RankGroupUser groupUser) throws DataSavingException {
		RankGroupUserEntity entity = new RankGroupUserEntity();
		BeanUtils.copyProperties(groupUser, entity);
		try {
			return rankGroupUserDao.insert(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public Optional<RankGroupUser> single(String ID) {
		RankGroupUserEntity entity = rankGroupUserDao.selectOne(ID);
		if(Objects.nonNull(entity)){
			RankGroupUser bo = new RankGroupUser();
			BeanUtils.copyProperties(entity, bo);
			return Optional.of(bo);
		}
		return Optional.empty();
	}

	@Override
	public List<RankGroupUser> list(String groupID) {
		return rankGroupUserDao.selectList(groupID).stream().map( entity->{
			RankGroupUser bo = new RankGroupUser();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}).collect(Collectors.toList());
	}

	@Override
	public List<RankGroupUser> list(String groupID, String status) {
		return rankGroupUserDao.selectListWithStatus(groupID, status).stream().map( entity->{
			RankGroupUser bo = new RankGroupUser();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}).collect(Collectors.toList());
	}

	@Override
	public boolean refreshStatus(String ID, String status) throws DataRefreshingException {
		try {
			return rankGroupUserDao.updateStatus(ID, status);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	public boolean refreshStatus(String ID, String currentStatus, String flowStatusFlag)
			throws DataRefreshingException {
		Optional<FlowStatus> flowStatus = flowStatusService.single(RankGroupUser.FLOW_TYPE, currentStatus, flowStatusFlag);
		if(flowStatus.isPresent()){
			try {
				return rankGroupUserDao.updateStatus(ID, flowStatus.get().getNextStatus());
			} catch (Exception e) {
				throw new DataRefreshingException(e);
			}
		}
		return false;
	}

	@Override
	public void notice(RankGroupUser user, String message) {
		System.out.println("====================发送通知===========================");
	
		
	}

	@Override
	public List<RankGroupUser> listUnPay() {
		return rankGroupUserDao.selectListUnPay().stream().map( entity->{
			RankGroupUser bo = new RankGroupUser();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}).collect(Collectors.toList());
	}

	@Override
	public List<RankGroupUser> listUnPay(String gid, String status) {
		
		return null;
	}
	
	
	

}
