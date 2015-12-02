package com.jingyunbank.etrade.pay.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.pay.bo.PayPlatform;
import com.jingyunbank.etrade.api.pay.bo.PayType;
import com.jingyunbank.etrade.api.pay.service.IPayPlatformService;
import com.jingyunbank.etrade.api.pay.service.IPayTypeService;
import com.jingyunbank.etrade.pay.bean.PayPlatformVO;
import com.jingyunbank.etrade.pay.bean.PayTypeVO;

@RestController
public class PayPlatformController {

	@Autowired
	private IPayPlatformService payPlatformService;
	@Autowired
	private IPayTypeService payTypeService;
	
	@RequestMapping(value="/api/pay/platform/list", method=RequestMethod.GET)
	public Result<List<PayPlatformVO>> list() throws Exception{
		List<PayPlatform> platforms = payPlatformService.list();
		List<PayPlatformVO> vos = platforms.stream().map(bo->{
			PayPlatformVO vo = new PayPlatformVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		
		return Result.ok(vos);
	}
	
	@RequestMapping(value="/api/pay/type/list", method=RequestMethod.GET)
	public Result<List<PayTypeVO>> listType() throws Exception{
		List<PayType> types = payTypeService.list();
		List<PayTypeVO> vos = types.stream().map(bo->{
			PayTypeVO vo = new PayTypeVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		
		return Result.ok(vos);
	}
}
