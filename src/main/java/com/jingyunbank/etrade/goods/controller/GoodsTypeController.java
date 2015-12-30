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
import com.jingyunbank.etrade.api.goods.bo.GoodsType;
import com.jingyunbank.etrade.api.goods.service.IGoodsTypeService;
import com.jingyunbank.etrade.goods.bean.GoodsTypesVO;

@RestController
@RequestMapping("/api/goodstype")
public class GoodsTypeController {
	@Resource
	private IGoodsTypeService goodsTypeService;
	/**
	 * 保存类别
	 * 
	 * @param request
	 * @param vo
	 * @param valid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Result<String> saveGoodsType(HttpServletRequest request, @RequestBody @Valid GoodsTypesVO vo, BindingResult valid)
			throws Exception {
		// 异常信息
		if (valid.hasErrors()) {
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream().map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
					.collect(Collectors.joining(" ; ")));
		}

		GoodsType goodsType = new GoodsType();
		BeanUtils.copyProperties(vo, goodsType);
		goodsType.setID(KeyGen.uuid());
		goodsType.setStatus(true);
		if (goodsTypeService.save(goodsType)) {
			return Result.ok("success");
		} else {
			return Result.fail("fail");
		}

	}
	/**
	 * 根据ID 获取GoodsTypesVO
	 * @param bid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/single/{tid}", method = RequestMethod.GET)
	public Result<GoodsTypesVO> queryGoodsTypeById(@PathVariable String tid) throws Exception {
		GoodsTypesVO goodsTypesVO = null;
		Optional<GoodsType> bo = goodsTypeService.singleById(tid);
		if (Objects.nonNull(bo)) {
			goodsTypesVO = new GoodsTypesVO();
			BeanUtils.copyProperties(bo.get(), goodsTypesVO);
		}
		return Result.ok(goodsTypesVO);
	}
	/**
	 * 更新类别
	 * @param request
	 * @param bid
	 * @param vo
	 * @param valid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/update/{tid}", method = RequestMethod.POST)
	public Result<String> updateBrand(HttpServletRequest request,@PathVariable String tid,  @RequestBody @Valid GoodsTypesVO vo, BindingResult valid)
			throws Exception {
		// 异常信息
		if (valid.hasErrors()) {
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream().map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
					.collect(Collectors.joining(" ; ")));
		}
		
		GoodsType goodsType = new GoodsType();
		BeanUtils.copyProperties(vo, goodsType);
		goodsType.setID(tid);
		if (goodsTypeService.refreshGoodsType(goodsType)) {
			return Result.ok("success");
		} else {
			return Result.fail("fail");
		}
	}
	/**
	 * 根据名称查询
	 * @param mid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list/{name}", method = RequestMethod.GET)
	public Result<List<GoodsTypesVO>> listTypesByName(@PathVariable String name) throws Exception{
		List<GoodsTypesVO> list = goodsTypeService.listGoodsTypesByName(name).stream().map(bo -> {
			GoodsTypesVO vo = new GoodsTypesVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}
	
	@RequestMapping(value = "/del/{tid}", method = RequestMethod.PUT)
	public Result<String> delGoodsType(@PathVariable String tid) throws Exception{
		if(goodsTypeService.delGoodsType(tid)){
			return Result.ok("success");
		}
		return Result.fail("fail");
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<List<GoodsTypesVO>> getGoodsTypes() throws Exception{
		List<GoodsTypesVO> list = goodsTypeService.listGoodsTypes().stream().map(bo -> {
			GoodsTypesVO vo = new GoodsTypesVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}
}
