package com.jingyunbank.etrade.pay.bean;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 用户支付请求VO<br>
 * 该VO包含需要支付的订单支付信息的列表（多个或者单个订单信息，包括卖家，交易号，商品名，订单价格等），<br>
 */
public class OrderPaymentRequestVO {
	
	@NotBlank
	private String platformCode;

	@NotNull
	@Size(min=1)
	private List<OrderPaymentVO> payments = new ArrayList<OrderPaymentVO>();
	
	public String getPlatformCode() {
		return platformCode;
	}
	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}
	public List<OrderPaymentVO> getPayments() {
		return payments;
	}
	public void setPayments(List<OrderPaymentVO> payments) {
		this.payments = payments;
	}
}
