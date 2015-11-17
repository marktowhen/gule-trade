package com.jingyunbank.etrade.vip.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 所有优惠方式的父类。
 */
public class BaseCouponVO implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 113271790301425523L;
	private boolean del;//是否被删除
	private String remark;//备注 

	private Date addtimeStart;
	private Date addtiemEnd;
	private BigDecimal threshholdLow;//查询时添加条件 and threshhold>=threshholdLow
	private BigDecimal threshholdHigh;//查询时添加条件 and threshhold<=threshholdHigh
	
	public Date getAddtimeStart() {
		return addtimeStart;
	}
	public void setAddtimeStart(Date addtimeStart) {
		this.addtimeStart = addtimeStart;
	}
	public Date getAddtiemEnd() {
		return addtiemEnd;
	}
	public void setAddtiemEnd(Date addtiemEnd) {
		this.addtiemEnd = addtiemEnd;
	}
	public BigDecimal getThreshholdLow() {
		return threshholdLow;
	}
	public void setThreshholdLow(BigDecimal threshholdLow) {
		this.threshholdLow = threshholdLow;
	}
	public BigDecimal getThreshholdHigh() {
		return threshholdHigh;
	}
	public void setThreshholdHigh(BigDecimal threshholdHigh) {
		this.threshholdHigh = threshholdHigh;
	}
	
	public boolean isDel() {
		return del;
	}
	public void setDel(boolean del) {
		this.del = del;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
