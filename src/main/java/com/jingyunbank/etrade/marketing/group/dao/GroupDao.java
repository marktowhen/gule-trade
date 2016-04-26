package com.jingyunbank.etrade.marketing.group.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.api.marketing.group.bo.GroupUser;
import com.jingyunbank.etrade.marketing.group.entity.GroupEntity;

public interface GroupDao {

	public boolean insert(GroupEntity entity) throws Exception;
	
	public GroupEntity selectOne(String ID);
	
	public boolean updateStatus(@Param("ID")String ID, @Param("status")String status) throws Exception;

	public List<GroupEntity> selectList(String status);

	public List<GroupEntity> selectListOnDeadline(int minute);

	public List<GroupUser> selectListOnSuccess();

	public List<GroupUser> selectListStartFail();

	public List<GroupUser> selectListConvening();

	public List<GroupUser> selectListStartSuccess();
}
