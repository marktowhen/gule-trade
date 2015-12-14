package com.jingyunbank.etrade.vip.service.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.vip.handler.ICouponStrategyResolver;
import com.jingyunbank.etrade.api.vip.handler.ICouponStrategyService;

@Service("couponStrategyResolver")
public class InternCouponStrategyResolver implements ICouponStrategyResolver {
	@Autowired
	private Map<String, ICouponStrategyService> services = new HashMap<String, ICouponStrategyService>();
	
	@Override
	public ICouponStrategyService resolve(String coupontype) throws IllegalArgumentException {
		ICouponStrategyService handler = services.get((coupontype+"Strategy").toUpperCase());
		if(handler == null) 
			throw new IllegalArgumentException("不合法优惠卡券类型！");
		
		return handler;
	}

}
