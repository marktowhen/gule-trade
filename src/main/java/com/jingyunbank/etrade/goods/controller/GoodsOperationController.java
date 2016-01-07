package com.jingyunbank.etrade.goods.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.goods.bo.BaseGoodsOperation;
import com.jingyunbank.etrade.api.goods.bo.Goods;
import com.jingyunbank.etrade.api.goods.bo.GoodsDetail;
import com.jingyunbank.etrade.api.goods.bo.GoodsImg;
import com.jingyunbank.etrade.api.goods.bo.GoodsOperation;
import com.jingyunbank.etrade.api.goods.service.IGoodsOperationService;
import com.jingyunbank.etrade.goods.bean.GoodsBrandVO;
import com.jingyunbank.etrade.goods.bean.GoodsMerchantVO;
import com.jingyunbank.etrade.goods.bean.GoodsOperationShowVO;
import com.jingyunbank.etrade.goods.bean.GoodsOperationVO;

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
	 * String 转Date
	 * 
	 * @param time
	 * @return
	 */
	public Date string2Date(String time) {
		Date date = null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			date = sf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

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
	public Result<String> saveGoods(HttpServletRequest request, @RequestBody @Valid GoodsOperationVO vo,
			BindingResult valid) throws Exception {
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

		if (vo.getUpTime() != null && vo.getUpTime() != "") {
			goods.setUpTime(string2Date(vo.getUpTime()));
		}
		if (vo.getDownTime() != null && vo.getDownTime() != "") {
			goods.setDownTime(string2Date(vo.getDownTime()));
		}
		if (vo.getOnSaleBeginTime() != null && vo.getOnSaleBeginTime() != "") {
			goods.setOnSaleBeginTime(string2Date(vo.getOnSaleBeginTime()));
		}
		if (vo.getOnSaleEndTime() != null && vo.getOnSaleEndTime() != "") {
			goods.setOnSaleEndTime(string2Date(vo.getOnSaleEndTime()));
		}

		goods.setAdminSort(0);// 管理员排序
		goods.setMerchantSort(0);// 商家排序
		goods.setExpandSort(0);// 推广排序
		goods.setRecordSort(0);// 推荐排序

		// ------封装商品详细信息-----------
		GoodsDetail detail = new GoodsDetail();
		BeanUtils.copyProperties(vo, detail);
		if (vo.getProductionDate() != null && vo.getProductionDate() != "") {
			detail.setProductionDate(string2Date(vo.getProductionDate()));
		}
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
	public Result<GoodsOperationShowVO> queryGoodsById(@PathVariable String gid) throws Exception {
		GoodsOperationShowVO vo = null;
		Optional<BaseGoodsOperation> showbo = goodsOperationService.singleById(gid);
		if (Objects.nonNull(showbo)) {
			vo = new GoodsOperationShowVO();
			BeanUtils.copyProperties(showbo.get(), vo);
		}
		// System.err.println(vo.getProductionDate());
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
	public Result<String> updateGoods(@PathVariable String gid, @RequestBody @Valid GoodsOperationVO vo,
			BindingResult valid) throws Exception {
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
		if (vo.getUpTime() != null && vo.getUpTime() != "") {
			goods.setUpTime(string2Date(vo.getUpTime()));
		}
		if (vo.getDownTime() != null && vo.getDownTime() != "") {
			goods.setDownTime(string2Date(vo.getDownTime()));
		}
		if (vo.getOnSaleBeginTime() != null && vo.getOnSaleBeginTime() != "") {
			goods.setOnSaleBeginTime(string2Date(vo.getOnSaleBeginTime()));
		}
		if (vo.getOnSaleEndTime() != null && vo.getOnSaleEndTime() != "") {
			goods.setOnSaleEndTime(string2Date(vo.getOnSaleEndTime()));
		}

		// ------封装商品详细信息-----------
		GoodsDetail detail = new GoodsDetail();
		BeanUtils.copyProperties(vo, detail);
		detail.setGID(gid);
		if (vo.getProductionDate() != null && vo.getProductionDate() != "") {
			detail.setProductionDate(string2Date(vo.getProductionDate()));
		}

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
	 * 
	 * @param gid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/up/{gid}", method = RequestMethod.PUT)
	public Result<String> goodsUp(@PathVariable String gid) throws Exception {
		if (goodsOperationService.refreshGoodsUp(gid)) {
			return Result.ok("success");
		}
		return Result.fail("fail");

	}

	/**
	 * 商品下架
	 * 
	 * @param gid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/down/{gid}", method = RequestMethod.PUT)
	public Result<String> goodsDown(@PathVariable String gid) throws Exception {
		if (goodsOperationService.refreshGoodsDown(gid)) {
			return Result.ok("success");
		}
		return Result.fail("fail");

	}

	/**
	 * 查询所属的店铺
	 * 
	 * @param mid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/merchant/list", method = RequestMethod.GET)
	public Result<List<GoodsMerchantVO>> getmerchants() throws Exception {
		List<GoodsMerchantVO> list = goodsOperationService.listMerchant().stream().map(bo -> {
			GoodsMerchantVO vo = new GoodsMerchantVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}

	/**
	 * 根据MID 查询所属的品牌
	 * 
	 * @param mid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/brands/{mid}", method = RequestMethod.GET)
	public Result<List<GoodsBrandVO>> getBrandByMid(@PathVariable String mid) throws Exception {
		List<GoodsBrandVO> list = goodsOperationService.listBrandsByMid(mid).stream().map(bo -> {
			GoodsBrandVO vo = new GoodsBrandVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}

	/**
	 * 直接修改库存
	 * 
	 * @param gid
	 * @param count
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/modfiycount/{gid}/{count}", method = RequestMethod.PUT)
	public Result<String> modfiyCount(@PathVariable String gid, @PathVariable String count) throws Exception {
		if (goodsOperationService.refreshCount(gid, count)) {
			return Result.ok("success");
		}
		return Result.fail("fail");
	}

	@RequestMapping(value = "/checkcode/{code}", method = RequestMethod.GET)
	public Result<String> checkcode(@PathVariable String code) throws Exception {
		if (goodsOperationService.checkCode(code)) {
			return Result.ok("success");
		}
		return Result.fail("fail");
	}
}
