package com.jingyunbank.etrade.pay.controller;

import java.util.List;
import java.util.stream.Collectors;



import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.pay.service.IPayBankService;
import com.jingyunbank.etrade.pay.bean.PayBankVO;

@RestController
public class PayBankController {

	@Autowired
	private IPayBankService payBankService;
	
	@RequestMapping(value="/api/pay/bank/gateway/list", method=RequestMethod.GET)
	public Result<List<PayBankVO>> listGateWayBank() throws Exception{
		
		return Result.ok(payBankService.listGateWayBanks().stream()
				.map(bo->{
					PayBankVO vo = new PayBankVO();
					BeanUtils.copyProperties(bo, vo);
					return vo;
				}).collect(Collectors.toList()));
	}
	
	@RequestMapping(value="/api/pay/bank/fastway/list", method=RequestMethod.GET)
	public Result<List<PayBankVO>> listFastWayBank() throws Exception{
		
		return Result.ok(payBankService.listFastWayBanks().stream()
				.map(bo->{
					PayBankVO vo = new PayBankVO();
					BeanUtils.copyProperties(bo, vo);
					return vo;
				}).collect(Collectors.toList()));
	}
	
	@RequestMapping(value="/api/pay/bank/gateway/list/{from}/{size}", method=RequestMethod.GET)
	public Result<List<PayBankVO>> listGateWayBank(@PathVariable("from") long from, @PathVariable("size") long size) throws Exception{
		
		return Result.ok(payBankService.listGateWayBanks(from, from+size).stream()
				.map(bo->{
					PayBankVO vo = new PayBankVO();
					BeanUtils.copyProperties(bo, vo);
					return vo;
				}).collect(Collectors.toList()));
	}
	
	@RequestMapping(value="/api/pay/bank/fastway/list/{from}/{size}", method=RequestMethod.GET)
	public Result<List<PayBankVO>> listFastWayBank(@PathVariable("from") long from, @PathVariable("size") long size) throws Exception{
		
		return Result.ok(payBankService.listFastWayBanks(from, from+size).stream()
				.map(bo->{
					PayBankVO vo = new PayBankVO();
					BeanUtils.copyProperties(bo, vo);
					return vo;
				}).collect(Collectors.toList()));
	}
}
