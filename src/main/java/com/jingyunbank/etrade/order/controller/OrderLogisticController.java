package com.jingyunbank.etrade.order.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.etrade.api.order.bo.OrderLogistic;
import com.jingyunbank.etrade.api.order.service.context.IOrderContextService;
import com.jingyunbank.etrade.order.bean.OrderLogisticVO;

@RestController
public class OrderLogisticController {
	
	@Autowired
	private IOrderContextService orderContextService;
	
	/**
	 * 接受用户成功支付后的订单信息
	 * @param oids
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/api/orders/accept", method=RequestMethod.PUT)
	public Result<String> accept(@NotNull @Size(min=1) @RequestBody List<String> oids, BindingResult valid) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的订单信息有误！");
		}
		if(!orderContextService.accept(oids)){
			return Result.fail("您提交的订单信息有误，请检查后重新尝试！");
		}
		return Result.ok();
	}
	
	@AuthBeforeOperation
	@RequestMapping(value="/api/orders/logistic", method=RequestMethod.PUT)
	public Result<String> dispatch(@Valid @RequestBody OrderLogisticVO logisticvo, BindingResult valid) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的物流信息有误！");
		}
		OrderLogistic logistic = new OrderLogistic();
		BeanUtils.copyProperties(logisticvo, logistic);
		logistic.setAddtime(new Date());
		logistic.setID(KeyGen.uuid());
		if(!orderContextService.dispatch(logistic)){
			return Result.fail("您提交的订单信息有误，请检查后重新尝试！");
		}
		return Result.ok();
	}
	
}
