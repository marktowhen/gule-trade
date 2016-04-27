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
	private int city;
	private String receiver;//收货人姓名
	private String address;//收货地址
	private String mobile ;//收货人电话
	private String zipcode;
	private String addressid;
	
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

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getAddressid() {
		return addressid;
	}

	public void setAddressid(String addressid) {
		this.addressid = addressid;
	}
}
