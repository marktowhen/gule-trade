package com.jingyunbank.etrade.marketing.rankgroup.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RankGroupGoodsEntity {
	private String ID;
	private String SKUID;
	private long duration;//开团后团的时间长度，second
	private Date deadline;//团购截止日期
	//团购阶梯价
	private List<RankGroupGoodsPriceSettingEntity> priceSettings = new ArrayList<RankGroupGoodsPriceSettingEntity>();
	private BigDecimal deposit;//订金，（多退少补）
	private boolean show;//是否上架
	private Date addtime;
	private String GID;
	private BigDecimal showPrice;
	public String getGID() {
		return GID;
	}
	public void setGID(String gID) {
		GID = gID;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public BigDecimal getShowPrice() {
		return showPrice;
	}
	public void setShowPrice(BigDecimal showPrice) {
		this.showPrice = showPrice;
	}
	public String getSKUID() {
		return SKUID;
	}
	public void setSKUID(String sKUID) {
		SKUID = sKUID;
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
	public List<RankGroupGoodsPriceSettingEntity> getPriceSettings() {
		return priceSettings;
	}
	public void setPriceSettings(List<RankGroupGoodsPriceSettingEntity> priceSettings) {
		this.priceSettings = priceSettings;
	}
	public BigDecimal getDeposit() {
		return deposit;
	}
	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	
}
