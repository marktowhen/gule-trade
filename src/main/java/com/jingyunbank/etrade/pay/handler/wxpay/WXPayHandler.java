package com.jingyunbank.etrade.pay.handler.wxpay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.util.MD5;
import com.jingyunbank.etrade.api.pay.bo.OrderPayment;
import com.jingyunbank.etrade.api.pay.bo.PayPipeline;
import com.jingyunbank.etrade.api.pay.handler.IPayHandler;
import com.jingyunbank.etrade.api.pay.service.IPayPipelineService;

@Service(PayPipeline.WXPAYHANDLER)//该名称不可改！！！！
public class WXPayHandler implements IPayHandler {

	@Autowired
	private IPayPipelineService payPipelineService;
	
	private static PayPipeline pipeline = new PayPipeline();
	
	@Override
	public Map<String, String> prepare(List<OrderPayment> payments, String bankCode) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		result.put("appid", "");//公众账号ID
		result.put("mch_id", "");//商户号
		result.put("device_info", "WEB");
		result.put("nonce_str", "");//随机字符串，不长于32位
		result.put("body", "");//商品或支付单简要描述
		result.put("out_trade_no", String.valueOf(payments.get(0).getExtransno()));//商户系统内部的订单号
		result.put("fee_type", "CNY");//默认人民币：CNY
		result.put("total_fee", "1");//单位分
		result.put("spbill_create_ip", "");//APP和网页支付提交用户端ip
		result.put("notify_url", pipeline.getNoticeUrl());//接收微信支付异步通知回调地址
		result.put("trade_type", "NATIVE");//交易类型
		result.put("product_id", "");//trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
		
		String signkey = pipeline.getSignkey();
		result.put("sign", MD5.digest(compositeWXPayKeyValuePaires(result, signkey)).toUpperCase());
		
		HttpPost post = new HttpPost(pipeline.getPayUrl());
		
		//post.setEntity(new UrlEncodedFormEntity(result));
		HttpClient client = HttpClients.createDefault();
		HttpResponse response = client.execute(post);
		int statuscode = response.getStatusLine().getStatusCode();//should go 200
		System.out.println(statuscode);
		return result;
	}
	
	private String compositeWXPayKeyValuePaires(Map<String, String> result, String key) {
		StringBuilder builder = new StringBuilder();
		result.entrySet().stream().sorted((x, y)->x.getKey().compareToIgnoreCase(y.getKey())).forEach((x)->{
			builder.append(x.getKey()).append("=").append(x.getValue()).append("&");
		});
		builder.append("key=").append(key);
		return builder.toString();
	}

	@PostConstruct
	public void postprocessor(){
		pipeline = payPipelineService.single(PayPipeline.ALIPAY);
	}
}
