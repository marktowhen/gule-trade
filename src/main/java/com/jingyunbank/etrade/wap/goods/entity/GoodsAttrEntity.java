package com.jingyunbank.etrade.wap.goods.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
* Title: GoodsAttr 商品属性
* @author    duanxf
* @date      2016年3月31日
 */
public class GoodsAttrEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String ID;
	private String name;
	private boolean status;
	
	private List<GoodsAttrValueEntity> valueList = new ArrayList<GoodsAttrValueEntity>();
	
	
	
	public List<GoodsAttrValueEntity> getValueList() {
		return valueList;
	}
	public void setValueList(List<GoodsAttrValueEntity> valueList) {
		this.valueList = valueList;
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
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	

}
