package com.jingyunbank.etrade.order.presale.service.context.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

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
import com.jingyunbank.etrade.api.weixinMessage.service.IWxMessageService;

public class AcceptOrderHandler implements IOrderStatusHandler {

	@Autowired
	private IOrderService orderService;
	@Autowired
	private IOrderGoodsService orderGoodsService;
	@Autowired
	private IOrderTraceService orderTraceService;
	@Autowired
	private IWxMessageService wxMessageService;
	@Autowired
	private ICouponStrategyResolver couponStrategyResolver;

	@Override
	public Result<String> handle(List<Orders> orders) throws Exception {
		// List<Orders> orders = orderService.list(oids);
		if (orders.size() == 0) {
			return Result.fail("orders is empty.");
		}
		if (orders.stream().anyMatch(
				order -> !OrderStatusDesc.PAID_CODE.equals(order
						.getStatusCode()))) {
			return Result.fail("orders not paid.");
		}
		List<String> oids = orders.stream().map(x -> x.getID())
				.collect(Collectors.toList());
		orderService.refreshStatus(oids, OrderStatusDesc.ACCEPT);
		List<OrderTrace> traces = new ArrayList<OrderTrace>();
		for (Orders order : orders) {
			order.setStatusCode(OrderStatusDesc.ACCEPT_CODE);
			order.setStatusName(OrderStatusDesc.ACCEPT.getName());
			traces.add(createOrderTrace(order, "卖家接受订单，商品即将出库。"));
		}
		orderTraceService.save(traces);
		// 刷新订单商品的状态
		orderGoodsService.refreshStatus(oids, OrderStatusDesc.ACCEPT);
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
