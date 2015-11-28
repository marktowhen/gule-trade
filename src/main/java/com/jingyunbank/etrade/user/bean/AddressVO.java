package com.jingyunbank.etrade.user.bean;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.jingyunbank.core.lang.Patterns;
import com.jingyunbank.etrade.api.user.bo.BaseAddress;

/**
 * 收货地址 业务类
 *
 */
public class AddressVO extends BaseAddress {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9040578176416379288L;
	
	private String ID;
	private String UID;//用户id
	private String name;//标题
	@NotNull(message="国家不能为空")
	private int country;//国家
	private String countryName;//国家名字
	@NotNull(message="省不能为空")
	private int province;//省
	private String provinceName;//省
	@NotNull(message="城市不能为空")
	private int city;//市
	private String cityName;//市
	@NotEmpty(message="详细地址不能为空")
	@Size(max=80, message="详细地址错误")
	private String address;//详细地址
	private String zipcode;//邮编
	@NotEmpty(message="接收人不能为空")
	private String receiver;//接收人
	@NotEmpty(message="手机不能为空")
	@Pattern(regexp=Patterns.INTERNAL_MOBILE_PATTERN, message="手机格式错误")
	private String mobile;//手机
	private String telephone;//座机
	private boolean defaulted;//是否默认
	private boolean valid;//是否有效
	private String[]IDArray;//id数组
	
	public String[] getIDArray() {
		return IDArray;
	}
	public void setIDArray(String[] iDArray) {
		IDArray = iDArray;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getUID() {
		return UID;
	}
	public void setUID(String uID) {
		UID = uID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCountry() {
		return country;
	}
	public void setCountry(int country) {
		this.country = country;
	}
	public int getProvince() {
		return province;
	}
	public void setProvince(int province) {
		this.province = province;
	}
	public int getCity() {
		return city;
	}
	public void setCity(int city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public boolean isDefaulted() {
		return defaulted;
	}
	public void setDefaulted(boolean defaulted) {
		this.defaulted = defaulted;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

}
