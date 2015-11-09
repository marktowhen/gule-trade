package com.jingyunbank.etrade.goods.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.util.CollectionUtils;
import com.jingyunbank.etrade.api.goods.bo.Goods;
import com.jingyunbank.etrade.api.goods.bo.GoodsShow;
import com.jingyunbank.etrade.api.goods.service.IGoodsService;
import com.jingyunbank.etrade.base.Page;
import com.jingyunbank.etrade.goods.bean.CommonGoodsVO;
import com.jingyunbank.etrade.goods.bean.GoodsBrandVO;
import com.jingyunbank.etrade.goods.bean.GoodsShowVO;
import com.jingyunbank.etrade.goods.bean.GoodsTypesVO;
import com.jingyunbank.etrade.goods.bean.HotGoodsVO;

/**
 * Title: 商品controller
 * 
 * @author duanxf
 * @date 2015年11月4日
 */
@RestController
@RequestMapping("/api/goods")
public class GoodsController {

	@Resource
	private IGoodsService goodsService;

	@RequestMapping(value = "/{goodsname}", method = RequestMethod.GET)
	public Result queryGoodsByName(HttpServletRequest request, @PathVariable String goodsname, Page page)
			throws Exception {
		Range range = new Range();
		range.setFrom(0);
		range.setTo(20);
		List<Goods> bolist = goodsService.listGoodsByLikeName(goodsname, range);
		List<CommonGoodsVO> goods = new ArrayList<CommonGoodsVO>();
		if (bolist != null) {
			goods = CollectionUtils.copyTo(bolist, CommonGoodsVO.class);
		}
		return Result.ok(goods);
	}

	@RequestMapping(value = "/listBrands", method = RequestMethod.POST)
	public Result queryBrands() throws Exception {
		List<GoodsBrandVO> brands = new ArrayList<GoodsBrandVO>();
		List<Goods> brandslist = goodsService.listBrands();
		if (brandslist != null) {
			brands = CollectionUtils.copyTo(brandslist, GoodsBrandVO.class);
		}
		return Result.ok(brands);
	}

	@RequestMapping(value = "/listTypes", method = RequestMethod.POST)
	public Result queryTypes() throws Exception {
		List<GoodsTypesVO> types = new ArrayList<GoodsTypesVO>();
		List<Goods> typeslist = goodsService.listTypes();
		if (typeslist != null) {
			types = CollectionUtils.copyTo(typeslist, GoodsTypesVO.class);
		}

		return Result.ok(types);
	}

	/**
	 * 根据条件查询商品信息
	 * 
	 * @param request
	 * @param goodvo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listByWhere", method = RequestMethod.GET)
	public Result queryGoodsByWhere(HttpServletRequest request, GoodsShowVO goodshowvo, Page page) throws Exception {
		//GoodsShow goodshowBO = getVo2Bo(goodshowvo);
		Range range = new Range();
		range.setFrom(0);
		range.setTo(20);
		
		GoodsShow goodshowBO = new GoodsShow();
		String brands[] ={"1"};
		String types[] ={"1"};
		goodshowBO.setBrands(null);
		goodshowBO.setTypes(null);
		goodshowBO.setBeginPrice(new BigDecimal(300));
		goodshowBO.setEndPrice(new BigDecimal(300));
		goodshowBO.setOrder(2);
		
		List<Goods> list = goodsService.listGoodsByWhere(goodshowBO, range);
		List<CommonGoodsVO> goodslist = new ArrayList<CommonGoodsVO>();
		if (list != null) {
			goodslist = CollectionUtils.copyTo(list, CommonGoodsVO.class);
		}
		return Result.ok(goodslist);
	}

	/**
	 * 首页热门推荐产品功能  待确定业务修改
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/hotgoods/list", method = RequestMethod.POST)
	public Result listHotGoods() throws Exception {
		List<HotGoodsVO> rltlist = new ArrayList<HotGoodsVO>();
		try {
			List<Goods> goodslist = goodsService.listHotGoods();
			List<Goods> tmplist = new ArrayList<Goods>();//存放商家分组LIST
			if (goodslist != null && goodslist.size() >0) {//将业务对象转换为页面VO对象
				HotGoodsVO hotGoodsVO = new HotGoodsVO();
				for(int i = 0;i<goodslist.size();i++){
					 //第一条处理
					if(i == 0){
						tmplist.add(goodslist.get(i));
					}else if(!goodslist.get(i-1).getMID().equals(goodslist.get(i).getMID())){//与上一家不是一家
						System.out.println("不是一个商家****************");
						hotGoodsVO.init(tmplist);
						rltlist.add(hotGoodsVO);
						tmplist = new ArrayList<Goods>();
						tmplist.add(goodslist.get(i));
						if(i == (goodslist.size()-1)){//最后一条
							hotGoodsVO = new HotGoodsVO();
							hotGoodsVO.init(tmplist);
							rltlist.add(hotGoodsVO);
						}else{
							hotGoodsVO = new HotGoodsVO();
						}
					}else if(goodslist.get(i-1).getMID().equals(goodslist.get(i).getMID())){//与上一家是一家
						System.out.println("是一个商家");
						tmplist.add(goodslist.get(i));
						if(i == (goodslist.size()-1)){//最后一条
							hotGoodsVO = new HotGoodsVO();
							hotGoodsVO.init(tmplist);
							rltlist.add(hotGoodsVO);
						}
					}
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Result.ok(rltlist);
	}

	/**
	 * 商品VO对象转成BO对象
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	private GoodsShow getVo2Bo(GoodsShowVO vo) throws Exception {
		GoodsShow bo = null;
		if (vo != null) {
			bo = new GoodsShow();
			BeanUtils.copyProperties(vo, bo);
		}
		return bo;
	}
}
