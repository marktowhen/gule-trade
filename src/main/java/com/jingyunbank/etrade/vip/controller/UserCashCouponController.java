package com.jingyunbank.etrade.vip.controller;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Page;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.vip.bo.CashCoupon;
import com.jingyunbank.etrade.api.vip.bo.UserCashCoupon;
import com.jingyunbank.etrade.api.vip.service.ICashCouponService;
import com.jingyunbank.etrade.api.vip.service.IUserCashCouponService;
import com.jingyunbank.etrade.vip.bean.CashCouponVO;
import com.jingyunbank.etrade.vip.bean.UserCashCouponVO;

@RestController
@RequestMapping("/api/user-cashcoupon")
public class UserCashCouponController {
	
	@Autowired
	private IUserCashCouponService userCashCouponService;
	@Autowired
	private ICashCouponService cashCouponService;
	
	/**
	 * 用户未使用的未过期现金券
	 * @param uid
	 * @param vo
	 * @param page
	 * @return
	 * @throws Exception
	 * 2015年11月19日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/{uid}",method=RequestMethod.GET)
	public Result getUserCashCoupon(@PathVariable String uid,UserCashCouponVO vo, Page page)
		throws Exception{
		UserCashCoupon boFromVo = getBoFromVo(vo);
		boFromVo.setUID(uid);
		Range range = null;
		if(page!=null){
			range = new Range();
			range.setFrom(page.getOffset());
			range.setTo(page.getOffset()+page.getSize());
		}
		return Result.ok(userCashCouponService.getUnusedCoupon(boFromVo, range)
			.stream().map( bo ->{ return getVoFromBo(bo);})
			.collect(Collectors.toList()));
	}
	
	/**
	 * 用户未使用的未过期现金券数量
	 * @param uid
	 * @param vo
	 * @param page
	 * @return
	 * @throws Exception
	 * 2015年11月19日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/amount",method=RequestMethod.GET)
	public Result getUserCashCouponAmount(HttpServletRequest request, UserCashCouponVO vo)
		throws Exception{
		UserCashCoupon boFromVo = getBoFromVo(vo);
		boFromVo.setUID(ServletBox.getLoginUID(request));
		return Result.ok(userCashCouponService.getUnusedCouponAmount(boFromVo));
	}
	/**
	 * 激活一张未使用的卡
	 * @param code
	 * @param request
	 * @return
	 * @throws Exception
	 * 2015年11月17日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/", method=RequestMethod.PUT)
	public Result active(String code, HttpServletRequest request) throws Exception {
		String uid = ServletBox.getLoginUID(request);
		Result valid = cashCouponService.canActive(code);
		if(valid.isBad()){
			return valid;
		}
		if(userCashCouponService.active(code, uid)){
			return Result.ok();
		}
		return Result.fail("系统繁忙,请稍后再试");
	}
	
	/**
	 * 测试消费
	 * @param couponId
	 * @param oid
	 * @return
	 * @throws Exception
	 * 2015年11月17日 qxs
	 */
	@RequestMapping(value="", method=RequestMethod.POST)
	public Result testConsume(String couponId, String oid) throws Exception{
		
		if(userCashCouponService.consume(couponId, oid)){
			return Result.ok();
		}
		return Result.fail("");
	}
	
	/**
	 * 是否可以消费
	 * @param couponId 券id
	 * @param orderPrice 订单价值
	 * @param request
	 * @return
	 * 2015年11月21日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="can-consume", method=RequestMethod.GET)
	public Result canConsume(String couponId, BigDecimal orderPrice,HttpServletRequest request){
		return userCashCouponService.canConsume(couponId, ServletBox.getLoginUID(request), orderPrice);
	}
	
	private UserCashCouponVO getVoFromBo(UserCashCoupon bo){
		if(bo!=null){
			UserCashCouponVO vo = new UserCashCouponVO();
			BeanUtils.copyProperties(bo, vo);
			if(bo.getCashCoupon()!=null){
				CashCouponVO cashVO = new CashCouponVO();
				BeanUtils.copyProperties(bo.getCashCoupon(), cashVO);
				vo.setCashCoupon(cashVO);
			}
			return vo;
		}
		return null;
	}
	
	private UserCashCoupon getBoFromVo(UserCashCouponVO vo){
		if(vo!=null){
			UserCashCoupon bo = new UserCashCoupon();
			BeanUtils.copyProperties(vo, bo);
			if(vo.getCashCoupon()!=null){
				CashCoupon cBo = new CashCoupon();
				BeanUtils.copyProperties(vo.getCashCoupon(), cBo);
				bo.setCashCoupon(cBo);
			}
			return bo;
		}
		
		return null;
	}
}
