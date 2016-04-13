package com.jingyunbank.etrade.marketing.group.bean;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class GroupGoodsPriceSettingVO {
	
	private String GGID;//团购商品id
	private GroupGoodsVO goods;
	@Min(1)
	private int floor;//人数门槛
	@Min(2)
	private int ceiling;//人数上限
	@NotNull
	@DecimalMin(value="0.0", inclusive=false)
	private BigDecimal price;
	public String getGGID() {
		return GGID;
	}
	public void setGGID(String gGID) {
		GGID = gGID;
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
	public GroupGoodsVO getGoods() {
		return goods;
	}
	public void setGoods(GroupGoodsVO goods) {
		this.goods = goods;
	}
	
}
