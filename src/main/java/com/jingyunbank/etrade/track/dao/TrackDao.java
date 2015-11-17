package com.jingyunbank.etrade.track.dao;

import java.util.List;
import java.util.Map;

import com.jingyunbank.etrade.track.entity.CollectEntity;
import com.jingyunbank.etrade.track.entity.CollectGoodsVEntity;
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
	public int isCollectExists(Map<String,String> map);
	/**
	 * 保存我的收藏信息
	 * @param fe
	 * @return
	 * @throws Exception
	 */
	public int insertCollect(CollectEntity ce) throws Exception;
	/**
	 * 查询我的收藏信息
	 * @return
	 * @throws Exception
	 */
	public List<CollectGoodsVEntity> selectMerchantCollect(Map<String,String> map) throws Exception;
}
