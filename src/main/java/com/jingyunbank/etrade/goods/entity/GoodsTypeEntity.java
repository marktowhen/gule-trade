package com.jingyunbank.etrade.goods.entity;

import java.io.Serializable;

/**
 * Title: 商品类型
 * @author duanxf
 * @date 2015年11月3日
 */
public class GoodsTypeEntity implements Serializable {

	private static final long serialVersionUID = -1927368923441904620L;
	/**主键*/
	private String ID;
	/**类型名称*/
	private String name;
	
	private boolean status;
	private int adminsort;
	
	
	
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
