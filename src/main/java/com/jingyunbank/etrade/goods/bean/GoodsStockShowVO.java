package com.jingyunbank.etrade.goods.bean;

import java.io.Serializable;
/**
 * 
* Title: 获取商品的库存数量
* @author    duanxf
* @date      2015年12月30日
 */
public class GoodsStockShowVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String GID; 
	private int count;
	
	public String getGID() {
		return GID;
	}
	public void setGID(String gID) {
		GID = gID;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

	
	
	
}
