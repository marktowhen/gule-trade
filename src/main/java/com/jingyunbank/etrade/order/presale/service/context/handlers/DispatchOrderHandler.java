package com.jingyunbank.etrade.order.presale.service.context.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.order.presale.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.presale.bo.OrderTrace;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.order.presale.service.IOrderGoodsService;
import com.jingyunbank.etrade.api.order.presale.service.IOrderLogisticService;
import com.jingyunbank.etrade.api.order.presale.service.IOrderService;
import com.jingyunbank.etrade.api.order.presale.service.IOrderTraceService;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderStatusHandler;

public class DispatchOrderHandler implements IOrderStatusHandler {

	@Autowired
	private IOrderService orderService;
	@Autowired
	private IOrderGoodsService orderGoodsService;
	@Autowired
	private IOrderTraceService orderTraceService;
	@Autowired
	private IOrderLogisticService orderLogisticService;

	@Override
	public Result<String> handle(List<Orders> orders) throws Exception {
		//String oid = logistic.getOID();
		//Optional<Orders> candidateorder = orderService.single(oid);
		if(orders.size() == 0){
			return Result.fail("orders is empty.");
		}
		if(orders.stream().anyMatch(order -> !OrderStatusDesc.ACCEPT_CODE.equals(order.getStatusCode()))){
			return Result.fail("illegal state.");
		}
		if(orders.stream().anyMatch(x->Objects.isNull(x.getLogistic()))){
			return Result.fail("null logistic.");
		}
		orders.forEach(x->{
			try {
				orderLogisticService.save(x.getLogistic());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		List<String> oids = orders.stream().map(x->x.getID()).collect(Collectors.toList());
		//刷新订单状态
		orderService.refreshStatus(oids, OrderStatusDesc.DELIVERED);
		List<OrderTrace> traces = new ArrayList<OrderTrace>();
		for (Orders order : orders) {
			order.setStatusCode(OrderStatusDesc.DELIVERED_CODE);
			order.setStatusName(OrderStatusDesc.DELIVERED.getName());
			traces.add(createOrderTrace(order, "卖家已发货"));
			
		}
		//刷新订单商品的状态
		orderGoodsService.refreshStatus(oids, OrderStatusDesc.DELIVERED);
		//保存订单状态追踪信息
		orderTraceService.save(traces);
		
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
