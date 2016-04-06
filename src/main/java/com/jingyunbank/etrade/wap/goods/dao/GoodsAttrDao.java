package com.jingyunbank.etrade.wap.goods.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.wap.goods.entity.GoodsAttrEntity;

/**
 * 
 * Title: GoodsAttrDao 商品属性dao
 * 
 * @author duanxf
 * @date 2016年3月31日
 */
public interface GoodsAttrDao {

	void insertGoodsAttr(GoodsAttrEntity attrEntity) throws Exception;

	GoodsAttrEntity selectOne(@Param("aid") String aid) throws Exception;

	void update(GoodsAttrEntity attrEntity) throws Exception;

	void delete(@Param("aid") String aid) throws Exception;

	List<GoodsAttrEntity> select() throws Exception;

}
