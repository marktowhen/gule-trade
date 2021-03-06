package com.jingyunbank.etrade.message.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.message.entity.MessageEntity;

public interface MessageDao {

	public boolean insert(MessageEntity m) throws Exception;
	
	public boolean insertMulti(@Param(value="messages") List<MessageEntity> messages) throws Exception;
	/**
	 * 更改消息状态 如删除等
	 * @param m
	 * @return
	 * @throws Exception
	 * 2015年11月13日 qxs
	 */
	public boolean updateStatus(MessageEntity m) throws Exception;
	/**
	 * 更改消息的是否已读状态
	 * @param m
	 * @return
	 * @throws Exception
	 * 2015年11月13日 qxs
	 */
	public boolean updateReadStatus(MessageEntity m) throws Exception;
	
	/**
	 * 列表查询
	 * @param m
	 * @param offset
	 * @param size
	 * @return
	 * 2015年12月5日 qxs
	 */
	public List<MessageEntity> selectList(@Param(value="entity") MessageEntity m, @Param(value="offset")long offset, @Param(value="size")long size);
	/**
	 * 单个查询
	 * @param id
	 * @return
	 * 2015年12月5日 qxs
	 */
	public MessageEntity selectSingle(@Param(value="ID")String id);

	/**
	 * 查询数量
	 * @param entity
	 * @return
	 * 2015年11月13日 qxs
	 */
	public int count(@Param(value="entity") MessageEntity entity);
	
	
}
