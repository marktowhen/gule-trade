package com.jingyunbank.etrade.area.entity;

import java.io.Serializable;

public class CountryEntity implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3222922595853288688L;
	private int countryID;
	private String countryName;
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
