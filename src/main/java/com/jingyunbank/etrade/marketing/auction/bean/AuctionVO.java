package com.jingyunbank.etrade.marketing.auction.bean;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

public class AuctionVO {

	@NotBlank
	private String GID;//商品id
	@NotBlank
	private long duration;//开团后团的时间长度，minutes
	@NotBlank
	private Date deadline;//团购截止日期
	private boolean upperlimit;//是否设置人数上限 
	private int minpeople;//最少成团人数
	private BigDecimal deposit;//订金，（多退少补）
	//团长佣金
	private BigDecimal commission;
	private boolean show;//是否上架
	
	
	
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
	public boolean isUpperlimit() {
		return upperlimit;
	}
	public void setUpperlimit(boolean upperlimit) {
		this.upperlimit = upperlimit;
	}
	public int getMinpeople() {
		return minpeople;
	}
	public void setMinpeople(int minpeople) {
		this.minpeople = minpeople;
	}
	public BigDecimal getDeposit() {
		return deposit;
	}
	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}
	public BigDecimal getCommission() {
		return commission;
	}
	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
	}
	
}