package com.jingyunbank.etrade.marketing.flashsale.dao;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.marketing.flashsale.entity.FlashSaleUserEntity;

public interface FlashSaleUserDao {
	public int add(FlashSaleUserEntity flashSaleUserEntity) throws Exception;
	
	public FlashSaleUserEntity selectFlashSaleUserByid(String id);
	
	public boolean updateStatus(@Param("id")String id,@Param("orderStatus")String orderStatus)throws Exception;
}
