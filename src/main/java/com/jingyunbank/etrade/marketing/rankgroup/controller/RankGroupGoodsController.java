package com.jingyunbank.etrade.marketing.rankgroup.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.etrade.api.marketing.rankgroup.bo.RankGroupGoods;
import com.jingyunbank.etrade.api.marketing.rankgroup.bo.RankGroupGoodsPriceSetting;
import com.jingyunbank.etrade.api.marketing.rankgroup.bo.RankGroupGoodsShow;
import com.jingyunbank.etrade.marketing.group.bean.GroupGoodsVO;
import com.jingyunbank.etrade.marketing.rankgroup.bean.RankGroupGoodsShowVO;
import com.jingyunbank.etrade.marketing.rankgroup.bean.RankGroupGoodsVO;
import com.jingyunbank.etrade.marketing.rankgroup.service.RankGroupGoodsService;
import com.jingyunbank.etrade.wap.goods.bean.GoodsSkuVO;
import com.jingyunbank.etrade.wap.goods.bean.GoodsVO;

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
	@AuthBeforeOperation
	@RequestMapping(value="/goods", method=RequestMethod.PUT)
	public Result<String> refresh(@RequestBody @Valid RankGroupGoodsVO goods, BindingResult valid) throws Exception{
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
		rankGroupGoodsService.refresh(goodsbo);
		
		return Result.ok();
	}
	
	//列出指定商家的团购商品，参数指定列出的条数
	@RequestMapping(value="/goods/list", method=RequestMethod.GET)
	public Result<List<RankGroupGoodsShowVO>> list(@RequestParam(required=false) String mid ,
			@RequestParam int offset, @RequestParam int size,@RequestParam(required=false) String name) throws Exception{
		size = size == 0? 10 : size;
		Range range = new Range(offset, size + offset);
		List<RankGroupGoodsShow> bos = rankGroupGoodsService.list(mid,name, range);
		List<RankGroupGoodsShowVO> vos = new ArrayList<RankGroupGoodsShowVO>();
		bos.forEach(bo -> {
			vos.add(getShowVOFromBo(bo));
		});
		return Result.ok(vos);
	}
	
	@RequestMapping(value="/goods/count", method=RequestMethod.GET)
	public Result<Integer> count(@RequestParam(required=false) String mid ,@RequestParam(required=false) String name) throws Exception{
		
		return Result.ok(rankGroupGoodsService.count(mid,name));
	}
	
	//查询指定团购商品的详情
	@RequestMapping(value="/goods/detail", method=RequestMethod.GET)
	public Result<GroupGoodsVO> single(@RequestParam(required=true) String ggid) throws Exception{
		
		Optional<RankGroupGoods> boc = rankGroupGoodsService.single(ggid);
		if(boc.isPresent()){
			GroupGoodsVO vo = new GroupGoodsVO();
			BeanUtils.copyProperties(boc.get(), vo);
			return Result.ok(vo);
		}
		return Result.fail("未找到指定的团购商品！");
	}
	
	private RankGroupGoodsShowVO getShowVOFromBo(RankGroupGoodsShow showBo){
		RankGroupGoodsShowVO vo = new RankGroupGoodsShowVO();
		BeanUtils.copyProperties(showBo, vo, "priceSettings", "groups");
		if(Objects.nonNull(showBo.getGoods())){
			GoodsVO goodsBo = new GoodsVO();
			BeanUtils.copyProperties(showBo.getGoods(), goodsBo);
			vo.setGoods(goodsBo);
		}
		if(Objects.nonNull(showBo.getSku())){
			GoodsSkuVO sku = new GoodsSkuVO();
			BeanUtils.copyProperties(showBo.getSku(), sku);
			vo.setSku(sku);
		}
		
		return vo;
	}


}
