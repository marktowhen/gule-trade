package com.jingyunbank.etrade.marketing.flashsale.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
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
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.marketing.flashsale.bo.FlashSale;
import com.jingyunbank.etrade.api.marketing.flashsale.bo.FlashSaleShow;
import com.jingyunbank.etrade.api.marketing.flashsale.service.IFlashSaleService;
import com.jingyunbank.etrade.marketing.flashsale.bean.FlashSaleShowVo;
import com.jingyunbank.etrade.marketing.flashsale.bean.FlashSaleVo;
import com.jingyunbank.etrade.wap.goods.bean.GoodsSkuVO;
import com.jingyunbank.etrade.wap.goods.bean.GoodsVO;

@RestController
@RequestMapping("api/flash/sale")
public class FlashSaleController {
	@Autowired 
	private IFlashSaleService flashSaleService;
	
	
	/**
	 * 保存秒杀商品的信息
	 * @param request
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public Result<String> saveFlashSale(HttpServletRequest request, @RequestBody @Valid FlashSaleVo vo,BindingResult valid) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的数据不合法，请检查后重新输入。");
		}	
				
		//-----------秒杀商品的基本信息-----------
		FlashSale flashSale = new FlashSale();
		BeanUtils.copyProperties(vo, flashSale);
		if(vo.isShows()){
			flashSale.setShowTime(new Date());
		}else{
			flashSale.setShowTime(null);
		}
		
		flashSale.setId(KeyGen.uuid());
		
		
		if(flashSaleService.saveFlashSale(flashSale)){
			return Result.ok("success");
		}
		return Result.fail("fail");
		
	}
	//列出所有的秒杀商品
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public Result<List<FlashSaleShowVo>> getFlashSaleMany(@RequestParam(required=false) String mid ,
			@RequestParam int offset, @RequestParam int size){
		/*size = size == 0? 10 : size;*/
		Range range = new Range(offset, size + offset);
		List<FlashSaleShow> list=flashSaleService.getFlashSaleMany(mid,range);
		List<FlashSaleShowVo> listVo = new ArrayList<FlashSaleShowVo>();
		list.forEach(bo ->{
			listVo.add(getShowVOFromBo(bo));
		});
		return Result.ok(listVo);
		
	}
	//通过id查出产品
	@RequestMapping(value="/detail",method=RequestMethod.GET)
	public Result<FlashSaleVo> getFlashSaleById(@RequestParam(required=true) String id){
		Optional<FlashSale> fso=flashSaleService.single(id);
		if(fso.isPresent()){
			FlashSaleVo vo = new FlashSaleVo();
			BeanUtils.copyProperties(fso.get(), vo);
			return Result.ok(vo);
		}
		return Result.fail("未找到秒杀的产品");
		
	}
	private FlashSaleShowVo getShowVOFromBo(FlashSaleShow showBo){
		FlashSaleShowVo vo = new FlashSaleShowVo();
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
		
		return vo;
	}
	//修改属性值
	@RequestMapping(value="/update",method=RequestMethod.PUT)
	public Result<String> updateFlashSale(HttpServletRequest request, @RequestBody @Valid FlashSaleVo vo,BindingResult valid) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的数据不合法，请检查后重新输入。");
		}	
		FlashSale bo = new FlashSale();
		if(vo.isShows()!=flashSaleService.single(vo.getId()).get().isShows()){
			bo.setShowTime(new Date());
		}else{
			bo.setShowTime(flashSaleService.single(vo.getId()).get().getShowTime());
		}
		BeanUtils.copyProperties(vo, bo);
		flashSaleService.refresh(bo);
		return Result.ok();
		
	}
	@RequestMapping(value="/bycondition/list",method=RequestMethod.GET)
	public Result<List<FlashSaleShowVo>> getFlashSaleByCondition(@RequestParam int offset, @RequestParam int size){
		Range range = new Range(offset, size + offset);
		List<FlashSaleShow> boList=flashSaleService.getFlashSaleByCondition(range);
		List<FlashSaleShowVo> voList = new ArrayList<FlashSaleShowVo>();
		boList.forEach(bo ->{
			voList.add(getShowVOFromBo(bo));
		});
		return Result.ok(voList);
		
	}
	/**
	 * 当订单完成时，减去活动数量！
	 * @param request
	 * @param vo
	 * @param valid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/update/stock",method=RequestMethod.PUT)
	public Result<String> updateStockFlashSale(HttpServletRequest request, @RequestBody @Valid FlashSaleVo vo,BindingResult valid) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的数据不合法，请检查后重新输入。");
		}	
		FlashSale bo = new FlashSale();
		BeanUtils.copyProperties(vo, bo);
		flashSaleService.refreshStock(bo);
		
		return Result.ok();
		
	}

}
