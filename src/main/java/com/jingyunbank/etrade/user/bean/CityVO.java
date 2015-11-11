package com.jingyunbank.etrade.user.bean;

import java.io.Serializable;

public class CityVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -537625202278128102L;
	private int cityID;
	private String cityName;
	private String zipCode;
	private int provinceID;
	public int getCityID() {
		return cityID;
	}
	public void setCityID(int cityID) {
		this.cityID = cityID;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public int getProvinceID() {
		return provinceID;
	}
	public void setProvinceID(int provinceID) {
		this.provinceID = provinceID;
	}
	
	

}
