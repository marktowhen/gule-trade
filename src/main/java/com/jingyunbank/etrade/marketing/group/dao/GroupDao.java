package com.jingyunbank.etrade.marketing.group.dao;

import com.jingyunbank.etrade.marketing.group.entity.GroupEntity;

public interface GroupDao {

	public boolean insert(GroupEntity entity) throws Exception;
	
	public GroupEntity selectOne(String ID);
}
