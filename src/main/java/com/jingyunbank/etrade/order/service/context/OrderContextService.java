package com.jingyunbank.etrade.order.service.context;

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

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.util.UniqueSequence;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.bo.OrderLogistic;
import com.jingyunbank.etrade.api.order.bo.OrderGoods;
import com.jingyunbank.etrade.api.order.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.bo.OrderTrace;
import com.jingyunbank.etrade.api.order.bo.Orders;
import com.jingyunbank.etrade.api.order.bo.Refund;
import com.jingyunbank.etrade.api.order.service.ICartService;
import com.jingyunbank.etrade.api.order.service.IOrderGoodsService;
import com.jingyunbank.etrade.api.order.service.IOrderLogisticService;
import com.jingyunbank.etrade.api.order.service.IOrderService;
import com.jingyunbank.etrade.api.order.service.IOrderTraceService;
import com.jingyunbank.etrade.api.order.service.context.IOrderContextService;
import com.jingyunbank.etrade.api.pay.bo.OrderPayment;
import com.jingyunbank.etrade.api.pay.bo.PayType;
import com.jingyunbank.etrade.api.pay.service.IPayService;
import com.jingyunbank.etrade.api.pay.service.context.IPayContextService;

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
		}catch(Exception e){
			throw new DataSavingException(e);
		}
	}

	@Override
	public void update(Orders order) throws DataSavingException {
	}

	@Override
	@Transactional
	public void paysuccess(String extransno) throws DataRefreshingException, DataSavingException {
		List<Orders> orders = orderService.listByExtransno(extransno);
		List<String> oids = orders.stream().map(x->x.getID()).collect(Collectors.toList());
		//刷新订单状态
		orderService.refreshStatus(oids, OrderStatusDesc.PAID);
		//刷新支付记录状态
		payService.refreshStatus(extransno, true);
		List<OrderTrace> traces = new ArrayList<OrderTrace>();
		for (Orders order : orders) {
			createOrderTrace(order, OrderStatusDesc.PAID);
			traces.addAll(order.getTraces());
		}
		//刷新订单商品的状态
		orderGoodsService.refreshStatus(oids, OrderStatusDesc.PAID);
		//保存订单状态追踪信息
		orderTraceService.save(traces);
	}

	@Override
	public void payfail(String extransno) throws DataRefreshingException, DataSavingException {
		List<Orders> orders = orderService.listByExtransno(extransno);
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
	}

	@Override
	public boolean accept(List<String> oids) throws DataRefreshingException, DataSavingException {
		List<Orders> orders = orderService.list(oids);
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
		createOrderTrace(order, OrderStatusDesc.ACCEPT);
		traces.addAll(order.getTraces());
		orderTraceService.save(traces);
		//刷新订单商品的状态
		orderGoodsService.refreshStatus(Arrays.asList(oid), OrderStatusDesc.ACCEPT);
		return true;
	}

	@Override
	public void received(String orderno) {

	}

	@Override
	public void cancel(String orderno, String reason) {

	}

	@Override
	public void remove(String id) throws DataRemovingException {
		orderService.remove(id);
	}

	@Override
	public void refund(Refund refund) {

	}

	@Override
	public void denyRefund(Refund refund) {

	}

	@Override
	public void approveRefund(Refund refund) {

	}

	@Override
	public boolean canRefund(String oid) {
		return false;
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
			op.setMoney(order.getPrice());
			op.setTransno(UniqueSequence.next());
			payments.add(op);
		}
	}

}
