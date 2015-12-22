package com.jingyunbank.etrade.order.postsale.bean;

import java.math.BigDecimal;
import java.util.Date;

public class Refund2ShowVO {
	
	private String ID;
	private String OGID;//订单商品id
	private long refundno;
	private String OID;//所退商品订单号
	private String MID;//商家
	private String UID;
	private BigDecimal omoney;//订单价格
	private BigDecimal money;//退款金额申请
	private String reason;//退款退货原因
	private String description;//说明
	private Date addtime;//申请时间
	private String statusCode;
	private String statusName;//RefundStatusDesc
	private boolean returnGoods;//是否退货
	private boolean received;
	
	private String GID;//商品id
	private String gname;//商品名
	private String gpath;//
	private String orderno;
	
	
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
	public String getGID() {
		return GID;
	}
	public void setGID(String gID) {
		GID = gID;
	}
	public String getGname() {
		return gname;
	}
	public void setGname(String gname) {
		this.gname = gname;
	}
	public long getRefundno() {
		return refundno;
	}
	public void setRefundno(long refundno) {
		this.refundno = refundno;
	}
	public String getOID() {
		return OID;
	}
	public void setOID(String oID) {
		OID = oID;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
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
	public String getGpath() {
		return gpath;
	}
	public void setGpath(String gpath) {
		this.gpath = gpath;
	}
	public boolean isReceived() {
		return received;
	}
	public void setReceived(boolean received) {
		this.received = received;
	}
}
