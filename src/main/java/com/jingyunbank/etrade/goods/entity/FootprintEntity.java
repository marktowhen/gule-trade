package com.jingyunbank.etrade.goods.entity;

import java.io.Serializable;
import java.util.Date;

public class FootprintEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**主键*/
	private String ID;
	/**用户ID*/
	private String UID;
	/**商品ID*/
	private String GID;
	/**浏览时间*/
	private Date visitTime;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getUID() {
		return UID;
	}
	public void setUID(String uID) {
		UID = uID;
	}
	public String getGID() {
		return GID;
	}
	public void setGID(String gID) {
		GID = gID;
	}
	public Date getVisitTime() {
		return visitTime;
	}
	public void setVisitTime(Date visitTime) {
		this.visitTime = visitTime;
	}
	
}
