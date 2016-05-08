package com.jingyunbank.etrade.marketing.rankgroup.entity;

import java.math.BigDecimal;

public class RankGroupGoodsPriceSettingEntity {
	
	private String ID;
	private String GGID;
	private RankGroupGoodsEntity goods;
	private int floor;//人数节点
	private int ceiling;//人数上限
	private BigDecimal price;  //对应单价

	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getGGID() {
		return GGID;
	}
	public void setGGID(String gGID) {
		GGID = gGID;
	}
	public RankGroupGoodsEntity getGoods() {
		return goods;
	}
	public void setGoods(RankGroupGoodsEntity goods) {
		this.goods = goods;
	}
	public int getFloor() {
		return floor;
	}
	public void setFloor(int floor) {
		this.floor = floor;
	}
	public int getCeiling() {
		return ceiling;
	}
	public void setCeiling(int ceiling) {
		this.ceiling = ceiling;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
}
