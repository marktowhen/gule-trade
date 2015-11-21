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
import com.jingyunbank.etrade.api.vip.bo.DiscountCoupon;
import com.jingyunbank.etrade.api.vip.bo.UserDiscountCoupon;
import com.jingyunbank.etrade.api.vip.service.IDiscountCouponService;
import com.jingyunbank.etrade.api.vip.service.IUserDiscountCouponService;
import com.jingyunbank.etrade.vip.bean.DiscountCouponVO;
import com.jingyunbank.etrade.vip.bean.UserDiscountCouponVO;

@RestController
@RequestMapping("/api/user-discountcoupon")
public class UserDiscountCouponController {

	@Autowired
	private IUserDiscountCouponService userDiscountCouponService;

	@Autowired
	private IDiscountCouponService discountCouponService;
	
	/**
	 * 查询未使用的优惠券
	 * @param uid
	 * @param vo
	 * @param page
	 * @return
	 * @throws Exception
	 * 2015年11月17日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/{uid}",method=RequestMethod.GET)
	public Result getUserCashCoupon(@PathVariable String uid,UserDiscountCouponVO vo, Page page)
		throws Exception{
		UserDiscountCoupon boFromVo = getBoFromVo(vo);
		boFromVo.setUID(uid);
		Range range = null;
		if(page!=null){
			range = new Range();
			range.setFrom(page.getOffset());
			range.setTo(page.getOffset()+page.getSize());
		}
		return Result.ok(userDiscountCouponService.getUnusedCoupon(boFromVo, range)
			.stream().map( bo ->{ return getVoFromBo(bo);})
			.collect(Collectors.toList()));
	}
	
	/**
	 * 用户未使用的现金券数量
	 * @param uid
	 * @param vo
	 * @param page
	 * @return
	 * @throws Exception
	 * 2015年11月19日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/amount",method=RequestMethod.GET)
	public Result getUserCashCouponAmount(HttpServletRequest request, UserDiscountCouponVO vo)
		throws Exception{
		UserDiscountCoupon boFromVo = getBoFromVo(vo);
		boFromVo.setUID(ServletBox.getLoginUID(request));
		return Result.ok(userDiscountCouponService.getUnusedCouponAmount(boFromVo));
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
		Result valid = discountCouponService.canActive(code);
		if(valid.isBad()){
			return valid;
		}
		if(userDiscountCouponService.active(code, uid)){
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
		
		if(userDiscountCouponService.consume(couponId, oid)){
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
		return userDiscountCouponService.canConsume(couponId, ServletBox.getLoginUID(request), orderPrice);
	}
	
	private UserDiscountCouponVO getVoFromBo(UserDiscountCoupon bo){
		if(bo!=null){
			UserDiscountCouponVO vo = new UserDiscountCouponVO();
			BeanUtils.copyProperties(bo, vo);
			if(bo.getDiscountCoupon()!=null){
				DiscountCouponVO dVo = new DiscountCouponVO();
				BeanUtils.copyProperties(bo.getDiscountCoupon(), dVo);
				vo.setDiscountCouponVO(dVo);
			}
			return vo;
		}
		return null;
	}
	
	private UserDiscountCoupon getBoFromVo(UserDiscountCouponVO vo){
		if(vo!=null){
			UserDiscountCoupon bo = new UserDiscountCoupon();
			BeanUtils.copyProperties(vo, bo);
			if(vo.getDiscountCouponVO()!=null){
				DiscountCoupon dBo = new DiscountCoupon();
				BeanUtils.copyProperties(vo.getDiscountCouponVO(), dBo);
				bo.setDiscountCoupon(dBo);
			}
			return bo;
		}
		
		return null;
	}
	
	
}
