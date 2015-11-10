package com.jingyunbank.etrade.goods.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.goods.entity.GoodsDaoEntity;
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
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<GoodsDaoEntity> selectGoodsByLikeName(Map<String,Object> map) throws Exception;
	/**
	 * 查询所有品牌
	 * @return
	 * @throws Exception
	 */
	public List<GoodsDaoEntity> selectBrands() throws Exception;
	/**
	 * 查询所有类型
	 * @return
	 * @throws Exception
	 */
	public List<GoodsDaoEntity> selectTypes() throws Exception;
	/**
	 * 多条件查询商品
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<GoodsDaoEntity> selectGoodsByWhere(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询热门推荐商品列表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<HotGoodsEntity> selectHotGoods() throws Exception;
	/**
	 * 查询宝贝推荐
	 * @return
	 */
	public List<GoodsDaoEntity> selectRecommend() throws Exception;
}
