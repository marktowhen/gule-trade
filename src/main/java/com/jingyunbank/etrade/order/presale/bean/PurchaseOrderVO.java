package com.jingyunbank.etrade.order.presale.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 订单确认请求（购物请求）中的订单信息。每一个商家将会生成一个订单。
 */
public class PurchaseOrderVO {

	private String ID;
	private long orderno;
	@NotNull
	@Size(min=1, max=22)
	private String MID;
	@NotNull
	private String mname;
	@NotNull
	private String deliveryTypeCode;
	@NotNull
	private String deliveryTypeName;
	@Size(min=1, max=12, message="支付类型错误。")
	@NotNull
	private String paytypeCode;//支付方式 code
	@NotNull
	private String paytypeName;
	@NotNull//订单计算出的邮费（主要用于数据校验）
	@DecimalMin(value="0.00", inclusive=true)
	private BigDecimal postage = new BigDecimal(0);
	@NotNull//订单使用卡券前的总价（包含邮费）
	@DecimalMin(value="0.00", inclusive=false)
	private BigDecimal price = new BigDecimal(0);
	
	private Date addtime;
	
	private String note;
	@NotBlank
	private String type;
	
	private String extradata;//附加数据，可以是团购id，竞拍id等附加信息
	
	@Valid
	@NotNull
	@Size(min=1)
	private List<PurchaseGoodsVO> goods = new ArrayList<>();

	/**
	 * @return the paytypeCode
	 */
	public String getPaytypeCode() {
		return paytypeCode;
	}

	/**
	 * @param paytypeCode the paytypeCode to set
	 */
	public void setPaytypeCode(String paytypeCode) {
		this.paytypeCode = paytypeCode;
	}

	/**
	 * @return the paytypeName
	 */
	public String getPaytypeName() {
		return paytypeName;
	}

	/**
	 * @param paytypeName the paytypeName to set
	 */
	public void setPaytypeName(String paytypeName) {
		this.paytypeName = paytypeName;
	}

	public String getMID() {
		return MID;
	}

	public void setMID(String mID) {
		MID = mID;
	}

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public List<PurchaseGoodsVO> getGoods() {
		return goods;
	}

	public void setGoods(List<PurchaseGoodsVO> goods) {
		this.goods = goods;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public BigDecimal getPostage() {
		return postage;
	}

	public void setPostage(BigDecimal postage) {
		this.postage = postage;
	}

	public long getOrderno() {
		return orderno;
	}

	public void setOrderno(long orderno) {
		this.orderno = orderno;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getExtradata() {
		return extradata;
	}

	public void setExtradata(String extradata) {
		this.extradata = extradata;
	}
}
