package com.jingyunbank.etrade.order.presale.dao;

import com.jingyunbank.etrade.order.presale.entity.OrderLogisticEntity;

public interface OrderLogisticDao {

	public void insertOne(OrderLogisticEntity logistic) throws Exception;

	public OrderLogisticEntity selectOID(String oid);
	
}
