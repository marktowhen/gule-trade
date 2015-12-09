package com.jingyunbank.etrade.order.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.api.order.bo.OrderStatusDesc;
import com.jingyunbank.etrade.order.entity.OrderEntity;

public interface OrderDao{

	public void insertMany(@Param(value="orders") List<OrderEntity> orders) throws Exception;
	
	public void insertOne(OrderEntity order) throws Exception ;

	public List<OrderEntity> selectAll();

	public List<OrderEntity> selectBetween(@Param(value="start") Date start, @Param(value="end") Date end) ;

	public List<OrderEntity> selectByUID(String uid);
	
	public List<OrderEntity> selectWithCondition(
			@Param("uid") String uid, 
			@Param("statuscode") String statuscode,
			@Param("fromdate") String fromdate,
			@Param("keywords") String keywords,
			@Param("from") long from, 
			@Param("size") int size);
	
	public void delete(String id) throws Exception;

	public void updateStatus(@Param("oids") List<String> oids, @Param("status") OrderStatusDesc status) throws Exception;

	public List<OrderEntity> selectByExtranso(String extransno);

	public OrderEntity selectOne(String oid);

}
