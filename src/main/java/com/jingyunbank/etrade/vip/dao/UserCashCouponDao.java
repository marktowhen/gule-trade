package com.jingyunbank.etrade.vip.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.api.vip.bo.UserCashCoupon;
import com.jingyunbank.etrade.vip.entity.UserCashCouponEntity;

public interface UserCashCouponDao {

	/**
	 * 新增
	 * @param userCashCoupon
	 * @return
	 * @throws Exception
	 * 2015年12月3日 qxs
	 */
	boolean insert(UserCashCoupon userCashCoupon) throws Exception;
	
	/**
	 * 修改消费状态
	 * @param entity
	 * @return
	 * @throws Exception
	 * 2015年12月3日 qxs
	 */
	boolean updateConsumeStatus(UserCashCouponEntity entity) throws Exception;

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
	List<UserCashCouponEntity> selectUnusedCoupon(@Param(value="UID") String uid ,@Param(value="offset") long offset, @Param(value="size") long size);

	/**
	 * 单个查询
	 * @param couponID
	 * @param uid
	 * @return
	 * 2015年12月3日 qxs
	 */
	UserCashCouponEntity selectUserCashCoupon(@Param(value="couponID")String couponID,@Param(value="UID") String uid);
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
	List<UserCashCouponEntity> selectConsumedCoupon(@Param(value="UID")String uid,@Param(value="offset") long offset, @Param(value="size") long size);
	
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
	List<UserCashCouponEntity> selectOverdueCoupon(@Param(value="UID")String uid,@Param(value="offset") long offset, @Param(value="size") long size);
	
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
	List<UserCashCouponEntity> selectUseableCoupon(@Param(value="UID")String uid,@Param(value="offset") long offset, @Param(value="size") long size);
	

}
