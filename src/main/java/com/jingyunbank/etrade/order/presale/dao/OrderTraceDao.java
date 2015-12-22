package com.jingyunbank.etrade.order.presale.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.order.presale.entity.OrderTraceEntity;

public interface OrderTraceDao {

	public void insertMany(@Param("traces") List<OrderTraceEntity> traces) throws Exception;

	public List<OrderTraceEntity> selectMany(String oid);
}
