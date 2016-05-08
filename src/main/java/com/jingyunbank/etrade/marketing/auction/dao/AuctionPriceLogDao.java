package com.jingyunbank.etrade.marketing.auction.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.marketing.auction.entity.AuctionPriceLogEntity;


public interface AuctionPriceLogDao {

	boolean insert(AuctionPriceLogEntity convert) throws Exception;

	AuctionPriceLogEntity selectOne(String ID);

	List<AuctionPriceLogEntity> selectListByAGID(String auctionGoodsID);

	int countByAGID(String auctionGoodsID);

	List<AuctionPriceLogEntity> selectListByUID(@Param("auctionGoodsID")String auctionGoodsID,@Param("UID") String UID);

	int countByUID(@Param("auctionGoodsID")String auctionGoodsID,@Param("UID") String UID);




}
