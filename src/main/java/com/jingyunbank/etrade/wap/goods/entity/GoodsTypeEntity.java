package com.jingyunbank.etrade.wap.goods.entity;

import java.io.Serializable;

/**
 * 
* Title: GoodsTypeEntity 类型
* @author    duanxf
* @date      2016年3月31日
 */
public class GoodsTypeEntity implements Serializable {

	private static final long serialVersionUID = -1927368923441904620L;
	private String ID;	//id
	private String name; //名称
	private boolean status; //状态 
	private int adminsort;  //排序
	
	
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public int getAdminsort() {
		return adminsort;
	}
	public void setAdminsort(int adminsort) {
		this.adminsort = adminsort;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
