package com.jingyunbank.etrade.vip.coupon.service.handler;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.vip.coupon.bo.BaseCoupon;
import com.jingyunbank.etrade.api.vip.coupon.bo.DiscountCoupon;
import com.jingyunbank.etrade.api.vip.coupon.bo.UserDiscountCoupon;
import com.jingyunbank.etrade.api.vip.coupon.handler.ICouponStrategyService;
import com.jingyunbank.etrade.api.vip.coupon.service.IDiscountCouponService;
import com.jingyunbank.etrade.api.vip.coupon.service.IUserDiscountCouponService;

@Service(BaseCoupon.DISCOUNTCOUPONSTRATEGY)
public class DiscountCouponStrategyService implements ICouponStrategyService {

	@Autowired
	private IDiscountCouponService discountCouponService;
	@Autowired
	private IUserDiscountCouponService userDiscountCouponService;
	
	@Override
	public boolean support(BaseCoupon coupon) {
		return coupon instanceof DiscountCoupon;
	}

	@Override
	public Result<BigDecimal> calculate(String UID, String couponID,
			BigDecimal originprice) throws UnsupportedOperationException {
		Result<UserDiscountCoupon> r = userDiscountCouponService.canConsume(couponID, UID, originprice);
		if(r.isBad()) return Result.fail(r.getMessage());
		DiscountCoupon coupon = r.getBody().getDiscountCoupon();
		BigDecimal discount = coupon.getDiscount();
		BigDecimal discountprice = originprice.multiply(discount).setScale(2, RoundingMode.HALF_UP);
		discountprice = discountprice.compareTo(coupon.getValue())<0?discountprice: coupon.getValue();
		return Result.ok(originprice.compareTo(coupon.getThreshhold()) >= 0? originprice.subtract(discountprice): originprice);
	}

	@Override
	public void consume(String UID, String couponID)
			throws DataRefreshingException {
		userDiscountCouponService.consume(couponID, UID);
	}

	@Override
	public void lock(String UID, String couponID)
			throws DataRefreshingException {
		userDiscountCouponService.lock(couponID, UID);
	}

	@Override
	public void unlock(String UID, String couponID)
			throws DataRefreshingException {
		userDiscountCouponService.unlock(couponID, UID);
	}
}
