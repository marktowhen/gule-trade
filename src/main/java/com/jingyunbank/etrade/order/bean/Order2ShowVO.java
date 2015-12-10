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
	private String mname;
	private String address;//收货地址
	private String mobile ;//收货人电话
	private String zipcode;
	private String UID;//下单人
	private Date addtime;//下单时间
	private String paytypeCode;//支付方式 id
	private String paytypeName;
	private String deliveryTypeCode;
	private String deliveryTypeName;
	private BigDecimal price;//订单总价
	private BigDecimal postage;
	private String statusCode;
	private String statusName;
	private String note;
	private List<OrderGoods2ShowVO> goods = new ArrayList<OrderGoods2ShowVO>();//商品图片路径
	
	public static class OrderGoods2ShowVO{
		private String GID;
		private String imgpath;
		private String gname;
		private int count;
		private BigDecimal price;
		
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
		public String getGname() {
			return gname;
		}
		public void setGname(String gname) {
			this.gname = gname;
		}
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
		public BigDecimal getPrice() {
			return price;
		}
		public void setPrice(BigDecimal price) {
			this.price = price;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
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
	public String getPaytypeCode() {
		return paytypeCode;
	}
	public void setPaytypeCode(String paytypeCode) {
		this.paytypeCode = paytypeCode;
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
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getDeliveryTypeCode() {
		return deliveryTypeCode;
	}
	public void setDeliveryTypeCode(String deliveryTypeCode) {
		this.deliveryTypeCode = deliveryTypeCode;
	}
	public String getDeliveryTypeName() {
		return deliveryTypeName;
	}
	public void setDeliveryTypeName(String deliveryTypeName) {
		this.deliveryTypeName = deliveryTypeName;
	}
}
