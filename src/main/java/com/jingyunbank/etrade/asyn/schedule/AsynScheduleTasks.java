package com.jingyunbank.etrade.asyn.schedule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.asyn.bo.AsynSchedule;
import com.jingyunbank.etrade.api.asyn.service.IAsynScheduleService;
import com.jingyunbank.etrade.api.asyn.service.context.IAsynRunService;
import com.jingyunbank.etrade.asyn.util.SpringContextUtils;

@Component
public class AsynScheduleTasks {

	@Autowired
	private IAsynScheduleService asynScheduleService;
	
	@Scheduled(fixedDelay=(1*60*1000))//1 minute
	public void runAsynSchedule(){
		Range range = new Range(0, 10);
		List<AsynSchedule> list = asynScheduleService.list(range);
		if(list!=null && !list.isEmpty()){
			for (AsynSchedule asynSchedule : list) {
				IAsynRunService service = (IAsynRunService)SpringContextUtils.getBeanById(asynSchedule.getServiceName());
				service.run(asynSchedule);
			}
		}
	}
	
}
