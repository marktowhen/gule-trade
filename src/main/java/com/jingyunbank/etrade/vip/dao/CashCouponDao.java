package com.jingyunbank.etrade.vip.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.vip.entity.CashCouponEntity;

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
	 * 查询
	 * @param entity
	 * @return
	 * 2015年12月9日 qxs
	 */
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


	/**
	 * 查询数量
	 * @param entityFromBo
	 * @return
	 * 2015年11月19日 qxs
	 */
	public int selectAmount(CashCouponEntity entity);


	/**
	 * 单个查询
	 * @param key 编码或id
	 * @return
	 * 2015年11月20日 qxs
	 */
	public CashCouponEntity selectSingle(String key);


	
}
