package com.jingyunbank.etrade.user.controller;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.lang.Patterns;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.user.bo.Address;
import com.jingyunbank.etrade.api.user.service.IAddressService;
import com.jingyunbank.etrade.user.bean.AddressVO;

@RestController
@RequestMapping("/address")
public class AddressController {
	@Autowired
	private IAddressService addressService;

	/**
	 * 新增
	 * 
	 * @param request
	 * @param session
	 * @param addressVO
	 * @return 2015年11月5日 qxs
	 */
	@RequestMapping(value = "/add", method = RequestMethod.PUT)
	public Result add(HttpServletRequest request, HttpSession session, AddressVO addressVO) throws DataSavingException {
		/*
		 * if(addressVO==null){ return Result.fail("参数传入错误"); }
		 */
		if(addressVO.getMobile()==null){
			return Result.fail("手机号码不能为空");
		}
		if(addressVO.getMobile()!=null){
			Pattern pr=Pattern.compile(Patterns.INTERNAL_MOBILE_PATTERN);
			if(!pr.matcher(addressVO.getMobile()).matches()){
				return Result.fail("手机号码不符合规格");
			}
		}
		Address address = new Address();
		BeanUtils.copyProperties(addressVO, address);
		addressService.save(address);
		return Result.ok(addressVO);
	}

	/**
	 * 修改
	 * 
	 * @param request
	 * @param session
	 * @param addressVO
	 * @return 2015年11月5日 qxs
	 */
	@RequestMapping(value = "/refresh", method = RequestMethod.POST)
	public Result refresh(HttpServletRequest request, HttpSession session, AddressVO addressVO) {

		return Result.ok("成功");
	}

	/**
	 * 删除
	 * 
	 * @param request
	 * @param session
	 * @param addressVO
	 * @return 2015年11月5日 qxs
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Result delete(HttpServletRequest request, HttpSession session, AddressVO addressVO) {

		return Result.ok("成功");
	}

	/**
	 * 查询详情
	 * 
	 * @param request
	 * @param session
	 * @param addressVO
	 * @return 2015年11月5日 qxs
	 */
	@RequestMapping(value = "/queryDetail", method = RequestMethod.GET)
	public Result queryDetail(HttpServletRequest request, HttpSession session, AddressVO addressVO) {

		return Result.ok("成功");
	}

	/**
	 * 分页查询
	 * 
	 * @param request
	 * @param session
	 * @param addressVO
	 * @return 2015年11月5日 qxs
	 */
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public Result query(HttpServletRequest request, HttpSession session, AddressVO addressVO) {

		return Result.ok("成功");
	}
}
