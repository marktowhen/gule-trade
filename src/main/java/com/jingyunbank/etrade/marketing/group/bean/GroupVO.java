package com.jingyunbank.etrade.marketing.group.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jingyunbank.etrade.api.user.bo.Users;

public class GroupVO {
	
	private GroupGoodsVO goods;
	private String leaderUID;
	private String ID;
	private Users leader;
	private Date start;
	private String groupGoodsID;
	private List<GroupUserVO> buyers = new ArrayList<GroupUserVO>();
	
	
	/**
	 * @return the leaderUID
	 */
	public String getLeaderUID() {
		return leaderUID;
	}
	/**
	 * @param leaderUID the leaderUID to set
	 */
	public void setLeaderUID(String leaderUID) {
		this.leaderUID = leaderUID;
	}
	/**
	 * @return the buyers
	 */
	public List<GroupUserVO> getBuyers() {
		return buyers;
	}
	/**
	 * @param buyers the buyers to set
	 */
	public void setBuyers(List<GroupUserVO> buyers) {
		this.buyers = buyers;
	}
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
	/**
	 * @return the groupGoodsID
	 */
	public String getGroupGoodsID() {
		return groupGoodsID;
	}
	/**
	 * @param groupGoodsID the groupGoodsID to set
	 */
	public void setGroupGoodsID(String groupGoodsID) {
		this.groupGoodsID = groupGoodsID;
	}
	
	
}
