package com.jingyunbank.etrade.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.user.entity.ManagerRoleEntity;

public interface UserRoleDao {
	
	public List<ManagerRoleEntity> selectList(String uid);
	
	public ManagerRoleEntity selectOne(String id);
	
	public boolean updateValid(@Param("valid") boolean valid,@Param("ids")  String[] ids);
	
	public ManagerRoleEntity selectByUidAndCode(@Param("uid")String uid,@Param("code") String roleCode);
	
	

}
