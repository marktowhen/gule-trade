package com.jingyunbank.etrade.weixin.bean;

import java.io.Serializable;

/**
 * 网页授权信息类
 * @author Administrator 
 * @date 2016年4月21日
	@todo TODO
 */
public class WeixinOauth2Token implements Serializable{

	private static final long serialVersionUID = 7908540199113293072L;

	private String accessToken;//网页授权接口调用凭证
	
	private int expiresIn;//凭证有效时长
	
	private String refreshToken;//用于刷新凭证
	
	private String openId;//用户凭证
	
	private String scope;//凭证授权作用域

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
	public int getExpiresIn() {
		return expiresIn;
	}

	/**
	 * @param expiresIn the expiresIn to set
	 */
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	/**
	 * @return the refreshToken
	 */
	public String getRefreshToken() {
		return refreshToken;
	}

	/**
	 * @param refreshToken the refreshToken to set
	 */
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

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
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}
	
	
}
