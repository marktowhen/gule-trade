package com.jingyunbank.etrade.wap.goods.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.etrade.api.wap.goods.service.IWapGoodsOperationService;

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
	private IWapGoodsOperationService wapGoodsOperationService;

	
/*	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Result<String> saveGoodsInfo(HttpServletRequest request,
			@RequestBody @Valid List<GoodsInfoVO> goodsInfoVOList, BindingResult valid) throws Exception {
		if (valid.hasErrors()) {
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream().map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
					.collect(Collectors.joining(" ; ")));
		}
		return Result.ok("success");
	}*/
}
