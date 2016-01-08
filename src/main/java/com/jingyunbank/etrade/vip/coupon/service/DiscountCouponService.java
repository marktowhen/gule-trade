package com.jingyunbank.etrade.vip.coupon.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.util.RndBuilder;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.vip.coupon.bo.DiscountCoupon;
import com.jingyunbank.etrade.api.vip.coupon.service.ICashCouponService;
import com.jingyunbank.etrade.api.vip.coupon.service.IDiscountCouponService;
import com.jingyunbank.etrade.vip.coupon.dao.DiscountCouponDao;
import com.jingyunbank.etrade.vip.coupon.entity.DiscountCouponEntity;

@Service("discountCouponService")
public class DiscountCouponService implements IDiscountCouponService{

	@Autowired
	private DiscountCouponDao discountCouponDao;

	@Override
	public boolean save(DiscountCoupon discountCoupon, Users manager) throws DataSavingException {
		try {
			discountCoupon.setLocked(true);
			
			discountCoupon.setID(KeyGen.uuid());
			while(true){
				discountCoupon.setCardNum(getNewCardNum(discountCoupon.getValue()));
				discountCoupon.setCode(new String(new RndBuilder().length(10).hasletter(true).next()));
				discountCoupon.setLocked(true);
				if(discountCouponDao.insert(getEntityFromBo(discountCoupon))){
					return true;
				}
			}
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}
	
	@Override
	public boolean saveMuti(DiscountCoupon discountCoupon, Users manager,
			int amount) throws DataSavingException {
		try {
			for (int i = 0; i < amount; i++) {
				save(discountCoupon, manager);
			}
			return true;
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
	public Result<DiscountCoupon> canActive(String code) {
		DiscountCouponEntity entity = discountCouponDao.selectSingleByKey(code);
		if(entity==null){
			return Result.fail("充值码错误,请重新输入");
		}
		if(entity.isDel()){
			return Result.fail("该券已作废,请输入其他充值码");
		}
		if(entity.isUsed()){
			return Result.fail("该券已被使用,请输入其他充值码");
		}
		if(entity.isLocked()){
			return Result.fail("该券未解锁,请联系客服或输入其他充值码");
		}
		if(entity.getEnd().before(new Date())){
			return Result.fail("该券已过期,请输入其他充值码");
		}
		return Result.ok(getBoFromEntity(entity));
	}

	@Override
	public DiscountCoupon singleByCode(String code) {
		DiscountCouponEntity entity = discountCouponDao.selectSingleByKey(code);
		if(entity!=null){
			return getBoFromEntity(entity);
		}
		return null;
	}
	@Override
	public DiscountCoupon singleByID(String ID) {
		DiscountCouponEntity entity = discountCouponDao.selectSingleByKey(ID);
		if(entity!=null){
			return getBoFromEntity(entity);
		}
		return null;
	}

	@Override
	public List<DiscountCoupon> list(Date addTimeFrom, Date addTimeTo, Range range){
		
		return discountCouponDao.selectByAddtime(addTimeFrom, addTimeTo, range.getFrom(), range.getTo()-range.getFrom())
				.stream().map(entity ->{
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
	public int count(Date addtimeFrom, Date addtimeTo) {
		return discountCouponDao.countByAddtime(addtimeFrom, addtimeTo);
	}
	
	/**
	 * 获取新的卡号
	 * @param value
	 * @return
	 * @throws Exception
	 * 2015年12月29日 qxs
	 */
	private String getNewCardNum(BigDecimal value) throws Exception{
		StringBuffer cardNum = new StringBuffer();
		cardNum.append(getCardNumPrifix(value));
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		cardNum.append(format.format(new Date()));
		return cardNum.append(getNewIndex(value)).toString();
	}
	/**
	 * 获取新的卡号后缀
	 * @param value
	 * @return
	 * 2015年12月29日 qxs
	 * @throws Exception 
	 */
	private long getNewIndex(BigDecimal value) throws Exception{
		StringBuffer cardNum = new StringBuffer();
		cardNum.append(getCardNumPrifix(value));
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		cardNum.append(format.format(new Date()));
		String lastCardNum = getLastCardNum(cardNum.toString());
		if(StringUtils.isEmpty(lastCardNum)){
			return ICashCouponService.CARD_NUM_SUFFIX_START;
		}
		//数据库中的最大的序号
		String index = lastCardNum.substring(cardNum.toString().length());
		return Long.parseLong(index)+1;
	}

	/**
	 * 获取数据库中的最大卡号
	 * @param cardNum
	 * @return
	 * 2015年12月29日 qxs
	 */
	private String getLastCardNum(String cardNum){
		List<DiscountCouponEntity> list = discountCouponDao.selectListByCardNum(cardNum, 0, 1);
		if(list!=null && !list.isEmpty()){
			return list.get(0).getCardNum();
		}
		return null;
	}
	
	
	/**
	 * 获取抵用券卡号前缀
	 * @param value
	 * @return
	 * @throws Exception
	 * 2015年12月29日 qxs
	 */
	private String getCardNumPrifix(BigDecimal value) throws Exception{
		return IDiscountCouponService.CARD_NUM_PRIFIX_50;
	}

	@Override
	public List<DiscountCoupon> list(String cardNum, BigDecimal value,
			Boolean locked, Range range) {
		DiscountCouponEntity condition = new DiscountCouponEntity();
		condition.setCardNum(cardNum);
		condition.setValue(value);
		if(locked!=null){
			condition.setNeedLocked(true);
			condition.setLocked(locked);
		}
		return discountCouponDao.selectList(condition, range.getFrom(), range.getTo()-range.getFrom())
				.stream().map(entity->{
						return (getBoFromEntity(entity));
				}).collect(Collectors.toList());
	}

	@Override
	public int count(String cardNum, BigDecimal value, Boolean locked) {
		DiscountCouponEntity condition = new DiscountCouponEntity();
		condition.setCardNum(cardNum);
		condition.setValue(value);
		if(locked!=null){
			condition.setNeedLocked(true);
			condition.setLocked(locked);
		}
		return discountCouponDao.count(condition);
	}

	@Override
	public boolean unlock(String[] ids) {
		return discountCouponDao.updateLocked(ids, false);
	}

	
	

}
