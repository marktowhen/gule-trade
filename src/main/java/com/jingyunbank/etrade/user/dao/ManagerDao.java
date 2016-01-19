package com.jingyunbank.etrade.user.dao;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.user.entity.ManagerEntity;

public interface ManagerDao {

	public ManagerEntity selectByKey(@Param("key")String idOrMname);
	
	public boolean updatePassword(@Param("id")String id, @Param("password")String password) throws Exception;
}
