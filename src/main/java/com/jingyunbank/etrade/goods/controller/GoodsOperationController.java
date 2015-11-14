package com.jingyunbank.etrade.goods.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.spi.LoggerFactory;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.goods.bo.Goods;
import com.jingyunbank.etrade.api.goods.bo.GoodsDetail;
import com.jingyunbank.etrade.api.goods.bo.GoodsImg;
import com.jingyunbank.etrade.api.goods.bo.GoodsOperation;
import com.jingyunbank.etrade.api.goods.service.IGoodsOperationService;
import com.jingyunbank.etrade.base.util.SystemConfigProperties;
import com.jingyunbank.etrade.goods.bean.GoodsOperationVO;
import com.jingyunbank.etrade.user.bean.AddressVO;

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
	private Logger logger = org.slf4j.LoggerFactory.getLogger(GoodsOperationController.class);
	@Resource
	private IGoodsOperationService goodsOperationService;

	/**
	 * 保存商品
	 * 
	 * @param request
	 * @param vo
	 * @param valid
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.PUT)
	//@RequestParam MultipartFile[] myfiles
	public Result saveGoods(HttpServletRequest request,
			@RequestBody @Valid GoodsOperationVO vo, BindingResult valid) {
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
		goods.setVolume(0); //销量
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
			return Result.ok("添加商品成功!");
		} else {
			return Result.fail("添加商品失败!");
		}

	}

}
