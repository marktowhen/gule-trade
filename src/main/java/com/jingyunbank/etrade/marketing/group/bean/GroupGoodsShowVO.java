package com.jingyunbank.etrade.marketing.group.bean;


import com.jingyunbank.etrade.wap.goods.bean.GoodsSkuVO;
import com.jingyunbank.etrade.wap.goods.bean.GoodsVO;

public class GroupGoodsShowVO extends GroupGoodsVO{

	
	private GoodsVO goods;
	private GoodsSkuVO sku;
	
	public GoodsSkuVO getSku() {
		return sku;
	}
	public void setSku(GoodsSkuVO sku) {
		this.sku = sku;
	}
	public GoodsVO getGoods() {
		return goods;
	}
	public void setGoods(GoodsVO goods) {
		this.goods = goods;
	}
	
}
