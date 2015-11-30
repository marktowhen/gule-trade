package com.jingyunbank.etrade.track.bean;

import java.util.List;

/**
 * 收藏商品
 * @author liug
 *
 */
public class FavoritesGoodsFacadeVO {
	/**收藏商品数量*/
	private int count;
	/**收藏的商品*/
	private List<FavoritesGoodsVO> goodslist;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<FavoritesGoodsVO> getGoodslist() {
		return goodslist;
	}
	public void setGoodslist(List<FavoritesGoodsVO> goodslist) {
		this.goodslist = goodslist;
	}
	
}
