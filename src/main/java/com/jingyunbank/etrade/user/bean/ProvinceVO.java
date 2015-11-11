package com.jingyunbank.etrade.user.bean;

import java.io.Serializable;

public class ProvinceVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1739908186582939341L;
	
	private int provinceID;
	private String provinceName;
	private int countryID;
	public int getProvinceID() {
		return provinceID;
	}
	public void setProvinceID(int provinceID) {
		this.provinceID = provinceID;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public int getCountryID() {
		return countryID;
	}
	public void setCountryID(int countryID) {
		this.countryID = countryID;
	}
	
	

}
