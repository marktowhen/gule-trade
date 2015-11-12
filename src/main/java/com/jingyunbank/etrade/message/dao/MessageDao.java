package com.jingyunbank.etrade.message.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.message.entity.MessageEntity;

public interface MessageDao {

	public boolean insert(MessageEntity m) throws Exception;
	
	public boolean insertMulti(@Param(value="messages") List<MessageEntity> messages) throws Exception;
	
	public boolean update(MessageEntity m) throws Exception;
	
	public List<MessageEntity> selectList(MessageEntity m);
	
	
}
