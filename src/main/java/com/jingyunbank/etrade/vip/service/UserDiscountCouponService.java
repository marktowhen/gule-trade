package com.jingyunbank.etrade.vip.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.vip.service.IUserDiscountCouponService;
import com.jingyunbank.etrade.vip.dao.UserDiscountCouponDao;

@Service("userDiscountCouponService")
public class UserDiscountCouponService implements IUserDiscountCouponService {
	
	@Autowired
	private UserDiscountCouponDao userDiscountCouponDao ;

}
