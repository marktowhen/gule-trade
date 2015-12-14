package com.jingyunbank.etrade.pay.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.pay.handler.IPayHandler;
import com.jingyunbank.etrade.api.pay.handler.IPayHandlerResolver;

@Service("payHandlerResolver")
public class InternPayHandlerResolver implements IPayHandlerResolver {
	
	@Autowired
	private Map<String, IPayHandler> handlers = new HashMap<String, IPayHandler>();
	
	@Override
	public IPayHandler resolve(String pipelinecode) throws IllegalArgumentException {
		IPayHandler handler = handlers.get((pipelinecode+"Handler").toUpperCase());
		if(handler == null) 
			throw new IllegalArgumentException("不合法支付平台码！");
		
		return handler;
	}

}
