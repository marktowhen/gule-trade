package com.jingyunbank.etrade.wap.goods.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.wap.goods.bo.Goods;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsAttrValue;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsImg;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsOperation;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsOperationShow;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsSku;
import com.jingyunbank.etrade.api.wap.goods.service.IWapGoodsOperationService;
import com.jingyunbank.etrade.wap.goods.bean.GoodsAttrVO;
import com.jingyunbank.etrade.wap.goods.bean.GoodsVO;

/**
 * 
 * Title: WapGoodsOperationController 商品操作
 * 
 * @author duanxf
 * @date 2016年4月12日
 */
@RestController
@RequestMapping("/api/goods/operation")
public class WapGoodsOperationController {
	@Autowired
	private IWapGoodsOperationService wapGoodsOperationService;

	/**
	 * 添加商品
	 * 
	 * @param request
	 * @param vo
	 * @param valid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Result<String> saveGoods(HttpServletRequest request, @RequestBody @Valid GoodsVO vo, BindingResult valid)
			throws Exception {
		if (valid.hasErrors()) {
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream().map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
					.collect(Collectors.joining(" ; ")));
		}
		// ---商品基本信息---
		Goods goods = new Goods();
		BeanUtils.copyProperties(vo, goods);
		goods.setID(KeyGen.uuid());
		goods.setStatus(true);

		// ------- sku信息---
		List<GoodsSku> skuList = vo.getSkuList().stream().map(sku -> {
			GoodsSku goodsSku = new GoodsSku();
			BeanUtils.copyProperties(sku, goodsSku);
			goodsSku.setID(KeyGen.uuid());
			goodsSku.setGID(goods.getID());
			goodsSku.setStatus(true);
			return goodsSku;
		}).collect(Collectors.toList());

		// -------属性信息---
		List<GoodsAttrValue> attrValueList = vo.getAttrValueList().stream().map(attrValue -> {
			GoodsAttrValue av = new GoodsAttrValue();
			BeanUtils.copyProperties(attrValue, av);
			av.setID(KeyGen.uuid());
			av.setGID(goods.getID());
			return av;
		}).collect(Collectors.toList());
		// --------图片信息---
		List<GoodsImg> imgList = vo.getImgList().stream().map(imgvo -> {
			GoodsImg img = new GoodsImg();
			BeanUtils.copyProperties(imgvo, img);
			img.setID(KeyGen.uuid());
			img.setGID(goods.getID());
			return img;
		}).collect(Collectors.toList());
		// ------------------
		GoodsOperation goodsOperation = new GoodsOperation();
		goodsOperation.setGoods(goods);
		goodsOperation.setAttrValueList(attrValueList);
		goodsOperation.setSkuList(skuList);
		goodsOperation.setImgList(imgList);

		if (wapGoodsOperationService.saveGoods(goodsOperation)) {
			return Result.ok("success");
		}
		return Result.fail("fail");
	}

	/**
	 * 修改商品
	 * 
	 * @param request
	 * @param gid
	 * @param vo
	 * @param valid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/update/{gid}", method = RequestMethod.POST)
	public Result<String> updateGoods(HttpServletRequest request, @PathVariable String gid,
			@RequestBody @Valid GoodsVO vo, BindingResult valid) throws Exception {
		if (valid.hasErrors()) {
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream().map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
					.collect(Collectors.joining(" ; ")));
		}
		// ---商品基本信息---
		Goods goods = new Goods();
		BeanUtils.copyProperties(vo, goods);
		goods.setID(gid);

		// ------- sku信息---
		List<GoodsSku> skuList = vo.getSkuList().stream().map(sku -> {
			GoodsSku goodsSku = new GoodsSku();
			BeanUtils.copyProperties(sku, goodsSku);
			goodsSku.setID(KeyGen.uuid());
			goodsSku.setGID(goods.getID());
			goodsSku.setStatus(true);
			return goodsSku;
		}).collect(Collectors.toList());

		// -------属性信息---
		List<GoodsAttrValue> attrValueList = vo.getAttrValueList().stream().map(attrValue -> {
			GoodsAttrValue av = new GoodsAttrValue();
			BeanUtils.copyProperties(attrValue, av);
			av.setID(KeyGen.uuid());
			av.setGID(goods.getID());
			return av;
		}).collect(Collectors.toList());
		// --------图片信息---
		List<GoodsImg> imgList = vo.getImgList().stream().map(imgvo -> {
			GoodsImg img = new GoodsImg();
			BeanUtils.copyProperties(imgvo, img);
			img.setID(KeyGen.uuid());
			img.setGID(goods.getID());
			return img;
		}).collect(Collectors.toList());

		GoodsOperation goodsOperation = new GoodsOperation();
		goodsOperation.setGoods(goods);
		goodsOperation.setAttrValueList(attrValueList);
		goodsOperation.setSkuList(skuList);
		goodsOperation.setImgList(imgList);
		if (wapGoodsOperationService.modfiyGoods(goodsOperation)) {
			return Result.ok("success");
		}
		return Result.fail("fail");
	}

	/**
	 * 商品修改回显
	 * @param request
	 * @param gid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/view/{gid}", method = RequestMethod.GET)
	public Result<GoodsVO> queryGoodsByGid(HttpServletRequest request, @PathVariable String gid) throws Exception {
		Optional<GoodsOperationShow> optional = wapGoodsOperationService.getGoodsByGid(gid);
		GoodsVO vo = null;
		if (Objects.nonNull(optional)) {
			vo = new GoodsVO();
			BeanUtils.copyProperties(optional.get(), vo);
		}
		//TODO sql语句没完
		return Result.ok(vo);
	}

	/**
	 * 商品上架
	 * 
	 * @param request
	 * @param skuId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/up/{skuId}", method = RequestMethod.POST)
	public Result<String> up(HttpServletRequest request, @PathVariable String skuId) throws Exception {
		if (wapGoodsOperationService.up(skuId)) {
			return Result.ok("success");
		}
		return Result.fail("fail");
	}

	/**
	 * 商品下架
	 * 
	 * @param request
	 * @param skuId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/down/{skuId}", method = RequestMethod.POST)
	public Result<String> down(HttpServletRequest request, @PathVariable String skuId) throws Exception {
		if (wapGoodsOperationService.down(skuId)) {
			return Result.ok("success");
		}
		return Result.fail("fail");
	}
}
