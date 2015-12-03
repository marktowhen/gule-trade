package com.jingyunbank.etrade.pay.handler.fastpay;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.pay.bo.OrderPayment;
import com.jingyunbank.etrade.api.pay.handler.PayHandler;

@Service("FASTPAYHANDLER")
public class FastPayHandler implements PayHandler {

	@Override
	public Map<String, String> prepare(List<OrderPayment> payments)
			throws Exception {
		return null;
	}

}
