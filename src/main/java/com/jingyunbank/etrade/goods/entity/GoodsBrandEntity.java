package com.jingyunbank.etrade.goods.entity;

import java.io.Serializable;

/**
 * Title: 品牌表实体
 * 
 * @author duanxf
 * @date 2015年11月3日
 */
public class GoodsBrandEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 品牌id */
	private String ID;
	/** 商家id */
	private String MID;
	/** 品牌名称 */
	private String name;
	/** 品牌描述 */
	private String desc;
	/**管理员排序*/
	private int admin_sort;
	
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

	public int getAdmin_sort() {
		return admin_sort;
	}

	public void setAdmin_sort(int admin_sort) {
		this.admin_sort = admin_sort;
	}

}
