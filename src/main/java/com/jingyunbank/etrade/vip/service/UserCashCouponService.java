package com.jingyunbank.etrade.vip.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.vip.bo.CashCoupon;
import com.jingyunbank.etrade.api.vip.bo.UserCashCoupon;
import com.jingyunbank.etrade.api.vip.service.ICashCouponService;
import com.jingyunbank.etrade.api.vip.service.IUserCashCouponService;
import com.jingyunbank.etrade.base.util.EtradeUtil;
import com.jingyunbank.etrade.vip.dao.UserCashCouponDao;
import com.jingyunbank.etrade.vip.entity.CashCouponEntity;
import com.jingyunbank.etrade.vip.entity.UserCashCouponEntity;

@Service("userCashCouponService")
public class UserCashCouponService  implements IUserCashCouponService {

	@Autowired
	private UserCashCouponDao userCashCouponDao;
	@Autowired
	private ICashCouponService cashCouponService;

	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean active(String code, String UID) throws DataRefreshingException, DataSavingException {
		CashCoupon cashCoupon = cashCouponService.singleByCode(code);
		//插入User_Cash_Coupon
		UserCashCoupon userCoupon = new UserCashCoupon();
		userCoupon.setID(KeyGen.uuid());
		userCoupon.setUID(UID);
		userCoupon.setCouponID(cashCoupon.getID());
		save(userCoupon);
		//更改Coupon状态
		cashCouponService.activeCoupon(code);
		return true;
	}

	@Override
	public boolean active(CashCoupon cashCoupon, Users user) throws DataRefreshingException, DataSavingException {
		return active(cashCoupon.getCode(), user.getID());
	}

	@Override
	public List<UserCashCoupon> listUnusedCoupon(String uid, Range range) {
		long offset = 0L;
		long size = 0L;
		if(range!=null){
			offset = range.getFrom();
			size = range.getTo()-range.getFrom();
		}
		return userCashCouponDao.selectUnusedCoupon(uid, offset, size)
			.stream().map( entityResul ->{return getBoFromEntity(entityResul);})
			.collect(Collectors.toList());
	}
	
	@Override
	public int countUnusedCoupon(String uid) {
		return userCashCouponDao.countUnusedCoupon(uid);
	}
	
	private boolean save(UserCashCoupon userCashCoupon) throws DataSavingException{
		try {
			return userCashCouponDao.insert(userCashCoupon);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}
	
	@Override
	public boolean consume(String couponId, String uid) throws DataRefreshingException {
		
		UserCashCouponEntity entity = new UserCashCouponEntity();
		entity.setCouponID(couponId);
		try {
			return userCashCouponDao.updateConsumeStatus(entity);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}
	
	
	private UserCashCoupon getBoFromEntity(UserCashCouponEntity entity){
		if(entity!=null){
			UserCashCoupon bo = new UserCashCoupon();
			BeanUtils.copyProperties(entity, bo);
			if(entity.getCashCoupon()!=null){
				CashCoupon cBo = new CashCoupon();
				BeanUtils.copyProperties(entity.getCashCoupon(), cBo);
				bo.setCashCoupon(cBo);
			}
			return bo;
		}
		return null;
	}


	@Override
	public Result<UserCashCoupon> canConsume(String couponId, String uid, BigDecimal orderPrice) {
		UserCashCouponEntity entity =  userCashCouponDao.selectUserCashCoupon(couponId,  uid);
		if(entity==null){
			return Result.fail("未找到");
		}
		CashCouponEntity cashCoupon = entity.getCashCoupon();
		if(cashCoupon==null){
			return Result.fail("数据错误");
		}
		if(entity.isConsumed()){
			return  Result.fail("该券已消费");
		}
		if(cashCoupon.isDel()){
			return  Result.fail("该券已被删除");
		}
		Date nowDate = EtradeUtil.getNowDate();
		if(cashCoupon.getStart().after(nowDate)){
			return Result.fail("未到使用时间");
		}
		if(cashCoupon.getEnd().before(nowDate)){
			return Result.fail("已失效");
		}
		if(orderPrice==null || orderPrice.compareTo(cashCoupon.getThreshhold())==-1){
			return Result.fail("未到使用门槛:"+cashCoupon.getThreshhold().doubleValue());
		}
		
		return Result.ok(getBoFromEntity(entity));
	}

	@Override
	public int countConsumedCoupon(String uid) {
		return userCashCouponDao.countConsumedCoupon(uid);
	}

	@Override
	public List<UserCashCoupon> listConsumedCoupon(String uid, Range range) {
		long offset = 0L;
		long size = 0L;
		if(range!=null){
			offset = range.getFrom();
			size = range.getTo()-range.getFrom();
		}
		return userCashCouponDao.selectConsumedCoupon(uid, offset, size)
			.stream().map( entityResul ->{return getBoFromEntity(entityResul);})
			.collect(Collectors.toList());
	}

	@Override
	public int countOverdueCoupon(String uid) {
		return userCashCouponDao.countOverdueCoupon(uid);
	}

	@Override
	public List<UserCashCoupon> listOverdueCoupon(String uid, Range range) {
		long offset = 0L;
		long size = 0L;
		if(range!=null){
			offset = range.getFrom();
			size = range.getTo()-range.getFrom();
		}
		return userCashCouponDao.selectOverdueCoupon(uid, offset, size)
			.stream().map( entityResul ->{return getBoFromEntity(entityResul);})
			.collect(Collectors.toList());
	}

	@Override
	public int countUseableCoupon(String uid) {
		return userCashCouponDao.countUseableCoupon(uid);
	}

	@Override
	public List<UserCashCoupon> listUseableCoupon(String uid, Range range) {
		long offset = 0L;
		long size = 0L;
		if(range!=null){
			offset = range.getFrom();
			size = range.getTo()-range.getFrom();
		}
		return userCashCouponDao.selectUseableCoupon(uid, offset, size)
			.stream().map( entityResul ->{return getBoFromEntity(entityResul);})
			.collect(Collectors.toList());
	}

	

	

}
