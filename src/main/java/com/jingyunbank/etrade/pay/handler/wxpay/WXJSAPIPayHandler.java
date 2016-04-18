package com.jingyunbank.etrade.pay.handler.wxpay;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.KeyStore;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.xml.bind.JAXBContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.util.MD5;
import com.jingyunbank.core.util.RndBuilder;
import com.jingyunbank.core.util.UniqueSequence;
import com.jingyunbank.etrade.api.pay.bo.OrderPayment;
import com.jingyunbank.etrade.api.pay.bo.PayPipeline;
import com.jingyunbank.etrade.api.pay.handler.IPayHandler;
import com.jingyunbank.etrade.api.pay.service.IPayPipelineService;

@Service(PayPipeline.JSAPIPAYHANDLER)//该名称不可改！！！！
public class WXJSAPIPayHandler implements IPayHandler {

	@Autowired
	private IPayPipelineService payPipelineService;
	
	private static PayPipeline pipeline = new PayPipeline();
	
	@Override
	public Map<String, String> prepay(List<OrderPayment> payments) throws Exception {
		BigDecimal money = payments.stream().map(x->x.getMoney()).reduce(BigDecimal.ZERO, (a, b)->a.add(b));
		String moneyfen = money.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).toString();
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("appid", "wx104070783e1981df");//公众账号ID
		requestParams.put("mch_id", pipeline.getPartner());//商户号
		requestParams.put("nonce_str", new String(new RndBuilder().length(16).hasletter(true).next()));//随机字符串，不长于32位
		requestParams.put("body", payments.get(0).getMname());//商品或支付单简要描述
		requestParams.put("out_trade_no", String.valueOf(payments.get(0).getExtransno()));//商户系统内部的订单号
		requestParams.put("fee_type", "CNY");//默认人民币：CNY
		requestParams.put("total_fee", moneyfen);//单位分
		requestParams.put("spbill_create_ip", "124.128.245.162");//APP和网页支付提交用户端ip
		requestParams.put("notify_url", pipeline.getNoticeUrl());//接收微信支付异步通知回调地址
		requestParams.put("trade_type", "JSAPI");//交易类型
		requestParams.put("openid", UniqueSequence.next()+"");//trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识
		
		String signkey = pipeline.getSignkey();
		requestParams.put("sign", MD5.digest(compositeWXPayKeyValuePaires(requestParams, signkey)).toUpperCase());
		
		HttpPost post = new HttpPost(pipeline.getPayUrl());
		StringBuilder xml = creatPostEntity(requestParams);
		post.setEntity(new StringEntity(xml.toString(), "utf-8"));
		
		HttpClient client = HttpClients.createDefault();
		HttpResponse response = client.execute(post);
		HttpEntity entity = response.getEntity();
		if(Objects.isNull(entity)){
			requestParams.clear();
			requestParams.put("error", "支付信息有误，请稍后重试！");
			return requestParams;
		}
		try(InputStream is = entity.getContent()){
			JAXBContext jaxbcontext = JAXBContext.newInstance(WXPrePayResult.class);
			WXPrePayResult wxresult = (WXPrePayResult) jaxbcontext.createUnmarshaller().unmarshal(is);
			if(StringUtils.hasText(wxresult.getReturn_code()) 
					&& StringUtils.hasText(wxresult.getResult_code())
					&& wxresult.getResult_code().equals("SUCCESS")
					&& wxresult.getReturn_code().equals("SUCCESS")){
				Map<String, String> mapresult = BeanUtils.describe(wxresult);
				String providedSign = wxresult.getSign();
				String calculatedSign = MD5.digest(compositeWXPayKeyValuePaires(mapresult, signkey)).toUpperCase();
				
				if(StringUtils.hasText(providedSign)
						&& StringUtils.hasText(calculatedSign)
						&& providedSign.equals(calculatedSign)){
					String prepay_id = wxresult.getPrepay_id();
					String app_id = wxresult.getAppid();
					Map<String, String> r = new HashMap<String, String>();
					r.put("appId", app_id);
					r.put("timeStamp", new Date().getTime()+"");
					r.put("nonceStr", KeyGen.uuid());
					r.put("package", "prepay_id="+prepay_id);
					r.put("signType", "MD5");
					r.put("paySign", MD5.digest(compositeWXPayKeyValuePaires(r, signkey)).toUpperCase());
					return r;
				}
			}
			Map<String, String> result = new HashMap<String, String>();
			result.put("error", "支付信息有误，请稍后重试！");
			return result;
		}
	}

	private StringBuilder creatPostEntity(Map<String, String> result) {
		StringBuilder xml = new StringBuilder();
        xml.append("<xml>\n");
		result.entrySet().stream().sorted((v1, v2)-> v1.getKey().compareTo(v2.getKey())).forEach(entry -> {
			if ("body".equals(entry.getKey()) || "sign".equals(entry.getKey())) {
                xml.append("<" + entry.getKey() + "><![CDATA[").append(entry.getValue()).append("]]></" + entry.getKey() + ">\n");  
            } else {
                xml.append("<" + entry.getKey() + ">").append(entry.getValue()).append("</" + entry.getKey() + ">\n");  
            }
		});
        xml.append("</xml>");
		return xml;
	}
	
	private String compositeWXPayKeyValuePaires(Map<String, String> result, String key) {
		StringBuilder builder = new StringBuilder();
		result.entrySet().stream()
			.filter(x-> !x.getKey().equals("sign") && !x.getKey().equals("class"))
			.sorted((x, y)->x.getKey().compareTo(y.getKey())).forEach((x)->{
				builder.append(x.getKey()).append("=").append(x.getValue()).append("&");
		});
		builder.append("key=").append(key);
		return builder.toString();
	}

	@PostConstruct
	public void postprocessor(){
		pipeline = payPipelineService.single(PayPipeline.WXPAY);
	}

	@Override
	public Map<String, String> prefund(OrderPayment payment) throws Exception {
		KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File("E:/Documents/支付中心/微信支付/cert11/apiclient_cert.p12"));
        try {
            keyStore.load(instream, "1310348201".toCharArray());
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, "1310348201".toCharArray())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                new DefaultHostnameVerifier());
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {
        	Map<String, String> requestParams = new HashMap<String, String>();
    		requestParams.put("appid", "wx545cb7b5501b1d9c");//公众账号ID
    		requestParams.put("mch_id", "1310348201");//商户号
    		requestParams.put("nonce_str", new String(new RndBuilder().length(16).hasletter(true).next()));//随机字符串，不长于32位
    		requestParams.put("out_trade_no", "190882525474411873");//商户系统内部的订单号
    		requestParams.put("out_refund_no", UniqueSequence.next()+"");
    		requestParams.put("total_fee", ""+79900);//单位分
    		requestParams.put("refund_fee", ""+1);//APP和网页支付提交用户端ip
    		requestParams.put("op_user_id", "1310348201");//交易类型
    		
    		String signkey = "";
    		requestParams.put("sign", MD5.digest(compositeWXPayKeyValuePaires(requestParams, signkey)).toUpperCase());
    		
            HttpPost httppost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");
            StringBuilder xml = creatPostEntity(requestParams);
            httppost.setEntity(new StringEntity(xml.toString(), "utf-8"));
            System.out.println("executing request" + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();

                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    System.out.println("Response content length: " + entity.getContentLength());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
                    String text;
                    while ((text = bufferedReader.readLine()) != null) {
                        System.out.println(text);
                    }
                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
		return null;
	}
}
