package com.jingyunbank.etrade.user.bean;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.jingyunbank.core.lang.Patterns;

public class LoginUserSmsVO {

	@NotEmpty(message="手机号不能为空")
	@Pattern(regexp=Patterns.INTERNAL_MOBILE_PATTERN,message="手机格式不正确")
	private String mobile;
	@NotEmpty(message="验证码不能为空")
	private String code;
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
}
