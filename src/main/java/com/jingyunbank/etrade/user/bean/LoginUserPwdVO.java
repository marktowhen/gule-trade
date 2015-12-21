package com.jingyunbank.etrade.user.bean;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginUserPwdVO {

	@NotNull
	@Size(min=4)
	private String key;
	@NotNull
	@Size(min=8, max=20)
	private String password;
	private String captcha;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCaptcha() {
		return captcha;
	}
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
	
}
