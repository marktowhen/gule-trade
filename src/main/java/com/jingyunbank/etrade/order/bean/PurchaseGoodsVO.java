package com.jingyunbank.etrade.order.bean;

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
	private BigDecimal pprice;//促销价
	@NotNull
	@DecimalMin(value="0.00", inclusive=false)
	private BigDecimal price;
	@NotNull
	@Min(value=1)
	private int count;
	private BigDecimal total;
	
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
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public BigDecimal getPprice() {
		return pprice;
	}
	public void setPprice(BigDecimal pprice) {
		this.pprice = pprice;
	}
}
