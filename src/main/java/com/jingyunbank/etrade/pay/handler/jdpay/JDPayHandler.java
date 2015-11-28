package com.jingyunbank.etrade.pay.handler.jdpay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jingyunbank.core.util.MD5;
import com.jingyunbank.etrade.api.pay.bo.OrderPayment;
import com.jingyunbank.etrade.api.pay.handler.PayHandler;

public class JDPayHandler implements PayHandler {

	@Override
	public Map<String, String> handle(List<OrderPayment> payments)
			throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		String vamount = payments.stream().map(x->x.getMoney()).reduce(new BigDecimal(0), (a, b)->a.add(b)).toString();
		String v_moneytype = "CNY";
		String v_oid = String.valueOf(payments.get(0).getExtransno());
		String v_mid = "1001";
		String v_url = "http://localhost:9000/#/order/success/oids";
		String key = "test";
		
		result.put("v_mid", v_mid);
		result.put("v_oid", v_oid);
		result.put("v_amount", vamount);
		result.put("v_moneytype", v_moneytype);
		result.put("v_url", v_url);
		result.put("pmode_id", "3080");
		result.put("v_md5info", MD5.digest(vamount+v_moneytype+v_oid+v_mid+v_url+key));
		result.put("remark2", v_url);
		
		result.put("payurl", "https://tmapi.jdpay.com/PayGate");
		return result;
	}

}
