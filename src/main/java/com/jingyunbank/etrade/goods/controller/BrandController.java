package com.jingyunbank.etrade.goods.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**
 * 品牌操作管理
* Title: BrandController
* @author    duanxf
* @date      2015年12月15日
 */

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.goods.bo.Brand;
import com.jingyunbank.etrade.api.goods.service.IBrandService;
import com.jingyunbank.etrade.goods.bean.BrandVO;
import com.jingyunbank.etrade.goods.bean.GoodsBrandVO;

@RestController
@RequestMapping("/api/brand")
public class BrandController {
	@Resource
	private IBrandService brandService;

	/**
	 * 保存品牌
	 * 
	 * @param request
	 * @param vo
	 * @param valid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Result saveBrand(HttpServletRequest request, @RequestBody @Valid BrandVO vo, BindingResult valid)
			throws Exception {
		// 异常信息
		if (valid.hasErrors()) {
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream().map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
					.collect(Collectors.joining(" ; ")));
		}

		Brand brand = new Brand();
		BeanUtils.copyProperties(vo, brand);
		brand.setID(KeyGen.uuid());
		brand.setAdmin_sort(0);
		brand.setStatus(true);
		if (brandService.save(brand)) {
			return Result.ok("success");
		} else {
			return Result.fail("fail");
		}

	}

	/**
	 * 根据ID 获取Brand
	 * @param bid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateveiw/{bid}", method = RequestMethod.GET)
	public Result queryBrandById(@PathVariable String bid) throws Exception {
		BrandVO brand = null;
		Optional<Brand> bo = brandService.singleById(bid);
		if (Objects.nonNull(bo)) {
			brand = new BrandVO();
			BeanUtils.copyProperties(bo.get(), brand);
		}
		return Result.ok(brand);
	}
	
	
	@RequestMapping(value = "/update/{bid}", method = RequestMethod.POST)
	public Result updateBrand(HttpServletRequest request,@PathVariable String bid,  @RequestBody @Valid BrandVO vo, BindingResult valid)
			throws Exception {
		// 异常信息
		if (valid.hasErrors()) {
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream().map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
					.collect(Collectors.joining(" ; ")));
		}
		
		Brand brand = new Brand();
		BeanUtils.copyProperties(vo, brand);
		brand.setID(bid);
		
		if (brandService.refreshBrand(brand)) {
			return Result.ok("success");
		} else {
			return Result.fail("fail");
		}
	}

	
	/**
	 * 根据MID 查询所属的品牌
	 * @param mid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/brands/{mid}", method = RequestMethod.GET)
	public Result<List<BrandVO>> getBrandByMid(@PathVariable String mid) throws Exception{
		List<BrandVO> list = brandService.listBrandsByMid(mid).stream().map(bo -> {
			BrandVO vo = new BrandVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}
	
	
	
	@RequestMapping(value = "/{bid}", method = RequestMethod.PUT)
	public Result delBrand(@PathVariable String bid) throws Exception{
		if(brandService.delBrand(bid)){
			return Result.ok("success");
		}
		return Result.fail("fail");
	}
	
	
	
	
	/**
	 * 根据MID 查询所属的品牌
	 * @param mid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/brands", method = RequestMethod.GET)
	public Result<List<BrandVO>> getBrands() throws Exception{
		List<BrandVO> list = brandService.listBrands().stream().map(bo -> {
			BrandVO vo = new BrandVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}
	
	
	
}
