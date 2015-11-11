package com.jingyunbank.etrade.order.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.order.entity.OrderEntity;

public interface OrderDao{

	public void insertMany(@Param(value="orders") List<OrderEntity> orders) throws Exception;
	
	public void insertOne(OrderEntity order) throws Exception ;

	//public boolean update(OrderEntity order) throws DataUpdatingException ;

	//public Optional<OrderEntity> selectByOrderNo(String orderno) ;

	public List<OrderEntity> select();

	public List<OrderEntity> selectBetween(@Param(value="start") Date start, @Param(value="end") Date end) ;

	public List<OrderEntity> selectByUID(String uid);

	public void delete(String id) throws Exception;

}
