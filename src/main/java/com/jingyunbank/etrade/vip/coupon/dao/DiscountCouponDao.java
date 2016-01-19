package com.jingyunbank.etrade.vip.coupon.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.vip.coupon.entity.DiscountCouponEntity;

public interface DiscountCouponDao {
	
	public boolean insert(DiscountCouponEntity entity) throws Exception;
	
	public boolean insertMuti(List<DiscountCouponEntity> list) throws Exception;
	
	public List<DiscountCouponEntity> selectList(DiscountCouponEntity entity); 
	
	/**
	 * 根据id或code获取单条详情
	 * @param key
	 * @return
	 * 2015年11月20日 qxs
	 */
	public DiscountCouponEntity selectSingleByKey(String key);
	
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
	
	public List<DiscountCouponEntity> selectByAddtime(
			@Param(value="from") Date addTimeFrom,
			@Param(value="to") Date addTimeTo,
			@Param(value="offset") long offset, 
			@Param(value="size") long size); 
	
	public int countByAddtime(@Param(value="from") Date addTimeFrom,@Param(value="to") Date addTimeTo);


	/**
	 * 根据卡号模糊查询
	 * @param cardNum
	 * @param offset
	 * @param size
	 * @return
	 * 2015年12月29日 qxs
	 */
	public List<DiscountCouponEntity> selectListByCardNum(
			@Param(value="cardNum") String cardNum,
			@Param(value="offset") long offset, 
			@Param(value="size") long size); 
	
	
	/**
	 * 列表
	 * @param entity
	 * @param offset
	 * @param size
	 * @return
	 * 2015年12月29日 qxs
	 */
	public List<DiscountCouponEntity> selectList(
			@Param(value="entity") DiscountCouponEntity entity,
			@Param(value="offset") long offset, 
			@Param(value="size") long size); 

	/**
	 * 数量
	 * @param entity
	 * @return
	 * 2015年12月29日 qxs
	 */
	public int count(@Param(value="entity") DiscountCouponEntity entity);

	/**
	 * 修改锁定状态
	 * @param ids
	 * @param locked
	 * @return
	 * 2015年12月29日 qxs
	 */
	public boolean updateLocked(@Param(value="ids")String[] ids,@Param(value="locked") boolean locked); 


}
