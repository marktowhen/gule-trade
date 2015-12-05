package com.jingyunbank.etrade.vip.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.vip.entity.UserDiscountCouponEntity;

public interface UserDiscountCouponDao {

	boolean insert(UserDiscountCouponEntity entityFromBo) throws Exception;

	boolean updateConsumeStatus(UserDiscountCouponEntity entity) throws Exception;

	UserDiscountCouponEntity getUserDiscountCoupon(@Param(value="couponID")String couponID,@Param(value="UID") String uid);

	/**
	 * 未消费未过期的数量
	 * @param uid
	 * @return
	 * 2015年12月3日 qxs
	 */
	int getUnusedCouponAmount(@Param(value="UID") String uid);
	/**
	 * 未消费未过期的列表
	 * @param uid
	 * @param offset
	 * @param size
	 * @return
	 * 2015年12月4日 qxs
	 */
	List<UserDiscountCouponEntity> getUnusedCoupon(@Param(value="UID") String uid ,@Param(value="offset") long offset, @Param(value="size") long size);

	/**
	 * 已消费的数量
	 * @param uid
	 * @return
	 * 2015年12月3日 qxs
	 */
	int getConsumedCouponAmount(@Param(value="UID")String uid);
	/**
	 * 已消费的列表
	 * @param uid
	 * @param offset
	 * @param size
	 * @return
	 * 2015年12月3日 qxs
	 */
	List<UserDiscountCouponEntity> getConsumedCoupon(@Param(value="UID")String uid,@Param(value="offset") long offset, @Param(value="size") long size);
	
	/**
	 * 已过期的数量
	 * @param uid
	 * @return
	 * 2015年12月3日 qxs
	 */
	int getOverdueCouponAmount(@Param(value="UID")String uid);
	/**
	 * 已过期的列表
	 * @param uid
	 * @param offset
	 * @param size
	 * @return
	 * 2015年12月3日 qxs
	 */
	List<UserDiscountCouponEntity> getOverdueCoupon(@Param(value="UID")String uid,@Param(value="offset") long offset, @Param(value="size") long size);
	
	/**
	 * 当前可用
	 * @param uid
	 * @return
	 * 2015年12月3日 qxs
	 */
	int getUseableCouponAmount(@Param(value="UID")String uid);

	/**
	 * 当前可用的列表
	 * @param uid
	 * @param offset
	 * @param size
	 * @return
	 * 2015年12月3日 qxs
	 */
	List<UserDiscountCouponEntity> getUseableCoupon(@Param(value="UID")String uid,@Param(value="offset") long offset, @Param(value="size") long size);
	
}
