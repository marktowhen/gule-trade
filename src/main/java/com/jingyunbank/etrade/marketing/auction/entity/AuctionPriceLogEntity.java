package com.jingyunbank.etrade.marketing.auction.entity;

import java.math.BigDecimal;
import java.util.Date;

public class AuctionPriceLogEntity  {

	
	private String ID;
	private String auctionGoodsID;
	private String auctionUserID;
	private String UID;
	private BigDecimal price;
	private Date addtime;
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
	public String getAuctionUserID() {
		return auctionUserID;
	}
	public void setAuctionUserID(String auctionUserID) {
		this.auctionUserID = auctionUserID;
	}
	public String getUID() {
		return UID;
	}
	public void setUID(String uID) {
		UID = uID;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	
	

}
