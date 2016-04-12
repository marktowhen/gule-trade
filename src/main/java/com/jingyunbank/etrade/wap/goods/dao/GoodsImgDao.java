package com.jingyunbank.etrade.wap.goods.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.wap.goods.entity.GoodsImgEntity;

public interface GoodsImgDao {

	/**
	 * 获取商品详情的图片集合
	 * 
	 * @param gid
	 * @return
	 * @throws Exception
	 */
	public List<GoodsImgEntity> selectGoodsDetailImgs(@Param("gid") String gid) throws Exception;

	/**
	 * 添加图片
	 * <p>
	 * skuid 为null 为商品的展示图片
	 * <p>
	 * 不为null 是sku的展示图片
	 * <p>
	 * 
	 * @param entity
	 * @throws Exception
	 */
	public void insertGoodsImg(GoodsImgEntity entity) throws Exception;

	/**
	 * 删除商品的图片 by gid
	 * 
	 * @param gid
	 * @throws Exception
	 */
	public void deleteGoodsImg(@Param("gid") String gid) throws Exception;

	/**
	 * 删除单个图片 by imgId
	 * 
	 * @param imgId
	 * @throws Exception
	 */
	public void deleteGoodsImgById(@Param("imgId") String imgId) throws Exception;

}
