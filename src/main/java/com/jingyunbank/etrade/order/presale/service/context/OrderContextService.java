package com.jingyunbank.etrade.order.presale.service.context;

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
import com.jingyunbank.etrade.api.cart.service.ICartService;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.goods.service.IGoodsOperationService;
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
	private ICartService cartService;
	@Autowired
	private IOrderLogisticService orderLogisticService;
	@Autowired
	private IGoodsOperationService goodsOperationService;
	@Autowired
	private ICouponStrategyResolver couponStrategyResolver;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void save(Orders order) throws DataSavingException {
		try{
			List<Orders> orders = new ArrayList<Orders>();
			orders.add(order);
			//保存订单信息
			orderService.save(orders);
			//保存订单支付状态
			List<OrderPayment> payments = new ArrayList<OrderPayment>();
			createPayment(order, payments);
			payService.save(payments);
			//保存订单的详情（每笔订单的商品信息）
			orderGoodsService.save(order.getGoods());
			
			createOrderTrace(order, OrderStatusDesc.NEW);
			//保存订单状态追踪信息
			orderTraceService.save(order.getTraces());
			//将下订单的商品从购物车中删除掉
			cartService.remove(order.getGoods().stream().map(x->x.getGID()).collect(Collectors.toList()), order.getUID());
		}catch(Exception e){
			throw new DataSavingException(e);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void save(List<Orders> orders) throws DataSavingException {
		try{
			//保存订单信息
			orderService.save(orders);
			//构建详情跟追踪状态
			List<OrderGoods> goods = new ArrayList<OrderGoods>();
			List<OrderTrace> traces = new ArrayList<OrderTrace>();
			List<OrderPayment> payments = new ArrayList<OrderPayment>();
			for (Orders order : orders) {
				goods.addAll(order.getGoods());
				createOrderTrace(order, OrderStatusDesc.NEW);
				traces.addAll(order.getTraces());
				createPayment(order, payments);
			}
			//保存订单的详情（每笔订单的商品信息）
			orderGoodsService.save(goods);
			//保存订单状态追踪信息
			orderTraceService.save(traces);
			//保存订单的支付信息
			payService.save(payments);
			//将下订单的商品从购物车中删除掉
			cartService.remove(goods.stream().map(x->x.getGID()).collect(Collectors.toList()), orders.get(0).getUID());
			//冻结优惠卡
			Orders fo = orders.get(0);
			if(StringUtils.hasText(fo.getCouponID()) && StringUtils.hasText(fo.getCouponType())){
				couponStrategyResolver.resolve(fo.getCouponType())
								.lock(fo.getUID(), fo.getCouponID());
			}
		}catch(Exception e){
			throw new DataSavingException(e);
		}
	}

	@Override
	@Transactional
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
			createOrderTrace(order, OrderStatusDesc.PAID);
			traces.addAll(order.getTraces());
			goods.addAll(order.getGoods());
		}
		//刷新订单商品的状态
		orderGoodsService.refreshStatus(oids, OrderStatusDesc.PAID);
		//保存订单状态追踪信息
		orderTraceService.save(traces);
		//更新优惠卡券状态
		List<String> consumedcouponids = orders.stream().map(x->x.getCouponID()).collect(Collectors.toList()); 
		for (Orders order : orders) {
			if(StringUtils.hasText(order.getCouponID()) && StringUtils.hasText(order.getCouponType())){
				if(consumedcouponids.contains(order.getCouponID())) continue;
				consumedcouponids.add(order.getCouponID());
				couponStrategyResolver.resolve(order.getCouponType())
								.consume(order.getUID(), order.getCouponID());
			}
		}
		Orders o = orders.get(0);
		String uid = o.getUID();
		String uname = o.getUname();
		//更新库存
		for (OrderGoods gs : goods) {
			goodsOperationService.refreshGoodsVolume(uid, uname, gs.getGID(), gs.getCount());
		}
	}

	@Override
	@Transactional
	public void payfail(String extransno) throws DataRefreshingException, DataSavingException {
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
			createOrderTrace(order, OrderStatusDesc.PAYFAIL);
			traces.addAll(order.getTraces());
		}
		//刷新订单商品的状态
		orderGoodsService.refreshStatus(oids, OrderStatusDesc.PAYFAIL);
		//保存订单状态追踪信息
		orderTraceService.save(traces);
		Orders fo = orders.get(0);
		if(StringUtils.hasText(fo.getCouponID()) && StringUtils.hasText(fo.getCouponType())){
			couponStrategyResolver.resolve(fo.getCouponType())
							.unlock(fo.getUID(), fo.getCouponID());
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
			createOrderTrace(order, OrderStatusDesc.ACCEPT);
			traces.addAll(order.getTraces());
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
		createOrderTrace(order, OrderStatusDesc.DELIVERED);
		traces.addAll(order.getTraces());
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
			createOrderTrace(order, OrderStatusDesc.RECEIVED);
			traces.addAll(order.getTraces());
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
			createOrderTrace(order, OrderStatusDesc.CLOSED);
			traces.addAll(order.getTraces());
		}
		//set note reason
		traces.forEach(trace->trace.setNote(reason));
		orderTraceService.save(traces);
		//刷新订单商品的状态
		orderGoodsService.refreshStatus(oids, OrderStatusDesc.CLOSED);
		//更新优惠卡券状态
		List<String> consumedcouponids = orders.stream().map(x->x.getCouponID()).collect(Collectors.toList()); 
		for (Orders order : orders) {
			if(StringUtils.hasText(order.getCouponID()) && StringUtils.hasText(order.getCouponType())){
				if(consumedcouponids.contains(order.getCouponID())) continue;
				consumedcouponids.add(order.getCouponID());
				couponStrategyResolver.resolve(order.getCouponType())
								.unlock(order.getUID(), order.getCouponID());
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
		createOrderTrace(order, OrderStatusDesc.REMOVED);
		traces.addAll(order.getTraces());
		//set note reason
		orderTraceService.save(traces);
		//刷新订单商品的状态
		orderGoodsService.refreshStatus(Arrays.asList(oid), OrderStatusDesc.REMOVED);
		return true;
	}

	//创建订单新建追踪状态
	private void createOrderTrace(Orders order, OrderStatusDesc status) {
		OrderTrace trace = new OrderTrace();
		trace.setAddtime(new Date());
		trace.setID(KeyGen.uuid());
		trace.setOID(order.getID());
		trace.setOrderno(order.getOrderno());
		trace.setOperator(order.getUID());
		trace.setStatusCode(status.getCode());
		trace.setStatusName(status.getName());
		trace.setNote(status.getDescription());
		order.getTraces().add(trace);
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
		orderGoodsService.refreshGoodStatus(ogid, OrderStatusDesc.REFUNDING);
		return true;
	}

	@Override
	public boolean cancelRefund(String oid, String ogid)
			throws DataRefreshingException, DataSavingException {
		Optional<Orders> candidate = orderService.single(oid);
		if(candidate.isPresent()){
			Orders o = candidate.get();
			orderGoodsService.refreshGoodStatus(ogid, OrderStatusDesc.resolve(o.getStatusCode()));
			return true;
		}
		return false;
	}

	@Override
	public void refundDone(String oid, String ogid)
			throws DataRefreshingException, DataSavingException {
		orderGoodsService.refreshGoodStatus(ogid, OrderStatusDesc.REFUNDED);
	}
}
