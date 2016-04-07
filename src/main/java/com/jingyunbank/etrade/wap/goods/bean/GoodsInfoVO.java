package com.jingyunbank.etrade.wap.goods.bean;

import java.io.Serializable;

/**
 * 
 * Title: GoodsInfoVO 商品参数vo
 * 
 * @author duanxf
 * @date 2016年4月7日
 */
public class GoodsInfoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String ID;
	private String GID;
	private String key;
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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
