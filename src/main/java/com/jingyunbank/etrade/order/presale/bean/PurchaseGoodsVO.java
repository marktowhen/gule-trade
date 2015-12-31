package com.jingyunbank.etrade.order.presale.bean;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 订单确认中的商品信息，其中每个purchaseordervo对应多个purchasegoodsvo
 */
public class PurchaseGoodsVO {

	private String MID;
	@NotNull
	@Size(min=22, max=22)
	private String GID;
	private String gname;
	private BigDecimal pprice;//商品下单时的促销价
	@NotNull
	@DecimalMin(value="0.00", inclusive=false)
	private BigDecimal price;//商品下单时的单价
	@NotNull
	@DecimalMin(value="0.00", inclusive=true)
	private BigDecimal postage = new BigDecimal(0);//商品邮费,用作数据校验不做存储
	@NotNull
	@Min(value=1)
	private int count;
	
	public String getMID() {
		return MID;
	}
	public void setMID(String mID) {
		MID = mID;
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
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public BigDecimal getPprice() {
		return pprice;
	}
	public void setPprice(BigDecimal pprice) {
		this.pprice = pprice;
	}
	public BigDecimal getPostage() {
		return postage;
	}
	public void setPostage(BigDecimal postage) {
		this.postage = postage;
	}
}
