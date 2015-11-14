package com.jingyunbank.etrade.pay.entity;


/**
 * 支付平台领域对象<br>
 * 该类实例描述可能的支付平台信息，如各家银行网关支付，支付宝，京东支付等等。<br>
 * 其中应当包括支付平台名称，平台代码，是否是第三方支付
 */
public class PayPlatformEntity{

	private String ID;
	private String name;
	private String code;
	private boolean thirdpaty;
	
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
	
}
