package com.jingyunbank.etrade.goods.bean;
/**
 * 
* Title: 宝贝推荐VO
* @author    duanxf
* @date      2015年11月10日
 */
public class RecommendGoods {
	
	private String GID; //商品id
	private String goodsName; //商品名称
	private String merchantName; //商家名称
	private String thumb_path_1 ;//推荐展示图片
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
	public String getThumb_path_1() {
		return thumb_path_1;
	}
	public void setThumb_path_1(String thumb_path_1) {
		this.thumb_path_1 = thumb_path_1;
	}
	
	
	
}
