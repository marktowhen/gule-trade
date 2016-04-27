package com.jingyunbank.etrade.weixin.bean;

import java.io.Serializable;
/**
 * 凭证实体类
 * @author Administrator 
 * @date 2016年4月21日
	@todo TODO
 */
public class Token implements Serializable{
	
	private static final long serialVersionUID = -45844022887477803L;

	private String accessToken;//接口访问凭证
	
	private String expiresIn;//凭证有效期

	/**
	 * @return the accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * @param accessToken the accessToken to set
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * @return the expiresIn
	 */
	public String getExpiresIn() {
		return expiresIn;
	}

	/**
	 * @param expiresIn the expiresIn to set
	 */
	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}
	
	
}
