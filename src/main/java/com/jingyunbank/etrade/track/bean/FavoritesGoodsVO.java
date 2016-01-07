package com.jingyunbank.etrade.track.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jingyunbank.etrade.api.goods.bo.BaseGoods;

/**
 * 我的收藏商品的信息
 * @author liug
 *
 */
public class FavoritesGoodsVO extends BaseGoods{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** id */
	private String ID;
	/****我的足迹 冗余浏览时间字段***/
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date visitTime;
	/****我的收藏 冗余收藏时间字段***/
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date collectTime;
	
	
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}
	
	public Date getVisitTime() {
		return visitTime;
	}
	public void setVisitTime(Date visitTime) {
		this.visitTime = visitTime;
	}
	public Date getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}
	
}
