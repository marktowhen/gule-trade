package com.jingyunbank.etrade.order.presale.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.order.presale.entity.OrderEntity;

public interface OrderDao{

	public void insertMany(@Param(value="orders")List<OrderEntity> orders) throws Exception;
	
	public void insertOne(OrderEntity order) throws Exception ;

	public void updateStatus(@Param("oids") List<String> oids, 
							@Param("statuscode") String statuscode,
							@Param("statusname") String statusname) throws Exception;

	public List<OrderEntity> selectByExtranso(String extransno);

	public OrderEntity selectOne(String oid);

	public List<OrderEntity> selectByOIDs(@Param("oids") List<String> oids);
	
	public List<OrderEntity> selectBefore(
				@Param("deadline") Date deadline, 
				@Param("statuscode") String statuscode);
	
	public List<OrderEntity> selectBetween(@Param("from") Date from, @Param("to") Date to, 
			@Param("statuscode") String statuscode);

	public List<OrderEntity> selectKeyStatus(
			@Param("uid") String uid, 
			@Param("mid") String mid,
			@Param("statuscode") String statuscode,
			@Param("from") long from, 
			@Param("size") int size);
	
	public List<OrderEntity> selectCondition(
			@Param("uid") String uid, 
			@Param("mid") String mid,
			@Param("statuscode") String statuscode, 
			@Param("orderno") String orderno, 
			@Param("gname") String gname, 
			@Param("uname") String uname,
			@Param("mname") String mname, 
			@Param("fromdate") String fromdate, 
			@Param("enddate") String enddate, 
			@Param("from") long from, 
			@Param("size") int size);
	
	public int selectKeywordsCount(
			@Param("uid") String uid, 
			@Param("mid") String mid, 
			@Param("statuscode") String statuscode,
			@Param("keywords") String keywords,
			@Param("fromdate") String fromdate,
			@Param("enddate") String enddate);
	
	public int selectConditionCount(
			@Param("uid") String uid, 
			@Param("mid") String mid,
			@Param("statuscode") String statuscode, 
			@Param("orderno") String orderno, 
			@Param("gname") String gname, 
			@Param("uname") String uname,
			@Param("mname") String mname, 
			@Param("fromdate") String fromdate, 
			@Param("enddate") String enddate);

	public int countCouponOrder(String couponid);
	
	
}
