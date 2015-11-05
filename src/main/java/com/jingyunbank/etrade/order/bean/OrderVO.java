package com.jingyunbank.etrade.order.bean;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class OrderVO {

	private String ID;
	private String orderno;
	@NotNull
	private String receiver;//收货人
	@NotNull
	private String MID;
	@Size(min=22, max=22, message="收货地址错误。")
	@NotNull
	private String addressID;//收货地址->address id
	private String UID;//下单人
	private Date addtime;//下单时间
	@Size(min=22, max=22, message="支付类型错误。")
	@NotNull
	private String paytypeID;//支付方式 id
	@NotNull
	private String paytypeName;
	@NotNull
	@DecimalMin(value="0", inclusive=true, message="订单总价不能小于0元。")
	private BigDecimal price;//订单总价
	@NotNull
	@DecimalMin(value="0", inclusive=true, message="邮费不能小于0元。")
	private BigDecimal postage;
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getMID() {
		return MID;
	}
	public void setMID(String mID) {
		MID = mID;
	}
	public String getAddressID() {
		return addressID;
	}
	public void setAddressID(String addressID) {
		this.addressID = addressID;
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
	public String getPaytypeID() {
		return paytypeID;
	}
	public void setPaytypeID(String paytypeID) {
		this.paytypeID = paytypeID;
	}
	public String getPaytypeName() {
		return paytypeName;
	}
	public void setPaytypeName(String paytypeName) {
		this.paytypeName = paytypeName;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getPostage() {
		return postage;
	}
	public void setPostage(BigDecimal postage) {
		this.postage = postage;
	}
	@Override
	public String toString() {
		return "OrderVO [ID=" + ID + ", orderno=" + orderno + ", receiver="
				+ receiver + ", MID=" + MID + ", addressID=" + addressID
				+ ", UID=" + UID + ", addtime=" + addtime + ", paytypeID="
				+ paytypeID + ", paytypeName=" + paytypeName + ", price="
				+ price + ", postage=" + postage + "]";
	}
}
