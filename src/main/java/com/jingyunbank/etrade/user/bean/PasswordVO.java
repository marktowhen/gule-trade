package com.jingyunbank.etrade.user.bean;

import org.hibernate.validator.constraints.NotEmpty;

public class PasswordVO {

	@NotEmpty(message="旧密码不能为空")
	private String oldPwd;
	@NotEmpty(message="新密码不能为空")
	private String newPwd;

	public String getOldPwd() {
		return oldPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
	
	
}
