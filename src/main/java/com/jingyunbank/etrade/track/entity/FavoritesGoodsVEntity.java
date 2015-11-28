package com.jingyunbank.etrade.track.entity;

import java.util.Date;

import com.jingyunbank.etrade.api.goods.bo.BaseGoods;
/**
 * 
* Title:    我的收藏商品查询视图
* @author    liug
* @date      2015年11月12日
 */
public class FavoritesGoodsVEntity  extends BaseGoods{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ID;
	private Date collectTime;
	public Date getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	

}
