package com.jingyunbank.etrade.vip.coupon.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.vip.coupon.entity.CashCouponEntity;

public interface CashCouponDao {

	/**
	 * 新增
	 * @param CashCouponEntity entity
	 * @return
	 * @throws Exception
	 * 2015年12月9日 qxs
	 */
	public boolean insert(CashCouponEntity entity) throws Exception;
	
	/**
	 * 多个新增
	 * @param List<CashCouponEntity> list
	 * @return
	 * 2015年12月9日 qxs
	 */
	public boolean insertMuti(@Param(value="list") List<CashCouponEntity> list);
	
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

	/**
	 * 查询数量
	 * @param entityFromBo
	 * @return
	 * 2015年11月19日 qxs
	 */
	public int countByAddTime(@Param(value="from") Date addTimeFrom,@Param(value="to") Date addTimeTo);

	/**
	 * 查询
	 * @param entity
	 * @return
	 * 2015年12月9日 qxs
	 */
	public List<CashCouponEntity> selectListByAddTime(
			@Param(value="from") Date addTimeFrom,
			@Param(value="to") Date addTimeTo,
			@Param(value="offset") long offset, 
			@Param(value="size") long size); 

	/**
	 * 单个查询
	 * @param key 编码或id
	 * @return
	 * 2015年11月20日 qxs
	 */
	public CashCouponEntity selectSingle(String key);
	
	/**
	 * 根据卡号模糊查询
	 * @param cardNum
	 * @param offset
	 * @param size
	 * @return
	 * 2015年12月29日 qxs
	 */
	public List<CashCouponEntity> selectListByCardNum(
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
	public List<CashCouponEntity> selectList(
			@Param(value="entity") CashCouponEntity entity,
			@Param(value="offset") long offset, 
			@Param(value="size") long size); 

	/**
	 * 数量
	 * @param entity
	 * @return
	 * 2015年12月29日 qxs
	 */
	public int count(@Param(value="entity") CashCouponEntity entity);

	/**
	 * 修改锁定状态
	 * @param ids
	 * @param locked
	 * @return
	 * 2015年12月29日 qxs
	 */
	public boolean updateLocked(@Param(value="ids")String[] ids,@Param(value="locked") boolean locked); 



	
}
