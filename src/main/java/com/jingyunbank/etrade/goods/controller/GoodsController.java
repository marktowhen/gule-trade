package com.jingyunbank.etrade.goods.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.util.CollectionUtils;
import com.jingyunbank.etrade.api.goods.bo.Goods;
import com.jingyunbank.etrade.api.goods.service.IGoodsService;
import com.jingyunbank.etrade.goods.bean.GoodsVO;
import com.jingyunbank.etrade.goods.bean.HotGoodsVO;

/**
 * Title: 商品controller
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
		return Result.ok(goods);
	}

	@RequestMapping(value = "/brands", method = RequestMethod.POST)
	public Result queryBrands() throws Exception {
		List<GoodsVO> brands = new ArrayList<GoodsVO>();
		List<Goods> brandslist = goodsService.listBrands();
		if (brandslist != null) {
			brands = CollectionUtils.copyTo(brandslist, GoodsVO.class);
		}
		return Result.ok(brands);
	}

	@RequestMapping(value = "/types", method = RequestMethod.POST)
	public Result queryTypes() throws Exception {
		List<GoodsVO> types = new ArrayList<GoodsVO>();
		List<Goods> typeslist = goodsService.listTypes();
		if (typeslist != null) {
			types = CollectionUtils.copyTo(typeslist, GoodsVO.class);
		}
		return Result.ok(types);
	}
	
	
	@RequestMapping(value = "/goodsByWhere", method = RequestMethod.POST)
	public Result queryGoodsByWhere() throws Exception {
		List<GoodsVO> goods = new ArrayList<GoodsVO>();
		Map<String,Object> map = new HashMap<String,Object>();
			//	品牌数组
			int brandArr []={5};;
			//类型数组
			int typeArr [] = {2};

			map.put("brandArr", "");
			map.put("typeArr", typeArr);
			map.put("beginprice", 50);
			map.put("endprice", 300);
			map.put("order", 1);
			/*在结果中搜索时代result 属性有值,不在结果中搜索时为空--待定*/
		List<Goods> goodsByWhere = goodsService.listGoodsByWhere(map);
		if (goodsByWhere != null) {
			goods = CollectionUtils.copyTo(goodsByWhere, GoodsVO.class);
		}
		for(GoodsVO v:goods){
			System.out.println(v.getGoodname() +"销量:"+v.getVolume()+"新品:"+v.getGoodaddtime()+"价格:"+v.getPrice());
		}
		return Result.ok(goods);
	}
	/**
	 * 首页热门推荐产品功能  待确定业务修改
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listHotGoods", method = RequestMethod.POST)
	public Result listHotGoods() throws Exception {
		List<HotGoodsVO> rltlist = new ArrayList<HotGoodsVO>();
		try {
			
			List<Goods> goodslist = goodsService.listHotGoods();
			List<Goods> tmplist = new ArrayList<Goods>();
			if (goodslist != null && goodslist.size() >0) {//将业务对象转换为页面VO对象
				for(int i = 0;i<goodslist.size();i++){
					tmplist.add(goodslist.get(i));
					if(i!=(goodslist.size()-1) && !goodslist.get(i).getMerchant_id().equals(goodslist.get(i+1).getMerchant_id())){
						HotGoodsVO hotGoodsVO = new HotGoodsVO();
						hotGoodsVO.init(goodslist);
						rltlist.add(hotGoodsVO);
						tmplist = new ArrayList<Goods>();
					}else{
						HotGoodsVO hotGoodsVO = new HotGoodsVO();
						hotGoodsVO.init(goodslist);
						rltlist.add(hotGoodsVO);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Result.ok(rltlist);
	}
}
