package com.jingyunbank.etrade.pay.handler.llpay;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jingyunbank.etrade.api.pay.bo.OrderPayment;
import com.jingyunbank.etrade.api.pay.handler.PayHandler;

public class LianlianPayHandler implements PayHandler {

	@Override
	public void handle(List<OrderPayment> payments, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		response.sendRedirect("https://yintong.com.cn/payment/bankgateway.htm");
	}

}
