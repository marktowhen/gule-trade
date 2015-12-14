package com.jingyunbank.etrade.vip.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.vip.entity.UserDiscountCouponEntity;

public interface UserDiscountCouponDao {

	boolean insert(UserDiscountCouponEntity entityFromBo) throws Exception;

	boolean updateConsumeStatus(UserDiscountCouponEntity entity) throws Exception;

	UserDiscountCouponEntity selectUserDiscountCoupon(@Param(value="couponID")String couponID,@Param(value="UID") String uid);

	/**
	 * 未消费未过期的数量
	 * @param uid
	 * @return
	 * 2015年12月3日 qxs
	 */
	int countUnusedCoupon(@Param(value="UID") String uid);
	/**
	 * 未消费未过期的列表
	 * @param uid
	 * @param offset
	 * @param size
	 * @return
	 * 2015年12月4日 qxs
	 */
	List<UserDiscountCouponEntity> selectUnusedCoupon(@Param(value="UID") String uid ,@Param(value="offset") long offset, @Param(value="size") long size);

	/**
	 * 已消费的数量
	 * @param uid
	 * @return
	 * 2015年12月3日 qxs
	 */
	int countConsumedCoupon(@Param(value="UID")String uid);
	/**
	 * 已消费的列表
	 * @param uid
	 * @param offset
	 * @param size
	 * @return
	 * 2015年12月3日 qxs
	 */
	List<UserDiscountCouponEntity> selectConsumedCoupon(@Param(value="UID")String uid,@Param(value="offset") long offset, @Param(value="size") long size);
	
	/**
	 * 已过期的数量
	 * @param uid
	 * @return
	 * 2015年12月3日 qxs
	 */
	int countOverdueCoupon(@Param(value="UID")String uid);
	/**
	 * 已过期的列表
	 * @param uid
	 * @param offset
	 * @param size
	 * @return
	 * 2015年12月3日 qxs
	 */
	List<UserDiscountCouponEntity> selectOverdueCoupon(@Param(value="UID")String uid,@Param(value="offset") long offset, @Param(value="size") long size);
	
	/**
	 * 当前可用
	 * @param uid
	 * @return
	 * 2015年12月3日 qxs
	 */
	int countUseableCoupon(@Param(value="UID")String uid);

	/**
	 * 当前可用的列表
	 * @param uid
	 * @param offset
	 * @param size
	 * @return
	 * 2015年12月3日 qxs
	 */
	List<UserDiscountCouponEntity> selectUseableCoupon(@Param(value="UID")String uid,@Param(value="offset") long offset, @Param(value="size") long size);
	
}
