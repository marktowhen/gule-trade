package com.jingyunbank.etrade.pay.bean;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 用户支付请求VO<p>
 * 该请求表单在支付平台选择页面中组装表提交。<p>
 * 该VO包含需要支付的订单支付信息的列表（多个或者单个订单信息，包括卖家，交易号，商品名，订单价格等），<br>
 */
public class OrderPaymentRequestVO {
	
	@NotBlank
	private String platformCode;
	@NotBlank
	private String platformName;
	@Valid
	@NotBlank
	@Size(min=6)
	private String tradepwd;

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
	public String getTradepwd() {
		return tradepwd;
	}
	public void setTradepwd(String tradepwd) {
		this.tradepwd = tradepwd;
	}
	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
}
