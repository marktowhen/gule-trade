package com.jingyunbank.etrade.goods.dao;

import java.util.List;

import com.jingyunbank.etrade.goods.entity.GoodsTypeEntity;

/**
 * 类别管理DAO
 * 
 * @author liug
 *
 */
public interface GoodsTypeDao {

	public int insertGoodsType(GoodsTypeEntity entity) throws Exception;

	public GoodsTypeEntity selectOneGoodsType(String tid) throws Exception;

	public int updateGoodsType(GoodsTypeEntity entity) throws Exception;

	public List<GoodsTypeEntity> selectGoodsTypes(String name) throws Exception;

	public List<GoodsTypeEntity> selectAllGoodsTypes() throws Exception;

	public int delGoodsType(String tid) throws Exception;
}
