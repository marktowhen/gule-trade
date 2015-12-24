package com.jingyunbank.etrade.vip.coupon.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.etrade.api.vip.coupon.handler.ICouponStrategyResolver;
import com.jingyunbank.etrade.api.vip.coupon.handler.ICouponStrategyService;

@RestController
public class CouponController {

	@Autowired
	private ICouponStrategyResolver couponStrategyResolver;
	
	@AuthBeforeOperation
	@RequestMapping(value="/api/vip/coupon/calculation", method=RequestMethod.GET)
	public Result<BigDecimal> calculate(@RequestParam(value="cid", required=true) String couponID,
						@RequestParam(value="type", required=true) String type,
						@RequestParam(value="uid", required=true) String UID, 
						@RequestParam(value="price", required=true) BigDecimal price) throws Exception{
		ICouponStrategyService service = couponStrategyResolver.resolve(type);
		Result<BigDecimal> result = service.calculate(UID, couponID, price);
		return result;
	}
	
}
