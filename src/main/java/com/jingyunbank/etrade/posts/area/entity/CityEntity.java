package com.jingyunbank.etrade.posts.area.entity;

import java.io.Serializable;

public class CityEntity implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5237758961798923189L;
	private int cityID;
	private String cityName;
	private String zipCode;
	private int provinceID;
	private long offset;
	private long size;
	
	public long getOffset() {
		return offset;
	}
	public void setOffset(long offset) {
		this.offset = offset;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
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
