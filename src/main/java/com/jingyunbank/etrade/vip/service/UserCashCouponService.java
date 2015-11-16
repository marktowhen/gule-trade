package com.jingyunbank.etrade.vip.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.vip.bo.CashCoupon;
import com.jingyunbank.etrade.api.vip.service.IUserCashCouponService;
import com.jingyunbank.etrade.vip.dao.UserCashCouponDao;

@Service("userCashCouponService")
public class UserCashCouponService  implements IUserCashCouponService {

	@Autowired
	private UserCashCouponDao userCashCouponDao;

	@Override
	public boolean active(String code, String UID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean active(CashCoupon cashCoupon, Users user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<CashCoupon> unusedCashCoupon(CashCoupon cashCoupon, Range range) {
		// TODO Auto-generated method stub
		return null;
	}
}
