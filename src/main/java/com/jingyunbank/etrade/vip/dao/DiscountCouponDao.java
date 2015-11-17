package com.jingyunbank.etrade.vip.dao;

import java.util.List;

import com.jingyunbank.etrade.vip.entity.DiscountCouponEntity;

public interface DiscountCouponDao {
	
	public boolean insert(DiscountCouponEntity entity) throws Exception;
	
	
	public List<DiscountCouponEntity> selectList(DiscountCouponEntity entity); 
	
	
	/**
	 * 修改删除状态
	 * @param entity
	 * @return
	 * 2015年11月16日 qxs
	 */
	public boolean updateDeleteStatus(DiscountCouponEntity entity) throws Exception;
	
	/**
	 * 置为已使用
	 * @param entity
	 * @return
	 * 2015年11月16日 qxs
	 */
	public boolean updateUsedStatus(DiscountCouponEntity entity) throws Exception;

}
