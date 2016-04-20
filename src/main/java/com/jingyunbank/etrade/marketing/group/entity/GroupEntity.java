package com.jingyunbank.etrade.marketing.group.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jingyunbank.etrade.api.user.bo.Users;

public class GroupEntity {

	private GroupGoodsEntity goods;
	private String ID;
	private Users leader;
	private Date start;
	private List<GroupUserEntity> buyers = new ArrayList<GroupUserEntity>();
	
	public GroupGoodsEntity getGoods() {
		return goods;
	}
	public void setGoods(GroupGoodsEntity goods) {
		this.goods = goods;
	}
	public Users getLeader() {
		return leader;
	}
	public void setLeader(Users leader) {
		this.leader = leader;
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
