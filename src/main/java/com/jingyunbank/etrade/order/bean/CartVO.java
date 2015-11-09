package com.jingyunbank.etrade.order.bean;

import java.util.ArrayList;
import java.util.List;

public class CartVO {

	private String UID;//下单人
	private List<CartMerchantVO> merchants = new ArrayList<CartMerchantVO>();
	
	public String getUID() {
		return UID;
	}
	public void setUID(String uID) {
		UID = uID;
	}
	
	public List<CartMerchantVO> getMerchants() {
		return merchants;
	}
	public void setMerchants(List<CartMerchantVO> merchants) {
		this.merchants = merchants;
	}
}
