package com.jingyunbank.etrade.marketing.group.dao;

import java.util.List;

import com.jingyunbank.etrade.marketing.group.entity.GroupOrderEntity;

public interface GroupOrderDao {

	public boolean insert(GroupOrderEntity entity) throws Exception;
	
	public GroupOrderEntity selectOne(String ID) ;
	
	public List<GroupOrderEntity> selectList(String groupUserID);
}
