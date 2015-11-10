package com.jingyunbank.etrade.goods.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Page;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.goods.bo.GoodsShow;
import com.jingyunbank.etrade.api.goods.bo.HotGoods;
import com.jingyunbank.etrade.api.goods.bo.ShowGoods;
import com.jingyunbank.etrade.api.goods.service.IGoodsService;
import com.jingyunbank.etrade.goods.bean.CommonGoodsVO;
import com.jingyunbank.etrade.goods.bean.GoodsBrandVO;
import com.jingyunbank.etrade.goods.bean.GoodsShowVO;
import com.jingyunbank.etrade.goods.bean.GoodsTypesVO;
import com.jingyunbank.etrade.goods.bean.HotGoodsVO;
import com.jingyunbank.etrade.goods.bean.RecommendGoods;
import com.jingyunbank.etrade.order.bean.OrderVO;

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
		List<ShowGoods> bolist = goodsService.listGoodsByLikeName(goodsname, range);
		List<CommonGoodsVO> list = goodsService.listGoodsByLikeName(goodsname, range).stream().map(bo -> {
			CommonGoodsVO vo = new CommonGoodsVO();
			try {
				BeanUtils.copyProperties(bo, vo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}

	@RequestMapping(value = "/listBrands", method = RequestMethod.POST)
	public Result queryBrands(HttpServletRequest request) throws Exception {
		List<GoodsBrandVO> list = goodsService.listBrands().stream().map(bo -> {
			GoodsBrandVO vo = new GoodsBrandVO();
			try {
				BeanUtils.copyProperties(bo, vo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}

	@RequestMapping(value = "/listTypes", method = RequestMethod.POST)
	public Result queryTypes(HttpServletRequest request) throws Exception {
		List<GoodsTypesVO> list = goodsService.listTypes().stream().map(bo -> {
			GoodsTypesVO vo = new GoodsTypesVO();
			try {
				BeanUtils.copyProperties(bo, vo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
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
		// GoodsShow goodshowBO = getVo2Bo(goodshowvo);
		Range range = new Range();
		range.setFrom(0);
		range.setTo(20);

		GoodsShow goodshowBO = new GoodsShow();
		String brands[] = { "1" };
		String types[] = { "1" };
		goodshowBO.setBrands(null);
		goodshowBO.setTypes(null);
		goodshowBO.setBeginPrice(new BigDecimal(300));
		goodshowBO.setEndPrice(new BigDecimal(300));
		goodshowBO.setOrder(2);
		List<CommonGoodsVO> goodslist = goodsService.listGoodsByWhere(goodshowBO, range).stream().map(bo -> {
			CommonGoodsVO vo = new CommonGoodsVO();
			try {
				BeanUtils.copyProperties(bo, vo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(goodslist);
	}
		/**
		 * 宝贝推荐
		 * @param request
		 * @return
		 * @throws Exception 
		 */
		@RequestMapping(value = "/listRecommendGoods", method = RequestMethod.GET)
		public Result queryGoodsByWhere(HttpServletRequest request) throws Exception{
			List<RecommendGoods> list = goodsService.listRecommend().stream().map(bo -> {
				RecommendGoods vo = new RecommendGoods();
				try {
					BeanUtils.copyProperties(bo, vo);
				} catch (Exception e) {
					e.printStackTrace();
				}
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
}
