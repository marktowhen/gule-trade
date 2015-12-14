package com.jingyunbank.etrade.order.schedule;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

	@Scheduled(fixedDelay=(60000*5))//5 minutes
	public void checkOrderStatus(){
		System.out.println("are you kiding me?"+new Date()+"++++++++++++++++++++++++++++++");
	}
}
