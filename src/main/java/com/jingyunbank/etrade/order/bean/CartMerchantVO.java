package com.jingyunbank.etrade.order.bean;

import java.util.ArrayList;
import java.util.List;

public class CartMerchantVO {

	private String ID;
	private String name;
	private List<GoodsInCartVO> goods = new ArrayList<GoodsInCartVO>();
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public List<GoodsInCartVO> getGoods() {
		return goods;
	}
	public void setGoods(List<GoodsInCartVO> goods) {
		this.goods = goods;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
