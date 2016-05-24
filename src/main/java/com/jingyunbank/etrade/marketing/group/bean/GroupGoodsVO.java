package com.jingyunbank.etrade.marketing.group.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;

public class GroupGoodsVO {

	private String ID;
	@NotBlank
	private String SKUID;
	@Min(1)
	private long duration;//开团后团的时间长度，second
	@NotNull
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date deadline;//团购截止日期
	private int groupPeople;//成团人数
	private BigDecimal groupPrice;//团购的价格
	/*//团购阶梯价
	@Valid
	@NotNull
	@Size(min=1)
	private List<GroupGoodsPriceSettingVO> priceSettings = new ArrayList<GroupGoodsPriceSettingVO>();
	private BigDecimal deposit;//订金，（多退少补）*/
	//团长佣金
	/*private BigDecimal commission;*/
	private boolean show;//是否上架
	@NotEmpty
	private String GID;
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
	
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
	}
	/**
	 * @return the groupPeople
	 */
	public int getGroupPeople() {
		return groupPeople;
	}
	/**
	 * @param groupPeople the groupPeople to set
	 */
	public void setGroupPeople(int groupPeople) {
		this.groupPeople = groupPeople;
	}
	/**
	 * @return the groupPrice
	 */
	public BigDecimal getGroupPrice() {
		return groupPrice;
	}
	/**
	 * @param groupPrice the groupPrice to set
	 */
	public void setGroupPrice(BigDecimal groupPrice) {
		this.groupPrice = groupPrice;
	}
	
}
