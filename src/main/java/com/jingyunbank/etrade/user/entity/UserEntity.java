/**
 * @Title:UserEntity.java
@Description:TODO
@author:Administrator
@date:上午9:41:44

 */
package com.jingyunbank.etrade.user.entity;


//import com.jingyunbank.etrade.api.user.bo.UserInfo;

/**
 * @author guoyuxue
 *
 */
public class UserEntity {
	private String ID;
	private String username;//global unique, ([a-zA-Z]+[a-zA-Z0-9]){4, 20}必须英文字母开头
	private String mobile;//11位数字的有效手机号
	private String email;//有效的邮箱
	private String password;
	private String tradepwd;
	private String nickname;
	//private UserInfo uinfo;
	private boolean locked;
	
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
	/*public UserInfo getUinfo() {
		return uinfo;
	}
	public void setUinfo(UserInfo uinfo) {
		this.uinfo = uinfo;
	}*/
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
}
