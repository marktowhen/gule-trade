package com.jingyunbank.etrade.vip.coupon.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UserCashCouponVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4086381597793946486L;
	private String ID;
	private String UID;
	private String couponID;
	
	@JsonFormat(pattern="yyyy-MM-dd" ,locale="zh", timezone="GMT+8")
	private Date consumeTime;//消费使用时间
	private boolean consumed;
	
	@JsonFormat(pattern="yyyy-MM-dd" ,locale="zh", timezone="GMT+8")
	private Date addTime;//激活时间
	private CashCouponVO cashCoupon;
	
	private boolean locked;//是否被锁定
	
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	public CashCouponVO getCashCoupon() {
		return cashCoupon;
	}
	public void setCashCoupon(CashCouponVO cashCoupon) {
		this.cashCoupon = cashCoupon;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	
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
	public Date getConsumeTime() {
		return consumeTime;
	}
	public void setConsumeTime(Date consumeTime) {
		this.consumeTime = consumeTime;
	}
	public boolean isConsumed() {
		return consumed;
	}
	public void setConsumed(boolean consumed) {
		this.consumed = consumed;
	}
	
}
