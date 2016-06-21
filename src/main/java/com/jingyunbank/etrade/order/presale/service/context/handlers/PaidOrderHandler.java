package com.jingyunbank.etrade.order.presale.service.context.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.order.presale.bo.OrderGoods;
import com.jingyunbank.etrade.api.order.presale.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.presale.bo.OrderTrace;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.order.presale.service.IOrderGoodsService;
import com.jingyunbank.etrade.api.order.presale.service.IOrderService;
import com.jingyunbank.etrade.api.order.presale.service.IOrderTraceService;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderStatusHandler;
import com.jingyunbank.etrade.api.weixinMessage.service.IWxMessageService;
import com.jingyunbank.etrade.weixinMessage.util.wx.WxConstants;

public class PaidOrderHandler implements IOrderStatusHandler {

	@Autowired
	private IOrderService orderService;
	@Autowired
	private IOrderGoodsService orderGoodsService;
	@Autowired
	private IOrderTraceService orderTraceService;
	@Autowired
	private IWxMessageService wxMessageService;
	
	@Override
	public Result<String> handle(List<Orders> orders) throws Exception {
		//List<Orders> orders = orderService.listByExtransno(extransno);
		orders = orders.stream()
				.filter(x-> OrderStatusDesc.NEW_CODE.equals(x.getStatusCode()))
				.collect(Collectors.toList());
		if(orders.size() == 0){
			return Result.fail("orders is empty.");
		}
		
		List<String> oids = orders.stream().map(x->x.getID()).collect(Collectors.toList());
		//刷新订单状态
		orderService.refreshStatus(oids, OrderStatusDesc.PAID);
		//刷新支付记录状态
		//payService.refreshStatus(extransno, true);
		List<OrderTrace> traces = new ArrayList<OrderTrace>();
		List<OrderGoods> goods = new ArrayList<OrderGoods>();
		for (Orders order : orders) {
			order.setStatusCode(OrderStatusDesc.PAID_CODE);
			order.setStatusName(OrderStatusDesc.PAID.getName());
			traces.add(createOrderTrace(order, "用户支付成功"));
			//用户支付成功信息推送到用户
			Map<String,String> dataMap=new HashMap<String,String>();
			dataMap.put("first", "尊敬的顾客您好，我们已收到您的付款。");
			dataMap.put("orderMoneySum",order.getPayout()+"");
			dataMap.put("orderProductName",order.getOrderno()+"");
			dataMap.put("Remark", "如有问题请致电400-800-8895或直接在微信留言，我们将第一时间为您服务！");
			wxMessageService.sendMessageToUser(WxConstants.getString(WxConstants.PAYMENT_SUCCESS), order.getUID(), dataMap);
			
			goods.addAll(order.getGoods());
		}
		//刷新订单商品的状态
		orderGoodsService.refreshStatus(oids, OrderStatusDesc.PAID);
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
