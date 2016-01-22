package com.jingyunbank.etrade.user.dao;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.user.entity.WeiboLoginEntity;

public interface WeiboLoginDao {
	
	WeiboLoginEntity selectOne(@Param("key")String accessTokenOrWeiboUID);

	boolean insert(WeiboLoginEntity entity) throws Exception;

	boolean updateLoginTime(@Param("key")String accessTokenOrWeiboUID) throws Exception;

}
