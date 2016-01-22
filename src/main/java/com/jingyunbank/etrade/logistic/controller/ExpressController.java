package com.jingyunbank.etrade.logistic.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.logistic.bo.KDNShow;
import com.jingyunbank.etrade.api.logistic.service.IExpressDeliveryService;
import com.jingyunbank.etrade.api.logistic.service.IExpressService;
import com.jingyunbank.etrade.goods.bean.GoodsOperationShowVO;
import com.jingyunbank.etrade.logistic.bean.ExpressVO;
import com.jingyunbank.etrade.logistic.bean.KDNShowVO;

/**
 * 
 * Title: 快递controller
 * 
 * @author duanxf
 * @date 2016年1月21日
 */
@RestController
@RequestMapping("/api/express")
public class ExpressController {

	@Autowired
	private IExpressService expressService;

	@Autowired
	private IExpressDeliveryService kdnService;

	@RequestMapping(value = "/info/{oid}/{code}/{codeid}", method = RequestMethod.GET)
	public Result<KDNShowVO> getExpressInfo(@PathVariable String oid, @PathVariable String code,
			@PathVariable String codeid) throws Exception {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("OrderCode", oid);
		map.put("ShipperCode", code);
		map.put("LogisticCode", codeid);
		KDNShowVO showVO = null;
		Optional<KDNShow> bo = kdnService.getRemoteExpress(map);
		if (Objects.nonNull(bo)) {
			showVO = new KDNShowVO();
			BeanUtils.copyProperties(bo.get(), showVO);
		}
		return Result.ok(showVO);
	}

	/**
	 * 查询可用的快递类型
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<List<ExpressVO>> queryAllExpress(HttpServletRequest request) throws Exception {
		List<ExpressVO> expressList = expressService.listExpress().stream().map(bo -> {
			ExpressVO vo = new ExpressVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(expressList);
	}
	
}
