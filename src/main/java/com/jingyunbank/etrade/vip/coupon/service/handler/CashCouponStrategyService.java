package com.jingyunbank.etrade.vip.coupon.service.handler;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.vip.coupon.bo.BaseCoupon;
import com.jingyunbank.etrade.api.vip.coupon.bo.CashCoupon;
import com.jingyunbank.etrade.api.vip.coupon.bo.UserCashCoupon;
import com.jingyunbank.etrade.api.vip.coupon.handler.ICouponStrategyService;
import com.jingyunbank.etrade.api.vip.coupon.service.ICashCouponService;
import com.jingyunbank.etrade.api.vip.coupon.service.IUserCashCouponService;

@Service(BaseCoupon.CASHCOUPONSTRATEGY)
public class CashCouponStrategyService  implements ICouponStrategyService {
	
	@Autowired
	private ICashCouponService cashCouponService;
	@Autowired
	private IUserCashCouponService userCashCouponService;

	@Override
	public boolean support(BaseCoupon coupon) {
		return coupon instanceof CashCoupon;
	}

	@Override
	public Result<BigDecimal> calculate(String UID, String couponID, BigDecimal originprice)
			throws UnsupportedOperationException {
		Result<UserCashCoupon> r = userCashCouponService.canConsume(couponID, UID, originprice);
		if(r.isBad()) return Result.fail(r.getMessage());
		CashCoupon coupon = r.getBody().getCashCoupon();
		return Result.ok(originprice.compareTo(coupon.getThreshhold()) >= 0? originprice.subtract(coupon.getValue()): originprice);
	}

	@Override
	public void consume(String UID, String couponID) throws DataRefreshingException {
		userCashCouponService.consume(couponID, UID);
	}

	@Override
	public void lock(String UID, String couponID)
			throws DataRefreshingException {
		userCashCouponService.lock(couponID, UID);
	}

	@Override
	public void unlock(String UID, String couponID)
			throws DataRefreshingException {
		userCashCouponService.unlock(couponID, UID);
	}


}
