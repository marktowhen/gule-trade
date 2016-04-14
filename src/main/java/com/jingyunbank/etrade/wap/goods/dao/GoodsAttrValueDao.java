package com.jingyunbank.etrade.wap.goods.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.wap.goods.entity.GoodsAttrValueEntity;

/**
 * 
 * Title: GoodsAttrValueDao
 * 
 * @author duanxf
 * @date 2016年4月12日
 */
public interface GoodsAttrValueDao {

	public void insertGoodsAttrValue(GoodsAttrValueEntity entity) throws Exception;

	public void deleteGoodsAttrValue(@Param("gid") String gid) throws Exception;

	public List<GoodsAttrValueEntity> selectGoodsAttrValue(@Param("gid") String gid) throws Exception;
}
