package com.jingyunbank.etrade.vip.coupon.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 现金购物金（代金券）<br>
 * <strong>规则：</strong><br>
 * 结账时的商品金额可抵扣100%；<br>
 * 不能抵扣运费；<br>
 * 一张购物金只能在一张订单中使用、使用后不找零、退货后不返回；<br>
 * 购物金不可与抵用券和优惠券叠加使用。<br>
 * 
 */
public class CashCouponShowVO extends BaseCouponVO implements Serializable {

	private static final long serialVersionUID = 1413508875776533336L;
	private String ID;
	private String code;//充值码
	
	@NotNull(message="金额不能为空")
	@DecimalMin(value="0.01", message="价值不能低于1分")
	private BigDecimal value;//
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date addtime;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date start;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date end;
	
	private boolean used;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date usedtime;
	
	@NotNull(message="使用门槛不能为空")
	@DecimalMin(value="0.00")
	private BigDecimal threshhold;//使用门槛
	
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
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
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
