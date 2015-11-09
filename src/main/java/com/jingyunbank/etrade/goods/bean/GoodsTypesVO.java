package com.jingyunbank.etrade.goods.bean;

import java.io.Serializable;
/**
 *  
* Title: 商品类型VO
* @author    duanxf
* @date      2015年11月9日
 */
public class GoodsTypesVO implements Serializable{

	private static final long serialVersionUID = 1L;
	private String TID; // 所属属性id
	private String typeName; // 所属类型
	public String getTID() {
		return TID;
	}
	public void setTID(String tID) {
		TID = tID;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	
}
