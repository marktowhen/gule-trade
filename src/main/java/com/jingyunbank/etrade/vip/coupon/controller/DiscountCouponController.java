package com.jingyunbank.etrade.vip.coupon.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.vip.coupon.bo.BaseCoupon;
import com.jingyunbank.etrade.api.vip.coupon.bo.DiscountCoupon;
import com.jingyunbank.etrade.api.vip.coupon.service.IDiscountCouponService;
import com.jingyunbank.etrade.vip.coupon.bean.DiscountCouponAddVO;
import com.jingyunbank.etrade.vip.coupon.bean.DiscountCouponShowVO;
import com.jingyunbank.etrade.vip.coupon.bean.DiscountCouponVO;

@RestController
@RequestMapping("/api/vip/coupon/discountcoupon")
public class DiscountCouponController {

	@Autowired
	private IDiscountCouponService discountCouponService;
	
	/**
	 * 接口生成抵用券 有效期6个月
	 * @param request
	 * @param accessKey
	 * @param amount 数量
	 * @param discount折扣 若为8折传入8
	 * @param threshhold 使用门槛
	 * @param reason 原因
	 * @return
	 * @throws Exception
	 * 2016年1月12日 qxs
	 */
	@RequestMapping(value = "/{accessKey}/{amount}" ,method= RequestMethod.POST)
	public Result<List<DiscountCouponShowVO>> addMuti(HttpServletRequest request,
			@PathVariable String accessKey,
			@PathVariable int amount,
			@RequestParam(value="discount",defaultValue="8") BigDecimal discount,
			@RequestParam(value="threshhold",required=false,defaultValue="0") BigDecimal threshhold,
			@RequestParam(value="reason",required=false,defaultValue="") String  reason) throws Exception{
		if(!BaseCoupon.ACCESS_KEY_JYJR.equals(accessKey)){
			return Result.fail("accessKey 错误");
		}
		if(amount<=0){
			return Result.fail("数量错误");
		}
		if(discount.compareTo(new BigDecimal("0"))<=0 || discount.compareTo(new BigDecimal("10"))>=0){
			return Result.fail("折扣错误");
		}
		//组装数据
		DiscountCoupon bo = new DiscountCoupon();
		bo.setRemark(BaseCoupon.ACCESS_ID_JYJR+" "+reason);
		bo.setLocked(false);
		bo.setValue(new BigDecimal("50"));
		bo.setDiscount(new BigDecimal("10").subtract(discount).divide(new BigDecimal("10")));
		bo.setThreshhold(threshhold);
		bo.setStart(new Date());
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 6);
		bo.setEnd(c.getTime());
		return Result.ok(discountCouponService.saveMuti(bo, new Users(), amount).stream().map( result ->{
			DiscountCouponShowVO vo = new DiscountCouponShowVO();
			BeanUtils.copyProperties(result, vo);
	 		return vo;
	 	}).collect(Collectors.toList()));
		
	}
	
	/**
	 * 新增多张券
	 * @param request
	 * @param vo
	 * @param valid
	 * @param amount
	 * @return
	 * @throws Exception
	 * 2015年12月9日 qxs
	 */
	//@AuthBeforeOperation
	@RequestMapping(value = "/{amount}" ,method= RequestMethod.POST)
	public Result<String> addMuti(HttpServletRequest request
			,@RequestBody @Valid DiscountCouponAddVO vo
			,BindingResult valid
			,@PathVariable int amount) throws Exception{
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
						.map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
						.collect(Collectors.joining(" ; ")));
		}
		if(vo.getStart().after(vo.getEnd())
				|| vo.getEnd().before(new Date())){
			return Result.fail("有效期限设置错误");
		}
		vo.setDiscount(new BigDecimal("0.2"));
		vo.setValue(new BigDecimal("50"));
		if(amount<=0){
			return Result.fail("请设置正确数量");
		}
		Users manager = new Users();
		manager.setID(Login.UID(request));
		discountCouponService.saveMuti(getBoFromVo(vo), manager, amount);
		return Result.ok();
	}
	

	/**
	 * 判断卡号是否可以被激活
	 * 校验1、卡号2、有效期3、删除状态 4、是否已被使用
	 * @param code
	 * @return
	 * 2015年11月16日 qxs
	 */
	@RequestMapping(value="/can/active/{code}", method=RequestMethod.GET)
	public Result<String> canActive(@PathVariable String code) throws Exception{
		
		Result<DiscountCoupon> canActive = discountCouponService.canActive(code);
		if(canActive.isBad()){
			return Result.fail(canActive.getMessage());
		}
		return Result.ok();
	}
	
	/**
	 * 删除
	 * @param code
	 * @return
	 * 2015年11月16日 qxs
	 * @throws Exception 
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/", method=RequestMethod.DELETE)
	public Result<String> remove(String code, HttpServletRequest request) throws Exception{
		
		Users manager = new Users();
		manager.setID(Login.UID(request));
		if(discountCouponService.remove(code, manager)){
			return Result.ok();
		}
		return Result.fail("未知错误,请稍后重试!");
	}
	
	
	
	
	/**
	 * 列表查询
	 * @param vo
	 * @param page
	 * @return
	 * 2015年11月19日 qxs
	 */
	@RequestMapping(value="/list/{from}/{size}", method=RequestMethod.GET)
	public Result<List<DiscountCouponVO>> list(
			@PathVariable long from,
			@PathVariable long size,
			@RequestParam(required=false) String cardNum,
			@RequestParam(required=false) BigDecimal value,
			@RequestParam(required=false) Boolean locked){
		Range range =  new Range();
		range.setFrom(from);
		range.setTo(from + size);
		return Result.ok(discountCouponService.list(cardNum, value,locked , range)
		 	.stream().map( bo ->{
		 		DiscountCouponVO vo = new DiscountCouponVO();
				BeanUtils.copyProperties(bo, vo,"code");
				return vo;
		 	}).collect(Collectors.toList()));
	}
	
	/**
	 * 查询数量
	 * @param vo
	 * @param page
	 * @return
	 * 2015年11月19日 qxs
	 */
	@RequestMapping(value="/amount", method=RequestMethod.GET)
	public Result<Integer> getAmount(
			@RequestParam(required=false) String cardNum,
			@RequestParam(required=false) BigDecimal value,
			@RequestParam(required=false) Boolean locked){
		return Result.ok(discountCouponService.count(cardNum, value,locked));
	}
	
	/**
	 * 解锁
	 * @param ids
	 * @return
	 * 2015年12月29日 qxs
	 */
	@RequestMapping(value="/unlock/{ids}", method=RequestMethod.PUT)
	public Result<String> unlock(@PathVariable String ids){
		if(!StringUtils.isEmpty(ids)){
			discountCouponService.unlock(ids.split(","));
			return Result.ok("");
		}
		
		return Result.fail("请选择要解锁的优惠券");
	}
	private DiscountCoupon getBoFromVo(DiscountCouponAddVO vo) {
		if(vo!=null){
			DiscountCoupon bo = new DiscountCoupon();
			BeanUtils.copyProperties(vo, bo);
			return bo;
		}
		return null;
	}
	
	
}
