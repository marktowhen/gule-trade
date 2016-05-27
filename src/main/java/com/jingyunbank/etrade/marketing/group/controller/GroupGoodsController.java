package com.jingyunbank.etrade.marketing.group.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.etrade.api.marketing.group.bo.Group;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupGoods;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupGoodsPriceSetting;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupGoodsShow;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupUser;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupGoodsService;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupService;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupUserService;
import com.jingyunbank.etrade.marketing.group.bean.GroupGoodsShowVO;
import com.jingyunbank.etrade.marketing.group.bean.GroupGoodsVO;
import com.jingyunbank.etrade.marketing.group.bean.GroupUserVO;
import com.jingyunbank.etrade.marketing.group.bean.GroupVO;
import com.jingyunbank.etrade.wap.goods.bean.GoodsSkuVO;
import com.jingyunbank.etrade.wap.goods.bean.GoodsVO;

@RestController
@RequestMapping("/api/marketing/group")
public class GroupGoodsController {

	@Autowired
	private IGroupGoodsService groupGoodsService;
	@Autowired
	private IGroupService groupService;
	@Autowired
	private IGroupUserService groupUserService;
	
	
	@AuthBeforeOperation
	@RequestMapping(value="/goods/addition", method=RequestMethod.POST)
	public Result<String> add(@RequestBody @Valid GroupGoodsVO goods, BindingResult valid) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的数据不合法，请检查后重新输入。");
		}
		
		GroupGoods goodsbo = new GroupGoods();
		BeanUtils.copyProperties(goods, goodsbo);
		/*goods.getPriceSettings().forEach(vo -> {
			GroupGoodsPriceSetting bo = new GroupGoodsPriceSetting();
			BeanUtils.copyProperties(vo, bo);
			goodsbo.getPriceSettings().add(bo);
		});*/
		groupGoodsService.save(goodsbo);
		
		return Result.ok();
	}
	
	/**
	 * 通过团的id查出对应团的信息
	 * @param groupid
	 * @return
	 */
	@RequestMapping(value="/{groupid}",method=RequestMethod.GET)
	public Result<GroupVO> getSingleGroup(@PathVariable String groupid){
		Optional<Group> bo = groupService.single(groupid);
		GroupVO vo = new GroupVO();
		BeanUtils.copyProperties(bo.get(), vo);
		return Result.ok(vo);
		
	}
	@AuthBeforeOperation
	@RequestMapping(value="/goods", method=RequestMethod.PUT)
	public Result<String> refresh(@RequestBody @Valid GroupGoodsVO goods, BindingResult valid) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的数据不合法，请检查后重新输入。");
		}
		
		GroupGoods goodsbo = new GroupGoods();
		BeanUtils.copyProperties(goods, goodsbo, "priceSettings");
		/*goods.getPriceSettings().forEach(vo -> {
			GroupGoodsPriceSetting bo = new GroupGoodsPriceSetting();
			BeanUtils.copyProperties(vo, bo);
			goodsbo.getPriceSettings().add(bo);
		});*/
		groupGoodsService.refresh(goodsbo);
		
		return Result.ok();
	}
	
	//列出指定商家的团购商品，参数指定列出的条数
	@RequestMapping(value="/goods/list", method=RequestMethod.GET)
	public Result<List<GroupGoodsShowVO>> list(@RequestParam(required=false) String mid ,
			@RequestParam int offset, @RequestParam int size,@RequestParam(required=false) String name) throws Exception{
		
		size = size == 0? 10 : size;
		Range range = new Range(offset, size + offset);
		List<GroupGoodsShow> bos = groupGoodsService.list(mid,name, range);
		List<GroupGoodsShowVO> vos = new ArrayList<GroupGoodsShowVO>();
		bos.forEach(bo -> {
			vos.add(getShowVOFromBo(bo));
		});
		return Result.ok(vos);
	}
	
	@RequestMapping(value="/goods/count", method=RequestMethod.GET)
	public Result<Integer> count(@RequestParam(required=false) String mid ,@RequestParam(required=false) String name) throws Exception{
		
		return Result.ok(groupGoodsService.count(mid,name));
	}
	
	//查询指定团购商品的详情
	@RequestMapping(value="/goods/detail", method=RequestMethod.GET)
	public Result<GroupGoodsVO> single(@RequestParam(required=true) String ggid) throws Exception{
		
		Optional<GroupGoods> boc = groupGoodsService.single(ggid);
		if(boc.isPresent()){
			GroupGoodsVO vo = new GroupGoodsVO();
			BeanUtils.copyProperties(boc.get(), vo);
			return Result.ok(vo);
		}
		return Result.fail("未找到指定的团购商品！");
	}
	
	private GroupGoodsShowVO getShowVOFromBo(GroupGoodsShow showBo){
		GroupGoodsShowVO vo = new GroupGoodsShowVO();
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
