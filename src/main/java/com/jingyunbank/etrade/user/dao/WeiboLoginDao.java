package com.jingyunbank.etrade.user.dao;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.user.entity.WeiboLoginEntity;

public interface WeiboLoginDao {
	
	WeiboLoginEntity selectOne(@Param("key")String accessTokenOrWeiboUID);

	boolean insert(WeiboLoginEntity entity) throws Exception;

	boolean updateLoginTime(@Param("key")String accessTokenOrWeiboUID) throws Exception;

	boolean updateByID(@Param("ID")String id,@Param("accessToken") String accessToken,@Param("UID") String uid);

}
