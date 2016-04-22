package com.jingyunbank.etrade.marketing.group.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.marketing.group.entity.GroupUserEntity;

public interface GroupUserDao {

	public boolean insert(GroupUserEntity entity) throws Exception;
	
	public GroupUserEntity selectOne(String ID);

	public List<GroupUserEntity> selectList(String groupID);

	public boolean updateStatus(@Param("ID")String iD,@Param("status") String status)throws Exception;
}
