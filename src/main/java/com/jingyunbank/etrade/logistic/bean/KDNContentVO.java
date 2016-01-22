package com.jingyunbank.etrade.logistic.bean;

import java.io.Serializable;

/**
 * 
* Title: 快递轨迹
* @author    duanxf
* @date      2016年1月21日
 */
public class KDNContentVO implements Serializable{

	
	private static final long serialVersionUID = 1L;

	private String accepttime; //时间
	private String acceptstation; //描述
	private String remark; //备注
	public String getAccepttime() {
		return accepttime;
	}
	public void setAccepttime(String accepttime) {
		this.accepttime = accepttime;
	}
	public String getAcceptstation() {
		return acceptstation;
	}
	public void setAcceptstation(String acceptstation) {
		this.acceptstation = acceptstation;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
	
}
