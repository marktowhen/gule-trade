package com.jingyunbank.etrade.track.dao;

import java.util.List;
import java.util.Map;

import com.jingyunbank.etrade.track.entity.FavoritesEntity;
import com.jingyunbank.etrade.track.entity.FavoritesGoodsVEntity;
import com.jingyunbank.etrade.track.entity.FootprintEntity;
import com.jingyunbank.etrade.track.entity.FootprintGoodsEntity;

/**
 * 
 * @author liug
 *
 */
public interface TrackDao {
	/**
	 * 查询我的足迹列表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<FootprintGoodsEntity> selectFootprintGoods() throws Exception;
	/**
	 * 保存我的足迹信息
	 * @param fe
	 * @return
	 * @throws Exception
	 */
	public int insertFootprint(FootprintEntity fe) throws Exception;
	/***
	 * 检查店铺和商家是否已经收藏
	 * @param map   --uid fid type
	 * @return
	 */
	public int isFavoritesExists(Map<String,String> map);
	/**
	 * 保存我的收藏信息
	 * @param fe
	 * @return
	 * @throws Exception
	 */
	public int insertFavorites(FavoritesEntity ce) throws Exception;
	/**
	 * 查询我的收藏信息
	 * @return
	 * @throws Exception
	 */
	public List<FavoritesGoodsVEntity> selectMerchantFavorites(Map<String,String> map) throws Exception;
}
