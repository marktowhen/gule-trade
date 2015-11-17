package com.jingyunbank.etrade.vip.dao;

import java.util.List;

import com.jingyunbank.etrade.vip.entity.CashCouponEntity;

public interface CashCouponDao {

	public boolean insert(CashCouponEntity entity) throws Exception;
	
	
	public List<CashCouponEntity> selectList(CashCouponEntity entity); 
	
	
	/**
	 * 修改删除状态
	 * @param entity
	 * @return
	 * 2015年11月16日 qxs
	 */
	public boolean updateDeleteStatus(CashCouponEntity entity) throws Exception;
	
	/**
	 * 置为已使用
	 * @param entity
	 * @return
	 * 2015年11月16日 qxs
	 */
	public boolean updateUsedStatus(CashCouponEntity entity) throws Exception;
}
