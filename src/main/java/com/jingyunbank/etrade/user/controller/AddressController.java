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
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Page;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
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
	 */
	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public Result add(HttpServletRequest request,@Valid AddressVO addressVO, BindingResult valid) throws DataSavingException {
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
	@RequestMapping(value="/{id}",method=RequestMethod.POST)
	public Result refresh(@PathVariable String id ,@Valid AddressVO addressVO , BindingResult valid) throws DataRefreshingException{
		
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
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public Result setDefualt(@PathVariable String id, HttpServletRequest request ) throws DataRefreshingException{
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
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public Result remove(HttpServletRequest request,@PathVariable String id) throws DataRefreshingException{
		String uid = ServletBox.getLoginUID(request);
		if(!StringUtils.isEmpty(uid)){
			AddressVO addressVO = new AddressVO();
			addressVO.setUID(uid);
			String [] IDArray = id.split(",");
			addressVO.setIDArray(IDArray);
			if(addressService.remove(getBoFromVo(addressVO))){
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
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public Result queryDetail(@PathVariable String id){
		Optional<Address> optional = addressService.singleById(id);
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
	public Result queryAll(HttpServletRequest request){
		List<AddressVO> result = new ArrayList<AddressVO>();
		String uid = ServletBox.getLoginUID(request);
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
	 * @param addressVO
	 * @return
	 * 2015年11月5日 qxs
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public Result queryPage(HttpServletRequest request,AddressVO addressVO,Page page ){
		List<AddressVO> result = new ArrayList<AddressVO>();
		if(addressVO==null){
			addressVO = new AddressVO();
		}
		String uid = ServletBox.getLoginUID(request);
		if(!StringUtils.isEmpty(uid)){
			return Result.fail("请先登录");
		}
		addressVO.setUID(uid);
		Range range = new Range();
		range.setFrom((page.getOffset()));
		range.setTo(page.getOffset()+page.getSize());
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
