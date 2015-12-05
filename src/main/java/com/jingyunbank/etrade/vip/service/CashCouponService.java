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
import com.jingyunbank.etrade.api.vip.bo.CashCoupon;
import com.jingyunbank.etrade.api.vip.service.ICashCouponService;
import com.jingyunbank.etrade.base.util.EtradeUtil;
import com.jingyunbank.etrade.vip.dao.CashCouponDao;
import com.jingyunbank.etrade.vip.entity.CashCouponEntity;

@Service("cashCouponService")
public class CashCouponService implements ICashCouponService{

	@Autowired
	private CashCouponDao cashCouponDao;

	@Override
	public boolean save(CashCoupon cashCoupon, Users manager) throws DataSavingException {
		
		try {
			return cashCouponDao.insert(getEntityFromBo(cashCoupon));
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
		
	}

	@Override
	public boolean remove(String code, Users manager) throws DataRemovingException {
		CashCouponEntity entity = new CashCouponEntity();
		entity.setCode(code);
		entity.setDel(true);
		try {
			return cashCouponDao.updateDeleteStatus(entity);
		} catch (Exception e) {
			throw new DataRemovingException(e);
		}
	}

	@Override
	public Result<String> canActive(String code) {
		CashCouponEntity entity = cashCouponDao.selectSingle(code);
		if(entity==null){
			return Result.fail("卡号错误");
		}
		if(entity.isDel()){
			return Result.fail("该卡已作废");
		}
		if(entity.isUsed()){
			return Result.fail("该卡已被使用");
		}
		if(entity.getEnd().before(EtradeUtil.getNowDate())){
			return Result.fail("已过期");
		}
		return Result.ok("可激活");
	}

	@Override
	public CashCoupon getSingleByCode(String code) {
		CashCouponEntity entity = cashCouponDao.selectSingle(code);
		if(entity!=null){
			return getBofromEntity(entity);
		}
		return null;
	}

	@Override
	public CashCoupon getSingleByID(String id) {
		CashCouponEntity entity = cashCouponDao.selectSingle(id);
		if(entity!=null){
			return getBofromEntity(entity);
		}
		return null;
	}

	@Override
	public List<CashCoupon> listAll(CashCoupon cashCoupon) {
		return cashCouponDao.selectList(getEntityFromBo(cashCoupon))
				.stream().map( entity -> {
					return getBofromEntity(entity);
				}).collect(Collectors.toList());
	}

	@Override
	public List<CashCoupon> listAll(CashCoupon cashCoupon, Range range) {
		CashCouponEntity cashCouponEntity = getEntityFromBo(cashCoupon);
		if(range!=null){
			cashCouponEntity.setOffset(range.getFrom());
			cashCouponEntity.setSize(range.getTo()-range.getFrom());
		}
		return cashCouponDao.selectList(getEntityFromBo(cashCoupon))
			.stream().map(entity->{
					return (getBofromEntity(entity));
			}).collect(Collectors.toList());
		
	}

	
	/**
	 * do转bo
	 * @param bo
	 * @return
	 * 2015年11月16日 qxs
	 */
	private CashCouponEntity getEntityFromBo(CashCoupon bo){
		if(bo!=null){
			CashCouponEntity entity = new CashCouponEntity();
			BeanUtils.copyProperties(bo, entity);
			return entity;
		}
		return null;
	}
	
	/**
	 * entity 转 bo
	 * @param entity
	 * @return
	 * 2015年11月16日 qxs
	 */
	private CashCoupon getBofromEntity(CashCouponEntity entity){
		if(entity!=null){
			CashCoupon bo = new CashCoupon();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}
		return null;
	}

	/**
	 * 激活一张卡 将其改为已使用状态
	 * @param code
	 * @return
	 * 2015年11月17日 qxs
	 * @throws DataRefreshingException 
	 */
	@Override
	public boolean activeCoupon(String code) throws DataRefreshingException {
		CashCouponEntity entity = new CashCouponEntity();
		entity.setCode(code);
		entity.setUsed(true);
		try {
			return cashCouponDao.updateUsedStatus(entity);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	public int getAmount(CashCoupon cashCoupon) {
		return cashCouponDao.selectAmount(getEntityFromBo(cashCoupon));
	}

	
}
