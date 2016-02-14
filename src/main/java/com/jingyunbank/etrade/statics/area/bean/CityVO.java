package com.jingyunbank.etrade.statics.area.bean;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class CityVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6372741420042757340L;
	
	private int cityID;
	@NotEmpty(message="名称不能为空")
	private String cityName;
	private String zipCode;
	@NotNull(message="省份不能为空")
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
