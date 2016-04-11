package com.jingyunbank.etrade.wap.goods.dao;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.wap.goods.entity.GoodsInfoEntity;

/**
 * 
 * Title: WapGoodsOperationDao 商品操作
 * 
 * @author duanxf
 * @date 2016年4月11日
 */
public interface WapGoodsInfoOperationDao {

	void insertGoodsInfo(GoodsInfoEntity entity) throws Exception;

	void deleteGoodsInfo(@Param("gid") String gid) throws Exception;

	void deleteGoodsInfoById( @Param("infoId") String infoId) throws Exception;

}
