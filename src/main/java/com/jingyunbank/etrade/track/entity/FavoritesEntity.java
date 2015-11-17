package com.jingyunbank.etrade.track.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * 我的收藏实体类
 * @author liug
 *
 */
public class FavoritesEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 主键*/
	private String ID;
	/** 用户ID*/
	private String UID;
	/** 收藏ID*/
	private String fid;
	/** 收藏类型 1商家2商品*/
	private String type;
	/** 收藏时间*/
	private Date collectTime;
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
	public String getFid() {
		return fid;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}
	
}
