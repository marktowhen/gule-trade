package com.jingyunbank.etrade.vip.dao;

import java.util.List;

import com.jingyunbank.etrade.vip.entity.DiscountCouponEntity;

public interface DiscountCouponDao {
	
	public boolean insert(DiscountCouponEntity entity) throws Exception;
	
	
	public List<DiscountCouponEntity> selectList(DiscountCouponEntity entity); 
	
	/**
	 * 根据id或code获取单条详情
	 * @param key
	 * @return
	 * 2015年11月20日 qxs
	 */
	public DiscountCouponEntity getSingleByKey(String key);
	
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
	
	/**
	 * 查询数量
	 * @param entityFromBo
	 * @return
	 * 2015年11月19日 qxs
	 */
	public int selectAmount(DiscountCouponEntity entity);

}
