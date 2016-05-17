package com.jingyunbank.etrade.marketing.rankgroup.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jingyunbank.etrade.api.marketing.rankgroup.bo.RankGroupUser;
import com.jingyunbank.etrade.api.user.bo.Users;

public class RankGroupVO {
	private String ID;
	private RankGroupGoodsVO rankGoods;
	private List<RankGroupUser> buyers = new ArrayList<RankGroupUser>();
	private Date start;  //开始时间
	private Users leader;//团长
	private String leaderUID;//团长id
	private String groupGoodsID;  //团购商品id
	public String getLeaderUID() {
		return leaderUID;
	}
	public void setLeaderUID(String leaderUID) {
		this.leaderUID = leaderUID;
	}
	public String getGroupGoodsID() {
		return groupGoodsID;
	}
	public void setGroupGoodsID(String groupGoodsID) {
		this.groupGoodsID = groupGoodsID;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}

	public RankGroupGoodsVO getRankGoods() {
		return rankGoods;
	}
	public void setRankGoods(RankGroupGoodsVO rankGoods) {
		this.rankGoods = rankGoods;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Users getLeader() {
		return leader;
	}
	public void setLeader(Users leader) {
		this.leader = leader;
	}
	public List<RankGroupUser> getBuyers() {
		return buyers;
	}
	public void setBuyers(List<RankGroupUser> buyers) {
		this.buyers = buyers;
	}

}
