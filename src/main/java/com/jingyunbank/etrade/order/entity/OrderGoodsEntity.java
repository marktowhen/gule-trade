package com.jingyunbank.etrade.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单详情业务对象<br>
 * 改对象定义订单中包含的商品以及下单时的商品价格等信息
 *
 */
public class OrderGoodsEntity implements Serializable{

	private static final long serialVersionUID = -3377063429817839243L;
	
	private String ID;
	private String OID;//订单id
	private long orderno;
	private String GID;//商品id
	private String gname;
	private BigDecimal price;//订单生成时的商品价格
	private int count;
	private int points;//赠送积分
	private String statusCode;//状态id，用户支持订单中某个商品的退款
	private BigDecimal payout;//实际付款
	private BigDecimal reduce;//优惠减免价格
	private Date addtime;
	
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
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
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
	public BigDecimal getReduce() {
		return reduce;
	}
	public void setReduce(BigDecimal reduce) {
		this.reduce = reduce;
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
	
}
