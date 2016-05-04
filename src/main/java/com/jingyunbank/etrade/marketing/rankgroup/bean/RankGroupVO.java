package com.jingyunbank.etrade.marketing.rankgroup.bean;

import java.util.Date;

import com.jingyunbank.etrade.api.user.bo.Users;

public class RankGroupVO {
	private String ID;
	private RankGroupGoodsVO rankGroupGoods;
	private Date start;  //开始时间
	private Users leader;//团长
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public RankGroupGoodsVO getRankGroupGoods() {
		return rankGroupGoods;
	}
	public void setRankGroupGoods(RankGroupGoodsVO rankGroupGoods) {
		this.rankGroupGoods = rankGroupGoods;
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

}
