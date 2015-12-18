package com.jingyunbank.etrade.cart.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
/**
 * @see CartController.list()
 */
public class OrdersInCartVO implements Serializable{
	
	private static final long serialVersionUID = -3657800214838173712L;
	private String ID;
	private long orderno;
	@NotNull
	@Size(min=22, max=22)
	private String MID;
	@NotNull
	private String mname;
	@NotNull
	@DecimalMin(value="0.00", inclusive=true)
	private BigDecimal postage = new BigDecimal(0);
	@NotNull
	@DecimalMin(value="0.00", inclusive=false)
	private BigDecimal price;
	private Date addtime;
	
	private String note;
	@NotNull
	@Size(min=1)
	private List<GoodsInCartVO> goods = new ArrayList<>();
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public long getOrderno() {
		return orderno;
	}
	public void setOrderno(long orderno) {
		this.orderno = orderno;
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
	public BigDecimal getPostage() {
		return postage;
	}
	public void setPostage(BigDecimal postage) {
		this.postage = postage;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public List<GoodsInCartVO> getGoods() {
		return goods;
	}
	public void setGoods(List<GoodsInCartVO> goods) {
		this.goods = goods;
	}
	
}
