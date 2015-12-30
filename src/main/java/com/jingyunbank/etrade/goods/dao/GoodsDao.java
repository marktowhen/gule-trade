package com.jingyunbank.etrade.goods.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.goods.entity.GoodsDaoEntity;
import com.jingyunbank.etrade.goods.entity.GoodsMerchantEntity;
import com.jingyunbank.etrade.goods.entity.HoneyGoodsEntity;
import com.jingyunbank.etrade.goods.entity.Hot24GoodsEntity;
import com.jingyunbank.etrade.goods.entity.HotGoodsEntity;
import com.jingyunbank.etrade.goods.entity.SalesRecordEntity;

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
	 * 查询3个品牌 用于首页菜单
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<GoodsDaoEntity> selectBrandsThree(Map<String, Object> map) throws Exception;
	
	
	/**
	 * 查询3个品牌 用于首页菜单
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<GoodsDaoEntity> selectTypesThree(Map<String, Object> map) throws Exception;

	/**
	 * 查询所有类型
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<GoodsDaoEntity> selectTypes() throws Exception;

	/**
	 * 查询所有商品
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<GoodsDaoEntity> selectAllGoods(Map<String, Object> map) throws Exception;

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
	public List<GoodsDaoEntity> selectRecommend(Map<String, Object> map) throws Exception;

	/**
	 * 根据商品条件查询关联店铺
	 * 
	 * @param show
	 * @return
	 * @throws Exception
	 */
	public List<GoodsMerchantEntity> selectMerchantByWhere(Map<String, Object> map) throws Exception;


	/**
	 * 查询店铺的关联产品(全部显示)
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<GoodsDaoEntity> selectMerchantByWhereGoodsMax(Map<String, Object> map) throws Exception;

	/**
	 * 24小时热卖推荐商品列表
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Hot24GoodsEntity> selectHot24Goods() throws Exception;

	/**
	 * 查询推广商品
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<GoodsDaoEntity> selectGoodsExpand() throws Exception;

	/**
	 * 在结果中查询商品
	 * 
	 * @param map
	 * @return
	 */
	public List<GoodsDaoEntity> selectGoodsByGoodsResult(Map<String, Object> map);

	/**
	 * 根据gid 查询商品属性
	 * 
	 * @param gid
	 * @return
	 */
	public GoodsDaoEntity selectOne(String gid);

	/**
	 * 阿胶详情页 宝贝排行列表
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<HoneyGoodsEntity> selectHoneyGoods() throws Exception;
	
	/**
	 * 查询商品的交易记录
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<SalesRecordEntity> selectSalesRecords(Map<String, Object> map) throws Exception;
	
	/**
	 * 获取商品的库存  by gid
	 * @param gids
	 * @return
	 * @throws Exception
	 */
	public List<GoodsDaoEntity> selectGoodsStock(@Param("gids") List<String> gids) throws Exception;

}
