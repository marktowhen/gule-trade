package com.jingyunbank.etrade.order.bean;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CartVO {

	private String ID;
	@NotNull
	private String MID;
	private String UID;//下单人
	private Date addtime;//下单时间
	@NotNull
	@DecimalMin(value="0", inclusive=true, message="订单总价不能小于0元。")
	private BigDecimal price;//订单总价
	@NotNull
	@Min(value=0, message="邮费不能小于0元。")
	private int count;
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getMID() {
		return MID;
	}
	public void setMID(String mID) {
		MID = mID;
	}
	public String getUID() {
		return UID;
	}
	public void setUID(String uID) {
		UID = uID;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
