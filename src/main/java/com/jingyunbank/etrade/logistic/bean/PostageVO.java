package com.jingyunbank.etrade.logistic.bean;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

public class PostageVO {

	private String ID;
	private String MID;//商家id
	private String title;//标题
	private String type;//收费类型 number:按件计费 weight:按重量 volume:体积
	private boolean valid;
	private BigDecimal postage;
	@Valid
	private List<PostageDetailVO> postageDetailList;
	
	
	public List<PostageDetailVO> getPostageDetailList() {
		return postageDetailList;
	}
	public void setPostageDetailList(List<PostageDetailVO> postageDetailList) {
		this.postageDetailList = postageDetailList;
	}
	public BigDecimal getPostage() {
		return postage;
	}
	public void setPostage(BigDecimal postage) {
		this.postage = postage;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getMID() {
		return MID;
	}
	public void setMID(String mID) {
		MID = mID;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
}
