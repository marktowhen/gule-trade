package com.jingyunbank.etrade.merchant.entity;

import java.io.Serializable;
/**
 * 商家发票类型表
 * @author liug
 *
 */
public class MerchantInvoiceEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**主键*/
	private String ID;
	/**商家ID*/
	private String MID;
	/**发票类型编码*/
	private String code;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
