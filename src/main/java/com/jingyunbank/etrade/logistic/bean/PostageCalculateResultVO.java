package com.jingyunbank.etrade.logistic.bean;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;

public class PostageCalculateResultVO {

	private BigDecimal total;
	//merchatList 里再包含gid的list
	@Valid
	@Size(min=1)
	private List<PostageMerchatVO> merchatList;
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public List<PostageMerchatVO> getMerchatList() {
		return merchatList;
	}
	public void setMerchatList(List<PostageMerchatVO> merchatList) {
		this.merchatList = merchatList;
	}
	
	
}
