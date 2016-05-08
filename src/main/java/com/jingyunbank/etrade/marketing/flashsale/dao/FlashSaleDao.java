package com.jingyunbank.etrade.marketing.flashsale.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.marketing.flashsale.entity.FlashSaleEntity;
import com.jingyunbank.etrade.marketing.flashsale.entity.FlashSaleShowEntity;

public interface FlashSaleDao {
	
	public int addFlashSale(FlashSaleEntity flashSaleEntity) throws Exception;
	
	public List<FlashSaleShowEntity> selectFlashSaleMany(@Param("MID")String MID, @Param("from") long from, 
			@Param("size") int size);
	
	public List<FlashSaleShowEntity> selectFlashSaleByCondition(@Param("from") long from,@Param("size") int size);
	
	public FlashSaleEntity selectFlashSaleById(String id);
	
	public int update(FlashSaleEntity flashSaleEntity) throws Exception;
	//修改库存的代码
	public int updateStock(FlashSaleEntity flashSaleEntity) throws Exception;
}
