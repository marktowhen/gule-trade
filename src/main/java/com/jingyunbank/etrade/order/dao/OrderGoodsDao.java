package com.jingyunbank.etrade.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.api.order.bo.OrderStatusDesc;
import com.jingyunbank.etrade.order.entity.OrderGoodsEntity;

public interface OrderGoodsDao {

	public void insertMany(@Param("goods") List<OrderGoodsEntity> goods) throws Exception;
	

	public List<OrderGoodsEntity> selectByUID(@Param(value="UID") String uid,@Param("status") OrderStatusDesc status);/*,@Param("status") OrderStatusDesc status*/
		
	public OrderGoodsEntity selectByGID(@Param(value="OID") String oid);
}
