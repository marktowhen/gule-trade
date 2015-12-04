package com.jingyunbank.etrade.pay.handler.alipay;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.pay.bo.OrderPayment;
import com.jingyunbank.etrade.api.pay.handler.PayHandler;

@Service("ALIPAYHANDLER")
public class AlipayHandler implements PayHandler {

	@Override
	public Map<String, String> prepare(List<OrderPayment> payments, String bankCode) throws Exception {
		return null;
	}

}
