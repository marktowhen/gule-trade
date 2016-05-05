package com.jingyunbank.etrade.marketing.rankgroup.controller;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.etrade.api.marketing.rankgroup.bo.RankGroupGoods;
import com.jingyunbank.etrade.api.marketing.rankgroup.bo.RankGroupGoodsPriceSetting;
import com.jingyunbank.etrade.marketing.rankgroup.bean.RankGroupGoodsVO;
import com.jingyunbank.etrade.marketing.rankgroup.service.RankGroupGoodsService;

@RestController
@RequestMapping("/api/marketing/rankgroup")
public class RankGroupGoodsController {
	
	@Autowired 
	RankGroupGoodsService rankGroupGoodsService;
	
	@AuthBeforeOperation
	@RequestMapping(value="/addGoods", method=RequestMethod.POST)
	public Result<String> add(@RequestBody @Valid RankGroupGoodsVO goods, BindingResult valid) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的数据不合法，请检查后重新输入。");
		}
		
		RankGroupGoods goodsbo = new RankGroupGoods();
		BeanUtils.copyProperties(goods, goodsbo, "priceSettings");
		goods.getPriceSettings().forEach(vo -> {
			RankGroupGoodsPriceSetting bo = new RankGroupGoodsPriceSetting();
			BeanUtils.copyProperties(vo, bo);
			goodsbo.getPriceSettings().add(bo);
		});
		rankGroupGoodsService.save(goodsbo);
		
		return Result.ok();
	}
	

}
