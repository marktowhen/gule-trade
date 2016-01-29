package com.jingyunbank.etrade.goods.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
* Title: 用于转换商品的价格参数
* @author    duanxf
* @date      2016年1月29日
 */
public class GoodsPriceConvert implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private BigDecimal beginPrice;
	private BigDecimal EndPrice;
	public BigDecimal getBeginPrice() {
		return beginPrice;
	}
	public void setBeginPrice(BigDecimal beginPrice) {
		this.beginPrice = beginPrice;
	}
	public BigDecimal getEndPrice() {
		return EndPrice;
	}
	public void setEndPrice(BigDecimal endPrice) {
		EndPrice = endPrice;
	}
	
	

}
