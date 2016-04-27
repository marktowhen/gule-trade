package com.jingyunbank.etrade.marketing.group.entity;

import java.math.BigDecimal;

import com.jingyunbank.etrade.wap.goods.entity.GoodsEntity;
import com.jingyunbank.etrade.wap.goods.entity.GoodsSkuEntity;

public class GroupGoodsShowEntity extends GroupGoodsEntity{

	
	private GoodsSkuEntity sku;
	private GoodsEntity goods;
	private BigDecimal showPrice;
	
	
	public BigDecimal getShowPrice() {
		return showPrice;
	}
	public void setShowPrice(BigDecimal showPrice) {
		this.showPrice = showPrice;
	}
	public GoodsSkuEntity getSku() {
		return sku;
	}
	public void setSku(GoodsSkuEntity sku) {
		this.sku = sku;
	}
	public GoodsEntity getGoods() {
		return goods;
	}
	public void setGoods(GoodsEntity goods) {
		this.goods = goods;
	}
	
}
