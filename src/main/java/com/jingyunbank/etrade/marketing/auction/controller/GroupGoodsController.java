package com.jingyunbank.etrade.marketing.auction.controller;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupGoods;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupGoodsPriceSetting;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupGoodsService;
import com.jingyunbank.etrade.marketing.group.bean.GroupGoodsVO;

@RestController
public class GroupGoodsController {

	@Autowired
	private IGroupGoodsService groupGoodsService;
	
	@RequestMapping(value="/api/group/goods/addition", method=RequestMethod.POST)
	public Result<String> add(@RequestBody @Valid GroupGoodsVO goods, BindingResult valid) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的数据不合法，请检查后重新输入。");
		}
		
		GroupGoods goodsbo = new GroupGoods();
		BeanUtils.copyProperties(goods, goodsbo, "priceSettings");
		goods.getPriceSettings().forEach(vo -> {
			GroupGoodsPriceSetting bo = new GroupGoodsPriceSetting();
			BeanUtils.copyProperties(vo, bo);
			goodsbo.getPriceSettings().add(bo);
		});
		groupGoodsService.save(goodsbo);
		
		return Result.ok();
	}
}
