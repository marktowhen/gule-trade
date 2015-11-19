package com.jingyunbank.etrade.order.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CartVO implements Serializable{

	private static final long serialVersionUID = -2754822804470567274L;

	private String UID;
	
	@NotNull
	@Size(min=1)
	private List<OrdersInCartVO> orders = new ArrayList<OrdersInCartVO>();

	public List<OrdersInCartVO> getOrders() {
		return orders;
	}

	public void setOrders(List<OrdersInCartVO> orders) {
		this.orders = orders;
	}

	public String getUID() {
		return UID;
	}

	public void setUID(String uID) {
		UID = uID;
	}
}
