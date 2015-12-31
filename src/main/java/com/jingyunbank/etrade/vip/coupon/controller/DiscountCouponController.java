package com.jingyunbank.etrade.vip.coupon.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.vip.coupon.bo.DiscountCoupon;
import com.jingyunbank.etrade.api.vip.coupon.service.IDiscountCouponService;
import com.jingyunbank.etrade.vip.coupon.bean.DiscountCouponVO;

@RestController
@RequestMapping("/api/vip/coupon/discountcoupon")
public class DiscountCouponController {

	@Autowired
	private IDiscountCouponService discountCouponService;
	/**
	 * 将形如yyyy-MM-dd HH:mm:ss的时间格式转正date类型
	 * @param request
	 * @param binder
	 * @throws Exception
	 * 2015年11月16日 qxs
	 */
	@InitBinder  
    protected void initBinder(HttpServletRequest request,  
            ServletRequestDataBinder binder) throws Exception {  
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
            CustomDateEditor editor = new CustomDateEditor(df, false);  
            binder.registerCustomEditor(Date.class, editor);  
    } 
	
	/**
	 * 新增一张券
	 * @param request
	 * @param vo
	 * @param valid
	 * @return
	 * 2015年11月16日 qxs
	 * @throws DataSavingException 
	 */
	//@AuthBeforeOperation
	@RequestMapping(value = "/" ,method= RequestMethod.POST)
	public Result<String> add(HttpServletRequest request, @RequestBody @Valid DiscountCouponVO vo,BindingResult valid) throws Exception{
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
		Users manager = new Users();
		manager.setID(ServletBox.getLoginUID(request));
		discountCouponService.save(getBoFromVo(vo), manager);
		return Result.ok();
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
			,@RequestBody @Valid DiscountCouponVO vo
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
		manager.setID(ServletBox.getLoginUID(request));
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
		manager.setID(ServletBox.getLoginUID(request));
		if(discountCouponService.remove(code, manager)){
			return Result.ok();
		}
		return Result.fail("未知错误");
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
		
		return Result.fail("请选择要解锁的券信息");
	}
	private DiscountCoupon getBoFromVo(DiscountCouponVO vo) {
		if(vo!=null){
			DiscountCoupon bo = new DiscountCoupon();
			BeanUtils.copyProperties(vo, bo);
			return bo;
		}
		return null;
	}
	
	
}
