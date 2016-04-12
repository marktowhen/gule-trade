package com.jingyunbank.etrade.wap.goods.dao;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.goods.entity.GoodsEntity;

public interface WapGoodsOperationDao {

	public void insertGoods(GoodsEntity entity) throws Exception;

	public void updateGoods(GoodsEntity entity) throws Exception;

	public void deleteGoods(@Param("gid") String gid) throws Exception;

}
