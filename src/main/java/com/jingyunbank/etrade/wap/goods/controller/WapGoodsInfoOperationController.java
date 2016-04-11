package com.jingyunbank.etrade.wap.goods.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
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
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsInfo;
import com.jingyunbank.etrade.api.wap.goods.service.IWapGoodsInfoOperationService;
import com.jingyunbank.etrade.api.wap.goods.service.IWapGoodsService;
import com.jingyunbank.etrade.wap.goods.bean.GoodsInfoVO;

/**
 * 
 * Title: WapGoodsInfoOperationController 商品info
 * 
 * @author duanxf
 * @date 2016年4月11日
 */
@RestController
@RequestMapping("/api/goods/operation/info")
public class WapGoodsInfoOperationController {
	@Autowired
	private IWapGoodsInfoOperationService wapGoodsOperationService;

	@Autowired
	private IWapGoodsService wapGoodsService;

	/**
	 * 增加商品info
	 * 
	 * @param request
	 * @param goodsInfoVOList
	 * @param valid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Result<String> saveGoodsInfo(HttpServletRequest request,
			@RequestBody @Valid List<GoodsInfoVO> goodsInfoVOList, BindingResult valid) throws Exception {
		if (valid.hasErrors()) {
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream().map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
					.collect(Collectors.joining(" ; ")));
		}

		for (GoodsInfoVO info : goodsInfoVOList) {
			info.setID(KeyGen.uuid());
			GoodsInfo infoBO = new GoodsInfo();
			BeanUtils.copyProperties(info, infoBO);
			wapGoodsOperationService.saveGoodsInfo(infoBO);
		}
		return Result.ok("success");
	}

	/**
	 * 获取产品参数 by gid
	 * 
	 * @param gid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/all/{gid}", method = RequestMethod.GET)
	public Result<List<GoodsInfoVO>> getGoodsInfo(@PathVariable String gid) throws Exception {
		List<GoodsInfoVO> list = wapGoodsService.listGoodsInfo(gid).stream().map(bo -> {
			GoodsInfoVO vo = new GoodsInfoVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());

		return Result.ok(list);
	}

	/**
	 * 修改goods info 
	 * @param request
	 * @param goodsInfoVOList
	 * @param gid
	 * @param valid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/update/{gid}", method = RequestMethod.POST)
	public Result<String> updateGoodsInfo(HttpServletRequest request,
			@RequestBody @Valid List<GoodsInfoVO> goodsInfoVOList, @PathVariable String gid, BindingResult valid)
					throws Exception {
		if (valid.hasErrors()) {
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream().map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
					.collect(Collectors.joining(" ; ")));
		}
		
		//delete all info 
		wapGoodsOperationService.removeGoodsInfo(gid);
		//insert info
		for (GoodsInfoVO info : goodsInfoVOList) {
			info.setID(KeyGen.uuid());
			info.setGID(gid);
			GoodsInfo infoBO = new GoodsInfo();
			BeanUtils.copyProperties(info, infoBO);
			wapGoodsOperationService.saveGoodsInfo(infoBO);
		}

		return Result.ok("success");
	}

	/**
	 * 删除商品info by id
	 * 
	 * @param request
	 * @param infoId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete/{infoId}", method = RequestMethod.PUT)
	public Result<String> deleteGoodsInfo(HttpServletRequest request, @PathVariable String infoId) throws Exception {
		wapGoodsOperationService.removeGoodsInfoById(infoId);
		return Result.ok("success");
	}
}
