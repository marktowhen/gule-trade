package com.jingyunbank.etrade.user.dao;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.user.entity.SellerEntity;

public interface SellerDao {

	public SellerEntity selectByKey(@Param("key") String idOrSname);
	
	public boolean updatePassword(@Param("id")String id, @Param("password")String password)  throws Exception;
}
