package com.jingyunbank.etrade.marketing.rankgroup.bean;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

public class RankGroupGoodsPriceSettingVO {
	private RankGroupGoodsVO goods;
	private String GGID;
	private int floor;//人数下边界
	private int ceil;  //人数上边界 
	@NotNull
	@DecimalMin(value="0.0", inclusive=false)
	private BigDecimal price; //相应的价格 
	
	public RankGroupGoodsVO getGoods() {
		return goods;
	}
	public void setGoods(RankGroupGoodsVO goods) {
		this.goods = goods;
	}
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
	public int getCeil() {
		return ceil;
	}
	public void setCeil(int ceil) {
		this.ceil = ceil;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
