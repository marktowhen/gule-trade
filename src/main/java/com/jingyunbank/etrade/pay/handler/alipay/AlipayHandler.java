package com.jingyunbank.etrade.pay.handler.alipay;

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

@Service(PayPipeline.ALIPAYHANDLER)//该名称不可改！！！！
public class AlipayHandler implements IPayHandler {

	@Autowired
	private IPayPipelineService payPipelineService;
	
	private static PayPipeline pipeline = new PayPipeline();
	
	@Override
	public Map<String, String> prepare(List<OrderPayment> payments, String bankCode) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		result.put("service", "create_direct_pay_by_user");
		//合作身份者ID，以2088开头由16位纯数字组成的字符串
        result.put("partner", pipeline.getPartner());
        // 收款支付宝账号，一般情况下收款账号就是签约账号
        result.put("seller_email", "devops@jingyunbank.com");
        result.put("_input_charset", "utf-8");
        //支付类型,1 商品购买
		result.put("payment_type", "1");
		
		//必填，不能修改 //服务器异步通知页面路径
		//需http://格式的完整路径，不能加?id=123这类自定义参数
		result.put("notify_url", pipeline.getNoticeUrl());
		//页面跳转同步通知页面路径
		//需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/
		result.put("return_url", pipeline.getReturnUrl());
		//商户订单号
		result.put("out_trade_no", String.valueOf(payments.get(0).getExtransno()));
		//订单名称//必填
		result.put("subject", payments.get(0).getMname());
		//付款金额//必填
		result.put("total_fee", "0.01");
		//订单描述
		//result.put("body", "");
		//商品展示地址
		//result.put("show_url", "");
		//防钓鱼时间戳//若要使用请调用类文件submit中的query_timestamp函数
		//result.put("anti_phishing_key", "");
		//非局域网的外网IP地址，如：221.0.0.1
		//result.put("exter_invoke_ip", "");
		
		String signkey = pipeline.getSignkey();
		
		result.put("sign", MD5.digest(compositeAlipayKeyValuePaires(result, signkey)));
		result.put("sign_type", pipeline.getSigntype().toUpperCase());
		
		result.put("payurl", pipeline.getPayUrl());
		
		return result;
	}

	private String compositeAlipayKeyValuePaires(Map<String, String> result, String key) {
		StringBuilder builder = new StringBuilder();
		result.entrySet().stream().sorted((x, y)->x.getKey().compareToIgnoreCase(y.getKey())).forEach((x)->{
			builder.append(x.getKey()).append("=").append(x.getValue()).append("&");
		});
		builder.delete(builder.length()-1, builder.length());
		builder.append(key);
		return builder.toString();
	}

	@PostConstruct
	public void postprocessor(){
		pipeline = payPipelineService.single(PayPipeline.ALIPAY);
	}

}
