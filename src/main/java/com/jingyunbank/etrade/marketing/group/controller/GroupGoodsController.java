package com.jingyunbank.etrade.marketing.group.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupGoods;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupGoodsPriceSetting;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupGoodsService;
import com.jingyunbank.etrade.marketing.group.bean.GroupGoodsVO;

@RestController
@RequestMapping("/api/marketing/group")
public class GroupGoodsController {

	@Autowired
	private IGroupGoodsService groupGoodsService;
	
	@RequestMapping(value="/goods/addition", method=RequestMethod.POST)
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
	
	//列出指定商家的团购商品，参数指定列出的条数
	@RequestMapping(value="/goods/list", method=RequestMethod.GET)
	public Result<List<GroupGoodsVO>> list(@RequestParam int offset, @RequestParam int size) throws Exception{
		
		size = size == 0? 10 : size;
		Range range = new Range(offset, size + offset);
		List<GroupGoods> bos = groupGoodsService.list(range);
		List<GroupGoodsVO> vos = new ArrayList<GroupGoodsVO>();
		bos.forEach(bo -> {
			GroupGoodsVO vo = new GroupGoodsVO();
			BeanUtils.copyProperties(bo, vo, "priceSettings", "groups");
			vos.add(vo);
		});
		return Result.ok(vos);
	}
	
	//查询指定团购商品的详情
	@RequestMapping(value="/goods/detail", method=RequestMethod.GET)
	public Result<GroupGoodsVO> single(@RequestParam(required=true) String ggid) throws Exception{
		
		Optional<GroupGoods> boc = groupGoodsService.single(ggid);
		if(boc.isPresent()){
			GroupGoodsVO vo = new GroupGoodsVO();
			BeanUtils.copyProperties(boc.get(), vo, "priceSettings");
			return Result.ok(vo);
		}
		return Result.fail("未找到指定的团购商品！");
	}
	
}
