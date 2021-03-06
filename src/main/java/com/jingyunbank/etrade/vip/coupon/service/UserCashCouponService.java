package com.jingyunbank.etrade.vip.coupon.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
import com.jingyunbank.etrade.api.vip.coupon.bo.CashCoupon;
import com.jingyunbank.etrade.api.vip.coupon.bo.UserCashCoupon;
import com.jingyunbank.etrade.api.vip.coupon.service.ICashCouponService;
import com.jingyunbank.etrade.api.vip.coupon.service.IUserCashCouponService;
import com.jingyunbank.etrade.vip.coupon.dao.UserCashCouponDao;
import com.jingyunbank.etrade.vip.coupon.entity.CashCouponEntity;
import com.jingyunbank.etrade.vip.coupon.entity.UserCashCouponEntity;

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
		
		try {
			return userCashCouponDao.updateConsumeStatus(couponId, uid);
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
			return Result.fail("该券未找到,请选择其他优惠券");
		}
		if(entity.isConsumed()){
			return  Result.fail("该券已消费,请选择其他优惠券");
		}
		
		if(entity.isLocked()){
			return  Result.fail("该券已被其他订单使用,请选择其他优惠券");
		}
		CashCouponEntity cashCoupon = entity.getCashCoupon();
		if(cashCoupon==null){
			return Result.fail("数据错误,请选择其他优惠券");
		}
		if(cashCoupon.isDel()){
			return  Result.fail("该券已作废,请选择其他优惠券");
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date nowDate = new Date();
		if(cashCoupon.getStart()!=null && cashCoupon.getStart().after(nowDate)){
			return Result.fail("请在"+format.format(nowDate)+"之后使用");
		}
		if(cashCoupon.getEnd()!=null && cashCoupon.getEnd().before(nowDate)){
			return Result.fail("该券已过期,请使用其他优惠券");
		}
		if(orderPrice==null || orderPrice.compareTo(cashCoupon.getThreshhold())==-1){
			return Result.fail("未到使用门槛:￥"+cashCoupon.getThreshhold().doubleValue());
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
		return userCashCouponDao.selectUseableCoupon(uid, null, offset, size)
			.stream().map( entityResul ->{return getBoFromEntity(entityResul);})
			.collect(Collectors.toList());
	}
	
	@Override
	public List<UserCashCoupon> listUseableCoupon(String uid, BigDecimal orderPrice, Range range) {
		long offset = 0L;
		long size = 0L;
		if(range!=null){
			offset = range.getFrom();
			size = range.getTo()-range.getFrom();
		}
		return userCashCouponDao.selectUseableCoupon(uid,orderPrice, offset, size)
			.stream().map( entityResul ->{return getBoFromEntity(entityResul);})
			.collect(Collectors.toList());
	}

	@Override
	public boolean isLocked(String couponID) {
		Optional<UserCashCoupon> single = single(couponID);
		if(single.isPresent() && !single.get().isLocked()){
			return true;
		}
		return false;
	}

	@Override
	public boolean lock(String couponID, String uid) throws DataRefreshingException {
		return userCashCouponDao.updateLockedStatus(couponID,uid, true);
	}

	@Override
	public boolean unlock(String couponID, String uid) throws DataRefreshingException {
		return userCashCouponDao.updateLockedStatus(couponID,uid, false);
	}

	@Override
	public Optional<UserCashCoupon> single(String couponID, String uid) {
		UserCashCouponEntity entity = userCashCouponDao.selectUserCashCoupon(couponID, uid);
		if(entity!=null){
			return Optional.of(getBoFromEntity(entity));
		}
		return Optional.empty();
	}

	@Override
	public Optional<UserCashCoupon> single(String couponID) {
		return single(couponID, null);
	}

	



	

	

}
