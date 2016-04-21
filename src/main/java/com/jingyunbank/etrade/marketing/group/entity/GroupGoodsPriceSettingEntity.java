package com.jingyunbank.etrade.marketing.group.entity;

import java.math.BigDecimal;

/**
 * 团购阶梯价，人数越多价格约低
 */
public class GroupGoodsPriceSettingEntity {

	private GroupGoodsEntity goods;
	private String ID;
	private String GGID;
	private int floor;//人数门槛
	private int ceiling;//人数上限
	private BigDecimal price;
	public GroupGoodsEntity getGoods() {
		return goods;
	}
	public void setGoods(GroupGoodsEntity goods) {
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
}
