package com.jingyunbank.etrade.pay.entity;


/**
 * 支付平台领域对象<br>
 * 该类实例描述可能的支付平台信息，如各家银行网关支付，支付宝，京东支付等等。<br>
 * 其中应当包括支付平台名称，平台代码，是否是第三方支付
 */
public class PayPipelineEntity{

	private String ID;
	private String name;
	private String code;
	private boolean thirdpaty;
	private String payUrl;
	private String returnUrl;
	private String noticeUrl;
	private String partner;
	private String signtype;
	private String signkey;
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public boolean isThirdpaty() {
		return thirdpaty;
	}
	public void setThirdpaty(boolean thirdpaty) {
		this.thirdpaty = thirdpaty;
	}
	public String getPayUrl() {
		return payUrl;
	}
	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public String getNoticeUrl() {
		return noticeUrl;
	}
	public void setNoticeUrl(String noticeUrl) {
		this.noticeUrl = noticeUrl;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getSigntype() {
		return signtype;
	}
	public void setSigntype(String signtype) {
		this.signtype = signtype;
	}
	public String getSignkey() {
		return signkey;
	}
	public void setSignkey(String signkey) {
		this.signkey = signkey;
	}
	
}
