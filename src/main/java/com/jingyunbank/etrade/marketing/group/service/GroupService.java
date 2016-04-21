package com.jingyunbank.etrade.marketing.group.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.marketing.group.bo.Group;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupGoodsService;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupService;
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

}
