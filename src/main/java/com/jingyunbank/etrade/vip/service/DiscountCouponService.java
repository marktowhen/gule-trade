package com.jingyunbank.etrade.vip.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.vip.bo.DiscountCoupon;
import com.jingyunbank.etrade.api.vip.service.IDiscountCouponService;
import com.jingyunbank.etrade.vip.dao.DiscountCouponDao;

@Service("discountCouponService")
public class DiscountCouponService implements IDiscountCouponService{

	@Autowired
	private DiscountCouponDao discountCouponDao;

	@Override
	public boolean save(DiscountCoupon discountCoupon, Users manager) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(DiscountCoupon discountCoupon, Users manager) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValid(String code) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DiscountCoupon getSingle(DiscountCoupon discountCoupon) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DiscountCoupon> listAll(DiscountCoupon discountCoupon) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DiscountCoupon> listAll(DiscountCoupon discountCoupon,
			Range range) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DiscountCoupon> listValid(DiscountCoupon discountCoupon) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DiscountCoupon> listValid(DiscountCoupon discountCoupon,
			Range range) {
		// TODO Auto-generated method stub
		return null;
	}
}
