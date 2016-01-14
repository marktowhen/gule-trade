package com.jingyunbank.etrade.order.aspect;

import java.util.List;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jingyunbank.etrade.api.order.presale.bo.OrderLogistic;
import com.jingyunbank.etrade.api.order.presale.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderEventService;


@Component
@Scope("singleton")
@Aspect
public class OrderInterceptor {
	
	@Autowired
	private IOrderEventService orderEventService;
	
	private Logger logger = LoggerFactory.getLogger(OrderInterceptor.class);
	
	@AfterReturning("com.jingyunbank.etrade.order.aspect.Pointcuts.save() && args(orders)")
	public void whenSave(List<Orders> orders){
		logger.info("用户确认订单，订单信息："+ orders);
		orderEventService.broadcast(orders, IOrderEventService.MQ_ORDER_QUEUE_SAVE);
		if(orders.stream().anyMatch(x-> OrderStatusDesc.PAID_CODE.equals(x.getStatusCode()))){
			orderEventService.broadcast(orders, IOrderEventService.MQ_ORDER_QUEUE_PAYSUCC);
			logger.info("使用优惠券抵消全部金额，支付成功："+ orders);
		}
	}
	
	@AfterReturning("com.jingyunbank.etrade.order.aspect.Pointcuts.paysuccess() && args(extransno)")
	public void whenPaysuccess(String extransno){
		logger.info("用户支付成功， 支付对外订单号："+ extransno);
		orderEventService.broadcast(extransno, IOrderEventService.MQ_ORDER_QUEUE_PAYSUCC);
	}
	
	@AfterReturning("com.jingyunbank.etrade.order.aspect.Pointcuts.payfail() && args(extransno, note)")
	public void whenPayfail(String extransno, String note){
		logger.info("用户支付失败， 失败信息："+ extransno+"->"+note);
	}
	
	@AfterReturning("com.jingyunbank.etrade.order.aspect.Pointcuts.accept() && args(oids)")
	public void accept(List<String> oids){
		logger.info("卖家接受订单："+ String.join(",", oids));
	}
	
	@AfterReturning("com.jingyunbank.etrade.order.aspect.Pointcuts.dispatch() && args(logistic)")
	public void dispatch(OrderLogistic logistic){
		logger.info("卖家已发货："+ logistic);
	}
	
	@AfterReturning("com.jingyunbank.etrade.order.aspect.Pointcuts.received() && args(oids, note)")
	public void received(List<String> oids, String note){
		logger.info("用户确认收货："+ String.join(",", oids)+"->"+note);
	}
	
	@AfterReturning("com.jingyunbank.etrade.order.aspect.Pointcuts.cancel() && args(oids, reason)")
	public void cancel(List<String> oids, String reason){
		logger.info("用户取消订单："+ String.join(",", oids)+"->"+reason);
	}
	
	@AfterReturning("com.jingyunbank.etrade.order.aspect.Pointcuts.refund() && args(oid, ogid)")
	public void refund(String oid, String ogid){
		logger.info("用户申请退款："+ oid+"-> ogid："+ogid);
	}
	
	@AfterReturning("com.jingyunbank.etrade.order.aspect.Pointcuts.refundDone() && args(ogids)")
	public void refundDone(List<String> ogids){
		logger.info("退款申请完成："+ String.join(",", ogids));
	}
	
	@AfterReturning("com.jingyunbank.etrade.order.aspect.Pointcuts.cancelRefund() && args(oid, ogid)")
	public void cancelRefund(String oid, String ogid){
		logger.info("用户取消申请退款："+ oid+"-> ogid："+ogid);
	}
}
