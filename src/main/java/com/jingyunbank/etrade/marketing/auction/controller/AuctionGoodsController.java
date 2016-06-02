package com.jingyunbank.etrade.marketing.auction.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.etrade.api.marketing.auction.bo.AuctionGoods;
import com.jingyunbank.etrade.api.marketing.auction.service.IAuctionGoodsService;
import com.jingyunbank.etrade.marketing.auction.bean.AuctionGoodsVO;
import com.jingyunbank.etrade.wap.goods.bean.GoodsSkuVO;
import com.jingyunbank.etrade.wap.goods.bean.GoodsVO;

@RestController
@RequestMapping("/api/marketing/auction/goods")
public class AuctionGoodsController {
	
	@Autowired
	private IAuctionGoodsService auctionGoodsService ;
	
	//新增
	@AuthBeforeOperation
	@RequestMapping(value="/addition", method=RequestMethod.POST)
	public Result<String> add(@RequestBody @Valid AuctionGoodsVO goods, BindingResult valid) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的数据不合法，请检查后重新输入。");
		}
		
		AuctionGoods goodsbo = new AuctionGoods();
		goods.setID(KeyGen.uuid());
		goods.setStatus(AuctionGoods.STATUS_NEW);
		BeanUtils.copyProperties(goods, goodsbo);
		auctionGoodsService.save(goodsbo);
		
		return Result.ok();
	}
	
	//list
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public Result<List<AuctionGoodsVO>> list(@RequestParam long offset,
			@RequestParam long size) throws Exception{
		Range range = new Range(offset, offset+size);
		return Result.ok(auctionGoodsService.list(range).stream().map(bo->{
			return getShowVOFromBo(bo);
		}).collect(Collectors.toList()));
	}
	
	@RequestMapping(value="/count", method=RequestMethod.GET)
	public Result<Integer> count() throws Exception{
		return Result.ok(auctionGoodsService.count());
	}
	
	//single
	@RequestMapping(value="/detail", method=RequestMethod.GET)
	public Result<AuctionGoodsVO> single(@RequestParam(required=true) String ID,HttpSession session) throws Exception{
		Optional<AuctionGoods> goods = auctionGoodsService.single(ID);
		if(goods.isPresent()){
			return Result.ok(getShowVOFromBo(goods.get()));
		}
		return Result.fail("未找到");
	}
	@RequestMapping(value="/single", method=RequestMethod.GET)
	public Result<AuctionGoodsVO> single(HttpSession session) throws Exception{
		System.out.println("ddd");
			String ID=session.getAttribute("AUCTION_ID").toString();
			System.out.println(ID+"KKKKKKKKKK");
		
		Optional<AuctionGoods> goods = auctionGoodsService.single(ID);
		if(goods.isPresent()){
			return Result.ok(getShowVOFromBo(goods.get()));
		}
		return Result.fail("未找到");
	}
	
	@RequestMapping(value="/goDetail", method=RequestMethod.GET)
	public String selDetail(@RequestParam(required=true) String ID){
		
		String id="VkkfDqkPR6upgaY_NA4WYA";
		String gid="6J5oe2uzTVuUCP-P3-sJbA";
		return "/auction-details.html?id="+id+"&gid="+gid+"";
		
	}

	
	private AuctionGoodsVO getShowVOFromBo(AuctionGoods showBo){
		AuctionGoodsVO vo = new AuctionGoodsVO();
		if(Objects.nonNull(showBo)){
			BeanUtils.copyProperties(showBo, vo);
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
		}
		return vo;
	}
	
	

}
