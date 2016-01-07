package com.jingyunbank.etrade.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.user.entity.RoleEntity;

public interface RoleDao {

	public List<RoleEntity> selectList();
	
	public RoleEntity selectOne(@Param("key") String idOrCode);
}
