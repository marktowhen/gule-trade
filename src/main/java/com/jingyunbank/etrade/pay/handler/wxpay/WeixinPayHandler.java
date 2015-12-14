package com.jingyunbank.etrade.pay.handler.wxpay;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.pay.bo.OrderPayment;
import com.jingyunbank.etrade.api.pay.bo.PayPipeline;
import com.jingyunbank.etrade.api.pay.handler.IPayHandler;

@Service(PayPipeline.WXPAYHANDLER)//该名称不可改！！！！
public class WeixinPayHandler implements IPayHandler {

	@Override
	public Map<String, String> prepare(List<OrderPayment> payments, String bankCode) throws Exception {
		return null;
	}

}
