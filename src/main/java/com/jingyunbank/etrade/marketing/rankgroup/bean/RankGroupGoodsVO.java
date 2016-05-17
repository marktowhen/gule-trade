package com.jingyunbank.etrade.marketing.rankgroup.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RankGroupGoodsVO {
	private String ID;
	private String SKUID;
	private String GID;
	private String groupID;
	private long duration;
	private Date deadline;
	private BigDecimal deposit;
	private Date addtime;
	private boolean show;
	private int userCount;
	private List<RankGroupGoodsPriceSettingVO> priceSettings=new ArrayList<RankGroupGoodsPriceSettingVO>();
	
	public String getGroupID() {
		return groupID;
	}
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	public int getUserCount() {
		return userCount;
	}
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getSKUID() {
		return SKUID;
	}
	public List<RankGroupGoodsPriceSettingVO> getPriceSettings() {
		return priceSettings;
	}
	public void setPriceSettings(List<RankGroupGoodsPriceSettingVO> priceSettings) {
		this.priceSettings = priceSettings;
	}
	public void setSKUID(String sKUID) {
		SKUID = sKUID;
	}
	
	public String getGID() {
		return GID;
	}
	public void setGID(String gID) {
		GID = gID;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public Date getDeadline() {
		return deadline;
	}
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	public BigDecimal getDeposit() {
		return deposit;
	}
	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
	}
	

}
