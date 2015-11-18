package com.jingyunbank.etrade.area.bean;

import java.io.Serializable;

public class CountryVO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4236983888643178415L;
	private int countryID;
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
