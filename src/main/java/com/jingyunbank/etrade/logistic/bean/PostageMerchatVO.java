package com.jingyunbank.etrade.logistic.bean;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class PostageMerchatVO {

	@NotEmpty
	private String MID;
	private BigDecimal price;
	@Size(min=1)
	@Valid
	private List<PostageCalculateByGIDVO> postageList;
	public String getMID() {
		return MID;
	}
	public void setMID(String mID) {
		MID = mID;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public List<PostageCalculateByGIDVO> getPostageList() {
		return postageList;
	}
	public void setPostageList(List<PostageCalculateByGIDVO> postageList) {
		this.postageList = postageList;
	}
	
}
