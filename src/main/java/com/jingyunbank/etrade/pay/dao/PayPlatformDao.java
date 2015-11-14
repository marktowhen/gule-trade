package com.jingyunbank.etrade.pay.dao;

import java.util.List;

import com.jingyunbank.etrade.pay.entity.PayPlatformEntity;

public interface PayPlatformDao {

	public List<PayPlatformEntity>  selectAll();	
}
