package com.jingyunbank.etrade.order.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.postsale.service.context.IRefundRobotService;

@Component
public class RefundScheduledTasks {
	@Autowired
	private IRefundRobotService refundRobotService;
	
	//自动接受买家的退款退货申请
	@Scheduled(fixedDelay=(10*60*1000))//10 minutes
	public void autoAccept() throws DataRefreshingException, DataSavingException{
		refundRobotService.autoAccept();
	}
	
	//自动完成买家的退货申请。
	@Scheduled(fixedDelay=(10*60*1000))//10 minutes
	public void autoReceive() throws DataRefreshingException, DataSavingException{
		refundRobotService.autoDone();
	}
}
