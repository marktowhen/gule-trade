package com.jingyunbank.etrade.marketing.auction.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.marketing.auction.entity.AuctionGoodsEntity;

public interface AuctionGoodsDao {

	boolean insert(AuctionGoodsEntity entity) throws Exception;

	boolean updateStatus(@Param("ID")String auctionGoodsID, @Param("status")String status) throws Exception;

	AuctionGoodsEntity selectOne(String ID);

	List<AuctionGoodsEntity> selectList(@Param("offset")long offset, @Param("size")long size);

	boolean updateSoldPrice(@Param("ID")String auctionGoodsID,@Param("soldPrice") BigDecimal soldPrice,@Param("soldUID") String soldUID)throws Exception;

	boolean updateDelayAmount(@Param("ID")String ID,@Param("endTime") Date endTime);

	int count();


}
