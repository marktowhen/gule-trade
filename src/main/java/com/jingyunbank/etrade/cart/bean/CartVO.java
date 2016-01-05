package com.jingyunbank.etrade.cart.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CartVO implements Serializable{

	private static final long serialVersionUID = -2754822804470567274L;

	private String UID;
	
	@Valid
	@NotNull
	@Size(min=1)
	private List<OrdersInCartVO> orders = new ArrayList<OrdersInCartVO>();

	private BigDecimal totalPrice = BigDecimal.ZERO;
	private BigDecimal totalPriceWithoutPostage = BigDecimal.ZERO;
	
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

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public BigDecimal getTotalPriceWithoutPostage() {
		return totalPriceWithoutPostage;
	}

	public void setTotalPriceWithoutPostage(BigDecimal totalPriceWithoutPostage) {
		this.totalPriceWithoutPostage = totalPriceWithoutPostage;
	}
}
