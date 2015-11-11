package com.jingyunbank.etrade.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.order.entity.OrderGoodsEntity;

public interface OrderGoodsDao {

	public void insertMany(@Param("goods") List<OrderGoodsEntity> goods) throws Exception;
	
}
