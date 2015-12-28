package com.jingyunbank.etrade.order.presale.service.context;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.presale.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.order.presale.service.IOrderService;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderContextService;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderRobotService;

@Service("orderRobotService")
public class OrderRobotService implements IOrderRobotService {

	//支付超时期限24hr
	public static final long UNPAID_EXPIRE_DURATION_IN_SECOND = 24 * 60 * 60;//24hr
	//确认收货超时期限10d
	public static final long UNRECEIVED_EXPIRE_DURATION_IN_SECOND = 10 *  24 * 60 * 60;//10d
	
	@Autowired
	private IOrderContextService orderContextService;
	@Autowired
	private IOrderService orderService;
	
	@Override
	@Transactional
	public void closeUnpaid() throws DataRefreshingException, DataSavingException {
		System.out.println("===================未支付订单超时关闭任务启动======================");
		Instant now = Instant.now();
		Instant deadline = now.minusSeconds(UNPAID_EXPIRE_DURATION_IN_SECOND);
		List<Orders> unpaid = orderService.listBefore(Date.from(deadline), OrderStatusDesc.NEW);
		List<String> oids = unpaid.stream().map(x->x.getID()).collect(Collectors.toList());
		System.out.println("待关闭订单："+oids);
		if(oids.size() > 0) orderContextService.cancel(oids, "超时关闭");
		System.out.println("===================未支付订单超时关闭任务完成======================");
	}

	@Override
	@Transactional
	public void autoReceive() throws DataRefreshingException, DataSavingException {
		System.out.println("===================未收货订单超时关闭任务启动======================");
		Instant now = Instant.now();
		Instant deadline = now.minusSeconds(UNRECEIVED_EXPIRE_DURATION_IN_SECOND);
		List<Orders> unpaid = orderService.listBefore(Date.from(deadline), OrderStatusDesc.DELIVERED);
		List<String> oids = unpaid.stream().map(x->x.getID()).collect(Collectors.toList());
		System.out.println("待收货订单："+oids);
		if(oids.size() > 0) orderContextService.cancel(oids, "超时自动确认收货");
		System.out.println("===================未收货订单超时关闭任务完成======================");
	}

}
