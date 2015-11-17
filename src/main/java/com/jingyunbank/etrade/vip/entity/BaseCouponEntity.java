package com.jingyunbank.etrade.vip.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 所有优惠方式的父类。
 */
public class BaseCouponEntity implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -4209769546160818768L;
	private boolean del;//是否被删除
	private String remark;//备注 
	private long offset;
	private long size;
	
	private Date addtimeStart;
	private Date addtiemEnd;
	private BigDecimal threshholdLow;//查询时添加条件 and threshhold>=threshholdLow
	private BigDecimal threshholdHigh;//查询时添加条件 and threshhold<=threshholdHigh
	
	private boolean validTime;//当前时间是否在有效期内
	private boolean needUsed;//查询时是否需要加上used这个字段的条件
	private boolean needDelete;//查询时是否需要加上Delete这个字段的条件
	
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
	public boolean isValidTime() {
		return validTime;
	}
	public void setValidTime(boolean validTime) {
		this.validTime = validTime;
	}
	public boolean isNeedUsed() {
		return needUsed;
	}
	public void setNeedUsed(boolean needUsed) {
		this.needUsed = needUsed;
	}
	public boolean isNeedDelete() {
		return needDelete;
	}
	public void setNeedDelete(boolean needDelete) {
		this.needDelete = needDelete;
	}
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
	public long getOffset() {
		return offset;
	}
	public void setOffset(long offset) {
		this.offset = offset;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
}
