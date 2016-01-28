package com.jingyunbank.etrade.user.bean;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ThirdLoginBindVO {

	@NotNull
	@Size(min=4)
	private String loginKey;
	@NotNull
	private String loginPassword;
	@NotNull
	private String thirdLoginKey;
	
	private String accessToken;

	public String getLoginKey() {
		return loginKey;
	}

	public void setLoginKey(String loginKey) {
		this.loginKey = loginKey;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getThirdLoginKey() {
		return thirdLoginKey;
	}

	public void setThirdLoginKey(String thirdLoginKey) {
		this.thirdLoginKey = thirdLoginKey;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	
}
