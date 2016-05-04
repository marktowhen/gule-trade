package com.jingyunbank.etrade.weixin.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * 凭证实体类
 * @author Administrator 
 * @date 2016年4月21日
	@todo TODO
 */
public class AccessToken implements Serializable{
	
	private static final long serialVersionUID = -45844022887477803L;
	private String access_token;//接口访问凭证
	private String expires_in;//凭证有效期
	/**
	 * @return the access_token
	 */
	public String getAccess_token() {
		return access_token;
	}
	/**
	 * @param access_token the access_token to set
	 */
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	/**
	 * @return the expires_in
	 */
	public String getExpires_in() {
		return expires_in;
	}
	/**
	 * @param expires_in the expires_in to set
	 */
	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	
}
