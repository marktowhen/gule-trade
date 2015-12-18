package com.jingyunbank.etrade.information.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class InformationDetailsVO {
	
	private String ID;
	private String SID;
	private String name;//主要的信息
	private String title;//标题
	private String content;//内容
	private String picture;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm" ,locale="zh", timezone="GMT+8")
	private Date addtime;//添加时间
	private String status;
	private int orders;//按order排序
	
	
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getSID() {
		return SID;
	}
	public void setSID(String sID) {
		SID = sID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getOrders() {
		return orders;
	}
	public void setOrders(int orders) {
		this.orders = orders;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
}
