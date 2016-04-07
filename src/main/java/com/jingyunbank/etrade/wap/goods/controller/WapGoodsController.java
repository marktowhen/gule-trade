package com.jingyunbank.etrade.wap.goods.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsDeatil;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsSku;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsSkuCondition;
import com.jingyunbank.etrade.api.wap.goods.service.IWapGoodsService;
import com.jingyunbank.etrade.wap.goods.bean.GoodsDeatilVO;
import com.jingyunbank.etrade.wap.goods.bean.GoodsInfoVO;
import com.jingyunbank.etrade.wap.goods.bean.GoodsShowVO;
import com.jingyunbank.etrade.wap.goods.bean.GoodsSkuConditionVO;
import com.jingyunbank.etrade.wap.goods.bean.GoodsSkuVO;

@RestController
@RequestMapping("/api/wap/goods/")
public class WapGoodsController {

	@Autowired
	private IWapGoodsService wapGoodsService;

	/**
	 * 展示商品的信息
	 * 
	 * @param mid
	 *            商家ID
	 * @param tid
	 *            类型ID
	 * @param order
	 *            排序字段 0 销量 1 时间 2 评论
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<List<GoodsShowVO>> getGoodsList(
			@RequestParam(value = "mid", required = false, defaultValue = "") String mid,
			@RequestParam(value = "tid", required = false, defaultValue = "") String tid,
			@RequestParam(value = "order", required = false, defaultValue = "") String order,
			@RequestParam(value = "name", required = false, defaultValue = "") String name) throws Exception {
		List<GoodsShowVO> list = wapGoodsService.listGoods(mid, tid, order,name).stream().map(bo -> {
			GoodsShowVO vo = new GoodsShowVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());

		return Result.ok(list);
	}

	/**
	 * 返回查询sku需要展示的条件
	 * <p>规格:</p>
	 *  <p>XXX XXX</p>
      * <p>颜色:</p>
	 *  <p>XXX XXX</p>
	 * @param gid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/condition/{gid}", method = RequestMethod.GET)
	public Result<GoodsSkuConditionVO> getGoodsSkuCondition(@PathVariable String gid) throws Exception {
		Optional<GoodsSkuCondition> optional = wapGoodsService.singleGoodsSkuCondition(gid);
		GoodsSkuConditionVO vo = null;
		if (Objects.nonNull(optional)) {
			vo = new GoodsSkuConditionVO();
			BeanUtils.copyProperties(optional.get(), vo);

		}
		return Result.ok(vo);
	}

	/**
	 * 根据属性获取对应的sku
	 * 
	 * @param condition
	 * @param gid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/single/{gid}/{condition}", method = RequestMethod.GET)
	public Result<GoodsSkuVO> getGoodsSku(@PathVariable String condition, @PathVariable String gid) throws Exception {
		Optional<GoodsSku> optional = wapGoodsService.singleGoodsSku(gid, condition);
		GoodsSkuVO vo = null;
		if (Objects.nonNull(optional)) {
			vo = new GoodsSkuVO();
			BeanUtils.copyProperties(optional.get(), vo);
		}
		return Result.ok(vo);
	}

	/**
	 * 获取商品详情信息
	 * @param gid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/detail/{gid}", method = RequestMethod.GET)
	public Result<GoodsDeatilVO> getGoodsDetail(@PathVariable String gid) throws Exception {
		Optional<GoodsDeatil> optional = wapGoodsService.singleGoodsDetail(gid);
		
		GoodsDeatilVO vo = null;
		if (Objects.nonNull(optional)) {
			vo = new GoodsDeatilVO();
			BeanUtils.copyProperties(optional.get(), vo);
		}
		return Result.ok(vo);
	}
	
	
	
	/**
	 * 获取产品参数
	 * @param gid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/info/{gid}", method = RequestMethod.GET)
	public Result<List<GoodsInfoVO>> getGoodsInfo(@PathVariable String gid) throws Exception {
		List<GoodsInfoVO> list = wapGoodsService.listGoodsInfo(gid).stream().map(bo -> {
			GoodsInfoVO vo = new GoodsInfoVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());

		return Result.ok(list);
	}
	
	

}
