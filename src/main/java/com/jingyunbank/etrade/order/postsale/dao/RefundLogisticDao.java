package com.jingyunbank.etrade.order.postsale.dao;

import com.jingyunbank.etrade.order.postsale.entity.RefundLogisticEntity;


public interface RefundLogisticDao {

	public void insertOne(RefundLogisticEntity logistic) throws Exception;

	public RefundLogisticEntity selectRID(String rid);
	
}
