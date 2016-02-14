package com.jingyunbank.etrade.statics.area.entity;

import java.io.Serializable;

public class ProvinceEntity implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6524287916350316128L;
	private int provinceID;
	private String provinceName;
	private int countryID;
	
	private long offset;
	private long size;
	
	//偏远地区
	private boolean faraway;
	
	public boolean isFaraway() {
		return faraway;
	}
	public void setFaraway(boolean faraway) {
		this.faraway = faraway;
	}
	
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
