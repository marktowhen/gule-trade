package com.jingyunbank.etrade.marketing.group.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GroupEntity {

	private String ID;
	private String leaderUID;
	private Date start;
	private String path;
	private String propertyValue;
	private String gname;
	private String groupGoodsID;
	private String status;//该团的状态
	private List<GroupUserEntity> buyers = new ArrayList<GroupUserEntity>();
	private GroupGoodsEntity goods;
	
	
	/**
	 * @return the gname
	 */
	public String getGname() {
		return gname;
	}
	/**
	 * @param gname the gname to set
	 */
	public void setGname(String gname) {
		this.gname = gname;
	}
	/**
	 * @return the propertyValue
	 */
	public String getPropertyValue() {
		return propertyValue;
	}
	/**
	 * @param propertyValue the propertyValue to set
	 */
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	public GroupGoodsEntity getGoods() {
		return goods;
	}
	public void setGoods(GroupGoodsEntity goods) {
		this.goods = goods;
	}
	public String getGroupGoodsID() {
		return groupGoodsID;
	}
	public void setGroupGoodsID(String groupGoodsID) {
		this.groupGoodsID = groupGoodsID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLeaderUID() {
		return leaderUID;
	}
	public void setLeaderUID(String leaderUID) {
		this.leaderUID = leaderUID;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public List<GroupUserEntity> getBuyers() {
		return buyers;
	}
	public void setBuyers(List<GroupUserEntity> buyers) {
		this.buyers = buyers;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	
}
