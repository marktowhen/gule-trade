package com.jingyunbank.etrade.goods.bean;

import com.jingyunbank.etrade.api.goods.bo.BaseGoods;

/**
 * 
* Title: 宝贝推荐VO
* @author    duanxf
* @date      2015年11月10日
 */
public class RecommendGoods extends BaseGoods{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String GID; //商品id
	private String goodsName; //商品名称
	private String merchantName; //商家名称
	private String thumbpath1 ;//推荐展示图片
	public String getGID() {
		return GID;
	}
	public void setGID(String gID) {
		GID = gID;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getThumbpath1() {
		return thumbpath1;
	}
	public void setThumbpath1(String thumbpath1) {
		this.thumbpath1 = thumbpath1;
	}
	
	
	
	
}
