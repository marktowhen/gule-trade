package com.jingyunbank.etrade.order.postsale.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RefundRequestVO {
	
	private String ID;
	@NotNull
	private String OGID;//订单商品id
	@NotNull
	private String OID;//所退商品订单号
	@NotNull
	private String MID;//商家
	private String UID;
	@NotNull
	private BigDecimal omoney;//订单价格
	@NotNull
	private BigDecimal money;//退款金额申请
	@NotNull
	private String reason;//退款退货原因	
	private String description;//说明
	private Date addtime;//申请时间
	private String statusCode;
	private String statusName;//RefundStatusDesc
	private long refundno;
	private boolean received;
	@NotNull
	private boolean returnGoods=false;//是否退货
	
	@Size(max=5)
	private List<String> certificates = new ArrayList<String>();

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getOGID() {
		return OGID;
	}

	public void setOGID(String oGID) {
		OGID = oGID;
	}

	public String getOID() {
		return OID;
	}

	public void setOID(String oID) {
		OID = oID;
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

	public BigDecimal getOmoney() {
		return omoney;
	}

	public void setOmoney(BigDecimal omoney) {
		this.omoney = omoney;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public boolean isReturnGoods() {
		return returnGoods;
	}

	public void setReturnGoods(boolean returnGoods) {
		this.returnGoods = returnGoods;
	}

	public List<String> getCertificates() {
		return certificates;
	}

	public void setCertificates(List<String> certificates) {
		this.certificates = certificates;
	}
	public long getRefundno() {
		return refundno;
	}
	public void setRefundno(long refundno) {
		this.refundno = refundno;
	}

	public boolean isReceived() {
		return received;
	}

	public void setReceived(boolean received) {
		this.received = received;
	}
}
