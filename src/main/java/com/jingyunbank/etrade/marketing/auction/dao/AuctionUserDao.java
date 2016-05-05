package com.jingyunbank.etrade.marketing.auction.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.marketing.auction.entity.AuctionUserEntity;

public interface AuctionUserDao {

	boolean insert(AuctionUserEntity convert) throws Exception;

	AuctionUserEntity selectOne(String ID);
	
	boolean updateStatus(@Param("ID")String ID, @Param("status")String status) throws Exception;

	List<AuctionUserEntity> selectList(String auctionGoodsID);

	int count(String auctionID);


}
