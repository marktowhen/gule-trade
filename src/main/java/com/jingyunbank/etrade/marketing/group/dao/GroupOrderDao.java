package com.jingyunbank.etrade.marketing.group.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.marketing.group.entity.GroupOrderEntity;

public interface GroupOrderDao {

	public boolean insert(GroupOrderEntity entity) throws Exception;
	
	public GroupOrderEntity selectOne(String ID) ;
	
	public List<GroupOrderEntity> selectList(String groupUserID);

	public GroupOrderEntity selectByOID(String OID);

	public GroupOrderEntity selectListByType(@Param("groupUserID")String groupUserID,@Param("type")String orderType);
}
