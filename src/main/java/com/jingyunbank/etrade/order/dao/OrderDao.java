package com.jingyunbank.etrade.order.dao;

import java.util.List;

import com.jingyunbank.etrade.order.entity.OrderEntity;

public interface OrderDao{

	//public boolean insert(OrderEntity order) throws DataSavingException ;

	//public boolean update(OrderEntity order) throws DataUpdatingException ;

	//public Optional<OrderEntity> selectByOrderNo(String orderno) ;

	public List<OrderEntity> select(String uid) ;

	//public List<OrderEntity> select(Date start, Date end) ;

}
