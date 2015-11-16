package com.jingyunbank.etrade.pay.handler.alipay;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jingyunbank.etrade.api.pay.bo.OrderPayment;
import com.jingyunbank.etrade.api.pay.handler.PayHandler;

public class AlipayHandler implements PayHandler {

	@Override
	public void handle(List<OrderPayment> payments, HttpServletRequest request, 
					HttpServletResponse response) throws Exception {
		
	}

}
