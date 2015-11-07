package com.jingyunbank.etrade.goods.dao;

import java.util.List;
import java.util.Map;

import com.jingyunbank.etrade.goods.entity.GoodsDaoVO;
import com.jingyunbank.etrade.goods.entity.HotGoodsEntity;

/**
 * 
 * Title: GoodsDao
 * 
 * @author duanxf
 * @date 2015年11月4日
 */

public interface GoodsDao {
	public List<GoodsDaoVO> selectGoodsByLikeName(String goodsname) throws Exception;

	public List<GoodsDaoVO> selectBrands() throws Exception;

	public List<GoodsDaoVO> selectTypes() throws Exception;

	public List<GoodsDaoVO> selectGoodsByWhere(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询热门推荐商品列表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<HotGoodsEntity> selectHotGoods() throws Exception;
}
