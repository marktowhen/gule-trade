package com.jingyunbank.etrade.asyn.service.context;

import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.asyn.bo.AsynSchedule;
import com.jingyunbank.etrade.api.asyn.service.context.IAsynRunService;

@Service("salseRegesterService")
public class SalesRegisterService implements IAsynRunService {

	@Override
	public void run(AsynSchedule schedule) {
		System.out.println("salseRegesterService----------run");
	}

}
