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
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsAttr;
import com.jingyunbank.etrade.api.wap.goods.service.IGoodsAttrService;
import com.jingyunbank.etrade.wap.goods.bean.GoodsAttrVO;

@RestController
@RequestMapping("/api/goods/attr")
public class GoodsAttrController {
	@Resource
	private IGoodsAttrService goodsAttrService;

	/**
	 * 保存属性
	 * 
	 * @param request
	 * @param vo
	 * @param valid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Result<String> saveGoodsAttr(HttpServletRequest request, @RequestBody @Valid GoodsAttrVO vo,
			BindingResult valid) throws Exception {
		if (valid.hasErrors()) {
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream().map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
					.collect(Collectors.joining(" ; ")));
		}

		GoodsAttr goodsAttr = new GoodsAttr();
		BeanUtils.copyProperties(vo, goodsAttr);
		goodsAttr.setID(KeyGen.uuid());
		goodsAttrService.save(goodsAttr);
		return Result.ok("success");

	}

	/**
	 * 根据id获取属性
	 * 
	 * @param aid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/single/{aid}", method = RequestMethod.GET)
	public Result<GoodsAttrVO> queryGoodsTypeById(@PathVariable String aid) throws Exception {
		Optional<GoodsAttr> attr = goodsAttrService.singleById(aid);
		GoodsAttrVO vo = null;
		if (Objects.nonNull(attr)) {
			vo = new GoodsAttrVO();
			BeanUtils.copyProperties(attr.get(), vo);
		}
		return Result.ok(vo);
	}

	/**
	 * 修改类型属性
	 * 
	 * @param request
	 * @param vo
	 * @param valid
	 * @param aid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/update/{aid}", method = RequestMethod.POST)
	public Result<String> updateGoodsAttr(HttpServletRequest request, @RequestBody @Valid GoodsAttrVO vo,
			BindingResult valid, @PathVariable String aid) throws Exception {
		if (valid.hasErrors()) {
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream().map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
					.collect(Collectors.joining(" ; ")));
		}
		GoodsAttr goodsAttr = new GoodsAttr();
		BeanUtils.copyProperties(vo, goodsAttr);
		goodsAttr.setID(aid);
		goodsAttrService.refreshGoodsAttr(goodsAttr);
		return Result.ok("success");
	}

	/**
	 * 删除当前属性
	 * 
	 * @param aid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/del/{aid}", method = RequestMethod.PUT)
	public Result<String> delGoodsAttr(@PathVariable String aid) throws Exception {
		goodsAttrService.romoveGoodsAttr(aid);
		return Result.ok("success");
	}

	/**
	 * 查询全部属性
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<List<GoodsAttrVO>> getGoodsAttr() throws Exception {
		List<GoodsAttrVO> list = goodsAttrService.listGoodsAttr().stream().map(bo -> {
			GoodsAttrVO vo = new GoodsAttrVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}
}
