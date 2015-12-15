package com.jingyunbank.etrade.vip.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping("/api/vip/discountcoupon/user")
public class UserDiscountCouponController {

	@Autowired
	private IUserDiscountCouponService userDiscountCouponService;

	@Autowired
	private IDiscountCouponService discountCouponService;
	
	/**
	 * 查询未使用的优惠券
	 * @param uid
	 * @param page
	 * @return
	 * @throws Exception
	 * 2015年11月17日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/unused/{uid}",method=RequestMethod.GET)
	public Result<List<UserDiscountCouponVO>> listUnusedCoupon(@PathVariable String uid, Page page)
		throws Exception{
		Range range = null;
		if(page!=null){
			range = new Range();
			range.setFrom(page.getOffset());
			range.setTo(page.getOffset()+page.getSize());
		}
		return Result.ok(userDiscountCouponService.listUnusedCoupon(uid, range)
			.stream().map( bo ->{ return getVoFromBo(bo);})
			.collect(Collectors.toList()));
	}
	
	/**
	 * 用户未使用的现金券数量
	 * @param uid
	 * @return
	 * @throws Exception
	 * 2015年11月19日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/unused/amount/{uid}",method=RequestMethod.GET)
	public Result<Integer> countUnusedCoupon(@PathVariable String uid)
		throws Exception{
		return Result.ok(userDiscountCouponService.countUnusedCoupon(uid));
	}
	
	
	
	
	/**
	 * 用户已消费的现金券
	 * @param uid
	 * @param page
	 * @return
	 * @throws Exception
	 * 2015年11月17日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/consumed/{uid}",method=RequestMethod.GET)
	public Result<List<UserDiscountCouponVO>> listConsumedCoupon(@PathVariable String uid, Page page)
		throws Exception{
		Range range = null;
		if(page!=null){
			range = new Range();
			range.setFrom(page.getOffset());
			range.setTo(page.getOffset()+page.getSize());
		}
		return Result.ok(userDiscountCouponService.listConsumedCoupon(uid, range)
			.stream().map( bo ->{ return getVoFromBo(bo);})
			.collect(Collectors.toList()));
	}
	
	/**
	 * 用户未使用的现金券数量
	 * @param uid
	 * @return
	 * @throws Exception
	 * 2015年11月19日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/consumed/amount/{uid}",method=RequestMethod.GET)
	public Result<Integer> countConsumedCoupon(@PathVariable String uid)
		throws Exception{
		return Result.ok(userDiscountCouponService.countConsumedCoupon(uid));
	}
	
	/**
	 * 查询已过期的现金券
	 * @param uid
	 * @param page
	 * @return
	 * @throws Exception
	 * 2015年11月17日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/overdue/{uid}",method=RequestMethod.GET)
	public Result<List<UserDiscountCouponVO>> listOverdueCoupon(@PathVariable String uid, Page page)
		throws Exception{
		Range range = null;
		if(page!=null){
			range = new Range();
			range.setFrom(page.getOffset());
			range.setTo(page.getOffset()+page.getSize());
		}
		return Result.ok(userDiscountCouponService.listOverdueCoupon(uid, range)
			.stream().map( bo ->{ return getVoFromBo(bo);})
			.collect(Collectors.toList()));
	}
	
	/**
	 * 用户未使用的现金券数量
	 * @param uid
	 * @return
	 * @throws Exception
	 * 2015年11月19日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/overdue/amount/{uid}",method=RequestMethod.GET)
	public Result<Integer> countOverdueCoupon(@PathVariable String uid)
		throws Exception{
		return Result.ok(userDiscountCouponService.countOverdueCoupon(uid));
	}
	
	/**
	 * 用户当前可用的现金券
	 * @param uid
	 * @param page
	 * @return
	 * @throws Exception
	 * 2015年11月17日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/useable/{uid}",method=RequestMethod.GET)
	public Result<List<UserDiscountCouponVO>> listUseableCoupon(@PathVariable String uid,@RequestParam(required=false) BigDecimal orderPrice, Page page)
		throws Exception{
		Range range = null;
		if(page!=null){
			range = new Range();
			range.setFrom(page.getOffset());
			range.setTo(page.getOffset()+page.getSize());
		}
		return Result.ok(userDiscountCouponService.listUseableCoupon(uid, orderPrice, range)
			.stream().map( bo ->{ return getVoFromBo(bo);})
			.collect(Collectors.toList()));
	}
	
	/**
	 * 用户未使用的现金券数量
	 * @param uid
	 * @return
	 * @throws Exception
	 * 2015年11月19日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/useable/amount/{uid}",method=RequestMethod.GET)
	public Result<Integer> countUseableCoupon(@PathVariable String uid)
		throws Exception{
		return Result.ok(userDiscountCouponService.countUseableCoupon(uid));
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
	@RequestMapping(value="/{code}", method=RequestMethod.PUT)
	public Result<DiscountCoupon> active(@PathVariable String code, HttpServletRequest request) throws Exception {
		String uid = ServletBox.getLoginUID(request);
		Result<DiscountCoupon> valid = discountCouponService.canActive(code);
		if(valid.isBad()){
			return valid;
		}
		if(userDiscountCouponService.active(code, uid)){
			return Result.ok();
		}
		return Result.fail("系统繁忙,请稍后再试");
	}
	
	
	private UserDiscountCouponVO getVoFromBo(UserDiscountCoupon bo){
		if(bo!=null){
			UserDiscountCouponVO vo = new UserDiscountCouponVO();
			BeanUtils.copyProperties(bo, vo);
			if(bo.getDiscountCoupon()!=null){
				DiscountCouponVO dVo = new DiscountCouponVO();
				BeanUtils.copyProperties(bo.getDiscountCoupon(), dVo);
				vo.setDiscountCoupon(dVo);
			}
			return vo;
		}
		return null;
	}
	
	
	
}
