package com.jingyunbank.etrade.marketing.group.service;

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
import com.jingyunbank.etrade.api.flow.service.IFlowStatusService;
import com.jingyunbank.etrade.api.marketing.group.bo.Group;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupGoods;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupUser;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupGoodsService;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupService;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupUserService;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.marketing.group.dao.GroupDao;
import com.jingyunbank.etrade.marketing.group.entity.GroupEntity;

@Service("groupService")
public class GroupService implements IGroupService{
	
	@Autowired
	private GroupDao groupDao;
	@Autowired
	private IUserService userService;
	@Autowired
	private IGroupGoodsService groupGoodsService;
	@Autowired
	private IGroupUserService groupUserService;
	@Autowired
	private IFlowStatusService flowStatusService;

	@Override
	public Optional<Group> single(String groupid) {
		GroupEntity entity = groupDao.selectOne(groupid);
		if(Objects.nonNull(entity)){
			Group bo = new Group();
			BeanUtils.copyProperties(entity, bo);
			return Optional.of(bo);
		}
		return Optional.empty();
	}

	@Override
	public boolean save(Group group) throws DataSavingException {
		GroupEntity entity = new GroupEntity();
		BeanUtils.copyProperties(group, entity);
		try {
			return groupDao.insert(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public boolean refreshStatus(String ID, String status)
			throws DataRefreshingException {
		try {
			return groupDao.updateStatus(ID, status);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	public boolean isLeader(String groupID, String uid) {
		Optional<Group> group = this.single(groupID);
		if(group.isPresent()){
			return group.get().getLeaderUID().equals(uid);
		}
		return false;
	}

	@Override
	public boolean full(String groupID) {
		Optional<Group> group = this.single(groupID);
		if(group.isPresent()){
			Optional<GroupGoods> goods = groupGoodsService.single(group.get().getGroupGoodsID());
			List<GroupUser> gUser = groupUserService.list(groupID, GroupUser.STATUS_PAID);
			if(goods.isPresent() && gUser.size()>=goods.get().getMinpeople()){
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Group> list(String status) {
		return groupDao.selectList(status).stream().map( entity->{
			Group bo = new Group();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}).collect(Collectors.toList());
	}

	@Override
	public boolean refreshStatus(String ID, String currentStatus,
			String flowStatusFlag) throws DataRefreshingException {
		Optional<FlowStatus> flowStatus = flowStatusService.single(Group.FLOW_TYPE, currentStatus, flowStatusFlag);
		if(flowStatus.isPresent()){
			try {
				return groupDao.updateStatus(ID, flowStatus.get().getNextStatus());
			} catch (Exception e) {
				throw new DataRefreshingException(e);
			}
		}
		return false;
	}

	@Override
	public List<Group> listOnDeadline(int minute) {
		return groupDao.selectListOnDeadline(minute).stream().map( entity->{
			Group bo = new Group();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}).collect(Collectors.toList());
	}

	@Override
	public List<Group> listOnSuccess() {
		return groupDao.selectListOnSuccess().stream().map( entity->{
			Group bo = new Group();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}).collect(Collectors.toList());
	}

	@Override
	public List<Group> listStartFail() {
		return groupDao.selectListStartFail().stream().map( entity->{
			Group bo = new Group();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}).collect(Collectors.toList());
	}

	@Override
	public List<Group> listConvening() {
		return groupDao.selectListConvening().stream().map( entity->{
			Group bo = new Group();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}).collect(Collectors.toList());
	}

	@Override
	public List<Group> listStartSuccess() {
		return groupDao.selectListStartSuccess().stream().map( entity->{
			Group bo = new Group();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}).collect(Collectors.toList());
	}


}
