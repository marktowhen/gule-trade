package com.jingyunbank.etrade.award.entity;

public class SalesUserrelationshipEntity {

	private String ID;
	private String SID;
	private String UID;
	private String realname;
	private String cardid;
	private String realnameremark ;
	private String phone;
	private String phoneremark ;
	private String password ;
	private String passwordremark ;
	public String getUID() {
		return UID;
	}
	public void setUID(String uID) {
		UID = uID;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getSID() {
		return SID;
	}
	public void setSID(String sID) {
		SID = sID;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getCardid() {
		return cardid;
	}
	public void setCardid(String cardid) {
		this.cardid = cardid;
	}
	public String getRealnameremark() {
		return realnameremark;
	}
	public void setRealnameremark(String realnameremark) {
		this.realnameremark = realnameremark;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPhoneremark() {
		return phoneremark;
	}
	public void setPhoneremark(String phoneremark) {
		this.phoneremark = phoneremark;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPasswordremark() {
		return passwordremark;
	}
	public void setPasswordremark(String passwordremark) {
		this.passwordremark = passwordremark;
	}
	
}
