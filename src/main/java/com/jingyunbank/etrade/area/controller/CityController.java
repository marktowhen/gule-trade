package com.jingyunbank.etrade.area.controller;

import java.util.Arrays;
import java.util.List;
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

import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.etrade.api.area.bo.City;
import com.jingyunbank.etrade.api.area.service.ICityService;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.area.bean.CityVO;

@RestController
@RequestMapping("/api/city")
public class CityController {
	
	@Autowired
	private ICityService cityService;
	
	/**
	 * 新增
	 * @param vo
	 * @param valid
	 * @return
	 * @throws Exception
	 * 2015年11月18日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public Result save(@Valid CityVO vo, BindingResult valid) throws Exception{
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
						.map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
						.collect(Collectors.joining(" ; ")));
		}
		City bo = new City();
		BeanUtils.copyProperties(vo, bo);
		cityService.save(bo);
		return Result.ok();
	}
	
	/**
	 * 删除
	 * 
	 * @param request
	 * @param id 
	 * @return
	 * 2015年11月5日 qxs
	 * @throws DataRefreshingException 
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public Result remove(HttpServletRequest request,@PathVariable int id) throws Exception{
		if(cityService.remove(id)){
			return Result.ok("成功");
		}
		return Result.fail("服务器繁忙,请稍后再试");
	}
	
	
	/**
	 * 修改
	 * @param cityVO
	 * @return
	 * 2015年11月5日 qxs
	 * @throws DataRefreshingException 
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public Result refresh(@PathVariable int id ,@Valid CityVO cityVO , BindingResult valid) throws Exception{
		
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
						.map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
						.collect(Collectors.joining(" ; ")));
		}
		cityVO.setCityID(id);
		City city = new City();
		BeanUtils.copyProperties(cityVO, city);
		//修改
		if(cityService.refresh(city)){
			return Result.ok("成功");
		}
		return Result.fail("服务器繁忙,请稍后再试");
	}
	
	/**
	 * 详情
	 * @param id
	 * @return
	 * 2015年11月5日 qxs
	 */
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public Result getDetail(@PathVariable int id) throws Exception{
		City city = cityService.single(id);
		if(city!=null){
			CityVO vo = new CityVO();
			BeanUtils.copyProperties(city, vo);
			return Result.ok(vo);
		}
		return Result.fail("地址不存在");
	}
	
	/**
	 * 查询
	 * @param id
	 * @return
	 * 2015年11月5日 qxs
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public Result getList(CityVO vo) throws Exception{
		City city = new City();
		BeanUtils.copyProperties(vo, city);
		return Result.ok(cityService.list(city)
				.stream().map( bo->{ 
					City c = new City();
					BeanUtils.copyProperties(bo, c);
					return c;
					}).collect(Collectors.toList())
				);
	}
	
	/**
	 * 查询
	 * @param id
	 * @return
	 * 2015年11月5日 qxs
	 */
	@RequestMapping(value="/list/{provinceID}",method=RequestMethod.GET)
	public Result getList(@PathVariable int provinceID) throws Exception{
		return Result.ok(cityService.listByProvince(provinceID)
				.stream().map( bo->{ 
					City c = new City();
					BeanUtils.copyProperties(bo, c);
					return c;
					}).collect(Collectors.toList())
				);
	}

}
