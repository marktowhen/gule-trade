package com.jingyunbank.etrade.weixin.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 通过网页授权获取用户的信息
 * @author Administrator 
 * @date 2016年4月21日
	@todo TODO
 */
public class SNSUserInfo implements Serializable{

	private static final long serialVersionUID = 6177015489632279670L;

	private String openId;
	
	private String username;//用户的昵称
	
	private int sex;
	
	private String country;
	
	private String provice;
	
	private String city;
	
	private String headImgUrl;//用户的头像链接
	
	private List<String> privilegeList;//用户的特权信息

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

	/**
	 * @return the privilegeList
	 */
	public List<String> getPrivilegeList() {
		return privilegeList;
	}

	/**
	 * @param privilegeList the privilegeList to set
	 */
	public void setPrivilegeList(List<String> privilegeList) {
		this.privilegeList = privilegeList;
	}
	
	

}
