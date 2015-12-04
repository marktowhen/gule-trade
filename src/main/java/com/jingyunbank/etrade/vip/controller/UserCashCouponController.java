package com.jingyunbank.etrade.vip.controller;

import java.util.List;
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
import com.jingyunbank.etrade.api.vip.bo.UserCashCoupon;
import com.jingyunbank.etrade.api.vip.service.ICashCouponService;
import com.jingyunbank.etrade.api.vip.service.IUserCashCouponService;
import com.jingyunbank.etrade.vip.bean.CashCouponVO;
import com.jingyunbank.etrade.vip.bean.UserCashCouponVO;

@RestController
@RequestMapping("/api/user/cashcoupon")
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
	public Result<List<UserCashCouponVO>> getUnusedCoupon(@PathVariable String uid , Page page)
		throws Exception{
		Range range = null;
		if(page!=null){
			range = new Range();
			range.setFrom(page.getOffset());
			range.setTo(page.getOffset()+page.getSize());
		}
		return Result.ok(userCashCouponService.getUnusedCoupon(uid, range)
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
	public Result<Integer> getUnusedCouponAmount(HttpServletRequest request, @PathVariable String uid)
		throws Exception{
		return Result.ok(userCashCouponService.getUnusedCouponAmount(uid));
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
	public Result<List<UserCashCouponVO>> getConsumedCoupon(@PathVariable String uid , Page page)
		throws Exception{
		Range range = null;
		if(page!=null){
			range = new Range();
			range.setFrom(page.getOffset());
			range.setTo(page.getOffset()+page.getSize());
		}
		return Result.ok(userCashCouponService.getConsumedCoupon(uid, range)
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
	public Result<Integer> getConsumedCouponAmount( @PathVariable String uid)
		throws Exception{
		return Result.ok(userCashCouponService.getConsumedCouponAmount(uid));
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
	public Result<List<UserCashCouponVO>> getOverdueCoupon(@PathVariable String uid , Page page)
		throws Exception{
		Range range = null;
		if(page!=null){
			range = new Range();
			range.setFrom(page.getOffset());
			range.setTo(page.getOffset()+page.getSize());
		}
		return Result.ok(userCashCouponService.getOverdueCoupon(uid, range)
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
	public Result<Integer> getOverdueCouponAmount(@PathVariable String uid)
		throws Exception{
		return Result.ok(userCashCouponService.getOverdueCouponAmount(uid));
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
	public Result<List<UserCashCouponVO>> getUseableCoupon(@PathVariable String uid , Page page)
		throws Exception{
		Range range = null;
		if(page!=null){
			range = new Range();
			range.setFrom(page.getOffset());
			range.setTo(page.getOffset()+page.getSize());
		}
		return Result.ok(userCashCouponService.getUseableCoupon(uid, range)
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
	public Result<Integer> getUseableCouponAmount( @PathVariable String uid)
		throws Exception{
		return Result.ok(userCashCouponService.getUseableCouponAmount(uid));
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
		Result<String> valid = cashCouponService.canActive(code);
		if(valid.isBad()){
			return valid;
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
