package com.jingyunbank.etrade.vip.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.vip.bo.DiscountCoupon;
import com.jingyunbank.etrade.api.vip.service.IDiscountCouponService;
import com.jingyunbank.etrade.vip.dao.DiscountCouponDao;
import com.jingyunbank.etrade.vip.entity.DiscountCouponEntity;

@Service("discountCouponService")
public class DiscountCouponService implements IDiscountCouponService{

	@Autowired
	private DiscountCouponDao discountCouponDao;

	@Override
	public boolean save(DiscountCoupon discountCoupon, Users manager) throws DataSavingException {
		try {
			return discountCouponDao.insert(getEntityFromBo(discountCoupon));
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public boolean remove(String code, Users manager) throws DataRemovingException {
		DiscountCouponEntity entity = new DiscountCouponEntity();
		entity.setCode(code);
		entity.setDel(true);
		try {
			return discountCouponDao.updateDeleteStatus(entity);
		} catch (Exception e) {
			throw new DataRemovingException(e);
		}
	}

	@Override
	public Result isValid(String code) {
		DiscountCoupon discountCoupon = new DiscountCoupon();
		discountCoupon.setCode(code);
		discountCoupon.setValidTime(true);
		discountCoupon = getSingle(discountCoupon);
		if(discountCoupon==null){
			return Result.fail("卡号错误,或已失效");
		}
		if(discountCoupon.isDel()){
			return Result.fail("该卡已作废");
		}
		if(discountCoupon.isUsed()){
			return Result.fail("该卡已被使用");
		}
		return Result.ok("可激活");
	}

	@Override
	public DiscountCoupon getSingle(DiscountCoupon discountCoupon) {
		List<DiscountCouponEntity> list = discountCouponDao.selectList(getEntityFromBo(discountCoupon));
		if(list!=null && !list.isEmpty()){
			return getBoFromEntity( list.get(0));
		}
		return null;
	}

	@Override
	public List<DiscountCoupon> listAll(DiscountCoupon discountCoupon) {
		
		return discountCouponDao.selectList(getEntityFromBo(discountCoupon))
				.stream().map(entity ->{
					return getBoFromEntity(entity);
				}).collect(Collectors.toList());
	}

	@Override
	public List<DiscountCoupon> listAll(DiscountCoupon discountCoupon,
			Range range) {
		DiscountCouponEntity entityFromBo = getEntityFromBo(discountCoupon);
		if(range!=null){
			entityFromBo.setOffset(range.getFrom());
			entityFromBo.setSize(range.getTo()-range.getFrom());
		}
		
		return discountCouponDao.selectList(entityFromBo).stream()
				.map(entity ->{
					return getBoFromEntity(entity);
				}).collect(Collectors.toList());
	}
	
	/**
	 * 激活
	 * @param code
	 * @return
	 * @throws DataRefreshingException
	 * 2015年11月17日 qxs
	 */
	@Override
	public boolean active(String code) throws DataRefreshingException {
		DiscountCouponEntity entity = new DiscountCouponEntity();
		entity.setCode(code);
		entity.setUsed(true);
		try {
			return discountCouponDao.updateUsedStatus(entity);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}
	
	/**
	 * entity转bo
	 * @param entity
	 * @return
	 * 2015年11月16日 qxs
	 */
	private DiscountCoupon getBoFromEntity(DiscountCouponEntity entity){
		if(entity!=null){
			DiscountCoupon bo = new DiscountCoupon();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}
		return null;
	}
	/**
	 * bo转entity
	 * @param bo
	 * @return
	 * 2015年11月16日 qxs
	 */
	private DiscountCouponEntity getEntityFromBo(DiscountCoupon bo){
		if(bo!=null){
			DiscountCouponEntity entity = new DiscountCouponEntity();
			BeanUtils.copyProperties(bo, entity);
			return entity;
		}
		return null;
	}

	@Override
	public int getAmount(DiscountCoupon cashCoupon) {
		return discountCouponDao.selectAmount(getEntityFromBo(cashCoupon));
	}
	

}
