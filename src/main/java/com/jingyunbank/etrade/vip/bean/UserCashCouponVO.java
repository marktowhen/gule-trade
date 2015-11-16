package com.jingyunbank.etrade.vip.bean;

import java.io.Serializable;
import java.util.Date;

public class UserCashCouponVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4086381597793946486L;
	private String ID;
	private String UID;
	private String couponID;
	private String OID;
	private Date rechargeTime;//充值时间
	private Date consumeTime;//消费使用时间
	private boolean recharged;
	private boolean consumed;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getUID() {
		return UID;
	}
	public void setUID(String uID) {
		UID = uID;
	}
	public String getCouponID() {
		return couponID;
	}
	public void setCouponID(String couponID) {
		this.couponID = couponID;
	}
	public String getOID() {
		return OID;
	}
	public void setOID(String oID) {
		OID = oID;
	}
	public Date getRechargeTime() {
		return rechargeTime;
	}
	public void setRechargeTime(Date rechargeTime) {
		this.rechargeTime = rechargeTime;
	}
	public Date getConsumeTime() {
		return consumeTime;
	}
	public void setConsumeTime(Date consumeTime) {
		this.consumeTime = consumeTime;
	}
	public boolean isRecharged() {
		return recharged;
	}
	public void setRecharged(boolean recharged) {
		this.recharged = recharged;
	}
	public boolean isConsumed() {
		return consumed;
	}
	public void setConsumed(boolean consumed) {
		this.consumed = consumed;
	}
	
}
