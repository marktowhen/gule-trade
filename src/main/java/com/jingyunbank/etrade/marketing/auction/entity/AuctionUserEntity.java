package com.jingyunbank.etrade.marketing.auction.entity;

import java.util.Date;

public class AuctionUserEntity  {
	
	private String ID;
	private String auctionGoodsID;
	private String UID;
	private String status;
	private Date joinTime;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getAuctionGoodsID() {
		return auctionGoodsID;
	}
	public void setAuctionGoodsID(String auctionGoodsID) {
		this.auctionGoodsID = auctionGoodsID;
	}
	public String getUID() {
		return UID;
	}
	public void setUID(String uID) {
		UID = uID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getJoinTime() {
		return joinTime;
	}
	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}
	
	

}
