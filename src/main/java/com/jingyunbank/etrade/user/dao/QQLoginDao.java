package com.jingyunbank.etrade.user.dao;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.user.entity.QQLoginEntity;

public interface QQLoginDao {

	QQLoginEntity selectOne(String ID);

	boolean insert(QQLoginEntity entity) throws Exception;


	boolean update(@Param("ID")String id,@Param("accessToken") String accessToken,@Param("UID") String uid);

}
