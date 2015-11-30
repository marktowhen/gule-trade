package com.jingyunbank.etrade.track.bean;

import java.util.List;

/**
 * 收藏商家展示
 * @author liug
 *
 */
public class FavoritesMerchantFacadeVO {
	/**收藏商家数量*/
	private int count;
	/**收藏的商家*/
	private List<FavoritesMerchantVO> merchantlist;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<FavoritesMerchantVO> getMerchantlist() {
		return merchantlist;
	}
	public void setMerchantlist(List<FavoritesMerchantVO> merchantlist) {
		this.merchantlist = merchantlist;
	}
	
}
