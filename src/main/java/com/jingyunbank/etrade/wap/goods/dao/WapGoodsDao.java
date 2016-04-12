package com.jingyunbank.etrade.wap.goods.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.wap.goods.entity.GoodsShowEntity;
import com.jingyunbank.etrade.wap.goods.entity.GoodsSkuConditionEntity;
import com.jingyunbank.etrade.wap.goods.entity.GoodsSkuEntity;

/**
 * 
 * Title: GoodsDao 商品dao
 * 
 * @author duanxf
 * @date 2016年4月6日
 */
public interface WapGoodsDao {

	public List<GoodsShowEntity> selectGoods(@Param("mid") String mid, @Param("tid") String tid,
			@Param("order") String order) throws Exception;

	public GoodsSkuConditionEntity selectGoodsSkuConditionByGid(@Param("gid") String gid) throws Exception;

	public GoodsSkuEntity selectGoodsSku(@Param("gid") String gid, @Param("condition") String condition);
}
