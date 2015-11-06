package com.jingyunbank.etrade.user.bean;

import java.io.Serializable;

/**
 * 国家
 * @author qxs
 *
 */
public class CountryVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8288500900094705271L;
	private int countryId;//编号
	private String countryName;//名称
	public int getCountryId() {
		return countryId;
	}
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
}
