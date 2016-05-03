package com.jingyunbank.etrade.order.presale.service.context.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.order.presale.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.presale.bo.OrderTrace;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.order.presale.service.IOrderGoodsService;
import com.jingyunbank.etrade.api.order.presale.service.IOrderService;
import com.jingyunbank.etrade.api.order.presale.service.IOrderTraceService;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderStatusHandler;
import com.jingyunbank.etrade.api.vip.coupon.handler.ICouponStrategyResolver;

public class CancelOrderHandler implements IOrderStatusHandler {

	@Autowired
	private IOrderService orderService;
	@Autowired
	private IOrderGoodsService orderGoodsService;
	@Autowired
	private IOrderTraceService orderTraceService;
	@Autowired
	private ICouponStrategyResolver couponStrategyResolver;

	@Override
	public Result<String> handle(List<Orders> orders) throws Exception {
		//List<Orders> orders = orderService.list(oids);
		if(orders.size() == 0){
			return Result.fail("orders is empty.");
		}
		if(orders.stream().anyMatch(order -> !OrderStatusDesc.NEW_CODE.equals(order.getStatusCode()))){
			return Result.fail("illegal state.");
		}
		
		List<String> oids = orders.stream().map(x -> x.getID())
				.collect(Collectors.toList());
		
		orderService.refreshStatus(oids, OrderStatusDesc.CLOSED);
		List<OrderTrace> traces = new ArrayList<OrderTrace>();
		for (Orders order : orders) {
			order.setStatusCode(OrderStatusDesc.CLOSED_CODE);
			order.setStatusName(OrderStatusDesc.CLOSED.getName());
			traces.add(createOrderTrace(order, ""));
		}
		//set note reason
		traces.forEach(trace->trace.setNote(""));
		orderTraceService.save(traces);
		//刷新订单商品的状态
		orderGoodsService.refreshStatus(oids, OrderStatusDesc.CLOSED);
		//更新优惠卡券状态
		List<String> temp = new ArrayList<String>(); 
		for (Orders order : orders) {
			if(StringUtils.hasText(order.getCouponID()) && StringUtils.hasText(order.getCouponType())){
				if(temp.contains(order.getCouponID())) continue;
				temp.add(order.getCouponID());
				if(!orderService.shareCoupon(order.getCouponID())){
					couponStrategyResolver.resolve(order.getCouponType())
								.unlock(order.getUID(), order.getCouponID());
				}
			}
		}
		return Result.ok();
	}

	// 创建订单新建追踪状态
	private OrderTrace createOrderTrace(Orders order, String note) {
		OrderTrace trace = new OrderTrace();
		trace.setAddtime(new Date());
		trace.setID(KeyGen.uuid());
		trace.setOID(order.getID());
		trace.setOrderno(order.getOrderno());
		trace.setOperator(order.getUID());
		trace.setStatusCode(order.getStatusCode());
		trace.setStatusName(order.getStatusName());
		trace.setNote(note);
		return trace;
	}

	@Override
	public Result<String> handle(Orders order) throws Exception {
		return null;
	}

}
