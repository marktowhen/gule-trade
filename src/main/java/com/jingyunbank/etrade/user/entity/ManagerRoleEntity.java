package com.jingyunbank.etrade.user.entity;

import java.util.Date;

/**
 *	用户角色
 */
public class ManagerRoleEntity{

	private String ID;
	private String roleID;//角色id
	private String UID;//用户id
	private boolean valid;//是否有效
	private Date updateTime;//变动时间
	private RoleEntity role = new RoleEntity();
	
	public RoleEntity getRole() {
		return role;
	}
	public void setRole(RoleEntity role) {
		this.role = role;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getRoleID() {
		return roleID;
	}
	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}
	public String getUID() {
		return UID;
	}
	public void setUID(String uID) {
		UID = uID;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	

}
