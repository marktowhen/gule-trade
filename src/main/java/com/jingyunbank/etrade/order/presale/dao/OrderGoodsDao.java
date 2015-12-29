package com.jingyunbank.etrade.order.presale.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.api.order.presale.bo.OrderStatusDesc;
import com.jingyunbank.etrade.order.presale.entity.OrderGoodsEntity;

public interface OrderGoodsDao {

	public void insertMany(@Param("goods") List<OrderGoodsEntity> goods) throws Exception;
	
	public List<OrderGoodsEntity> selectByUID(@Param(value="UID") String uid,@Param("status") OrderStatusDesc status);/*,@Param("status") OrderStatusDesc status*/
		
	public OrderGoodsEntity selectByID(String id);

	public void updateStatus(@Param("oids") List<String> oids, @Param("status") OrderStatusDesc status) throws Exception;
	
	public void updateGoodStatus(@Param("IDs") List<String> ids, @Param("status") OrderStatusDesc status) throws Exception;
	
	public int count(@Param(value="OID") String uid,@Param("status") OrderStatusDesc status);
}
