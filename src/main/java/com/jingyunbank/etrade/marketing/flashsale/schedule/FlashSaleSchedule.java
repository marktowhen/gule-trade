package com.jingyunbank.etrade.marketing.flashsale.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jingyunbank.etrade.api.marketing.flashsale.service.context.IFlashSaleTimeOutService;

@Component
public class FlashSaleSchedule {
	@Autowired 
	private IFlashSaleTimeOutService  flashSaleTimeOutService;
	
	//逾期未支付 关闭订单状态
		@Scheduled(fixedDelay=(60*1000))
		public void payTimeout() {
			flashSaleTimeOutService.payTimeout();
		}
}
