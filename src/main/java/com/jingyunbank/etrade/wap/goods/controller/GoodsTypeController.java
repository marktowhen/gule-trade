package com.jingyunbank.etrade.wap.goods.controller;

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

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsType;
import com.jingyunbank.etrade.api.wap.goods.service.IGoodsTypeService;
import com.jingyunbank.etrade.wap.goods.bean.GoodsTypeVO;

@RestController
@RequestMapping("/api/goods/type")
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
	public Result<String> saveGoodsType(HttpServletRequest request, @RequestBody @Valid GoodsTypeVO vo,
			BindingResult valid) throws Exception {
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
		goodsTypeService.save(goodsType);
		return Result.ok("success");

	}

	/**
	 * 根据ID 获取GoodsTypeVO
	 * 
	 * @param bid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/single/{tid}", method = RequestMethod.GET)
	public Result<GoodsTypeVO> queryGoodsTypeById(@PathVariable String tid) throws Exception {
		GoodsTypeVO goodsTypeVO = null;
		Optional<GoodsType> bo = goodsTypeService.singleById(tid);
		if (Objects.nonNull(bo)) {
			goodsTypeVO = new GoodsTypeVO();
			BeanUtils.copyProperties(bo.get(), goodsTypeVO);
		}
		return Result.ok(goodsTypeVO);
	}

	/**
	 * 更新类别
	 * 
	 * @param request
	 * @param bid
	 * @param vo
	 * @param valid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/update/{tid}", method = RequestMethod.POST)
	public Result<String> updateBrand(HttpServletRequest request, @PathVariable String tid,
			@RequestBody @Valid GoodsTypeVO vo, BindingResult valid) throws Exception {
		// 异常信息
		if (valid.hasErrors()) {
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream().map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
					.collect(Collectors.joining(" ; ")));
		}

		GoodsType goodsType = new GoodsType();
		BeanUtils.copyProperties(vo, goodsType);
		goodsType.setID(tid);
		goodsTypeService.refreshGoodsType(goodsType);
		return Result.ok("success");
	}

	/**
	 * 根据名称查询
	 * 
	 * @param mid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/singleByName/{name}", method = RequestMethod.GET)
	public Result<List<GoodsTypeVO>> listTypesByName(@PathVariable String name) throws Exception {
		List<GoodsTypeVO> list = goodsTypeService.listGoodsTypesByName(name).stream().map(bo -> {
			GoodsTypeVO vo = new GoodsTypeVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}

	@RequestMapping(value = "/del/{tid}", method = RequestMethod.PUT)
	public Result<String> delGoodsType(@PathVariable String tid) throws Exception {
		goodsTypeService.delGoodsType(tid);
		return Result.ok("success");
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<List<GoodsTypeVO>> getGoodsTypes() throws Exception {
		List<GoodsTypeVO> list = goodsTypeService.listGoodsTypes().stream().map(bo -> {
			GoodsTypeVO vo = new GoodsTypeVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}
}
