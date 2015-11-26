package com.jingyunbank.etrade.goods.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品查询VO对象
 * @author liug
 *
 */
public class GoodsShowVO implements Serializable  {

	private static final long serialVersionUID = 1L;

	private String [] brands; // 品牌数组
	private String [] types;  //类别数组
	private String [] accessorys; //辅料数组
	
	private int priceFlag;
	
	private BigDecimal beginPrice;
	private BigDecimal endPrice;
	
	private int order; //排序规则;
	/*
	销量排序  1
	价格排序  2
	评论排序  3
	新品排序  4
	*/
	private String goodsName; //结果查询使用
	private String MID; //  相关产品 -->店铺ID
	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getMID() {
		return MID;
	}

	public void setMID(String mID) {
		MID = mID;
	}

	public String[] getBrands() { 
		return brands;
	}

	public void setBrands(String[] brands) {
		this.brands = brands;
	}

	public String[] getTypes() {
		return types;
	}

	public void setTypes(String[] types) {
		this.types = types;
	}

	public String[] getAccessorys() {
		return accessorys;
	}

	public void setAccessorys(String[] accessorys) {
		this.accessorys = accessorys;
	}

	


	public BigDecimal getBeginPrice() {
		return beginPrice;
	}

	public void setBeginPrice(BigDecimal beginPrice) {
		this.beginPrice = beginPrice;
	}

	public BigDecimal getEndPrice() {
		return endPrice;
	}

	public void setEndPrice(BigDecimal endPrice) {
		this.endPrice = endPrice;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getPriceFlag() {
		return priceFlag;
	}

	public void setPriceFlag(int priceFlag) {
		this.priceFlag = priceFlag;
	}


	
}
