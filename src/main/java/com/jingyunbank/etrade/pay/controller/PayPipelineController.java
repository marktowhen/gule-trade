package com.jingyunbank.etrade.pay.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.pay.bo.PayPipeline;
import com.jingyunbank.etrade.api.pay.bo.PayType;
import com.jingyunbank.etrade.api.pay.service.IPayPipelineService;
import com.jingyunbank.etrade.api.pay.service.IPayTypeService;
import com.jingyunbank.etrade.pay.bean.PayPipelineVO;
import com.jingyunbank.etrade.pay.bean.PayTypeVO;

@RestController
public class PayPipelineController {

	@Autowired
	private IPayPipelineService payPipelineService;
	@Autowired
	private IPayTypeService payTypeService;
	
	@RequestMapping(value="/api/pay/pipeline/list", method=RequestMethod.GET)
	public Result<List<PayPipelineVO>> list() throws Exception{
		List<PayPipeline> pipelines = payPipelineService.list();
		List<PayPipelineVO> vos = pipelines.stream().map(bo->{
			PayPipelineVO vo = new PayPipelineVO();
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
