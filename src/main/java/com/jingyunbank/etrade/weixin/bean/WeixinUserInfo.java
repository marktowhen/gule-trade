package com.jingyunbank.etrade.weixin.bean;

import java.io.Serializable;

/**
 * 微信用户的基本信息
 * @author Administrator 
 * @date 2016年4月21日
	@todo TODO
 */
public class WeixinUserInfo implements Serializable{

	private static final long serialVersionUID = 3606518825269995999L;

	private String openId;
	
	private int subscribe;//关注状态(1:关注;0:为关注)为关注时取不到任何数据
	
	private String username;//
	
	private int sex;
	
	private String country;
	
	private String provice;
	
	private String city;
	
	private String language;
	
	private String headImgUrl;//用户的头像

	/**
	 * @return the openId
	 */
	public String getOpenId() {
		return openId;
	}

	/**
	 * @param openId the openId to set
	 */
	public void setOpenId(String openId) {
		this.openId = openId;
	}

	/**
	 * @return the subscribe
	 */
	public int getSubscribe() {
		return subscribe;
	}

	/**
	 * @param subscribe the subscribe to set
	 */
	public void setSubscribe(int subscribe) {
		this.subscribe = subscribe;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the sex
	 */
	public int getSex() {
		return sex;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(int sex) {
		this.sex = sex;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the provice
	 */
	public String getProvice() {
		return provice;
	}

	/**
	 * @param provice the provice to set
	 */
	public void setProvice(String provice) {
		this.provice = provice;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the headImgUrl
	 */
	public String getHeadImgUrl() {
		return headImgUrl;
	}

	/**
	 * @param headImgUrl the headImgUrl to set
	 */
	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	
	
}
