package com.jingyunbank.etrade.order.presale.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.order.presale.entity.OrderEntity;

public interface OrderDao{

	public void insertMany(@Param(value="orders")List<OrderEntity> orders) throws Exception;
	
	public void insertOne(OrderEntity order) throws Exception ;

	public List<OrderEntity> selectWithCondition(
			@Param("uid") String uid, 
			@Param("statuscode") String statuscode,
			@Param("fromdate") String fromdate,
			@Param("keywords") String keywords,
			@Param("from") long from, 
			@Param("size") int size);
	public List<OrderEntity> selectmWithCondition(
			@Param("mid") String mid, 
			@Param("statuscode") String statuscode,
			@Param("fromdate") String fromdate,
			@Param("keywords") String keywords,
			@Param("from") long from, 
			@Param("size") int size);
	
	public void updateStatus(@Param("oids") List<String> oids, 
							@Param("statuscode") String statuscode,
							@Param("statusname") String statusname) throws Exception;

	public List<OrderEntity> selectByExtranso(String extransno);

	public OrderEntity selectOne(String oid);

	public List<OrderEntity> selectByOIDs(@Param("oids") List<String> oids);
	
	public int selectCount(
			@Param("uid") String uid, 
			@Param("statuscode") String statuscode,
			@Param("fromdate") String fromdate,
			@Param("keywords") String keywords);
	
	public List<OrderEntity> selectBefore(@Param("deadline") Date deadline, @Param("statuscode") String statuscode);
	
	public List<OrderEntity> selectBetween(@Param("from") Date from, @Param("to") Date to, 
			@Param("statuscode") String statuscode);

}
