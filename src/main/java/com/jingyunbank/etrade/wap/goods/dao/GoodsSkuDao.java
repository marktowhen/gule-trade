package com.jingyunbank.etrade.wap.goods.dao;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.wap.goods.entity.GoodsOperationEntity;
import com.jingyunbank.etrade.wap.goods.entity.GoodsSkuEntity;

/**
 * 
 * Title: GoodsSkuDao
 * 
 * @author duanxf
 * @date 2016年4月12日
 */
public interface GoodsSkuDao {

	public void insertGoodsSku(GoodsSkuEntity entity) throws Exception;

	public void updateGoodsSku(GoodsSkuEntity entity) throws Exception;

	public void deleteGoodsSku(@Param("gid") String gid) throws Exception;

	public void up(@Param("skuId") String skuId) throws Exception;

	public void down(@Param("skuId") String skuId) throws Exception;

	public GoodsOperationEntity selectGoodsByGid(@Param("gid") String gid) throws Exception;
}
