package com.jingyunbank.etrade.wap.goods.dao;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.api.wap.goods.bo.ShowGoods;
import com.jingyunbank.etrade.wap.goods.entity.GoodsDeatilEntity;
import com.jingyunbank.etrade.wap.goods.entity.GoodsInfoEntity;
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
			@Param("order") String order, @Param("name") String name) throws Exception;

	public GoodsSkuConditionEntity selectGoodsSkuConditionByGid(@Param("gid") String gid) throws Exception;

	public GoodsSkuEntity selectGoodsSku(@Param("gid") String gid, @Param("condition") String condition)
			throws Exception;

	public List<GoodsInfoEntity> selectGoodsInfo(@Param("gid") String gid) throws Exception;

	public GoodsDeatilEntity selectGoodsDetail(@Param("gid") String gid) throws Exception;

	public boolean updateStock(@Param("skuid") String skuid, @Param("count") String count) throws Exception;

	public List<GoodsSkuEntity> selectGoodsStcokBySkuIds(@Param("skuids") List<String> skuids) throws Exception;

}
