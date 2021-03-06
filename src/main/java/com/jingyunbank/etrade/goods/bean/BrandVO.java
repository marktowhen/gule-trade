package com.jingyunbank.etrade.goods.bean;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * 
 * Title: 品牌录入VO
 * 
 * @author duanxf
 * @date 2015年12月15日
 */
public class BrandVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ID;
	@NotNull(message="所属商家不能为空")
	private String MID;
	@NotNull(message="品牌名称不能为空")
	private String name;
	private String desc;
	private int adminsort;
	private boolean status;
	
	

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getMID() {
		return MID;
	}

	public void setMID(String mID) {
		MID = mID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getAdminsort() {
		return adminsort;
	}

	public void setAdminsort(int adminsort) {
		this.adminsort = adminsort;
	}



}
