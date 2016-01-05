package com.jingyunbank.etrade.user.bean;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;

import com.jingyunbank.core.lang.Patterns;

/**
 * @author Administrator
 *
 */
public class UserVO {
	private String ID;
	@Pattern(regexp="^([a-zA-Z]+[a-zA-Z0-9]{3,19})$",message="用户名必须以字母开头，并且只能是字母或数字")
	@NotNull(message="用户名不能为空")
	private String username;//global unique, ([a-zA-Z]+[a-zA-Z0-9]){4, 20}必须英文字母开头
	@Pattern(regexp=Patterns.INTERNAL_MOBILE_PATTERN,message="手机格式不正确")
	private String mobile;//11位数字的有效手机号
	@Email(regexp=Patterns.INTERNAL_EMAIL_PATTERN,message="邮箱格式不正确")
	private String email;//有效的邮箱
	@NotNull(message="密码不能为空")
	private String password;
	private String tradepwd;
	private String nickname;
	//private UserInfo uinfo;
	private boolean locked;
	private String code;
	
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
}
