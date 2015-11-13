package com.jingyunbank.etrade.goods.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.goods.bo.Goods;
import com.jingyunbank.etrade.api.goods.service.IGoodsOperationService;
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
	public Result saveGoods(HttpServletRequest request, @Valid GoodsOperationVO vo, BindingResult valid) {
		// 异常信息
		if (valid.hasErrors()) {
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream().map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
					.collect(Collectors.joining(" ; ")));
		}
		//------封装商品信息---------------
		
		//------封装商品详细信息-----------

		//------封装商品图片信息-----------

		return Result.ok();
	}
}
