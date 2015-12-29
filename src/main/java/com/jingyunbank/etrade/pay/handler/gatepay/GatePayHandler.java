package com.jingyunbank.etrade.pay.handler.gatepay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.util.MD5;
import com.jingyunbank.etrade.api.pay.bo.OrderPayment;
import com.jingyunbank.etrade.api.pay.bo.PayPipeline;
import com.jingyunbank.etrade.api.pay.handler.IPayHandler;
import com.jingyunbank.etrade.api.pay.service.IPayPipelineService;

@Service(PayPipeline.GATEPAYHANDLER)//该名称不可改！！！！
public class GatePayHandler implements IPayHandler {
	
	@Autowired
	private IPayPipelineService payPipelineService;
	
	private static PayPipeline pipeline = new PayPipeline();
	
	@Override
	public Map<String, String> prepare(List<OrderPayment> payments, String bankCode) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		String money = "0.01";//payments.stream().map(x->x.getMoney()).reduce(new BigDecimal(0), (a, b)->a.add(b)).toString();
		String orderno = String.valueOf(payments.get(0).getExtransno());
		String notify_url = pipeline.getNoticeUrl();
		String return_url = pipeline.getReturnUrl();
		String key = pipeline.getSignkey();
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		
		//required
		result.put("version", "1.0");
		result.put("oid_partner", pipeline.getPartner());
		result.put("user_id", payments.get(0).getUID());
		result.put("timestamp", timestamp);
		result.put("sign_type", pipeline.getSigntype().toUpperCase());
		result.put("busi_partner", "109001");//商户业务类型，实物：109001， 虚拟：101001
		result.put("no_order", orderno);//订单号
		result.put("dt_order", timestamp);
		result.put("name_goods", payments.get(0).getMname());
		result.put("money_order", money);
		result.put("notify_url", notify_url);

		//non required
		result.put("bank_code", bankCode);
		//result.put("pay_type", "8");支付类型，1表示借记卡，8表示信用卡，不写表示都支持
		result.put("url_return", return_url);
		
		result.put("sign", MD5.digest(compositeGatewayKeyValuePaires(result, key)));
		//result.put("userreq_ip", "192.168.1.1");
		//result.put("valid_order", "10080");
		//result.put("risk_item", "");
		//result.put("info_order", "");//订单详情
		//result.put("url_order", "");//订单详情地址
		
		result.put("payurl", pipeline.getPayUrl());
		
		return result;
	}

	private String compositeGatewayKeyValuePaires(Map<String, String> result, String key) {
		StringBuilder builder = new StringBuilder();
		result.entrySet().stream().sorted((x, y)->x.getKey().compareToIgnoreCase(y.getKey())).forEach((x)->{
			builder.append(x.getKey()).append("=").append(x.getValue()).append("&");
		});
		builder.append("key=").append(key);
		return builder.toString();
	}

	@PostConstruct
	public void postprocessor(){
		pipeline = payPipelineService.single(PayPipeline.GATEPAY);
	}

}
