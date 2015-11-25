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
import com.jingyunbank.etrade.api.area.bo.Province;
import com.jingyunbank.etrade.api.area.service.IProvinceService;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.area.bean.ProvinceVO;

@RestController
@RequestMapping("/api/province")
public class ProvinceController {
	
	@Autowired
	private IProvinceService provinceService;
	
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
	public Result save(@Valid ProvinceVO vo, BindingResult valid) throws Exception{
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
						.map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
						.collect(Collectors.joining(" ; ")));
		}
		Province bo = new Province();
		BeanUtils.copyProperties(vo, bo);
		provinceService.save(bo);
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
		if(provinceService.remove(id)){
			return Result.ok("成功");
		}
		return Result.fail("服务器繁忙,请稍后再试");
	}
	
	
	/**
	 * 修改
	 * @param provinceVO
	 * @return
	 * 2015年11月5日 qxs
	 * @throws DataRefreshingException 
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public Result refresh(@PathVariable int id ,@Valid ProvinceVO provinceVO , BindingResult valid) throws Exception{
		
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
						.map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
						.collect(Collectors.joining(" ; ")));
		}
		provinceVO.setProvinceID(id);
		Province province = new Province();
		BeanUtils.copyProperties(provinceVO, province);
		//修改
		if(provinceService.refresh(province)){
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
		Province province = provinceService.single(id);
		if(province!=null){
			ProvinceVO vo = new ProvinceVO();
			BeanUtils.copyProperties(province, vo);
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
	public Result getList(ProvinceVO vo) throws Exception{
		Province province = new Province();
		BeanUtils.copyProperties(vo, province);
		return Result.ok(provinceService.list(province)
				.stream().map( bo->{ 
					Province c = new Province();
					BeanUtils.copyProperties(bo, c);
					return c;
					}).collect(Collectors.toList())
				);
	}
	
	/**
	 * 查询
	 * @param countryID
	 * @return
	 * 2015年11月5日 qxs
	 */
	@RequestMapping(value="/list/{countryID}",method=RequestMethod.GET)
	public Result getListByCountry(@PathVariable int countryID) throws Exception{
		return Result.ok(provinceService.listByCountry(countryID)
				.stream().map( bo->{ 
					Province c = new Province();
					BeanUtils.copyProperties(bo, c);
					return c;
					}).collect(Collectors.toList())
				);
	}

}
