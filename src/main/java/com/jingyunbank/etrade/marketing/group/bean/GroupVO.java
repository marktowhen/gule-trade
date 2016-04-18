package com.jingyunbank.etrade.marketing.group.bean;

import java.util.Date;

import com.jingyunbank.etrade.api.user.bo.Users;

public class GroupVO {
	
	private GroupGoodsVO goods;
	private String ID;
	private Users leader;
	private Date start;
	public GroupGoodsVO getGoods() {
		return goods;
	}
	public void setGoods(GroupGoodsVO goods) {
		this.goods = goods;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
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
	
}
