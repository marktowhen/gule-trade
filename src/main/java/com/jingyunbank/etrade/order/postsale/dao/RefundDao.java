package com.jingyunbank.etrade.order.postsale.dao;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.api.order.postsale.bo.RefundStatusDesc;
import com.jingyunbank.etrade.order.postsale.entity.RefundEntity;

public interface RefundDao {

	public void insertOne(RefundEntity refund) throws Exception;

	public void updateStatus(@Param("RID")String rID, @Param("status") RefundStatusDesc status) throws Exception;

	public RefundEntity selectOne(String rid);

	
}
