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
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Page;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.goods.bo.GoodsShow;
import com.jingyunbank.etrade.api.goods.bo.HoneyGoods;
import com.jingyunbank.etrade.api.goods.bo.Hot24Goods;
import com.jingyunbank.etrade.api.goods.bo.HotGoods;
import com.jingyunbank.etrade.api.goods.bo.SalesRecord;
import com.jingyunbank.etrade.api.goods.bo.ShowGoods;
import com.jingyunbank.etrade.api.goods.service.IGoodsService;
import com.jingyunbank.etrade.goods.bean.CommonGoodsVO;
import com.jingyunbank.etrade.goods.bean.GoodsBrandVO;
import com.jingyunbank.etrade.goods.bean.GoodsMerchantVO;
import com.jingyunbank.etrade.goods.bean.GoodsShowVO;
import com.jingyunbank.etrade.goods.bean.GoodsTypesVO;
import com.jingyunbank.etrade.goods.bean.GoodsVO;
import com.jingyunbank.etrade.goods.bean.HoneyGoodsVO;
import com.jingyunbank.etrade.goods.bean.Hot24GoodsVO;
import com.jingyunbank.etrade.goods.bean.HotGoodsVO;
import com.jingyunbank.etrade.goods.bean.RecommendGoods;
import com.jingyunbank.etrade.goods.bean.SaleRecordVO;

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

	
	/*
	@RequestMapping(value = "/{goodsname}", method = RequestMethod.GET)
	public Result<List<CommonGoodsVO>> queryGoodsByName(HttpServletRequest request, @PathVariable String goodsname,
			Page page) throws Exception {
		Range range = new Range();
		range.setFrom(page.getOffset());
		range.setTo(page.getSize());
		List<CommonGoodsVO> list = goodsService.listGoodsByLikeName(goodsname, range).stream().map(bo -> {
			CommonGoodsVO vo = new CommonGoodsVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}*/

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
	public Result<List<GoodsBrandVO>> queryBrandsThree(HttpServletRequest request,
			@PathVariable int from,
			@PathVariable int size
			) throws Exception {
		List<GoodsBrandVO> list = goodsService.listBrandsThree(new Range(from, size)).stream().map(bo -> {
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
	 * 赋值价格区间对应的benginPrice, endPrice
	 * 
	 * @param goodshowvo
	 * @param goodshowBO
	 * @return
	 */
	public GoodsShow setBenginEndPrice(GoodsShowVO goodshowvo, GoodsShow goodshowBO) {
		if (goodshowvo.getPriceFlag() == 1) {
			goodshowBO.setBeginPrice(new BigDecimal(0));
			goodshowBO.setEndPrice(new BigDecimal(69));
		} else if (goodshowvo.getPriceFlag() == 2) {
			goodshowBO.setBeginPrice(new BigDecimal(70));
			goodshowBO.setEndPrice(new BigDecimal(199));
		} else if (goodshowvo.getPriceFlag() == 3) {
			goodshowBO.setBeginPrice(new BigDecimal(200));
			goodshowBO.setEndPrice(new BigDecimal(399));
		} else if (goodshowvo.getPriceFlag() == 4) {
			goodshowBO.setBeginPrice(new BigDecimal(400));
			goodshowBO.setEndPrice(new BigDecimal(799));
		} else if (goodshowvo.getPriceFlag() == 5) {
			goodshowBO.setBeginPrice(new BigDecimal(800));
			goodshowBO.setEndPrice(new BigDecimal(1199));
		} else if (goodshowvo.getPriceFlag() == 6) {
			goodshowBO.setBeginPrice(new BigDecimal(1200));
			goodshowBO.setEndPrice(new BigDecimal(999999));
		} else if (goodshowvo.getPriceFlag() == 0) {
			goodshowBO.setBeginPrice(null);
			goodshowBO.setEndPrice(null);
		}
		return goodshowBO;
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
		GoodsShow goodshowBO = getVo2Bo(goodshowvo);
		// 接收价格区间
		goodshowBO = setBenginEndPrice(goodshowvo, goodshowBO);
		Range range = new Range();
		range.setFrom(page.getOffset());
		range.setTo(page.getSize());

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
	// @RequestMapping(value = "/goodsMerchantByWhere/list", method =
	// RequestMethod.GET)
	@RequestMapping(value = "/merchant/list", method = RequestMethod.GET)
	public Result<List<GoodsMerchantVO>> queryMerchantByWhere(HttpServletRequest request, GoodsShowVO goodshowvo,
			Page page) throws Exception {
		GoodsShow goodshowBO = getVo2Bo(goodshowvo);
		// 接收价格区间
		goodshowBO = setBenginEndPrice(goodshowvo, goodshowBO);
		Range range = new Range();
		range.setFrom(page.getOffset());
		range.setTo(page.getSize());
		List<GoodsMerchantVO> list = goodsService.listMerchantByWhere(goodshowBO, range).stream().map(bo -> {
			GoodsMerchantVO vo = new GoodsMerchantVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());

		return Result.ok(list);
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
			@PathVariable String mid,
			GoodsShowVO goodshowvo, Page page) throws Exception {
		GoodsShow goodshowBO = getVo2Bo(goodshowvo);
		// 接收价格区间
		goodshowBO = setBenginEndPrice(goodshowvo, goodshowBO);
		Range range = new Range();
		range.setFrom(page.getOffset());
		range.setTo(page.getSize());
		List<CommonGoodsVO> list = goodsService.listMerchantByWhereGoodsMax(goodshowBO, range).stream().map(bo -> {
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
	@RequestMapping(value = "/hotgoods/list", method = RequestMethod.GET)
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
	@RequestMapping(value = "/hot24goods/list", method = RequestMethod.GET)
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
		GoodsShow goodshowBO = getVo2Bo(vo);
		// 接收价格区间
		goodshowBO = setBenginEndPrice(vo, goodshowBO);

		List<CommonGoodsVO> list = goodsService.listGoodsByGoodsResult(goodshowBO, range).stream().map(bo -> {
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
	@RequestMapping(value = "/honeygoods/list", method = RequestMethod.GET)
	public Result<List<HoneyGoodsVO>> listHoneyGoods() throws Exception {
		List<HoneyGoods> goodslist = goodsService.listHoneyGoods();
		List<HoneyGoodsVO> list = goodslist.stream().map(bo -> {
			HoneyGoodsVO vo = new HoneyGoodsVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}

	/**
	 * 获取商品的购买记录
	 * 
	 * @param gid
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/salesrecords/list/{gid}", method = RequestMethod.GET)
	public Result<List<SaleRecordVO>> querySalesRecords(@PathVariable String gid, Page page) throws Exception {
		Range range = new Range();
		range.setFrom(page.getOffset());
		range.setTo(page.getSize());
		List<SalesRecord> salelist = goodsService.listSalesRecords(gid, range);
		List<SaleRecordVO> list = salelist.stream().map(bo -> {
			SaleRecordVO vo = new SaleRecordVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}

}
