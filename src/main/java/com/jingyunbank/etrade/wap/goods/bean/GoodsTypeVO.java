package com.jingyunbank.etrade.wap.goods.bean;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
* Title: GoodsTypesVO	商品类型VO
* @author    duanxf
* @date      2016年3月31日
 */
public class GoodsTypeVO implements Serializable{

	private static final long serialVersionUID = 1L;
	private String ID;
	@NotNull(message="类别名称不能为空")
	private String name;
	private boolean status;
	private int adminsort;
	
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
	
	
	
	
	
}
