package com.jingyunbank.etrade.pay.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.pay.entity.OrderPaymentEntity;

public interface PayDao {

	public void insertOne(OrderPaymentEntity payment) throws Exception;
	
	public void insertMany(@Param("payments") List<OrderPaymentEntity> payments) throws Exception;

	public List<OrderPaymentEntity> selectPayments(@Param("oids") List<String> oids);
	
	public void updateMany(@Param("payments") List<OrderPaymentEntity> payments) throws Exception;
	
}
