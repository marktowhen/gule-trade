package com.jingyunbank.etrade.order.presale.service.context;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.util.UniqueSequence;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.presale.bo.OrderGoods;
import com.jingyunbank.etrade.api.order.presale.bo.OrderLogistic;
import com.jingyunbank.etrade.api.order.presale.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.presale.bo.OrderTrace;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.order.presale.service.IOrderGoodsService;
import com.jingyunbank.etrade.api.order.presale.service.IOrderLogisticService;
import com.jingyunbank.etrade.api.order.presale.service.IOrderService;
import com.jingyunbank.etrade.api.order.presale.service.IOrderTraceService;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderContextService;
import com.jingyunbank.etrade.api.pay.bo.OrderPayment;
import com.jingyunbank.etrade.api.pay.bo.PayType;
import com.jingyunbank.etrade.api.pay.service.IPayService;
import com.jingyunbank.etrade.api.pay.service.context.IPayContextService;
import com.jingyunbank.etrade.api.vip.coupon.handler.ICouponStrategyResolver;

@Service("orderContextService")
public class OrderContextService implements IOrderContextService {

	@Autowired
	private IOrderService orderService;
	@Autowired
	private IOrderGoodsService orderGoodsService;
	@Autowired
	private IOrderTraceService orderTraceService;
	@Autowired
	private IPayContextService payContextService;
	@Autowired
	private IPayService payService;
	@Autowired
	private IOrderLogisticService orderLogisticService;
	@Autowired
	private ICouponStrategyResolver couponStrategyResolver;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor={DataSavingException.class, DataRefreshingException.class})
	public void save(List<Orders> orders) throws DataSavingException, DataRefreshingException {
		//如果订单支付金额为0则将状态设为已支付
		refreshOrderStatusBasedOnOrderPayout(orders);
		//保存订单信息
		orderService.save(orders);
		//构建详情跟追踪状态
		List<OrderGoods> goods = new ArrayList<OrderGoods>();
		List<OrderTrace> traces = new ArrayList<OrderTrace>();
		List<OrderPayment> payments = new ArrayList<OrderPayment>();
		for (Orders order : orders) {
			goods.addAll(order.getGoods());
			traces.add(createOrderTrace(order, "用户创建订单。"));
			createPayment(order, payments);
		}
		//保存订单的详情（每笔订单的商品信息）
		orderGoodsService.save(goods);
		//保存订单状态追踪信息
		orderTraceService.save(traces);
		//保存订单的支付信息
		payService.save(payments);
		//冻结优惠卡
		List<String> temp = new ArrayList<String>();
		for (Orders order : orders) {
			if(StringUtils.hasText(order.getCouponID()) && StringUtils.hasText(order.getCouponType())){
				if(temp.contains(order.getCouponID())) continue;
				temp.add(order.getCouponID());
				try {
					couponStrategyResolver.resolve(order.getCouponType())
									.lock(order.getUID(), order.getCouponID());
				} catch (IllegalArgumentException | DataRefreshingException e) {
					throw new DataRefreshingException(e);
				}
			}
		}
	}

	private void refreshOrderStatusBasedOnOrderPayout(List<Orders> orders) {
		BigDecimal total = orders.stream().map(x->x.getPayout()).reduce(BigDecimal.ZERO, (x,y)->x.add(y));
		if(BigDecimal.ZERO.compareTo(total) == 0){
			for (Orders order : orders) {
				order.setStatusCode(OrderStatusDesc.PAID.getCode());
				order.setStatusName(OrderStatusDesc.PAID.getName());
				List<OrderGoods> goods = order.getGoods();
				for (OrderGoods orderGoods : goods) {
					orderGoods.setStatusCode(order.getStatusCode());
					orderGoods.setStatusName(order.getStatusName());
				}
			}
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor={DataSavingException.class, DataRefreshingException.class})
	public void paysuccess(String extransno) throws DataRefreshingException, DataSavingException {
		List<Orders> orders = orderService.listByExtransno(extransno);
		if(orders.size() == 0){
			return;
		}
		
		List<String> oids = orders.stream().map(x->x.getID()).collect(Collectors.toList());
		//刷新订单状态
		orderService.refreshStatus(oids, OrderStatusDesc.PAID);
		//刷新支付记录状态
		payService.refreshStatus(extransno, true);
		List<OrderTrace> traces = new ArrayList<OrderTrace>();
		List<OrderGoods> goods = new ArrayList<OrderGoods>();
		for (Orders order : orders) {
			order.setStatusCode(OrderStatusDesc.PAID_CODE);
			order.setStatusName(OrderStatusDesc.PAID.getName());
			traces.add(createOrderTrace(order, "用户支付成功"));
			goods.addAll(order.getGoods());
		}
		//刷新订单商品的状态
		orderGoodsService.refreshStatus(oids, OrderStatusDesc.PAID);
		//保存订单状态追踪信息
		orderTraceService.save(traces);
	}

	@Override
	@Transactional
	public void payfail(String extransno, String note) throws DataRefreshingException, DataSavingException {
		List<Orders> orders = orderService.listByExtransno(extransno);
		if(orders.size() == 0){
			return;
		}
		List<String> oids = orders.stream().map(x->x.getID()).collect(Collectors.toList());
		//刷新订单状态
		orderService.refreshStatus(oids, OrderStatusDesc.PAYFAIL);
		//刷新支付记录状态
		payService.refreshStatus(extransno, false);
		List<OrderTrace> traces = new ArrayList<OrderTrace>();
		for (Orders order : orders) {
			order.setStatusCode(OrderStatusDesc.PAYFAIL_CODE);
			order.setStatusName(OrderStatusDesc.PAYFAIL.getName());
			traces.add(createOrderTrace(order, note));
		}
		//刷新订单商品的状态
		orderGoodsService.refreshStatus(oids, OrderStatusDesc.PAYFAIL);
		//保存订单状态追踪信息
		orderTraceService.save(traces);
		List<String> temp = new ArrayList<String>();
		for (Orders order : orders) {
			if(StringUtils.hasText(order.getCouponID()) && StringUtils.hasText(order.getCouponType())){
				if(temp.contains(order.getCouponID())) continue;
				temp.add(order.getCouponID());
				couponStrategyResolver.resolve(order.getCouponType())
								.unlock(order.getUID(), order.getCouponID());
			}
		}
	}

	@Override
	@Transactional
	public boolean accept(List<String> oids) throws DataRefreshingException, DataSavingException {
		List<Orders> orders = orderService.list(oids);
		if(orders.size() == 0){
			return false;
		}
		if(orders.stream().anyMatch(order -> !OrderStatusDesc.PAID_CODE.equals(order.getStatusCode()))){
			return false;
		}
		orderService.refreshStatus(oids, OrderStatusDesc.ACCEPT);
		List<OrderTrace> traces = new ArrayList<OrderTrace>();
		for (Orders order : orders) {
			order.setStatusCode(OrderStatusDesc.ACCEPT_CODE);
			order.setStatusName(OrderStatusDesc.ACCEPT.getName());
			traces.add(createOrderTrace(order, "卖家接受订单，商品即将出库。"));
		}
		orderTraceService.save(traces);
		//刷新订单商品的状态
		orderGoodsService.refreshStatus(oids, OrderStatusDesc.ACCEPT);
		return true;
	}
	
	@Override
	@Transactional
	public boolean dispatch(OrderLogistic logistic) throws DataRefreshingException, DataSavingException {
		String oid = logistic.getOID();
		Optional<Orders> candidateorder = orderService.single(oid);
		if(!candidateorder.isPresent()){
			return false;
		}
		Orders order = candidateorder.get();
		if(!OrderStatusDesc.ACCEPT_CODE.equals(order.getStatusCode())){
			return false;
		}
		orderLogisticService.save(logistic);
		orderService.refreshStatus(Arrays.asList(oid), OrderStatusDesc.DELIVERED);
		List<OrderTrace> traces = new ArrayList<OrderTrace>();
		order.setStatusCode(OrderStatusDesc.DELIVERED_CODE);
		order.setStatusName(OrderStatusDesc.DELIVERED.getName());
		traces.add(createOrderTrace(order, "卖家已发货"));
		orderTraceService.save(traces);
		//刷新订单商品的状态
		orderGoodsService.refreshStatus(Arrays.asList(oid), OrderStatusDesc.DELIVERED);
		return true;
	}

	@Override
	@Transactional
	public boolean received(List<String> oids, String note) throws DataRefreshingException, DataSavingException{
		List<Orders> orders = orderService.list(oids);
		if(orders.size() == 0){
			return false;
		}
		if(orders.stream().anyMatch(order -> !OrderStatusDesc.DELIVERED_CODE.equals(order.getStatusCode()))){
			return false;
		}
		orderService.refreshStatus(oids, OrderStatusDesc.RECEIVED);
		List<OrderTrace> traces = new ArrayList<OrderTrace>();
		for (Orders order : orders) {
			order.setStatusCode(OrderStatusDesc.RECEIVED_CODE);
			order.setStatusName(OrderStatusDesc.RECEIVED.getName());
			traces.add(createOrderTrace(order, note));
		}
		traces.forEach(trace->trace.setNote(note));
		orderTraceService.save(traces);
		//刷新订单商品的状态
		orderGoodsService.refreshStatus(oids, OrderStatusDesc.RECEIVED);
		
		return true;
	}

	@Override
	@Transactional
	public boolean cancel(List<String> oids, String reason) throws DataRefreshingException, DataSavingException{
		List<Orders> orders = orderService.list(oids);
		if(orders.size() == 0){
			return false;
		}
		if(orders.stream().anyMatch(order -> !OrderStatusDesc.NEW_CODE.equals(order.getStatusCode()))){
			return false;
		}
		
		orderService.refreshStatus(oids, OrderStatusDesc.CLOSED);
		List<OrderTrace> traces = new ArrayList<OrderTrace>();
		for (Orders order : orders) {
			order.setStatusCode(OrderStatusDesc.CLOSED_CODE);
			order.setStatusName(OrderStatusDesc.CLOSED.getName());
			traces.add(createOrderTrace(order, reason));
		}
		//set note reason
		traces.forEach(trace->trace.setNote(reason));
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
		return true;
	}

	@Override
	public boolean remove(String oid) throws DataRefreshingException, DataSavingException {
		Optional<Orders> candidateorder = orderService.single(oid);
		if(!candidateorder.isPresent()){
			return false;
		}
		Orders order = candidateorder.get();
		if(!OrderStatusDesc.CLOSED_CODE.equals(order.getStatusCode())){
			return false;
		}
		
		orderService.refreshStatus(Arrays.asList(oid), OrderStatusDesc.REMOVED);
		List<OrderTrace> traces = new ArrayList<OrderTrace>();
		order.setStatusCode(OrderStatusDesc.REMOVED_CODE);
		order.setStatusName(OrderStatusDesc.REMOVED.getName());
		traces.add(createOrderTrace(order, "买家移除订单。"));
		//set note reason
		orderTraceService.save(traces);
		//刷新订单商品的状态
		orderGoodsService.refreshStatus(Arrays.asList(oid), OrderStatusDesc.REMOVED);
		return true;
	}

	//创建订单新建追踪状态
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
	private void createPayment(Orders order, List<OrderPayment> payments){
		if(PayType.ONLINE_CODE.equals(order.getPaytypeCode())){
			OrderPayment op = new OrderPayment();
			BeanUtils.copyProperties(order, op);
			op.setAddtime(new Date());
			op.setDone(false);
			op.setID(KeyGen.uuid());
			op.setOID(order.getID());
			op.setMoney(order.getPayout());
			op.setTransno(UniqueSequence.next());
			payments.add(op);
		}
	}

	@Override
	public boolean refund(String oid, String ogid)
			throws DataRefreshingException, DataSavingException {
		orderGoodsService.refreshGoodStatus(Arrays.asList(ogid), OrderStatusDesc.REFUNDING);
		return true;
	}

	@Override
	public boolean cancelRefund(String oid, String ogid)
			throws DataRefreshingException, DataSavingException {
		Optional<Orders> candidate = orderService.single(oid);
		if(candidate.isPresent()){
			Orders o = candidate.get();
			orderGoodsService.refreshGoodStatus(Arrays.asList(ogid), OrderStatusDesc.resolve(o.getStatusCode()));
			return true;
		}
		return false;
	}

	@Override
	public void refundDone(List<String> ogids)
			throws DataRefreshingException, DataSavingException {
		orderGoodsService.refreshGoodStatus(ogids, OrderStatusDesc.REFUNDED);
	}
}
