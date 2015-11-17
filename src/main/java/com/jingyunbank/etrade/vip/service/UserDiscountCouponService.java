package com.jingyunbank.etrade.vip.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.vip.bo.DiscountCoupon;
import com.jingyunbank.etrade.api.vip.bo.UserDiscountCoupon;
import com.jingyunbank.etrade.api.vip.service.IDiscountCouponService;
import com.jingyunbank.etrade.api.vip.service.IUserDiscountCouponService;
import com.jingyunbank.etrade.vip.dao.UserDiscountCouponDao;
import com.jingyunbank.etrade.vip.entity.DiscountCouponEntity;
import com.jingyunbank.etrade.vip.entity.UserDiscountCouponEntity;

@Service("userDiscountCouponService")
public class UserDiscountCouponService implements IUserDiscountCouponService {
	
	@Autowired
	private UserDiscountCouponDao userDiscountCouponDao ;
	@Autowired
	private IDiscountCouponService discountCouponService;

	@Override
	public boolean active(String code, String uid) throws DataSavingException, DataRefreshingException {
		DiscountCoupon discountCoupon = new DiscountCoupon();
		discountCoupon.setCode(code);
		discountCoupon = discountCouponService.getSingle(discountCoupon);
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
		return false;
	}
	
	@Override
	public List<UserDiscountCoupon> getUnusedCoupon(UserDiscountCoupon bo,
			Range range) {
		UserDiscountCouponEntity entity = getEntityFromBo(bo);
		if(range!=null){
			entity.setOffset(range.getFrom());
			entity.setSize(range.getTo()-range.getFrom());
		}
		return userDiscountCouponDao.getUnusedCoupon(entity)
				.stream().map(rEntity->{return getBoFromEntity(rEntity);})
				.collect(Collectors.toList());
	}


	@Override
	public boolean consume(String couponId, String oid) throws DataRefreshingException {
		UserDiscountCouponEntity entity = new UserDiscountCouponEntity();
		entity.setCouponID(couponId);
		entity.setOID(oid);
		try {
			return userDiscountCouponDao.updateConsumeStatus(entity);
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

}
