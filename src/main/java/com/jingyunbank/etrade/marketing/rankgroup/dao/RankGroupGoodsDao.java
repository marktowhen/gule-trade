package com.jingyunbank.etrade.marketing.rankgroup.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.marketing.rankgroup.entity.RankGroupGoodsEntity;
import com.jingyunbank.etrade.marketing.rankgroup.entity.RankGroupGoodsShowEntity;

public interface RankGroupGoodsDao {
	
	/**
	 * 添加团购商品
	 * @param goods
	 * @throws Exception
	 */
	public void insert(RankGroupGoodsEntity goods) throws Exception;
	
	/**
	 * 查询商品详情
	 * @param ggid
	 * @return
	 */
	public RankGroupGoodsEntity selectOne(String ggid) ;
	
	/**
	 * 查询商品列表
	 * @param MID
	 * @param goodsName
	 * @param from
	 * @param size
	 * @return
	 */
	public List<RankGroupGoodsShowEntity> selectMany(@Param("MID")String MID,@Param("goodsName")String goodsName, @Param("from") long from, 
			@Param("size") int size) ;

	/**
	 * 根据团购id 查询该团购商品信息
	 * @param groupID
	 * @return
	 */
	public RankGroupGoodsEntity selectOneByGroupID(String groupID);
	
	/**
	 * 修改商品
	 * @param goods
	 * @throws Exception
	 */
	public void update(RankGroupGoodsEntity goods) throws Exception;

    public int count(@Param("MID")String MID,@Param("goodsName")String goodsName);


}
