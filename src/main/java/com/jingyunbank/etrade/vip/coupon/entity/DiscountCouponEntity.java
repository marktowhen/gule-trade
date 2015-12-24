package com.jingyunbank.etrade.vip.coupon.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 折扣抵用券<br>
 * <strong>规则：</strong><br>
 * 结账时的商品金额可抵扣20%；<br>
 * 不能抵扣运费（即运费部分另计，只对商品部分进行打折）；<br>
 * 一张抵用券只能在一张订单中使用，且不找零；<br>
 * 抵用券不可与优惠券购物金叠加使用。
 */
public class DiscountCouponEntity extends BaseCouponEntity implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 537309161697855484L;
	private String ID;
	private String code;//充值码
	private BigDecimal discount;//折扣
	private Date addtime;
	private Date start;
	private Date end;
	private boolean used;//是否充值到某用户账户中
	private Date usedtime;//充值时间
	private BigDecimal threshhold;//使用门槛
	
	private BigDecimal value;//面值 抵用的最高值
	
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public boolean isUsed() {
		return used;
	}
	public void setUsed(boolean used) {
		this.used = used;
	}
	public BigDecimal getThreshhold() {
		return threshhold;
	}
	public void setThreshhold(BigDecimal threshhold) {
		this.threshhold = threshhold;
	}
	public Date getUsedtime() {
		return usedtime;
	}
	public void setUsedtime(Date usedtime) {
		this.usedtime = usedtime;
	}
	
}
