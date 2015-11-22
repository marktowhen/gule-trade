package com.jingyunbank.etrade.pay.bean;

import java.io.Serializable;


/**
 * 
 * 支付方式领域类
 *	包括在线支付，线下支付，货到付款
 */
public class PayTypeVO implements Serializable{

	private static final long serialVersionUID = 5372268679510354301L;
	
	private String ID;
	private String code;
	private String name;
	private String description;
	private boolean disable;
	private boolean online;
	private String url;
	
	public String getID() {
		return ID;
	}
	public void setID(String id) {
		this.ID = id;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isDisable() {
		return disable;
	}
	public void setDisable(boolean disable) {
		this.disable = disable;
	}
	public boolean isOnline() {
		return online;
	}
	public void setOnline(boolean online) {
		this.online = online;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
