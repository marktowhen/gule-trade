package com.jingyunbank.etrade.goods.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 
* Title: 	根据条件搜索的店铺信息
* @author    duanxf
* @date      2015年11月10日
 */
public class GoodsMerchantEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String MID; // 店铺ID
	private String merchantName; // 店铺名
	private String merchantBrands; // 主营品牌
	private String merchantDesc; // 店铺介绍
	private String merchantImg; //店铺图片
	private String goodscount; // 相关产品数量
	public String getMID() {
		return MID;
	}
	public void setMID(String mID) {
		MID = mID;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getMerchantBrands() {
		return merchantBrands;
	}
	public void setMerchantBrands(String merchantBrands) {
		this.merchantBrands = merchantBrands;
	}
	public String getMerchantDesc() {
		return merchantDesc;
	}
	public void setMerchantDesc(String merchantDesc) {
		this.merchantDesc = merchantDesc;
	}
	public String getGoodscount() {
		return goodscount;
	}
	public void setGoodscount(String goodscount) {
		this.goodscount = goodscount;
	}
	public String getMerchantImg() {
		return merchantImg;
	}
	public void setMerchantImg(String merchantImg) {
		this.merchantImg = merchantImg;
	}

	
}
