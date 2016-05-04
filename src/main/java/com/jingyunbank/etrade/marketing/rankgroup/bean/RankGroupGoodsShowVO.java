package com.jingyunbank.etrade.marketing.rankgroup.bean;

import java.math.BigDecimal;

import com.jingyunbank.etrade.wap.goods.bean.GoodsSkuVO;
import com.jingyunbank.etrade.wap.goods.bean.GoodsVO;

public class RankGroupGoodsShowVO {
	private GoodsVO goods;
	private GoodsSkuVO sku;
	private BigDecimal showPrice;
	public GoodsVO getGoods() {
		return goods;
	}
	public void setGoods(GoodsVO goods) {
		this.goods = goods;
	}
	public GoodsSkuVO getSku() {
		return sku;
	}
	public void setSku(GoodsSkuVO sku) {
		this.sku = sku;
	}
	public BigDecimal getShowPrice() {
		return showPrice;
	}
	public void setShowPrice(BigDecimal showPrice) {
		this.showPrice = showPrice;
	}
	

}
