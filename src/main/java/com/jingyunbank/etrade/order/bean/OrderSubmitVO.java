package com.jingyunbank.etrade.order.bean;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 订单表单提交的VO，包含基本的订单信息，以及多种商品信息
 */
public class OrderSubmitVO {

	@NotNull
	private String receiver;//收货人
	@Size(min=22, max=22, message="收货地址错误。")
	@NotNull
	private String addressID;//收货地址->address id
	private String UID;//下单人
	@Size(min=22, max=22, message="支付类型错误。")
	@NotNull
	private String paytypeID;//支付方式 id
	@NotNull
	private String paytypeName;
	@NotNull
	private List<OrderGoodsVO> goods = new ArrayList<>();
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
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
	public List<OrderGoodsVO> getGoods() {
		return goods;
	}
	public void setGoods(List<OrderGoodsVO> goods) {
		this.goods = goods;
	}
	
}
