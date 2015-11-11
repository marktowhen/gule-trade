package com.jingyunbank.etrade.goods.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Page;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.goods.bo.FootprintGoods;
import com.jingyunbank.etrade.api.goods.bo.GoodsShow;
import com.jingyunbank.etrade.api.goods.bo.Hot24Goods;
import com.jingyunbank.etrade.api.goods.bo.HotGoods;
import com.jingyunbank.etrade.api.goods.service.IGoodsService;
import com.jingyunbank.etrade.goods.bean.CommonGoodsVO;
import com.jingyunbank.etrade.goods.bean.FootprintGoodsVO;
import com.jingyunbank.etrade.goods.bean.GoodsBrandVO;
import com.jingyunbank.etrade.goods.bean.GoodsMerchantVO;
import com.jingyunbank.etrade.goods.bean.GoodsShowVO;
import com.jingyunbank.etrade.goods.bean.GoodsTypesVO;
import com.jingyunbank.etrade.goods.bean.Hot24GoodsVO;
import com.jingyunbank.etrade.goods.bean.HotGoodsVO;
import com.jingyunbank.etrade.goods.bean.RecommendGoods;

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
	protected IGoodsService goodsService;

	/**
	 * 根据名称模糊查询商品
	 * 
	 * @param request
	 * @param goodsname
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{goodsname}", method = RequestMethod.GET)
	public Result queryGoodsByName(HttpServletRequest request, @PathVariable String goodsname, Page page)
			throws Exception {
		Range range = new Range();
		range.setFrom(0);
		range.setTo(20);
		List<CommonGoodsVO> list = goodsService.listGoodsByLikeName(goodsname, range).stream().map(bo -> {
			CommonGoodsVO vo = new CommonGoodsVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}

	/**
	 * 查询品牌列表
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listBrands", method = RequestMethod.POST)
	public Result queryBrands(HttpServletRequest request) throws Exception {
		List<GoodsBrandVO> list = goodsService.listBrands().stream().map(bo -> {
			GoodsBrandVO vo = new GoodsBrandVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}

	/**
	 * 查询类型类别
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listTypes", method = RequestMethod.POST)
	public Result queryTypes(HttpServletRequest request) throws Exception {
		List<GoodsTypesVO> list = goodsService.listTypes().stream().map(bo -> {
			GoodsTypesVO vo = new GoodsTypesVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}

	/**
	 * 根据条件查询商品信息 (商品查询)
	 * 
	 * @param request
	 * @param goodvo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listByWhere", method = RequestMethod.GET)
	public Result queryGoodsByWhere(HttpServletRequest request, GoodsShowVO goodshowvo, Page page) throws Exception {
		// GoodsShow goodshowBO = getVo2Bo(goodshowvo);
		Range range = new Range();
		range.setFrom(0);
		range.setTo(20);

		GoodsShow goodshowBO = new GoodsShow();
		String brands[] = { "1" };
		String types[] = { "1" };
		goodshowBO.setBrands(null);
		goodshowBO.setTypes(null);
		goodshowBO.setBeginPrice(new BigDecimal(100));
		goodshowBO.setEndPrice(new BigDecimal(300));
		goodshowBO.setOrder(2);
		List<CommonGoodsVO> goodslist = goodsService.listGoodsByWhere(goodshowBO, range).stream().map(bo -> {
			CommonGoodsVO vo = new CommonGoodsVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(goodslist);
	}

	/**
	 * 宝贝推荐
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listRecommendGoods", method = RequestMethod.GET)
	public Result queryGoodsByWhere(HttpServletRequest request) throws Exception {
		List<RecommendGoods> list = goodsService.listRecommend().stream().map(bo -> {
			RecommendGoods vo = new RecommendGoods();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}

	/**
	 * 根据搜索条件查询店铺 (店铺查询)
	 * 
	 * @param request
	 * 			@param goodshowvo @param page @return @throws
	 *            Exception @throws
	 */
	@RequestMapping(value = "/listGoodsMerchantByWhere", method = RequestMethod.GET)
	public Result queryMerchantByWhere(HttpServletRequest request, GoodsShowVO goodshowvo, Page page) throws Exception {
		// GoodsShow goodshowBO = getVo2Bo(goodshowvo);

		Range range = new Range();
		range.setFrom(0);
		range.setTo(20);
		GoodsShow goodshowBO = new GoodsShow();
		String brands[] = { "1", "2" };
		String types[] = { "4" };
		goodshowBO.setBrands(brands);
		goodshowBO.setTypes(types);
		goodshowBO.setBeginPrice(new BigDecimal(100));
		goodshowBO.setEndPrice(new BigDecimal(350));

		List<GoodsMerchantVO> list = goodsService.listMerchantByWhere(goodshowBO, range).stream().map(bo -> {
			GoodsMerchantVO vo = new GoodsMerchantVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());

		return Result.ok(list);
	}

	/**
	 * 店铺相关商品 (点击X件相关产品 MID )
	 * 
	 * @param request
	 * @param goodshowvo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listMerchantByWhereGoods", method = RequestMethod.GET)
	public Result queryGoodsMerchantByWhereGoods(HttpServletRequest request, GoodsShowVO goodshowvo) throws Exception {
		// GoodsShow goodshowBO = getVo2Bo(goodshowvo);
		GoodsShow goodshowBO = new GoodsShow();
		String brands[] = { "1", "2" };
		String types[] = { "4" };
		goodshowBO.setBrands(brands);
		goodshowBO.setTypes(types);
		goodshowBO.setBeginPrice(new BigDecimal(100));
		goodshowBO.setEndPrice(new BigDecimal(350));
		goodshowBO.setMID("4");
		List<CommonGoodsVO> list = goodsService.listMerchantByWhereGoods(goodshowBO).stream().map(bo -> {
			CommonGoodsVO vo = new CommonGoodsVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());

		return Result.ok(list);
	}

	/**
	 * 首页热门推荐产品功能 待确定业务修改
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/hotgoods/list", method = RequestMethod.POST)
	public Result listHotGoods() throws Exception {
		List<HotGoodsVO> rltlist = new ArrayList<HotGoodsVO>();
		try {
			List<HotGoods> goodslist = goodsService.listHotGoods();
			List<HotGoods> tmplist = new ArrayList<HotGoods>();// 存放商家分组LIST
			if (goodslist != null && goodslist.size() > 0) {// 将业务对象转换为页面VO对象
				HotGoodsVO hotGoodsVO = new HotGoodsVO();
				for (int i = 0; i < goodslist.size(); i++) {
					// 第一条处理
					if (i == 0) {
						tmplist.add(goodslist.get(i));
					} else if (!goodslist.get(i - 1).getMID().equals(goodslist.get(i).getMID())) {// 与上一家不是一家
						hotGoodsVO.init(tmplist);
						rltlist.add(hotGoodsVO);
						tmplist = new ArrayList<HotGoods>();
						tmplist.add(goodslist.get(i));
						if (i == (goodslist.size() - 1)) {// 最后一条
							hotGoodsVO = new HotGoodsVO();
							hotGoodsVO.init(tmplist);
							rltlist.add(hotGoodsVO);
						} else {
							hotGoodsVO = new HotGoodsVO();
						}
					} else if (goodslist.get(i - 1).getMID().equals(goodslist.get(i).getMID())) {// 与上一家是一家
						tmplist.add(goodslist.get(i));
						if (i == (goodslist.size() - 1)) {// 最后一条
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

	/**
	 * 阿胶后台24小时热卖 待确定业务修改
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/hot24goods/list", method = RequestMethod.POST)
	public Result listHot24Goods() throws Exception {
		List<Hot24Goods> goodslist = goodsService.listHot24Goods();
		Hot24GoodsVO hot24GoodsVO = new Hot24GoodsVO();
		hot24GoodsVO.init(goodslist);
		return Result.ok(hot24GoodsVO);
	}

	/**
	 * 推广商品
	 * 
	 * @return
	 */
	@RequestMapping(value = "/GoodsExpand/list", method = RequestMethod.GET)
	public Result listGoodsExpand() throws Exception {
		List<CommonGoodsVO> list = goodsService.listGoodsExpand().stream().map(bo -> {
			CommonGoodsVO vo = new CommonGoodsVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}
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
