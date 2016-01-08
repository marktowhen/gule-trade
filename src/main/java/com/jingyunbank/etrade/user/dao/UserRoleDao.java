package com.jingyunbank.etrade.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.user.entity.UserRoleEntity;

public interface UserRoleDao {
	
	public List<UserRoleEntity> selectList(String uid);
	
	public UserRoleEntity selectOne(String id);
	
	public boolean updateValid(@Param("valid") boolean valid,@Param("ids")  String[] ids);
	
	public UserRoleEntity selectByUidAndCode(@Param("uid")String uid,@Param("code") String roleCode);
	
	

}
