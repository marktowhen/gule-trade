package com.jingyunbank.etrade.wap.goods.entity;

import java.io.Serializable;

/**
 * 
 * Title: GoodsAttrValueEntity 商品属性值
 * 
 * @author duanxf
 * @date 2016年4月1日
 */
public class GoodsAttrValueEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String ID;
	private String GID;
	private String attrId;
	private String attrName;
	private String value;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getGID() {
		return GID;
	}

	public void setGID(String gID) {
		GID = gID;
	}

	public String getAttrId() {
		return attrId;
	}

	public void setAttrId(String attrId) {
		this.attrId = attrId;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
