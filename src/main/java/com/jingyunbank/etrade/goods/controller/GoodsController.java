package com.jingyunbank.etrade.goods.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.test.web.servlet.result.RequestResultMatchers;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.util.CollectionUtils;
import com.jingyunbank.etrade.api.goods.bo.Goods;
import com.jingyunbank.etrade.api.goods.service.IGoodsService;
import com.jingyunbank.etrade.goods.bean.GoodsVO;
import com.jingyunbank.etrade.goods.service.GoodsService;

import jdk.nashorn.internal.ir.RuntimeNode.Request;

/**
 * Title: 商品controller
 * 
 * @author duanxf
 * @date 2015年11月4日
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Resource
	private IGoodsService goodsService;

	@RequestMapping(value = "/query/{goodsname}", method = RequestMethod.POST)
	public Result queryGoodsByName(HttpServletRequest request, @PathVariable String goodsname) throws Exception {
		List<Goods> bolist = goodsService.listGoodsByLikeName(goodsname);
		List<GoodsVO> goods = new ArrayList<GoodsVO>();
		if (bolist != null) {
				goods = CollectionUtils.copyTo(bolist, GoodsVO.class);
		}
//		for(GoodsVO v :goods){
//			System.out.println("name:"+v.getGoodname() +"重量:"+v.getWeight()+v.getUnit() +"现价:"+v.getDiscount_price()
//			+" 原价:"+v.getPrice()+"折扣:"+v.getDiscount());
//		}
		return Result.ok(goods);
	}
	@RequestMapping(value = "/brands", method = RequestMethod.POST)
	public Result queryBrands() throws Exception{
		System.out.println("queryBrands");
		List<GoodsVO> brands = new ArrayList<GoodsVO>();
		List<Goods> brandslist = goodsService.listBrands();
		if(brandslist!=null){
			brands = CollectionUtils.copyTo(brandslist, GoodsVO.class);
		}
		System.out.println("brands::"+brands.size());
		for(GoodsVO v :brands){
			System.out.println(v.getBrand_id() +"---"+v.getBrandname());
	}
		return Result.ok(brands);
	}
	
	
	
	@RequestMapping(value = "/types", method = RequestMethod.POST)
	public Result queryTypes() throws Exception{
		List<GoodsVO> types = new ArrayList<GoodsVO>();
		List<Goods> typeslist = goodsService.listTypes();
		if(typeslist!=null){
			types = CollectionUtils.copyTo(typeslist, GoodsVO.class);
		}
		for(GoodsVO v :types){
			System.out.println(v.getType_id() +"---"+v.getGood_type_name());
	}
		return Result.ok(types);
	}
}
