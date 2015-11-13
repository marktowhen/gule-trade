package com.jingyunbank.etrade.goods.dao;

import com.jingyunbank.etrade.goods.entity.GoodsDetailEntity;
import com.jingyunbank.etrade.goods.entity.GoodsEntity;
import com.jingyunbank.etrade.goods.entity.GoodsImgEntity;

/**
 * 
 * Title: 商品的操作DAO
 * 
 * @author duanxf
 * @date 2015年11月13日
 */
public interface GoodsOperationDao {
	/**
	 * 保存商品信息
	 * 
	 * @param goodsEntity
	 * @return
	 * @throws Exception
	 */
	public int insertGoods(GoodsEntity goodsEntity) throws Exception;

	/**
	 * 保存商品详细信息
	 * 
	 * @param goodsDetailEntity
	 * @return
	 * @throws Exception
	 */
	public int insertGoodsDetail(GoodsDetailEntity goodsDetailEntity) throws Exception;

	/**
	 *  保存商品图片
	 * @param goodsImgEntity
	 * @return
	 * @throws Exception
	 */
	public int insertGoodsImg(GoodsImgEntity goodsImgEntity) throws Exception;
}
