package com.jingyunbank.etrade.marketing.group.bean;

import java.math.BigDecimal;
import java.util.Date;

public class GroupUserVO {

	private String ID;
	private String groupID;
	private String UID;
	private Date jointime;
	private BigDecimal paid;
	private String status;
	private String headImgUrl;
	
	private String nickname;
	
	
	
	/**
	 * @return the headImgUrl
	 */
	public String getHeadImgUrl() {
		return headImgUrl;
	}
	/**
	 * @param headImgUrl the headImgUrl to set
	 */
	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}
	/**
	 * @param nickname the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getGroupID() {
		return groupID;
	}
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	public Date getJointime() {
		return jointime;
	}
	public void setJointime(Date jointime) {
		this.jointime = jointime;
	}
	public String getUID() {
		return UID;
	}
	public void setUID(String uID) {
		UID = uID;
	}
	public BigDecimal getPaid() {
		return paid;
	}
	public void setPaid(BigDecimal paid) {
		this.paid = paid;
	}
	
	
}
