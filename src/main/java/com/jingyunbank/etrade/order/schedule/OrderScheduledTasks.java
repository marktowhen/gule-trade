package com.jingyunbank.etrade.order.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.exception.NoticeDispatchException;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderRobotService;

@Component
public class OrderScheduledTasks {

	@Autowired
	private IOrderRobotService orderRobotService;
	
	//关闭过期的未付款的订单
	@Scheduled(fixedDelay=(5*60*1000))//5 minutes
	public void closeExpiredUnpaidOrder() throws DataRefreshingException, DataSavingException{
		orderRobotService.closeUnpaid();
	}
	
	@Scheduled(fixedDelay=(30*60*1000))//30 minutes
	public void remindBeforeAutoReceive() throws NoticeDispatchException {
		orderRobotService.remindReceive();
	}
	
	//关闭过期的未付款的订单
	@Scheduled(fixedDelay=(30*60*1000))//30 minutes
	public void autoReceive() throws DataRefreshingException, DataSavingException{
		orderRobotService.autoReceive();
	}
	
}
