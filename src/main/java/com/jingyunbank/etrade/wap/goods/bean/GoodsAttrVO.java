package com.jingyunbank.etrade.wap.goods.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * 
 * Title: GoodsAttr 商品属性
 * 
 * @author duanxf
 * @date 2016年3月31日
 */
public class GoodsAttrVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String ID;
	@NotNull(message = "属性名称不能为空")
	private String name;
	private boolean status;

	private List<GoodsAttrValueVO> valueList = new ArrayList<GoodsAttrValueVO>();

	public List<GoodsAttrValueVO> getValueList() {
		return valueList;
	}

	public void setValueList(List<GoodsAttrValueVO> valueList) {
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
