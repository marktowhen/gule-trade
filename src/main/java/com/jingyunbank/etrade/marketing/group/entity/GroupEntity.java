package com.jingyunbank.etrade.marketing.group.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GroupEntity {

	private String ID;
	private String leaderUID;
	private Date start;
	
	private String groupGoodsID;
	private String status;//该团的状态
	private List<GroupUserEntity> buyers = new ArrayList<GroupUserEntity>();
	private GroupGoodsEntity goods;
	
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
