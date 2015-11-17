package com.jingyunbank.etrade.vip.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserCashCouponEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2359794331170232158L;
	private String ID;
	private String UID;
	private String couponID;
	private String OID;
	private Date consumeTime;//消费使用时间
	private boolean consumed;
	private long offset;
	private long size;
	
	private Date addTime;//激活时间
	private CashCouponEntity cashCoupon;
	
	public CashCouponEntity getCashCoupon() {
		return cashCoupon;
	}
	public void setCashCoupon(CashCouponEntity cashCoupon) {
		this.cashCoupon = cashCoupon;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public long getOffset() {
		return offset;
	}
	public void setOffset(long offset) {
		this.offset = offset;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
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
	public String getOID() {
		return OID;
	}
	public void setOID(String oID) {
		OID = oID;
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
