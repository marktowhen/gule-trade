package com.jingyunbank.etrade.vip.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.vip.bo.CashCoupon;
import com.jingyunbank.etrade.api.vip.bo.UserCashCoupon;
import com.jingyunbank.etrade.api.vip.service.ICashCouponService;
import com.jingyunbank.etrade.api.vip.service.IUserCashCouponService;
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
		CashCoupon cashCoupon = new CashCoupon();
		cashCoupon.setCode(code);
		cashCoupon = cashCouponService.getSingle(cashCoupon);
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
	public List<UserCashCoupon> getUnusedCoupon(UserCashCoupon userCoupon, Range range) {
		UserCashCouponEntity entity = getEntityFromBo(userCoupon);
		if(range!=null){
			entity.setOffset(range.getFrom());
			entity.setSize(range.getTo()-range.getFrom());
		}
		return userCashCouponDao.getUnusedCoupon(entity)
			.stream().map( entityResul ->{return getBoFromEntity(entityResul);})
			.collect(Collectors.toList());
	}
	
	@Override
	public int getUnusedCouponAmount(UserCashCoupon bo) {
		
		return userCashCouponDao.getUnusedCouponAmount(getEntityFromBo(bo));
	}
	
	private boolean save(UserCashCoupon userCashCoupon) throws DataSavingException{
		try {
			return userCashCouponDao.insert(userCashCoupon);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}
	
	@Override
	public boolean consume(String couponId, String oid) throws DataRefreshingException {
		
		UserCashCouponEntity entity = new UserCashCouponEntity();
		entity.setCouponID(couponId);
		entity.setOID(oid);
		try {
			return userCashCouponDao.updateConsumeStatus(entity);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}
	
	private UserCashCouponEntity getEntityFromBo(UserCashCoupon userCoupon){
		if(userCoupon!=null){
			UserCashCouponEntity entity = new UserCashCouponEntity();
			BeanUtils.copyProperties(userCoupon, entity);
			if(userCoupon.getCashCoupon()!=null){
				CashCouponEntity cEntity = new CashCouponEntity();
				BeanUtils.copyProperties(userCoupon.getCashCoupon(), cEntity);
				entity.setCashCoupon(cEntity);
			}
			return entity;
		}
		return null;
		
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

	

	

}
