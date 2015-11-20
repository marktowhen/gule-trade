package com.jingyunbank.etrade.vip.dao;

import java.util.List;

import com.jingyunbank.etrade.vip.entity.UserDiscountCouponEntity;

public interface UserDiscountCouponDao {

	boolean insert(UserDiscountCouponEntity entityFromBo) throws Exception;

	List<UserDiscountCouponEntity> getUnusedCoupon(UserDiscountCouponEntity entity);

	boolean updateConsumeStatus(UserDiscountCouponEntity entity) throws Exception;

	int getUnusedCouponAmount(UserDiscountCouponEntity entityFromBo);

	UserDiscountCouponEntity getUserDiscountCoupon(String couponID, String uid);

}
