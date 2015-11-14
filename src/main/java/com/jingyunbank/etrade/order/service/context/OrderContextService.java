package com.jingyunbank.etrade.order.service.context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.bo.OrderGoods;
import com.jingyunbank.etrade.api.order.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.bo.OrderTrace;
import com.jingyunbank.etrade.api.order.bo.Orders;
import com.jingyunbank.etrade.api.order.bo.Refund;
import com.jingyunbank.etrade.api.order.service.IOrderGoodsService;
import com.jingyunbank.etrade.api.order.service.IOrderService;
import com.jingyunbank.etrade.api.order.service.IOrderTraceService;
import com.jingyunbank.etrade.api.order.service.context.IOrderContextService;
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
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void save(Orders order) throws DataSavingException {
		try{
			List<Orders> orders = new ArrayList<Orders>();
			orders.add(order);
			//保存订单信息
			orderService.save(orders);
			//保存订单支付状态
			payContextService.save(orders);
			//保存订单的详情（每笔订单的商品信息）
			orderGoodsService.save(order.getGoods());
			initNewOrderTrace(order);
			//保存订单状态追踪信息
			orderTraceService.save(order.getTraces());
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
			//保存订单支付状态
			payContextService.save(orders);
			//构建详情跟追踪状态
			List<OrderGoods> goods = new ArrayList<OrderGoods>();
			List<OrderTrace> traces = new ArrayList<OrderTrace>();
			for (Orders order : orders) {
				goods.addAll(order.getGoods());
				initNewOrderTrace(order);
				traces.addAll(order.getTraces());
			}
			//保存订单的详情（每笔订单的商品信息）
			orderGoodsService.save(goods);
			//保存订单状态追踪信息
			orderTraceService.save(traces);
			
		}catch(Exception e){
			throw new DataSavingException(e);
		}
	}

	//创建订单新建追踪状态
	private void initNewOrderTrace(Orders order) {
		OrderTrace trace = new OrderTrace();
		trace.setAddtime(new Date());
		trace.setID(KeyGen.uuid());
		trace.setOID(order.getID());
		trace.setOrderno(order.getOrderno());
		trace.setOperator(order.getUID());
		trace.setStatusCode(OrderStatusDesc.NEW_CODE);
		trace.setStatusName(OrderStatusDesc.NEW.getName());
		trace.setNote(OrderStatusDesc.NEW.getDesc());
		order.getTraces().add(trace);
	}

	@Override
	public void update(Orders order) throws DataSavingException {

	}

	@Override
	public void pay(Orders order) throws DataSavingException {

	}

	@Override
	public void paid(String orderno) throws DataSavingException {

	}

	@Override
	public void payfail(String orderno) throws DataSavingException {

	}

	@Override
	public void delivering(String orderno) throws DataSavingException {

	}

	@Override
	public void delivered(String orderno) {

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

}
