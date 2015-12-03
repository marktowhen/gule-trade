package com.jingyunbank.etrade.pay.bean;

public class PayPipelineVO {
	
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
