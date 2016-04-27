package com.jingyunbank.etrade.order.presale.bean;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class OrderGoodsVO {
	private String ID;
	private String OID;//订单id
	private String UID;
	private long orderno;
	private String GID;//商品id
	private String SKUID;
	private String gname;
	private BigDecimal pprice;//促销价
	private BigDecimal price;//订单生成时的商品价格
	private int count;
	private String statusCode;//状态id，用户支持订单中某个商品的退款
	private String statusName;
	private BigDecimal payout;//实际付款
	private BigDecimal couponReduce;//优惠减免价格
	@JsonFormat(pattern="yyyy-MM-dd HH:mm" ,locale="zh", timezone="GMT+8")
	private Date addtime;
	//to show
	private String MID;
	private String mname;
	private String imgpath;
	private BigDecimal postage;
	
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
	public String getGID() {
		return GID;
	}
	public void setGID(String gID) {
		GID = gID;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public long getOrderno() {
		return orderno;
	}
	public void setOrderno(long orderno) {
		this.orderno = orderno;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getPayout() {
		return payout;
	}
	public void setPayout(BigDecimal payout) {
		this.payout = payout;
	}
	public String getGname() {
		return gname;
	}
	public void setGname(String gname) {
		this.gname = gname;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	public BigDecimal getPprice() {
		return pprice;
	}
	public void setPprice(BigDecimal pprice) {
		this.pprice = pprice;
	}
	public BigDecimal getCouponReduce() {
		return couponReduce;
	}
	public void setCouponReduce(BigDecimal couponReduce) {
		this.couponReduce = couponReduce;
	}
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public String getImgpath() {
		return imgpath;
	}
	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}
	public String getMID() {
		return MID;
	}
	public void setMID(String mID) {
		MID = mID;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public BigDecimal getPostage() {
		return postage;
	}
	public void setPostage(BigDecimal postage) {
		this.postage = postage;
	}
	public String getSKUID() {
		return SKUID;
	}
	public void setSKUID(String sKUID) {
		SKUID = sKUID;
	}
}
