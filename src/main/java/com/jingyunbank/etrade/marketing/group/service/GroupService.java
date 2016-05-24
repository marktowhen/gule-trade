package com.jingyunbank.etrade.marketing.group.service;

import java.util.ArrayList;
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
import com.jingyunbank.etrade.marketing.group.entity.GroupGoodsEntity;
import com.jingyunbank.etrade.marketing.group.entity.GroupUserEntity;

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
			if(goods.isPresent() && gUser.size()==goods.get().getGroupPeople()){
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Group> list(String status) {
		return convertEntityToBo( groupDao.selectList(status));
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
	public List<Group> listOnDeadline() {
		return convertEntityToBo( groupDao.selectListOnDeadline());
	}

	@Override
	public List<Group> listOnSuccess() {
		return convertEntityToBo( groupDao.selectListOnSuccess());
	}

	@Override
	public List<Group> listStartFail() {
		return convertEntityToBo(groupDao.selectListStartFail());
	}

	@Override
	public List<Group> listConveneTimeOut() {
		return convertEntityToBo(groupDao.selectListConveneTimeOut());
	}

	@Override
	public List<Group> listStartSuccess() {
		return convertEntityToBo(groupDao.selectListStartSuccess());
	}
	
	private List<Group> convertEntityToBo(List<GroupEntity> entityList){
		if(entityList!=null){
			return entityList.stream().map( entity->{
				Group bo = new Group();
				BeanUtils.copyProperties(entity, bo, "buyers","goods");
				
				bo.setBuyers(converUserEnToBo(entity.getBuyers()));
				bo.setGoods(converGoodsEnToBo(entity.getGoods()));
				return bo;
			}).collect(Collectors.toList());
		}
		return new ArrayList<Group>();
	}
	
	private List<GroupUser> converUserEnToBo(List<GroupUserEntity> entityList){
		if(entityList!=null){
			return entityList.stream().map( entity->{
				GroupUser bo = new GroupUser();
				BeanUtils.copyProperties(entity, bo);
				return bo;
			}).collect(Collectors.toList());
		}
		return new ArrayList<GroupUser>();
	}
	
	private GroupGoods converGoodsEnToBo(GroupGoodsEntity entity){
		GroupGoods bo = new GroupGoods();
		if(entity!=null){
			BeanUtils.copyProperties(entity, bo);
		}
		return bo;
	}


}
