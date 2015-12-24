package com.jingyunbank.etrade.vip.coupon.controller;

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
import com.jingyunbank.etrade.api.vip.coupon.bo.CashCoupon;
import com.jingyunbank.etrade.api.vip.coupon.bo.UserCashCoupon;
import com.jingyunbank.etrade.api.vip.coupon.service.ICashCouponService;
import com.jingyunbank.etrade.api.vip.coupon.service.IUserCashCouponService;
import com.jingyunbank.etrade.vip.coupon.bean.CashCouponVO;
import com.jingyunbank.etrade.vip.coupon.bean.UserCashCouponVO;

@RestController
@RequestMapping("/api/vip/coupon/cashcoupon/user")
public class UserCashCouponController {
	
	@Autowired
	private IUserCashCouponService userCashCouponService;
	@Autowired
	private ICashCouponService cashCouponService;
	
	/**
	 * 用户未消费的现金券
	 * @param uid
	 * @param page
	 * @return
	 * @throws Exception
	 * 2015年11月19日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/unused/{uid}",method=RequestMethod.GET)
	public Result<List<UserCashCouponVO>> listtUnusedCoupon(@PathVariable String uid , Page page)
		throws Exception{
		Range range = null;
		if(page!=null){
			range = new Range();
			range.setFrom(page.getOffset());
			range.setTo(page.getOffset()+page.getSize());
		}
		return Result.ok(userCashCouponService.listUnusedCoupon(uid, range)
			.stream().map( bo ->{ return getVoFromBo(bo);})
			.collect(Collectors.toList()));
	}
	
	/**
	 * 用户未消费的现金券数量
	 * @param uid
	 * @return
	 * @throws Exception
	 * 2015年11月19日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/unused/amount/{uid}",method=RequestMethod.GET)
	public Result<Integer> countUnusedCoupon(HttpServletRequest request, @PathVariable String uid)
		throws Exception{
		return Result.ok(userCashCouponService.countUnusedCoupon(uid));
	}
	
	/**
	 * 用户已消费的现金券
	 * @param uid
	 * @param page
	 * @return
	 * @throws Exception
	 * 2015年11月19日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/consumed/{uid}",method=RequestMethod.GET)
	public Result<List<UserCashCouponVO>> listConsumedCoupon(@PathVariable String uid , Page page)
		throws Exception{
		Range range = null;
		if(page!=null){
			range = new Range();
			range.setFrom(page.getOffset());
			range.setTo(page.getOffset()+page.getSize());
		}
		return Result.ok(userCashCouponService.listConsumedCoupon(uid, range)
			.stream().map( bo ->{ return getVoFromBo(bo);})
			.collect(Collectors.toList()));
	}
	
	/**
	 * 用户已消费的现金券数量
	 * @param uid
	 * @return
	 * @throws Exception
	 * 2015年11月19日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/consumed/amount/{uid}",method=RequestMethod.GET)
	public Result<Integer> countConsumedCoupon( @PathVariable String uid)
		throws Exception{
		return Result.ok(userCashCouponService.countConsumedCoupon(uid));
	}
	
	/**
	 * 用户已过期的现金券
	 * @param uid
	 * @param page
	 * @return
	 * @throws Exception
	 * 2015年11月19日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/overdue/{uid}",method=RequestMethod.GET)
	public Result<List<UserCashCouponVO>> listOverdueCoupon(@PathVariable String uid , Page page)
		throws Exception{
		Range range = null;
		if(page!=null){
			range = new Range();
			range.setFrom(page.getOffset());
			range.setTo(page.getOffset()+page.getSize());
		}
		return Result.ok(userCashCouponService.listOverdueCoupon(uid, range)
			.stream().map( bo ->{ return getVoFromBo(bo);})
			.collect(Collectors.toList()));
	}
	
	/**
	 * 用户已过期的现金券数量
	 * @param uid
	 * @return
	 * @throws Exception
	 * 2015年11月19日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/overdue/amount/{uid}",method=RequestMethod.GET)
	public Result<Integer> countOverdueCoupon(@PathVariable String uid)
		throws Exception{
		return Result.ok(userCashCouponService.countOverdueCoupon(uid));
	}
	
	/**
	 * 用户当前可用的现金券
	 * @param uid
	 * @param page
	 * @return
	 * @throws Exception
	 * 2015年11月19日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/useable/{uid}",method=RequestMethod.GET)
	public Result<List<UserCashCouponVO>> listUseableCoupon(@PathVariable String uid ,@RequestParam(required=false) BigDecimal orderPrice , Page page)
		throws Exception{
		Range range = null;
		if(page!=null){
			range = new Range();
			range.setFrom(page.getOffset());
			range.setTo(page.getOffset()+page.getSize());
		}
		return Result.ok(userCashCouponService.listUseableCoupon(uid, orderPrice, range)
			.stream().map( bo ->{ return getVoFromBo(bo);})
			.collect(Collectors.toList()));
	}
	
	/**
	 * 用户当前可用的现金券数量
	 * @param uid
	 * @return
	 * @throws Exception
	 * 2015年11月19日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/useable/amount/{uid}",method=RequestMethod.GET)
	public Result<Integer> countUseableCoupon( @PathVariable String uid)
		throws Exception{
		return Result.ok(userCashCouponService.countUseableCoupon(uid));
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
	public Result<String> active(@PathVariable String code, HttpServletRequest request) throws Exception {
		String uid = ServletBox.getLoginUID(request);
		Result<CashCoupon> valid = cashCouponService.canActive(code);
		if(valid.isBad()){
			return Result.fail(valid.getMessage());
		}
		if(userCashCouponService.active(code, uid)){
			return Result.ok();
		}
		return  Result.fail("系统繁忙,请稍后再试");
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
	
}
