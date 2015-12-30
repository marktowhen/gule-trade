package com.jingyunbank.etrade.order.postsale.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.api.order.postsale.bo.RefundStatusDesc;
import com.jingyunbank.etrade.order.postsale.entity.RefundEntity;

public interface RefundDao {

	public void insertOne(RefundEntity refund) throws Exception;

	public void update(RefundEntity entity) throws Exception;
	
	public void updateStatus(@Param("RIDs")List<String> rIDs, @Param("status") RefundStatusDesc status) throws Exception;

	public RefundEntity selectOne(String rid);

	public List<RefundEntity> selectmWithCondition(
			@Param("mid") String mid, 
			@Param("statuscode") String statuscode,
			@Param("fromdate") String fromdate,
			@Param("keywords") String keywords,
			@Param("from") long from, 
			@Param("size") int size);
	
	public List<RefundEntity> selectWithCondition(
			@Param("uid") String uid, 
			@Param("statuscode") String statuscode,
			@Param("fromdate") String fromdate,
			@Param("keywords") String keywords,
			@Param("from") long from, 
			@Param("size") int size);

	/**
	 * 按照ogid查找最新的退款明细
	 * @param ogid
	 * @return
	 */
	public RefundEntity selectOneByOGID(String ogid);

	public List<RefundEntity> selectByRIDs(@Param("rids")List<String> rids);

	public List<RefundEntity> selectBefore(@Param("deadline")Date deadline, @Param("statuscode")String code);
	
}
