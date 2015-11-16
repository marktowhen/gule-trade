package com.jingyunbank.etrade.vip.service.context;

import java.math.BigDecimal;

import com.jingyunbank.etrade.api.vip.bo.BaseCoupon;
import com.jingyunbank.etrade.api.vip.bo.CashCoupon;
import com.jingyunbank.etrade.api.vip.service.context.ICouponStrategyService;

public class CashCouponStrategyService  implements ICouponStrategyService {
	

	@Override
	public BigDecimal calculate(BigDecimal originCost, BaseCoupon coupon)  throws UnsupportedOperationException {
		if(! (coupon instanceof CashCoupon)) throw new UnsupportedOperationException("不支持的优惠活动。");
		CashCoupon cc = (CashCoupon)coupon;
		return originCost.compareTo(cc.getThreshhold()) >= 0? originCost.subtract(cc.getValue()): originCost;
	}

	@Override
	public boolean support(BaseCoupon coupon) {
		return coupon instanceof CashCoupon;
	}


}
