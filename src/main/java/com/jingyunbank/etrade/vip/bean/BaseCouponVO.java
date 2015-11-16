package com.jingyunbank.etrade.vip.bean;

import java.io.Serializable;

/**
 * 所有优惠方式的父类。
 */
public class BaseCouponVO implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 113271790301425523L;
	private boolean delete;//是否被删除
	private String remark;//备注 

	public boolean isDelete() {
		return delete;
	}
	public void setDelete(boolean delete) {
		this.delete = delete;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
