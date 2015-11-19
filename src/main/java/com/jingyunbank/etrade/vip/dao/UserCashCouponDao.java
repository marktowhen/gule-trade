package com.jingyunbank.etrade.vip.dao;

import java.util.List;

import com.jingyunbank.etrade.api.vip.bo.UserCashCoupon;
import com.jingyunbank.etrade.vip.entity.UserCashCouponEntity;

public interface UserCashCouponDao {

	boolean insert(UserCashCoupon userCashCoupon) throws Exception;
	
	List<UserCashCouponEntity> getUnusedCoupon(UserCashCouponEntity entity);

	boolean updateConsumeStatus(UserCashCouponEntity entity) throws Exception;

	int getUnusedCouponAmount(UserCashCouponEntity entityFromBo);

}
