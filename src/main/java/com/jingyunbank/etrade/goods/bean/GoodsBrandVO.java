package com.jingyunbank.etrade.goods.bean;

import java.io.Serializable;

/**
 * 
* Title: 	品牌VO
* @author    duanxf
* @date      2015年11月9日
 */
public class GoodsBrandVO implements Serializable{

	private static final long serialVersionUID = 1L;

	private String BID; // 品牌id
	private String brandName; // 所属品牌
	private String brandDesc; // 所属品牌描述
	public String getBID() {
		return BID;
	}
	public void setBID(String bID) {
		BID = bID;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getBrandDesc() {
		return brandDesc;
	}
	public void setBrandDesc(String brandDesc) {
		this.brandDesc = brandDesc;
	}
	
	
	
	
}
