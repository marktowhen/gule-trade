package com.jingyunbank.etrade.marketing.flashsale.dao;

import com.jingyunbank.etrade.marketing.flashsale.entity.FlashSaleOrderEntity;

public interface FlashSaleOrderDao {

	public int add(FlashSaleOrderEntity flashSaleOrderentity) throws Exception;
	
	public FlashSaleOrderEntity selectFlashOrderByoid(String oid);
	
	public FlashSaleOrderEntity selectFlashOrderByuid(String flashUserId);
}
