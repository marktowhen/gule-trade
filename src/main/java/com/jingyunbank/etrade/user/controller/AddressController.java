package com.jingyunbank.etrade.user.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.lang.Patterns;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.user.bo.Address;
import com.jingyunbank.etrade.api.user.service.IAddressService;
import com.jingyunbank.etrade.base.Page;
import com.jingyunbank.etrade.base.util.RequestUtil;
import com.jingyunbank.etrade.infrastructure.SmsMessager;
import com.jingyunbank.etrade.user.bean.AddressVO;
@RestController
@RequestMapping("/api/address")
public class AddressController {
	@Autowired
	private IAddressService addressService;
	
	@Autowired
	private SmsMessager smsMessager;

	/**
	 * 新增
	 * 
	 * @param request
	 * @param session
	 * @param addressVO
	 * @return 2015年11月5日 qxs
	 */
	@RequestMapping(value = "/", method = RequestMethod.PUT)
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
	 * @param request
	 * @param session
	 * @param addressVO
	 * @return
	 * 2015年11月5日 qxs
	 * @throws DataRefreshingException 
	 */
	@RequestMapping(value="/{ID}",method=RequestMethod.POST)
	public Result refresh(HttpServletRequest request, HttpSession session,AddressVO addressVO ) throws DataRefreshingException{
		//校验
		if(addressVO==null){
			return Result.fail("参数为空");
		}
		if(StringUtils.isEmpty(addressVO.getID())){
			return Result.fail("缺少必要参数");
		}
		if(!StringUtils.isEmpty(addressVO.getName())&&addressVO.getName().getBytes().length>20){
			return Result.fail("标题超长");
		}
		if(StringUtils.isEmpty(addressVO.getAddress())){
			return Result.fail("收货地址不能为空");
		}
		if(addressVO.getName().getBytes().length>80){
			return Result.fail("收货地址超长");
		}
		if(StringUtils.isEmpty(addressVO.getReceiver())){
			return Result.fail("收货人不能为空");
		}
		if(addressVO.getReceiver().getBytes().length>30){
			return Result.fail("收货人超长");
		}
		Pattern p = Pattern.compile(Patterns.INTERNAL_MOBILE_PATTERN);
		if(!p.matcher(addressVO.getMobile()).matches()){
			return Result.fail("手机号格式错误");
		}
		if(!StringUtils.isEmpty(addressVO.getTelephone())&&addressVO.getTelephone().getBytes().length>20){
			return Result.fail("座机号码超长");
		}
		Address address = getBoFromVo(addressVO);

		//修改
		if(addressService.refresh(address)){
			return Result.ok("成功");
		}
		return Result.fail("服务器繁忙,请稍后再试");
	}

	/**
	 * 删除
	 * 
	 * @param request
	 * @param session
	 * @param addressVO
	 * @param ID 多个id逗号分隔
	 * @return
	 * 2015年11月5日 qxs
	 * @throws DataRefreshingException 
	 */
	@RequestMapping(value="/{ID}",method=RequestMethod.DELETE)
	public Result delete(HttpServletRequest request, HttpSession session,AddressVO addressVO) throws DataRefreshingException{
		String uid = RequestUtil.getLoginId(request);
		if(!StringUtils.isEmpty(uid)){
			addressVO.setUID(uid);
			String [] IDArray = addressVO.getID().split(",");
			addressVO.setIDArray(IDArray);
			if(addressService.delete(getBoFromVo(addressVO))){
				return Result.ok("成功");
			}
			return Result.fail("服务器繁忙,请稍后再试");
		}
		return Result.fail("请先登录");
	}
	
	/**
	 * 查询详情
	 * @param request
	 * @param session
	 * @param addressVO
	 * @return
	 * 2015年11月5日 qxs
	 */
	@RequestMapping(value="/{ID}",method=RequestMethod.GET)
	public Result queryDetail(HttpServletRequest request, HttpSession session,AddressVO addressVO ){
		if(addressVO.getID()==null || "".equals(addressVO.getID())){
			return Result.ok("缺少必要参数");
		}
		Optional<Address> optional = addressService.singleById(addressVO.getID());
		if(optional.isPresent()){
			return Result.ok(optional.get());
		}
		return Result.ok("地址不存在");
	}
	
	/**
	 * 登录用户的所有地址
	 * @param request
	 * @param session
	 * @param addressVO
	 * @return
	 * 2015年11月5日 qxs
	 */
	@RequestMapping(value="/all",method=RequestMethod.GET)
	public Result queryAll(HttpServletRequest request, HttpSession session,AddressVO addressVO ){
		List<AddressVO> result = new ArrayList<AddressVO>();
		if(addressVO==null){
			addressVO = new AddressVO();
		}
		String uid = RequestUtil.getLoginId(request);
		if(!StringUtils.isEmpty(uid)){
			return Result.fail("请先登录");
		}
		List<Address> list = addressService.list(uid);
		//格式转换
		if(list!=null && !list.isEmpty()){
			for (Address address : list) {
				result.add(getVoFrombo(address));
			}
		}
		return Result.ok(result);
	}

	/**
	 * 分页查询
	 * 
	 * @param request
	 * @param session
	 * @param addressVO
	 * @return
	 * 2015年11月5日 qxs
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public Result queryPage(HttpServletRequest request, HttpSession session,AddressVO addressVO,Page page ){
		List<AddressVO> result = new ArrayList<AddressVO>();
		if(addressVO==null){
			addressVO = new AddressVO();
		}
		String uid = RequestUtil.getLoginId(request);
		if(!StringUtils.isEmpty(uid)){
			return Result.fail("请先登录");
		}
		addressVO.setUID(uid);
		Range range = new Range();
		range.setFrom((page.getOffset()-1)*page.getSize());
		range.setTo(page.getOffset()*page.getSize());
		List<Address> list = addressService.listPage(getBoFromVo(addressVO), range);
		//格式转换
		if(list!=null && !list.isEmpty()){
			for (Address address : list) {
				result.add(getVoFrombo(address));
			}
		}
		return Result.ok(result);
	}
	
	
	/**
	 * vo 转 bo
	 * @param vo
	 * @return bo
	 * 2015年11月5日 qxs
	 */
	private Address getBoFromVo(AddressVO vo){
		Address bo = null;
		if(vo!=null){
			bo = new Address();
			BeanUtils.copyProperties(vo, bo);
		}
		return bo;
	}
	
	/**
	 * bo转vo
	 * @param vo
	 * @return bo
	 * 2015年11月5日 qxs
	 */
	private AddressVO getVoFrombo(Address bo){
		AddressVO vo = null;
		if(bo!=null){
			vo = new AddressVO();
			BeanUtils.copyProperties(bo, vo);
		}
		return vo;
	}
	
}
