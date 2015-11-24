package com.jingyunbank.etrade.area.bean;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

public class CountryVO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4236983888643178415L;
	private int countryID;
	@NotEmpty(message="名称不能为空")
	private String countryName;
	
	public int getCountryID() {
		return countryID;
	}
	public void setCountryID(int countryID) {
		this.countryID = countryID;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	

}
