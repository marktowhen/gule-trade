package com.jingyunbank.etrade.goods.entity;

import com.jingyunbank.etrade.api.goods.bo.BaseGoods;
/**
 * 
* Title:    热门推荐商品查询视图
* @author    liug
* @date      2015年11月7日
 */
public class HotGoodsEntity  extends BaseGoods{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String imgPath; // 商家图片
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
}
