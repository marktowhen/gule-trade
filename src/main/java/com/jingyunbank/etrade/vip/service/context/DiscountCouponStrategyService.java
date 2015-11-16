package com.jingyunbank.etrade.vip.service.context;

import java.math.BigDecimal;

import com.jingyunbank.etrade.api.vip.bo.BaseCoupon;
import com.jingyunbank.etrade.api.vip.bo.DiscountCoupon;
import com.jingyunbank.etrade.api.vip.service.context.ICouponStrategyService;

public class DiscountCouponStrategyService implements ICouponStrategyService {
	

	@Override
	public BigDecimal calculate(BigDecimal originCost, BaseCoupon coupon) {
		if(! (coupon instanceof DiscountCoupon)) throw new UnsupportedOperationException("不支持的优惠活动。");
		DiscountCoupon cc = (DiscountCoupon)coupon;
		return originCost.compareTo(cc.getThreshhold()) >= 0? 
				originCost.subtract(originCost.multiply(cc.getDiscount())): 
					originCost;
	}

	@Override
	public boolean support(BaseCoupon coupon) {
		return coupon instanceof DiscountCoupon;
	}


}
