package com.jingyunbank.etrade.pay.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.etrade.api.pay.bo.OrderPayment;
import com.jingyunbank.etrade.api.pay.service.IPayService;
import com.jingyunbank.etrade.api.pay.service.PayHandler;
import com.jingyunbank.etrade.api.pay.service.PayHandlerResolver;
import com.jingyunbank.etrade.api.pay.service.context.IPayPlatformService;
import com.jingyunbank.etrade.pay.bean.OrderPaymentRequestVO;
import com.jingyunbank.etrade.pay.bean.OrderPaymentVO;

@RestController
public class PayController {

	@Autowired
	private IPayService payService;
	@Autowired
	private IPayPlatformService payPlatformService;
	
	/**
	 * 初始化订单支付接口。<p>
	 * 查询出制定订单的支付状态信息
	 * <p>
	 * uri: put /api/pay/init[oid, oid, oid]
	 * <p>
	 * <b>调用时机：</b><p>
	 * <ul>
	 * 	<li>1，订单确认页，用户点击确认提交订单后，当订单信息保存完成后，将返回以保存的订单信息，并将请求重定向到该接口进行支付请求的操作。
	 *  <li>2，我的订单页，用户选中未支付的订单，并点击立即支付按钮时。
	 * </ul>
	 * <b>注：</b><p>
	 * 该操作会检查是否创建了交易记录（order_payment），如果没有则新建，如果已存在则查询出来返回.
	 * @param payvo
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/pay/init", method=RequestMethod.GET,
					consumes="application/json;charset=UTF-8",
					produces="application/json;charset=UTF-8")
	@AuthBeforeOperation
	public Result init(@Valid @RequestBody List<String> oids, BindingResult valid, HttpSession session) throws Exception{
		
		if(valid.hasErrors()|| oids.size() == 0){
			return Result.fail("您提交的订单信息有误，请核实后进行支付。");
		}
		
		List<OrderPayment> payments = payService.listPayments(oids)
							.stream().filter(x-> !x.isDone()).collect(Collectors.toList());
		
		if(Objects.isNull(payments) || payments.size() == 0 || payments.size() != oids.size()){
			return Result.fail("订单已经失效或者已完成支付，无法完成本次支付，请重新下单。");
		}
		List<OrderPaymentVO> opvs = new ArrayList<OrderPaymentVO>();
		payments.forEach(p -> {
			OrderPaymentVO opv = new OrderPaymentVO();
			BeanUtils.copyProperties(p, opvs);
			opvs.add(opv);
		});
		return Result.ok(opvs);
	}
	
	/**
	 * 获取用户提交的订单支付方式
	 * @param payvo
	 * @param valid
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/pay/redirect", method=RequestMethod.POST,
			consumes="application/json;charset=UTF-8",
			produces="application/json;charset=UTF-8")
	@AuthBeforeOperation
	public Result redirect(@Valid @RequestBody OrderPaymentRequestVO payvo, BindingResult valid,  HttpSession session) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的订单信息有误，请核实正确后支付。");
		}
		String platformCode = payvo.getPlatformCode();
		
		PayHandlerResolver resolver = null;
		PayHandler handler = resolver.resolve(platformCode);
		handler.handle(null, payvo.getPayments().stream().map(x->{return new OrderPayment();}).collect(Collectors.toList()));
		return Result.ok(platformCode);
	}
}
