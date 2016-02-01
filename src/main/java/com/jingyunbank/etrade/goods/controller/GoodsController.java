package com.jingyunbank.etrade.goods.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Page;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.goods.bo.GoodsMerchant;
import com.jingyunbank.etrade.api.goods.bo.HoneyGoods;
import com.jingyunbank.etrade.api.goods.bo.Hot24Goods;
import com.jingyunbank.etrade.api.goods.bo.HotGoods;
import com.jingyunbank.etrade.api.goods.bo.ShowGoods;
import com.jingyunbank.etrade.api.goods.service.IBrandService;
import com.jingyunbank.etrade.api.goods.service.IGoodsService;
import com.jingyunbank.etrade.goods.bean.CommonGoodsVO;
import com.jingyunbank.etrade.goods.bean.GoodsBrandVO;
import com.jingyunbank.etrade.goods.bean.GoodsMerchantVO;
import com.jingyunbank.etrade.goods.bean.GoodsPriceConvert;
import com.jingyunbank.etrade.goods.bean.GoodsShowVO;
import com.jingyunbank.etrade.goods.bean.GoodsStockShowVO;
import com.jingyunbank.etrade.goods.bean.GoodsTypesVO;
import com.jingyunbank.etrade.goods.bean.GoodsVO;
import com.jingyunbank.etrade.goods.bean.HoneyGoodsVO;
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

	@Resource
	protected IBrandService brandService;
	

	/**
	 * 查询品牌列表
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/brand/list", method = RequestMethod.GET)
	public Result<List<GoodsBrandVO>> queryBrands(HttpServletRequest request) throws Exception {
		List<GoodsBrandVO> list = goodsService.listBrands().stream().map(bo -> {
			GoodsBrandVO vo = new GoodsBrandVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}

	/**
	 * 首页商品分类 ----显示3条品牌
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/brand/{from}/{size}/list", method = RequestMethod.GET)
	public Result<List<GoodsBrandVO>> queryBrandsThree(HttpServletRequest request, @PathVariable String from,
			@PathVariable String size) throws Exception {
		List<GoodsBrandVO> list = goodsService
				.listBrandsThree(new Range(Integer.parseInt(from), Integer.parseInt(size))).stream().map(bo -> {
					GoodsBrandVO vo = new GoodsBrandVO();
					BeanUtils.copyProperties(bo, vo);
					return vo;
				}).collect(Collectors.toList());
		return Result.ok(list);
	}

	/**
	 * 首页商品分类 ----显示3条类别
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/type/{from}/{size}/list", method = RequestMethod.GET)
	public Result<List<GoodsTypesVO>> queryTypesThree(HttpServletRequest request, @PathVariable String from,
			@PathVariable String size) throws Exception {
		List<GoodsTypesVO> list = goodsService.listTypesThree(new Range(Integer.parseInt(from), Integer.parseInt(size)))
				.stream().map(bo -> {
					GoodsTypesVO vo = new GoodsTypesVO();
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
	@RequestMapping(value = "/type/list", method = RequestMethod.GET)
	public Result<List<GoodsTypesVO>> queryTypes(HttpServletRequest request) throws Exception {
		List<GoodsTypesVO> list = goodsService.listTypes().stream().map(bo -> {
			GoodsTypesVO vo = new GoodsTypesVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}

	/**
	 * 查询所有商品
	 * 
	 * @param request
	 * @param range
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/all/list", method = RequestMethod.GET)
	public Result<List<CommonGoodsVO>> queryAllgoods(HttpServletRequest request, Page page) throws Exception {
		Range range = new Range();
		range.setFrom(page.getOffset());
		range.setTo(page.getSize());
		List<CommonGoodsVO> goodslist = goodsService.listAll(range).stream().map(bo -> {
			CommonGoodsVO vo = new CommonGoodsVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(goodslist);
	}

	// ---------------------
	/**
	 * 赋值价格区间对应的benginPrice, endPrice
	 * 
	 * @param goodshowvo
	 * @return
	 */
	public GoodsPriceConvert setPrice(GoodsShowVO goodshowvo) {
		GoodsPriceConvert price = new GoodsPriceConvert();
		if (goodshowvo.getPriceFlag() == 1) {
			price.setBeginPrice(new BigDecimal(0));
			price.setEndPrice(new BigDecimal(69));
		} else if (goodshowvo.getPriceFlag() == 2) {
			price.setBeginPrice(new BigDecimal(70));
			price.setEndPrice(new BigDecimal(199));
		} else if (goodshowvo.getPriceFlag() == 3) {
			price.setBeginPrice(new BigDecimal(200));
			price.setEndPrice(new BigDecimal(399));
		} else if (goodshowvo.getPriceFlag() == 4) {
			price.setBeginPrice(new BigDecimal(400));
			price.setEndPrice(new BigDecimal(799));
		} else if (goodshowvo.getPriceFlag() == 5) {
			price.setBeginPrice(new BigDecimal(800));
			price.setEndPrice(new BigDecimal(1199));
		} else if (goodshowvo.getPriceFlag() == 6) {
			price.setBeginPrice(new BigDecimal(1200));
			price.setEndPrice(new BigDecimal(999999));
		} else if (goodshowvo.getPriceFlag() == 0) {
			price.setBeginPrice(null);
			price.setEndPrice(null);
		}
		return price;

	}
	// --------------------

	/**
	 * 根据条件查询商品信息 (商品查询)
	 * 
	 * @param request
	 * @param goodvo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<List<CommonGoodsVO>> queryGoodsByWhere(HttpServletRequest request, GoodsShowVO goodshowvo, Page page)
			throws Exception {

		Range range = new Range();
		range.setFrom(page.getOffset());
		range.setTo(page.getSize());

		// 接收价格区间
		GoodsPriceConvert price = setPrice(goodshowvo);

		String[] brands = goodshowvo.getBrands();
		String[] types = goodshowvo.getTypes();
		int order = goodshowvo.getOrder();
		BigDecimal beginPrice = price.getBeginPrice();
		BigDecimal endPrice = price.getEndPrice();

		List<CommonGoodsVO> goodslist = goodsService.listGoodsByWhere(brands, types, beginPrice, endPrice, order, range)
				.stream().map(bo -> {
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
	@RequestMapping(value = "/recommend/list/{from}/{size}", method = RequestMethod.GET)
	public Result<List<RecommendGoods>> queryGoodsByWhere(HttpServletRequest request, @PathVariable String from,
			@PathVariable String size) throws Exception {
		List<RecommendGoods> list = goodsService.listRecommend(from, size).stream().map(bo -> {
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
	 * @param goodshowvo
	 * @param page
	 * @return @throws Exception @throws
	 */

	@RequestMapping(value = "/merchant/list", method = RequestMethod.GET)
	public Result<List<GoodsMerchantVO>> queryMerchantByWhere(HttpServletRequest request, GoodsShowVO goodshowvo,
			Page page) throws Exception {

		Range range = new Range();
		range.setFrom(page.getOffset());
		range.setTo(page.getSize());

		// 接收价格区间
		GoodsPriceConvert price = setPrice(goodshowvo);

		String[] brands = goodshowvo.getBrands();
		String[] types = goodshowvo.getTypes();
		BigDecimal beginPrice = price.getBeginPrice();
		BigDecimal endPrice = price.getEndPrice();

		List<GoodsMerchant> list = goodsService.listMerchantByWhere(brands, types, beginPrice, endPrice, range);
		List<GoodsMerchantVO> newlist = new ArrayList<GoodsMerchantVO>();
		for (GoodsMerchant m : list) {
			GoodsMerchantVO vo = new GoodsMerchantVO();
			BeanUtils.copyProperties(m, vo);
			vo.setBrands(brandService.listBrandsByMid(m.getMID()));
			newlist.add(vo);
		}

		/*
		 * List<GoodsMerchantVO> list =
		 * goodsService.listMerchantByWhere(goodshowBO, range).stream().map(bo
		 * -> { GoodsMerchantVO vo = new GoodsMerchantVO();
		 * BeanUtils.copyProperties(bo, vo); return vo;
		 * }).collect(Collectors.toList());
		 */

		return Result.ok(newlist);
	}

	/**
	 * 店铺相关产品(分页)
	 * 
	 * @param request
	 * @param goodshowvo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{mid}/list", method = RequestMethod.GET)
	public Result<List<CommonGoodsVO>> queryGoodsMerchantByWhereGoodsMax(HttpServletRequest request,
			@PathVariable String mid, GoodsShowVO goodshowvo, Page page) throws Exception {

		Range range = new Range();
		range.setFrom(page.getOffset());
		range.setTo(page.getSize());

		// 接收价格区间
		GoodsPriceConvert price = setPrice(goodshowvo);

		String[] brands = goodshowvo.getBrands();
		String[] types = goodshowvo.getTypes();
		int order = goodshowvo.getOrder();
		BigDecimal beginPrice = price.getBeginPrice();
		BigDecimal endPrice = price.getEndPrice();

		List<CommonGoodsVO> list = goodsService
				.listMerchantByWhereGoodsMax(brands, types, beginPrice, endPrice, mid, order, range).stream()
				.map(bo -> {
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
	@RequestMapping(value = "/hot/list", method = RequestMethod.GET)
	public Result<List<HotGoodsVO>> listHotGoods() throws Exception {
		List<HotGoodsVO> rltlist = new ArrayList<HotGoodsVO>();
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
		return Result.ok(rltlist);
	}

	/**
	 * 阿胶后台24小时热卖 待确定业务修改
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/hot24/list", method = RequestMethod.GET)
	public Result<Hot24GoodsVO> listHot24Goods() throws Exception {
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
	@RequestMapping(value = "/expand/list", method = RequestMethod.GET)
	public Result<List<CommonGoodsVO>> listGoodsExpand() throws Exception {
		List<CommonGoodsVO> list = goodsService.listGoodsExpand().stream().map(bo -> {
			CommonGoodsVO vo = new CommonGoodsVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}

	/**
	 * 多条件查询-->在结果中搜索
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/result/list", method = RequestMethod.GET)
	public Result<List<CommonGoodsVO>> listGoodsByGoodsResult(GoodsShowVO vo, Page page) throws Exception {
		// ----分页条件 [待修改]
		Range range = new Range();
		range.setFrom(page.getOffset());
		range.setTo(page.getSize());

		// 接收价格区间
		GoodsPriceConvert price = setPrice(vo);

		String[] brands = vo.getBrands();
		String[] types = vo.getTypes();
		int order = vo.getOrder();
		BigDecimal beginPrice = price.getBeginPrice();
		BigDecimal endPrice = price.getEndPrice();
		String goodsname = vo.getGoodsName();
		
		List<CommonGoodsVO> list = goodsService
				.listGoodsByGoodsResult(brands, types, beginPrice, endPrice, goodsname, order, range).stream()
				.map(bo -> {
					CommonGoodsVO vos = new CommonGoodsVO();
					BeanUtils.copyProperties(bo, vos);
					return vos;
				}).collect(Collectors.toList());
		return Result.ok(list);
	}

	/**
	 * 根据ID 查询商品属性
	 * 
	 * @param gid
	 * @return
	 */
	@RequestMapping(value = "/{gid}", method = RequestMethod.GET)
	public Result<GoodsVO> queryGoodsById(@PathVariable String gid) throws Exception {
		GoodsVO vo = null;
		Optional<ShowGoods> showbo = goodsService.singleById(gid);
		if (Objects.nonNull(showbo)) {
			vo = new GoodsVO();
			BeanUtils.copyProperties(showbo.get(), vo);
		}
		System.out.println(vo);
		return Result.ok(vo);
	}

	/**
	 * 阿胶详情页 宝贝推荐排行
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/honey/list/{gid}", method = RequestMethod.GET)
	public Result<List<HoneyGoodsVO>> listHoneyGoods(@PathVariable String gid) throws Exception {
		List<HoneyGoods> goodslist = goodsService.listHoneyGoods(gid);
		List<HoneyGoodsVO> list = goodslist.stream().map(bo -> {
			HoneyGoodsVO vo = new HoneyGoodsVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}

	/**
	 * http://localhost:8080/api/goods/stock/list?gids=1&gids=2 根据GID 获取商品的库存
	 * 
	 * @param request
	 * @param gids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stock/list", method = RequestMethod.GET)
	public Result<List<GoodsStockShowVO>> queryStockByGids(HttpServletRequest request, @RequestParam List<String> gids)
			throws Exception {
		List<GoodsStockShowVO> list = goodsService.listGoodsStcok(gids).stream().map(bo -> {
			GoodsStockShowVO vo = new GoodsStockShowVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}

}
