package com.jingyunbank.etrade.back.goods.dao;

import java.util.List;
import java.util.Map;

import com.jingyunbank.etrade.back.goods.entity.GoodsListEntity;

/**
 * 商品后台管理
 * @author liug
 *
 */
public interface GoodsBKDao {
	public List<GoodsListEntity> selectGoodsByCondition(Map<String, Object> map) throws Exception;
}
