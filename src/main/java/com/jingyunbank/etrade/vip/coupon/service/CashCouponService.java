package com.jingyunbank.etrade.vip.coupon.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.jingyunbank.etrade.api.vip.coupon.bo.CashCoupon;
import com.jingyunbank.etrade.api.vip.coupon.service.ICashCouponService;
import com.jingyunbank.etrade.vip.coupon.dao.CashCouponDao;
import com.jingyunbank.etrade.vip.coupon.entity.CashCouponEntity;

@Service("cashCouponService")
public class CashCouponService implements ICashCouponService{

	@Autowired
	private CashCouponDao cashCouponDao;

	@Override
	public CashCoupon save(CashCoupon cashCoupon, Users manager) throws DataSavingException {
		
		try {
			cashCoupon.setID(KeyGen.uuid());
			while(true){
				cashCoupon.setCardNum(getNewCardNum(cashCoupon.getValue()));
				cashCoupon.setCode(new String(new RndBuilder().length(10).hasletter(true).next()));
				if(cashCouponDao.insert(getEntityFromBo(cashCoupon))){
					CashCoupon result = new CashCoupon();
					BeanUtils.copyProperties(cashCoupon, result);
					return result;
				}
			}
			
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
		
	}
	
	@Override
	public List<CashCoupon> saveMuti(CashCoupon cashCoupon, Users manager, int amount) throws DataSavingException {
		
		try {
			List<CashCoupon> list = new ArrayList<CashCoupon>();
			for (int i = 0; i < amount; i++) {
				list.add(save(cashCoupon, manager));
			}
			return list;
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
	public Result<CashCoupon> canActive(String code) {
		CashCouponEntity entity = cashCouponDao.selectSingle(code);
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
		if(entity.getEnd()!=null && entity.getEnd().before(new Date())){
			return Result.fail("该券已过期,请输入其他充值码");
		}
		return Result.ok(getBofromEntity(entity));
	}

	@Override
	public CashCoupon singleByCode(String code) {
		CashCouponEntity entity = cashCouponDao.selectSingle(code);
		if(entity!=null){
			return getBofromEntity(entity);
		}
		return null;
	}

	@Override
	public CashCoupon singleByID(String id) {
		CashCouponEntity entity = cashCouponDao.selectSingle(id);
		if(entity!=null){
			return getBofromEntity(entity);
		}
		return null;
	}


	@Override
	public List<CashCoupon> list(Date addTimeFrom, Date addTimeTo, Range range) {
		return cashCouponDao.selectListByAddTime(addTimeFrom, addTimeTo, range.getFrom(), range.getTo()-range.getFrom())
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
	public int count(Date addTimeFrom, Date addTimeTo) {
		return cashCouponDao.countByAddTime(addTimeFrom, addTimeTo);
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
		List<CashCouponEntity> list = cashCouponDao.selectListByCardNum(cardNum, 0, 1);
		if(list!=null && !list.isEmpty()){
			return list.get(0).getCardNum();
		}
		return null;
	}
	
	
	/**
	 * 获取购物金卡号前缀
	 * @param value
	 * @return
	 * @throws Exception
	 * 2015年12月29日 qxs
	 */
	private String getCardNumPrifix(BigDecimal value) throws Exception{
		if(new BigDecimal("1000").compareTo(value)==0){
			return (ICashCouponService.CARD_NUM_PRIFIX_1000);
		}else if(new BigDecimal("500").compareTo(value)==0){
			return (ICashCouponService.CARD_NUM_PRIFIX_500);
		}else if(new BigDecimal("200").compareTo(value)==0){
			return (ICashCouponService.CARD_NUM_PRIFIX_200);
		}else{
			return (ICashCouponService.CARD_NUM_PRIFIX_OTHER);
		}
	}

	@Override
	public List<CashCoupon> list(String cardNum, BigDecimal value,
			Boolean locked, Range range) {
		CashCouponEntity condition = new CashCouponEntity();
		condition.setCardNum(cardNum);
		condition.setValue(value);
		if(locked!=null){
			condition.setNeedLocked(true);
			condition.setLocked(locked);
		}
		return cashCouponDao.selectList(condition, range.getFrom(), range.getTo()-range.getFrom())
				.stream().map(entity->{
						return (getBofromEntity(entity));
				}).collect(Collectors.toList());
	}

	@Override
	public int count(String cardNum, BigDecimal value, Boolean locked) {
		CashCouponEntity condition = new CashCouponEntity();
		condition.setCardNum(cardNum);
		condition.setValue(value);
		if(locked!=null){
			condition.setNeedLocked(true);
			condition.setLocked(locked);
		}
		return cashCouponDao.count(condition);
	}

	@Override
	public boolean unlock(String[] ids) {
		return cashCouponDao.updateLocked(ids, false);
	}
	
}
