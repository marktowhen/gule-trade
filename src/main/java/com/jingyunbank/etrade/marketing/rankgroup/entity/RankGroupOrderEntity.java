package com.jingyunbank.etrade.marketing.rankgroup.entity;

public class RankGroupOrderEntity {
	private String ID;
	private String groupID;
	private String groupUserID; //参团用户表 id
	private String OID;
	private long orderno;
	private String type;//指明是押金、尾款等
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
	public String getGroupUserID() {
		return groupUserID;
	}
	public void setGroupUserID(String groupUserID) {
		this.groupUserID = groupUserID;
	}
	public String getOID() {
		return OID;
	}
	public void setOID(String oID) {
		OID = oID;
	}
	public long getOrderno() {
		return orderno;
	}
	public void setOrderno(long orderno) {
		this.orderno = orderno;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
