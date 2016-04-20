package com.jingyunbank.etrade.marketing.group.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.jingyunbank.etrade.api.user.bo.Users;

public class GroupUserEntity {

	private Users user;
	private GroupEntity group;
	private Date jointime;
	private BigDecimal paid;
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
	public GroupEntity getGroup() {
		return group;
	}
	public void setGroup(GroupEntity group) {
		this.group = group;
	}
	public Date getJointime() {
		return jointime;
	}
	public void setJointime(Date jointime) {
		this.jointime = jointime;
	}
	public BigDecimal getPaid() {
		return paid;
	}
	public void setPaid(BigDecimal paid) {
		this.paid = paid;
	}
	
}
