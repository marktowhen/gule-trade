package com.jingyunbank.etrade.pay.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.pay.handler.PayHandler;
import com.jingyunbank.etrade.api.pay.handler.PayHandlerResolver;

@Service("payHandlerResolver")
public class InternPayHandlerResolver implements PayHandlerResolver {
	
	@Autowired
	private Map<String, PayHandler> handlers = new HashMap<String, PayHandler>();
	
	@Override
	public PayHandler resolve(String platformcode) throws IllegalArgumentException {
		PayHandler handler = handlers.get((platformcode+"Handler").toUpperCase());
		if(handler == null) 
			throw new IllegalArgumentException("不合法支付平台码！");
		
		return handler;
	}

}
