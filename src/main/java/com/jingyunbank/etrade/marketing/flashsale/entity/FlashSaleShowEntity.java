package com.jingyunbank.etrade.marketing.flashsale.entity;

import com.jingyunbank.etrade.wap.goods.entity.GoodsEntity;
import com.jingyunbank.etrade.wap.goods.entity.GoodsSkuEntity;

public class FlashSaleShowEntity extends FlashSaleEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6548091462325348557L;
	private GoodsSkuEntity sku;
	private GoodsEntity goods;
	/**
	 * @return the sku
	 */
	public GoodsSkuEntity getSku() {
		return sku;
	}
	/**
	 * @param sku the sku to set
	 */
	public void setSku(GoodsSkuEntity sku) {
		this.sku = sku;
	}
	/**
	 * @return the goods
	 */
	public GoodsEntity getGoods() {
		return goods;
	}
	/**
	 * @param goods the goods to set
	 */
	public void setGoods(GoodsEntity goods) {
		this.goods = goods;
	}
	
}
