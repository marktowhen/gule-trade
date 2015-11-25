package com.jingyunbank.etrade.user.controller;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Page;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.user.bo.Address;
import com.jingyunbank.etrade.api.user.service.IAddressService;
import com.jingyunbank.etrade.user.bean.AddressVO;
@RestController
@RequestMapping("/api/address")
public class AddressController {
	@Autowired
	private IAddressService addressService;
	

	/**
	 * 新增
	 * @param request
	 * @param addressVO
	 * @return 2015年11月5日 qxs
	 * @throws DataRefreshingException 
	 */
	@AuthBeforeOperation
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public Result add(HttpServletRequest request,@Valid AddressVO addressVO, BindingResult valid) throws Exception {
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
						.map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
						.collect(Collectors.joining(" ; ")));
		}
		Address address = new Address();
		BeanUtils.copyProperties(addressVO, address);
		address.setUID(ServletBox.getLoginUID(request));
		addressService.save(address);
		return Result.ok("保存成功");
	}

	/**
	 * 修改
	 * @param addressVO
	 * @return
	 * 2015年11月5日 qxs
	 * @throws DataRefreshingException 
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public Result refresh(@PathVariable String id ,@Valid AddressVO addressVO , BindingResult valid) throws Exception{
		
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
						.map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
						.collect(Collectors.joining(" ; ")));
		}
		addressVO.setID(id);
		Address address = getBoFromVo(addressVO);
		//修改
		if(addressService.refresh(address)){
			return Result.ok("成功");
		}
		return Result.fail("服务器繁忙,请稍后再试");
	}
	/**
	 * 设定默认接收地址
	 * @param addressVO
	 * @return
	 * 2015年11月5日 qxs
	 * @throws DataRefreshingException 
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/default/{id}",method=RequestMethod.PUT)
	public Result setDefualt(@PathVariable String id, HttpServletRequest request ) throws Exception{
		addressService.refreshDefualt(id, ServletBox.getLoginUID(request));
		return Result.ok("成功");
	}

	/**
	 * 删除
	 * 
	 * @param request
	 * @param id 多个id逗号分隔
	 * @return
	 * 2015年11月5日 qxs
	 * @throws DataRefreshingException 
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public Result remove(HttpServletRequest request,@PathVariable String id) throws Exception{
		if(addressService.remove(id.split(","), ServletBox.getLoginUID(request))){
			return Result.ok("成功");
		}
		return Result.fail("服务器繁忙,请稍后再试");
	}
	
	/**
	 * 查询详情
	 * @param request
	 * @param session
	 * @param addressVO
	 * @return
	 * 2015年11月5日 qxs
	 */
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public Result getDetail(@PathVariable String id) throws Exception{
		Optional<Address> optional = addressService.singleById(id);
		if(optional.isPresent()){
			return Result.ok(optional.get());
		}
		return Result.fail("地址不存在");
	}
	
	/**
	 * 查询默认地址
	 * @param request
	 * @param session
	 * @param addressVO
	 * @return
	 * 2015年11月5日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/default",method=RequestMethod.GET)
	public Result getDefaultAddress(HttpServletRequest request)throws Exception{
		
		Optional<Address> optional = addressService.getDefaultAddress(ServletBox.getLoginUID(request));
		if(optional.isPresent()){
			return Result.ok(optional.get());
		}
		return Result.ok("地址不存在");
	}
	
	/**
	 * 登录用户的所有有效地址
	 * @param request
	 * @param session
	 * @param addressVO
	 * @return
	 * 2015年11月5日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/all",method=RequestMethod.GET)
	public Result queryAll(HttpServletRequest request) throws Exception{
		List<AddressVO> result = new ArrayList<AddressVO>();
		String uid = ServletBox.getLoginUID(request);
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
	 * 列表查询
	 * @param uid
	 * @param page
	 * @return
	 * @throws Exception
	 * 2015年11月20日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public Result queryPage( String uid,Page page )throws Exception{
		Range range = new Range();
		range.setFrom((page.getOffset()));
		range.setTo(page.getOffset()+page.getSize());
		return Result.ok(addressService.list(uid, range).stream().map( bo->{
			return getVoFrombo(bo);
		}).collect(Collectors.toList()));
		
	}
	
	/**
	 * 查询数量
	 * 
	 * @param request
	 * @param addressVO
	 * @return
	 * 2015年11月5日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/amount",method=RequestMethod.GET)
	public Result getAmount(HttpServletRequest request,AddressVO addressVO  )throws Exception{
		return Result.ok(addressService.getAmount(ServletBox.getLoginUID(request)));
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
