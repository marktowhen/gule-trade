package com.jingyunbank.etrade.statics.area.bean;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class ProvinceVO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5819650834620876659L;
	private int provinceID;
	@NotEmpty(message="名称不能为空")
	private String provinceName;
	@NotNull(message="国家不能为空")
	private int countryID;
	//偏远地区
	private boolean faraway;
	
	private List<CityVO> cityList ;
	
	public List<CityVO> getCityList() {
		return cityList;
	}
	public void setCityList(List<CityVO> cityList) {
		this.cityList = cityList;
	}
	public boolean isFaraway() {
		return faraway;
	}
	public void setFaraway(boolean faraway) {
		this.faraway = faraway;
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
