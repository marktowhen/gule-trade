package com.jingyunbank.etrade.logistic.bean;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class PostageMerchatVO {

	@NotEmpty
	private String MID;
	private BigDecimal postage;
	@Size(min=1)
	@Valid
	private List<PostageCalculateByGIDVO> goods;
	public String getMID() {
		return MID;
	}
	public void setMID(String mID) {
		MID = mID;
	}
	
	public BigDecimal getPostage() {
		return postage;
	}
	public void setPostage(BigDecimal postage) {
		this.postage = postage;
	}
	public List<PostageCalculateByGIDVO> getGoods() {
		return goods;
	}
	public void setGoods(List<PostageCalculateByGIDVO> goods) {
		this.goods = goods;
	}
}
