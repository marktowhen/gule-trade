package com.jingyunbank.etrade.vip.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.vip.bo.CashCoupon;
import com.jingyunbank.etrade.api.vip.service.ICashCouponService;
import com.jingyunbank.etrade.vip.dao.CashCouponDao;

@Service("cashCouponService")
public class CashCouponService implements ICashCouponService{

	@Autowired
	private CashCouponDao cashCouponDao;

	@Override
	public boolean save(CashCoupon cashCoupon, Users manager) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(CashCoupon cashCoupon, Users manager) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValid(String code) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CashCoupon getSingle(CashCoupon cashCoupon) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CashCoupon> listAll(CashCoupon cashCoupon) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CashCoupon> listAll(CashCoupon cashCoupon, Range range) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CashCoupon> listValid(CashCoupon cashCoupon) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CashCoupon> listValid(CashCoupon cashCoupon, Range range) {
		// TODO Auto-generated method stub
		return null;
	}
}
