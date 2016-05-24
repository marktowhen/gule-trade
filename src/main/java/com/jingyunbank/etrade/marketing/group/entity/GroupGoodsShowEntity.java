package com.jingyunbank.etrade.marketing.group.entity;


import com.jingyunbank.etrade.wap.goods.entity.GoodsEntity;
import com.jingyunbank.etrade.wap.goods.entity.GoodsSkuEntity;

public class GroupGoodsShowEntity extends GroupGoodsEntity{

	
	private GoodsSkuEntity sku;
	private GoodsEntity goods;

	
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
