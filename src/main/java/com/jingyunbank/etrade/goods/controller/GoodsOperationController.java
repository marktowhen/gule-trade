package com.jingyunbank.etrade.goods.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.goods.bo.Goods;
import com.jingyunbank.etrade.api.goods.bo.GoodsDetail;
import com.jingyunbank.etrade.api.goods.bo.GoodsImg;
import com.jingyunbank.etrade.api.goods.bo.GoodsOperation;
import com.jingyunbank.etrade.api.goods.bo.ShowGoods;
import com.jingyunbank.etrade.api.goods.service.IGoodsOperationService;
import com.jingyunbank.etrade.goods.bean.GoodsBrandVO;
import com.jingyunbank.etrade.goods.bean.GoodsMerchantVO;
import com.jingyunbank.etrade.goods.bean.GoodsOperationVO;
import com.jingyunbank.etrade.goods.bean.GoodsVO;

/**
 * 
 * Title: 商品的操作Controller
 * 
 * @author duanxf
 * @date 2015年11月13日
 */
@RestController
@RequestMapping("/api/goodsOperation")
public class GoodsOperationController {
	@Resource
	private IGoodsOperationService goodsOperationService;

	/**
	 * 保存商品
	 * 
	 * @param request
	 * @param vo
	 * @param valid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Result saveGoods(HttpServletRequest request, @RequestBody @Valid GoodsOperationVO vo, BindingResult valid)
			throws Exception {
		// 异常信息
		if (valid.hasErrors()) {
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream().map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
					.collect(Collectors.joining(" ; ")));
		}
		// ------封装商品信息---------------
		Goods goods = new Goods();
		BeanUtils.copyProperties(vo, goods);
		goods.setID(KeyGen.uuid()); // id
		goods.setVolume(0); // 销量
		goods.setAddTime(new Date());// 添加时间
		goods.setAdminSort(0);// 管理员排序
		goods.setMerchantSort(0);// 商家排序
		goods.setExpandSort(0);// 推广排序
		goods.setRecordSort(0);// 推荐排序

		// ------封装商品详细信息-----------
		GoodsDetail detail = new GoodsDetail();
		BeanUtils.copyProperties(vo, detail);
		detail.setID(KeyGen.uuid());
		detail.setGID(goods.getID());
		// ------封装商品图片信息-----------
		GoodsImg img = new GoodsImg();
		img.setID(KeyGen.uuid());
		img.setGID(goods.getID());
		img.setThumbpath1(vo.getThumbpath1());
		img.setThumbpath2(vo.getThumbpath2());
		img.setThumbpath3(vo.getThumbpath3());
		img.setThumbpath4(vo.getThumbpath4());
		img.setThumbpath5(vo.getThumbpath5());
		img.setContent(vo.getContent());

		// ------封装bo对象--------------------
		GoodsOperation operation = new GoodsOperation();
		operation.setGoods(goods);
		operation.setGoodsDetail(detail);
		operation.setGoodsImg(img);
		// ------------------------------------
		if (goodsOperationService.save(operation)) {
			return Result.ok("success");
		} else {
			return Result.fail("fail");
		}
	}

	/**
	 * 根据GID 获取商品信息 用于修改商品回显
	 * 
	 * @param gid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateveiw/{gid}", method = RequestMethod.GET)
	public Result queryGoodsById(@PathVariable String gid) throws Exception {
		GoodsVO vo = null;
		Optional<ShowGoods> showbo = goodsOperationService.singleById(gid);
		if (Objects.nonNull(showbo)) {
			vo = new GoodsVO();
			BeanUtils.copyProperties(showbo.get(), vo);
		}
		return Result.ok(vo);
	}

	/**
	 * 修改商品详细信息
	 * 
	 * @param gid
	 * @param vo
	 * @param valid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/update/{gid}", method = RequestMethod.PUT)
	public Result updateGoods(@PathVariable String gid, @RequestBody @Valid GoodsOperationVO vo, BindingResult valid)
			throws Exception {
		// 异常信息
		if (valid.hasErrors()) {
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream().map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
					.collect(Collectors.joining(" ; ")));
		}

		// ------封装商品信息---------------
		Goods goods = new Goods();
		BeanUtils.copyProperties(vo, goods);
		goods.setID(gid);

		// ------封装商品详细信息-----------
		GoodsDetail detail = new GoodsDetail();
		BeanUtils.copyProperties(vo, detail);
		detail.setGID(gid);

		// ------封装商品图片信息-----------
		GoodsImg img = new GoodsImg();
		img.setGID(gid);
		img.setThumbpath1(vo.getThumbpath1());
		img.setThumbpath2(vo.getThumbpath2());
		img.setThumbpath3(vo.getThumbpath3());
		img.setThumbpath4(vo.getThumbpath4());
		img.setThumbpath5(vo.getThumbpath5());
		img.setContent(vo.getContent());
		// ------封装bo对象--------------------
		GoodsOperation operation = new GoodsOperation();
		operation.setGoods(goods);
		operation.setGoodsDetail(detail);
		operation.setGoodsImg(img);
		// ------------------------------------
		if (goodsOperationService.refreshGoods(operation)) {
			return Result.ok("success");
		} else {
			return Result.fail("fail");
		}
	}

	/**
	 * 商品上架
	 * @param gid
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/up/{gid}", method = RequestMethod.PUT)
	public Result goodsUp(@PathVariable String gid) throws Exception {
		if(goodsOperationService.refreshGoodsUp(gid)){
			return Result.ok("success");
		}
		return Result.fail("fail");

	}
	/**
	 * 商品下架
	 * @param gid
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/down/{gid}", method = RequestMethod.PUT)
	public Result goodsDown(@PathVariable String gid) throws Exception {
		if(goodsOperationService.refreshGoodsDown(gid)){
			return Result.ok("success");
		}
		return Result.fail("fail");

	}
	/**
	 * 修改库存和销量
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateVolume", method = RequestMethod.POST)
	public Result goodsUp(HttpServletRequest request) throws Exception {
		String gid = request.getParameter("gid");
		int count =  Integer.valueOf(request.getParameter("count"));
		boolean flag = goodsOperationService.refreshGoodsVolume(gid,count);
		return Result.ok(flag);
	}
	
	
	
	/**
	 * 查询所属的店铺
	 * @param mid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/merchant/list", method = RequestMethod.GET)
	public Result getmerchants() throws Exception{
		List<GoodsMerchantVO> list =goodsOperationService.listMerchant().stream().map(bo -> {
			GoodsMerchantVO vo = new GoodsMerchantVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}
	
	/**
	 * 根据MID 查询所属的品牌
	 * @param mid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/brands/{mid}", method = RequestMethod.GET)
	public Result getBrandByMid(@PathVariable String mid) throws Exception{
		List<GoodsBrandVO> list = goodsOperationService.listBrandsByMid(mid).stream().map(bo -> {
			GoodsBrandVO vo = new GoodsBrandVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}

}
