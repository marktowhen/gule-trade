package com.jingyunbank.etrade.goods.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.goods.bo.FootprintGoods;
import com.jingyunbank.etrade.goods.bean.FootprintGoodsVO;

/**
 * Title: 商品controller
 * 
 * @author duanxf
 * @date 2015年11月4日
 */
@RestController
@RequestMapping("/api/goods2")
public class GoodsController2 extends GoodsController{
	/**
	 * 我的足迹商品查询
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/footprint/list", method = RequestMethod.POST)
	public Result listFootprintGoods() throws Exception {
		List<FootprintGoods> goodslist = goodsService.listFootprintGoods();
		FootprintGoodsVO footprintGoodsVO = new FootprintGoodsVO();
		footprintGoodsVO.init(goodslist);
		return Result.ok(footprintGoodsVO);
	}
}
