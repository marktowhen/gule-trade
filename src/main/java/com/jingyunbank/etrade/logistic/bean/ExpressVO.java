package com.jingyunbank.etrade.logistic.bean;

import java.io.Serializable;

/**
 *  
* Title:  快递类型VO
* @author    duanxf
* @date      2016年1月21日
 */
public class ExpressVO implements Serializable{

	private static final long serialVersionUID = 1L;
	

	private String ID; //id
	private String code; //快递编码
	private String name; //快递名称
	private boolean status; //可用状态
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
}
