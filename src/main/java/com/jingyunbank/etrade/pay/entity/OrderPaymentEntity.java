package com.jingyunbank.etrade.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 付款业务对象（领域对象）<br>
 * 该类描述了某个订单的支付信息，<br>
 * 包括支付金额，付款人，订单号，交易号，付款状态，付款方式等<br>
 *
 */
public class OrderPaymentEntity{

	private String ID;
	private String OID;
	private String UID;
	private String MID;
	private String mname;
	private long transno;
	private long extransno;
	private BigDecimal money;
	private String note;
	private boolean done;
	private String platformCode;
	private String platformName;
	private Date addtime;
	private Date paidtime;
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getOID() {
		return OID;
	}
	public void setOID(String oID) {
		OID = oID;
	}
	public String getUID() {
		return UID;
	}
	public void setUID(String uID) {
		UID = uID;
	}
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public long getTransno() {
		return transno;
	}
	public void setTransno(long transno) {
		this.transno = transno;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getMID() {
		return MID;
	}
	public void setMID(String mID) {
		MID = mID;
	}
	public boolean isDone() {
		return done;
	}
	public void setDone(boolean done) {
		this.done = done;
	}
	public String getPlatformCode() {
		return platformCode;
	}
	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}
	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
	public Date getPaidtime() {
		return paidtime;
	}
	public void setPaidtime(Date paidtime) {
		this.paidtime = paidtime;
	}
	public long getExtransno() {
		return extransno;
	}
	public void setExtransno(long extransno) {
		this.extransno = extransno;
	}
}
