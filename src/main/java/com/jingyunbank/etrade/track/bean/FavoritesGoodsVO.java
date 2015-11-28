package com.jingyunbank.etrade.track.bean;

import com.jingyunbank.etrade.goods.bean.CommonGoodsVO;

/**
 * 我的收藏商品的信息
 * @author liug
 *
 */
public class FavoritesGoodsVO extends CommonGoodsVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** id */
	private String ID;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}
	
}
