package com.jingyunbank.etrade.goods.bean;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
/**
 *  
* Title: 商品类型VO
* @author    duanxf
* @date      2015年11月9日
 */
public class GoodsTypesVO implements Serializable{

	private static final long serialVersionUID = 1L;
	/**主键*/
	private String ID;
	/**类型名称*/
	@NotNull(message="类别名称不能为空")
	private String name;
	
	private String TID;
	private String typeName;
	
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
