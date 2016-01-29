package com.jingyunbank.etrade.back.goods.controller;


import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Page;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.goods.bo.GoodsSearch;
import com.jingyunbank.etrade.api.goods.service.IGoodsService;
import com.jingyunbank.etrade.back.goods.bean.GoodsListShowVO;
import com.jingyunbank.etrade.back.goods.bean.GoodsSearchVO;


/**
 * 商品后台管理系统
 * @author liug
 *
 */
@RestController
@RequestMapping("/api/back/goods")
public class GoodsBKController {

	@Resource
	protected IGoodsService goodsService;

	/**
	 * 根据条件查询商品列表
	 * @param request
	 * @param goodsSearchVO
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<List<GoodsListShowVO>> queryGoodsByCondition(HttpServletRequest request, GoodsSearchVO goodsSearchVO, Page page)
			throws Exception {
		Range range = new Range();
		range.setFrom(page.getOffset());
		range.setTo(page.getSize());

		
		List<GoodsListShowVO> goodslist = goodsService.listGoodsByCondition(goodsSearchVO.getName(), goodsSearchVO.getState(), goodsSearchVO.getMID(), goodsSearchVO.getBID(), range).stream().map(bo -> {
			GoodsListShowVO vo = new GoodsListShowVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		
		return Result.ok(goodslist);
	}
    
}
