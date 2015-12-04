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
	private String pipelineCode;
	@NotBlank
	private String pipelineName;
	@NotBlank
	private String bankCode;
	@NotBlank
	@Size(min=6)
	private String tradepwd;

	@Valid
	@NotNull
	@Size(min=1)
	private List<OrderPaymentVO> payments = new ArrayList<OrderPaymentVO>();
	
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
	public String getPipelineCode() {
		return pipelineCode;
	}
	public void setPipelineCode(String pipelineCode) {
		this.pipelineCode = pipelineCode;
	}
	public String getPipelineName() {
		return pipelineName;
	}
	public void setPipelineName(String pipelineName) {
		this.pipelineName = pipelineName;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
}
