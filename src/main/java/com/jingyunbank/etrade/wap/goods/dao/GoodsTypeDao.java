package com.jingyunbank.etrade.wap.goods.dao;

import java.util.List;

import com.jingyunbank.etrade.wap.goods.entity.GoodsTypeEntity;

/**
 * 
 * Title: GoodsTypeDao 商品类型dao
 * 
 * @author duanxf
 * @date 2016年3月31日
 */
public interface GoodsTypeDao {

	public void insertGoodsType(GoodsTypeEntity entity) throws Exception;

	public GoodsTypeEntity selectOneGoodsType(String tid) throws Exception;

	public void updateGoodsType(GoodsTypeEntity entity) throws Exception;

	public List<GoodsTypeEntity> selectGoodsTypes(String name) throws Exception;

	public List<GoodsTypeEntity> selectAllGoodsTypes() throws Exception;

	public void delGoodsType(String tid) throws Exception;
}
