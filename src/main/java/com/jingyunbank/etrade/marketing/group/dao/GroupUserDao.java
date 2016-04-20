package com.jingyunbank.etrade.marketing.group.dao;

import com.jingyunbank.etrade.marketing.group.entity.GroupUserEntity;

public interface GroupUserDao {

	public boolean insert(GroupUserEntity entity) throws Exception;
	
	public GroupUserEntity selectOne(String ID);
}
