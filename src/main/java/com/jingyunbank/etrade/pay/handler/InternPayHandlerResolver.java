package com.jingyunbank.etrade.pay.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.pay.handler.PayHandler;
import com.jingyunbank.etrade.api.pay.handler.PayHandlerResolver;
import com.jingyunbank.etrade.pay.handler.alipay.AlipayHandler;
import com.jingyunbank.etrade.pay.handler.llpay.LianlianPayHandler;

@Service("payHandlerResolver")
public class InternPayHandlerResolver implements PayHandlerResolver {

	private static Map<String, PayHandler> handlers = new HashMap<String, PayHandler>();
	static{
		handlers.put("LLPAY", new LianlianPayHandler());
		handlers.put("ALIPAY", new AlipayHandler());
	}
	
	@Override
	public PayHandler resolve(String platformcode) throws IllegalArgumentException {
		PayHandler handler = handlers.get(platformcode);
		if(handler == null) 
			throw new IllegalArgumentException("不合法支付平台码！");
		
		return handler;
	}

}
