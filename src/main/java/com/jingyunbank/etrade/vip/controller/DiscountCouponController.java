package com.jingyunbank.etrade.vip.controller;

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
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.vip.bo.DiscountCoupon;
import com.jingyunbank.etrade.api.vip.service.IDiscountCouponService;
import com.jingyunbank.etrade.vip.bean.DiscountCouponVO;

@RestController
@RequestMapping("/api/discountcoupon")
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
	@AuthBeforeOperation
	@RequestMapping(value = "/" ,method= RequestMethod.PUT)
	public Result add(HttpServletRequest request, @Valid DiscountCouponVO vo,BindingResult valid) throws Exception{
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
		vo.setID(KeyGen.uuid());
		Users manager = new Users();
		manager.setID(ServletBox.getLoginUID(request));
		discountCouponService.save(getBoFromVo(vo), manager);
		return Result.ok();
	}
	

	/**
	 * 判断卡号是否可以被激活
	 * 校验1、卡号2、有效期3、删除状态 4、是否已被使用
	 * @param code
	 * @return
	 * 2015年11月16日 qxs
	 */
	@RequestMapping(value="isvalid", method=RequestMethod.GET)
	public Result isValid(String code) throws Exception{
		
		return discountCouponService.isValid(code);
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
	public Result remove(String code, HttpServletRequest request) throws Exception{
		
		Users manager = new Users();
		manager.setID(ServletBox.getLoginUID(request));
		if(discountCouponService.remove(code, manager)){
			return Result.ok();
		}
		return Result.fail("未知错误");
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
