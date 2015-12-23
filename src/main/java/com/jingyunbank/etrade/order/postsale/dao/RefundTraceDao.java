package com.jingyunbank.etrade.order.postsale.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.order.postsale.entity.RefundTraceEntity;

public interface RefundTraceDao {
	
	public void insertMany(@Param("traces") List<RefundTraceEntity> traces) throws Exception;

	public List<RefundTraceEntity> selectMany(String rid);
	
	public List<RefundTraceEntity> selectWithStatus(@Param("rid")String rid, @Param("status")String status);
}
