package com.jingyunbank.etrade.vip.service.handler;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.vip.bo.BaseCoupon;
import com.jingyunbank.etrade.api.vip.bo.DiscountCoupon;
import com.jingyunbank.etrade.api.vip.bo.UserDiscountCoupon;
import com.jingyunbank.etrade.api.vip.handler.ICouponStrategyService;
import com.jingyunbank.etrade.api.vip.service.IDiscountCouponService;
import com.jingyunbank.etrade.api.vip.service.IUserDiscountCouponService;

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
		BigDecimal discountprice = originprice.multiply(discount);
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
