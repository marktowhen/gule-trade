package com.jingyunbank.etrade.order.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order2ShowVO {

	private String ID;
	private String orderno;
	private String receiver;//收货人
	private String MID;
	private String addressID;//收货地址->address id
	private String UID;//下单人
	private Date addtime;//下单时间
	private String paytypeID;//支付方式 id
	private String paytypeName;
	private BigDecimal price;//订单总价
	private BigDecimal postage;
	private List<OrderGoods2ShowVO> goods = new ArrayList<OrderGoods2ShowVO>();//商品图片路径
	
	public static class OrderGoods2ShowVO{
		private String GID;
		private String imgpath;
		public String getGID() {
			return GID;
		}
		public void setGID(String gID) {
			GID = gID;
		}
		public String getImgpath() {
			return imgpath;
		}
		public void setImgpath(String imgpath) {
			this.imgpath = imgpath;
		}
	}
	
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
	public List<OrderGoods2ShowVO> getGoods() {
		return goods;
	}
	public void setGoods(List<OrderGoods2ShowVO> goods) {
		this.goods = goods;
	}
}