package com.jingyunbank.etrade.pay.handler.gatepay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.pay.bo.OrderPayment;
import com.jingyunbank.etrade.api.pay.handler.PayHandler;

@Service("BANKPAYHANDLER")
public class GatePayHandler implements PayHandler {
	
	@Override
	public Map<String, String> prepare(List<OrderPayment> payments)
			throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		String money = "0.01";//payments.stream().map(x->x.getMoney()).reduce(new BigDecimal(0), (a, b)->a.add(b)).toString();
		String v_moneytype = "CNY";
		String orderno = String.valueOf(payments.get(0).getExtransno());
		String v_mid = "1001";
		String notify_url = "http://localhost:9000/#/order/success/oids";
		String return_url = "http://localhost:9000/#/order/success/oids";
		String key = "test";
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		
		//required
		result.put("version", "1.0");
		result.put("oid_partner", "201408071000001545");
		result.put("user_id", money);
		result.put("timestamp", timestamp);
		result.put("sign_type", "MD5");
		result.put("sign", notify_url);
		result.put("busi_partner", "109001");//商户业务类型，实物：109001， 虚拟：101001
		result.put("no_order", orderno);//订单号
		result.put("dt_order", timestamp);
		result.put("name_goods", payments.get(0).getMname());
		result.put("money_order", money);
		result.put("notify_url", notify_url);
		
		//non required
		result.put("url_return", return_url);
		result.put("userreq_ip", "192.168.1.1");
		result.put("valid_order", "10080");
		result.put("risk_item", v_moneytype);
		result.put("info_order", "");//订单详情
		result.put("url_order", "");//订单详情地址
		
		result.put("payurl", "https://yintong.com.cn/payment/bankgateway.htm");
		
		return result;
	}

}
