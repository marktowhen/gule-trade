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
import com.jingyunbank.etrade.api.weixinMessage.service.WxMessageService;

public class PayfailOrderHandler implements IOrderStatusHandler {

	@Autowired
	private IOrderService orderService;
	@Autowired
	private IOrderGoodsService orderGoodsService;
	@Autowired
	private IOrderTraceService orderTraceService;
	@Autowired
	private WxMessageService wxMessageService;
	@Autowired
	private ICouponStrategyResolver couponStrategyResolver;

	@Override
	public Result<String> handle(List<Orders> orders) throws Exception {
		// List<Orders> orders = orderService.listByExtransno(extransno);
		if (orders.size() == 0) {
			return Result.fail("orders is empty.");
		}
		List<String> oids = orders.stream().map(x -> x.getID())
				.collect(Collectors.toList());
		// 刷新订单状态
		orderService.refreshStatus(oids, OrderStatusDesc.PAYFAIL);
		// 刷新支付记录状态
		// payService.refreshStatus(extransno, false);
		List<OrderTrace> traces = new ArrayList<OrderTrace>();
		for (Orders order : orders) {
			order.setStatusCode(OrderStatusDesc.PAYFAIL_CODE);
			order.setStatusName(OrderStatusDesc.PAYFAIL.getName());
			traces.add(createOrderTrace(order, ""));

		}
		// 刷新订单商品的状态
		orderGoodsService.refreshStatus(oids, OrderStatusDesc.PAYFAIL);
		// 保存订单状态追踪信息
		orderTraceService.save(traces);
		List<String> temp = new ArrayList<String>();
		for (Orders order : orders) {
			if (StringUtils.hasText(order.getCouponID())
					&& StringUtils.hasText(order.getCouponType())) {
				if (temp.contains(order.getCouponID()))
					continue;
				temp.add(order.getCouponID());
				couponStrategyResolver.resolve(order.getCouponType()).unlock(
						order.getUID(), order.getCouponID());
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
