package com.jingyunbank.etrade.goods.dao;

import java.util.List;
import java.util.Map;

import com.jingyunbank.etrade.goods.entity.GoodsDaoEntity;
import com.jingyunbank.etrade.goods.entity.GoodsMerchantEntity;
import com.jingyunbank.etrade.goods.entity.Hot24GoodsEntity;
import com.jingyunbank.etrade.goods.entity.HotGoodsEntity;

/**
 * 
 * Title: GoodsDao
 * 
 * @author duanxf
 * @date 2015年11月4日
 */

public interface GoodsDao {
	/**
	 * 根据商品名模糊查询商品
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<GoodsDaoEntity> selectGoodsByLikeName(Map<String, Object> map) throws Exception;

	/**
	 * 查询所有品牌
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<GoodsDaoEntity> selectBrands() throws Exception;

	/**
	 * 查询所有类型
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<GoodsDaoEntity> selectTypes() throws Exception;

	/**
	 * 多条件查询商品
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<GoodsDaoEntity> selectGoodsByWhere(Map<String, Object> map) throws Exception;

	/**
	 * 查询热门推荐商品列表
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<HotGoodsEntity> selectHotGoods() throws Exception;

	/**
	 * 查询宝贝推荐
	 * 
	 * @return
	 */
	public List<GoodsDaoEntity> selectRecommend() throws Exception;

	/**
	 * 根据商品条件查询关联店铺
	 * 
	 * @param show
	 * @return
	 * @throws Exception
	 */
	public List<GoodsMerchantEntity> selectMerchantByWhere(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询店铺的关联产品
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<GoodsDaoEntity> selectMerchantByWhereGoods(Map<String, Object> map) throws Exception;
	
	/**
	 * 24小时热卖推荐商品列表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Hot24GoodsEntity> selectHot24Goods() throws Exception;
}
