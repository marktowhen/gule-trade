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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Page;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.util.UniqueSequence;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.vip.bo.CashCoupon;
import com.jingyunbank.etrade.api.vip.service.ICashCouponService;
import com.jingyunbank.etrade.vip.bean.CashCouponVO;

@RestController
@RequestMapping("/api/cashcoupon")
public class CashCouponController {
	
	@Autowired
	private ICashCouponService cashCouponService;
	
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
	public Result<String> add(HttpServletRequest request, @Valid CashCouponVO vo,BindingResult valid) throws Exception{
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
		vo.setCode(String.valueOf(UniqueSequence.next18()));
		Users manager = new Users();
		manager.setID(ServletBox.getLoginUID(request));
		cashCouponService.save(getBoFromVo(vo), manager);
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
		
		return cashCouponService.canActive(code);
	}
	
	/**
	 * 删除
	 * @param code
	 * @return
	 * 2015年11月16日 qxs
	 * @throws DataRemovingException 
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/", method=RequestMethod.DELETE)
	public Result<String> remove(String code, HttpServletRequest request) throws Exception{
		
		CashCoupon cashCoupon = new CashCoupon();
		cashCoupon.setCode(code);
		Users manager = new Users();
		manager.setID(ServletBox.getLoginUID(request));
		if(cashCouponService.remove(code, manager)){
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
	@AuthBeforeOperation
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public Result<List<CashCouponVO>> getList(CashCouponVO vo, Page page){
		Range range = null;
		if(page!=null){
			range = new Range();
			range.setFrom(page.getOffset());
			range.setTo(page.getOffset()+page.getSize());
		}
		return Result.ok(cashCouponService.listAll(getBoFromVo(vo), range)
		 	.stream().map( bo ->{
		 		return getVoFromBo(bo);
		 	}).collect(Collectors.toList()));
	}
	
	/**
	 * 查询数量
	 * @param vo
	 * @param page
	 * @return
	 * 2015年11月19日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/amount", method=RequestMethod.GET)
	public Result<Integer> getAmount(CashCouponVO vo){
		return Result.ok(cashCouponService.getAmount(getBoFromVo(vo)));
	}
	
	
	
	private CashCoupon getBoFromVo(CashCouponVO vo){
		if(vo!=null){
			CashCoupon bo = new CashCoupon();
			BeanUtils.copyProperties(vo, bo);
			return bo;
		}
		return null;
	}
	
	private CashCouponVO getVoFromBo(CashCoupon bo){
		if(bo!=null){
			CashCouponVO vo = new CashCouponVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}
		return null;
	}

}
