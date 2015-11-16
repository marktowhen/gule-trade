package com.jingyunbank.etrade.merchant.bean;

import java.io.Serializable;
/**
 * 发票类型
 * @author liug
 *
 */
public class DeliveryTypeVO implements Serializable {

	private static final long serialVersionUID = 1L;
	/**快递类型编码*/
	private String code;
	/**快递类型名称*/
	private String name;
	/**快递类型备注*/
	private String description;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
