package com.jingyunbank.etrade.marketing.auction.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.marketing.auction.entity.AuctionOrderEntity;


public interface AuctionOrderDao {

	boolean insert(AuctionOrderEntity convert) throws Exception;

	AuctionOrderEntity selectOne(String IDorOID);

	List<AuctionOrderEntity> selectList(@Param("auctionGoodsID")String auctionGoodsID,@Param("auctionUserID") String auctionUserID);



}
