package com.jingyunbank.etrade.vip.coupon.service;

import java.math.BigDecimal;
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
import com.jingyunbank.etrade.api.vip.coupon.bo.DiscountCoupon;
import com.jingyunbank.etrade.api.vip.coupon.bo.UserDiscountCoupon;
import com.jingyunbank.etrade.api.vip.coupon.service.IDiscountCouponService;
import com.jingyunbank.etrade.api.vip.coupon.service.IUserDiscountCouponService;
import com.jingyunbank.etrade.base.util.EtradeUtil;
import com.jingyunbank.etrade.vip.coupon.dao.UserDiscountCouponDao;
import com.jingyunbank.etrade.vip.coupon.entity.DiscountCouponEntity;
import com.jingyunbank.etrade.vip.coupon.entity.UserDiscountCouponEntity;

@Service("userDiscountCouponService")
public class UserDiscountCouponService implements IUserDiscountCouponService {
	
	@Autowired
	private UserDiscountCouponDao userDiscountCouponDao ;
	@Autowired
	private IDiscountCouponService discountCouponService;

	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean active(String code, String uid) throws DataSavingException, DataRefreshingException {
		DiscountCoupon discountCoupon  = discountCouponService.singleByCode(code);
		if(discountCouponService==null){
			return false;
		}
		//插入关联表
		UserDiscountCoupon userDiscountCoupon = new UserDiscountCoupon();
		userDiscountCoupon.setID(KeyGen.uuid());
		userDiscountCoupon.setUID(uid);
		userDiscountCoupon.setCouponID(discountCoupon.getID());
		save(userDiscountCoupon);
		//更改是体表状态
		discountCouponService.active(code);
		return true;
	}
	
	@Override
	public int countUnusedCoupon(String uid) {
		return userDiscountCouponDao.countUnusedCoupon(uid);
	}
	
	
	@Override
	public List<UserDiscountCoupon> listUnusedCoupon(String uid, Range range) {
		long offset = 0L;
		long size = 0L;
		if(range!=null){
			offset = range.getFrom();
			size = range.getTo()-range.getFrom();
		}
		return userDiscountCouponDao.selectUnusedCoupon(uid, offset, size )
			.stream().map(rEntity->{return getBoFromEntity(rEntity);})
			.collect(Collectors.toList());
	}
	
	@Override
	public Result<UserDiscountCoupon> canConsume(String couponId, String uid, BigDecimal orderPrice) {
		UserDiscountCouponEntity entity =  userDiscountCouponDao.selectUserDiscountCoupon(couponId,  uid);
		if(entity==null){
			return Result.fail("未找到");
		}
		if(entity.isConsumed()){
			return  Result.fail("该券已消费");
		}
		if(entity.isLocked()){
			return  Result.fail("该券已锁定");
		}
		DiscountCouponEntity discountCoupon = entity.getDiscountCouponEntity();
		if(discountCoupon==null){
			return Result.fail("数据错误");
		}
		if(discountCoupon.isDel()){
			return  Result.fail("该券已被删除");
		}
		Date nowDate = EtradeUtil.getNowDate();
		if(discountCoupon.getStart().after(nowDate)){
			return Result.fail("未到使用时间");
		}
		if(discountCoupon.getEnd().before(nowDate)){
			return Result.fail("已失效");
		}
		if(orderPrice==null || orderPrice.compareTo(discountCoupon.getThreshhold())==-1){
			return Result.fail("未到使用门槛:"+discountCoupon.getThreshhold().doubleValue());
		}
		
		return Result.ok(getBoFromEntity(entity));
	}

	@Override
	public boolean consume(String couponId, String uid) throws DataRefreshingException {
		try {
			return userDiscountCouponDao.updateConsumeStatus(couponId, uid);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}
	
	private boolean save(UserDiscountCoupon bo) throws DataSavingException{
		try {
			return userDiscountCouponDao.insert(getEntityFromBo(bo));
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}
	
	
	private UserDiscountCoupon getBoFromEntity(UserDiscountCouponEntity entity){
		if(entity!=null){
			UserDiscountCoupon bo = new UserDiscountCoupon();
			BeanUtils.copyProperties(entity, bo);
			if(entity.getDiscountCouponEntity()!=null){
				DiscountCoupon dBo = new DiscountCoupon();
				BeanUtils.copyProperties(entity.getDiscountCouponEntity(), dBo);
				bo.setDiscountCoupon(dBo);
			}
			return bo;
		}
		return null;
	}
	
	private UserDiscountCouponEntity getEntityFromBo(UserDiscountCoupon bo){
		if(bo!=null){
			UserDiscountCouponEntity entity = new UserDiscountCouponEntity();
			BeanUtils.copyProperties(bo, entity);
			if(bo.getDiscountCoupon()!=null){
				DiscountCouponEntity dEntity = new  DiscountCouponEntity();
				BeanUtils.copyProperties(bo.getDiscountCoupon(), dEntity);
				entity.setDiscountCouponEntity(dEntity);
			}
			return entity;
		}
		return null;
	}

	@Override
	public int countConsumedCoupon(String uid) {
		return userDiscountCouponDao.countConsumedCoupon(uid);
	}

	@Override
	public List<UserDiscountCoupon> listConsumedCoupon(String uid, Range range) {
		long offset = 0L;
		long size = 0L;
		if(range!=null){
			offset = range.getFrom();
			size = range.getTo()-range.getFrom();
		}
		return userDiscountCouponDao.selectConsumedCoupon(uid, offset, size )
			.stream().map(rEntity->{return getBoFromEntity(rEntity);})
			.collect(Collectors.toList());
	}

	@Override
	public int countOverdueCoupon(String uid) {
		return userDiscountCouponDao.countOverdueCoupon(uid);
	}

	@Override
	public List<UserDiscountCoupon> listOverdueCoupon(String uid, Range range) {
		long offset = 0L;
		long size = 0L;
		if(range!=null){
			offset = range.getFrom();
			size = range.getTo()-range.getFrom();
		}
		return userDiscountCouponDao.selectOverdueCoupon(uid, offset, size )
			.stream().map(rEntity->{return getBoFromEntity(rEntity);})
			.collect(Collectors.toList());
	}

	@Override
	public int countUseableCoupon(String uid) {
		return userDiscountCouponDao.countUseableCoupon(uid);
	}

	@Override
	public List<UserDiscountCoupon> listUseableCoupon(String uid, Range range) {
		long offset = 0L;
		long size = 0L;
		if(range!=null){
			offset = range.getFrom();
			size = range.getTo()-range.getFrom();
		}
		return userDiscountCouponDao.selectUseableCoupon(uid, null, offset, size )
			.stream().map(rEntity->{return getBoFromEntity(rEntity);})
			.collect(Collectors.toList());
	}
	
	@Override
	public List<UserDiscountCoupon> listUseableCoupon(String uid, 	BigDecimal orderPrice, Range range) {
		long offset = 0L;
		long size = 0L;
		if(range!=null){
			offset = range.getFrom();
			size = range.getTo()-range.getFrom();
		}
		return userDiscountCouponDao.selectUseableCoupon(uid, orderPrice, offset, size )
			.stream().map(rEntity->{return getBoFromEntity(rEntity);})
			.collect(Collectors.toList());
	}

	@Override
	public boolean isLocked(String couponID) {
		Optional<UserDiscountCoupon> single = single(couponID);
		if(single.isPresent() && !single.get().isLocked()){
			return true;
		}
		return false;
	}

	@Override
	public boolean lock(String couponID, String uid) throws DataRefreshingException {
		return userDiscountCouponDao.updateLockedStatus(couponID, uid, true);
	}

	@Override
	public boolean unlock(String couponID, String uid) throws DataRefreshingException {
		return userDiscountCouponDao.updateLockedStatus(couponID, uid, false);
	}

	@Override
	public Optional<UserDiscountCoupon> single(String couponID, String uid) {
		UserDiscountCouponEntity entity = userDiscountCouponDao.selectUserDiscountCoupon(couponID, uid);
		if(entity!=null){
			return Optional.of(getBoFromEntity(entity));
		}
		return Optional.empty();
	}

	@Override
	public Optional<UserDiscountCoupon> single(String couponID) {
		return single(couponID, null);
	}

	

	

	

	

}
