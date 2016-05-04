package com.jingyunbank.etrade.marketing.flashsale.bean;

import com.jingyunbank.etrade.wap.goods.bean.GoodsSkuVO;
import com.jingyunbank.etrade.wap.goods.bean.GoodsVO;

public class FlashSaleShowVo extends FlashSaleVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2816213240416982024L;
	private GoodsVO goods;
	private GoodsSkuVO sku;
	/**
	 * @return the goods
	 */
	public GoodsVO getGoods() {
		return goods;
	}
	/**
	 * @param goods the goods to set
	 */
	public void setGoods(GoodsVO goods) {
		this.goods = goods;
	}
	/**
	 * @return the sku
	 */
	public GoodsSkuVO getSku() {
		return sku;
	}
	/**
	 * @param sku the sku to set
	 */
	public void setSku(GoodsSkuVO sku) {
		this.sku = sku;
	}
	
	

}
