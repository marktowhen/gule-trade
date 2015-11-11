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
import com.jingyunbank.etrade.api.exception.OrderDeliveringException;
import com.jingyunbank.etrade.api.exception.OrderPaidException;
import com.jingyunbank.etrade.api.exception.OrderPayException;
import com.jingyunbank.etrade.api.exception.OrderPayFailException;
import com.jingyunbank.etrade.api.exception.OrderUpdateException;
import com.jingyunbank.etrade.api.order.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.bo.OrderTrace;
import com.jingyunbank.etrade.api.order.bo.Orders;
import com.jingyunbank.etrade.api.order.bo.Refund;
import com.jingyunbank.etrade.api.order.service.IOrderGoodsService;
import com.jingyunbank.etrade.api.order.service.IOrderService;
import com.jingyunbank.etrade.api.order.service.IOrderTraceService;
import com.jingyunbank.etrade.api.order.service.context.IOrderContextService;

@Service("orderContextService")
public class OrderContextService implements IOrderContextService {

	@Autowired
	private IOrderService orderService;
	@Autowired
	private IOrderGoodsService orderGoodsService;
	@Autowired
	private IOrderTraceService orderTraceService;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void save(Orders order) throws DataSavingException {
		try{
			List<Orders> orders = new ArrayList<Orders>();
			orders.add(order);
			orderService.save(orders);
			orderGoodsService.save(order.getGoods());
			initNewOrderTrace(order);
			orderTraceService.save(order.getTraces());
		}catch(Exception e){
			throw new DataSavingException(e);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void save(List<Orders> orders) throws DataSavingException {
		try{
			orderService.save(orders);
			for (Orders order : orders) {
				orderGoodsService.save(order.getGoods());
				initNewOrderTrace(order);
				orderTraceService.save(order.getTraces());
			}
		}catch(Exception e){
			throw new DataSavingException(e);
		}
	}

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
	public void update(Orders order) throws OrderUpdateException {

	}

	@Override
	public void pay(Orders order) throws OrderPayException {

	}

	@Override
	public void paid(String orderno) throws OrderPaidException {

	}

	@Override
	public void payfail(String orderno) throws OrderPayFailException {

	}

	@Override
	public void delivering(String orderno) throws OrderDeliveringException {

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
