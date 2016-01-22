package com.jingyunbank.etrade.user.dao;

import com.jingyunbank.etrade.user.entity.QQLoginEntity;

public interface QQLoginDao {

	QQLoginEntity selectOne(String accessToken);

	boolean insert(QQLoginEntity entity) throws Exception;

	boolean updateLoginTime(String accessToken) throws Exception;

}
