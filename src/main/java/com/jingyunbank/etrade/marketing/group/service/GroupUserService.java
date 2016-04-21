package com.jingyunbank.etrade.marketing.group.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupUser;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupUserService;
import com.jingyunbank.etrade.marketing.group.dao.GroupUserDao;
import com.jingyunbank.etrade.marketing.group.entity.GroupUserEntity;

@Service("groupUserService")
public class GroupUserService implements IGroupUserService {
	
	@Autowired
	private GroupUserDao groupUserDao;

	@Override
	public boolean save(GroupUser groupUser) throws DataSavingException {
		GroupUserEntity entity = new GroupUserEntity();
		BeanUtils.copyProperties(groupUser, entity);
		try {
			return groupUserDao.insert(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public Optional<GroupUser> single(String ID) {
		GroupUserEntity entity = groupUserDao.selectOne(ID);
		if(Objects.nonNull(entity)){
			GroupUser bo = new GroupUser();
			BeanUtils.copyProperties(entity, bo);
			return Optional.of(bo);
		}
		return Optional.empty();
	}

	@Override
	public List<GroupUser> list(String groupID) {
		return groupUserDao.selectList(groupID).stream().map( entity->{
			GroupUser bo = new GroupUser();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}).collect(Collectors.toList());
	}

}
