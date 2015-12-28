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
	private int admin_sort;
	
	
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public int getAdmin_sort() {
		return admin_sort;
	}
	public void setAdmin_sort(int admin_sort) {
		this.admin_sort = admin_sort;
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
