package com.jingyunbank.etrade.track.entity;

import java.util.Date;

import com.jingyunbank.etrade.api.goods.bo.BaseGoods;
/**
 * 
* Title:    我的足迹商品查询实体
* @author    liug
* @date      2015年11月11日
 */
public class FootprintGoodsEntity  extends BaseGoods{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date visitTime;
	public Date getVisitTime() {
		return visitTime;
	}
	public void setVisitTime(Date visitTime) {
		this.visitTime = visitTime;
	}
	
	
}
