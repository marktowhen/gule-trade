package com.jingyunbank.etrade.marketing.group.dao;

import java.util.List;

import com.jingyunbank.etrade.marketing.group.entity.GroupUserEntity;

public interface GroupUserDao {

	public boolean insert(GroupUserEntity entity) throws Exception;
	
	public GroupUserEntity selectOne(String ID);

	public List<GroupUserEntity> selectList(String groupID);
}
