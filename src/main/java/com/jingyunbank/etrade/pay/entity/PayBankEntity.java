package com.jingyunbank.etrade.pay.entity;

public class PayBankEntity {
	
	private long ID;
	private String code;
	private String name;
	private String description;
	private boolean gateCredit;//是否支持信用卡网关支付
	private boolean gateDebit;//是否支持储蓄卡网关支付
	private boolean fastCredit;//是否支持信用卡快捷支付
	private boolean fastDebit;//是否支持储蓄卡快捷支付
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isGateCredit() {
		return gateCredit;
	}
	public void setGateCredit(boolean gateCredit) {
		this.gateCredit = gateCredit;
	}
	public boolean isGateDebit() {
		return gateDebit;
	}
	public void setGateDebit(boolean gateDebit) {
		this.gateDebit = gateDebit;
	}
	public boolean isFastCredit() {
		return fastCredit;
	}
	public void setFastCredit(boolean fastCredit) {
		this.fastCredit = fastCredit;
	}
	public boolean isFastDebit() {
		return fastDebit;
	}
	public void setFastDebit(boolean fastDebit) {
		this.fastDebit = fastDebit;
	}
	
}
