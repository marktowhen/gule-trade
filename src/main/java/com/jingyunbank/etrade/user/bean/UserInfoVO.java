package com.jingyunbank.etrade.user.bean;

import java.util.Date;

/**
 * @author Administrator 
 * @date 2015年11月6日
	@todo TODO
 */
public class UserInfoVO {
	private String UID;
	private Date birthday;//生日
	private boolean marriage;//婚否
	private Gender gender;//性别
	private int country;//国家
	private int province;//省份
	private int city;//城市
	private String address;//街道
	private int education;//教育程度
	private int job;//职业
	private int income;//收入
	private String avatar;//头像路径
	private Date regtime;//注册时间
	private String regip;//注册ip
	private Device regdevice;//注册设备
	private String picture;//头像文件名
	
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getUID() {
		return UID;
	}
	public void setUID(String uID) {
		UID = uID;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public boolean isMarriage() {
		return marriage;
	}
	public void setMarriage(boolean marriage) {
		this.marriage = marriage;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
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
	public int getEducation() {
		return education;
	}
	public void setEducation(int education) {
		this.education = education;
	}
	public int getJob() {
		return job;
	}
	public void setJob(int job) {
		this.job = job;
	}
	public int getIncome() {
		return income;
	}
	public void setIncome(int income) {
		this.income = income;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public Date getRegtime() {
		return regtime;
	}
	public void setRegtime(Date regtime) {
		this.regtime = regtime;
	}
	public String getRegip() {
		return regip;
	}
	public void setRegip(String regip) {
		this.regip = regip;
	}
	public Device getRegdevice() {
		return regdevice;
	}
	public void setRegdevice(Device regdevice) {
		this.regdevice = regdevice;
	}
	public enum Gender{
		MAIL, FEMALE, NONE
	}
	public enum Device{
		PC, WAP, ANDROID, IPHONE, WINPHONE
	}
}
