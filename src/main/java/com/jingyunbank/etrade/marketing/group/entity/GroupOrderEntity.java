package com.jingyunbank.etrade.marketing.group.entity;

import java.math.BigDecimal;
import java.util.Date;

public class GroupOrderEntity  {


	private String ID;
	private String groupID;
	private String groupUserID; //group_user表 id
	private String OID;
	private long orderno;
	private String type;//指明是押金、尾款等
	
	private Date addtime;
	private BigDecimal postage;
	
	
	/**
	 * @return the postage
	 */
	public BigDecimal getPostage() {
		return postage;
	}
	/**
	 * @param postage the postage to set
	 */
	public void setPostage(BigDecimal postage) {
		this.postage = postage;
	}
	/**
	 * @return the addtime
	 */
	public Date getAddtime() {
		return addtime;
	}
	/**
	 * @param addtime the addtime to set
	 */
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
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
