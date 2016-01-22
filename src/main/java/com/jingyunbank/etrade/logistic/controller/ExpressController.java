package com.jingyunbank.etrade.logistic.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.logistic.service.IExpressService;
import com.jingyunbank.etrade.api.logistic.service.ILogisticService;
import com.jingyunbank.etrade.logistic.bean.ExpressVO;
import com.jingyunbank.etrade.logistic.bean.LogisticDataVO;

/**
 * 
 * Title: 快递controller
 * 
 * @author duanxf
 * @date 2016年1月21日
 */
@RestController
@RequestMapping("/api/logistic")
public class ExpressController {

	@Autowired
	private IExpressService expressService;

	@Autowired
	private ILogisticService kdnService;

	@RequestMapping(value = "/express/info/{oid}/{code}/{codeid}", method = RequestMethod.GET)
	public Result<List<LogisticDataVO>> getExpressInfo(@PathVariable String oid, @PathVariable String code,
			@PathVariable String codeid) throws Exception {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("OrderCode", oid);
		map.put("ShipperCode", code);
		map.put("LogisticCode", codeid);
		List<LogisticDataVO> list = kdnService.getRemoteExpress(map).stream().map(bo -> {
			LogisticDataVO vo = new LogisticDataVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}

	/**
	 * 查询可用的快递类型
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/express/list", method = RequestMethod.GET)
	public Result<List<ExpressVO>> queryAllExpress(HttpServletRequest request) throws Exception {
		List<ExpressVO> expressList = expressService.listExpress().stream().map(bo -> {
			ExpressVO vo = new ExpressVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(expressList);
	}

}
