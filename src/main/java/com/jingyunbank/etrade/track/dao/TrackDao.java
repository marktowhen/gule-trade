package com.jingyunbank.etrade.track.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.track.entity.AdDetailEntity;
import com.jingyunbank.etrade.track.entity.AdModuleEntity;
import com.jingyunbank.etrade.track.entity.FavoritesEntity;
import com.jingyunbank.etrade.track.entity.FavoritesGoodsVEntity;
import com.jingyunbank.etrade.track.entity.FootprintEntity;
import com.jingyunbank.etrade.track.entity.FootprintGoodsEntity;
import com.jingyunbank.etrade.track.entity.OtherGoodsEntity;
import com.jingyunbank.etrade.track.entity.RecommendGoodsEntity;

/**
 * 
 * @author liug
 *
 */
public interface TrackDao {
	/**
	 * 查询我的足迹列表
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<FootprintGoodsEntity> selectFootprintGoods(Map<String, Object> params) throws Exception;

	/**
	 * 保存我的足迹信息
	 * 
	 * @param fe
	 * @return
	 * @throws Exception
	 */
	public int insertFootprint(FootprintEntity fe) throws Exception;

	/***
	 * 检查店铺和商家是否已经收藏
	 * 
	 * @param map
	 *            --uid fid type
	 * @return
	 */
	public int isFavoritesExists(Map<String, String> map);

	/**
	 * 保存我的收藏信息
	 * 
	 * @param fe
	 * @return
	 * @throws Exception
	 */
	public int insertFavorites(FavoritesEntity ce) throws Exception;

	/**
	 * 查询我的收藏信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<FavoritesGoodsVEntity> selectMerchantFavorites(Map<String, Object> map) throws Exception;

	/**
	 * 查询我的收藏数量
	 * 
	 * @return
	 * @throws Exception
	 */
	public int selectMerchantFavoritesCount(Map<String, Object> map) throws Exception;

	/**
	 * 删除我的收藏
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteFavoritesById(@Param("id") List<String> id) throws Exception;

	/**
	 * 查询广告明细
	 * 
	 * @param params
	 * @return
	 */
	public List<AdDetailEntity> selectAdDetails(Map<String, String> params);

	/**
	 * 根据ID查询广告模块信息
	 * 
	 * @param id
	 * @return
	 */
	public AdModuleEntity selectAdmoduleById(String id);

	/**
	 * 根据ID查询广告信息
	 * 
	 * @param id
	 * @return
	 */
	public AdDetailEntity selectAddetailById(String id);

	/**
	 * 保存广告模块
	 * 
	 * @param adModuleEntity
	 * @return
	 * @throws Exception
	 */
	public boolean insertAdModule(AdModuleEntity adModuleEntity) throws Exception;

	/**
	 * 保存广告
	 * 
	 * @param adDetailEntity
	 * @return
	 * @throws Exception
	 */
	public boolean insertAdDetail(AdDetailEntity adDetailEntity) throws Exception;

	/**
	 * 更新广告模块
	 * 
	 * @param adModuleEntity
	 * @return
	 * @throws DataRefreshingException
	 */
	public boolean updateAdmodule(AdModuleEntity adModuleEntity) throws DataRefreshingException;

	/**
	 * 更新广告
	 * 
	 * @param adDetailEntity
	 * @return
	 * @throws DataRefreshingException
	 */
	public boolean updateAddetail(AdDetailEntity adDetailEntity) throws DataRefreshingException;

	public List<AdModuleEntity> selectModulesByCondition(Map<String, Object> map) throws Exception;

	public List<AdDetailEntity> selectAddetailsByCondition(Map<String, Object> map) throws Exception;

	/**
	 * 删除广告
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteAddetail(@Param("id") List<String> id) throws Exception;

	/**
	 * 删除广告模块信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteAdmodule(@Param("id") List<String> id) throws Exception;

	/**
	 * 查询广告数量
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int selectAddetailsCount(@Param("id") List<String> id) throws Exception;

	/**
	 * 查询推荐品牌
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> selectRecommendBidstr(Map<String, Object> params) throws Exception;

	/**
	 * 查询推荐类别
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> selectRecommendTidstr(Map<String, Object> params) throws Exception;

	/**
	 * 查询推荐商品
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<RecommendGoodsEntity> selectRecommendGoods(@Param("bidstr") String bidstr,
			@Param("tidstr") String tidstr, @Param("bids") List<String> bids, @Param("tids") List<String> tids,
			@Param("from") int from, @Param("to") int to, @Param("uid") String uid) throws Exception;

	/**
	 * 查询其他用户购买的商品
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<OtherGoodsEntity> selectOtherGoods(Map<String, Object> params) throws Exception;
	
	/**
	 * 判断是否收藏过商品
	 * @param uid
	 * @param gid
	 * @param string
	 * @return
	 * @throws Exception
	 */
	public FavoritesEntity isFav(@Param("uid") String uid, @Param("gid") String gid, @Param("type")String string) throws Exception;

}
