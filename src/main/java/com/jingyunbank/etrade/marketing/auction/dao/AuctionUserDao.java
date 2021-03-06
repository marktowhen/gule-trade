package com.jingyunbank.etrade.marketing.auction.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.marketing.auction.entity.AuctionUserEntity;

public interface AuctionUserDao {

	boolean insert(AuctionUserEntity convert) throws Exception;

	AuctionUserEntity selectOne(String ID);
	
	AuctionUserEntity selByUserId(@Param("auctionid")String auctionid,@Param("userid")String userid);
	
	boolean updateStatus(@Param("ID")String ID, @Param("status")String status) throws Exception;

	List<AuctionUserEntity> selectList(String auctionGoodsID);
	
	List<AuctionUserEntity> selMyAuction(@Param("userid")String userid,@Param("status")String status);

	int count(String auctionID);
	
	int ifSign(@Param("auctionid")String auctionid,@Param("uid")String uid);
	
	


}
