package com.jingyunbank.etrade.wap.goods.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.wap.goods.entity.GoodsImgEntity;

public interface GoodsImgDao {

	/**
	 * 获取商品详情的图片集合
	 * @param gid
	 * @return
	 * @throws Exception
	 */
	public List<GoodsImgEntity> selectGoodsDetailImgs(@Param("gid") String gid) throws Exception;
}
