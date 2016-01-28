package com.jingyunbank.etrade.user.bean;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.jingyunbank.core.lang.Patterns;

public class ThirdLoginRegistVO {

	@Pattern(regexp="^([a-zA-Z]+[a-zA-Z0-9]{3,19})$",message="用户名必须以字母开头，并且只能是字母或数字")
	@NotNull(message="用户名不能为空")
	private String username;//global unique, ([a-zA-Z]+[a-zA-Z0-9]){4, 20}必须英文字母开头
	@NotNull(message="手机号不能为空")
	@Pattern(regexp=Patterns.INTERNAL_MOBILE_PATTERN,message="手机格式不正确")
	private String mobile;//11位数字的有效手机号
	@NotNull(message="密码不能为空")
	private String password;
	private String tradepwd;
	private String nickname; 
	private String code;
	
	private String type;//登录类型
	private String accessToken;//授权码
	private String thirdLoginKey;//唯一标示
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTradepwd() {
		return tradepwd;
	}
	public void setTradepwd(String tradepwd) {
		this.tradepwd = tradepwd;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getThirdLoginKey() {
		return thirdLoginKey;
	}
	public void setThirdLoginKey(String thirdLoginKey) {
		this.thirdLoginKey = thirdLoginKey;
	}
	
	
}
