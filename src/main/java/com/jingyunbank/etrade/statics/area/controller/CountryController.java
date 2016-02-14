package com.jingyunbank.etrade.statics.area.controller;

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
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.statics.area.bo.Country;
import com.jingyunbank.etrade.api.statics.area.service.ICountryService;
import com.jingyunbank.etrade.statics.area.bean.CountryVO;

@RestController
@RequestMapping("/api/statics/area/country")
public class CountryController {
	
	@Autowired
	private ICountryService countryService;
	
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
	public Result<String> save(@Valid CountryVO vo, BindingResult valid) throws Exception{
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
						.map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
						.collect(Collectors.joining(" ; ")));
		}
		Country bo = new Country();
		BeanUtils.copyProperties(vo, bo);
		countryService.save(bo);
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
	public Result<String> remove(HttpServletRequest request,@PathVariable int id) throws Exception{
		if(countryService.remove(id)){
			return Result.ok("成功");
		}
		return Result.fail("服务器繁忙,请稍后再试");
	}
	
	
	/**
	 * 修改
	 * @param countryVO
	 * @return
	 * 2015年11月5日 qxs
	 * @throws DataRefreshingException 
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public Result<String> refresh(@PathVariable int id ,@Valid CountryVO countryVO , BindingResult valid) throws Exception{
		
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
						.map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
						.collect(Collectors.joining(" ; ")));
		}
		countryVO.setCountryID(id);
		Country country = new Country();
		BeanUtils.copyProperties(countryVO, country);
		//修改
		if(countryService.refresh(country)){
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
	public Result<CountryVO> getDetail(@PathVariable int id) throws Exception{
		Country country = countryService.single(id);
		if(country!=null){
			CountryVO vo = new CountryVO();
			BeanUtils.copyProperties(country, vo);
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
	public Result<List<CountryVO>> getList(CountryVO vo) throws Exception{
		Country country = new Country();
		BeanUtils.copyProperties(vo, country);
		return Result.ok(countryService.list(country)
				.stream().map( bo->{ 
					CountryVO c = new CountryVO();
					BeanUtils.copyProperties(bo, c);
					return c;
					}).collect(Collectors.toList())
				);
	}
	

}
